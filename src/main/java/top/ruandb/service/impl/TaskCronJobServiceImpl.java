package top.ruandb.service.impl;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.pentaho.di.core.exception.KettleException;
import org.quartz.JobDataMap;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import top.ruandb.Job.BaseJob;
import top.ruandb.config.TaskSchedulerFactory;
import top.ruandb.entity.TaskCronJob;
import top.ruandb.exception.ParamException;
import top.ruandb.init.TaskInit;
import top.ruandb.repository.TaskCronJobRepository;
import top.ruandb.service.JobMonitorService;
import top.ruandb.service.TaskCronJobService;
import top.ruandb.utils.BeanValidator;
import top.ruandb.utils.StringUtils;
import top.ruandb.utils.TaskUtils;


/**
 * 基于Cron调度的服务类接口实现类
 * 
 * @author rdb
 *
 */
@Service("taskCronJobService")
public class TaskCronJobServiceImpl implements TaskCronJobService {

	@SuppressWarnings("unused")
	private  final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Resource
	private TaskCronJobRepository taskCronJobRepository;
	
	@Autowired
	private JobMonitorService jobMonitorService;
	
	@Autowired 
	@Qualifier("Scheduler")
    private Scheduler scheduler;
	
	@Autowired 
	private TaskInit taskInit;

	@Resource
	private TaskSchedulerFactory taskSchedulerFactory;
	
	/**
	 * 新增或更新 job,同时新增或更新新调度器中的定时任务
	 */
	public void addJob(TaskCronJob taskCronJob) throws ClassNotFoundException, SchedulerException {
		
		if(taskCronJob == null ) {
			throw new ParamException("不能新建空Job");
		}
		//检验属性
		BeanValidator.check(taskCronJob);
		
		if(!TaskUtils.isValidExpression(taskCronJob.getCron())) {
			throw new ParamException("调度表达式无效");
		}
		//更新or新增
		if(taskCronJob.isNotNullId()) {
			//获取原配置
			TaskCronJob oldTaskCronJob = this.findOne(taskCronJob.getId());
			TaskUtils.updateJobAndCronTrigger(taskCronJob, oldTaskCronJob, scheduler);
			taskCronJobRepository.save(taskCronJob);
		}else {
			//先保存到库中，获取id
			taskCronJob = taskCronJobRepository.save(taskCronJob);
			//启用or不启用
			if(taskCronJob.isEnable()) {
			TaskUtils.newJobAndCronTrigger(taskCronJob, scheduler);
			}
		}
		
		jobMonitorService.initPart(taskCronJob);
	}
	

	
	/**
	 * 查找单独任务
	 */
	@Override
	public TaskCronJob findOne(Long id) {
		return taskCronJobRepository.findById(id).get();
	}

	/**
	 * 查找所有任务
	 */
	@Override
	public List<TaskCronJob> findAll() {
		return taskCronJobRepository.findAll();
	}

	/**
	 * 根据条件分页查找
	 * 
	 * @param taskCronJob
	 * @param page
	 * @param pageSize
	 * @param direction
	 * @param properties
	 * @return
	 */

	public List<TaskCronJob> findAll(TaskCronJob taskCronJob, Integer page, Integer pageSize, Direction direction,
			String... properties) {
		
		Pageable pageable = PageRequest.of(page - 1, pageSize, direction, properties);
		Page<TaskCronJob> pageTaskCronJob = taskCronJobRepository.findAll(new Specification<TaskCronJob>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Predicate toPredicate(Root<TaskCronJob> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				Predicate predicate = criteriaBuilder.conjunction();
				if (taskCronJob != null) {
					if (StringUtils.isNotEmpty(taskCronJob.getJobName())) {
						predicate.getExpressions()
								.add(criteriaBuilder.like(root.get("jobName"), "%" + taskCronJob.getJobName() + "%"));
					}
				}
				return predicate;
			}
		}, pageable);

		return pageTaskCronJob.getContent();
	}

	/**
	 * 根据条件获取总记录数
	 * 
	 * @param taskCronJob
	 * @return
	 */
	public Long getCount(TaskCronJob taskCronJob) {
		Long count = taskCronJobRepository.count(new Specification<TaskCronJob>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<TaskCronJob> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				Predicate predicate = criteriaBuilder.conjunction();
				if (taskCronJob != null) {
					if (StringUtils.isNotEmpty(taskCronJob.getJobName())) {
						predicate.getExpressions()
								.add(criteriaBuilder.like(root.get("jobName"), "%" + taskCronJob.getJobName() + "%"));
					}
				}
				return predicate;
			}
		});
		return count;
	}

	@Override
	public void deleteOneJob(Long id) throws SchedulerException {
		TaskCronJob taskCronJob = findOne(id);
		TaskUtils.deleteJobAndCronTrigger(taskCronJob, scheduler);
		taskCronJobRepository.deleteById(id);
		if(taskCronJob.isEnable()) {
			jobMonitorService.deleteOne(id);//修改后同步监控表
		}
	}
	/**
	 * 手动执行
	 * @throws SchedulerException 
	 */
	@Override
	public Boolean runJob(TaskCronJob taskCronJob) throws SchedulerException {
		JobKey jobKey = TaskUtils.getCronJobKey(taskCronJob);
		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put("manualExecution", BaseJob.SCHEDULER_1);
		scheduler.triggerJob(jobKey,jobDataMap);
		return true;
	}
	
	/**
	 * 批量手动执行
	 * @throws SchedulerException 
	 */
	@Override
	public void batchRunJob(String ids) throws SchedulerException  {
		
		List<TaskCronJob> jobList =  getJobList(ids);
		
		//按组排序
		jobList.sort(new Comparator<TaskCronJob>() {
			@Override
			public int compare(TaskCronJob o1, TaskCronJob o2) {
				
				return o1.getJobGroup().compareTo(o2.getJobGroup());
			}
		});
		//遍历循环执行作业
		for(TaskCronJob j : jobList) {
			this.runJob(j);
		}
	}
	
	public static void main(String[] args) {
		System.out.println("B".compareTo("A"));
	}



	/**
	 * 	判断是否为空，为空返回 "1900-01-01 00:00:00",相当于全量抽取
	 */
	@Override
	public String getLastDate(TaskCronJob taskCronJob) {
		TaskCronJob tj = this.findOne(taskCronJob.getId());
		if( tj != null && tj.getLastDate() != null && !tj.getLastDate().equals("") ) {
			return StringUtils.redundant1s(tj.getLastDate());
		}else {
			return "1900-01-01 00:00:00";
		}
	}



	/**
	 * 	更新最新的更新日期
	 */
	@Override
	public void updateLastDate(TaskCronJob taskCronJob,String lastDate) {
		TaskCronJob tj = this.findOne(taskCronJob.getId());
		tj.setLastDate(lastDate);
		taskCronJobRepository.save(tj);
	}



	/**
	 * 	初始化调度配置
	 * @throws SchedulerException 
	 * @throws KettleException 
	 * @throws ClassNotFoundException 
	 */
	@Override
	public void csh() throws ClassNotFoundException, KettleException, SchedulerException {
		taskInit.manualExecutionRun();
	}



	/**
	 * 批量更新cron
	 * @throws SchedulerException 
	 * @throws ClassNotFoundException 
	 */
	@Override
	public void batchEditCron(String ids,String cron ) throws ClassNotFoundException, SchedulerException {
		
		if(!TaskUtils.isValidExpression(cron)) {
			throw new ParamException("调度表达式无效");
		}
		List<TaskCronJob> jobList =  getJobList(ids);
		for(TaskCronJob job : jobList) {
			job.setCron(cron);
			addJob(job);
		}
		
	}
	
	/**
	 * 批量更新param
	 * @throws SchedulerException 
	 * @throws ClassNotFoundException 
	 */
	@Override
	public void batchEditParam(String ids,String param ) throws ClassNotFoundException, SchedulerException {
		List<TaskCronJob> jobList =  getJobList(ids);
		for(TaskCronJob job : jobList) {
			job.setJobParams(param);
			addJob(job);
		}
		
		
	}
	
	private  List<TaskCronJob> getJobList(String ids) {
		String[] idS = ids.split(",");
		TaskCronJob job;
		List<TaskCronJob> jobList =  new ArrayList<TaskCronJob>();
		for(String id : idS) {
			 job = this.findOne(Long.parseLong(id));
			 jobList.add(job);	
		}
		return jobList;
	}



	/**
	 * 批量启停作业
	 */
	@Override
	public void batchST(String ids, String param) throws ClassNotFoundException, SchedulerException {
		List<TaskCronJob> jobList =  getJobList(ids);
		Boolean b = null;
		if(param.equals("Y")) {
			b=true;
		}
		if(param.equals("N")) {
			b=false;
		}
		for(TaskCronJob job : jobList) {
			job.setEnabled(b);
			addJob(job);
		}
		
	}



	/**
	 * 	查找相同组别的依赖作业,剔除自己
	 */
	@Override
	public List<TaskCronJob> findGroupPro(Long id) {
		return taskCronJobRepository.findGroupPro(id);
	}
	
	/**
	 * 	查找相同组别的依赖作业，包括自己
	 */
	@Override
	public List<TaskCronJob> findGroupProAll(String  jobGroup) {
		return taskCronJobRepository.findGroupProAll(jobGroup);
	}



	
}

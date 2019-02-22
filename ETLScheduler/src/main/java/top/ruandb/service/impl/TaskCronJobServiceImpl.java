package top.ruandb.service.impl;
import java.util.List;
import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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
import top.ruandb.config.TaskSchedulerFactory;
import top.ruandb.entity.TaskCronJob;
import top.ruandb.exception.ParamException;
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
		jobMonitorService.deleteOne(id);//修改后同步监控表
	
		

	}
	/**
	 * 手动执行
	 * @throws SchedulerException 
	 */
	@Override
	public Boolean runJob(TaskCronJob taskCronJob) throws SchedulerException {
		JobKey jobKey = TaskUtils.getCronJobKey(taskCronJob);
		scheduler.triggerJob(jobKey);
		return true;
	}

}

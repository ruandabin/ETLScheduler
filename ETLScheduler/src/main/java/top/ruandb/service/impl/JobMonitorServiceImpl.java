package top.ruandb.service.impl;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.ruandb.Job.BaseJob;
import top.ruandb.entity.JobMonitor;
import top.ruandb.entity.TaskCronJob;
import top.ruandb.repository.JobMonitorRepository;
import top.ruandb.service.JobMonitorService;
import top.ruandb.service.TaskCronJobService;
import top.ruandb.utils.StringUtils;
import top.ruandb.utils.TaskUtils;

@Service("jobMonitor")
public class JobMonitorServiceImpl implements JobMonitorService{

	
	@Resource
	private JobMonitorRepository jobMonitorRepository;
	
	@Resource
	private TaskCronJobService  taskCronJobService;
	
	@Autowired 
	@Qualifier("Scheduler")
    private Scheduler scheduler;
	
	/**
	 *根据配置表 初始化监控表
	 * @throws SchedulerException 
	 */
	@SuppressWarnings("unused")
	@Transactional
	public void initMonitor() throws SchedulerException {
		List<TaskCronJob> taskCronJobs = taskCronJobService.findAll();
		List<JobMonitor> jobMonitors = new ArrayList<>();
		
		JobKey jobKey = null ;
		TriggerKey triggerKey = null ;
		for(TaskCronJob job : taskCronJobs) {
			if(!job.isEnable()) {
				continue;
			}
			JobMonitor jobMonitor = new JobMonitor();
			jobMonitor.setId(job.getId());
			jobMonitor.setJobName(job.getJobName());
			jobMonitor.setStatus(BaseJob.JOB_DONE);
			jobMonitor.setErrors(BaseJob.SUCCESS);
			jobKey = TaskUtils.getCronJobKey(job);
			triggerKey = TaskUtils.getCronTriggerKey(job);
			Trigger tigger = scheduler.getTrigger(triggerKey);
			if(tigger != null ) {
				jobMonitor.setPrviousDate(scheduler.getTrigger(triggerKey).getPreviousFireTime());
				jobMonitor.setNextDate(scheduler.getTrigger(triggerKey).getNextFireTime());
			}
			jobMonitors.add(jobMonitor);
		}
		jobMonitorRepository.deleteAll();
		if(!jobMonitors.isEmpty()) {
			jobMonitorRepository.saveAll(jobMonitors);
		}
		
		
	}
	
	/**
	 * 每次更新或修改作业同步监控表
	 */
	public void initPart(TaskCronJob taskCronJob) throws SchedulerException  {
		if(taskCronJob.isEnable()) {
			JobMonitor jobMonitor;
			Date nextDate = null ;
			TriggerKey triggerKey = TaskUtils.getCronTriggerKey(taskCronJob);
			Trigger tigger = scheduler.getTrigger(triggerKey);
			if(tigger != null ) {
				nextDate = scheduler.getTrigger(triggerKey).getNextFireTime();
			}
			if(!jobMonitorRepository.existsById(taskCronJob.getId())) {
			    jobMonitor = new JobMonitor();
				jobMonitor.setId(taskCronJob.getId());
				jobMonitor.setJobName(taskCronJob.getJobName());
				jobMonitor.setStatus(BaseJob.JOB_DONE);
				jobMonitor.setErrors(BaseJob.SUCCESS);
				jobMonitor.setNextDate(nextDate);
			}else {
				jobMonitor = jobMonitorRepository.findById(taskCronJob.getId()).get();
				jobMonitor.setJobName(taskCronJob.getJobName());
				jobMonitor.setNextDate(nextDate);
			}
			jobMonitorRepository.save(jobMonitor);
		}else {
			if(jobMonitorRepository.existsById(taskCronJob.getId())) {
				deleteOne(taskCronJob.getId());
			}
			
		}
	}

	/**
	 * 根据调度情况更新监控表
	 */
	public void updateMonitor(JobMonitor jobMonitor) {

		JobMonitor oldJobMonitor = jobMonitorRepository.findById(jobMonitor.getId()).get();
		if(jobMonitor.getNextDate() == null ) {
			jobMonitor.setNextDate(oldJobMonitor.getNextDate());
		}
		jobMonitorRepository.save(jobMonitor);
	}

	/**
	 * 根据条件分页查找
	 */
	@Override
	public List<JobMonitor> findAll(JobMonitor jobMonitor, Integer page, Integer pageSize, Direction direction,
			String... properties) {
		Pageable pageable = PageRequest.of(page - 1, pageSize, direction, properties);
		Page<JobMonitor> pageJobMonitor = jobMonitorRepository.findAll(new Specification<JobMonitor>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Predicate toPredicate(Root<JobMonitor> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {

				Predicate predicate = criteriaBuilder.conjunction();
				if (jobMonitor != null) {
					if (StringUtils.isNotEmpty(jobMonitor.getJobName())) {
						predicate.getExpressions()
								.add(criteriaBuilder.like(root.get("jobName"), "%" + jobMonitor.getJobName() + "%"));
					}
				}
				return predicate;
			}
		}, pageable);

		return pageJobMonitor.getContent();
	}

	/**
	 * 根据条件统计
	 */
	@Override
	public Long getCount(JobMonitor jobMonitor) {
		Long count = jobMonitorRepository.count(new Specification<JobMonitor>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Predicate toPredicate(Root<JobMonitor> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				Predicate predicate = criteriaBuilder.conjunction();
				if (jobMonitor != null) {
					if (StringUtils.isNotEmpty(jobMonitor.getJobName())) {
						predicate.getExpressions()
								.add(criteriaBuilder.like(root.get("jobName"), "%" + jobMonitor.getJobName() + "%"));
					}
				}
				return predicate;
			}
		} );
		return count;
	}
	
	/**
	 * 删除一条监控
	 */
	@Transactional
	public void deleteOne(Long id) {
		jobMonitorRepository.deleteById(id);
	}
}

package top.ruandb.service;

import java.util.List;

import org.quartz.SchedulerException;
import org.springframework.data.domain.Sort.Direction;

import top.ruandb.entity.TaskCronJob;

/**
 * 基于Cron调度的服务类接口
 * @author rdb
 *
 */
public interface TaskCronJobService {
	
	/**
	 * 新增或更新作业
	 * @param taskCronJob
	 * @throws ClassNotFoundException
	 * @throws SchedulerException
	 */
	public void addJob(TaskCronJob taskCronJob) throws ClassNotFoundException, SchedulerException ;
	
	/**
	 * 删除一个作业
	 * @param id
	 * @throws SchedulerException
	 */
	public void deleteOneJob(Long id) throws SchedulerException;
	
	/**
	 * 查找一个作业
	 * @param id
	 * @return
	 */
	public TaskCronJob findOne(Long id);
	
	/**
	 * 查找所有作业
	 * @return
	 */
	public List<TaskCronJob> findAll();
	
	/**
	 * 根据条件分页查找所有作业
	 * @param taskCronJob
	 * @param page
	 * @param pageSize
	 * @param direction
	 * @param properties
	 * @return
	 */
	public List<TaskCronJob> findAll(TaskCronJob taskCronJob ,Integer page, Integer pageSize, Direction direction, String... properties);
	
	/**
	 * 根据条件统计数量
	 * @param taskCronJob
	 * @return
	 */
	public Long getCount(TaskCronJob taskCronJob);
	
	/**
	 * 手动执行任务
	 * @return
	 * @throws SchedulerException 
	 */
	public Boolean runJob(TaskCronJob taskCronJob) throws SchedulerException ;
}

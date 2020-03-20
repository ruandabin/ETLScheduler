package top.ruandb.service;

import java.util.List;

import org.pentaho.di.core.exception.KettleException;
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
	
	/**
	 * 	批量手动执行
	 * @param ids
	 * @return
	 */
	public void batchRunJob (String ids) throws SchedulerException;
	
	/**
	 * 	获取上次增量抽取的更新时间
	 * @return
	 */
	public String getLastDate(TaskCronJob taskCronJob) ;
	
	/**
	 * 	更新增量抽取更新时间
	 * @param lastDate
	 */
	public void updateLastDate(TaskCronJob taskCronJob,String lastDate);
	
	/**
	 * 	初始化调度配置
	 * @throws SchedulerException 
	 * @throws KettleException 
	 * @throws ClassNotFoundException 
	 */
	public void csh() throws ClassNotFoundException, KettleException, SchedulerException;
	
	/**
	 * 批量更新cron
	 */
	public void batchEditCron(String ids,String cron ) throws ClassNotFoundException, SchedulerException;
	
	/**
	 * 批量更新param
	 */
	public void batchEditParam(String ids,String param ) throws ClassNotFoundException, SchedulerException;
	
	/**
	 * 批量启停作业
	 */
	public void batchST(String ids,String param ) throws ClassNotFoundException, SchedulerException;
	
	
	public List<TaskCronJob> findGroupPro(Long id);
	
	public List<TaskCronJob> findGroupProAll(String  jobGroup);
	

}

package top.ruandb.service;

import java.util.List;
import org.quartz.SchedulerException;
import org.springframework.data.domain.Sort.Direction;
import top.ruandb.entity.JobMonitor;
import top.ruandb.entity.TaskCronJob;

public interface JobMonitorService {
	
	public void initMonitor() throws SchedulerException ;
	public void initPart(TaskCronJob taskCronJob) throws SchedulerException;
	
	public List<JobMonitor> findAll(JobMonitor jobMonitor,Integer page, Integer pageSize, Direction direction, String... properties);
	
	public Long getCount(JobMonitor jobMonitor);
	
	public void deleteOne(Long id);
	
	public void updateMonitor(JobMonitor jobMonitor);
	
	public  List<JobMonitor> findAll() ;
	
	/**
	 * 	初始化主键配置
	 */
	public void initPk();
}

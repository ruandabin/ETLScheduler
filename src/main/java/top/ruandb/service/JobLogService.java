package top.ruandb.service;

import java.util.List;

import org.springframework.data.domain.Sort.Direction;

import top.ruandb.entity.JobLog;

public interface JobLogService {

	/**
	 * 按条件分页列表
	 * @param kettleJobLog
	 * @param page
	 * @param pageSize
	 * @param direction
	 * @param properties
	 * @return
	 */
	public List<JobLog> findAll(JobLog jobLog,Integer page, Integer pageSize, Direction direction, String... properties);
	
	/**
	 * 按条件汇总
	 * @param kettleJobLog
	 * @return
	 */
	public Long getCount(JobLog jobLog);
	
	/**
	 * 新增或更新日志
	 * @param jobLog
	 * @return
	 */
	public JobLog addJobLog(JobLog jobLog);
	
	/**
	 * 查询单个
	 * @param id
	 * @return
	 */
	public JobLog findOne(Long id);
	
	/**
	 * 更新处理情况
	 * @param id
	 * @param idDeal
	 */
	public void updateIsDeal(Long id,String isDeal);
	
	/**
	 * sql分页查找
	 * @param jobLog
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public List<JobLog> findAll(JobLog jobLog, Integer page, Integer pageSize);
	
	
	public JobLog getLastedLog(String jobName);
	
}

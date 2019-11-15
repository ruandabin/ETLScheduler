package top.ruandb.Job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 每天定时更新内存job的状态，保证每天一个批次
 * @author rdb
 *
 */
public class DailyInitJobStatusJob extends BaseJob{

	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("{}----------------->开始运行", "DailyInitJobStatusJob");
		this.dailyInitScheduler();
		logger.info("{}----------------->运行成功", "DailyInitJobStatusJob");
	}

}

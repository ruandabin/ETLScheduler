package top.ruandb.init;

import javax.annotation.Resource;

import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.plugins.PluginFolder;
import org.pentaho.di.core.plugins.StepPluginType;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import top.ruandb.config.KettleConfig;
import top.ruandb.entity.TaskCronJob;
import top.ruandb.service.JobMonitorService;
import top.ruandb.service.TaskCronJobService;
import top.ruandb.utils.StringUtils;
import top.ruandb.utils.TaskUtils;

/**
 ** 根据配置表 初始化quartz 调度器
 * 
 * @author rdb
 *
 */
@Component
public class TaskInit implements CommandLineRunner {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	

	@Autowired 
	@Qualifier("Scheduler")
    private Scheduler scheduler;
	
	@Resource
	private JobMonitorService jobMonitorService;

	@Override
	public void run(String... args) throws KettleException, SchedulerException, ClassNotFoundException {
		this.kettleInit();//kettle初始化
		this.quartzInit();//quartz 任务初始化	
	}

	@Resource
	private TaskCronJobService taskCronJobService;


	public void quartzInit() throws SchedulerException, ClassNotFoundException {
		if (scheduler == null) {
			logger.error("初始化定时任务组件失败，Scheduler is null...");
			return;
		}
		logger.info("重新启动清空scheduler");
		initCronJobs(scheduler);// 初始化表里的任务
		//初始化监控表
		jobMonitorService.initMonitor();
	}
	
	
	/**
	 * 初始化
	 * 
	 * @param scheduler
	 * @throws SchedulerException 
	 * @throws ClassNotFoundException 
	 */
	private void initCronJobs(Scheduler scheduler) throws SchedulerException, ClassNotFoundException {
		scheduler.clear();//清空原任务
		Iterable<TaskCronJob> jobList = taskCronJobService.findAll();
		if (jobList != null) {
			for (TaskCronJob job : jobList) {
				scheduleCronJob(job, scheduler);
			}
		}
	}

	/**
	 * 安排cron任务
	 * 
	 * @param job
	 * @param scheduler
	 * @throws SchedulerException 
	 * @throws ClassNotFoundException 
	 */
	private void scheduleCronJob(TaskCronJob job, Scheduler scheduler) throws ClassNotFoundException, SchedulerException {
		if (job != null && StringUtils.isNotEmpty(job.getJobName()) && StringUtils.isNotEmpty(job.getJobClassName())
				&& StringUtils.isNotEmpty(job.getCron()) && scheduler != null) {
			if (job.getEnabled() == null || !job.getEnabled()) {
				return;
			}
			//JobKey jobKey = TaskUtils.getCronJobKey(job);
			logger.info("初始化job到调度器-------------------------------->jobname:{}", job.getJobName());
			TaskUtils.newJobAndCronTrigger(job, scheduler);
		}
	}


	/**
	 * 初始化kettle
	 * @return
	 * @throws KettleException
	 */
	private void  kettleInit() throws KettleException {
		//初始化
		System.setProperty("KETTLE_HOME", KettleConfig.KETTLE_HOME);
		//kettle插件加载
		StepPluginType.getInstance().getPluginFolders().add(new PluginFolder(KettleConfig.KETTLE_HOME + "\\plugins", false, true));
		KettleEnvironment.init();
	}

}

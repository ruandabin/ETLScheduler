package top.ruandb.init;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.plugins.PluginFolder;
import org.pentaho.di.core.plugins.StepPluginType;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import top.ruandb.Job.BaseJob;
import top.ruandb.Job.DailyInitJobStatusJob;
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
	
	@Resource
	private TaskCronJobService taskCronJobService;
	
	@Value("${dailyInitJobStatusCron}")
	public String dailyInitJobStatusCron;
	
	@Value("${exitFile}")
	private String exitFile;

	
	//初始化
	@Override
	public void run(String... args) throws KettleException, SchedulerException, ClassNotFoundException {
		this.kettleInit();//kettle初始化
		this.quartzInit();//quartz 任务初始化	
		this.dailyInitScheduler();//调度依依赖初始化归为
		this.dailyInitJobStatus();//每天初始化内存中的job状态的定时job
		this.pkinit();//初始化主键配置
		//this.exitSystem();//优雅退出
		
		
	}
	
	public void manualExecutionRun() throws KettleException, ClassNotFoundException, SchedulerException {
		this.kettleInit();//kettle初始化
		this.quartzInit();//quartz 任务初始化	
		this.dailyInitScheduler();//调度依依赖初始化归位
		this.dailyInitJobStatus();//每天初始化内存中的job状态的定时job
		this.pkinit();//初始化主键配置
	}


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
		//System.out.println();
		File file = new File(KettleConfig.KETTLE_HOME);
		if(file.exists()) {
			//初始化
			logger.info("kettle初始化开始-------------------------------------------------------->");
			KettleEnvironment.reset();
			System.setProperty("KETTLE_HOME", KettleConfig.KETTLE_HOME);
			//kettle插件加载
			StepPluginType.getInstance().getPluginFolders().add(new PluginFolder(KettleConfig.KETTLE_HOME + "\\plugins", false, true));
			KettleEnvironment.init();
			logger.info("kettle初始化结束-------------------------------------------------------->");
		}
		
	}
	


	
	/**
	 * 初始化调度情况
	 * 每天调度之前读取，后面依赖调度会根据这个内容
	 */
	private void dailyInitScheduler() {
		BaseJob.jobMap.clear();
		BaseJob.groupMap.clear();
		logger.info(" ----------------------->依赖初始化开始");
		List<String> groupA = new ArrayList<>();
		List<String> groupB = new ArrayList<>();
		List<String> groupC = new ArrayList<>();
		List<String> groupD = new ArrayList<>();
		List<TaskCronJob> taskCronJobs = taskCronJobService.findAll();
		for(TaskCronJob t:taskCronJobs) {
			if(!t.isEnable()) {
				continue;
			}
			BaseJob.jobMap.put(t.getId().toString(), "PENDDING");
			if(t.getJobGroup()!=null && !t.getJobGroup().equals("") && t.getJobGroup().equals(BaseJob.GROUP_1)) {
				groupA.add(t.getId().toString());
			}else if(t.getJobGroup()!=null && !t.getJobGroup().equals("") && t.getJobGroup().equals(BaseJob.GROUP_2)) {
				groupB.add(t.getId().toString());
			}else if(t.getJobGroup()!=null && !t.getJobGroup().equals("") && t.getJobGroup().equals(BaseJob.GROUP_3)){
				groupC.add(t.getId().toString());
			}else if(t.getJobGroup()!=null && !t.getJobGroup().equals("") && t.getJobGroup().equals(BaseJob.GROUP_4)) {
				groupD.add(t.getId().toString());
			}
		}
		BaseJob.groupMap.put(BaseJob.GROUP_1, groupA);
		BaseJob.groupMap.put(BaseJob.GROUP_2, groupB);
		BaseJob.groupMap.put(BaseJob.GROUP_3, groupC);
		BaseJob.groupMap.put(BaseJob.GROUP_4, groupD);
		logger.info("jobMap--->"+BaseJob.jobMap.toString());
		logger.info("groupMap--->"+BaseJob.groupMap.toString());
		logger.info(" ----------------------->依赖初始化成功");
	}

	/**
	 * @throws SchedulerException 
	 * 
	 */
	private void dailyInitJobStatus() throws SchedulerException {
		logger.info(" ----------------------->定时依赖初始化开始");
		JobKey jobKey = new JobKey("DailyInitJobStatusJob");
		TriggerKey triggerKey = new TriggerKey("Trigger_DailyInitJobStatusJob");
		JobDetail 	jobDetail  = JobBuilder.newJob(DailyInitJobStatusJob.class).withIdentity(jobKey).build();
		
		CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).forJob(jobKey)
				.withSchedule(CronScheduleBuilder.cronSchedule(dailyInitJobStatusCron).withMisfireHandlingInstructionDoNothing())
				.build();
		scheduler.scheduleJob(jobDetail, trigger);
		logger.info(" ----------------------->定时依赖初始化结束");
	}
	
	private void pkinit() {
		jobMonitorService.initPk();
	}
	
	private void exitSystem() {
		logger.info("程序启动***********************************");
		while(1==1){
			checkExitProgram();
		}
	}
	//优雅的退出程序
	//根据退出标识文件，检测到文件就退出整个程序
	private void checkExitProgram() {
		File exit = new File(exitFile);
		if(exit.exists()) {
			logger.info("程序停止***********************************");
			System.exit(0);
		}
		
	}
	
}

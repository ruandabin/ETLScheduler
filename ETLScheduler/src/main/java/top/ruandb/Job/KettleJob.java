package top.ruandb.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import top.ruandb.config.KettleConfig;
import top.ruandb.entity.JobLog;
import top.ruandb.entity.JobMonitor;
import top.ruandb.entity.TaskCronJob;
import top.ruandb.service.JobLogService;
import top.ruandb.service.JobMonitorService;
import top.ruandb.utils.StringUtils;

import java.util.Date;
import javax.annotation.Resource;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.logging.KettleLogStore;
import org.pentaho.di.core.logging.LogLevel;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.repository.RepositoryDirectoryInterface;
import org.pentaho.di.repository.filerep.KettleFileRepository;
import org.pentaho.di.repository.filerep.KettleFileRepositoryMeta;

/**
 * 调度kettle job
 * 
 * @author rdb
 *
 */
public class KettleJob implements BaseJob {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private JobLogService jobLogService;
	
	@Resource
	private JobMonitorService jobMonitorService;
	

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDetail detail = context.getJobDetail();
		Trigger trigger = context.getTrigger();
		TaskCronJob taskCronJob = (TaskCronJob) detail.getJobDataMap().get("taskCronJob"); // 获得JobDataMap
		JobMonitor jobMonitor = new JobMonitor(taskCronJob.getId(),taskCronJob.getJobName(), BaseJob.JOB_RUN,BaseJob.RUNNING,new Date(), trigger.getNextFireTime());
		jobMonitorService.updateMonitor(jobMonitor);//更新监控
		
		Boolean runResult = runJob(taskCronJob);//真正执行
		
		jobMonitor.setStatus(BaseJob.JOB_DONE);
		jobMonitor.setNextDate(trigger.getNextFireTime());
		jobMonitor.setPrviousDate(trigger.getPreviousFireTime());
		jobMonitor.setErrors(runResult? BaseJob.SUCCESS:BaseJob.ERROR);//根据runJob结果更新
		jobMonitorService.updateMonitor(jobMonitor);//更新监控
	}
	
	/**
	 * 调度kettle 作业
	 * @param taskCronJob
	 * @return
	 */
	private Boolean runJob(TaskCronJob taskCronJob) {
		String jobDirName = KettleConfig.KETTLE_JOB_DIR + "\\" +taskCronJob.getDir() + "\\" + taskCronJob.getJobName() + ".kjb";
		org.pentaho.di.job.Job kettleJob = null;
		JobLog jobLog = new JobLog(taskCronJob.getJobName(), null, new Date(), null, null);
		JobMeta jobMeta = null;
		try {
			//资源库
			KettleFileRepository kettleFileRepository = new KettleFileRepository();
			KettleFileRepositoryMeta kettleFileRepositoryMeta = new KettleFileRepositoryMeta("ETL", "SK_HDW_Repository", "description", KettleConfig.KETTLE_JOB_DIR);
			kettleFileRepository.init(kettleFileRepositoryMeta);
			kettleFileRepository.connect("SK_HDW_Repository", "");
			RepositoryDirectoryInterface directory = kettleFileRepository.findDirectory(taskCronJob.getDir());
			//资源库方式获取jobMeta
			jobMeta =  kettleFileRepository.loadJob(taskCronJob.getJobName(),directory, null, null);
			//直接获取jobMeta
			//jobMeta = new JobMeta(jobDirName, null);
			//传入参数
			jobMeta.setParameterValue("extract_style", taskCronJob.getExtractStyle()+"");//调度方式：0全量和1增量
			jobMeta.setParameterValue("start_time",StringUtils.getStartTime(taskCronJob.getIncreaseTime()));//增量抽取开始时间
			jobMeta.setParameterValue("end_time", StringUtils.getEndTime(taskCronJob.getIncreaseTime()));//增量抽取结束时间
			// 创建job
			kettleJob = new org.pentaho.di.job.Job(kettleFileRepository, jobMeta);
			
			//设置日志级别
			kettleJob.setLogLevel(LogLevel.BASIC);
			logger.info("{}----------------->开始运行", jobDirName);
			
			// 启动
			kettleJob.start();
			kettleJob.waitUntilFinished();
			
			jobLog.setEnddate(new Date());
			jobLog.setStatus(kettleJob.getStatus());
			jobLog.setErrors(kettleJob.getErrors());
			jobLog.setLogText(getJobLog(kettleJob));
		} catch (KettleException e) {
			logger.error("{}运行异常",e);
			jobLog.setErrors(1);
			jobLog.setLogText("job初始化异常,无法生存Job"+e.getMessage());
			insertLog(jobLog);
			return false;
		}
		insertLog(jobLog);
		if (kettleJob.getErrors() > 0) {
			logger.error("{}----------------->运行失败，请查阅日志", jobDirName);	
			return false;
		} else {
			logger.info("{}----------------->运行成功", jobDirName);
			return true;
		}
		
	}

//	/**
//	 * 调度 kettle 作业
//	 * 
//	 * @param jobName
//	 */
//	private void runJob(String jobName, String jobDir) {
//		String jobDirName = KettleConfig.KETTLE_JOB_DIR + "\\" +jobDir + "\\" + jobName + ".kjb";
//		JobMeta jobMeta = null;
//		org.pentaho.di.job.Job kettleJob = null;
//		JobLog jobLog = new JobLog(jobName, null, '0', null, null, null);
//
//		try {
//			//初始化，放到启动初始化了
//			//kettleInit();
//			
//		/*	操作资源库
//		 * kettleFileRepository.loadRepositoryDirectoryTree();
//			获取Job路径
//			RepositoryDirectoryInterface directory = kettleFileRepository.findDirectory(jobDir);
//			这样获得的jobMeta无法调用资源库中的包含tr的jb
//			jobMeta =  kettleFileRepository.loadJob(jobName,directory, null, null);*/	
//			
//			jobMeta = new JobMeta(jobDirName, null);
//			// 创建job
//			kettleJob = new org.pentaho.di.job.Job(null, jobMeta);
//			//设置日志级别
//			kettleJob.setLogLevel(LogLevel.BASIC);
//			logger.info("{}----------------->开始运行", jobDirName);
//			//JobLog jobLog = new JobLog(jobName, kettleJob.getStatus(), kettleJob.getErrors(), new Timestamp(new Date().getTime()), null, null);
//			jobLog.setStartdate(new Timestamp(new Date().getTime()));
//			jobLog.setStatus(kettleJob.getStatus());
//			jobLog.setErrors(kettleJob.getErrors());
//			jobLog = insertLog(jobLog);
//			// 启动
//			kettleJob.start();
//			jobLog.setStatus(kettleJob.getStatus());
//			insertLog(jobLog);
//			kettleJob.waitUntilFinished();
//			//kettleTrans.waitUntilFinished();
//			jobLog.setEnddate(new Timestamp(new Date().getTime()));
//			jobLog.setErrors(kettleJob.getErrors());
//			jobLog.setStatus(kettleJob.getStatus());
//			if (kettleJob.getErrors() > 0) {
//				logger.error("{}----------------->运行失败，请查阅日志", jobDirName);	
//			} else {
//				logger.info("{}----------------->运行成功", jobDirName);
//			}
//			jobLog.setLogText(getJobLog(kettleJob));
//			insertLog(jobLog);
//			kettleJob.stop();
//		} catch (KettleException e) {
//			logger.error("{}运行异常",e);
//			if(kettleJob == null) {
//				jobLog.setErrors(1);
//				jobLog.setLogText("job初始化异常,无法生存Job"+e.getMessage());
//				insertLog(jobLog);
//			}else {
//				jobLog.setStatus(kettleJob.getStatus());
//				jobLog.setErrors(kettleJob.getErrors());
//				jobLog.setEnddate(new Timestamp(new Date().getTime()));
//				jobLog.setLogText(e.getMessage());
//				insertLog(jobLog);
//			}
//			
//		}
//
//	}
//	
	/**
	 * 获取调度日志
	 * @return
	 */
	private String getJobLog(org.pentaho.di.job.Job job ) {
		int lastLineNr = KettleLogStore.getLastBufferLineNr();
		int startLineNr = 0;
		String msg = KettleLogStore.getAppender().getBuffer(
				job.getLogChannel().getLogChannelId(), false, 
                startLineNr , lastLineNr ).toString();
		KettleLogStore.getAppender().clear();
		return msg;
	}
	
	/**
	 * 插入日志
	 * @param jobLog
	 */
	private JobLog insertLog(JobLog jobLog) {
		return jobLogService.addJobLog(jobLog);
	}
	
	/**
	 * 初始化，带资源库初始化
	 * @return
	 * @throws KettleException
	 */
//	private KettleFileRepository  kettleInit() throws KettleException {
//		//初始化
//		StepPluginType.getInstance().getPluginFolders().add(new PluginFolder("D:\\Soft\\Kettle\\data-integration\\plugins", false, true));
//		KettleEnvironment.init();
//		//初始化连接资源库
//		KettleFileRepository kettleFileRepository = new KettleFileRepository();
//		KettleFileRepositoryMeta kettleFileRepositoryMeta = new KettleFileRepositoryMeta("ETL", "SKRepository", "description", KettleConfig.KETTLE_JOB_DIR);
//		kettleFileRepository.init(kettleFileRepositoryMeta);		
//		kettleFileRepository.connect("SKRepository", "");
//		return  kettleFileRepository ;
//	}
	

}

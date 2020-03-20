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
import top.ruandb.service.TaskCronJobService;
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
public class KettleJob extends BaseJob {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private JobLogService jobLogService;
	
	@Resource
	private JobMonitorService jobMonitorService;
	
	@Resource
	private TaskCronJobService  taskCronJobService;
	

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		
		JobDetail detail = context.getJobDetail();
		Trigger trigger = context.getTrigger();
		TaskCronJob taskCronJob = (TaskCronJob) detail.getJobDataMap().get("taskCronJob"); // 获得JobDataMap
		JobMonitor jobMonitor = new JobMonitor(taskCronJob.getId(),taskCronJob.getNickName(),taskCronJob.getJobName(), BaseJob.JOB_PEN,BaseJob.RUNNING,new Date(), trigger.getNextFireTime());
		//检测是否是临时调度，临时调度不检查依赖项
		if(trigger.getJobDataMap().get("manualExecution") == null) {
			if((taskCronJob.getProJob()!=null && !taskCronJob.getProJob().equals(""))||(taskCronJob.getJobGroup()!=null && !taskCronJob.getJobGroup().equals(""))) {
				jobMonitorService.updateMonitor(jobMonitor);//更新监控
				Boolean b = proExecute(taskCronJob);//检查依赖项，并等待依赖项
				if(!b) {
					jobMonitor.setStatus(BaseJob.JOB_DONE);
					jobMonitor.setErrors(BaseJob.ERROR);
					try {
						jobMonitorService.updateMonitor(jobMonitor);//更新监控
						JobLog jobLog = new JobLog(taskCronJob.getNickName(), taskCronJob.getJobName(),
								"Finished(with errors)", new Date(), new Date(), "等待依赖程序超时");
						insertLog(jobLog);
					} finally {
						afterExecute(taskCronJob);//报备自己的状态
					}
					return ;
				}
			}
			
		}
		try {
			jobMonitor.setPrviousDate(new Date());
			jobMonitor.setStatus(BaseJob.JOB_RUN);
			jobMonitorService.updateMonitor(jobMonitor);//更新监控
			Boolean runResult = runJob(taskCronJob);//真正执行
			jobMonitor.setStatus(BaseJob.JOB_DONE);
			jobMonitor.setNextDate(trigger.getNextFireTime());
			//jobMonitor.setPrviousDate(trigger.getPreviousFireTime());
			jobMonitor.setErrors(runResult ? BaseJob.SUCCESS : BaseJob.ERROR);//根据runJob结果更新
			jobMonitorService.updateMonitor(jobMonitor);//更新监控
		} finally {
			//检测是否是临时调度，临时调度不检查依赖项
			if(trigger.getJobDataMap().get("manualExecution") == null) {
				afterExecute(taskCronJob);//报备自己的状态
			}
		}
		
	}
	
	/**
	 * 调度kettle 作业
	 * @param taskCronJob
	 * @return
	 */
	private Boolean runJob(TaskCronJob taskCronJob) {
		String jobDirName = KettleConfig.KETTLE_JOB_DIR + "\\" +taskCronJob.getDir() + "\\" + taskCronJob.getJobName() + ".kjb";
		String startTime = taskCronJobService.getLastDate(taskCronJob);//获取上次增量抽取的时间
		String endTime = StringUtils.getNow();//设置增量抽取的结束时间（当前时间）
		org.pentaho.di.job.Job kettleJob = null;
		JobLog jobLog = new JobLog(taskCronJob.getNickName(),taskCronJob.getJobName(), null, new Date(), null, null);
		JobMeta jobMeta = null;
		String logTest=null;
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
			if (taskCronJob.getJobParams() != null && !taskCronJob.getJobParams().equals("")) {
				logger.info("调度方式(全量/增量):{}", taskCronJob.getJobParams()+"");
				jobMeta.setParameterValue("extract_style", taskCronJob.getJobParams()+"");//调度方式：0全量和1增量
			} 
			
			//jobMeta.setParameterValue("start_time",startTime);//增量抽取开始时间
			//jobMeta.setParameterValue("end_time", endTime);//增量抽取结束时间
			// 创建job
			kettleJob = new org.pentaho.di.job.Job(kettleFileRepository, jobMeta);
			
			//设置日志级别
			kettleJob.setLogLevel(LogLevel.MINIMAL);
			logger.info("{}----------------->开始运行", jobDirName);
			
			// 启动
			kettleJob.start();
			kettleJob.waitUntilFinished();
			
			jobLog.setEnddate(new Date());
			jobLog.setStatus(kettleJob.getStatus());
			jobLog.setErrors(kettleJob.getErrors());
			logTest = getJobLog(kettleJob);
			jobLog.setLogText(logTest);
		} catch (KettleException e) {
			logger.error("运行异常",e);
			jobLog.setErrors(1);
			jobLog.setStatus("Finished(with errors)");
			jobLog.setLogText("job初始化异常,无法生存Job"+e.getMessage());
			insertLog(jobLog);
			return false;
		}
		if (isError(kettleJob,logTest)) {
			logger.error("{}----------------->运行失败，请查阅日志", jobDirName);	
			jobLog.setStatus("Finished(with errors)");
			insertLog(jobLog);
			return false;
		} else {
			logger.info("{}----------------->运行成功", jobDirName);
			taskCronJobService.updateLastDate(taskCronJob,endTime);//记录增量抽取结束时间
			insertLog(jobLog);
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
	private static synchronized  String getJobLog(org.pentaho.di.job.Job job ) {
		int lastLineNr = KettleLogStore.getLastBufferLineNr();
		int startLineNr = 0;
		String msg = KettleLogStore.getAppender().getBuffer(
				job.getLogChannel().getLogChannelId(), false, 
                startLineNr , lastLineNr ).toString();
		//KettleLogStore.getAppender().clear();
		KettleLogStore.getAppender().close();
		return msg;
	}
	
	private boolean isError(org.pentaho.di.job.Job job,String log) {
		if(job.getErrors() > 0) {
			return true;
		}else if(log.contains("ERROR")) {
			return true;
		}else {
			return false;
		}
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

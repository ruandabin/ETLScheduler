package top.ruandb.Job;
import java.util.Date;
import javax.annotation.Resource;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import top.ruandb.config.ProcedureConfig;
import top.ruandb.entity.JobLog;
import top.ruandb.entity.JobMonitor;
import top.ruandb.entity.OdrProLog;
import top.ruandb.entity.TaskCronJob;
import top.ruandb.repository.ProcedureJobRepository;
import top.ruandb.service.JobLogService;
import top.ruandb.service.JobMonitorService;
import top.ruandb.service.OdrProLogService;
import top.ruandb.utils.TaskUtils;

public class SqlServerProcedureJob extends BaseJob{

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private ProcedureJobRepository	procedureJobRepository;
	
	@Resource
	private JobMonitorService jobMonitorService;
	
	@Resource
	private OdrProLogService odrProLogService;
	
	@Value("${sqlserverProcedurePro}")
	public String sqlserverProcedurePro;
	
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDetail detail = context.getJobDetail();
		Trigger trigger = context.getTrigger();
		TaskCronJob taskCronJob = (TaskCronJob) detail.getJobDataMap().get("taskCronJob"); // 获得JobDataMap
		JobMonitor jobMonitor = new JobMonitor(taskCronJob.getId(),taskCronJob.getNickName(), taskCronJob.getJobName(), BaseJob.JOB_PEN,
				BaseJob.RUNNING, new Date(), trigger.getNextFireTime());
		// 检测是否是临时调度,临时调度不检查依赖项
		if (trigger.getJobDataMap().get("manualExecution") == null) {
			if ((taskCronJob.getProJob() != null && !taskCronJob.getProJob().equals(""))
					|| (taskCronJob.getJobGroup() != null && !taskCronJob.getJobGroup().equals(""))) {
				jobMonitorService.updateMonitor(jobMonitor);// 更新监控
				Boolean b = proExecute(taskCronJob);// 检查依赖项，并等待依赖项
				if (!b) {
					jobMonitor.setStatus(BaseJob.JOB_DONE);
					jobMonitor.setErrors(BaseJob.ERROR);
					jobMonitorService.updateMonitor(jobMonitor);// 更新监控
					JobLog jobLog = new JobLog(taskCronJob.getNickName(),taskCronJob.getJobName(), "Finished(with errors)", new Date(),
							new Date(), "等待依赖程序超时");
					insertLog(jobLog);
					afterExecute(taskCronJob);// 报备自己的状态
					return;
				}
			}

		}
		jobMonitor.setPrviousDate(new Date());
		jobMonitor.setStatus(BaseJob.JOB_RUN);
		jobMonitorService.updateMonitor(jobMonitor);// 更新监控
		Boolean runResult = runJob(taskCronJob);// 真正执行

		jobMonitor.setStatus(BaseJob.JOB_DONE);
		jobMonitor.setNextDate(trigger.getNextFireTime());
		// jobMonitor.setPrviousDate(trigger.getPreviousFireTime());
		jobMonitor.setErrors(runResult ? BaseJob.SUCCESS : BaseJob.ERROR);// 根据runJob结果更新
		jobMonitorService.updateMonitor(jobMonitor);// 更新监控

		// 检测是否是临时调度,临时调度不检查依赖项
		if (trigger.getJobDataMap().get("manualExecution") == null) {
			afterExecute(taskCronJob);// 报备自己的状态
		}
		
	}

	/**
	 * 调度存储过程 作业
	 * @param taskCronJob
	 * @return
	 */
	private Boolean runJob(TaskCronJob taskCronJob) {
		Boolean result = true;
		JobLog jobLog = new JobLog(taskCronJob.getNickName(),taskCronJob.getJobName(), null, new Date(), null, null);
		StringBuilder stringBuilder=new StringBuilder("******程序作业日志******"+ "\n");
		String[] jobNames = taskCronJob.getJobName().split(",");
		logger.info("{}---------------------------------------->开始运行", taskCronJob.getJobName());
		for(int i=0;i<jobNames.length;i++) {
			Boolean rt = true;
			String rmsg = null;
			try {
				logger.info("{}----------------->开始运行", jobNames[i]);
				runSingelJob(jobNames[i], taskCronJob);
				rmsg = jobNames[i]+":运行成功"+ "\n";
			} catch (Exception e) {
				rt = false ;
				rmsg = jobNames[i]+" 存储过程运行异常:"+"\n"+e.getMessage();
				logger.error("{}----------------->运行失败，请查阅日志", jobNames[i]);
				stringBuilder.append(rmsg);
				result = result & rt;
				continue;
			}
			OdrProLog odrProLog = odrProLogService.getLastedSqlserverOdrProLog(jobNames[i]);
			
			if(odrProLog==null || (odrProLog != null && odrProLog.getProStatus() != null &&  !odrProLog.getProStatus().equals("0") )) {
				if(odrProLog == null) {
					rmsg = jobNames[i]+" 存储过程运行异常:"+"存储过程未打印日志，默认异常"+ "\n";
				}else {
					rmsg = jobNames[i]+" 存储过程运行异常:"+odrProLog.getErrorMessage()+ "\n";
				}
				rt = false ;
				logger.error("{}----------------->运行失败，请查阅日志", jobNames[i]);
			}
			logger.info("{}----------------->运行成功", jobNames[i]);
			stringBuilder.append(rmsg);
			result = result & rt;
		}
		logger.info("{}--------------------------------------->运行结束", taskCronJob.getJobName());
		if(result) {
			jobLog.setEnddate(new Date());
			jobLog.setStatus("Finished");
			jobLog.setErrors(0);
			jobLog.setLogText(stringBuilder.toString());
		}else {
			jobLog.setEnddate(new Date());
			jobLog.setStatus("Finished(with errors)");
			jobLog.setEnddate(new Date());
			jobLog.setErrors(1);
			jobLog.setLogText(stringBuilder.toString());
		}
		insertLog(jobLog);
		return result;
	}

	private void runSingelJob(String name, TaskCronJob taskCronJob) throws Exception {
	//	String sql  = "exec "+sqlserverProcedurePro+name+" "+taskCronJob.getJobParams();
		String sql = "";
		if (taskCronJob.getJobParams() != null && !taskCronJob.getJobParams().equals("")) {
			//sql =  "exec "+sqlserverProcedurePro + name+" "+taskCronJob.getJobParams();
			sql = "{call " + sqlserverProcedurePro + name + "(?)}";
		} else {
			//sql = "exec "+ sqlserverProcedurePro + name ;
			sql = "{call " + sqlserverProcedurePro + name + "()}";
		}
		logger.info(sql);
	    procedureJobRepository.runSqlserverPro(sql,taskCronJob.getJobParams());
		

	}
}

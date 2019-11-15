package top.ruandb.utils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import top.ruandb.Job.BaseJob;
import top.ruandb.entity.TaskCronJob;
import top.ruandb.exception.ParamException;

/**
 * 调度任务管理模块工具类
 * 
 * @author rdb
 *
 */
public class TaskUtils {

	private final static Logger logger = LoggerFactory.getLogger(TaskUtils.class);
	
	
	// 返回Jobkey
	public static JobKey getCronJobKey(TaskCronJob cronJob) {
		return new JobKey(cronJob.getId().toString(),// + "_" + cronJob.getJobName(),
				cronJob.getJobClassName().substring(cronJob.getJobClassName().indexOf(".") + 1));
	}

	// 返回TriggerKey
	public static TriggerKey getCronTriggerKey(TaskCronJob cronJob) {
		return new TriggerKey("trigger_" + cronJob.getId().toString(),// + "_" + cronJob.getJobName(),
				cronJob.getJobClassName().substring(cronJob.getJobClassName().lastIndexOf(".") + 1));
	}

	// 判断两个TriggerKey是否相等
	public static boolean isTriggerKeyEqual(TriggerKey tk1, TriggerKey tk2) {
		return tk1.getName().equals(tk2.getName()) && ((tk1.getGroup() == null && tk2.getGroup() == null)
				|| (tk1.getGroup() != null && tk1.getGroup().equals(tk2.getGroup())));
	}

	// 判断Cron表达式是否有效
	public static boolean isValidExpression(String cronExpression) {
		if (StringUtils.isEmpty(cronExpression)) {
			return false;
		}
		return CronExpression.isValidExpression(cronExpression);
	}

	/**
	 * 新建job和trigger到scheduler(基于cron触发器)
	 * 
	 * @param job
	 * @param scheduler
	 * @param jobKey
	 * @throws ClassNotFoundException
	 * @throws SchedulerException
	 */
	@SuppressWarnings("unchecked")
	public static void newJobAndCronTrigger(TaskCronJob job, Scheduler scheduler)
			throws ClassNotFoundException, SchedulerException {
		TriggerKey triggerKey = TaskUtils.getCronTriggerKey(job);
		JobKey jobKey = TaskUtils.getCronJobKey(job);
		String cronExpr = job.getCron();
		if (!TaskUtils.isValidExpression(cronExpr)) {
			logger.info("{}----------------->无效的表达式", cronExpr);
			new ParamException("cron表达式无效");
		}
		@SuppressWarnings("rawtypes")
		Class jobClass = Class.forName(job.getJobClassName().trim());
		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put("taskCronJob", job);
//		JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobKey).withDescription(job.getJobDescription())
//				.usingJobData("jobDir", KettleConfig.KETTLE_JOB_DIR + job.getDir() + "\\" + job.getJobName()).build();
		
		JobDetail 	jobDetail  = JobBuilder.newJob(jobClass).withIdentity(jobKey).withDescription(job.getJobDescription()).
				usingJobData(jobDataMap).build();
		CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).forJob(jobKey)
				.withSchedule(CronScheduleBuilder.cronSchedule(cronExpr).withMisfireHandlingInstructionDoNothing())
				.build();
		scheduler.scheduleJob(jobDetail, trigger);
	}

	/**
	 * 更新job的trigger(基于cron触发器)
	 * 
	 * @param job
	 * @param scheduler
	 * @param jobKey
	 * @throws SchedulerException
	 * @throws ClassNotFoundException
	 */
	public static void updateJobAndCronTrigger(TaskCronJob newJob, TaskCronJob oldJob, Scheduler scheduler)
			throws SchedulerException, ClassNotFoundException {
		JobKey oldJobKey = TaskUtils.getCronJobKey(oldJob);

		if (!newJob.isEnable()) {
			scheduler.deleteJob(oldJobKey);
		} else {
			if (!newJob.getJobName().equals(oldJob.getJobName()) || !newJob.getJobClassName().equals(oldJob.getJobClassName())
					|| !newJob.getCron().equals(oldJob.getCron()) || !newJob.getDir().equals(oldJob.getDir()) || newJob.isEnable()) {
				
				if(scheduler.checkExists(oldJobKey)) {
					scheduler.deleteJob(oldJobKey);
				}
				TaskUtils.newJobAndCronTrigger(newJob, scheduler);
			}
		}
	}
	
	/**
	 * 删除调度任务
	 * @param job
	 * @param scheduler
	 * @throws SchedulerException
	 */
	public static void deleteJobAndCronTrigger(TaskCronJob job, Scheduler scheduler) throws SchedulerException {
		JobKey jobKey = TaskUtils.getCronJobKey(job);
		scheduler.deleteJob(jobKey);
	}
	
	/**
	 * 判断是否临时手动调度
	 * @return
	 */
	public static boolean isTempRun(String triggerName) {
		Pattern p = Pattern.compile("MT_.*");
		Matcher m = p.matcher(triggerName);
		return m.matches();
	}
	
}

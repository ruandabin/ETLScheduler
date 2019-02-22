package top.ruandb.Job;

import java.util.Date;
import javax.annotation.Resource;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;
import top.ruandb.entity.JobMonitor;
import top.ruandb.entity.TaskCronJob;
import top.ruandb.service.JobMonitorService;

public class TestJob implements BaseJob {

	@Resource
	private JobMonitorService jobMonitorService;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		JobDetail detail = context.getJobDetail();
		Trigger trigger = context.getTrigger();

		TaskCronJob taskCronJob = (TaskCronJob) detail.getJobDataMap().get("taskCronJob"); // 获得JobDataMap

		JobMonitor jobMonitor = new JobMonitor(taskCronJob.getId(),taskCronJob.getJobName(), BaseJob.JOB_RUN,BaseJob.RUNNING,new Date(), trigger.getNextFireTime());
		jobMonitorService.updateMonitor(jobMonitor);//更新监控
		Boolean runResult = runJob(taskCronJob);
		jobMonitor.setStatus(BaseJob.JOB_DONE);
		jobMonitor.setNextDate(trigger.getNextFireTime());
		jobMonitor.setPrviousDate(trigger.getPreviousFireTime());
		jobMonitor.setErrors(runResult? BaseJob.SUCCESS:BaseJob.ERROR);//根据runJob结果更新
		jobMonitorService.updateMonitor(jobMonitor);//更新监控

	}

	private Boolean runJob(TaskCronJob taskCronJob) {
		System.out.println("***********");
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return false;
	}

}

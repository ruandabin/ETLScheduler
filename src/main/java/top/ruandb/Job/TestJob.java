package top.ruandb.Job;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import javax.annotation.Resource;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;
import top.ruandb.entity.JobMonitor;
import top.ruandb.entity.TaskCronJob;
import top.ruandb.service.JobMonitorService;
//import com.microsoft.sqlserver.jdbc.SQLServerDriver;

public class TestJob extends BaseJob {

	@Resource
	private JobMonitorService jobMonitorService;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		JobDetail detail = context.getJobDetail();
		Trigger trigger = context.getTrigger();

		TaskCronJob taskCronJob = (TaskCronJob) detail.getJobDataMap().get("taskCronJob"); // 获得JobDataMap

		JobMonitor jobMonitor = new JobMonitor(taskCronJob.getId(),taskCronJob.getNickName(),taskCronJob.getJobName(), BaseJob.JOB_RUN,BaseJob.RUNNING,new Date(), trigger.getNextFireTime());
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

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		String URL="jdbc:oracle:thin:@172.17.1.233:1521:orcl";
		String USER="ETL_MONITOR";
		String PASSWORD="e1m2r3";
		//SQLServerDriver s
		Class.forName("oracle.jdbc.OracleDriver");
		Connection conn=DriverManager.getConnection(URL, USER, PASSWORD);
		//Statement st=conn.createStatement();
		//st.execute();
		PreparedStatement stmt = conn.prepareCall("call  HOSPITAL_CUBEDB.usp_fat_basyjb(0)");
				//prepareStatement("exec usp_fact_yy_mz_tjrc_fat 0");//HDW_SK.dbo.sp_create_tmp_table HLHT_BLBGD");
		//st.execute("exec  HDW_SK.dbo.sp_create_tmp_table HLHT_BLBGD");
		boolean b = stmt.execute();
		System.out.println(b);
		System.out.println("dddd");
	}


}

package top.ruandb.Job;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.plugins.PluginFolder;
import org.pentaho.di.core.plugins.StepPluginType;
import org.quartz.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import top.ruandb.config.KettleConfig;
import top.ruandb.entity.JobLog;
import top.ruandb.entity.TaskCronJob;
import top.ruandb.service.JobLogService;
import top.ruandb.service.TaskCronJobService;
/**
 * 基础作业类，所有其他作业必须基础该类
 * @author rdb
 *
 */
public abstract class BaseJob implements Job{

	/**
	 * 自定义JOB运行结果的三种状态：成功、失败、正在进行
	 */
	public static final Integer SUCCESS = 0;
	public static final Integer ERROR = 1;
	public static final Integer RUNNING = 2;
	
	/**
	 * 自定义JOB运行的两种状态，结束、运行、等待依赖作业中
	 */
	public static final String JOB_DONE = "Done" ;
	public static final String JOB_RUN = "Running" ;
	public static final String JOB_PEN = "Pendding" ;
	
	/**
	 * 自定义JOB抽取方式 0 全量抽取 1增量抽取
	 */
	public static final Integer EXTRACT_0 = 0;
	public static final Integer EXTRACT_1 = 1;
	
	/**
	 * 自定义JOB手动调用方式  1单独调用一个JOB  2全量调度
	 */
	public static final Integer SCHEDULER_1 = 1;
	public static final Integer SCHEDULER_2 = 2;
	
	/**
	 * 自定义JOB组：A->B->C->D .
	 * 默认B组的job依赖A组调用完成才开始，C,D组以此类推
	 */
	public static final String GROUP_1 = "A";
	public static final String GROUP_2 = "B";
	public static final String GROUP_3 = "C";
	public static final String GROUP_4 = "D";
	public static final String GROUP_5 = "S";//无依赖组
	
	/**
	 * 有依赖得job最长等待时间设置：180*60=18000s=3h
	 */
	public static final int WAIT_TIME = 180;
	
	//单独的容器，初始化依赖情况
	public static ConcurrentHashMap<String,String> jobMap=new ConcurrentHashMap<>();
	public static ConcurrentHashMap<String,List<String>> groupMap=new ConcurrentHashMap<>();

	
	
	@Autowired
	private JobLogService jobLogService;
	
	@Resource
	private TaskCronJobService taskCronJobService;
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * 初始化调度情况
	 * 每天调度之前读取，后面依赖调度会根据这个内容
	 */
	public  void dailyInitScheduler() {
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
		
		//夜晚初始化
		logger.info("kettle初始化开始-------------------------------------------------------->");
		KettleEnvironment.reset();
		System.setProperty("KETTLE_HOME", KettleConfig.KETTLE_HOME);
		//kettle插件加载
		StepPluginType.getInstance().getPluginFolders().add(new PluginFolder(KettleConfig.KETTLE_HOME + "\\plugins", false, true));
		try {
			KettleEnvironment.init();
		} catch (KettleException e) {
			e.printStackTrace();
		}
		logger.info("kettle初始化结束-------------------------------------------------------->");
	}
	
	/**
	 * 插入日志
	 * @param jobLog
	 */
	public JobLog insertLog(JobLog jobLog) {
		return jobLogService.addJobLog(jobLog);
	}
	

	/**
	 * 检查依赖性
	 */
	public Boolean proExecute(TaskCronJob taskCronJob) {
		
		//组别等待
		if(taskCronJob.getJobGroup()!=null && !taskCronJob.getJobGroup().equals("") && !taskCronJob.getJobGroup().equals(BaseJob.GROUP_5)) {
			int i=BaseJob.WAIT_TIME;
			while(i-- > 0) {
				
				if(taskCronJob.getJobGroup().equals(BaseJob.GROUP_1)) {
					//A组不需要等待
					break ;
				}else if(taskCronJob.getJobGroup().equals(BaseJob.GROUP_2)) {
					//检测A组是否全部完成
					logger.info("{}--------->"+"等待A组",taskCronJob.getJobName());
					if(detectGroupStatus(BaseJob.GROUP_1)) {
						break;
					}else {
						try {
							Thread.sleep(60000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}else if(taskCronJob.getJobGroup().equals(BaseJob.GROUP_3)) {
					//检测B组是否全部完成
					logger.info("{}--------->"+"等待B组",taskCronJob.getJobName());
					if(detectGroupStatus(BaseJob.GROUP_2)) {
						break;
					}else {
						try {
							Thread.sleep(60000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}else if(taskCronJob.getJobGroup().equals(BaseJob.GROUP_4)) {
					//检测C组是否全部完成
					logger.info("{}--------->"+"等待C组",taskCronJob.getJobName());
					if(detectGroupStatus(BaseJob.GROUP_3)) {
						break;
					}else {
						try {
							Thread.sleep(60000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				//等待5h后依赖还没完成，默认依赖出作业问题，退出当前调用
				if(i == 0) {
					logger.info("{}--------->"+"已经等待3h,默认组别作业出问题，退出当前作业",taskCronJob.getJobName());
					return false;
				}
			}
		}else {
			logger.info("{}--------->"+"无组别依赖",taskCronJob.getJobName());
		}
		
		//依赖等待
		if(taskCronJob.getProJob()!=null && !taskCronJob.getProJob().equals("")) {
			String[] jobId = taskCronJob.getProJob().split(",");
			int i=BaseJob.WAIT_TIME;
			while(i-- > 0) {
				logger.info("{}--------->"+"等待依赖项："+taskCronJob.getProJob(),taskCronJob.getJobName());
				Boolean resu = true ;
				for(String id : jobId) {
					resu = resu && detectJobStatus(id) ;
				}
				if(resu) {
					break;
				}else {
					try {
						Thread.sleep(60000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				//等待5h后依赖还没完成，默认依赖出作业问题，退出当前调用
				if(i == 0) {
					logger.info("{}--------->"+"已经等待3h,默认依赖作业出问题，退出当前作业",taskCronJob.getJobName());
					return false;
				}
			}
		}else {
			logger.info("{}--------->"+"无依赖项",taskCronJob.getJobName());
		}
		return true;
	}
	
	/**
	 * 更新内存自己的状态
	 */
	public void afterExecute(TaskCronJob taskCronJob) {
		BaseJob.jobMap.put(taskCronJob.getId().toString(), "DONE");
	}
	
	/**
	 * 检测单个job的运行状态	 * @param jobId
	 * @return
	 */
	private boolean  detectJobStatus(String jobId) {
		if(BaseJob.jobMap.get(jobId) != null && BaseJob.jobMap.get(jobId).equals("DONE")) {
			return true ;
		}else {
			return false ;
		}
	}
	
	/**
	 *  检测组的状态
	 * @param groupName 组名
	 * @return
	 */
	private boolean detectGroupStatus(String groupName) {
		List<String> lIds = BaseJob.groupMap.get(groupName);
		Boolean  resu = true ;
		if(lIds != null) {
			for(String id : lIds) {
				resu = resu && detectJobStatus(id) ;
			}
		}
		return resu ;
	}
	
}

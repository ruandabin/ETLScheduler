package top.ruandb.Job;

import org.quartz.Job;

public interface BaseJob  extends Job{
	/**
	 * 自定义JOB运行结果的三种状态：成功、失败、正在进行
	 */
	public static final Integer SUCCESS = 0;
	public static final Integer ERROR = 1;
	public static final Integer RUNNING = 2;
	
	/**
	 * 自定义JOB运行的两种状态，结束、运行
	 */
	public static final String JOB_DONE = "Done" ;
	public static final String JOB_RUN = "Running" ;
	
	/**
	 * 自定义JOB抽取方式 0 全量抽取 1增量抽取
	 */
	public static final Integer EXTRACT_0 = 0;
	public static final Integer EXTRACT_1 = 1;
	
}

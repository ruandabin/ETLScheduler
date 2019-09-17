package top.ruandb.entity;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.validation.constraints.NotBlank;

import top.ruandb.Job.BaseJob;

/**
 * 基于Cron触发器的定时任务实体类
 * 
 * @author rdb
 *
 */
@Entity
@Table(name = "TASK_CRON_JOB")
public class TaskCronJob implements Serializable{

	// Job主键
	/*@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="mseq")
	@SequenceGenerator(name="mseq",sequenceName="TASK_CRON_JOB_SEQ",allocationSize=1)*/
	
	@Id
    @GeneratedValue(strategy = GenerationType.TABLE,generator="cron_job_gen")
	@TableGenerator(name = "cron_job_gen", table="TB_GENERATOR",
					pkColumnName="gen_name", valueColumnName="gen_value",
					pkColumnValue="TASK_CRON_JOB_PK",allocationSize=1)
	private Long id;

	// cron表达式
	@NotBlank(message = "调度表达式不能为空")
	private String cron;

	// Job名称
	@NotBlank(message = "作业名称不能为空")
	@Column(name="job_name",columnDefinition="varchar(max)")
	private String jobName;

	// Job相关的类全名
	@NotBlank(message = "作业类型不能为空")
	@Column(name="job_class_name")
	private String jobClassName;

	// Job描述
	@Column(name="job_description")
	private String jobDescription;

	// 依赖job
	@Column(name="pro_job")
	private String proJob;

	// Job是否启用
	@Column(name="enabled")
	private Boolean enabled = false;
	
	//Job路径
	@NotBlank(message = "作业路径不能为空")
	@Column(name="dir")
	private String dir;
	
	//抽取方式 0-全量 1-增量
	@Column(name="extract_style")
	private int extractStyle;
	
	//job的参数，存储json键值对
	@Column(name="job_params")
	private String jobParams;
	
	//增量抽取方式的时间点
	@Column(name="increase_time")
	private String increaseTime;

	//依赖组
	@Column(name="pro_group")
	private String proGroup;
	
	//所属组组
	@Column(name="job_group")
	private String jobGroup;
	
	//上一次增量抽取的时间记录
	@Column(name="last_date")
	private String lastDate;
	
	//排序标志
	@Column(name="sort_bz")
	private String sortBz;
	
	//作业别名
	@Column(name="nick_name")
	private String nickName;
	
	
	public Long getId() {
		return id;
	}

	public String getCron() {
		return cron;
	}

	public String getJobName() {
		return jobName;
	}

	public String getJobClassName() {
		return jobClassName;
	}

	public String getJobDescription() {
		return jobDescription;
	}

	

	public String getProGroup() {
		return proGroup;
	}

	
	public void setProGroup(String proGroup) {
		this.proGroup = proGroup;
	}

	

	public String getJobGroup() {
		return jobGroup;
	}

	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
		if (jobGroup != null && jobGroup.equals(BaseJob.GROUP_5)) {
			this.sortBz = "1";
		} else if (jobGroup != null && jobGroup.equals(BaseJob.GROUP_1)) {
			this.sortBz = "2";
		} else if (jobGroup != null && jobGroup.equals(BaseJob.GROUP_2)) {
			this.sortBz = "3";
		} else if (jobGroup != null && jobGroup.equals(BaseJob.GROUP_3)) {
			this.sortBz = "4";
		} else if (jobGroup != null && jobGroup.equals(BaseJob.GROUP_4)) {
			this.sortBz = "5";
		}
	}

	public String getProJob() {
		return proJob;
	}

	public void setProJob(String proJob) {
		this.proJob = proJob;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public String getDir() {
		return dir;
	}

	public int getExtractStyle() {
		return extractStyle;
	}

	public String getJobParams() {
		return jobParams;
	}

	public String getIncreaseTime() {
		return increaseTime;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setCron(String cron) {
		this.cron = cron;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public void setJobClassName(String jobClassName) {
		this.jobClassName = jobClassName;
	}

	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}

	

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public void setExtractStyle(int extractStyle) {
		this.extractStyle = extractStyle;
	}

	public void setJobParams(String jobParams) {
		this.jobParams = jobParams;
	}

	public void setIncreaseTime(String increaseTime) {
		this.increaseTime = increaseTime;
	}

	public String getLastDate() {
		return lastDate;
	}

	public void setLastDate(String lastDate) {
		this.lastDate = lastDate;
	}
	

	
//	public String getSortBz() {
//		return sortBz;
//	}
//
//	public void setSortBz(String sortBz) {
//		this.sortBz = sortBz;
//	}


	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	//判断Id是否为空
	public boolean isNotNullId() {
		return id == null ? false : true ;
	}
	
	//是否启用
	public boolean isEnable() {
		if(enabled != null ) {
			return enabled;
		}else {
			return false ;
		}
	}
	


	public TaskCronJob() {
		super();
	}

	@Override
	public String toString() {
		return "TaskCronJob [id=" + id + ", cron=" + cron + ", jobName=" + jobName + ", jobClassName=" + jobClassName
				+ ", jobDescription=" + jobDescription + ", proJob=" + proJob + ", enabled=" + enabled + ", dir=" + dir
				+ ", extractStyle=" + extractStyle + ", jobParams=" + jobParams + ", increaseTime=" + increaseTime
				+ ", proGroup=" + proGroup + ", jobGroup=" + jobGroup + ", lastDate=" + lastDate + "]";
	}

//	@Override
//	public String toString() {
//		return "TaskCronJob [id=" + id + ", cron=" + cron + ", jobName=" + jobName + ", jobClassName=" + jobClassName
//				+ ", jobDescription=" + jobDescription + ", proJob=" + proJob + ", enabled=" + enabled + ", dir=" + dir
//				+ ", extractStyle=" + extractStyle + ", jobParams=" + jobParams + ", increaseTime=" + increaseTime
//				+ ", proGroup=" + proGroup + ", jobGroup=" + jobGroup + "]";
//	}
	

}

package top.ruandb.entity;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

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
	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// cron表达式
	@NotBlank(message = "调度表达式不能为空")
	private String cron;

	// Job名称
	@NotBlank(message = "作业名称不能为空")
	@Column(name="job_name")
	private String jobName;

	// Job相关的类全名
	@NotBlank(message = "作业类型不能为空")
	@Column(name="job_class_name")
	private String jobClassName;

	// Job描述
	@Column(name="job_description")
	private String jobDescription;

	// Job编号
	@Column(name="job_number")
	private String jobNumber;

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

	public String getJobNumber() {
		return jobNumber;
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

	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
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

	@Override
	public String toString() {
		return "TaskCronJob [id=" + id + ", cron=" + cron + ", jobName=" + jobName + ", jobClassName=" + jobClassName
				+ ", jobDescription=" + jobDescription + ", jobNumber=" + jobNumber + ", enabled=" + enabled + ", dir="
				+ dir + ", extractStyle=" + extractStyle + ", jobParams=" + jobParams + ", increaseTime=" + increaseTime
				+ "]";
	}

	
}

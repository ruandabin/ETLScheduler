package top.ruandb.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name="JOB_LOG")
public class JobLog implements Serializable {

	// log主键
	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	//job名称
	@Column(name="jobname")
	private String jobName;
	
	//状态
	@Column(name="status")
	private String status;
	
	//错误
	@Column(name="errors")
	private long errors;
	
	//开始时间
	@Column(name="startdate")
	private Date startdate;
	
	//结束时间
	@Column(name="enddate")
	private Date enddate;
	
	@Lob
	@Column(name="logtext")
	private String logText;

	public Long getId() {
		return id;
	}

	public String getJobName() {
		return jobName;
	}

	public String getStatus() {
		return status;
	}

	public long getErrors() {
		return errors;
	}

	public Date getStartdate() {
		return startdate;
	}

	public Date getEnddate() {
		return enddate;
	}

	public String getLogText() {
		return logText;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setErrors(long errors) {
		this.errors = errors;
	}

	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}

	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}

	public void setLogText(String logText) {
		this.logText = logText;
	}

	

	public JobLog(String jobName, String status, Date startdate, Date enddate, String logText) {
		super();
		this.jobName = jobName;
		this.status = status;
		this.startdate = startdate;
		this.enddate = enddate;
		this.logText = logText;
	}

	public JobLog() {}

}

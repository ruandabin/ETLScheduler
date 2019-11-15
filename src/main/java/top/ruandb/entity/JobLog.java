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

@Entity
@Table(name="JOB_LOG")
public class JobLog implements Serializable {

	/*// log主键
	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="mseq")
	@SequenceGenerator(name="mseq",sequenceName="JOB_LOG_SEQ",allocationSize=1)*/
	
	@Id
    @GeneratedValue(strategy = GenerationType.TABLE,generator="job_log_gen")
	@TableGenerator(name = "job_log_gen", table="TB_GENERATOR",
					pkColumnName="gen_name", valueColumnName="gen_value",
					pkColumnValue="JOB_LOG_PK",allocationSize=1)
	private Long id;
	
	//job名称
	@Column(name="jobname",columnDefinition="varchar(max)")
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
	
	//日志报错是否处理标志
	@Column(name="is_deal")
	private String isDeal;
	
	//作业别名
	@Column(name="nick_name")
	private String nickName;
	
	/*@Lob 
	@Basic(fetch = FetchType.LAZY) 
	@Column(name="logtext",columnDefinition="CLOB", nullable=true)*/
	@Column(name="logtext",columnDefinition="varchar(max)")
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
	
	public String getIsDeal() {
		return isDeal;
	}

	public void setIsDeal(String isDeal) {
		this.isDeal = isDeal;
	}
	

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public JobLog(String nickName,String jobName, String status, Date startdate, Date enddate, String logText) {
		super();
		this.jobName = jobName;
		this.status = status;
		this.startdate = startdate;
		this.enddate = enddate;
		this.logText = logText;
		this.nickName = nickName;
	}
	

	public JobLog() {}

	@Override
	public String toString() {
		return "JobLog [id=" + id + ", jobName=" + jobName + ", status=" + status + ", errors=" + errors
				+ ", startdate=" + startdate + ", enddate=" + enddate + ", isDeal=" + isDeal + ", logText=" + logText
				+ "]";
	}
	
	
	

}

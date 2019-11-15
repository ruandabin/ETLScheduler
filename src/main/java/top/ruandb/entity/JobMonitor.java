package top.ruandb.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="JOB_MONITOR")
public class JobMonitor implements Serializable{

		// 主键
		@Id
		@Column(name="id")
		private Long id;
		
		//job名称
		@Column(name="jobname",columnDefinition="varchar(max)")
		private String jobName;
		
		//状态
		@Column(name="status")
		private String status;
		
		//错误
		@Column(name="errors")
		private Integer errors;
		
		//下一次开始时间
		@Column(name="prviousDate")
		private Date prviousDate;
		
		//下一次开始时间
		@Column(name="nextDate")
		private Date nextDate;
		
		//作业别名
		@Column(name="nick_name")
		private String nickName;
		
		

		public String getNickName() {
			return nickName;
		}


		public void setNickName(String nickName) {
			this.nickName = nickName;
		}


		public Long getId() {
			return id;
		}


		public String getJobName() {
			return jobName;
		}


		public String getStatus() {
			return status;
		}


		public Integer getErrors() {
			return errors;
		}


		public Date getPrviousDate() {
			return prviousDate;
		}


		public Date getNextDate() {
			return nextDate;
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


		public void setErrors(Integer errors) {
			this.errors = errors;
		}


		public void setPrviousDate(Date prviousDate) {
			this.prviousDate = prviousDate;
		}


		public void setNextDate(Date nextDate) {
			this.nextDate = nextDate;
		}


		public JobMonitor(Long id,String jobName, String status, Date nextDate) {
			super();
			this.id = id;
			this.jobName = jobName;
			this.status = status;
			this.nextDate = nextDate;
		}

		
		public JobMonitor(Long id, String jobName, String status, Date prviousDate, Date nextDate) {
			super();
			this.id = id;
			this.jobName = jobName;
			this.status = status;
			//this.errors = errors;
			this.prviousDate = prviousDate;
			this.nextDate = nextDate;
		}
		
		

		public JobMonitor(Long id, String nickName,String jobName, String status, Integer errors, Date prviousDate, Date nextDate) {
			super();
			this.id = id;
			this.jobName = jobName;
			this.nickName = nickName;
			this.status = status;
			this.errors = errors;
			this.prviousDate = prviousDate;
			this.nextDate = nextDate;
		}


		public JobMonitor() {}

		@Override
		public String toString() {
			return "JobMonitor [id=" + id + ", jobName=" + jobName + ", status=" + status + ", errors=" + errors
					+ ", prviousDate=" + prviousDate + ", nextDate=" + nextDate + "]";
		}
		
		
		
}

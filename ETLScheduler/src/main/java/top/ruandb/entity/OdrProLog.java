package top.ruandb.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class OdrProLog {

	
	@Id
	@Column(name="id")
	private Long id;
	
	@Column(name="pro_status")
	private String proStatus;
	
	@Column(name="errormessage")
	private String errorMessage;

	public Long getId() {
		return id;
	}

	public String getProStatus() {
		return proStatus;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setProStatus(String proStatus) {
		this.proStatus = proStatus;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@Override
	public String toString() {
		return "OdrProLog [id=" + id + ", proStatus=" + proStatus + ", errorMessage=" + errorMessage + "]";
	}
	
	
	
	
}

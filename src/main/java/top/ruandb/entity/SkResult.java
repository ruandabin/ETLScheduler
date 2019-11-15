package top.ruandb.entity;

import java.util.Map;
/**
 * 	申康集成平台登陆接口返回json对象
 * @author rdb
 *
 */
public class SkResult {

	private int statuscode;
	private String repMsg;
	private Map repData;
	public int getStatuscode() {
		return statuscode;
	}
	public void setStatuscode(int statuscode) {
		this.statuscode = statuscode;
	}
	public String getRepMsg() {
		return repMsg;
	}
	public void setRepMsg(String repMsg) {
		this.repMsg = repMsg;
	}
	public Map getRepData() {
		return repData;
	}
	public void setRepData(Map repData) {
		this.repData = repData;
	}
	
	
}

package top.ruandb.entity;

import java.util.List;

public class Serie {


    		
    private String name;
	private String type ="line";
	//private String stack = "耗时";
	private String[] data ;
	public String getName() {
		return name;
	}
	
	

	public void setName(String name) {
		this.name = name;
	}
	
	


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String[] getData() {
		return data;
	}

	public void setData(String[] data) {
		this.data = data;
	}

	
	
	
	
			
}

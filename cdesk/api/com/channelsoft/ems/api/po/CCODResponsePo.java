package com.channelsoft.ems.api.po;

public class CCODResponsePo {

	private String result;
	private String desc;
	private String groupId;
	
	public CCODResponsePo(){
		
	}
	
	public CCODResponsePo(String result,String desc){
		super();
		this.result = result;
		this.desc = desc;
	}
	public CCODResponsePo(String result,String desc,String groupId){
		super();
		this.result = result;
		this.desc = desc;
		this.groupId=groupId;
	}
	
	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	
}

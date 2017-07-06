package com.channelsoft.ems.template.po;

public class TemplatePo {

	private String tempId;//模板Id
	private String tempName;//模板名
	private String tempType;//模板类型（工单，用户）
	private String status;//模板状态
	private boolean isDefault;//是否是默认模板
	private boolean editable;//是否可编辑
	private boolean statusChange;//是否可变状态	
	private String sortNum;//排序值
	private String createTime;
	
	private String defineType;
	
	public String getTempId() {
		return tempId;
	}
	public void setTempId(String tempId) {
		this.tempId = tempId;
	}
	public String getTempName() {
		return tempName;
	}
	public void setTempName(String tempName) {
		this.tempName = tempName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public boolean isDefault() {
		return isDefault;
	}
	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}
	public String getSortNum() {
		return sortNum;
	}
	public void setSortNum(String sortNum) {
		this.sortNum = sortNum;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	public String getDefineType() {
		if(isDefault()){
			return "系统内置";
		}else{
			return "自定义";
		}
	}
	public void setDefineType(String defineType) {
		this.defineType = defineType;
	}
	
	public String getTempType() {
		return tempType;
	}
	public void setTempType(String tempType) {
		this.tempType = tempType;
	}
	public boolean isEditable() {
		return editable;
	}
	public void setEditable(boolean editable) {
		this.editable = editable;
	}
	public boolean isStatusChange() {
		return statusChange;
	}
	public void setStatusChange(boolean statusChange) {
		this.statusChange = statusChange;
	}	
	
}

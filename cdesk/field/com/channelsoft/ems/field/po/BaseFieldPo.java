package com.channelsoft.ems.field.po;

import com.channelsoft.ems.field.constants.ComponentTypeEnum;

public class BaseFieldPo {

	
	private String tempId;//模板Id	
	private String key;//字段Id
	private String name;//字段名
	private String tempType;//模板类型
	private String type;
	private String componentType;//组件类型,文本，下拉框等
	
	private boolean isShow;//是否显示
	private boolean editable;//是否可编辑
	private boolean isDefault;//是否是默认字段
	private boolean statusChange;//是否状态可变
	
	private String eventKey;
	private String eventShow;
	private String status;//字段状态
	private String remark;//字段描述
	private String[] candidateValue;//候选值
	private String defaultValue;//默认值
	private String sortNum;//排序	
	private String createTime;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getComponentType() {
		return componentType;
	}
	public void setComponentType(String componentType) {
		this.componentType = componentType;
	}
	public boolean isShow() {
		return isShow;
	}
	public void setShow(boolean isShow) {
		this.isShow = isShow;
	}
	public boolean getIsDefault() {
		return isDefault;
	}
	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}
	public String getEventKey() {
		return eventKey;
	}
	public void setEventKey(String eventKey) {
		this.eventKey = eventKey;
	}
	public String getEventShow() {
		return eventShow;
	}
	public void setEventShow(String eventShow) {
		this.eventShow = eventShow;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String[] getCandidateValue() {
		return candidateValue;
	}
	public void setCandidateValue(String[] candidateValue) {
		this.candidateValue = candidateValue;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public String getSortNum() {
		return sortNum;
	}
	public void setSortNum(String sortNum) {
		this.sortNum = sortNum;
	}
	
	public String getTempId() {
		return tempId;
	}
	public void setTempId(String tempId) {
		this.tempId = tempId;
	}
	public boolean isStatusChange() {
		return statusChange;
	}
	public void setStatusChange(boolean statusChange) {
		this.statusChange = statusChange;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public boolean isEditable() {
		return editable;
	}
	public void setEditable(boolean editable) {
		this.editable = editable;
	}
	public String getTempType() {
		return tempType;
	}
	public void setTempType(String tempType) {
		this.tempType = tempType;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}  
	public String getDefineType() {
		if(getIsDefault()){
			return "系统内置";
		}else{
			return "用户自定义";
		}
	}
	public String getComponentTypeStr() {
		return ComponentTypeEnum.getEnum(getComponentType()).getDesc();
	}
}

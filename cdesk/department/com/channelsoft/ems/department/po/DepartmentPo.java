package com.channelsoft.ems.department.po;

public class DepartmentPo {
	private String id;
	private String name;
	private String entId;
	private String level;
	private String parentId;
	private String sortWeight;
	private String reamrk;
	private String createTime;
	private String updateTime;
	private String status;	
	private int sortWeightInt;
	private int levelInt;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEntId() {
		return entId;
	}
	public void setEntId(String entId) {
		this.entId = entId;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getSortWeight() {
		return sortWeight;
	}
	public void setSortWeight(String sortWeight) {
		this.sortWeight = sortWeight;
	}
	public String getReamrk() {
		return reamrk;
	}
	public void setReamrk(String reamrk) {
		this.reamrk = reamrk;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getSortWeightInt() {
		return sortWeightInt;
	}
	public void setSortWeightInt(int sortWeightInt) {
		this.sortWeightInt = sortWeightInt;
	}
	public int getLevelInt() {
		return levelInt;
	}
	public void setLevelInt(int levelInt) {
		this.levelInt = levelInt;
	}	
}

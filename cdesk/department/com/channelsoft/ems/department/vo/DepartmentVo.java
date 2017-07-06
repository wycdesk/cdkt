package com.channelsoft.ems.department.vo;

import com.channelsoft.cri.vo.BaseTreeVo;

public class DepartmentVo extends BaseTreeVo{		
	private static final long serialVersionUID = -4904723066593078698L;
	private String entId;
	private String level;
	private String sortWeight;
	private String reamrk;
	private String createTime;
	private String updateTime;
	private String status;
	private String parentName;	
	private String dptId;
	
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
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	public String getDptId() {
		return dptId;
	}
	public void setDptId(String dptId) {
		this.dptId = dptId;
	}	
}

package com.channelsoft.ems.group.po;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GroupPo implements Serializable{
	private static final long serialVersionUID = -3561807533989879763L;
	private String groupId;
	private String groupName;
	private String groupType;
	private String groupDesc;
	private String creatorId;
	private String creatorName;
	private String createTime;
	private String updatorId;
	private String updatorName;
	private String updateTime;
	List<AgentPo> members;
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public int getCount() {
		if(members==null) return 0;
		return members.size();
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getGroupType() {
		return groupType;
	}
	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}
	public String getGroupDesc() {
		return groupDesc;
	}
	public void setGroupDesc(String groupDesc) {
		this.groupDesc = groupDesc;
	}
	public String getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}
	public String getCreatorName() {
		return creatorName;
	}
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUpdatorId() {
		return updatorId;
	}
	public void setUpdatorId(String updatorId) {
		this.updatorId = updatorId;
	}
	public String getUpdatorName() {
		return updatorName;
	}
	public void setUpdatorName(String updatorName) {
		this.updatorName = updatorName;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public List<AgentPo> getMembers() {
		return members;
	}
	public void setMembers(List<AgentPo> members) {
		this.members = members;
	}
	
	
}

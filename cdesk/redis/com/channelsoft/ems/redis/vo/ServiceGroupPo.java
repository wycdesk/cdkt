package com.channelsoft.ems.redis.vo;

public class ServiceGroupPo {
	
	private String ServiceGroupId;
	private String ServiceGroupName;
	private String groupId;
	private String groupName;
	
	public String getServiceGroupId() {
		return ServiceGroupId;
	}
	public void setServiceGroupId(String serviceGroupId) {
		ServiceGroupId = serviceGroupId;
	}
	public String getServiceGroupName() {
		return ServiceGroupName;
	}
	public void setServiceGroupName(String serviceGroupName) {
		ServiceGroupName = serviceGroupName;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
}

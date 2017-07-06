package com.channelsoft.ems.api.po;

import java.util.List;

public class GroupSimplePo {
	private String groupId;
	private String groupName;
	List<AgentSimplePo> agentList;
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
	public List<AgentSimplePo> getAgentList() {
		return agentList;
	}
	public void setAgentList(List<AgentSimplePo> agentList) {
		this.agentList = agentList;
	}
	
	
}

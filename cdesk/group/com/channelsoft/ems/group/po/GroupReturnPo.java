package com.channelsoft.ems.group.po;

import java.util.ArrayList;
import java.util.List;

public class GroupReturnPo {
	private String groupId;
	private String groupName;
	private List<AgentReturnPo> members=new ArrayList<AgentReturnPo>();
	private String edit;
	public String getEdit() {
		return edit;
	}
	public void setEdit(String edit) {
		this.edit = edit;
	}
	public int getCount() {
		return members.size();
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
	public List<AgentReturnPo> getMembers() {
		return members;
	}
	public void setMembers(List<AgentReturnPo> members) {
		this.members = members;
	}
}

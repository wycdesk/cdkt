package com.channelsoft.ems.api.po;

public class CCODRequestPo {

	private String enterpriseId;//企业ID
	private String agentId;//坐席ID（仅限阿拉伯数字）
	private String agentName;//坐席名称
	private String agentPassword;//坐席登录密码
	private String agentRole;//坐席角色：1-普通坐席;2-班长坐席;3-无终端坐席
	//以上字段为必选字段
	
	private String agentDn;//坐席分机
	private String agentIp;//坐席ip
	private String agentPort;//坐席端口
	private String agentEmail;//邮箱
	private String agentPlaceId;//地区编号
	private String recordStatus;//逻辑标记位：1 新增; 0 删除
	private String extension;//扩展字段
	private String sex;//性别：0 女;1 男
	
	
	public String getEnterpriseId() {
		return enterpriseId;
	}
	public void setEnterpriseId(String enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
	public String getAgentId() {
		return agentId;
	}
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	public String getAgentPassword() {
		return agentPassword;
	}
	public void setAgentPassword(String agentPassword) {
		this.agentPassword = agentPassword;
	}
	public String getAgentRole() {
		return agentRole;
	}
	public void setAgentRole(String agentRole) {
		this.agentRole = agentRole;
	}
	public String getAgentDn() {
		return agentDn;
	}
	public void setAgentDn(String agentDn) {
		this.agentDn = agentDn;
	}
	public String getAgentIp() {
		return agentIp;
	}
	public void setAgentIp(String agentIp) {
		this.agentIp = agentIp;
	}
	public String getAgentPort() {
		return agentPort;
	}
	public void setAgentPort(String agentPort) {
		this.agentPort = agentPort;
	}
	public String getAgentEmail() {
		return agentEmail;
	}
	public void setAgentEmail(String agentEmail) {
		this.agentEmail = agentEmail;
	}
	public String getAgentPlaceId() {
		return agentPlaceId;
	}
	public void setAgentPlaceId(String agentPlaceId) {
		this.agentPlaceId = agentPlaceId;
	}
	public String getRecordStatus() {
		return recordStatus;
	}
	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
	}
	public String getExtension() {
		return extension;
	}
	public void setExtension(String extension) {
		this.extension = extension;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}

}

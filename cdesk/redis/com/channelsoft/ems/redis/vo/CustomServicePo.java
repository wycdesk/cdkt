package com.channelsoft.ems.redis.vo;

public class CustomServicePo {
	private String CustomServiceId;
	private String CustomServiceName;
	private String userId;
	private String userName;
	public String getCustomServiceId() {
		return CustomServiceId;
	}
	public void setCustomServiceId(String customServiceId) {
		CustomServiceId = customServiceId;
	}
	public String getCustomServiceName() {
		return CustomServiceName;
	}
	public void setCustomServiceName(String customServiceName) {
		CustomServiceName = customServiceName;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
}

package com.channelsoft.ems.api.po;

import java.io.Serializable;

public class AgentSimplePo implements Serializable {
	private static final long serialVersionUID = -6847725967569128477L;
	private String loginName;
	private String userId;
	private String userName;
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
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

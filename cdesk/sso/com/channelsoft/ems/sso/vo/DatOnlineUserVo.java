package com.channelsoft.ems.sso.vo;

import com.channelsoft.ems.user.po.DatEntUserPo;


public class DatOnlineUserVo extends DatEntUserPo {
	private String sessionKey;
	private String loginTime;
	public String getSessionKey() {
		return sessionKey;
	}
	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}
	public String getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}
}

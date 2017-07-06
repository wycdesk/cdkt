package com.channelsoft.ems.sso.vo;

import java.io.Serializable;

public class SsoResponseVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4376551329377517473L;

	private SsoUserVo ssoUser;
	private String resultCode;
	private String resultDescription;

	public SsoUserVo getSsoUser() {
		return ssoUser;
	}

	public void setSsoUser(SsoUserVo ssoUser) {
		this.ssoUser = ssoUser;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultDescription() {
		return resultDescription;
	}

	public void setResultDescription(String resultDescription) {
		this.resultDescription = resultDescription;
	}

}

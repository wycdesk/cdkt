package com.channelsoft.ems.communicate.po;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.channelsoft.ems.communicate.constant.WorkSource;

public class CommPo {
	private String commId;
	private String userId;
	private String loginName;
	private String userName;
	private String source;
	private String content;
	private String sessionId;
	private Date createTime;
	private String opId;
	private String opName;
	private Date startTime;
	private Date endTime;
	private String phoneNumber;
	private String imNumber;
	private String weChat;
	private String openId;
	private String email;
	private String commTime;
	private String ccodEntId;
	private String ccodAgentId;
	private String remoteUrl;
	private String callType;
	private String strAni;
	private String ccodAgentName;
	private String strDnis;
	private String isConnected;
	
	public String getIsConnected() {
		return isConnected;
	}
	public void setIsConnected(String isConnected) {
		this.isConnected = isConnected;
	}
	public String getStrDnis() {
		return strDnis;
	}
	public void setStrDnis(String strDnis) {
		this.strDnis = strDnis;
	}
	public String getCreateTimeStr(){
		SimpleDateFormat fmTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return fmTime.format(createTime);
	}
	public String getSourceStr(){
		if(StringUtils.isBlank(source)){
			return WorkSource.getEnum("0").desc;
		}else{
			return WorkSource.getEnum(source).desc;
		}
	}
	
	
	
	public String getCcodAgentName() {
		return ccodAgentName;
	}
	public void setCcodAgentName(String ccodAgentName) {
		this.ccodAgentName = ccodAgentName;
	}
	public String getCcodAgentId() {
		return ccodAgentId;
	}
	public void setCcodAgentId(String ccodAgentId) {
		this.ccodAgentId = ccodAgentId;
	}
	public String getCcodEntId() {
		return ccodEntId;
	}
	public void setCcodEntId(String ccodEntId) {
		this.ccodEntId = ccodEntId;
	}
	public String getRemoteUrl() {
		return remoteUrl;
	}
	public void setRemoteUrl(String remoteUrl) {
		this.remoteUrl = remoteUrl;
	}
	public String getCallType() {
		return callType;
	}
	public void setCallType(String callType) {
		this.callType = callType;
	}
	public String getStrAni() {
		return strAni;
	}
	public void setStrAni(String strAni) {
		this.strAni = strAni;
	}
	public String getCommTime() {
		return commTime;
	}
	public void setCommTime(String commTime) {
		this.commTime = commTime;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getImNumber() {
		return imNumber;
	}
	public void setImNumber(String imNumber) {
		this.imNumber = imNumber;
	}
	public String getWeChat() {
		return weChat;
	}
	public void setWeChat(String weChat) {
		this.weChat = weChat;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getCommId() {
		return commId;
	}
	public void setCommId(String commId) {
		this.commId = commId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getOpId() {
		return opId;
	}
	public void setOpId(String opId) {
		this.opId = opId;
	}
	public String getOpName() {
		return opName;
	}
	public void setOpName(String opName) {
		this.opName = opName;
	}
	
}

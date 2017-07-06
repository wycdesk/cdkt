package com.channelsoft.ems.sso.vo;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SsoUserVo implements Serializable{
	private static final long serialVersionUID = -5726533077500831592L;
	private String userId;
	private String entId;
	private String entName;
	private String userType;
	private String roleId;
	private String loginType;
	private String loginName;
	private String loginPwd;
	private String nickName;
	private String email;
	private String emailPwd;
	private String telPhone;
	private String telPwd;
	private String qq;
	private String qqPwd;
	private String weixin;
	private String wxPwd;
	private String weibo;
	private String wbPwd;
	private String userName;
	private String sex;
	private String age;
	private String contactPhone;
	private String fixedPhone;
	private String address;
	private String creatorId;
	private String creatorName;
	private String creatorTime;
	private String updatorId;
	private String updatorName;
	private String updateTime;
	private String userStatus;
	private String group;
	
	private String sessionKey;
	private String loginTime;
	private String ccodEntId;//ccod的企业编号
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getEntId() {
		return entId;
	}
	public void setEntId(String entId) {
		this.entId = entId;
	}
	public String getEntName() {
		return entName;
	}
	public void setEntName(String entName) {
		this.entName = entName;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getLoginType() {
		return loginType;
	}
	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getLoginPwd() {
		return loginPwd;
	}
	public void setLoginPwd(String loginPwd) {
		this.loginPwd = loginPwd;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEmailPwd() {
		return emailPwd;
	}
	public void setEmailPwd(String emailPwd) {
		this.emailPwd = emailPwd;
	}
	public String getTelPhone() {
		return telPhone;
	}
	public void setTelPhone(String telPhone) {
		this.telPhone = telPhone;
	}
	public String getTelPwd() {
		return telPwd;
	}
	public void setTelPwd(String telPwd) {
		this.telPwd = telPwd;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getQqPwd() {
		return qqPwd;
	}
	public void setQqPwd(String qqPwd) {
		this.qqPwd = qqPwd;
	}
	public String getWeixin() {
		return weixin;
	}
	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}
	public String getWxPwd() {
		return wxPwd;
	}
	public void setWxPwd(String wxPwd) {
		this.wxPwd = wxPwd;
	}
	public String getWeibo() {
		return weibo;
	}
	public void setWeibo(String weibo) {
		this.weibo = weibo;
	}
	public String getWbPwd() {
		return wbPwd;
	}
	public void setWbPwd(String wbPwd) {
		this.wbPwd = wbPwd;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getContactPhone() {
		return contactPhone;
	}
	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}
	public String getFixedPhone() {
		return fixedPhone;
	}
	public void setFixedPhone(String fixedPhone) {
		this.fixedPhone = fixedPhone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
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
	public String getCreatorTime() {
		return creatorTime;
	}
	public void setCreatorTime(String creatorTime) {
		this.creatorTime = creatorTime;
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
	public String getUserStatus() {
		return userStatus;
	}
	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
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
	public String getCcodEntId() {
		return ccodEntId;
	}
	public void setCcodEntId(String ccodEntId) {
		this.ccodEntId = ccodEntId;
	}
	
}

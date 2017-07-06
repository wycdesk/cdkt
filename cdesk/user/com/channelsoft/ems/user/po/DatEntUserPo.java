package com.channelsoft.ems.user.po;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

public class DatEntUserPo implements Serializable {
	private static final long serialVersionUID = 2382667398471260616L;
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
	private String groupId;
	private String activeCode;
	private String creatorId;
	private String creatorName;
	private String createTime;
	private String updatorId;
	private String updatorName;
	private String updateTime;
	private String userStatus;
	private String org;
	private String userLabel;
	private String lang;
	private String remark;
	private String userDesc;
	private String phoneBinded;
	private String photoUrl;
	
	private String webchatId;
	private String webchatPwd;
	private String loginTime;
	private String signature;
	private String ip;
	private String ccodEntId;//ccod的企业编号
	private String founder;//创始人标记，默认为0，为1的时候表示创始人
	
	private String commId;
	private String openId;

	private String vedioId;
	private String vedioPwd;
	
	private String dptId;//部门编号
	private String jobId;//岗位编号
	
	public String getCommId() {
		return commId;
	}
	public void setCommId(String commId) {
		this.commId = commId;
	}
	public String getFounder() {
		return founder;
	}
	public void setFounder(String founder) {
		this.founder = founder;
	}
	public String getWebchatId() {
		return webchatId;
	}
	public void setWebchatId(String webchatId) {
		this.webchatId = webchatId;
	}
	public String getLoginTime() {
		if(StringUtils.isNotBlank(loginTime)&&loginTime.length()>=19){
			return loginTime.substring(0, 19);
		}
		return loginTime;
	}
	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}
	
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
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getActiveCode() {
		return activeCode;
	}
	public void setActiveCode(String activeCode) {
		this.activeCode = activeCode;
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
	public String getCreateTime() {
		if(StringUtils.isNotBlank(createTime)&&createTime.length()>=19){
			return createTime.substring(0, 19);
		}

		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
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
		if(StringUtils.isNotBlank(updateTime)&&updateTime.length()>=19){
			return updateTime.substring(0, 19);
		}
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
	public String getOrg() {
		return org;
	}
	public void setOrg(String org) {
		this.org = org;
	}
	public String getUserLabel() {
		return userLabel;
	}
	public void setUserLabel(String userLabel) {
		this.userLabel = userLabel;
	}
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getUserDesc() {
		return userDesc;
	}
	public void setUserDesc(String userDesc) {
		this.userDesc = userDesc;
	}
	public String getPhoneBinded() {
		return phoneBinded;
	}
	public void setPhoneBinded(String phoneBinded) {
		this.phoneBinded = phoneBinded;
	}
	public String getPhotoUrl() {
		return photoUrl;
	}
	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getCcodEntId() {
		return ccodEntId;
	}
	public void setCcodEntId(String ccodEntId) {
		this.ccodEntId = ccodEntId;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getVedioId() {
		return vedioId;
	}
	public void setVedioId(String vedioId) {
		this.vedioId = vedioId;
	}
	public String getWebchatPwd() {
		return webchatPwd;
	}
	public void setWebchatPwd(String webchatPwd) {
		this.webchatPwd = webchatPwd;
	}
	public String getVedioPwd() {
		return vedioPwd;
	}
	public void setVedioPwd(String vedioPwd) {
		this.vedioPwd = vedioPwd;
	}
	public String getDptId() {
		return dptId;
	}
	public void setDptId(String dptId) {
		this.dptId = dptId;
	}
	public String getJobId() {
		return jobId;
	}
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
	
}

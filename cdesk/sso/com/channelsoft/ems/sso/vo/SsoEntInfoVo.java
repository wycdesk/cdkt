package com.channelsoft.ems.sso.vo;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SsoEntInfoVo  implements Serializable {
	private static final long serialVersionUID = -6590305992856109448L;
	private String entId;
	private String entName;
	private String entDesc;
	private String email;
	private String register;
	private String telphone;
	private String password;
	private String domainName;
	private String areaCode;
	private String domainCode;
	private String province;
	private String city;
	private String area;
	private String industry;
	private String scale;
	private String contactUser;
	private String address;
	private String zipCode;
	private String seo;
	private String logoUrl;
	private String faviconUrl;
	private String createTime;
	private String creatorId;
	private String creatorName;
	private String status;
	private String updatorId;
	private String updatorName;
	private String ccodEntId;//ccod的企业编号
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
	public String getEntDesc() {
		return entDesc;
	}
	public void setEntDesc(String entDesc) {
		this.entDesc = entDesc;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRegister() {
		return register;
	}
	public void setRegister(String register) {
		this.register = register;
	}
	public String getTelphone() {
		return telphone;
	}
	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDomainName() {
		return domainName;
	}
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getDomainCode() {
		return domainCode;
	}
	public void setDomainCode(String domainCode) {
		this.domainCode = domainCode;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getIndustry() {
		return industry;
	}
	public void setIndustry(String industry) {
		this.industry = industry;
	}
	public String getScale() {
		return scale;
	}
	public void setScale(String scale) {
		this.scale = scale;
	}
	public String getContactUser() {
		return contactUser;
	}
	public void setContactUser(String contactUser) {
		this.contactUser = contactUser;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getLogoUrl() {
		return logoUrl;
	}
	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}
	public String getFaviconUrl() {
		return faviconUrl;
	}
	public void setFaviconUrl(String faviconUrl) {
		this.faviconUrl = faviconUrl;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public String getSeo() {
		return seo;
	}
	public void setSeo(String seo) {
		this.seo = seo;
	}
	public String getCcodEntId() {
		return ccodEntId;
	}
	public void setCcodEntId(String ccodEntId) {
		this.ccodEntId = ccodEntId;
	}
	
}

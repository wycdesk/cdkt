package com.channelsoft.ems.sso.vo;

import java.io.Serializable;

public class SsoPermissionVo implements Serializable {
	private static final long serialVersionUID = 2931631094981687722L;
	private String id; //主键
	private String name; //菜单/权限名
	private String parentId; //父节点ID
	private String type;
	private String platformId;
	private String actionUrl;
	private String url; //菜单绝对路径：平台+相对路径+SessionKey+参数
	private String sortWeight; //排序
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getSortWeight() {
		return sortWeight;
	}
	public void setSortWeight(String sortWeight) {
		this.sortWeight = sortWeight;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getType() {
		return type;
	}
	public void setActionUrl(String actionUrl) {
		this.actionUrl = actionUrl;
	}
	public String getActionUrl() {
		return actionUrl;
	}
	public void setPlatformId(String platformId) {
		this.platformId = platformId;
	}
	public String getPlatformId() {
		return platformId;
	}
}

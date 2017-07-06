package com.channelsoft.ems.sso.vo;

import java.io.Serializable;

public class SsoRoleVo implements Serializable {
	private static final long serialVersionUID = -847546460097700529L;
	private String id;
	private String name;
	private String description;
	private String parentId;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
}

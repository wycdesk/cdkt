package com.channelsoft.ems.privilege.po;

import java.io.Serializable;
/**
 * 权限-角色绑定关系表。
 * <dl>
 * <dt>portal</dt>
 * <dd>Description:</dd>
 * <dd>Copyright: Copyright (C) 2013</dd>
 * <dd>Company: 青牛(北京)技术有限公司 成都研究所</dd>
 * <dd>CreateDate: 2013-6-7</dd>
 * </dl>
 * @author 魏铭
 */
public class CfgRolePermissionPo implements Serializable{
	private static final long serialVersionUID = -2756877083943064760L;
	private String id;
	private String permissionId;
	private String roleId;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPermissionId() {
		return permissionId;
	}
	public void setPermissionId(String permissionId) {
		this.permissionId = permissionId;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
}

package com.channelsoft.ems.sso.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 登陆结果，封装了各种登陆信息
 * <dl>
 * <dt>portal</dt>
 * <dd>Description:</dd>
 * <dd>Copyright: Copyright (C) 2013</dd>
 * <dd>Company: 青牛(北京)技术有限公司 成都研究所</dd>
 * <dd>CreateDate: 2013-6-7</dd>
 * </dl>
 * 
 * @author 魏铭
 */
public class LoginResultVo implements Serializable {
	private static final long serialVersionUID = -2191240298703313040L;
	/**
	 * 会话KEY标识
	 */
	private String sessionKey;
	/**
	 * 用户信息
	 */
	private SsoUserVo userInfo;
	/**
	 * 租户信息
	 */

	private SsoEntInfoVo entInfo;

	/**
	 * 下属角色列表
	 */
	private List<SsoRoleVo> roleList;
	/**
	 * 菜单列表
	 */
	private List<SsoPermissionVo> menuList;
	/**
	 * 权限列表
	 */
	private Map<String, String> privilegeList;

	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	public SsoUserVo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(SsoUserVo userInfo) {
		this.userInfo = userInfo;
	}

	public List<SsoRoleVo> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<SsoRoleVo> roleList) {
		this.roleList = roleList;
	}

	public List<SsoPermissionVo> getMenuList() {
		return menuList;
	}

	public void setMenuList(List<SsoPermissionVo> menuList) {
		this.menuList = menuList;
	}

	public Map<String, String> getPrivilegeList() {
		return privilegeList;
	}

	public void setPrivilegeList(Map<String, String> privilegeList) {
		this.privilegeList = privilegeList;
	}

	public SsoEntInfoVo getEntInfo() {
		return entInfo;
	}

	public void setEntInfo(SsoEntInfoVo entInfo) {
		this.entInfo = entInfo;
	}
}

package com.channelsoft.ems.sso.service.impl;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.channelsoft.cri.constant.BaseErrCode;
import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.logger.SystemLogUtils;
import com.channelsoft.cri.service.BaseService;
import com.channelsoft.ems.ent.service.IDatEntService;
import com.channelsoft.ems.group.service.IGroupMongoService;
import com.channelsoft.ems.group.service.IGroupService;
import com.channelsoft.ems.privilege.service.ICfgPermissionService;
import com.channelsoft.ems.privilege.service.ICfgRoleService;
import com.channelsoft.ems.sso.service.IDatOnlineUserService;
import com.channelsoft.ems.sso.service.ISsoServerService;
import com.channelsoft.ems.sso.util.SsoSessionUtils;
import com.channelsoft.ems.sso.vo.LoginResultVo;
import com.channelsoft.ems.sso.vo.SsoEntInfoVo;
import com.channelsoft.ems.sso.vo.SsoPermissionVo;
import com.channelsoft.ems.sso.vo.SsoRoleVo;
import com.channelsoft.ems.sso.vo.SsoUserVo;
import com.channelsoft.ems.user.service.IUserService;

public class SsoServerServiceImpl extends BaseService implements
		ISsoServerService {
	@Autowired
	IUserService userService;
	@Autowired
	IDatOnlineUserService onlineUserService;
	@Autowired
	ICfgRoleService roleService;
	@Autowired
	ICfgPermissionService permissionService;
	@Autowired
	IDatEntService entService;
	@Autowired
	IGroupService groupService;

	@Autowired
	IGroupMongoService groupMongoService;
	@Override
	@Transactional
	public LoginResultVo login(String enterpriseid, String loginName,
			String password, String platformId, HttpServletRequest request)
			throws ServiceException {
		/**
		 * 参数异常时抛出错误
		 */
		if ((StringUtils.isBlank(enterpriseid)
				|| StringUtils.isBlank(loginName) || StringUtils
					.isBlank(password))) {
			throw new ServiceException(BaseErrCode.PARAM_ERROR);
		}
		LoginResultVo login = new LoginResultVo();
		/**
		 * 查询租户信息
		 */

		SsoEntInfoVo entInfo = entService.query(enterpriseid);
		login.setEntInfo(entInfo);

		/**
		 * 查询用户信息
		 */
		SsoUserVo userInfo = userService.login(enterpriseid, loginName,
				password, request);
		/**
		 * 查询组
		 */
		String groupStr = groupService.getAgentGroupsStr(enterpriseid, loginName);
		userInfo.setGroup(groupStr);
		login.setUserInfo(userInfo);

		String sessionKey = onlineUserService.login(enterpriseid, userInfo,
				platformId, request);
		login.setSessionKey(sessionKey);
		SsoSessionUtils.loginMap.put(loginName + "_" + enterpriseid, sessionKey);
		/**
		 * 查询用户下属的角色
		 */
		List<SsoRoleVo> roleList = roleService.getChildList(enterpriseid,
				userInfo.getRoleId());
		login.setRoleList(roleList);
		/**
		 * 查询权限和菜单
		 */
		List<SsoPermissionVo> menuList = permissionService.getMenuList(
				enterpriseid, userInfo.getRoleId());
		login.setMenuList(menuList);

		Map<String, String> privilegeList = permissionService.getPrivilege(
				enterpriseid, userInfo.getRoleId());
		login.setPrivilegeList(privilegeList);
		
		return login;
	}

	@Override
	@Transactional
	public LoginResultVo platformSwitch(String enterpriseid, String sessionKey,
			String platformId) throws ServiceException {

		if (StringUtils.isBlank(sessionKey) || StringUtils.isBlank(platformId)) {
			throw new ServiceException(BaseErrCode.PARAM_ERROR);
		}
		LoginResultVo login = new LoginResultVo();

		SsoUserVo userInfo = onlineUserService.getUserBySessionKey(
				enterpriseid, sessionKey);
		login.setUserInfo(userInfo);

		login.setSessionKey(sessionKey);
		/**
		 * 查询用户下属的角色
		 */
		List<SsoRoleVo> roleList = roleService.getChildList(enterpriseid,
				userInfo.getRoleId());
		login.setRoleList(roleList);
		/**
		 * 查询权限和菜单
		 */
		List<SsoPermissionVo> menuList = permissionService.getMenuList(
				enterpriseid, userInfo.getRoleId());
		login.setMenuList(menuList);

		Map<String, String> privilegeList = permissionService.getPrivilege(
				enterpriseid, userInfo.getRoleId());
		login.setPrivilegeList(privilegeList);

		return login;
	}

	@Override
	@Transactional
	public String logout(String enterpriseid, String sessionKey,
			String platformId) throws ServiceException {
		if (StringUtils.isBlank(sessionKey)) {
			throw new ServiceException(BaseErrCode.PARAM_ERROR);
		}
		SsoUserVo userInfo = onlineUserService.getUserBySessionKey(
				enterpriseid, sessionKey);
		onlineUserService.logout(enterpriseid, sessionKey);
		return userInfo.getLoginName();
	}

	@Override
	public SsoUserVo getLoginUserInfo(String enterpriseid, String sessionKey)
			throws ServiceException {
		if (StringUtils.isBlank(sessionKey)) {
			throw new ServiceException(BaseErrCode.PARAM_ERROR);
		}

		SsoUserVo userInfo = onlineUserService.getUserBySessionKey(
				enterpriseid, sessionKey);

		return userInfo;
	}

}

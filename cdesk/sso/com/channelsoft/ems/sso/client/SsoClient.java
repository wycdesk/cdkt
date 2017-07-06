package com.channelsoft.ems.sso.client;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.channelsoft.cri.constant.BaseErrCode;
import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.logger.SystemLogUtils;
import com.channelsoft.cri.util.CdeskEncrptDes;
import com.channelsoft.cri.util.HttpPostUtils;
import com.channelsoft.cri.util.JsonUtils;
import com.channelsoft.ems.sso.constant.SsoParamConstants;
import com.channelsoft.ems.sso.util.SsoSessionUtils;
import com.channelsoft.ems.sso.vo.LoginResultVo;
import com.channelsoft.ems.sso.vo.SsoEntInfoVo;
import com.channelsoft.ems.sso.vo.SsoPermissionVo;
import com.channelsoft.ems.sso.vo.SsoResponseVo;
import com.channelsoft.ems.sso.vo.SsoRoleVo;
import com.channelsoft.ems.sso.vo.SsoUserVo;

/**
 * SSO客户端工具类
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
public class SsoClient {
	/**
	 * sso登录
	 * 
	 * @param loginName
	 * @param password
	 * @param platformId
	 * @return
	 */
	public static void login(String enterpriseid, String sessionKey,
			String loginName, String password, String platformId,
			HttpServletRequest request) throws ServiceException {

		if (StringUtils.isBlank(loginName) || StringUtils.isBlank(password)
				|| StringUtils.isBlank(platformId)) {
			throw new ServiceException(BaseErrCode.PARAM_ERROR);
		}
		/**
		 * 对密码进行MD5加密
		 */
//		String MD5Password = DigestUtils.md5Hex(password.trim());
		String desPassword = CdeskEncrptDes.encryptST(password.trim());
		Map<String, String> postMap = new HashMap<String, String>();

		/**
		 * Http请求参数
		 */
		postMap.put("enterpriseid", enterpriseid.trim());
		postMap.put("sessionKey", sessionKey.trim());
		postMap.put("loginName", loginName.trim());
		postMap.put("password", desPassword);
		postMap.put("platformId", platformId.trim());

		LoginResultVo loginResult = new LoginResultVo();
		SystemLogUtils.Debug("向服务端发送Http post请求" + postMap.get("loginName")
				+ postMap.get("password") + postMap.get("platformId")
				+ SsoParamConstants.SSO_LOGIN_URL);

		SsoResponseVo resp = null;
		resp = getSsoResponseVo(postMap);
		/**
		 * 登陆结果
		 */
		if (!BaseErrCode.SUCCESS.code.equals(resp.getResultCode())) {
			throw new ServiceException(resp.getResultCode(),
					resp.getResultDescription());
		}
		if (resp.getSsoUser() == null
				|| StringUtils.isBlank(resp.getSsoUser().getSessionKey())) {
			throw new ServiceException("返回结果异常");
		}

		/**
		 * portal验证后返回用户信息
		 */
		SsoUserVo userInfo = resp.getSsoUser();
		loginResult.setUserInfo(userInfo);
		loginResult.setSessionKey(userInfo.getSessionKey());
		Map<String, String> postMap2 = new HashMap<String, String>();

		postMap2.put("enterpriseid", enterpriseid.trim());
		postMap2.put("roleId", userInfo.getRoleId().trim());

	 
		/**
		 * 查询角色
		 */
		List<SsoRoleVo> roleList = getChildList(postMap2);
		loginResult.setRoleList(roleList);

		/**
		 * 查询菜单列表
		 */
		List<SsoPermissionVo> menuList = getMunuList(postMap2);
		loginResult.setMenuList(menuList);
		/**
		 * 查询权限列表
		 */
		Map<String, String> privilegeList = getPrivilege(postMap2);
		loginResult.setPrivilegeList(privilegeList);

		/**
		 * 处理Session
		 */
		SsoSessionUtils.login(loginResult, request);
	}

	/**
	 * sso平台切换
	 * 
	 * @param sessionKey
	 * @return
	 */
	public static void platformSwitch(String enterpriseid, String sessionKey,
			String platformId, HttpServletRequest request)
			throws ServiceException {

		if (StringUtils.isBlank(enterpriseid)
				|| StringUtils.isBlank(sessionKey)
				|| StringUtils.isBlank(platformId)) {
			throw new ServiceException(BaseErrCode.PARAM_ERROR);
		}
		Map<String, String> postMap = new HashMap<String, String>();

		LoginResultVo loginResult = new LoginResultVo();
		postMap.put("enterpriseid", enterpriseid.trim());
		postMap.put("sessionKey", sessionKey.trim());
		postMap.put("platformId", platformId.trim());

		/**
		 * portal验证后返回用户信息
		 */
		SsoUserVo ssoUsrVo = getSsoUserVo(postMap);
		if (ssoUsrVo == null || StringUtils.isBlank(ssoUsrVo.getSessionKey())) {
			throw new ServiceException("返回结果异常");
		}
		loginResult.setUserInfo(ssoUsrVo);
		loginResult.setSessionKey(ssoUsrVo.getSessionKey());
		Map<String, String> postMap2 = new HashMap<String, String>();

		postMap2.put("enterpriseid", enterpriseid.trim());
		postMap2.put("roleId", ssoUsrVo.getRoleId().trim());
		
		/**
		 * 查询企业信息
		 */
		SsoEntInfoVo entVo = getEntInfoVo(postMap2);
		loginResult.setEntInfo(entVo);
		/**
		 * 查询角色
		 */

		List<SsoRoleVo> roleList = getChildList(postMap2);
		loginResult.setRoleList(roleList);
		/**
		 * 查询菜单列表
		 */
		List<SsoPermissionVo> menuList = getMunuList(postMap2);
		loginResult.setMenuList(menuList);
		/**
		 * 查询权限列表
		 */
		Map<String, String> privilegeList = getPrivilege(postMap2);
		loginResult.setPrivilegeList(privilegeList);

		/**
		 * 子系统将返回结果 处理Session
		 */
		SsoSessionUtils.login(loginResult, request);
	}
	
	private static SsoEntInfoVo getEntInfoVo(Map<String, String> postMap) {
		SsoEntInfoVo ssoEntInfoVo = null;

		String ssoEntInfoVoReturn = "";
		try {
			ssoEntInfoVoReturn = HttpPostUtils.httpPost(
					SsoParamConstants.SSO_LOGIN_ENT, postMap);
		} catch (Exception e) {
			e.printStackTrace();
			ssoEntInfoVoReturn = "";
		}
		if (StringUtils.isBlank(ssoEntInfoVoReturn)) {
			throw new ServiceException("企业信息获得失败");
		}

		try {
			ssoEntInfoVoReturn = URLDecoder.decode(ssoEntInfoVoReturn, "UTF-8");
			ssoEntInfoVo = (SsoEntInfoVo) JsonUtils.fromJson(ssoEntInfoVoReturn,
					SsoEntInfoVo.class);

		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("解析服务端返回的信息失败");
		}
		return ssoEntInfoVo;
	}

	@SuppressWarnings("unchecked")
	private static List<SsoRoleVo> getChildList(Map<String, String> postMap) {
		List<SsoRoleVo> ssoRoleVo = null;

		String ssoRoleVoReturn = "";
		try {
			ssoRoleVoReturn = HttpPostUtils.httpPost(
					SsoParamConstants.SSO_LOGIN_ROLE, postMap);
		} catch (Exception e) {
			e.printStackTrace();
			ssoRoleVoReturn = "";
		}
		if (StringUtils.isBlank(ssoRoleVoReturn)) {
			throw new ServiceException("角色信息获得失败");
		}

		try {
			ssoRoleVoReturn = URLDecoder.decode(ssoRoleVoReturn, "UTF-8");
			ssoRoleVo = (List<SsoRoleVo>) JsonUtils.fromJson(ssoRoleVoReturn,
					List.class);

		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("解析服务端返回的信息失败");
		}
		return ssoRoleVo;
	}

	@SuppressWarnings("unchecked")
	private static Map<String, String> getPrivilege(Map<String, String> postMap) {
		Map<String, String> privilegeList = null;

		String privilegeStr = "";
		try {
			privilegeStr = HttpPostUtils.httpPost(
					SsoParamConstants.SSO_LOGIN_PRIVILEGELIST, postMap);
			
		} catch (Exception e) {
			e.printStackTrace();
			privilegeStr = "";
		}
		if (StringUtils.isBlank(privilegeStr)) {
			throw new ServiceException("用户信息获得失败");
		}

		try {
			privilegeStr = URLDecoder.decode(privilegeStr, "UTF-8");
			privilegeList = (Map<String, String>) JsonUtils.fromJson(
					privilegeStr, Map.class);

		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("解析服务端返回的信息失败");
		}
		return privilegeList;
	}

	@SuppressWarnings("unchecked")
	private static List<SsoPermissionVo> getMunuList(Map<String, String> postMap) {
		List<SsoPermissionVo> ssoPermissionVo = null;

		String permissionReturn = "";
		try {
			permissionReturn = HttpPostUtils.httpPost(
					SsoParamConstants.SSO_LOGIN_MENULIST, postMap);
		} catch (Exception e) {
			e.printStackTrace();
			permissionReturn = "";
		}
		if (StringUtils.isBlank(permissionReturn)) {
			throw new ServiceException("用户信息获得失败");
		}

		try {
			permissionReturn = URLDecoder.decode(permissionReturn, "UTF-8");
			ssoPermissionVo = (List<SsoPermissionVo>) JsonUtils.fromJson(
					permissionReturn, List.class);

		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("解析服务端返回的信息失败");
		}
		return ssoPermissionVo;
	}

	/**
	 * sso登出
	 * 
	 * @param sessionKey
	 * @return
	 */
	public static void logout(String enterpriseid, String sessionKey,
			String platformId, HttpServletRequest request)
			throws ServiceException {

		Map<String, String> postMap = new HashMap<String, String>();
		postMap.put("enterpriseid", enterpriseid);
		// Http请求参数

		postMap.put("sessionKey", sessionKey);
		postMap.put("platformId", platformId);

		try {
			// 向服务端发送Http post请求
			HttpPostUtils.httpPost(SsoParamConstants.SSO_LOGOUT_URL, postMap);
			SsoSessionUtils.logout(request);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("访问登出接口失败");
		}
	}

	private static SsoResponseVo getSsoResponseVo(Map<String, String> postMap) {
		SsoResponseVo ssoInof = null;

		String rsponseReturn = "";
		try {
			rsponseReturn = HttpPostUtils.httpPost(
					SsoParamConstants.SSO_LOGIN_URL, postMap);
		} catch (Exception e) {
			e.printStackTrace();
			rsponseReturn = "";
		}
		if (StringUtils.isBlank(rsponseReturn)) {
			throw new ServiceException("登陆信息结果获得失败");
		}

		try {
			rsponseReturn = URLDecoder.decode(rsponseReturn, "UTF-8");
			ssoInof = (SsoResponseVo) JsonUtils.fromJson(rsponseReturn,
					SsoResponseVo.class);

		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("解析服务端返回的信息失败");
		}
		return ssoInof;

	}

	private static SsoUserVo getSsoUserVo(Map<String, String> postMap) {
		SsoUserVo ssoUserInof = null;

		String userInfoReturn = "";
		try {
			userInfoReturn = HttpPostUtils.httpPost(
					SsoParamConstants.SSO_LOGIN_USERINFO, postMap);
		} catch (Exception e) {
			e.printStackTrace();
			userInfoReturn = "";
		}
		if (StringUtils.isBlank(userInfoReturn)) {
			throw new ServiceException("用户信息获得失败");
		}

		try {
			userInfoReturn = URLDecoder.decode(userInfoReturn, "UTF-8");
			ssoUserInof = (SsoUserVo) JsonUtils.fromJson(userInfoReturn,
					SsoUserVo.class);

		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("解析服务端返回的信息失败");
		}
		return ssoUserInof;

	}

	public static void updateUnitName(String unitId, String unitName,
			String userType, HttpServletRequest request) {
		Map<String, String> postMap = new HashMap<String, String>();

		// Http请求参数
		postMap.put("unitId", unitId);
		postMap.put("unitName", unitName);
		postMap.put("userType", userType);
		try {
			// 向服务端发送Http post请求
			HttpPostUtils
					.httpPost(SsoParamConstants.SSO_UNIT_NAME_URL, postMap);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("访问更新用户接口失败");
		}
	}
}

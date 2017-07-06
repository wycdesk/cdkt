package com.channelsoft.ems.sso.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.channelsoft.cri.logger.SystemLogUtils;
import com.channelsoft.ems.sso.vo.LoginResultVo;
import com.channelsoft.ems.sso.vo.SsoEntInfoVo;
import com.channelsoft.ems.sso.vo.SsoPermissionVo;
import com.channelsoft.ems.sso.vo.SsoRoleVo;
import com.channelsoft.ems.sso.vo.SsoUserVo;

public class SsoSessionUtils {
	/**
	 * 登陆结果会话参数名
	 */
	public static final String CLIENT_SESSION_LOGIN_RESULT = "PORTAL_CLIENT_LOGIN_RESULT";

	public static final String CLIENT_SESSION_SESSION_KEY = "PORTAL_CLIENT_SESSION_KEY";

	public static final String CLIENT_SESSION_USERINFO = "PORTAL_CLIENT_SESSION_USERINFO";
	
	public static Map<String,String> loginMap = new HashMap<String,String>();

	public static void login(LoginResultVo result, HttpServletRequest request) {
		request.getSession().setAttribute(CLIENT_SESSION_LOGIN_RESULT, result);
		request.getSession().setAttribute(CLIENT_SESSION_SESSION_KEY,
				result.getSessionKey());
		 
	}

	public static void logout(HttpServletRequest request) {
		request.getSession().removeAttribute(CLIENT_SESSION_LOGIN_RESULT);
		request.getSession().removeAttribute(CLIENT_SESSION_SESSION_KEY);
	}
	
	public static void updateGroup(String groupStr, HttpServletRequest request) {
		LoginResultVo loginResult = (LoginResultVo) request.getSession().getAttribute(CLIENT_SESSION_LOGIN_RESULT);
		loginResult.getUserInfo().setGroup(groupStr);
		request.getSession().setAttribute(CLIENT_SESSION_LOGIN_RESULT, loginResult);
		
		LoginResultVo loginResulta = (LoginResultVo) request.getSession().getAttribute(CLIENT_SESSION_LOGIN_RESULT);
		String test=loginResulta.getUserInfo().getGroup();
		
	}

	private static LoginResultVo getLoginResult(HttpServletRequest request) {
		if (isHasLogin(request)) {
			return (LoginResultVo) request.getSession().getAttribute(CLIENT_SESSION_LOGIN_RESULT);
		}
		return null;
	}

	public static boolean isHasLogin(HttpServletRequest request) {

		return request.getSession().getAttribute(CLIENT_SESSION_LOGIN_RESULT) != null
				&& request.getSession()
						.getAttribute(CLIENT_SESSION_SESSION_KEY) != null;
	}

	public static SsoUserVo getUserInfo(HttpServletRequest request) {
		if (isHasLogin(request)) {
			return getLoginResult(request).getUserInfo();
		}
		return null;
	}

	public static String getLoginName(HttpServletRequest request) {
		if (isHasLogin(request)) {
			return getUserInfo(request).getLoginName();
		}
		return "";
	}

	public static List<SsoRoleVo> getRoleList(HttpServletRequest request) {
		if (isHasLogin(request)) {
			return getLoginResult(request).getRoleList();
		}
		return new ArrayList<SsoRoleVo>();
	}

	public static Map<String, String> getPrivilege(HttpServletRequest request) {
		if (isHasLogin(request)) {
			return getLoginResult(request).getPrivilegeList();
		}
		return new HashMap<String, String>();
	}

	public static List<SsoPermissionVo> getMenuList(HttpServletRequest request) {
		if (isHasLogin(request)) {
			return getLoginResult(request).getMenuList();
		}
		return new ArrayList<SsoPermissionVo>();
	}

	public static String getSessionKey(HttpServletRequest request) {
		if (isHasLogin(request)) {
			return (String) request.getSession().getAttribute(
					CLIENT_SESSION_SESSION_KEY);
		}
		return "";
	}

	public static int hasPrivilege(String url, HttpServletRequest request) {
		if (isHasLogin(request)) {
			Map<String, String> permissionList = getPrivilege(request);
			String v = permissionList.get(url);
			if (StringUtils.isBlank(v)) {
				return 1; // 平台没配置此url
			} else if ("0".equalsIgnoreCase(v)) {
				return 2; // 没权限
			}
			return 0;// 有权限
		}
		return -1;// 超时
	}

	/**
	 * @author 程立涛
	 * @date 2015-4-14 14:21
	 * @title 根据输入的数据库Id,返回数据库名
	 * @param entId
	 * @return
	 */
	public static String getEntDB(String entId) {
		return "`ent_" + entId + "`";
	}

	/**
	 * @title 查询租户信息
	 * @author 程立涛
	 * @param request
	 * @date 2015-4-15 13:44
	 * @return
	 */
	public static SsoEntInfoVo getEntInfo(HttpServletRequest request) {
		if (isHasLogin(request)) {
			return getLoginResult(request).getEntInfo();
		}
		return null;
	}

}

package com.channelsoft.ems.sso.controller;

import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.channelsoft.cri.constant.BaseConstants;
import com.channelsoft.cri.controller.BaseController;
import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.logger.LoginLogUtils;
import com.channelsoft.cri.util.JsonUtils;
import com.channelsoft.cri.vo.AjaxResultPo;
import com.channelsoft.ems.ent.service.IDatEntService;
import com.channelsoft.ems.group.service.IGroupService;
import com.channelsoft.ems.privilege.service.ICfgPermissionService;
import com.channelsoft.ems.privilege.service.ICfgRoleService;
import com.channelsoft.ems.sso.constant.SsoParamConstants;
import com.channelsoft.ems.sso.service.ISsoServerService;
import com.channelsoft.ems.sso.util.SsoSessionUtils;
import com.channelsoft.ems.sso.vo.LoginResultVo;
import com.channelsoft.ems.sso.vo.SsoEntInfoVo;
import com.channelsoft.ems.sso.vo.SsoPermissionVo;
import com.channelsoft.ems.sso.vo.SsoResponseVo;
import com.channelsoft.ems.sso.vo.SsoRoleVo;
import com.channelsoft.ems.sso.vo.SsoUserVo;

@Controller
@RequestMapping("/sso")
public class SsoServerController extends BaseController {
	@Autowired
	ICfgRoleService roleService;
	@Autowired
	ISsoServerService ssoServerService;
	@Autowired
	ICfgPermissionService permissionService;
	@Autowired
	IGroupService groupService;
	@Autowired
	IDatEntService entService;

	/**
	 * SSO单点登陆接口。 返回结果为Json串，串为空表示登陆失败。
	 * 
	 * @param loginName
	 * @param password
	 * @param platformId
	 * @return
	 * @CreateDate: 2013-6-7 下午07:17:05
	 * @author 魏铭
	 */
	@RequestMapping(value = "/login")
	@ResponseBody
	public String login(String enterpriseid, String sessionKey,
			String loginName, String password, String platformId) {
		String jsonStr = "";
		enterpriseid=enterpriseid.toLowerCase();
		SsoResponseVo resp = new SsoResponseVo();
		try {
			SsoUserVo userVo = null;
			try {
				userVo = ssoServerService.getLoginUserInfo(enterpriseid, sessionKey);
				String groupStr = groupService.getAgentGroupsStr(enterpriseid, loginName);
				userVo.setGroup(groupStr);
				resp.setSsoUser(userVo);
				LoginLogUtils.LoginSuccess(userVo.getLoginName(), platformId);
			} catch (ServiceException e) {
				LoginLogUtils.LoginFail(sessionKey, platformId, e);
				resp.setResultCode("1");
				resp.setResultDescription(e.desc);
			}
			jsonStr = JsonUtils.toJson(resp);
			jsonStr = URLEncoder.encode(jsonStr, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			LoginLogUtils.LoginFail(sessionKey, platformId,
					new ServiceException(e.getMessage()));
		}
		return jsonStr;
	}
	
	@RequestMapping(value = "/ent")
	@ResponseBody
	public String ent(String enterpriseid) {
		String jsonStr = "";
		SsoEntInfoVo ent = null;
		try {
			try {
				ent = entService.query(enterpriseid);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
			jsonStr = JsonUtils.toJson(ent);
			jsonStr = URLEncoder.encode(jsonStr, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();

		}
		return jsonStr;
	}

	/**
	 * @title 验证Sessionkey,并获得角色列表
	 * 
	 * @param enterpriseid
	 * @param sessionKey
	 * @param platformId
	 * @return
	 * 
	 * @author 程立涛
	 * @date 2015-4-27 18:07
	 */

	@RequestMapping(value = "/role")
	@ResponseBody
	public String role(String enterpriseid, String roleId) {
		String jsonStr = "";
		List<SsoRoleVo> roleList = null;
		try {
			try {
				roleList = roleService.getChildList(enterpriseid, roleId);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
			jsonStr = JsonUtils.toJson(roleList);
			jsonStr = URLEncoder.encode(jsonStr, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();

		}
		return jsonStr;
	}

	/**
	 * @title 验证Sessionkey,并获得用户信息
	 * 
	 * @param enterpriseid
	 * @param sessionKey
	 * @param platformId
	 * @return
	 * 
	 * @author 程立涛
	 * @date 2015-4-24 11:27
	 */

	@RequestMapping(value = "/userInfo")
	@ResponseBody
	public String userInfo(String enterpriseid, String sessionKey,
			String platformId) {
		String jsonStr = "";
		try {
			SsoUserVo userVo = null;
			try {
				userVo = ssoServerService.getLoginUserInfo(enterpriseid, sessionKey);
				String groupStr = groupService.getAgentGroupsStr(enterpriseid, userVo.getLoginName());
				userVo.setGroup(groupStr);
			} catch (ServiceException e) {
				LoginLogUtils.LoginFail(sessionKey, platformId, e);
                e.printStackTrace();
			}
			jsonStr = JsonUtils.toJson(userVo);
			jsonStr = URLEncoder.encode(jsonStr, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			LoginLogUtils.LoginFail(sessionKey, platformId,
					new ServiceException(e.getMessage()));
		}
		return jsonStr;
	}

	/**
	 * @title 查询菜单列表
	 * 
	 * @param enterpriseid
	 * @param sessionKey
	 * @param platformId
	 * @return
	 * 
	 * @author 程立涛
	 * @date 2015-4-24 17:24
	 */

	@RequestMapping(value = "/menuList")
	@ResponseBody
	public String menuList(String enterpriseid, String roleId) {
		String jsonStr = "";
		List<SsoPermissionVo> menuList = null;
		try {
			try {
				menuList = permissionService.getMenuList(enterpriseid, roleId);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
			jsonStr = JsonUtils.toJson(menuList);
			jsonStr = URLEncoder.encode(jsonStr, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();

		}
		return jsonStr;
	}

	/**
	 * @title 查询权限列表
	 * 
	 * @param enterpriseid
	 * @param sessionKey
	 * @param platformId
	 * @return
	 * 
	 * @author 程立涛
	 * @date 2015-4-24 17:27
	 */

	@RequestMapping(value = "/privilegeList")
	@ResponseBody
	public String privilegeList(String enterpriseid, String roleId) {
		String jsonStr = "";
		Map<String, String> privilegeList = null;
		try {
			try {
				privilegeList = permissionService.getPrivilege(enterpriseid, roleId);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
			jsonStr = JsonUtils.toJson(privilegeList);
			jsonStr = URLEncoder.encode(jsonStr, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();

		}
		return jsonStr;
	}

	/**
	 * SSO登出 1. 用户点击登出按钮 2. Session失效时自动调用
	 * 
	 * @param sessionkey
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/logout")
	@ResponseBody
	public AjaxResultPo logout(String enterpriseid, String sessionKey,
			String platformId) throws Exception {
		try {
			String loginName = ssoServerService.logout(enterpriseid, sessionKey, platformId);
			LoginLogUtils.LogoutSuccess(loginName, platformId);
		} catch (ServiceException e) {
			LoginLogUtils.LogoutFail(sessionKey, platformId, e);
		}
		return BaseConstants.SUCCESS;
	}
	
	/**
     * 检测账号是否被二次登录
     * @date: 2014-03-24
     * @Author xugan
     */
    @RequestMapping(value = "/checkLoginName")
    @ResponseBody
    public AjaxResultPo checkLoginName(HttpServletRequest request) throws Exception{
        try {
            SsoUserVo userVo = SsoSessionUtils.getUserInfo(request);
            if(userVo==null) return new AjaxResultPo(false, "会话过期");
            String key = SsoSessionUtils.loginMap.get(userVo.getLoginName() + "_" + userVo.getEntId());
            String sKey = SsoSessionUtils.getSessionKey(request);
            if(StringUtils.isBlank(key)||!key.equals(sKey))
            {
                return new AjaxResultPo(false, "该账号在别处登录，你已被迫下线");
            }
        } catch (ServiceException e) {
        }
        return BaseConstants.SUCCESS;
    }
    /**
     * 为SSO FILTER提供的自动登陆方法
     * @param enterpriseid
     * @param sessionKey
     * @param loginName
     * @param MD5Password
     * @param platformId
     * @param request
     * @return
     */
    @RequestMapping(value = "/loginForFilter")
	@ResponseBody
	public boolean loginForFilter(String enterpriseid, String sessionKey,String loginName, String MD5Password, String platformId,HttpServletRequest request) {
    	try{
    		LoginResultVo result = ssoServerService.login(enterpriseid, loginName,MD5Password, SsoParamConstants.PLATFORM_ID, request);
    		SsoSessionUtils.login(result, request);
    		LoginLogUtils.LoginSuccess(loginName, SsoParamConstants.PLATFORM_ID);
    		return true;
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return false;
	}
}

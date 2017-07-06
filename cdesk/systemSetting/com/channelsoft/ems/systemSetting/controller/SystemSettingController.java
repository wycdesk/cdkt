package com.channelsoft.ems.systemSetting.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.logger.ManageLogUtils;
import com.channelsoft.cri.util.DomainUtils;
import com.channelsoft.cri.vo.AjaxResultPo;
import com.channelsoft.ems.privilege.service.ICfgPermissionService;
import com.channelsoft.ems.privilege.vo.CfgPermissionVo;
import com.channelsoft.ems.sso.util.SsoSessionUtils;
import com.channelsoft.ems.sso.vo.SsoEntInfoVo;
import com.channelsoft.ems.sso.vo.SsoUserVo;
import com.channelsoft.ems.systemSetting.service.ICancelSettingService;

@Controller
@RequestMapping("/systemSetting")
public class SystemSettingController {

	
	@Autowired
	ICfgPermissionService permissionService;
	
	@Autowired
	ICancelSettingService cancelService;
	
	
    /**
     * 系统设置
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/index")
	public String systemSetting(HttpServletRequest request, HttpServletResponse response,Model model){
    	
    	String enterpriseid=DomainUtils.getEntId(request);
   	    model.addAttribute("entId", enterpriseid);
   	
   	    String sessionKey = SsoSessionUtils.getSessionKey(request);
   		String roleId = SsoSessionUtils.getUserInfo(request).getRoleId();
   		List<CfgPermissionVo> list = permissionService.querySubMenuTree(enterpriseid, roleId, sessionKey, "16"); // 4是“系统设置2级菜单”权限
   		boolean hasGroup1 = false;
		boolean hasGroup2 = false;
		boolean hasGroup3 = false;
		for (CfgPermissionVo vo : list) {
			if ("1".equals(vo.getGroupId())) {
				hasGroup1 = true;
			}
			if ("2".equals(vo.getGroupId())) {
				hasGroup2 = true;
			}
			if ("3".equals(vo.getGroupId())) {
				hasGroup3 = true;
			}
		}
		model.addAttribute("permissionList", list);
		model.addAttribute("hasGroup1", hasGroup1);
		model.addAttribute("hasGroup2", hasGroup2);
		model.addAttribute("hasGroup3", hasGroup3);
   
		return "systemSetting/index";
	}
    @RequestMapping("/enterpriseCancel")
    public String enterpriseCancel(HttpServletRequest request,Model model){
    	SsoEntInfoVo entInfo = SsoSessionUtils.getEntInfo(request);
    	SsoUserVo user = SsoSessionUtils.getUserInfo(request);
    	model.addAttribute("entId", entInfo.getEntId());
    	model.addAttribute("userType", user.getUserType());
    	return "systemSetting/enterpriseCancel";
    }
    @ResponseBody
    @RequestMapping("/goCancel")
    public AjaxResultPo goCancel(HttpServletRequest request,String entId,String userType) throws JSONException{
    	int cancel=0;
    	try {
			cancel=cancelService.goCancel(request,entId,userType);
		} catch (ServiceException e) {
			e.printStackTrace();
			return AjaxResultPo.failed(e);
		}catch(Exception e){
			e.printStackTrace();
			return AjaxResultPo.failed(e);
		}
    	if(cancel>0){
    		ManageLogUtils.DeleteSuccess(request, "注销企业", entId, "entId="+entId);
    		return AjaxResultPo.success("销户成功", 1, entId);
    	}else{
    		return AjaxResultPo.failed(new Exception("销户失败"));
    	}
    	
    }
}

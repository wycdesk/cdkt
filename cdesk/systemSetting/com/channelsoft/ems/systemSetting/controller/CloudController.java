package com.channelsoft.ems.systemSetting.controller;


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.channelsoft.cri.vo.AjaxResultPo;
import com.channelsoft.cri.vo.PageInfo;
import com.channelsoft.ems.ent.service.IDatEntService;
import com.channelsoft.ems.log.constants.LogTypeEnum;
import com.channelsoft.ems.log.po.CfgUserOperateLogPo;
import com.channelsoft.ems.log.service.ILogMongoService;
import com.channelsoft.ems.sso.util.SsoSessionUtils;
import com.channelsoft.ems.sso.vo.SsoEntInfoVo;
import com.channelsoft.ems.sso.vo.SsoUserVo;



@Controller
@RequestMapping("/cloud")
public class CloudController {
	
	@Autowired
	IDatEntService entService;

    /**
     * 云客服帐号-增值服务
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/account")
	public String account(HttpServletRequest request, Model model){
		return "systemSetting/cloud_account";
	}
    
    /**
     * 云客服帐号-公司信息
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/company")
	public String company(HttpServletRequest request, Model model){
    	SsoEntInfoVo entOldVo = SsoSessionUtils.getEntInfo(request);
    	SsoEntInfoVo entNowVo = entService.query(entOldVo.getEntId());
    	model.addAttribute("entVo", entNowVo);
		return "systemSetting/cloud_company";
	}
    
    /**
     * 云客服帐号-财务信息
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/financial")
	public String financial(HttpServletRequest request, Model model){
		return "systemSetting/cloud_financial";
	}
    
    /**
     * 云客服帐号-发票管理
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/invoice")
	public String invoice(HttpServletRequest request, Model model){
		return "systemSetting/cloud_invoice";
	}
    
    /**
     * 云客服帐号-推广计划
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/refer")
	public String refer(HttpServletRequest request, Model model){
		return "systemSetting/cloud_refer";
	}
}

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
import com.channelsoft.ems.log.constants.LogTypeEnum;
import com.channelsoft.ems.log.po.CfgUserOperateLogPo;
import com.channelsoft.ems.log.service.ILogMongoService;
import com.channelsoft.ems.sso.util.SsoSessionUtils;
import com.channelsoft.ems.sso.vo.SsoUserVo;



@Controller
@RequestMapping("/manageLog")
public class ManageLogController {

	   @Autowired
	   ILogMongoService logMongoService;
	
    /**
     * 管理日志
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/index")
	public String index(HttpServletRequest request, HttpServletResponse response,Model model){
    	String type=request.getParameter("type");
    	if(StringUtils.isBlank(type)){
    		type="all";
    	}
    	model.addAttribute("type", type);
		return "systemSetting/manageLog_all";
	}
    /**
     * 查询日志
     * @param rows
     * @param page
     * @param request
     * @return
     */
    @ResponseBody
	@RequestMapping(value = "/query")
	public AjaxResultPo query(int rows, int page, HttpServletRequest request){
    	try{
    		PageInfo pageInfo = new PageInfo((page-1)*rows, rows);
    		CfgUserOperateLogPo po=new CfgUserOperateLogPo();
    		SsoUserVo user=SsoSessionUtils.getUserInfo(request);
    		po.setEntId(user.getEntId());
    		String type=request.getParameter("type");
    		
    		if("login".equals(type)){
    			po.setOperation(LogTypeEnum.LOGIN.value);
    		}
    		else if("edit".equals(type)){
    			po.setOperation(LogTypeEnum.UPDATE.value);
    		}
    		else if("delete".equals(type)){
    			po.setOperation(LogTypeEnum.DELETE.value);
    		}
    		else{
    			
    		}
    		List<CfgUserOperateLogPo>  list=logMongoService.query(po, pageInfo, user.getEntId());
    		
    		return AjaxResultPo.success("查询成功", pageInfo.getTotalRecords(), list);
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
    	return new AjaxResultPo(false, "查询失败");
	}
}

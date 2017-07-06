package com.channelsoft.ems.workorder.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.channelsoft.cri.logger.SystemLogUtils;
import com.channelsoft.cri.util.JsonStrUtils;
import com.channelsoft.cri.vo.AjaxResultPo;
import com.channelsoft.ems.api.controller.UserApiController;
import com.channelsoft.ems.group.po.AgentPo;
import com.channelsoft.ems.group.po.GroupPo;
import com.channelsoft.ems.group.service.IGroupService;
import com.channelsoft.ems.user.po.DatEntUserPo;
import com.channelsoft.ems.user.service.IUsrManageService;

@Controller
@RequestMapping("/workInfo")
public class WorkOrderController {

	@Autowired
	IUsrManageService usrManageService;
	
	@Autowired
	IGroupService groupService;
	
	private Logger logger=Logger.getLogger(WorkOrderController.class);
	
	/**
	 * 查询坐席的分组
	 * @param request
	 * @return
	 * @throws JSONException
	 * @throws UnsupportedEncodingException 
	 */
	@ResponseBody
	@RequestMapping(value = "/queryAgentGroup")
	public AjaxResultPo queryAgentGroup(HttpServletRequest request) throws JSONException, UnsupportedEncodingException{
		String userId=request.getParameter("userId");
		String entId=request.getParameter("entId");
		SystemLogUtils.Debug("UserApiController.queryAgentGroup,userId="+userId+",entId="+entId);
		try {
			   if(userId == null || entId == null){
				   throw  new Exception();
			   }
				DatEntUserPo user=new DatEntUserPo();
				user.setUserId(userId);
				List<GroupPo> group=groupService.getAgentGroups(entId, user);
				return AjaxResultPo.success("查询成功", group.size(), group);
	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return AjaxResultPo.failed(new Exception("查询失败"));
		}
	}
	
	/**
	 * 查询用户组内坐席
	 * @param request
	 * @return
	 * @throws JSONException
	 * @throws UnsupportedEncodingException 
	 */
	@ResponseBody
	@RequestMapping(value = "/queryGroupAgent",produces="text/json;charset=UTF-8")
	public AjaxResultPo queryGroupAgent(HttpServletRequest request) throws JSONException, UnsupportedEncodingException{
		String groupId=request.getParameter("groupId");
		String entId=request.getParameter("entId");
		SystemLogUtils.Debug("UserApiController.queryGroupAgent,groupId="+groupId+",entId="+entId);
		
		try {
			  if(groupId == null || entId == null){
				   throw  new Exception();
			   }
				List<AgentPo> list=groupService.queryGroupAgent(entId, groupId);
				return AjaxResultPo.success("查询成功", list.size(), list);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return AjaxResultPo.failed(new Exception("查询失败"));
		}
		
	}
	
	
}

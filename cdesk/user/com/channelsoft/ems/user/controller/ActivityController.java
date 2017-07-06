package com.channelsoft.ems.user.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.channelsoft.cri.util.DBObjectUtils;
import com.channelsoft.cri.util.WebappConfigUtil;
import com.channelsoft.ems.redis.util.ParamUtils;
import com.channelsoft.ems.ent.po.DatEntInfoPo;
import com.channelsoft.ems.group.constant.GroupType;
import com.channelsoft.ems.group.po.GroupPo;
import com.channelsoft.ems.sso.util.SsoSessionUtils;
import com.channelsoft.ems.sso.vo.SsoUserVo;
import com.channelsoft.ems.user.po.DatEntUserPo;
import com.channelsoft.ems.user.service.IUserMongoService;
import com.channelsoft.ems.user.service.IUserService;
import com.channelsoft.ems.user.util.PhotoUrlUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

@Controller
@RequestMapping("/activity")
public class ActivityController {

	@Autowired
	IUserService userService;

	@Autowired
	IUserMongoService userMongoService;
	
	  public String getSkillId(String entId){
	    	List<GroupPo> glist=ParamUtils.getEntGroupList(entId);
	    	String groupId="";
	    	for(GroupPo g:glist){
	    		if(GroupType.IM.value.equals(g.getGroupType())){
	    			groupId=g.getGroupId();
	    			break;
	    		}
	    	}
	    	return groupId;
	    }
	  
	/**
	 * 进入我的活动界面
	 * @param request
	 * @param model
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/my")
	public String myActivity(HttpServletRequest request, Model model,HttpServletResponse response) {
		
		String sessionKey = SsoSessionUtils.getSessionKey(request);
		SsoUserVo user0=SsoSessionUtils.getUserInfo(request);
		
		DatEntUserPo user=userMongoService.queryUserByLoginName( user0.getLoginName(),user0.getEntId());
		PhotoUrlUtil.getPhotoUrl(request, model, user.getEntId(), user.getPhotoUrl());
		DatEntInfoPo entPo = ParamUtils.getEntInfo(user.getEntId());
		model.addAttribute("ccodEntId", entPo.getCcodEntId());
		model.addAttribute("skillId",getSkillId(user.getEntId()));
		model.addAttribute("user", user);
		model.addAttribute("enterpriseid", user.getEntId());
		model.addAttribute("entName", user.getEntName());
		model.addAttribute("sessionKey", sessionKey);
		String workPath=WebappConfigUtil.getParameter("workPath");
		model.addAttribute("workPath", workPath);
		model.addAttribute("IM_ROOT", WebappConfigUtil.getParameter("IM_ROOT"));
		return "myActivity";
	   
	}
}

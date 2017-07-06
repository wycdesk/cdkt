package com.channelsoft.ems.user.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.channelsoft.cri.util.DBObjectUtils;
import com.channelsoft.cri.util.DomainUtils;
import com.channelsoft.cri.util.WebappConfigUtil;
import com.channelsoft.ems.ent.service.IDatEntService;
import com.channelsoft.ems.log.constants.BusinessTypeEnum;
import com.channelsoft.ems.log.constants.LogTypeEnum;
import com.channelsoft.ems.log.service.ILogMongoService;
import com.channelsoft.ems.register.util.LogoUrlUtil;
import com.channelsoft.ems.sso.util.SsoCookieUtils;
import com.channelsoft.ems.sso.util.SsoSessionUtils;
import com.channelsoft.ems.sso.vo.SsoEntInfoVo;
import com.channelsoft.ems.sso.vo.SsoUserVo;
import com.channelsoft.ems.user.po.DatEntUserPo;
import com.channelsoft.ems.user.service.IUserMongoService;
import com.channelsoft.ems.user.service.IUserService;
import com.channelsoft.ems.user.util.PhotoUrlUtil;
import com.mongodb.DBObject;


@Controller
@RequestMapping("/check")
public class CheckProblemsController {

	@Autowired
    IDatEntService datEntService;
	
	@Autowired
    ILogMongoService logMongoService;
	
	@Autowired
	IUserService userService;
	@Autowired
	IUserMongoService userMongoService;
	/**
	 * 进入查看问题页面
	 */
	@RequestMapping("/problem/{workId}")
	public String gotoCheckProble(@PathVariable("workId") String workId ,HttpServletRequest request, Model model,HttpServletResponse response){
		
		SsoUserVo user = SsoSessionUtils.getUserInfo(request);
		user.setSessionKey(SsoSessionUtils.getSessionKey(request));
		
		String entId = user.getEntId();
		//获取平台LOGO和Favicon
		SsoEntInfoVo logoEnt=datEntService.query(DomainUtils.getEntId(request));
		LogoUrlUtil.getLogoUrl(request, model, entId, logoEnt.getLogoUrl(), logoEnt.getFaviconUrl());
		
		model.addAttribute("user", user);
		model.addAttribute("enterpriseid", entId);
		model.addAttribute("entName", user.getEntName());
		 //获取用户头像
//		DatEntUserPo userPo = userService.queryUser(entId, user.getUserId());
		DBObject dbo=userMongoService.queryById(entId, user.getUserId());
	    DatEntUserPo userPo=new DatEntUserPo();
		DBObjectUtils.getObject(dbo, userPo);
	    PhotoUrlUtil.getPhotoUrl(request, model, entId, userPo.getPhotoUrl());
		logMongoService.add(request, LogTypeEnum.LOGIN, BusinessTypeEnum.ELSE, "", "", user);
		
		String workPath=WebappConfigUtil.getParameter("workPath");
		model.addAttribute("workPath", workPath);

		model.addAttribute("workId", workId);
		return "checkProblems";
	}
}

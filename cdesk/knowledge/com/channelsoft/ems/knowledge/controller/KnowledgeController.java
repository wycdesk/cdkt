package com.channelsoft.ems.knowledge.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.channelsoft.ems.sso.util.SsoSessionUtils;





@Controller
@RequestMapping("/knowledge")
public class KnowledgeController {

	
	/**
	 * 进去发布知识库文档界面
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	 @RequestMapping(value = "/goPublish")
	 public String goPublish(HttpServletRequest request, HttpServletResponse response, Model model){
	 
	    String userId=SsoSessionUtils.getUserInfo(request).getUserId();
	    String userName=SsoSessionUtils.getUserInfo(request).getUserName();
	    
	    request.setAttribute("userId", userId);
	    request.setAttribute("userName", userName);
	    
	    return "help/publish";
	 }
	 
	 /**
		 * 进去发布知识库文档界面
		 * @param request
		 * @param response
		 * @param model
		 * @return
		 */
		 @RequestMapping(value = "/goManage")
		 public String goManage(HttpServletRequest request, HttpServletResponse response, Model model){
		 
		    
		    return "help/manage";
		 }
	
}

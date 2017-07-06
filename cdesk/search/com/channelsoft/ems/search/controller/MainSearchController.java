package com.channelsoft.ems.search.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.channelsoft.cri.cache.constant.WorkPriority;
import com.channelsoft.cri.cache.constant.WorkType;
import com.channelsoft.cri.logger.SystemLogUtils;
import com.channelsoft.cri.util.Converter;
import com.channelsoft.cri.vo.AjaxResultPo;
import com.channelsoft.cri.vo.PageInfo;
import com.channelsoft.ems.search.service.ISearchMongoService;
import com.channelsoft.ems.sso.util.SsoSessionUtils;
import com.channelsoft.ems.sso.vo.SsoUserVo;
import com.channelsoft.ems.user.service.IUserMongoService;
import com.channelsoft.ems.user.service.IUsrManageService;
import com.channelsoft.ems.user.util.PhotoUrlUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;


@Controller
@RequestMapping("/msearch")
public class MainSearchController {
	@Autowired
	IUsrManageService usrManageService;
	
	@Autowired
	ISearchMongoService searchMongoService;
	
	@Autowired
	IUserMongoService userMongoService;
	
	/**
	 * 主界面搜索
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	 @RequestMapping(value = "/")
	 public String search(HttpServletRequest request, HttpServletResponse response, Model model){
	    String key=request.getParameter("key");
	    String type=request.getParameter("type"); 
	    
		SsoUserVo user=SsoSessionUtils.getUserInfo(request);
		String entId=user.getEntId();
	    String userId=user.getUserId();
	    String userName=user.getUserName();
	    
	    model.addAttribute("key", key);
	    model.addAttribute("type", type);
	    model.addAttribute("entId",entId);
	    model.addAttribute("userId", userId);
	    model.addAttribute("userName", userName);
		
	    /*是否为创始人*/
		DBObject queryObj=new BasicDBObject();
		queryObj.put("entId", entId);
		queryObj.put("userId", userId);
		List<DBObject>  list=userMongoService.queryUserDetail(queryObj, null);
		String isFounder=list.get(0).get("founder")+"";
	    
		model.addAttribute("isFounder",isFounder);
	    
	    String userType1=SsoSessionUtils.getUserInfo(request).getUserType();
	    request.setAttribute("userType1", userType1);	
	    
	    return "mainSearch";
	 }
	 
	 
	/*用户模糊搜索*/
	@ResponseBody
	@RequestMapping(value = "/fuzzyQueryUsers")
	public AjaxResultPo fuzzyQueryUsers(String keyword, String startTime, String endTime, String searchType, HttpServletRequest request,int rows, int page) {
		PageInfo pageInfo = new PageInfo((page-1)*rows, rows);	
		SsoUserVo user=SsoSessionUtils.getUserInfo(request);
		String entId=user.getEntId();
		
		if(startTime!="" && endTime!=""){
			startTime = startTime + " 00:00:00";
			endTime = endTime + " 23:59:59";
		}
		
	    if(searchType.equals("模糊搜索")){
	    	searchType="";
	    }
	    if(searchType.equals("邮箱")){
	    	searchType="email";
	    }
	    if(searchType.equals("手机号")){
	    	searchType="telPhone";
	    }
	    if(searchType.equals("用户名")){
	    	searchType="userName";
	    }
	    if(searchType.equals("昵称")){
	    	searchType="nickName";
	    }
		
		List<DBObject> list=searchMongoService.queryUserList(keyword, entId, startTime, endTime, searchType, pageInfo);
		
		//用户头像处理
		for(DBObject vo : list){		
			String photopath = PhotoUrlUtil.getPhotoPath(request, entId, vo.get("photoUrl")+"");
			vo.put("photoUrl", photopath);
		}		
		for(int i=0;i<list.size();i++){
			if(list.get(i).get("userType").equals("1")){
				list.get(i).put("userType", "普通用户");
			}
			if(list.get(i).get("userType").equals("2")){
				list.get(i).put("userType", "坐席客服");
			}
			if(list.get(i).get("userType").equals("3")){
				list.get(i).put("userType", "管理员");
			}
		}				
		
		request.setAttribute("list", list);
	    
		return AjaxResultPo.success("查询成功", pageInfo.getTotalRecords(), list);
	}
	
	
	/*工单模糊搜索*/
	@ResponseBody
	@RequestMapping(value = "/fuzzyQueryOrders")
	public AjaxResultPo fuzzyQueryOrders(String keyword, String startTime, String endTime, String searchType, HttpServletRequest request,int rows, int page) {
		PageInfo pageInfo = new PageInfo((page-1)*rows, rows);	
		SsoUserVo user=SsoSessionUtils.getUserInfo(request);
		String entId=user.getEntId();
		
		if(startTime!="" && endTime!=""){
			startTime = startTime + " 00:00:00";
			endTime = endTime + " 23:59:59";
		}
		
	    if(searchType.equals("模糊搜索")){
	    	searchType="";
	    }
	    if(searchType.equals("工单编号")){
	    	searchType="workId";
	    }
	    if(searchType.equals("工单标题")){
	    	searchType="title";
	    }
	    if(searchType.equals("工单优先级")){
	    	searchType="priority";
	    }
	    if(searchType.equals("类型")){
	    	searchType="type";
	    }
	    if(searchType.equals("工单发起人")){
	    	searchType="creatorName";
	    }
	    if(searchType.equals("受理客服")){
	    	searchType="customServiceName";
	    }
	    if(searchType.equals("受理客服组")){
	    	searchType="serviceGroupName";
	    }
		
	    List<DBObject> list=new ArrayList<DBObject>();

	    list=searchMongoService.queryOrderList(keyword, entId, startTime, endTime, searchType, pageInfo);
	    		
	    for(DBObject dbo:list){
	    	String priority=dbo.get("priority")+"";
	    	String type=dbo.get("type")+"";
	    	
	    	priority=WorkPriority.getEnum(priority).desc;
	    	type=WorkType.getEnum(type).desc;
	    	
	    	dbo.put("priority", priority);
	    	dbo.put("type", type);
	    }
	    
		request.setAttribute("list", list);
	    
		return AjaxResultPo.success("查询成功", pageInfo.getTotalRecords(), list);
	}
	
}

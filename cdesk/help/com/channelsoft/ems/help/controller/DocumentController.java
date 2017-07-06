package com.channelsoft.ems.help.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.channelsoft.cri.constant.BaseConstants;
import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.logger.ManageLogUtils;
import com.channelsoft.cri.vo.AjaxResultPo;
import com.channelsoft.cri.vo.PageInfo;
import com.channelsoft.ems.api.client.EntClient;
import com.channelsoft.ems.help.service.IDocumentService;
import com.channelsoft.ems.log.constants.BusinessTypeEnum;
import com.channelsoft.ems.log.constants.LogTypeEnum;
import com.channelsoft.ems.sso.util.SsoSessionUtils;
import com.channelsoft.ems.sso.vo.SsoUserVo;
import com.channelsoft.ems.user.po.DatEntUserPo;
import com.mongodb.DBObject;

@Controller
@RequestMapping("/help/document")
public class DocumentController {

	@Autowired
	IDocumentService documentMongoService;
	
	
	/*文档查询*/
	@ResponseBody
	@RequestMapping(value = "/queryDocument")
	public AjaxResultPo queryDocument(String startTime, String endTime, String fastSearch, HttpServletRequest request,int rows, int page) {
		PageInfo pageInfo = new PageInfo((page-1)*rows, rows);	
		SsoUserVo user=SsoSessionUtils.getUserInfo(request);
		String entId=user.getEntId();
		
		startTime = startTime + "00:00:00";
		endTime = endTime + "23:59:59";
		
	    List<DBObject> list=new ArrayList<DBObject>();

	    list=documentMongoService.queryDocument(entId, startTime, endTime, fastSearch, pageInfo);
	    
		request.setAttribute("list", list);
		
		return AjaxResultPo.success("查询成功", pageInfo.getTotalRecords(), list);
	}
	
	
	/*根据Id查询文档*/
	@RequestMapping(value = "/queryDocById")
	public String queryDocById(String docId, HttpServletRequest request, HttpServletResponse response) throws Exception {
		SsoUserVo user=SsoSessionUtils.getUserInfo(request);
		String entId=user.getEntId();
		String isEdit="true";
		
	    List<DBObject> list=new ArrayList<DBObject>();

	    list=documentMongoService.queryDocById(entId, docId, null);
	    
	    String userId=user.getUserId();
	    String userName=user.getUserName();
	    
	    request.setAttribute("userId", userId);
	    request.setAttribute("userName", userName);
	    
	    request.setAttribute("docId", docId);
		request.setAttribute("doc", list.get(0));
		request.setAttribute("isEdit", isEdit);
	    
		return "help/publish";
	}
	
	
	/*删除文档*/
	@ResponseBody
	@RequestMapping(value = "/deleteDocument")
	public AjaxResultPo deleteDocument(String[] ids, HttpServletRequest request) throws Exception {
		try {
			SsoUserVo user=SsoSessionUtils.getUserInfo(request);
			String entId=user.getEntId();
			
			int success=documentMongoService.deleteDocument(entId, ids);
			if(success>0)
			{							
				return AjaxResultPo.successDefault();	
			}
			else{
				return AjaxResultPo.failed(new Exception("删除文档失败"));
			}				
		} catch (ServiceException e) {			
			e.printStackTrace();
			return new AjaxResultPo(false, e.getMessage());
		}
	}
	
	/*我发布的文档*/
	@ResponseBody
	@RequestMapping(value = "/queryDocBelongMe")
	public AjaxResultPo queryDocBelongMe(HttpServletRequest request, HttpServletResponse response, int rows, int page) {
		PageInfo pageInfo = new PageInfo((page-1)*rows, rows);	
		SsoUserVo user=SsoSessionUtils.getUserInfo(request);
		String entId=user.getEntId();
		String creatorId=user.getUserId();
		
	    List<DBObject> list=new ArrayList<DBObject>();

	    list=documentMongoService.queryDocBelongMe(creatorId, entId, pageInfo);
	    
	    request.setAttribute("queryDocument", list.size());
	    
		return AjaxResultPo.success("查询成功", pageInfo.getTotalRecords(), list);
	}
	
	
	/*文档详情页*/
	@RequestMapping(value = "/viewDetails")
	public String viewDetails(String docId, HttpServletRequest request, HttpServletResponse response) throws Exception {
		SsoUserVo user=SsoSessionUtils.getUserInfo(request);
		String entId=user.getEntId();
		
	    List<DBObject> list=new ArrayList<DBObject>();
	    List<DBObject> attachList=new ArrayList<DBObject>();
	    
	    list=documentMongoService.queryDocById(entId, docId, null);
	    
	    String attachment=list.get(0).get("attachment")+"";
	    
	    List<Integer> attachId=new ArrayList<Integer>();
	    
	    if(!attachment.equals("null")){
	    	String[] idStr=attachment.split(",");
	    	for(int i=0;i<idStr.length;i++){
	    		int aId=Integer.parseInt(idStr[i]);
	    		
	    	    attachId.add(aId);
	    	}   	    	
		    /*当前文档下的附件*/
		    attachList=documentMongoService.queryAttachById(entId, attachId, null);
	    }
	    	    	    
		request.setAttribute("doc", list.get(0));
		request.setAttribute("attachList", attachList);
	    
		return "help/docDetails";
	}
	
	/*获取登录用户类型 */
	@ResponseBody
	@RequestMapping(value = "/getUserType")
	public AjaxResultPo getUserType(HttpServletRequest request, HttpServletResponse response) {
		SsoUserVo user=SsoSessionUtils.getUserInfo(request);
		String userType=user.getUserType();
	    
		return AjaxResultPo.success("查询成功", 1, userType);
	}
	
}

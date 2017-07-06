package com.channelsoft.ems.field.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.vo.AjaxResultPo;
import com.channelsoft.ems.field.po.UserDefinedFiedPo;
import com.channelsoft.ems.field.service.IUserFieldService;
import com.channelsoft.ems.sso.util.SsoSessionUtils;
import com.channelsoft.ems.sso.vo.SsoUserVo;

@Controller
@RequestMapping("/userField")
public class UserFieldController {

	@Autowired
	IUserFieldService userFieldService;
	
	/**
	 * 查询用户自定义字段
	 * @param rows
	 * @param page
	 * @param po
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/query")
	public AjaxResultPo query(UserDefinedFiedPo po, HttpServletRequest request){
    	try{
    		SsoUserVo user=SsoSessionUtils.getUserInfo(request);
    		List<UserDefinedFiedPo> list=userFieldService.queryDefinedFiled(user.getEntId(), "", null);
    		
    		return AjaxResultPo.success("查询成功", list.size(), list);
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
    	return new AjaxResultPo(false, "查询失败");
	}
	
	/*
	 * 用户自定义字段首页
	 */
	@RequestMapping(value = "/userfields")
	public String userFields(HttpServletRequest request,Model model){
		List<UserDefinedFiedPo> activeList=new ArrayList<UserDefinedFiedPo>();
		List<UserDefinedFiedPo> deactiveList=new ArrayList<UserDefinedFiedPo>();
		try{
    		SsoUserVo user=SsoSessionUtils.getUserInfo(request);
    		List<UserDefinedFiedPo> list=userFieldService.queryDefinedFiled(user.getEntId(), "", null);
    		if(list.size()>0){
    			for(int i=0;i<list.size();i++){
    				if(list.get(i).getStatus()!=null){
    					if(list.get(i).getStatus().equals("1")){
        					activeList.add(list.get(i));
        				}
    					if(list.get(i).getStatus().equals("0")){
        					deactiveList.add(list.get(i));
        				}
    				}
    			}
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
		model.addAttribute("activeList", activeList);
		model.addAttribute("deactiveList", deactiveList);
		model.addAttribute("size1", activeList.size());
		model.addAttribute("size2", deactiveList.size());
		return "functionManage/userfields";
	}
	
	/*
	 * 进入用户自定义字段编辑页面
	 */
	@RequestMapping(value = "/edit")
	public String fieldEdit(HttpServletRequest request,String field,Model model){
		SsoUserVo user=SsoSessionUtils.getUserInfo(request);
		UserDefinedFiedPo po=null;
		try {
			po=userFieldService.queryFieldByKey(user.getEntId(),field,"");
		} catch (ServiceException e) {
			e.printStackTrace();
			return "error";
		}
		if(po!=null){
			model.addAttribute("field",po);
			String type=po.getComponentType();
			boolean multp=false;
			if(type!=null){
				if(type.equals("4")||type.equals("3")){
					multp=true;
					model.addAttribute("contentNum","0");
					model.addAttribute("tcolTitle","下拉菜单的选项");
					String[] candidate=po.getCandidateValue();
					JSONObject json=new JSONObject();
					for(int i=0;i<candidate.length;i++){
						try {
							json.put(i+"",  candidate[i]);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					model.addAttribute("candidate", json.toString());
				}else if(type.equals("8")){
					multp=true;
					model.addAttribute("contentNum","1");
					model.addAttribute("tcolTitle","正则匹配字段");
					String[] candidate=po.getCandidateValue();
					if(candidate.length>0){
						model.addAttribute("candidate",candidate[0]);
					}else{
						model.addAttribute("candidate","");
					}
				}			
			}
			model.addAttribute("multp", multp);
			model.addAttribute("type",type);
			
			if(po.getCandidateValue()!=null&&po.getCandidateValue().length>0){
				
			}
		}
		return "functionManage/userfieldEdit";
	}
	
	/*
	 * 编辑用户自定义字段
	 */
	@ResponseBody
	@RequestMapping(value = "/goEdit/{key}")
	public AjaxResultPo goEdit(HttpServletRequest request,String agentTitle,
			String description,String userFieldItems,@PathVariable String key){
		SsoUserVo user=SsoSessionUtils.getUserInfo(request);
		String entId=user.getEntId();
		String[] candidateValue=null;
		if(StringUtils.isNotBlank(userFieldItems)){
			try {
				JSONObject json=new JSONObject(userFieldItems);
				candidateValue=new String[json.length()];
				Iterator iterator = json.keys();
				int i=json.length()-1;
				String k;
				while(iterator.hasNext()){
					k = (String) iterator.next();
					candidateValue[i]= json.getString(k);
					i--;
				}
			} catch (JSONException e) {
				e.printStackTrace();
				return new AjaxResultPo(false, "传递参数有错");
			}
		}
		UserDefinedFiedPo po=new UserDefinedFiedPo();
		po.setKey(key);
		
		String type=request.getParameter("type");		
		String reg="((\\d{11})|^((\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1})|(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1}))$)";
		String[] checklist=new String[]{reg};
				
		if(type.equals("9")){
			po.setCandidateValue(checklist);
		}else{
			po.setCandidateValue(candidateValue);		
		}
		
		po.setName(agentTitle);
		po.setRemark(description);
		int edit=0;
		try {
			edit=userFieldService.goEdit(entId,po);
		} catch (ServiceException e) {
			e.printStackTrace();
			return new AjaxResultPo(false, e.getMessage());
		}
		if(edit>0){
			return new AjaxResultPo(true, "编辑成功");
		}else{
			return new AjaxResultPo(false, "编辑失败");
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/change/{type}/{key}")
	public AjaxResultPo change(HttpServletRequest request,@PathVariable String type,@PathVariable String key){
		SsoUserVo user=SsoSessionUtils.getUserInfo(request);
		String entId=user.getEntId();
		if(type.equals("delete")){
			int del=0;
			try {
				del=userFieldService.deleteField(entId,key);
				if(del>0){
					return AjaxResultPo.success("删除成功", del, null);
				}else{
					return AjaxResultPo.failed(new Exception("删除失败"));
				}
			} catch (ServiceException e) {
				e.printStackTrace();
				return AjaxResultPo.failed(e);
			}
		}else{
			int chg=0;
			try {
				chg=userFieldService.changeMode(entId,type,key);
				if(chg>0){
					return AjaxResultPo.success("更改成功", chg, null);
				}else{
					return AjaxResultPo.failed(new Exception("更改失败"));
				}
			} catch (ServiceException e) {
				e.printStackTrace();
				return AjaxResultPo.failed(e);
			}
		}
	}
	
	@ResponseBody
	@RequestMapping("/ajax")
	public AjaxResultPo ajax(HttpServletRequest request,String type,String ids){
		if(type.equals("sort")){
			SsoUserVo user=SsoSessionUtils.getUserInfo(request);
			String entId=user.getEntId();
			try {
				int suc=userFieldService.sortUserField(entId,ids);
				if(suc>=0){
					return AjaxResultPo.success("排序成功", 0, null);
				}else{
					return AjaxResultPo.failed(new Exception("排序失败"));
				}
			} catch (ServiceException e) {
				e.printStackTrace();
				return AjaxResultPo.failed(e);
			}
		}else{
			return AjaxResultPo.failed(new Exception("unknow type"));
		}
	}
	
	/**
	 * 添加自定义字段
	 * @param po
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/addDefinedField")
	public AjaxResultPo addDefinedField(UserDefinedFiedPo po,String candidateValue, HttpServletRequest request){
    	try{
    		//String candidateValue1=request.getParameter("candidateValue");
    		//po.setCandidateValue(new String[]{ candidateValue});
    		if(candidateValue!=""){
        	    JSONObject json=new JSONObject(candidateValue);
        	    String[] candidateValue1=new String[json.length()];
        	    
    			Iterator iterator = json.keys();
    			int i=json.length()-1;
    			String k;
    			while(iterator.hasNext()){
    				k = (String) iterator.next();
    				candidateValue1[i]= json.getString(k);
    				i--;
    			}   			
    			po.setCandidateValue(candidateValue1);
    		}
    	    			
    		SsoUserVo user=SsoSessionUtils.getUserInfo(request);
    		int num=userFieldService.addDefinedField(po, user.getEntId());
    		
    		String title=po.getName();
    		AjaxResultPo ret=new AjaxResultPo(true,"添加成功");
    		ret.setRows(title);
            return ret;  		 
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
    	return new AjaxResultPo(false, "提交失败");
	}
	   
	/*用户自定义字段类型选择页面*/
	@RequestMapping(value = "/userFieldType")
	public String userFieldType(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception{
		
	    return "functionManage/userFieldType";
	}	
	
	/*添加自定义字段页面*/
	@RequestMapping(value = "/addField")
	public String addField(String type, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception{
		
		request.setAttribute("type", type);
	    return "functionManage/addField";
	}	
	
	/*常见正则表达式范例页面*/
	@RequestMapping(value = "/regularExpression")
	public String regularExpression(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception{

	    return "functionManage/regularExpression";
	}	
}

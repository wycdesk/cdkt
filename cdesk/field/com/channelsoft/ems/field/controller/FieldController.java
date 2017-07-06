package com.channelsoft.ems.field.controller;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import com.channelsoft.ems.field.po.BaseFieldPo;
import com.channelsoft.ems.field.service.IFieldService;
import com.channelsoft.ems.redis.constant.CacheGroup;
import com.channelsoft.ems.redis.util.ParamUtils;
import com.channelsoft.ems.sso.util.SsoSessionUtils;
import com.channelsoft.ems.sso.vo.SsoUserVo;
import com.channelsoft.ems.template.po.TemplatePo;
import com.channelsoft.ems.template.service.ITemplateService;

@Controller
@RequestMapping("/field")
public class FieldController {

	@Autowired
	IFieldService fieldService;
	
	@Autowired
	ITemplateService templateService;
	
	/*
	 * 进入自定义字段编辑页面
	 */
	@RequestMapping(value = "/edit")
	public String fieldEdit(HttpServletRequest request, String key, String tempId, Model model){
		SsoUserVo user=SsoSessionUtils.getUserInfo(request);
		BaseFieldPo po=null;
		try {
			po=fieldService.queryFieldByKey(user.getEntId(), key);
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
			TemplatePo tpo = new TemplatePo();
			tpo.setTempId(tempId);
			List<TemplatePo> temp=templateService.query(user.getEntId(), tpo, null);			
			TemplatePo template = new TemplatePo();
			if(temp.size()>0){
				template = temp.get(0);
			}		
			model.addAttribute("template", template);
			model.addAttribute("tempId", tempId);
			
			model.addAttribute("multp", multp);
			model.addAttribute("type",type);
			
			if(po.getCandidateValue()!=null&&po.getCandidateValue().length>0){
				
			}
		}
		return "functionManage/field/fieldEdit";
	}
	
	
	/*
	 * 编辑自定义字段
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
		BaseFieldPo po=new BaseFieldPo();
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
			edit=fieldService.goEdit(entId,po);
			
			ParamUtils.refreshCache(CacheGroup.WORK_TEMPLATE_FIELD, entId);
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
	
    /*自定义字段的启停用和删除（逻辑删除）*/
	@ResponseBody
	@RequestMapping(value = "/change/{type}/{key}")
	public AjaxResultPo change(HttpServletRequest request,@PathVariable String type,@PathVariable String key){
		SsoUserVo user=SsoSessionUtils.getUserInfo(request);
		String entId=user.getEntId();
		if(type.equals("delete")){
			int del=0;
			try {
				del=fieldService.deleteField(entId,key);
				if(del>0){
					ParamUtils.refreshCache(CacheGroup.WORK_TEMPLATE_FIELD, entId);
					
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
				chg=fieldService.changeMode(entId,type,key);
				if(chg>0){
					ParamUtils.refreshCache(CacheGroup.WORK_TEMPLATE_FIELD, entId);
					
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
	
	/*自定义字段移动排序*/
	@ResponseBody
	@RequestMapping("/ajax")
	public AjaxResultPo ajax(HttpServletRequest request,String type,String ids, String tempId){
		if(type.equals("sort")){
			SsoUserVo user=SsoSessionUtils.getUserInfo(request);
			String entId=user.getEntId();
			try {
				int suc=fieldService.sortField(entId, tempId, ids);
				if(suc>=0){
					ParamUtils.refreshCache(CacheGroup.WORK_TEMPLATE_FIELD, entId);
					
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
	
}

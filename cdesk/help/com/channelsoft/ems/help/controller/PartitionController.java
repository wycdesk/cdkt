package com.channelsoft.ems.help.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.util.DomainUtils;
import com.channelsoft.cri.vo.AjaxResultPo;
import com.channelsoft.ems.help.constant.PublishDocPo;
import com.channelsoft.ems.help.po.PartitionPo;
import com.channelsoft.ems.help.po.PartitionSubPo;
import com.channelsoft.ems.help.service.IPartitionService;
import com.channelsoft.ems.sso.util.SsoSessionUtils;

@Controller
@RequestMapping("/help/partition")
public class PartitionController {
	@Autowired
	IPartitionService partitionService;
	
	@ResponseBody
	@RequestMapping("/getPartition")
	public AjaxResultPo getPartition(HttpServletRequest request,PartitionPo po){
		String entId=DomainUtils.getEntId(request);
		if(StringUtils.isBlank(entId)){
			return AjaxResultPo.failed(new Exception("企业名称为空"));
		}
		List<PartitionPo> partitions=null;
		try {
			partitions=partitionService.getDetailPartition(entId,po,true);
		} catch (ServiceException e) {
			e.printStackTrace();
			return AjaxResultPo.failed(new Exception(e.getMessage()));
		}
		return AjaxResultPo.success("查询成功", partitions==null?-1:partitions.size(), partitions);
	}
	
	@ResponseBody
	@RequestMapping("/addPartition")
	public AjaxResultPo addPartition(HttpServletRequest request,PartitionPo po){
		String entId=DomainUtils.getEntId(request);
		if(StringUtils.isBlank(entId)){
			return AjaxResultPo.failed(new Exception("企业名称为空"));
		}
		int add=0;
		try {
			add=partitionService.addPartition(entId,po);
		} catch (ServiceException e) {
			e.printStackTrace();
			return AjaxResultPo.failed(e);
		}
		if(add<=0){
			return AjaxResultPo.failed(new Exception("分区添加失败"));
		}
		return AjaxResultPo.success("分区添加成功", add, null);
	}
	
	@ResponseBody
	@RequestMapping("/addPartitionSub")
	public AjaxResultPo addPartitionSub(HttpServletRequest request,PartitionSubPo po){
		String entId=DomainUtils.getEntId(request);
		if(StringUtils.isBlank(entId)){
			return AjaxResultPo.failed(new Exception("企业名称为空"));
		}
		int add=0;
		try {
			add=partitionService.addPartitionSub(entId,po);
		} catch (ServiceException e) {
			e.printStackTrace();
			return AjaxResultPo.failed(e);
		}
		if(add<=0){
			return AjaxResultPo.failed(new Exception("分区子类添加失败"));
		}
		return AjaxResultPo.success("分区子类添加成功", add, null);
	}
	
	@ResponseBody
	@RequestMapping("/editPartition")
	public AjaxResultPo editPartition(HttpServletRequest request,PartitionPo po){
		String entId=DomainUtils.getEntId(request);
		if(StringUtils.isBlank(entId)){
			return AjaxResultPo.failed(new Exception("企业名称为空"));
		}
		int edit=0;
		try {
			edit=partitionService.editPartition(entId,po);
		} catch (ServiceException e) {
			e.printStackTrace();
			return AjaxResultPo.failed(e);
		}
		if(edit<=0){
			return AjaxResultPo.failed(new Exception("分区编辑失败"));
		}
		return AjaxResultPo.success("分区编辑成功", edit, null);
	}
	
	@ResponseBody
	@RequestMapping("/editPartitionSub")
	public AjaxResultPo editPartitionSub(HttpServletRequest request,PartitionSubPo po){
		String entId=DomainUtils.getEntId(request);
		if(StringUtils.isBlank(entId)){
			return AjaxResultPo.failed(new Exception("企业名称为空"));
		}
		int edit=0;
		try {
			edit=partitionService.editPartitionSub(entId,po);
		} catch (ServiceException e) {
			e.printStackTrace();
			return AjaxResultPo.failed(e);
		}
		if(edit<=0){
			return AjaxResultPo.failed(new Exception("分区子类编辑失败"));
		}
		return AjaxResultPo.success("分区子类编辑成功", edit, null);
	}
	
	@ResponseBody
	@RequestMapping("/deletePartition")
	public AjaxResultPo deletePartition(HttpServletRequest request,PartitionPo po){
		String entId=DomainUtils.getEntId(request);
		if(StringUtils.isBlank(entId)){
			return AjaxResultPo.failed(new Exception("企业名称为空"));
		}
		if(StringUtils.isBlank(po.getId())){
			return AjaxResultPo.failed(new Exception("分区id为空"));
		}
		int del=0;
		try {
			del=partitionService.deletePartition(entId,po);
		} catch (ServiceException e) {
			e.printStackTrace();
			return AjaxResultPo.failed(e);
		}
		if(del<=0){
			return AjaxResultPo.failed(new Exception("分区删除失败"));
		}
		return AjaxResultPo.success("分区删除成功", del, null);
	}
	
	@ResponseBody
	@RequestMapping("/deletePartitionSub")
	public AjaxResultPo deletePartitionSub(HttpServletRequest request,PartitionSubPo po){
		String entId=DomainUtils.getEntId(request);
		if(StringUtils.isBlank(entId)){
			return AjaxResultPo.failed(new Exception("企业名称为空"));
		}
		if(StringUtils.isBlank(po.getId())){
			return AjaxResultPo.failed(new Exception("分区子类id为空"));
		}
		int del=0;
		try {
			del=partitionService.deletePartitionSub(entId,po);
		} catch (ServiceException e) {
			e.printStackTrace();
			return AjaxResultPo.failed(e);
		}
		if(del<=0){
			return AjaxResultPo.failed(new Exception("分区子类删除失败"));
		}
		return AjaxResultPo.success("分区子类删除成功", del, null);
	}
	
	@ResponseBody
	@RequestMapping("/goPublish")
	public AjaxResultPo goPublish(HttpServletRequest request,PublishDocPo doc){
		if(StringUtils.isBlank(doc.getTitle())){
			return AjaxResultPo.failed(new Exception("标题不能为空"));
		}
		if(StringUtils.isBlank(doc.getContent())){
			return AjaxResultPo.failed(new Exception("内容不能为空"));
		}
		String entId=DomainUtils.getEntId(request);
		int pub=-1;
		try {
			doc.setCreatorId(SsoSessionUtils.getUserInfo(request).getUserId());
			doc.setAuthor(SsoSessionUtils.getUserInfo(request).getUserName());
			pub=partitionService.goPublish(entId,doc);
		} catch (ServiceException e) {
			e.printStackTrace();
			return AjaxResultPo.failed(e);
		}
		if(pub<0){
			return AjaxResultPo.failed(new Exception("发布失败"));
		}
		return AjaxResultPo.successDefault();
	}
	
	
	/*编辑文档*/
	@ResponseBody
	@RequestMapping("/goEdit")
	public AjaxResultPo goEdit(HttpServletRequest request,PublishDocPo doc){
		if(StringUtils.isBlank(doc.getTitle())){
			return AjaxResultPo.failed(new Exception("标题不能为空"));
		}
		if(StringUtils.isBlank(doc.getContent())){
			return AjaxResultPo.failed(new Exception("内容不能为空"));
		}
		String entId=DomainUtils.getEntId(request);
		int pub=-1;
		try {
			pub=partitionService.goEdit(entId,doc);
		} catch (ServiceException e) {
			e.printStackTrace();
			return AjaxResultPo.failed(e);
		}
		if(pub<0){
			return AjaxResultPo.failed(new Exception("更新失败"));
		}
		return AjaxResultPo.successDefault();
	}
}

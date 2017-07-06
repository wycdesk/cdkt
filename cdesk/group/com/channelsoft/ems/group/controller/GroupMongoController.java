package com.channelsoft.ems.group.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.channelsoft.cri.exception.BaseException;
import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.logger.ManageLogUtils;
import com.channelsoft.cri.logger.SystemLogUtils;
import com.channelsoft.cri.util.DomainUtils;
import com.channelsoft.cri.vo.AjaxResultPo;
import com.channelsoft.cri.vo.PageInfo;
import com.channelsoft.ems.api.client.CCODClient;
import com.channelsoft.ems.api.client.EntClient;
import com.channelsoft.ems.api.po.CCODResponsePo;
import com.channelsoft.ems.redis.constant.CacheGroup;
import com.channelsoft.ems.redis.util.ParamUtils;
import com.channelsoft.ems.group.po.AgentReturnPo;
import com.channelsoft.ems.group.po.GroupPo;
import com.channelsoft.ems.group.po.GroupReturnPo;
import com.channelsoft.ems.group.service.IGroupMongoService;
import com.channelsoft.ems.group.service.IGroupService;
import com.channelsoft.ems.sso.util.SsoSessionUtils;
import com.channelsoft.ems.sso.vo.SsoUserVo;
import com.channelsoft.ems.user.po.DatEntUserPo;
import com.channelsoft.ems.user.util.PhotoUrlUtil;

@Controller
@RequestMapping("/groupMongo")
public class GroupMongoController {

	@Autowired
	IGroupMongoService groupMongoService;
	@Autowired
	IGroupService groupService;
	/**
	 * 查询所有客服组
	 * @param request
	 * @param po
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/allGroups")
	public AjaxResultPo allGroups(HttpServletRequest request,GroupPo po){
		String entId=DomainUtils.getEntId(request);
		AjaxResultPo ret = new AjaxResultPo(true, "成功");
		List<GroupPo> groups=groupService.queryUserGroup(request, entId, po);
		ret.setRows(groups);
		return ret;
	}
	/**
	 * 查询所有客服组信息(不分页)
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getAllGroup")
	public AjaxResultPo getAllGroup(HttpServletRequest request){
		String entId=DomainUtils.getEntId(request);
		try {
			List<GroupPo> list0 = ParamUtils.getEntGroupList(entId);
			if(list0!=null&&list0.size()>0){
				return AjaxResultPo.success("查询成功", list0.size(), list0);
			}
			List<GroupPo> list=groupService.queryUserGroup(request, entId, null);
			return AjaxResultPo.success("查询成功", list.size(), list);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return AjaxResultPo.failed(new Exception("查询失败"));
	}
	/**
	 * 查询所有客服组信息(分页)
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getGroups")
	public AjaxResultPo getGroups(HttpServletRequest request,Integer rows, Integer page){
		String entId=DomainUtils.getEntId(request);
		try {
			PageInfo pageInfo= new PageInfo((page-1)*rows, rows);
			
			List<GroupReturnPo> groups = groupService.getGroups(entId,pageInfo);
			
			for(int i=0;i<groups.size();i++){
				List<AgentReturnPo> members = groupMongoService.getMembers(entId,groups.get(i).getGroupId());
				groups.get(i).setMembers(members);
			}
			return AjaxResultPo.success("查询成功",pageInfo.getTotalRecords(), groups);
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxResultPo.failed(e);
		}
	}
	
	/**
	 * 编辑某客服组的时候查询所有客服，包括属于和不属于这个客服组的
	 * @param request
	 * @param groupId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getAgents")
	public AjaxResultPo getAgents(HttpServletRequest request,String groupId){
		String entId=DomainUtils.getEntId(request);
		try {
			List<AgentReturnPo> agents=groupMongoService.getAgents(entId,groupId);
			return  AjaxResultPo.success("查询成功", agents.size(), agents);
		} catch (Exception e) {
			e.printStackTrace();
			return  AjaxResultPo.failed(e);
		}
	}
	
	/**
	 * 添加新的客服组
	 * @param request
	 * @param po
	 * @param agents
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/addGroup")
	public AjaxResultPo addGroup(HttpServletRequest request,GroupPo po,String userIds){
		String entId=DomainUtils.getEntId(request);
		SsoUserVo user=SsoSessionUtils.getUserInfo(request);
		String ccodEntId=SsoSessionUtils.getEntInfo(request).getCcodEntId();
		try {
			po.setCreatorId(SsoSessionUtils.getUserInfo(request).getLoginName());
			po.setCreatorName(SsoSessionUtils.getUserInfo(request).getNickName());
			groupService.addUserGroup(entId,po,ccodEntId);
			ManageLogUtils.AddSucess(request, "添加客服组", po.getGroupName(), "userIds:"+userIds);
			
			//绑定坐席
			if(StringUtils.isNotBlank(userIds)){
				//调用CCOD技能组绑定坐席接口
				groupService.addAgents(user,entId,po.getGroupId(),userIds);
				ManageLogUtils.AddSucess(request, "添加坐席", po.getGroupName(), "userIds:"+userIds);
				
			}
			//更新缓存
			ParamUtils.refreshCache(CacheGroup.GROUP, entId);
			ParamUtils.refreshCache(CacheGroup.GROUP_AGENT, entId);
//			EntClient.refresh(entId, CacheGroup.GROUP.id);s
			return  AjaxResultPo.success("添加成功", 0, null);
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxResultPo.failed(e);
		}
	}
	/**
	 * 前台提交对客服组的编辑，即对客服组进行跟新
	 * @param request
	 * @param groupId
	 * @param agents
	 * @param groupName
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateGroup")
	public AjaxResultPo updateGroup(HttpServletRequest request,String groupId,String userIds,String groupName){
		try {
			String entId = DomainUtils.getEntId(request);
			SsoUserVo user=SsoSessionUtils.getUserInfo(request);
			String ccodEntId = SsoSessionUtils.getEntInfo(request).getCcodEntId();
			if(StringUtils.isBlank(groupName)){
				SystemLogUtils.Debug(String.format("更新客服组，名称为空，groupId=%s", groupId));
				throw new Exception("客服组名称为空");
			}
			//判断客服组名称是否更改，若更改，判断客服组名称是否已经存在
			String preName = groupService.getGroupName(entId,groupId);
			boolean isNameChanged=false;
			if(!groupName.equals(preName)){
				List<GroupReturnPo> groups=groupService.getGroups(entId,null);
				for(int i=0;i<groups.size();i++){
					if(groups.get(i).getGroupName().equals(groupName)){
						SystemLogUtils.Debug(String.format("更改客服组，更改后的客服组名称已经存在，groupName=%s", groupName));
						throw new Exception("更改后的客服组名称已经存在");
					}
				}
				isNameChanged=true;
			}
			if(isNameChanged){
				GroupPo gp=new GroupPo();
				gp.setGroupId(groupId);
				gp.setGroupName(groupName);
				gp.setUpdatorId(user.getLoginName());
				gp.setUpdatorName(user.getNickName());
				//技能组修改
				groupService.updateUserGroup(ccodEntId, entId, gp);
				SystemLogUtils.Debug(String.format("本地客服组修改成功，groupId=%s,groupName=%s",groupId, groupName));
			}
			List<String> all=null;
			if(StringUtils.isNotBlank(userIds)){
				all=stringToList(userIds);
			}
			//获取数据库中已绑定的坐席
			List<AgentReturnPo> exists=groupMongoService.getMembers(entId,groupId);
			List<String> delete=new ArrayList<String>();
			String add=null;
			if(exists.size()==0){
				if(all!=null){
					//技能组绑定坐席
					groupService.addAgents(user, entId, groupId, userIds);
					SystemLogUtils.Debug(String.format("客服组添加坐席成功，groupId=%s,agentIds=%s",groupId, userIds));
				}
			}else{
				if(all==null){
					String existAll=exists.get(0).getUserId();
					for(int i=1;i<exists.size();i++){
						existAll+=","+exists.get(i).getUserId();
					}
					groupService.deleteAgents(ccodEntId,entId, groupId,existAll,true);
					SystemLogUtils.Debug(String.format("客服组解绑坐席成功，groupId=%s,agentIds=%s",groupId, userIds));
					
				}else{
					
					//比较传入坐席和数据库中的坐席得出新增的坐席和要解绑的坐席
					add=groupMongoService.compare(all,exists,delete);
					//解绑坐席
					if(delete.size()>0){
						String deleteTmp=delete.toString().substring(1,delete.toString().length()-1).replaceAll(" ", "");
						
						groupService.deleteAgents(ccodEntId,entId, groupId,deleteTmp,false);
						SystemLogUtils.Debug(String.format("客服组解绑坐席成功，groupId=%s,deleteAgentIds=%s",groupId, deleteTmp));
					}
					//绑定坐席
					if(StringUtils.isNotBlank(add)){
						groupService.addAgents(user, entId, groupId, add);
						SystemLogUtils.Debug(String.format("客服组解绑坐席成功，groupId=%s,agentIds=%s",groupId, userIds));
					}
				}
			}
			SystemLogUtils.Debug("客服组："+groupId+"编辑成功");
			ParamUtils.refreshCache(CacheGroup.GROUP, entId);
			ParamUtils.refreshCache(CacheGroup.GROUP_AGENT, entId);
			EntClient.refresh(entId, CacheGroup.GROUP.id);
			return  AjaxResultPo.success("更新成功", 0, null);
		} catch (Exception e) {
			e.printStackTrace();
			return  AjaxResultPo.failed(e);
		}
	}
	
	/**
	 * 删除指定客服组
	 * @param request
	 * @param groupId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deleteGroup")
	public AjaxResultPo deleteGroup(HttpServletRequest request,String groupId){
		String entId=DomainUtils.getEntId(request);
		String ccodEntId=SsoSessionUtils.getEntInfo(request).getCcodEntId();
		try {
			groupService.deleteUserGroup(ccodEntId, entId, groupId);
			SystemLogUtils.Debug(String.format("删除客服组成功，groupId=%s",groupId));
			
			ParamUtils.refreshCache(CacheGroup.GROUP, entId);
			ParamUtils.refreshCache(CacheGroup.GROUP_AGENT, entId);
			EntClient.refresh(entId, CacheGroup.GROUP.id);
			return AjaxResultPo.success("成功删除客服组", 1, groupId);
		} catch (Exception e) {
			SystemLogUtils.Debug(String.format("删除客服组失败，groupId=%s",groupId));
			return AjaxResultPo.failed(e);
		}
		
	}
	
	/**
	 * 查询指定客服组的客服的详细信息(分页)
	 * @param request
	 * @param groupId
	 * @param rows
	 * @param page
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getDetailMembers")
	public AjaxResultPo getMembers(HttpServletRequest request,String groupId,String userStatus,Integer rows, Integer page){
		String entId=DomainUtils.getEntId(request);
		try {
			PageInfo pageInfo =new PageInfo((page-1)*rows, rows);
			List<DatEntUserPo> list = groupMongoService.getDetailMembers(entId,groupId,pageInfo,userStatus);
			for(DatEntUserPo po:list){
				po.setPhotoUrl(PhotoUrlUtil.getPhotoPath(request, entId, po.getPhotoUrl()));
			}
			for(int i=0;i<list.size();i++){
				if(list.get(i).getUserType().equals("1")){
					list.get(i).setUserType("普通用户") ;
				}
				if(list.get(i).getUserType().equals("2")){
					list.get(i).setUserType("坐席客服") ;
				}
				if(list.get(i).getUserType().equals("3")){
					list.get(i).setUserType("管理员") ;
				}
			}			
			return AjaxResultPo.success("成功查询",pageInfo.getTotalRecords(), list);
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxResultPo.failed(e);
		}
	}
	
	private List<String> stringToList(String userIds){
		String spl[]=userIds.split(",");
		List<String> all=new ArrayList<String>();
		for(int i=0;i<spl.length;i++){
			all.add(spl[i]);
		}
		return all;
	}
	private String getExistAgents(String entId, String groupId) {
		List<AgentReturnPo> exists=groupMongoService.getMembers(entId,groupId);
		String existAll=exists.get(0).getUserId();
		for(int i=1;i<exists.size();i++){
			existAll+=","+exists.get(i).getUserId();
		}
		return existAll;
	}
}

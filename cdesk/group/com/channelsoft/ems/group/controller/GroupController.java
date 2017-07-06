package com.channelsoft.ems.group.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import com.channelsoft.ems.api.client.EntClient;
import com.channelsoft.ems.api.po.AgentSimplePo;
import com.channelsoft.ems.redis.constant.CacheGroup;
import com.channelsoft.ems.redis.util.ParamUtils;
import com.channelsoft.ems.sso.util.SsoSessionUtils;
import com.channelsoft.ems.sso.vo.SsoUserVo;
import com.channelsoft.ems.group.po.AgentPo;
import com.channelsoft.ems.group.po.AgentReturnPo;
import com.channelsoft.ems.group.po.GroupPo;
import com.channelsoft.ems.group.po.GroupReturnPo;
import com.channelsoft.ems.group.service.IGroupService;
import com.channelsoft.ems.user.po.DatEntUserPo;
import com.channelsoft.ems.user.util.PhotoUrlUtil;

@Controller
@RequestMapping("/group")
public class GroupController {
	
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
	public AjaxResultPo getGroups(HttpServletRequest request,int rows, int page){
		String entId=DomainUtils.getEntId(request);
		PageInfo pageInfo = new PageInfo((page-1)*rows, rows);
		List<GroupReturnPo> groups=groupService.getGroups(entId,pageInfo);
		if(groups!=null&&groups.size()>0){
			for(int i=0;i<groups.size();i++){
				List<AgentReturnPo> members=groupService.getMembers(entId,groups.get(i).getGroupId());
				groups.get(i).setMembers(members);
			}
		}
		return AjaxResultPo.success("查询成功",pageInfo.getTotalRecords(), groups);
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
		List<AgentReturnPo> agents=groupService.getAgents(request,entId,groupId);
		return  AjaxResultPo.success("查询成功", agents.size(), agents);
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
	public AjaxResultPo addGroup(HttpServletRequest request,GroupPo po,String agents){
		String entId=DomainUtils.getEntId(request);
		SsoUserVo user=SsoSessionUtils.getUserInfo(request);
		if(!StringUtils.isNotBlank(po.getGroupName())){
			ManageLogUtils.AddFail(request, new BaseException("客服组名称为空"), "添加客服组",
					po.getGroupName(), "agents:"+agents);
			return AjaxResultPo.failed(new Exception("客服组名称为空"));
		}
		try {
			List<GroupReturnPo> groups=groupService.getGroups(entId,null);
			for(int i=0;i<groups.size();i++){
				if(groups.get(i).getGroupName().equals(po.getGroupName())){
					ManageLogUtils.AddFail(request, new BaseException("客服组名称已经存在"), "添加客服组",
							po.getGroupName(), "agents:"+agents);
					return AjaxResultPo.failed(new Exception("客服组名称已经存在"));
				}
			}
			int addGroup=groupService.addUserGroup(entId,po,user.getCcodEntId());
			if(addGroup>0){
				ManageLogUtils.AddSucess(request, "添加客服组", po.getGroupName(), "agents:"+agents);
			}
			if(StringUtils.isNotBlank(agents)){
				int addAgents=groupService.addAgents(user,entId,po.getGroupId(),agents);
				if(addAgents>0){
					ManageLogUtils.AddSucess(request, "添加坐席", po.getGroupName(), "agents:"+agents);
				}
			}
		} catch (ServiceException e) {
			ManageLogUtils.AddFail(request, new BaseException("添加客服组失败"), "添加客服组",po.getGroupName(), "agents:"+agents);
			e.printStackTrace();
			return AjaxResultPo.failed(e);
		}
		ParamUtils.refreshCache(CacheGroup.GROUP, entId);
		ParamUtils.refreshCache(CacheGroup.GROUP_AGENT, entId);
		EntClient.refresh(entId, CacheGroup.GROUP.id);
		return  AjaxResultPo.success("添加成功", 0, null);
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
	public AjaxResultPo updateGroup(HttpServletRequest request,String groupId,String agents,String groupName){
		SsoUserVo user=SsoSessionUtils.getUserInfo(request);
		if(!StringUtils.isNotBlank(groupName)){
			SystemLogUtils.Fail("GroupController", "编辑客服组", "groupId="+groupId, new BaseException("客服组名称为空"));
			return AjaxResultPo.failed(new Exception("客服组名称为空"));
		}
		String entId=DomainUtils.getEntId(request);
		String gName=groupService.getGroupName(entId,groupId);
		if(!groupName.equals(gName)){
			List<GroupReturnPo> groups=groupService.getGroups(entId,null);
			for(int i=0;i<groups.size();i++){
				if(groups.get(i).getGroupName().equals(groupName)){
					SystemLogUtils.Fail("GroupController", "编辑客服组", "groupId="+groupId, new BaseException("更改后的客服组名称已经存在空"));
					return AjaxResultPo.failed(new Exception("更改后的客服组名称已经存在"));
				}
			}
		}
		List<String> all=null;
		int delN=0,addN=0,upN=0;
		
		if(StringUtils.isNotBlank(agents))
			all=stringToList(agents);
		
		try {
			List<AgentReturnPo> exists=groupService.getMembers(entId,groupId);
			List<String> delete=new ArrayList<String>();
			String add=null;
			if(exists.size()>0){
				if(all==null){
					delN=groupService.deleteAgents(user.getCcodEntId(), entId, groupId,null,true);
					if(delN>0){
						ManageLogUtils.DeleteSuccess(request, "删除客服组坐席", gName, "entId="+entId+",agents:"+agents);
					}
				}else{
					add=groupService.compare(all,exists,delete);
					if(delete.size()>0){
						delN=groupService.deleteAgents(user.getCcodEntId(), entId, groupId,
								delete.toString().substring(1,delete.toString().length()-1),false);
						if(delN>0){
							String agts=delete.toString().substring(1,delete.toString().length()-1);
							ManageLogUtils.DeleteSuccess(request, "删除客服组坐席", gName, "entId="+entId+",agents:"+agts);
						}
					}
					if(StringUtils.isNotBlank(add)){
						addN=groupService.addAgents(user, entId, groupId, add);
						if(addN>0){
							ManageLogUtils.AddSucess(request, "添加客服组坐席", gName, "entId="+entId+",agents:"+add);
						}
					}
				}
				
			}else{
				if(all!=null){
					addN=groupService.addAgents(user, entId, groupId, agents);
					if(addN>0){
						ManageLogUtils.AddSucess(request, "添加客服组坐席", gName,"entId="+entId+ ",agents:"+agents);
					}
				}
			}
			if(groupName.equals(gName)){
				if(delN>0||addN>0){
					GroupPo gPo=new GroupPo();
					gPo.setGroupId(groupId);
					gPo.setGroupName(groupName);
					upN=groupService.updateUserGroup(user.getCcodEntId(), entId, gPo);
				}
			}else{
				GroupPo gPo=new GroupPo();
				gPo.setGroupId(groupId);
				gPo.setGroupName(groupName);
				upN=groupService.updateUserGroup(user.getCcodEntId(), entId, gPo);
				if(upN>0){
					ManageLogUtils.EditSuccess(request, "编辑客服组", gName, "entId="+entId+",new groupName="+groupName);
				}
			}
		} catch (ServiceException e) {
			ManageLogUtils.EditSuccess(request, "编辑客服组", gName, "entId="+entId+",new groupName="+groupName+",agents="+agents);
			e.printStackTrace();
			return  AjaxResultPo.failed(e);
		}
		if(delN>0||addN>0||upN>0){
			SystemLogUtils.Debug("客服组："+groupId+"编辑成功");
			ParamUtils.refreshCache(CacheGroup.GROUP, entId);
			ParamUtils.refreshCache(CacheGroup.GROUP_AGENT, entId);
			EntClient.refresh(entId, CacheGroup.GROUP.id);
			return  AjaxResultPo.success("更新成功", 0, null);
		}else{
			SystemLogUtils.Debug("客服组："+groupId+"操作成功，没有任何改动");
			return  AjaxResultPo.success("操作成功，没有任何改动", 0, null);
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
		int delG=0;
		int delA=0;
		try {
			delG=groupService.deleteUserGroup(ccodEntId, entId, groupId);
			if(delG>0){
				ManageLogUtils.DeleteSuccess(request, "删除客服组", groupId, "entId="+entId+",groupId="+groupId);
			}
			delA=groupService.deleteAgents(ccodEntId, entId, groupId, null, true);
			if(delA>0){
				ManageLogUtils.DeleteSuccess(request, "删除客服组坐席", groupId, "entId="+entId+",groupId="+groupId+",agents=all agents");
			}
			ParamUtils.refreshCache(CacheGroup.GROUP, entId);
			ParamUtils.refreshCache(CacheGroup.GROUP_AGENT, entId);
			EntClient.refresh(entId, CacheGroup.GROUP.id);
			return AjaxResultPo.success("成功删除客服组", delG, null);
		} catch (ServiceException e) {
			ManageLogUtils.DeleteFail(request, new BaseException(e.getMessage()), 
					"删除客服组", "groupId:"+groupId, "坐席数："+delA);
			e.printStackTrace();
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
	public AjaxResultPo getMembers(HttpServletRequest request,String groupId,String userStatus,int rows, int page){
		String entId=DomainUtils.getEntId(request);
		PageInfo pageInfo = new PageInfo((page-1)*rows, rows);
		List<DatEntUserPo> list = groupService.getDetailMembers(entId,groupId,pageInfo,userStatus);
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
		return AjaxResultPo.success("成功查询", pageInfo.getTotalRecords(), list);
	}
	
	@ResponseBody
	@RequestMapping("/getAllMember")
	public AjaxResultPo getAllMember(HttpServletRequest request,String groupId){
		String entId=DomainUtils.getEntId(request);
		List<AgentPo> list;
		if(StringUtils.isBlank(groupId))
			return AjaxResultPo.failed(new Exception("请指定客服组"));
		try {
			list = groupService.queryGroupAgent(entId, groupId);
			return AjaxResultPo.success("成功查询", list.size(), list);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return AjaxResultPo.failed(new Exception("查询失败"));
	}
	
	/**
	 * 获取指定坐席Id的所有所属客服组（ID）
	 * @param request
	 * @param agentId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getGroupIds")
	public AjaxResultPo  getGroupIds(HttpServletRequest request,String agentId){
		String entId=DomainUtils.getEntId(request);
		String ids=groupService.getAgentGroupsStr(entId, agentId);
		return AjaxResultPo.success("", 1, ids);
	}
	
	private List<String> stringToList(String agents){
		String spl[]=agents.split(",");
		List<String> all=new ArrayList<String>();
		for(int i=0;i<spl.length;i++){
			all.add(spl[i]);
		}
		return all;
	}
	
	@ResponseBody
	@RequestMapping("/getGroupsByIds")
	public AjaxResultPo getGroupsByIds(HttpServletRequest request,String groupIds){
		String entId=DomainUtils.getEntId(request);
		if(StringUtils.isBlank(groupIds)){
			return AjaxResultPo.failed(new Exception("客服组id为空"));
		}
		List<GroupPo> groups=groupService.getGroupsByIds(entId,groupIds.replaceAll(" ", ""));
		return AjaxResultPo.success("查询成功", groups.size(), groups);
	}
	
	/*从客服组中删除客服*/
	@ResponseBody
	@RequestMapping("/deleteAgent")
	public AjaxResultPo deleteAgent(HttpServletRequest request,String agentId){
		String entId=DomainUtils.getEntId(request);
		String ccodEntId=SsoSessionUtils.getEntInfo(request).getCcodEntId();
		int delA=0;
		try {
			delA=groupService.deleteAgents(ccodEntId, entId, null, agentId, false);

			ParamUtils.refreshCache(CacheGroup.GROUP, entId);
			ParamUtils.refreshCache(CacheGroup.GROUP_AGENT, entId);
			EntClient.refresh(entId, CacheGroup.GROUP.id);
			
			return AjaxResultPo.success("成功删除客服", delA, null);
		} catch (ServiceException e) {
			e.printStackTrace();
			return AjaxResultPo.failed(e);
		}
		
	}
	
}

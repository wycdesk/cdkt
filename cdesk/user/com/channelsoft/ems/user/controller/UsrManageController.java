package com.channelsoft.ems.user.controller;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.channelsoft.cri.constant.BaseConstants;
import com.channelsoft.cri.exception.BaseException;
import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.logger.ManageLogUtils;
import com.channelsoft.cri.util.CdeskEncrptDes;
import com.channelsoft.cri.util.DomainUtils;
import com.channelsoft.cri.vo.AjaxResultPo;
import com.channelsoft.cri.vo.PageInfo;
import com.channelsoft.ems.api.client.CCODClient;
import com.channelsoft.ems.api.client.EntClient;
import com.channelsoft.ems.api.po.CCODResponsePo;
import com.channelsoft.ems.redis.constant.CacheGroup;
import com.channelsoft.ems.field.constants.UserFieldStatusEnum;
import com.channelsoft.ems.field.po.UserDefinedFiedPo;
import com.channelsoft.ems.field.service.IUserFieldService;
import com.channelsoft.ems.group.po.AgentPo;
import com.channelsoft.ems.group.po.GroupPo;
import com.channelsoft.ems.group.service.IGroupService;
import com.channelsoft.ems.log.constants.BusinessTypeEnum;
import com.channelsoft.ems.log.constants.LogTypeEnum;
import com.channelsoft.ems.log.service.ILogMongoService;
import com.channelsoft.ems.privilege.service.ICfgPermissionService;
import com.channelsoft.ems.privilege.vo.CfgPermissionVo;
import com.channelsoft.ems.register.po.RegisterInfoPo;
import com.channelsoft.ems.register.util.MD5Util;
import com.channelsoft.ems.sso.util.SsoSessionUtils;
import com.channelsoft.ems.sso.vo.SsoUserVo;
import com.channelsoft.ems.user.constant.LoginType;
import com.channelsoft.ems.user.constant.UserStatus;
import com.channelsoft.ems.user.dao.IUserDao;
import com.channelsoft.ems.user.po.DatEntUserPo;
import com.channelsoft.ems.user.po.RolePo;
import com.channelsoft.ems.user.service.IUserMongoService;
import com.channelsoft.ems.user.service.IUserService;
import com.channelsoft.ems.user.service.IUsrManageService;
import com.channelsoft.ems.user.util.PhotoUrlUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

@Controller
@RequestMapping("/usrManage")
public class UsrManageController {
	@Autowired
	IUsrManageService usrManageService;
	
	@Autowired
	ICfgPermissionService permissionService;
	@Autowired
	IUserFieldService userFieldService;
	@Autowired
	IUserDao userDao;
	
	@Autowired
	IUserService userService;
	@Autowired
	ILogMongoService logMongoService;
	
	@Autowired
	IGroupService groupService;

	@Autowired
	IUserMongoService userMongoService;
	
	/*用户用户组管理首页*/
	@RequestMapping(value = "/list")
	public String userList(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {		   
	    String enterpriseid = SsoSessionUtils.getUserInfo(request).getEntId();
	    String userId=SsoSessionUtils.getUserInfo(request).getUserId();
	    
	    model.addAttribute("entId", enterpriseid);
	        
	    /*是否为创始人*/
		DBObject queryObj=new BasicDBObject();
		queryObj.put("entId", enterpriseid);
		queryObj.put("userId", userId);
		List<DBObject>  list1=userMongoService.queryUserDetail(queryObj, null);
		String isFounder=list1.get(0).get("founder")+"";
	    
		model.addAttribute("isFounder",isFounder);
	    
	    	    
	    String sessionKey = SsoSessionUtils.getSessionKey(request);
		String roleId = SsoSessionUtils.getUserInfo(request).getRoleId();
		List<CfgPermissionVo> list = permissionService.querySubMenuTree(enterpriseid, roleId, sessionKey, "1"); // 1是“用户/组”权限
		boolean hasGroup1 = false;
		boolean hasGroup2 = false;
		boolean hasGroup3 = false;
				
		for (CfgPermissionVo vo : list) {
			if ("1".equals(vo.getGroupId())) {
				hasGroup1 = true;
			}
			if ("2".equals(vo.getGroupId())) {
				hasGroup2 = true;
			}
			if ("3".equals(vo.getGroupId())) {
				hasGroup3 = true;
			}
		}
		
		/*登录人用户角色*/
	    String userType1=SsoSessionUtils.getUserInfo(request).getUserType();
	    request.setAttribute("userType1", userType1);
	    request.setAttribute("roleId", roleId);
		
		model.addAttribute("permissionList", list);
		model.addAttribute("hasGroup1", hasGroup1);
		model.addAttribute("hasGroup2", hasGroup2);
		model.addAttribute("hasGroup3", hasGroup3);
		List<UserDefinedFiedPo> fieldList=userFieldService.queryDefinedFiled(enterpriseid, UserFieldStatusEnum.NORMAL.value, null);
//		JSONArray fieldArr=new JSONArray(fieldList);
//		model.addAttribute("fieldArr", fieldArr);
		request.setAttribute("activeFieldList", fieldList);	
		
	    return "userManage/index";
	}	
	
       /*用户详情页*/
	   /*@RequestMapping(value = "/userDetails")
	   	public String userDetails(String loginTime,HttpServletRequest request, Model model,HttpServletResponse response) throws Exception {		   
		    String domainName = SsoSessionUtils.getUserInfo(request).getEntId();
		    String loginType=SsoSessionUtils.getUserInfo(request).getUserType();
		    String userId= request.getParameter("userId");
		    DatEntUserPo user=usrManageService.queryUserById(domainName, userId);
		    model.addAttribute("user", user);
		    request.setAttribute("entId", domainName);
		    request.setAttribute("loginType", loginType);	    
		    request.setAttribute("loginTime", loginTime);
		    
		    //获取用户头像
		    PhotoUrlUtil.getPhotoUrl(request, model, domainName, user.getPhotoUrl());
		    
		    String userStatus=user.getUserStatus();
		    
		    for(UserStatus e:UserStatus.values()){
		    	if(userStatus.equals(e.value)){
		    		userStatus=e.desc;
		    	}
		    }
		    request.setAttribute("userStatus", userStatus);
		    
		     登录账号的用户Id和用户类型
		    String userId1=SsoSessionUtils.getUserInfo(request).getUserId();
		    String userType1=SsoSessionUtils.getUserInfo(request).getUserType();
		    
		    request.setAttribute("userId1", userId1);
		    request.setAttribute("userType1", userType1);		   		    		    
		    		    
		    登录账号的昵称和邮箱
		    String creatorName=SsoSessionUtils.getUserInfo(request).getNickName();
		    String creatorId=SsoSessionUtils.getUserInfo(request).getEmail();
		    
		    request.setAttribute("creatorName", creatorName);
		    request.setAttribute("creatorId", creatorId);
		    
		    创始人邮箱和登录人邮箱
		    String founderEmail = usrManageService.queryFounder(domainName);
		    String loginEmail= SsoSessionUtils.getUserInfo(request).getEmail();
		    
		    model.addAttribute("founderEmail", founderEmail);
		    model.addAttribute("loginEmail", loginEmail);
		    
		    return "userManage/userDetails";
	   	}*/
	   
	/*查询用户*/
	/*@ResponseBody
	@RequestMapping(value = "/queryUser/{menuId}")
	public AjaxResultPo queryUser(@PathVariable("menuId") String menuId, String entId,String userType,DatEntUserPo po,int rows, int page, HttpServletRequest request,Model model){
		PageInfo pageInfo = new PageInfo((page-1)*rows, rows);
		po.setEntId(entId);
		po.setUserType(userType);
		List<DatEntUserPo> list = usrManageService.queryUser(po,pageInfo);

		
		//用户头像处理
		for(DatEntUserPo vo : list){
			String photopath = PhotoUrlUtil.getPhotoPath(request, entId, vo.getPhotoUrl());
			//System.out.println(photopath);
			vo.setPhotoUrl(photopath);
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
		request.setAttribute("list", list);
		
		return AjaxResultPo.success("查询成功", pageInfo.getTotalRecords(), list);
	}*/
	
	/*查询最新用户*/
	/*@ResponseBody
	@RequestMapping(value = "/queryLately")
	public AjaxResultPo queryLately(String entId,String userType,DatEntUserPo po,int rows, int page, HttpServletRequest request,Model model){
		PageInfo pageInfo = new PageInfo((page-1)*rows, rows);
		po.setEntId(entId);
		List<DatEntUserPo> list = usrManageService.queryLately(po,pageInfo);
		
		model.addAttribute("list", list);
		return AjaxResultPo.success("查询成功", pageInfo.getTotalRecords(), list);
	}*/
	
	/*删除用户*/
	/*@ResponseBody
	@RequestMapping(value = "/deleteUser")
	public AjaxResultPo delete(String entId, String[] ids, HttpServletRequest request) throws Exception {
		try {
			List<DatEntUserPo> uList=new ArrayList<DatEntUserPo>();
			StringBuffer agents=new StringBuffer();
			for(int i=0;i<ids.length;i++){
				DatEntUserPo user=usrManageService.queryUserById(entId, ids[i]);
				if(Integer.valueOf(user.getUserType())>1){
					if(StringUtils.isNotBlank(agents.toString()))
						agents.append(user.getLoginName());
					else
						agents.append(","+user.getLoginName());
				}
				uList.add(user);
			}
			int success=usrManageService.delete(entId, ids);
			if(success>0)
			{				
				int delAgents=0;
				ManageLogUtils.DeleteSuccess(request, "删除用户", ids.toString(), "企业:"+entId);
				for(int i=0;i<ids.length;i++){
					DatEntUserPo user=uList.get(i);
					logMongoService.add(request, LogTypeEnum.DELETE, BusinessTypeEnum.USER,ids[i] , BusinessTypeEnum.USER.desc+"("+user.getLoginName()+")", user);
				}
				//删除客服组中的对应客服
				if(StringUtils.isNotBlank(agents.toString())){
					delAgents=groupService.deleteAgents(null, entId, null, agents.toString(), false);
				}
				删除用户名下的所有工单				
				EntClient.deleteWf(entId, ids);
			
				return BaseConstants.SUCCESS;
			}
			else{
				ManageLogUtils.DeleteFail(request, new ServiceException("删除失败"), "删除用户", ids.toString(), "企业:"+entId);
				return AjaxResultPo.failed(new Exception("删除失败"));
			}
				
		} catch (ServiceException e) {
			ManageLogUtils.DeleteFail(request, new ServiceException("删除失败,"+e.getMessage()), "删除用户", ids.toString(), "企业:"+entId);
			e.printStackTrace();
			return new AjaxResultPo(false, e.getMessage());
		}
	}*/
	
	/*添加用户*/
	/*@ResponseBody
	@RequestMapping(value = "/addUser")
	public AjaxResultPo addUser(String entId,DatEntUserPo po, HttpServletRequest request) throws Exception {
		try {
			    AjaxResultPo ret = new AjaxResultPo(true, "添加成功");			
			    String userId=userDao.getUserId();
			    RegisterInfoPo entPo=userService.getEntInfo(entId);
			    
				if(userService.existsEmails(entPo.getEntId(), po.getEmail()))
					return AjaxResultPo.failed(new Exception("邮箱已被注册 "));
				else{
			    po.setUserId(userId);
			    po.setEntName(entPo.getEntName());
			    po.setLoginType(LoginType.MAILBOX.value);
			    po.setLoginName(po.getEmail());
			    po.setUserStatus(UserStatus.NORMAL.value);
			    String activeCode=MD5Util.MD5(po.getEmail()+new Date().getTime());
			    po.setActiveCode(activeCode);
			    
			    ret.setRows(userId);			   			    
				int success=usrManageService.add(po);

				userService.sendMailMongo(request,po,activeCode,false);				
				if(success>0){		
					ManageLogUtils.AddSucess(request, "添加用户", po.getEmail(), "entId="+entId+",userType="+po.getUserType()+",roleId="+po.getRoleId()+",nickName="+po.getNickName()+",userName="+po.getUserName());
					logMongoService.add(request, LogTypeEnum.ADD, BusinessTypeEnum.USER, userId, BusinessTypeEnum.USER.desc+"("+po.getEmail()+")", po);
					return ret;
				}else{
					ManageLogUtils.AddFail(request, new ServiceException("添加失败"), "添加用户", po.getEmail(), "entId="+entId+",userType="+po.getUserType()+",roleId="+po.getRoleId()+",nickName="+po.getNickName()+",userName="+po.getUserName());
				}
					return AjaxResultPo.failed(new Exception("添加失败"));
				}
		} catch (ServiceException e) {
			ManageLogUtils.AddFail(request, new ServiceException("添加失败,"+e.getMessage()), "添加用户", po.getEmail(), "entId="+entId+",userType="+po.getUserType()+",roleId="+po.getRoleId()+",nickName="+po.getNickName()+",userName="+po.getUserName());
			e.printStackTrace();
			return new AjaxResultPo(false, e.getMessage());
		}
	}*/
	
	/*修改用户信息*/
	/*@ResponseBody
	@RequestMapping(value = "/updateUser")
	public AjaxResultPo updateUser(DatEntUserPo po, HttpServletRequest request) throws Exception {
		try {
		    String entId=SsoSessionUtils.getUserInfo(request).getEntId();  		    
		    po.setEntId(entId);	 

			int success=usrManageService.update(po);
			if(success>0){
				ManageLogUtils.EditSuccess(request, "修改用户", po.getEmail(), "entId="+entId+",userType="+po.getUserType()+",fixedPhone="+po.getFixedPhone()+",userLabel="+po.getUserLabel()+",userName="+po.getUserName()+",nickName="+po.getNickName());
				logMongoService.add(request, LogTypeEnum.UPDATE, BusinessTypeEnum.USER, po.getUserId(), BusinessTypeEnum.USER.desc+"("+po.getEmail()+")", po);
				return BaseConstants.SUCCESS;
			}			
			else{
				ManageLogUtils.EditFail(request, new ServiceException("修改失败"), "修改用户", po.getEmail(), "entId="+entId+",userType="+po.getUserType()+",fixedPhone="+po.getFixedPhone()+",userLabel="+po.getUserLabel()+",userName="+po.getUserName()+",nickName="+po.getNickName());
			
				return AjaxResultPo.failed(new Exception("更新失败"));
			}
				
		} catch (ServiceException e) {
			ManageLogUtils.EditFail(request, new ServiceException("修改失败,"+e.getMessage()), "修改用户", po.getEmail(), "userType="+po.getUserType()+",fixedPhone="+po.getFixedPhone()+",userLabel="+po.getUserLabel()+",userName="+po.getUserName()+",nickName="+po.getNickName());
			e.printStackTrace();
			return new AjaxResultPo(false, e.getMessage());
		}
	}*/
		
	/*批量编辑用户信息*/
	/*@ResponseBody
	@RequestMapping(value = "/updateBatch")
	public AjaxResultPo updateBatch(String[] ids,String entId,String userLabel,String userStatus,DatEntUserPo po, HttpServletRequest request) throws Exception {
		try {
			po.setEntId(entId);
			po.setUserStatus(userStatus);
			po.setUserLabel(userLabel);
			
			String email=SsoSessionUtils.getUserInfo(request).getEmail();
			String ids1="";
			for(int i=0;i<ids.length;i++){
				ids1+=ids[i]+",";
			}
			
			int success=usrManageService.updateBatch(po,ids);
			if(success>0){
				ManageLogUtils.EditSuccess(request, "修改用户", ids.toString(), "entId="+entId+",userLabel="+userLabel+",userStatus="+userStatus);
			
			    logMongoService.add(request, LogTypeEnum.UPDATE, BusinessTypeEnum.USER,email , BusinessTypeEnum.USER.desc+"("+ids1+")", email);	
				return BaseConstants.SUCCESS;
			}
			else{
				ManageLogUtils.EditFail(request, new ServiceException("修改失败"), "修改用户", ids.toString(), "entId="+entId+",userLabel="+userLabel+",userStatus="+userStatus);
				return AjaxResultPo.failed(new Exception("更新失败"));
			}				
		} catch (ServiceException e) {
			ManageLogUtils.EditFail(request, new ServiceException("修改失败,"+e.getMessage()), "修改用户", ids.toString(), "entId="+entId+",userLabel="+userLabel+",userStatus="+userStatus);
			e.printStackTrace();
			
			return new AjaxResultPo(false, e.getMessage());
		}
	}*/
	
	/*设置密码*/
	/*@ResponseBody
	@RequestMapping("/setPwd")
	public AjaxResultPo setPwd(HttpServletRequest request,String password,String userId){
		String entId=SsoSessionUtils.getUserInfo(request).getEntId();
		String email=SsoSessionUtils.getUserInfo(request).getEmail();
		DatEntUserPo user=usrManageService.queryUserById(entId, userId);
		try {			
			int suc=usrManageService.setPwd(password,entId,userId);
			if(suc>0){
				ManageLogUtils.EditSuccess(request, "修改用户密码", email, "entId="+entId+",email="+email+",password="+password+",userId="+userId);
				logMongoService.add(request, LogTypeEnum.UPDATE, BusinessTypeEnum.USER, userId, BusinessTypeEnum.USER.desc+"("+user.getEmail()+")", null);
				
				return AjaxResultPo.success("密码安全更新成功！",0,null);
			}
			else{
				ManageLogUtils.EditFail(request, new ServiceException("修改密码失败"), "修改用户密码", email, "entId="+entId+",email="+email+",password="+password+",userId="+userId);
				return AjaxResultPo.failed(new Exception("设置失败 "));
			}
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			ManageLogUtils.EditFail(request, new ServiceException("修改密码失败,"+e.getMessage()), "修改用户密码", email, "entId="+entId+",email="+email+",password="+password+",userId="+userId);
			e.printStackTrace();
			
			AjaxResultPo ajaxPo=AjaxResultPo.failed(new Exception("执行出错 "));
			ajaxPo.setRows("forRedirect");	
			return ajaxPo;
		}
	}*/
	
	/*修改用户状态*/
	/*@ResponseBody
	@RequestMapping(value = "/updateStatus")
	public AjaxResultPo updateStatus(DatEntUserPo po, HttpServletRequest request) throws Exception {		
		String email=SsoSessionUtils.getUserInfo(request).getEmail();
		String entId=SsoSessionUtils.getUserInfo(request).getEntId();
		String userId=request.getParameter("userId");
		String status=request.getParameter("status");
		status = URLDecoder.decode(status, "UTF-8");
		
		DatEntUserPo user=usrManageService.queryUserById(entId, userId);
		try {					
			for(UserStatus e:UserStatus.values()){
				if(e.desc.equals(status)){
					status=e.value;
				}
			}
			po.setEntId(entId);
			po.setUserId(userId);
			po.setUserStatus(status);
			
			int success=usrManageService.updateStatus(po);
			if(success>0){
				ManageLogUtils.EditSuccess(request, "修改用户状态", email, "entId="+entId+",userStatus="+status+",email="+email+",userId="+userId);
				logMongoService.add(request, LogTypeEnum.UPDATE, BusinessTypeEnum.USER, userId, BusinessTypeEnum.USER.desc+"("+user.getEmail()+")", po);
				return BaseConstants.SUCCESS;
			}
			else{
				ManageLogUtils.EditFail(request, new ServiceException("修改用户状态失败"), "修改用户状态", email, "entId="+entId+",userStatus="+status+",email="+email+",userId="+userId);
				return AjaxResultPo.failed(new Exception("更新用户状态失败"));
			}
		} catch (ServiceException e) {
			ManageLogUtils.EditFail(request, new ServiceException("修改用户状态失败,"+e.getMessage()), "修改用户状态", email, "entId="+entId+",userStatus="+status+",email="+email+",userId="+userId);
			e.printStackTrace();
			return new AjaxResultPo(false, e.getMessage());
		}
	}*/
	
	/*客服类型的二级下拉框*/
	@ResponseBody
	@RequestMapping(value = "/secondLevel")
	public AjaxResultPo secondLevel(String parentId,String id,RolePo po,HttpServletRequest request) throws Exception {
		try {
		    AjaxResultPo ret = new AjaxResultPo(true, "成功");		
			String entId=SsoSessionUtils.getUserInfo(request).getEntId();
			po.setParentId(parentId);
			po.setEntId(entId);
			po.setId(id);
			
			List<RolePo> list = usrManageService.querySecondLevel(po);
			
			ret.setRows(list);
			return ret;
		} catch (ServiceException e) {
			e.printStackTrace();
			return new AjaxResultPo(false, e.getMessage());
		}
	}

	/*客服所属客服组*/
	@ResponseBody
	@RequestMapping(value = "/belongGroup")
	public AjaxResultPo belongGroup(String userId,String loginName,DatEntUserPo po, HttpServletRequest request) throws Exception{
	try {
		String entId=SsoSessionUtils.getUserInfo(request).getEntId();
		AjaxResultPo ret = new AjaxResultPo(true, "成功");
		
		po.setUserId(userId);
		po.setLoginName(loginName);
		po.setEntId(entId);		
		List<GroupPo> list = usrManageService.belongGroup(po);
	
		ret.setRows(list);		
		return ret;
	} catch (ServiceException e) {
		e.printStackTrace();
		return new AjaxResultPo(false, e.getMessage());
	}
  }
	
	/*详情页分配客服组*/
	@ResponseBody
	@RequestMapping(value = "/assignAgent")
	public AjaxResultPo assignAgent(String[] groupId,String[] newGroupId,String agentId,String creatorId,String creatorName,String userId, HttpServletRequest request) throws Exception {    
		String entId=SsoSessionUtils.getUserInfo(request).getEntId();
		try {
			    AjaxResultPo ret = new AjaxResultPo(true, "添加成功");			
			  			    
			    List<AgentPo> agentPos=new ArrayList<AgentPo>();		    
			    String newGroupId1="";
			    String groupId1="";
			    
			    for(int i=0;i<groupId.length;i++){
			    	groupId1+=groupId[i]+",";
			    }			    
			    for(int i=0;i<newGroupId.length;i++){
			    	AgentPo apo=new AgentPo();		    	
			    	apo.setAgentId(agentId);
			    	apo.setCreatorId(creatorId);
			    	apo.setCreatorName(creatorName);
			    	apo.setGroupId(newGroupId[i]);
			    	apo.setUserId(userId);
			    	
			    	agentPos.add(apo);			    	
			    	newGroupId1+=newGroupId[i]+",";
			    }			
			    
			    
			    int success=0;	
				/*调用ccod接口*/
				String ccodEntId = SsoSessionUtils.getEntInfo(request).getCcodEntId();
				CCODClient.changeUserGroups(groupId, newGroupId, userId, ccodEntId);
				success=usrManageService.assignAgent(entId,groupId,agentId,agentPos);				
				
				if(success>0){
					ManageLogUtils.DeleteSuccess(request, "从所属客服组删除当前用户", groupId.toString(), "企业:"+entId);
					logMongoService.add(request, LogTypeEnum.DELETE, BusinessTypeEnum.USER, groupId.toString(), BusinessTypeEnum.USERGROUP.desc+"("+groupId1+")", "");

					ManageLogUtils.AddSucess(request, "添加用户到所选的客服组", creatorId, "entId="+entId+",creatorId="+creatorId+",creatorName="+creatorName+",groupId="+newGroupId+",agentId="+agentId);
					logMongoService.add(request, LogTypeEnum.ADD, BusinessTypeEnum.USER, agentId, BusinessTypeEnum.USERGROUP.desc+"("+newGroupId1+")", "");
//					EntClient.refresh(entId, CacheGroup.GROUP.id);
					return ret;
				}
				else{
					ManageLogUtils.EditFail(request, new ServiceException("分配客服组失败"), "分配客服组", creatorId, "entId="+entId+",creatorId="+creatorId+",creatorName="+creatorName+",agentId="+agentId);
					return AjaxResultPo.failed(new Exception("分配失败"));
				}
		} catch (ServiceException e) {
			ManageLogUtils.EditFail(request, new ServiceException("分配客服组失败,"+e.getMessage()), "分配客服组", creatorId, "entId="+entId+",creatorId="+creatorId+",creatorName="+creatorName+",agentId="+agentId);
			e.printStackTrace();
			
			return new AjaxResultPo(false, e.getMessage());
		}
	}
		
	/*绑定手机*/
	/*@ResponseBody
	@RequestMapping(value = "/bindPhone")
	public AjaxResultPo bindPhone(String userId,String bind,DatEntUserPo po, HttpServletRequest request) throws Exception {
		String entId=SsoSessionUtils.getUserInfo(request).getEntId();
		String email=SsoSessionUtils.getUserInfo(request).getEmail();		
		DatEntUserPo user=usrManageService.queryUserById(entId, userId);
		try {						
			po.setUserId(userId);
			po.setPhoneBinded(bind);
			po.setEntId(entId);
			int success=usrManageService.bindPhone(po);			
			if(success>0){
				ManageLogUtils.EditSuccess(request, "绑定手机", userId, "entId="+entId+",phone_binded="+bind+",email="+email+",userId="+userId);
				logMongoService.add(request, LogTypeEnum.UPDATE, BusinessTypeEnum.USER, userId, BusinessTypeEnum.USER.desc+"("+user.getEmail()+")", po);
				
				return BaseConstants.SUCCESS;
			}
			else{
				ManageLogUtils.EditFail(request, new ServiceException("绑定手机失败"), "绑定手机", userId, "entId="+entId+",phone_binded="+bind+",email="+email+",userId="+userId);
				return AjaxResultPo.failed(new Exception("绑定失败!"));
			}
		} catch (ServiceException e) {
			ManageLogUtils.EditFail(request, new ServiceException("绑定手机失败,"+e.getMessage()), "绑定手机", userId, "entId="+entId+",phone_binded="+bind+",email="+email+",userId="+userId);
			e.printStackTrace();
			
			return new AjaxResultPo(false, e.getMessage());
		}
	}*/
	
	/*检测手机是否已经绑定*/
	/*@ResponseBody
	@RequestMapping(value = "/existsPhone")
	public AjaxResultPo existsPhone(String telPhone,String userId, HttpServletRequest request) throws Exception {
		try {	
			    String entId=SsoSessionUtils.getUserInfo(request).getEntId();
			    boolean success=usrManageService.existsPhone(entId,telPhone,userId);
				boolean success1=usrManageService.existsPhone1(entId,telPhone,userId);
				
				if(success){		
					return AjaxResultPo.failed(new Exception("该手机号码已被绑定，请先解绑或更换其他手机号码！"));
				}
				if(success1){
					return AjaxResultPo.failed(new Exception("手机'"+telPhone+"'已被取用."));
				}
				else{
					return AjaxResultPo.successDefault();
				}
		} catch (ServiceException e) {	
			e.printStackTrace();
			return new AjaxResultPo(false, e.getMessage());
		}
	}*/
	
	/*检测手机是否已存在*/
	/*@ResponseBody
	@RequestMapping(value = "/existsPhone1")
	public AjaxResultPo existsPhone1(String telPhone,String userId, HttpServletRequest request) throws Exception {
		try {	
			    String entId=SsoSessionUtils.getUserInfo(request).getEntId();
			    boolean success=usrManageService.existsPhone1(entId,telPhone,userId);
			    
				if(success){		
					return AjaxResultPo.failed(new Exception("手机'"+telPhone+"'已被取用."));
				}else{
					return AjaxResultPo.successDefault();
				}
		} catch (ServiceException e) {
			e.printStackTrace();
			return new AjaxResultPo(false, e.getMessage());
		}
	}*/
	
	/**
	 * 用户头像上传
	 * @param request
	 * @param userId
	 * @return
	 * @author wangjie
	 * @time 2015年12月1日下午5:46:31
	 */
	/*@ResponseBody
	@RequestMapping("/changePhoto")
	public AjaxResultPo changePhoto(HttpServletRequest request, String userId){
		try{
			DatEntUserPo po = new DatEntUserPo();
			po.setUserId(userId);
			String path = usrManageService.changePhoto(request, po);
			return AjaxResultPo.success("上传成功!", 0, path);
		}catch(ServiceException e){
			e.printStackTrace();
			return new AjaxResultPo(false, e.getMessage());
		}
	}*/
	/*@ResponseBody
	@RequestMapping("/updateInformation")
	public AjaxResultPo updateInformation(HttpServletRequest request,String email,String nickName,String userDesc){
		String loginName=SsoSessionUtils.getLoginName(request);
		
		if(!loginName.equals(email)){
			return AjaxResultPo.failed(new Exception("非当前登录者"));
		}
		try {
			String entId=DomainUtils.getEntId(request);
			int update=userService.updateInformation(entId,email,nickName,userDesc);
			if(update<=0){
				ManageLogUtils.EditFail(request, new BaseException("更新失败"), "更新用户基本信息", email, "nickName="+nickName+",userDesc="+userDesc);
				return AjaxResultPo.failed(new Exception("更新失败"));
			}
			ManageLogUtils.EditSuccess(request, "更新用户基本信息", email, "nickName="+nickName+",userDesc="+userDesc);
		} catch (ServiceException e) {
			ManageLogUtils.EditFail(request, e, "更新用户基本信息", email, "nickName="+nickName+",userDesc="+userDesc);
			e.printStackTrace();
			return AjaxResultPo.failed(e);
		}
		return AjaxResultPo.success("更新成功", 1, null);
	}*/
	/*@ResponseBody
	@RequestMapping("/updatePassword")
	public AjaxResultPo updatePassword(HttpServletRequest request,String email,String loginPwd,String newLoginPwd){
		String userId=SsoSessionUtils.getUserInfo(request).getUserId();
		String entId=null;
		DatEntUserPo loginPo=null;
		try {
			entId=DomainUtils.getEntId(request);
			loginPo = userService.queryUser(entId, userId);
		} catch (ServiceException e1) {
			e1.printStackTrace();
			return AjaxResultPo.failed(e1);
		}
		if(!(loginPo.getEmail().equals(email))){
			return AjaxResultPo.failed(new Exception("非当前登录者"));
		}
		if(!(loginPo.getLoginPwd().equals(CdeskEncrptDes.encryptST(loginPwd)))){
			return AjaxResultPo.failed(new Exception("密码不正确"));
		}
		try {
			int update=userService.updatePassword(entId,email,CdeskEncrptDes.encryptST(newLoginPwd));
			if(update<=0){
				ManageLogUtils.EditFail(request, new BaseException("更新失败"), "更新用户密码", email, "nickName="+
						loginPo.getNickName()+",entId="+loginPo.getEntId());
				return AjaxResultPo.failed(new Exception("更新失败"));
			}
			ManageLogUtils.EditSuccess(request, "更新用户基本信息", email, "nickName="+loginPo.getNickName()+
					",entId="+loginPo.getEntId());
		} catch (ServiceException e) {
			ManageLogUtils.EditFail(request, new BaseException("更新失败"), "更新用户密码", email, "nickName="+
					loginPo.getNickName()+",entId="+loginPo.getEntId());
			e.printStackTrace();
			return AjaxResultPo.failed(e);
		}
		return AjaxResultPo.success("更新成功", 1, null);
	}	*/
	
	/*根据用户id的数组得到用户的信息*/
	@ResponseBody
	@RequestMapping("/getUsersByIds")
	public AjaxResultPo getUsersByIds(HttpServletRequest request,String ids){
		AjaxResultPo ret = new AjaxResultPo(true, "成功");
		List<DatEntUserPo> uList=new ArrayList<DatEntUserPo>();
		String entId=DomainUtils.getEntId(request);
		
		String[] idArray = ids.split(",");
		
		if (idArray.length == 1 & idArray[0].equals("")){
			ret.setRows(uList);
		}else{
			for(int i=0;i<idArray.length;i++){
				//DatEntUserPo user=usrManageService.queryUserById(entId, idArray[i]);
				DatEntUserPo po=userMongoService.queryUserById(entId, idArray[i]);
				uList.add(po);
			}
			
			ret.setRows(uList);	
		}
		return ret;
	}
	/*
	 * 合并用户
	 */
	/*@ResponseBody
	@RequestMapping(value = "/mergeUser")
	public AjaxResultPo mergeUser(HttpServletRequest request,String userMerge,String userTarget){
		String entId=DomainUtils.getEntId(request);
		int suc=0;
		try {
			suc=usrManageService.mergeUser(entId,userMerge,userTarget);
		} catch (ServiceException e) {
			ManageLogUtils.EditFail(request, e, "合并用户", userMerge+":"+userTarget,
					"merge users from "+userMerge+" to "+userTarget);
			e.printStackTrace();
			return AjaxResultPo.failed(e);
		}
		if(suc>0){
			ManageLogUtils.EditSuccess(request, "合并用户", userMerge+":"+userTarget,
					"merge users from "+userMerge+" to "+userTarget);
			return AjaxResultPo.success("合并成功", 1, "");
		}else{
			ManageLogUtils.EditFail(request, new BaseException("合并失败"), "合并用户", userMerge+":"+userTarget,
					"merge users from "+userMerge+" to "+userTarget);
			return AjaxResultPo.failed(new Exception("合并失败"));
		}
	}*/
	/*@ResponseBody
	@RequestMapping(value = "/queryOrdinary")
	public AjaxResultPo queryOrdinaryByValue(HttpServletRequest request,String value,String email,String all){
		String entId=DomainUtils.getEntId(request);
		List<DatEntUserPo> list=null;
		try {
			list=usrManageService.queryOrdinaryByValue(entId,value,email);
			for(int i=0;i<list.size();i++){
				list.get(i).setPhotoUrl(PhotoUrlUtil.getPhotoPath(request, entId, list.get(i).getPhotoUrl()));
			}
		} catch (ServiceException e) {
			e.printStackTrace();
			return AjaxResultPo.failed(e);
		}
		return AjaxResultPo.success(all, list.size(), list);
	}*/
	
	
}

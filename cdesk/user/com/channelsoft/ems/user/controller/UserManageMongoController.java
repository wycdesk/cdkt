package com.channelsoft.ems.user.controller;

import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

import com.channelsoft.cri.constant.BaseConstants;
import com.channelsoft.cri.constant.DateConstants;
import com.channelsoft.cri.exception.BaseException;
import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.logger.ManageLogUtils;
import com.channelsoft.cri.logger.SystemLogUtils;
import com.channelsoft.cri.util.CdeskEncrptDes;
import com.channelsoft.cri.util.DBObjectUtils;
import com.channelsoft.cri.util.DomainUtils;
import com.channelsoft.cri.vo.AjaxResultPo;
import com.channelsoft.cri.vo.PageInfo;
import com.channelsoft.ems.api.client.CCODClient;
import com.channelsoft.ems.api.constants.AgentRoleType;
import com.channelsoft.ems.api.po.CCODRequestPo;
import com.channelsoft.ems.redis.constant.CacheGroup;
import com.channelsoft.ems.redis.util.ParamUtils;
import com.channelsoft.ems.ent.po.DatEntInfoPo;
import com.channelsoft.ems.field.po.UserDefinedFiedPo;
import com.channelsoft.ems.field.service.IUserFieldService;
import com.channelsoft.ems.group.service.IGroupService;
import com.channelsoft.ems.log.constants.BusinessTypeEnum;
import com.channelsoft.ems.log.constants.LogTypeEnum;
import com.channelsoft.ems.log.service.ILogMongoService;
import com.channelsoft.ems.register.util.MD5Util;
import com.channelsoft.ems.sso.util.SsoSessionUtils;
import com.channelsoft.ems.user.constant.LoginType;
import com.channelsoft.ems.user.constant.RoleType;
import com.channelsoft.ems.user.constant.UserStatus;
import com.channelsoft.ems.user.constant.UserType;
import com.channelsoft.ems.user.dao.IUserDao;
import com.channelsoft.ems.user.po.DatEntUserPo;
import com.channelsoft.ems.user.service.IUserMongoService;
import com.channelsoft.ems.user.service.IUserService;
import com.channelsoft.ems.user.service.IUsrManageService;
import com.channelsoft.ems.user.util.PhotoUrlUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;




@Controller
@RequestMapping("/userManageMongo")
public class UserManageMongoController {

	@Autowired
	IUserMongoService userMongoService;
	
	@Autowired
	IUserService userService;
	
	@Autowired
	IUserDao userDao;
	
	@Autowired
	ILogMongoService logMongoService;
	
	@Autowired
	IUsrManageService usrManageService;
	
	@Autowired
	IGroupService groupService;
	
	@Autowired
	IUserFieldService userFieldService;
	
	@ResponseBody
	@RequestMapping(value = "/updateInformation")
	public AjaxResultPo updateInformation(DatEntUserPo po, HttpServletRequest request) throws Exception {
		SystemLogUtils.Debug("updateInformation,loginName="+po.getUserId());
		String userId=SsoSessionUtils.getUserInfo(request).getUserId();
		
		if(!userId.equals(po.getUserId())){
			return AjaxResultPo.failed(new Exception("非当前登录者"));
		}
		try {
			String entId=DomainUtils.getEntId(request);
			int update=userMongoService.updateInformation(entId,po);
			if(update<0){
				ManageLogUtils.EditFail(request, new BaseException("更新失败"), "更新用户基本信息", po.getEmail(), "nickName="+po.getNickName()+",userDesc="+po.getUserDesc());
				return AjaxResultPo.failed(new Exception("更新失败"));
			}
			ManageLogUtils.EditSuccess(request, "更新用户基本信息", po.getEmail(), "nickName="+po.getNickName()+",userDesc="+po.getUserDesc());
		} catch (ServiceException e) {
			ManageLogUtils.EditFail(request, e, "更新用户基本信息", po.getEmail(), "nickName="+po.getNickName()+",userDesc="+po.getUserDesc());
			e.printStackTrace();
			return AjaxResultPo.failed(e);
		}
		return AjaxResultPo.success("更新成功", 1, po.getUserName());
	}
	
	/*修改用户信息*/
	@ResponseBody
	@RequestMapping(value = "/updateUser")
	public AjaxResultPo updateUser(String userInfos, HttpServletRequest request) throws Exception {
		SystemLogUtils.Debug("updateUser,userInfos="+userInfos);
		
		/**
		 * 通知CCOD修改坐席
		 */
		JSONObject json = new JSONObject(userInfos);
		
		try {			
			DatEntUserPo userpo=ParamUtils.getAgentById(json.getString("userId"), json.getString("entId"));

			if(userpo == null){
				SystemLogUtils.Debug("========updateUser,未查询到缓存用户,不通知CCOD修改坐席==========");
				 userpo=userMongoService.queryUserById(json.getString("entId"), json.getString("userId"));
			}
			String userType = userpo.getUserType();
			
			boolean b = false;
			if(json.has("userName") || json.has("userType") || json.has("roleId")){
				b = true;
			}
			
			if((UserType.SERVICE.value.equals(userType)||UserType.ADMINISTRATOR.value.equals(userType)) && b){
				
				CCODRequestPo ccodPo = new CCODRequestPo();
				ccodPo.setEnterpriseId(userpo.getCcodEntId());
				ccodPo.setAgentId(json.getString("userId"));
				if(json.has("userName")){
					ccodPo.setAgentName(json.getString("userName"));
				}else{
					ccodPo.setAgentName(userpo.getUserName());
				}
				ccodPo.setAgentPassword(CdeskEncrptDes.decryptST(userpo.getLoginPwd()));
				String roleId = "4";
				if(json.has("roleId")){
					roleId = json.getString("roleId");
				}
				if(RoleType.MONITOR.value.equals(roleId)){
					ccodPo.setAgentRole(AgentRoleType.MANAGER.value);
				}else{
					ccodPo.setAgentRole(AgentRoleType.NORMAL.value);
				}
				CCODClient.updateAgent(ccodPo);
			}

			String updatorId = SsoSessionUtils.getUserInfo(request).getUserId();
			String updatorName = SsoSessionUtils.getUserInfo(request).getUserName();
			userMongoService.updateUser(userInfos,updatorId,updatorName);
			//客服或者管理员修改需要刷新缓存
			if(UserType.SERVICE.value.equals(userType)||UserType.ADMINISTRATOR.value.equals(userType)){
				ParamUtils.refreshCache(CacheGroup.ENT_USER, userpo.getEntId());
			}
		    return AjaxResultPo.successDefault();	
			
		}catch (ServiceException e) {
			e.printStackTrace();
			return new AjaxResultPo(false, e.getMessage());
		}
	}
	
	/**
	 * 修改用户信息-IM页面调用
	 * @param userInfos
	 * @param request
	 * @return
	 * @throws Exception
	 * @author wangjie
	 * @time 2016年3月24日下午8:56:29
	 */
	@ResponseBody
	@RequestMapping(value = "/updateUserIM")
	public AjaxResultPo updateUserIM(String userInfos, HttpServletRequest request) throws Exception {
		SystemLogUtils.Debug("updateUserIM,userInfos="+userInfos);
		
		/**
		 * 通知CCOD修改坐席
		 */
		String entId = DomainUtils.getEntId(request);
		JSONObject json = new JSONObject(userInfos);
		//DatEntUserPo userpo=userMongoService.queryUserById(json.getString("entId"), json.getString("userId"));
		DatEntUserPo userpo=ParamUtils.getAgentById(json.getString("userId"), entId);

		if(userpo == null){
			SystemLogUtils.Debug("========updateUser,未查询到缓存用户,不通知CCOD修改坐席==========");
			 userpo=userMongoService.queryUserById(entId, json.getString("userId"));
		}
		String userType = userpo.getUserType();
		if(UserType.SERVICE.value.equals(userType)||UserType.ADMINISTRATOR.value.equals(userType)){
			
			CCODRequestPo ccodPo = new CCODRequestPo();
			ccodPo.setEnterpriseId(userpo.getCcodEntId());
			ccodPo.setAgentId(json.getString("userId"));
			if(json.has("userName")){
				ccodPo.setAgentName(json.getString("userName"));
			}else{
				ccodPo.setAgentName(userpo.getUserName());
			}
			ccodPo.setAgentPassword(CdeskEncrptDes.decryptST(userpo.getLoginPwd()));
			String roleId = "4";
			if(json.has("roleId")){
				roleId = json.getString("roleId");
			}
			if(RoleType.MONITOR.value.equals(roleId)){
				ccodPo.setAgentRole(AgentRoleType.MANAGER.value);
			}else{
				ccodPo.setAgentRole(AgentRoleType.NORMAL.value);
			}
			CCODClient.updateAgent(ccodPo);
		}

		String updatorId = SsoSessionUtils.getUserInfo(request).getUserId();
		String updatorName = SsoSessionUtils.getUserInfo(request).getUserName();
	    String updateTime=DateConstants.DATE_FORMAT().format(new Date());
	    DBObject dbo = (DBObject) JSON.parse(userInfos);
	    dbo.put("entId", entId);
	    dbo.put("updateTime", updateTime);
		dbo.put("updatorId", updatorId);
		dbo.put("updatorName", updatorName);
	    
		userMongoService.updateUser(dbo,entId,dbo.get("userId")+"");
		//客服或者管理员修改需要刷新缓存
		if(UserType.SERVICE.value.equals(userType)||UserType.ADMINISTRATOR.value.equals(userType)){
			ParamUtils.refreshCache(CacheGroup.ENT_USER, userpo.getEntId());
		}
	    return AjaxResultPo.successDefault();	
	}
	
	/*添加用户*/
	@ResponseBody
	@RequestMapping(value = "/addUser")
	public AjaxResultPo addUser(String entId, String userInfos, String send, String loginType, HttpServletRequest request) throws Exception {
		userInfos = URLDecoder.decode(userInfos,"utf-8");
		JSONObject json = null;
		DBObject dbo = null;
		try {
			json = new JSONObject(userInfos);
			dbo=(DBObject)JSON.parse(userInfos);
			String loginPwd = null;
			if(dbo.containsField("loginPwd")){
				loginPwd=(String)dbo.get("loginPwd");	
				loginPwd=CdeskEncrptDes.encryptST(json.getString("loginPwd").trim());
				dbo.put("loginPwd", loginPwd);
			}
			String creatorId = SsoSessionUtils.getUserInfo(request).getEmail();
			String creatorName = SsoSessionUtils.getUserInfo(request).getUserName();
            
			String entName = SsoSessionUtils.getUserInfo(request).getEntName();
			
			Date date = new Date();
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time = format.format(date);

			AjaxResultPo ret = new AjaxResultPo(true, "添加成功");
			String userId = userDao.getUserId();
			
			//设置userName,nickName
			if(StringUtils.isBlank((String)dbo.get("userName"))){
				if (loginType.equals(LoginType.MAILBOX.value)) {
					dbo.put("userName", json.getString("loginName").split("@")[0]);
					dbo.put("nickName", json.getString("loginName").split("@")[0]);
				}else{	
					dbo.put("userName", json.getString("loginName"));
					dbo.put("nickName", json.getString("loginName"));
				}
			} else{
				dbo.put("nickName", json.getString("userName"));
			}
			
			if(dbo.containsField("loginName")){
				if (loginType.equals(LoginType.MAILBOX.value)) {
					if(loginPwd != null){
						dbo.put("emailPwd", loginPwd);
					}
					if (userMongoService.existsLoginName(userInfos, request)){
						throw new Exception("邮箱已被注册 ");
					}			
				}else if(loginType.equals(LoginType.TELEPHONE.value)){	
					if(userMongoService.existsLoginName(userInfos, request)){		
						throw new Exception("手机'"+ json.getString("loginName") +"'已被取用.");
					}
				}else if(loginType.equals(LoginType.USER_DEFINE.value)){
					if(userMongoService.existsLoginName(userInfos, request)){		
						throw new Exception("此账号'"+ json.getString("loginName") +"'已被取用.");
					}
				}
			}
			String activeCode = "";
			if (json.has("email") && StringUtils.isNotBlank(json.getString("email"))) {
				activeCode = MD5Util.MD5(json.getString("email") + new Date().getTime());
				dbo.put("activeCode", activeCode);
			}
			dbo.put("entId", entId);		
			dbo.put("userId", userId);
			dbo.put("entName", entName);
			dbo.put("userStatus", UserStatus.NORMAL.value);
			dbo.put("createTime", time);
			dbo.put("updateTime", time);
			dbo.put("updatorId", creatorId);
			dbo.put("updatorName", creatorName);
			dbo.put("creatorId", creatorId);
			dbo.put("creatorName", creatorName);			
			dbo.put("loginType", loginType);
			
			/**
			 * 通知CCOD添加坐席
			 * 管理员和客服才添加坐席
			 */
			String userType = json.getString("userType");
			if(UserType.SERVICE.value.equals(userType)||UserType.ADMINISTRATOR.value.equals(userType)){
				DatEntInfoPo entPo=ParamUtils.getEntInfo(entId);
				CCODRequestPo ccodPo = new CCODRequestPo();
				ccodPo.setEnterpriseId(entPo.getCcodEntId());
				ccodPo.setAgentId(userId);
				ccodPo.setAgentName(json.getString("userName"));
				ccodPo.setAgentPassword(CdeskEncrptDes.decryptST(loginPwd));
				String roleId = "4";
				if(json.has("roleId")){
					roleId = json.getString("roleId");
				}
				if(RoleType.MONITOR.value.equals(roleId)){
					ccodPo.setAgentRole(AgentRoleType.MANAGER.value);
				}else{
					ccodPo.setAgentRole(AgentRoleType.NORMAL.value);
				}
				CCODClient.addAgent(ccodPo);
			}
			
		    ret.setRows(userId);
			
			userMongoService.add(dbo,userId, entId);
			ManageLogUtils.AddSucess(request, "添加用户", json.getString("userName"),"entId=" + entId + ",userType=" + json.getString("userType") + ",roleId=" + json.getString("roleId") +",nickName=" + json.getString("userName")+ ",userName=" + json.getString("userName"));
			
			//客服或者管理员添加需要刷新缓存
			if(UserType.SERVICE.value.equals(userType)||UserType.ADMINISTRATOR.value.equals(userType)){
				ParamUtils.refreshCache(CacheGroup.ENT_USER, entId);
			}
			
			
			
			/* 发送邮件(邮箱不为空且勾选邮件提醒) */
			if (send.equals("true") && StringUtils.isNotBlank(json.getString("email"))) {	
				DatEntUserPo po=new DatEntUserPo();
				po.setEntId(entId);
				po.setEmail(json.getString("email"));
				
				userService.sendMailMongo(request, po, activeCode, false);
			}
			return AjaxResultPo.success("用户添加成功",1,userId);
		} catch (Exception e) {
			e.printStackTrace();
			SystemLogUtils.Debug(String.format("用户 %s 添加失败", json.getString("loginName")));
			return new AjaxResultPo(false, e.getMessage());
		}
	}
		
    /*用户详情页*/
	   @RequestMapping(value = "/userDetails")
	   	public String userDetails(HttpServletRequest request, Model model,HttpServletResponse response) throws Exception {		   
		    String domainName = SsoSessionUtils.getUserInfo(request).getEntId();
		    String loginType=SsoSessionUtils.getUserInfo(request).getUserType();
		    String userId= request.getParameter("userId");
		    
			DBObject queryObject=new BasicDBObject();
			queryObject.put("entId", domainName);
			queryObject.put("userId", userId);
			List<DBObject>  list=userMongoService.queryUserDetail(queryObject, null);
		    
		    model.addAttribute("user", list.get(0));
		    request.setAttribute("entId", domainName);
		    request.setAttribute("loginType", loginType);	
		    
		    /*用户上次登录时间*/		    
		    String loginTime=usrManageService.queryLastLogin(domainName, userId);
		    if(loginTime!=null){
		    	loginTime = loginTime.substring(0, 19);			    	
		    }
		    request.setAttribute("loginTime", loginTime);
		    
		    //获取用户头像
		    PhotoUrlUtil.getPhotoUrl(request, model, domainName, list.get(0).get("photoUrl")+"");
		    
		    String userStatus=list.get(0).get("userStatus")+"";
		    
		    for(UserStatus e:UserStatus.values()){
		    	if(userStatus.equals(e.value)){
		    		userStatus=e.desc;
		    	}
		    }
		    request.setAttribute("userStatus", userStatus);
		    
		    /* 登录账号的用户Id和用户类型*/
		    String userId1=SsoSessionUtils.getUserInfo(request).getUserId();
		    String userType1=SsoSessionUtils.getUserInfo(request).getUserType();
		    String roleId=SsoSessionUtils.getUserInfo(request).getRoleId();
		    
		    request.setAttribute("userId1", userId1);
		    request.setAttribute("userType1", userType1);
		    request.setAttribute("roleId", roleId);
		    		    
		    /*登录人的用户名和账号*/
		    String creatorName=SsoSessionUtils.getUserInfo(request).getUserName();
		    String creatorId=SsoSessionUtils.getUserInfo(request).getLoginName();
		    
		    request.setAttribute("creatorName", creatorName);
		    request.setAttribute("creatorId", creatorId);
		    
		    /*是否为创始人*/
			DBObject queryObj=new BasicDBObject();
			queryObj.put("entId", domainName);
			queryObj.put("userId", userId1);
			List<DBObject>  list1=userMongoService.queryUserDetail(queryObj, null);
			String isFounder=list1.get(0).get("founder")+"";
		    
			model.addAttribute("isFounder",isFounder);
			
		    /*启用的用户自定义字段*/
			List<UserDefinedFiedPo> activeFieldList=userFieldService.queryDefinedFiled(domainName, "1", null);			
			request.setAttribute("activeFieldList", activeFieldList);	
		    
		    return "userManage/userDetails";
	   	}
	   
		/*检测手机是否已经绑定*/
		@ResponseBody
		@RequestMapping(value = "/existsPhone")
		public AjaxResultPo existsPhone(String userInfos, HttpServletRequest request) throws Exception {
			try {	
				    boolean success=userMongoService.existsPhone(userInfos);
				    boolean success1=userMongoService.existsPhone1(userInfos);
				    
				    JSONObject json=new JSONObject(userInfos);
				    String telPhone=json.getString("telPhone");
				    
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
		}
		
		/*检测手机号是否已存在*/
		@ResponseBody
		@RequestMapping(value = "/existsPhone1")
		public AjaxResultPo existsPhone1(String userInfos, String telPhone, HttpServletRequest request) throws Exception {
			try {	
				    boolean success=userMongoService.existsPhone1(userInfos);
					
					if(success){		
						return AjaxResultPo.failed(new Exception("手机'"+telPhone+"'已被取用."));
					}
					else{
						return AjaxResultPo.successDefault();
					}
			} catch (ServiceException e) {	
				e.printStackTrace();
				return new AjaxResultPo(false, e.getMessage());
			}
		}
		
		/**
		 * 检测手机号是否已存在-IM页面调用
		 * @param userInfos
		 * @param telPhone
		 * @param request
		 * @return
		 * @throws Exception
		 * @author wangjie
		 * @time 2016年3月24日下午8:26:39
		 */
		@ResponseBody
		@RequestMapping(value = "/existsPhoneIM")
		public AjaxResultPo existsPhoneIM(String userInfos, String telPhone, HttpServletRequest request) throws Exception {
			try {	
				    boolean success=userMongoService.existsPhoneIM(userInfos,request);
					
					if(success){		
						return AjaxResultPo.failed(new Exception("手机'"+telPhone+"'已被取用."));
					}
					else{
						return AjaxResultPo.successDefault();
					}
			} catch (ServiceException e) {	
				e.printStackTrace();
				return new AjaxResultPo(false, e.getMessage());
			}
		}
		
		/*设置密码*/
		@ResponseBody
		@RequestMapping("/setPwd")
		public AjaxResultPo setPwd(String userInfos, HttpServletRequest request) throws JSONException{
			String email=SsoSessionUtils.getUserInfo(request).getEmail();
			String updatorName = SsoSessionUtils.getUserInfo(request).getUserName();
			
			JSONObject json=new JSONObject(userInfos);
			try {
				
				/**
				 * 通知CCOD修改坐席
				 */
				//DatEntUserPo userpo=userMongoService.queryUserById(json.getString("entId"), json.getString("userId"));
				DatEntUserPo userpo=ParamUtils.getAgentById(json.getString("userId"), json.getString("entId"));
				if(userpo!=null){
					String userType = userpo.getUserType();
					if(UserType.SERVICE.value.equals(userType)||UserType.ADMINISTRATOR.value.equals(userType)){
						CCODRequestPo ccodPo = new CCODRequestPo();
						ccodPo.setEnterpriseId(userpo.getCcodEntId());
						ccodPo.setAgentId(json.getString("userId"));
						if(json.has("userName")){
							ccodPo.setAgentName(json.getString("userName"));
						}else{
							ccodPo.setAgentName(userpo.getUserName());
						}
						if(json.has("loginPwd")){
							ccodPo.setAgentPassword(json.getString("loginPwd"));
						}else{
							ccodPo.setAgentPassword(CdeskEncrptDes.decryptST(userpo.getLoginPwd()));
						}
						String roleId = "4";
						if(json.has("roleId")){
							roleId = json.getString("roleId");
						}
						if(RoleType.MONITOR.value.equals(roleId)){
							ccodPo.setAgentRole(AgentRoleType.MANAGER.value);
						}else{
							ccodPo.setAgentRole(AgentRoleType.NORMAL.value);
						}
						CCODClient.updateAgent(ccodPo);
					}
				}
				
				
				int suc=userMongoService.setPwd(userInfos,email,updatorName);
				
				DBObject queryObject=new BasicDBObject();
				queryObject.put("entId", json.getString("entId"));	
				queryObject.put("userId", json.getString("userId"));	
				List<DBObject> list=userMongoService.queryUserList(queryObject, null);
				
				if(suc>0){
					ManageLogUtils.EditSuccess(request, "修改用户密码", email, "entId="+json.getString("entId")+",email="+email+",password="+json.getString("loginPwd")+",userId="+json.getString("userId"));
					logMongoService.add(request, LogTypeEnum.UPDATE, BusinessTypeEnum.USER, json.getString("userId"), BusinessTypeEnum.USER.desc+"("+list.get(0).get("email")+")", null);
					
					return AjaxResultPo.success("密码安全更新成功！",0,null);
				}
				else{
					ManageLogUtils.EditFail(request, new ServiceException("修改密码失败"), "修改用户密码", email, "entId="+json.getString("entId")+",email="+email+",password="+json.getString("loginPwd")+",userId="+json.getString("userId"));
					return AjaxResultPo.failed(new Exception("设置失败 "));
				}
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				ManageLogUtils.EditFail(request, new ServiceException("修改密码失败,"+e.getMessage()), "修改用户密码", email, "entId="+json.getString("entId")+",email="+email+",password="+json.getString("loginPwd")+",userId="+json.getString("userId"));
				e.printStackTrace();
				
				AjaxResultPo ajaxPo=AjaxResultPo.failed(new Exception("执行出错 "));
				ajaxPo.setRows("forRedirect");	
				return ajaxPo;
			}
		}	
		
		/*删除用户*/
		@ResponseBody
		@RequestMapping(value = "/deleteUser")
		public AjaxResultPo delete(String entId, String[] ids, HttpServletRequest request) throws Exception {
			try {
				String ccodEntId=SsoSessionUtils.getEntInfo(request).getCcodEntId();
				List<DatEntUserPo> uList=new ArrayList<DatEntUserPo>();
				StringBuffer agents=new StringBuffer();
				for(int i=0;i<ids.length;i++){
					DatEntUserPo user=userMongoService.queryUserById(entId, ids[i]);
					/**
					 * 通知CCOD删除坐席
					 * 管理员和客服才删除
					 */
					String userType = user.getUserType();
					if(UserType.SERVICE.value.equals(userType)||UserType.ADMINISTRATOR.value.equals(userType)){
						CCODClient.deleteAgent(user.getCcodEntId(), user.getUserId());
					}
					
					if(Integer.valueOf(user.getUserType())>1){
						if(StringUtils.isNotBlank(agents.toString()))
							agents.append(","+user.getLoginName());
						else
							agents.append(user.getLoginName());
					}
					uList.add(user);
				}
				int success=userMongoService.deleteUser(entId, ids);
			
				ParamUtils.refreshCache(CacheGroup.ENT_USER, entId);
			
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
					
					/*删除用户名下的所有工单*/				
					//EntClient.deleteWf(entId, ids);
				
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
		}
		
		/*批量编辑用户信息*/
		@ResponseBody
		@RequestMapping(value = "/updateBatch")
		public AjaxResultPo updateBatch(String[] ids,String userInfos,DatEntUserPo po, HttpServletRequest request) throws Exception {
			JSONObject json=new JSONObject(userInfos);
			
			String email=SsoSessionUtils.getUserInfo(request).getEmail();
			String updatorName = SsoSessionUtils.getUserInfo(request).getUserName();
			try {
				  
				  String ids1="";
				  for(int i=0;i<ids.length;i++){
					 ids1+=ids[i]+",";
				  }
					  
				int success=userMongoService.updateBatch(ids,userInfos,email,updatorName);
				if(success>0){
					ManageLogUtils.EditSuccess(request, "修改用户", ids.toString(), "entId="+json.getString("entId")+",userLabel="+json.getString("userLabel")+",userStatus="+json.getString("userStatus"));
				
				    logMongoService.add(request, LogTypeEnum.UPDATE, BusinessTypeEnum.USER,email , BusinessTypeEnum.USER.desc+"("+ids1+")", email);	
				    return AjaxResultPo.successDefault();	
				}
				else{
					ManageLogUtils.EditFail(request, new ServiceException("修改失败"), "修改用户", ids.toString(), "entId="+json.getString("entId")+",userLabel="+json.getString("userLabel")+",userStatus="+json.getString("userStatus"));
					return AjaxResultPo.failed(new Exception("更新失败"));
				}				
			} catch (ServiceException e) {
				ManageLogUtils.EditFail(request, new ServiceException("修改失败,"+e.getMessage()), "修改用户", ids.toString(), "entId="+json.getString("entId")+",userLabel="+json.getString("userLabel")+",userStatus="+json.getString("userStatus"));
				e.printStackTrace();
				
				return new AjaxResultPo(false, e.getMessage());
			}
		}
		

		/*更新用户详情复选框类型自定义字段*/
		@ResponseBody
		@RequestMapping(value = "/updateCheckBox")
		public AjaxResultPo updateCheckBox(String key,String value,String entId,String userId,String field,String checked, HttpServletRequest request) throws Exception {
			
			String updatorId=SsoSessionUtils.getUserInfo(request).getEmail();
			String updatorName = SsoSessionUtils.getUserInfo(request).getUserName();
			
			userMongoService.updateCheckBox(key,value,entId,userId,field,checked,updatorId,updatorName);
			
		    return AjaxResultPo.successDefault();	
		}

		/**
		 * 用户头像上传
		 * @param request
		 * @param userId
		 * @return
		 * @author wangjie
		 * @time 2016年1月22日上午9:51:35
		 */
		@ResponseBody
		@RequestMapping("/changePhoto")
		public AjaxResultPo changePhoto(HttpServletRequest request, String userId){
			try{
				DatEntUserPo po = new DatEntUserPo();
				po.setUserId(userId);
				String path = userMongoService.changePhoto(request, po);
				return AjaxResultPo.success("上传成功!", 0, path);
			}catch(ServiceException e){
				e.printStackTrace();
				return new AjaxResultPo(false, e.getMessage());
			}
		}
		
		@ResponseBody
		@RequestMapping("/updatePassword")
		public AjaxResultPo updatePassword(HttpServletRequest request,String userId,String loginPwd,String newLoginPwd){
			String userIdC=SsoSessionUtils.getUserInfo(request).getUserId();
			String entId=null;
			DatEntUserPo loginPo=null;
			try {
				entId=DomainUtils.getEntId(request);
				//loginPo = userMongoService.queryUserByLoginName(email, entId);
				loginPo = userMongoService.queryUserById(entId, userId);
			} catch (ServiceException e1) {
				e1.printStackTrace();
				return AjaxResultPo.failed(e1);
			}
			if(!(userIdC.equals(userId))){
				return AjaxResultPo.failed(new Exception("非当前登录者"));
			}
			if(!(loginPo.getLoginPwd().equals(CdeskEncrptDes.encryptST(loginPwd)))){
				return AjaxResultPo.failed(new Exception("密码不正确"));
			}
			try {
				/**
				 * 通知CCOD修改坐席
				 */
				if(loginPo!=null){
					String userType = loginPo.getUserType();
					if(UserType.SERVICE.value.equals(userType)||UserType.ADMINISTRATOR.value.equals(userType)){
						CCODRequestPo ccodPo = new CCODRequestPo();
						ccodPo.setEnterpriseId(loginPo.getCcodEntId());
						ccodPo.setAgentId(loginPo.getUserId());
						ccodPo.setAgentName(loginPo.getUserName());
						ccodPo.setAgentPassword(loginPwd);
						String roleId = loginPo.getRoleId();
						if(RoleType.MONITOR.value.equals(roleId)){
							ccodPo.setAgentRole(AgentRoleType.MANAGER.value);
						}else{
							ccodPo.setAgentRole(AgentRoleType.NORMAL.value);
						}
						CCODClient.updateAgent(ccodPo);
					}
				}
				
				int update=userMongoService.updatePassword(entId,userId,CdeskEncrptDes.encryptST(newLoginPwd));
				if(update<=0){
					ManageLogUtils.EditFail(request, new BaseException("更新失败"), "更新用户密码", userId, "nickName="+
							loginPo.getNickName()+",entId="+loginPo.getEntId());
					return AjaxResultPo.failed(new Exception("更新失败"));
				}
				ManageLogUtils.EditSuccess(request, "更新用户基本信息", userId, "nickName="+loginPo.getNickName()+
						",entId="+loginPo.getEntId());
			} catch (ServiceException e) {
				ManageLogUtils.EditFail(request, new BaseException("更新失败"), "更新用户密码", userId, "nickName="+
						loginPo.getNickName()+",entId="+loginPo.getEntId());
				e.printStackTrace();
				return AjaxResultPo.failed(e);
			}
			return AjaxResultPo.success("更新成功", 1, null);
		}
		
		/*查询用户*/
		@ResponseBody
		@RequestMapping(value = "/queryUser/{menuId}")
		public AjaxResultPo queryUser(@PathVariable("menuId") String menuId, String entId,String userType,DatEntUserPo po,int rows, int page, HttpServletRequest request,Model model){
			PageInfo pageInfo = new PageInfo((page-1)*rows, rows);
			po.setEntId(entId);
			po.setUserType(userType);
			
			DBObject queryObject=DBObjectUtils.getDBObject(po);
			List<DBObject> list=userMongoService.queryUserList(queryObject, pageInfo);
			//用户头像处理
			for(DBObject vo : list){			
				String photopath = PhotoUrlUtil.getPhotoPath(request, entId, vo.get("photoUrl")+"");
				vo.put("photoUrl", photopath);
			}
			
			String userType1 = SsoSessionUtils.getUserInfo(request).getUserType();
			String roleId = SsoSessionUtils.getUserInfo(request).getRoleId();
			
			/*对普通客服号码隐藏中间四位*/
			if(userType1.equals("2") && roleId.equals("4")){
				for(int i=0;i<list.size();i++){
					if(list.get(i).containsField("telPhone")){
						String tel = list.get(i).get("telPhone").toString();
						if(tel.length() > 8){
							list.get(i).put("telPhone", tel.substring(0, tel.length()-8)+"****"+tel.substring(tel.length()-4, tel.length()));				            				        	
						}
					}
				}				
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
		
		/**
		 * 用户合并的时候根据输入查询用户
		 * @param request
		 * @param value
		 * @param loginName
		 * @param all
		 * @return
		 */
		@ResponseBody
		@RequestMapping(value = "/queryOrdinary")
		public AjaxResultPo queryOrdinaryByValue(HttpServletRequest request,String value,String userId,String loginName,String all){
			String entId=DomainUtils.getEntId(request);
			List<DatEntUserPo> list=null;
			try {
				list=userMongoService.queryOrdinaryByValue(entId,value,loginName,userId);
				for(int i=0;i<list.size();i++){
					list.get(i).setPhotoUrl(PhotoUrlUtil.getPhotoPath(request, entId, list.get(i).getPhotoUrl()));
				}
			} catch (ServiceException e) {
				e.printStackTrace();
				return AjaxResultPo.failed(e);
			}
			return AjaxResultPo.success(all, list.size(), list);
		}
		
		@ResponseBody
		@RequestMapping(value = "/queryOrdinaryByEP")
		public AjaxResultPo queryOrdinaryByEP(HttpServletRequest request,DatEntUserPo po){
			String entId=DomainUtils.getEntId(request);
			List<DatEntUserPo> list=null;
			try {
				list=userMongoService.queryOrdinaryByEP(entId,po);
				for(int i=0;i<list.size();i++){
					list.get(i).setPhotoUrl(PhotoUrlUtil.getPhotoPath(request, entId, list.get(i).getPhotoUrl()));
				}
			} catch (ServiceException e) {
				e.printStackTrace();
				return AjaxResultPo.failed(e);
			}
			return AjaxResultPo.success("查询成功", list.size(), list);
		}
		
		/*
		 * 合并用户
		 */
		@ResponseBody
		@RequestMapping(value = "/mergeUser")
		public AjaxResultPo mergeUser(HttpServletRequest request,String userMergeId,String userTargetId){
			String entId=DomainUtils.getEntId(request);
			String sessionkey=SsoSessionUtils.getUserInfo(request).getSessionKey();
			try {
				userMongoService.mergeUser(entId,userMergeId,userTargetId,sessionkey);
				
				ManageLogUtils.EditSuccess(request, "合并用户", userMergeId+":"+userTargetId,
						"merge users from "+userMergeId+" to "+userTargetId);
				return AjaxResultPo.success("合并成功", 1, "");
			} catch (Exception e) {
				e.printStackTrace();
				ManageLogUtils.EditFail(request, new BaseException("合并失败"), "合并用户", userMergeId+":"+userTargetId,
						"merge users from "+userMergeId+" to "+userTargetId);
				return AjaxResultPo.failed(new Exception("合并失败"));
			}
		}		
		
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
					DatEntUserPo po=userMongoService.queryUserById(entId, idArray[i]);
					uList.add(po);
				}
				
				ret.setRows(uList);	
			}
			return ret;
		}
		
		/*检测邮箱是否已存在*/
		@ResponseBody
		@RequestMapping(value = "/existsEmail")
		public AjaxResultPo existsEmail(String entId, String email, HttpServletRequest request) throws Exception {
			try {	
				    String userId = request.getParameter("userId");
				    boolean success=userMongoService.existsEmailsNotMe(entId,email,userId);
					
					if(success){		
						return AjaxResultPo.failed(new Exception("邮箱'"+email+"'已被取用."));
					}
					else{
						return AjaxResultPo.successDefault();
					}
			} catch (ServiceException e) {	
				e.printStackTrace();
				return new AjaxResultPo(false, e.getMessage());
			}
		}
		
		/**
		 * 检测邮箱是否已存在-IM页面调用
		 * @param email
		 * @param request
		 * @return
		 * @throws Exception
		 * @author wangjie
		 * @time 2016年3月24日下午8:39:15
		 */
		@ResponseBody
		@RequestMapping(value = "/existsEmailIM")
		public AjaxResultPo existsEmailIM(String email, HttpServletRequest request) throws Exception {
			try {	
				String entId = DomainUtils.getEntId(request);
				    boolean success=userMongoService.existsEmails(entId,email);
					
					if(success){		
						return AjaxResultPo.failed(new Exception("邮箱'"+email+"'已被取用."));
					}
					else{
						return AjaxResultPo.successDefault();
					}
			} catch (ServiceException e) {	
				e.printStackTrace();
				return new AjaxResultPo(false, e.getMessage());
			}
		}
		
		
		/*检测登录名是否已存在*/
		@ResponseBody
		@RequestMapping(value = "/existsLoginName")
		public AjaxResultPo existsLoginName(String userInfos, HttpServletRequest request) throws Exception {
			try {	
				    boolean success=userMongoService.existsLoginName(userInfos, request);
					
					if(success){		
						return AjaxResultPo.failed(new Exception("登录名已被取用."));
					}
					else{
						return AjaxResultPo.successDefault();
					}
			} catch (ServiceException e) {	
				e.printStackTrace();
				return new AjaxResultPo(false, e.getMessage());
			}
		}
		
		/**
		 * 云联保版的用户添加页面
		 * @param request
		 * @param entId
		 * @param model
		 * @return
		 */
		@RequestMapping("getUserNewPage")
		public String getUserNewPage(HttpServletRequest request,String entId,Model model){
			entId = DomainUtils.getEntId(request);
			model.addAttribute("entId", entId);
			return "userManage/newUser";
		}
		
		@RequestMapping("userDetailsYlb")
		public String userDetailsLyb(HttpServletRequest request,String entId,String userId,Model model){
			try {
				entId = DomainUtils.getEntId(request);
				DBObject user = userMongoService.queryById(entId, userId);
				user.removeField("loginPwd");
				model.addAttribute("entId", entId);
				model.addAttribute("user", user);
				model.addAttribute("userId", user.get("userId"));
				
				//获取用户头像
			    PhotoUrlUtil.getPhotoUrl(request, model, entId, user.get("photoUrl")+"");
			    
				return "userManage/userDetailsYlb";
			} catch (Exception e) {
				e.printStackTrace();
				return "error";
			}
		}
		/*查询用户*/
		@ResponseBody
		@RequestMapping(value = "/queryChargeman")
		public AjaxResultPo queryChargeman(String entId,DatEntUserPo po,int rows, int page, HttpServletRequest request,Model model){
			PageInfo pageInfo = new PageInfo((page-1)*rows, rows);
			po.setEntId(entId);
			
			DBObject queryObject=DBObjectUtils.getDBObject(po);
			queryObject.put("isCharge", "yes");
			List<DBObject> list=userMongoService.queryUserList(queryObject, pageInfo);
			//用户头像处理
			for(DBObject vo : list){			
				String photopath = PhotoUrlUtil.getPhotoPath(request, entId, vo.get("photoUrl")+"");
				vo.put("photoUrl", photopath);
			}
			
			String userType1 = SsoSessionUtils.getUserInfo(request).getUserType();
			String roleId = SsoSessionUtils.getUserInfo(request).getRoleId();
			
			/*对普通客服号码隐藏中间四位*/
			if(userType1.equals("2") && roleId.equals("4")){
				for(int i=0;i<list.size();i++){
					if(list.get(i).containsField("telPhone")){
						String tel = list.get(i).get("telPhone").toString();
						if(tel.length() > 8){
							list.get(i).put("telPhone", tel.substring(0, tel.length()-8)+"****"+tel.substring(tel.length()-4, tel.length()));				            				        	
						}
					}
				}				
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

		/*检测用户名是否已存在*/
		@ResponseBody
		@RequestMapping(value = "/checkUserName")
		public AjaxResultPo checkUserName(String userInfos, String userName, HttpServletRequest request) throws Exception {
			try {	
				    boolean success=userMongoService.existsUserName(userInfos);
					
					if(success){		
						return AjaxResultPo.failed(new Exception("用户名'"+userName+"'已被取用."));
					}
					else{
						return AjaxResultPo.successDefault();
					}
			} catch (ServiceException e) {	
				e.printStackTrace();
				return new AjaxResultPo(false, e.getMessage());
			}
		}
}

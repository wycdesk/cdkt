package com.channelsoft.ems.register.controller;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import sun.net.util.URLUtil;

import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.logger.LoginLogUtils;
import com.channelsoft.cri.util.CdeskEncrptDes;
import com.channelsoft.cri.util.DomainUtils;
import com.channelsoft.cri.util.WebappConfigUtil;
import com.channelsoft.ems.api.client.EntClient;
import com.channelsoft.ems.ent.po.DatEntInfoPo;
import com.channelsoft.ems.ent.service.IDatEntService;
import com.channelsoft.ems.field.constants.UserFieldStatusEnum;
import com.channelsoft.ems.field.po.BaseFieldPo;
import com.channelsoft.ems.field.po.UserDefinedFiedPo;
import com.channelsoft.ems.field.service.IUserFieldService;
import com.channelsoft.ems.field.util.FieldUtil;
import com.channelsoft.ems.group.constant.GroupType;
import com.channelsoft.ems.group.po.GroupPo;
import com.channelsoft.ems.group.service.IGroupService;
import com.channelsoft.ems.log.constants.BusinessTypeEnum;
import com.channelsoft.ems.log.constants.LogTypeEnum;
import com.channelsoft.ems.log.service.ILogMongoService;
import com.channelsoft.ems.privilege.service.ICfgPermissionService;
import com.channelsoft.ems.privilege.vo.CfgPermissionVo;
import com.channelsoft.ems.redis.constant.CacheGroup;
import com.channelsoft.ems.redis.util.ParamUtils;
import com.channelsoft.ems.register.util.LogoUrlUtil;
import com.channelsoft.ems.sso.constant.SsoParamConstants;
import com.channelsoft.ems.sso.service.ISsoServerService;
import com.channelsoft.ems.sso.util.SsoCookieUtils;
import com.channelsoft.ems.sso.util.SsoSessionUtils;
import com.channelsoft.ems.sso.vo.LoginResultVo;
import com.channelsoft.ems.sso.vo.SsoEntInfoVo;
import com.channelsoft.ems.sso.vo.SsoUserVo;
import com.channelsoft.ems.template.po.TemplatePo;
import com.channelsoft.ems.template.service.ICommHistoryTemplateService;
import com.channelsoft.ems.template.service.ITemplateService;
import com.channelsoft.ems.user.constant.UserAccountType;
import com.channelsoft.ems.user.constant.UserStatus;
import com.channelsoft.ems.user.po.DatEntUserPo;
import com.channelsoft.ems.user.service.IUserMongoService;
import com.channelsoft.ems.user.service.IUserService;
import com.channelsoft.ems.user.service.IUsrManageService;
import com.channelsoft.ems.user.util.PhotoUrlUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * Created by zhouzhongyu on 15/11/9.
 */
@Controller
@RequestMapping("")
public class LoginController {
    @Autowired
	ISsoServerService ssoServerService;
    
    @Autowired
    IDatEntService datEntService;
    
    @Autowired
	ICfgPermissionService permissionService;
    @Autowired
    ILogMongoService logMongoService;

	@Autowired
	IGroupService groupService;
	@Autowired
	IUserService userService;
	
	@Autowired
	IUsrManageService usrManageService;
	
	@Autowired
	IUserMongoService userMongoService;
	
	@Autowired
	IUserFieldService userFieldService;
	
	@Autowired
	ITemplateService templateService;
	@Autowired
	ICommHistoryTemplateService commHistoryTemplateService;
    private Logger logger=Logger.getLogger(LoginController.class);
    
    public String getSkillId(String entId){
    	List<GroupPo> glist=ParamUtils.getEntGroupList(entId);
    	String groupId="";
    	for(GroupPo g:glist){
    		if(GroupType.IM.value.equals(g.getGroupType())){
    			groupId=g.getGroupId();
    			break;
    		}
    	}
    	return groupId;
    }

    /**
     * 访问首页，做平台访问还是企业访问
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/")
   	public String index(HttpServletRequest request, HttpServletResponse response, Model model){
       	String page="index";
       	String entId=DomainUtils.getEntId(request);
       	try{
       		if(StringUtils.isNotBlank(entId)){
           		//平台访问
           		if("www".equalsIgnoreCase(entId)){
           			
           		}
           		//企业访问,跳转到企业首页
           		else{

           			DatEntInfoPo ent=ParamUtils.getEntInfo(entId);
           			logger.info("index,entId="+entId);
           			if(ent==null){
           				logger.info("index,entId="+entId+",企业不存在");
           				page="error";
           			}
           			else{
           				

              			 return login(entId,"","","","","","","", request,response,model);
           			}
           		
           		}
           	}
       	}catch(Exception e){
       		page="error";
       		e.printStackTrace();
       	}
       	
       	return page;
       }
    
    
//    @RequestMapping(value = "/")
//	public String index(HttpServletRequest request, HttpServletResponse response, Model model){
//    	String page="index";
//    	String entId=DomainUtils.getEntId(request);
//    	try{
//    		if(StringUtils.isNotBlank(entId)){
//        		//平台访问
//        		if("www".equalsIgnoreCase(entId)){
//        			
//        		}
//        		//企业访问,跳转到企业首页
//        		else{
//        			response.sendRedirect(request.getContextPath()+"/login");
//        			page="entIndex";
//        			DatEntInfoPo ent=ParamUtils.getEntInfo(entId);
//        			logger.info("index,entId="+entId);
//        			if(ent==null){
//        				logger.info("index,entId="+entId+",企业不存在");
//        				page="error";
//        			}
//        			else{
//        				//获取平台LOGO和Favicon
//        				LogoUrlUtil.getLogoUrl(request, model, entId, ent.getLogoUrl(), ent.getFaviconUrl());
//        				
//        				model.addAttribute("entId", entId);
//            			model.addAttribute("entName", ent.getEntName());
//            			//获取CCOD企业编号
//            			DatEntInfoPo entPo = ParamUtils.getEntInfo(entId);
//            			model.addAttribute("ccodEntId", entPo.getCcodEntId());
//            			
//            			// 获取用户
//            			SsoUserVo user = SsoSessionUtils.getUserInfo(request);
//            			model.addAttribute("user", user);
//            			 //获取用户头像
//            			if (user != null) {
//            				DatEntUserPo user1=userMongoService.queryUserByLoginName(user.getLoginName(), entId);
//            				PhotoUrlUtil.getPhotoUrl(request, model, entId, user1.getPhotoUrl());
//            			}
//            			model.addAttribute("skillId",getSkillId(entId));
//        			}
//        		
//        		}
//        	}
//    	}catch(Exception e){
//    		page="error";
//    		e.printStackTrace();
//    	}
//    	
//    	return page;
//    }
	/**
	 * 平台登陆
	 * 
	 * @param loginName
	 * @param password
	 * @param request
	 * @param model
	 * @return
	 * @CreateDate: 2013-6-7 下午06:41:38
	 * @author 魏铭
	 */
	@RequestMapping(value = "login")
	public String login(String entId, String loginName, String password,String email,String regist,
			String rememberMe, String usePhone, String phoneNumber, HttpServletRequest request, HttpServletResponse response, Model model) {
		try {
			// 读取cookie保存的帐号密码
			if (StringUtils.isBlank(entId)) {
				entId = SsoCookieUtils.getCookieEntId(request);
			}
			if (StringUtils.isBlank(loginName)) {
				loginName = SsoCookieUtils.getCookieLoginName(request);
			}
			if (StringUtils.isBlank(password)) {
				password = SsoCookieUtils.getCookiePassword(request);
			}
			if(StringUtils.isNotBlank(email))
				model.addAttribute("email", email);
			if(StringUtils.isNotBlank(regist))
				model.addAttribute("regist", regist);
	
			//获取CCOD企业编号
			if(StringUtils.isBlank(entId)){
				entId=DomainUtils.getEntId(request);
			}
			DatEntInfoPo entPo = ParamUtils.getEntInfo(entId);
			model.addAttribute("ccodEntId", entPo.getCcodEntId());
			model.addAttribute("skillId",getSkillId(entId));
			//获取平台LOGO和Favicon
			SsoEntInfoVo logoEnt=datEntService.query(DomainUtils.getEntId(request));
			LogoUrlUtil.getLogoUrl(request, model, entId, logoEnt.getLogoUrl(), logoEnt.getFaviconUrl());
				
			model.addAttribute("entId", entId);
   			model.addAttribute("entName", entPo.getEntName());
   
			// 登录
			if (StringUtils.isNotBlank(password)) {
				String desPassword = CdeskEncrptDes.encryptST(password.trim());
//				String MD5Password = DigestUtils.md5Hex(password.trim());
				LoginResultVo result = ssoServerService.login(entId.toLowerCase(), loginName,desPassword, SsoParamConstants.PLATFORM_ID, request);
				SsoSessionUtils.login(result, request);
				LoginLogUtils.LoginSuccess(loginName, SsoParamConstants.PLATFORM_ID);
				
				// 登录成功后帐号密码保存到cookie
				if (StringUtils.isNotBlank(rememberMe)) {
					SsoCookieUtils.saveCookie(entId, loginName, password, request, response);
				}
				
				//判断是否启用电话功能
				if (StringUtils.isNotBlank(usePhone)) {
					model.addAttribute("usePhone", "1");
				}else{
					model.addAttribute("usePhone", "0");
				}
				model.addAttribute("phoneNumber", phoneNumber);
				SsoUserVo user = SsoSessionUtils.getUserInfo(request);
				user.setSessionKey(SsoSessionUtils.getSessionKey(request));
				model.addAttribute("user", user);
				model.addAttribute("enterpriseid", entId);
				model.addAttribute("entName", user.getEntName());
				model.addAttribute("ccodEntId", user.getCcodEntId());//CCOD企业ID
				
				/*更新最新登录时间*/
			    String loginTime = usrManageService.queryLastLogin(entId, user.getUserId());
			    loginTime = loginTime.substring(0, 19);			    
			    DBObject dbo = new BasicDBObject();	
				dbo.put("loginTime", loginTime);			
				userMongoService.updateUser(dbo, entId, user.getUserId());
				
				/* 获取用户头像*/				
				DatEntUserPo user1=userMongoService.queryUserByLoginName(loginName, entId);
				PhotoUrlUtil.getPhotoUrl(request, model, entId, user1.getPhotoUrl());
				
				logMongoService.add(request, LogTypeEnum.LOGIN, BusinessTypeEnum.ELSE, "", "", user);
				
//				return "entIndex";
				if(UserAccountType.ADMINISTRATOR.value.equals(user.getUserType())||UserAccountType.SERVICE.value.equals(user.getUserType())){
					return main(request,response,model);
				}
				else{
					response.sendRedirect(request.getContextPath()+"/activity/my");
				}
				
			}
	
			model.addAttribute("IM_ROOT", WebappConfigUtil.getParameter("IM_ROOT"));
		} catch (ServiceException e) {
			e.printStackTrace();
			LoginLogUtils.LoginFail(loginName, SsoParamConstants.PLATFORM_ID, e);
			model.addAttribute("errMsg", "登陆失败");
			LogoUrlUtil.getLogoUrl(request, model, entId, "", "");
		} catch (Exception e) {
			e.printStackTrace();
			ServiceException ex = new ServiceException("登陆失败");
			LoginLogUtils.LoginFail(loginName, SsoParamConstants.PLATFORM_ID, ex);
			model.addAttribute("errMsg", ex.getDesc());
			LogoUrlUtil.getLogoUrl(request, model, entId, "", "");
		}
		
		return "login";
	}

	@RequestMapping(value = "/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		// 删除cookie
		SsoCookieUtils.deleteCookies(request, response);
		SsoUserVo user=SsoSessionUtils.getUserInfo(request);
		String platformId = "portal";
		String sessionKey = SsoSessionUtils.getSessionKey(request);
		SsoEntInfoVo ent = SsoSessionUtils.getEntInfo(request);
		if (ent == null) {
			String entId=DomainUtils.getEntId(request);
			ent=datEntService.query(entId);
		}
		
		model.addAttribute("entName", ent.getEntName());
		model.addAttribute("entId", ent.getEntId());
		model.addAttribute("ccodEntId", ent.getCcodEntId());
		//获取平台LOGO和Favicon
//		LogoUrlUtil.getLogoUrl(request, model, ent.getEntId(), ent.getLogoUrl(), ent.getFaviconUrl());
//
//		if (StringUtils.isBlank(sessionKey)) {
//			return "entIndex";
//		}
		try {
			String loginName = ssoServerService.logout(ent.getEntId(), sessionKey, platformId);
			SsoSessionUtils.logout(request);
			LoginLogUtils.LogoutSuccess(loginName, platformId);
			logMongoService.add(request,user,LogTypeEnum.LOGOUT, BusinessTypeEnum.ELSE, "", "", null);
		} catch (ServiceException e) {
			LoginLogUtils.LogoutFail(sessionKey, platformId, e);
			e.printStackTrace();
		}
		 return login(ent.getEntId(),"","","","","", "", "", request,response,model);
//		 return "login";
	}

    /**
     * 登陆成功进入首页
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="main")
    public String main(HttpServletRequest request, HttpServletResponse response, Model model){
    	String sessionKey = SsoSessionUtils.getSessionKey(request);
		String roleId = SsoSessionUtils.getUserInfo(request).getRoleId();
		String userType=SsoSessionUtils.getUserInfo(request).getUserType();

		SsoEntInfoVo entiInfo = SsoSessionUtils.getEntInfo(request);
		String enterpriseid = entiInfo.getEntId();
		model.addAttribute("entId", enterpriseid);
		List<CfgPermissionVo> list = permissionService.queryMenuTree(enterpriseid, roleId, sessionKey);
		model.addAttribute("permissionList", list);
		
    	SsoUserVo user = SsoSessionUtils.getUserInfo(request);
		user.setSessionKey(sessionKey);
		user.setUserId(SsoSessionUtils.getUserInfo(request).getUserId());
		model.addAttribute("user", user);
		String pwd=CdeskEncrptDes.decryptST(user.getLoginPwd());
		model.addAttribute("pwd", pwd);
		String workPath=WebappConfigUtil.getParameter("workPath");
		model.addAttribute("workPath", workPath);
		//获取平台LOGO和Favicon
		LogoUrlUtil.getLogoUrl(request, model, entiInfo.getEntId(), entiInfo.getLogoUrl(), entiInfo.getFaviconUrl());
		//获取用户头像
		DatEntUserPo user1=userMongoService.queryUserByLoginName(user.getLoginName(), enterpriseid);
		PhotoUrlUtil.getPhotoUrl(request, model, enterpriseid, user1.getPhotoUrl());
		
	    /*用户上次登陆的时间*/
	    String loginTime = usrManageService.queryLastLogin(enterpriseid, user.getUserId());
	    loginTime = loginTime.substring(0, 19);
	    request.setAttribute("loginTime", loginTime);
	    /**
	     * 签名参数
	     */
	    String skey = WebappConfigUtil.getParameter("ENT_WX_SKEY");
    	String signKey = DigestUtils.md5Hex(enterpriseid + "_" + skey);
    	model.addAttribute("signKey", signKey);
    	
    	/*用户自定义字段*/
		List<UserDefinedFiedPo> fieldList=userFieldService.queryDefinedFiled(enterpriseid, UserFieldStatusEnum.NORMAL.value, null);
		request.setAttribute("activeFieldList", fieldList);	
		model.addAttribute("WebAgent_ROOT", WebappConfigUtil.getParameter("WebAgent_ROOT"));
		
		
		/*判断是否存在默认模板，若无则添加*/
		boolean existDefaultTemp = false;
		List<TemplatePo> tempList = ParamUtils.getTemplateList(enterpriseid, CacheGroup.WORK_TEMPLATE);
		for(TemplatePo po : tempList){
			if(po.isDefault()){
				existDefaultTemp = true;
			}
		}
		if(!existDefaultTemp){
			templateService.addDefaultWorkTemp(enterpriseid);
			
			ParamUtils.refreshCache(CacheGroup.WORK_TEMPLATE, enterpriseid);
			ParamUtils.refreshCache(CacheGroup.WORK_TEMPLATE_FIELD, enterpriseid);
		}
		
		
		
		
		/*判断是否存在默认联络历史模板，若无则添加*/
		boolean existDefaultCommHistoryTemp = false;
		List<TemplatePo> tempCommHistoryList = ParamUtils.getTemplateList(enterpriseid, CacheGroup.CONTACT_HISTORY_TEMPLATE);
		for(TemplatePo po : tempCommHistoryList){
			if(po.isDefault()){
				existDefaultCommHistoryTemp = true;
			}
		}
		if(!existDefaultCommHistoryTemp){
			commHistoryTemplateService.addDefaultCommHistoryTemp(enterpriseid);
			
			ParamUtils.refreshCache(CacheGroup.CONTACT_HISTORY_TEMPLATE, enterpriseid);
			ParamUtils.refreshCache(CacheGroup.CONTACT_HISTORY_TEMPLATE_FIELD, enterpriseid);
		}
		
	    /*是否为创始人*/
		String isFounder=user1.getFounder();
	    
		model.addAttribute("isFounder",isFounder);	
		model.addAttribute("userType", userType);
		model.addAttribute("roleId", roleId);
		
		return "main";
    }

	/**
	 * 控制台首页
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("console/index")
	public String console(HttpServletRequest request, HttpServletResponse response, Model model){
		SsoUserVo user = SsoSessionUtils.getUserInfo(request);
		try {
			model.addAttribute("entId",user.getEntId());
			model.addAttribute("userId", user.getUserId());
			model.addAttribute("userName", user.getUserName());
			model.addAttribute("mailAddr", user.getEmail());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "console/index";
	}

	/**
	 * 工单中心
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("order/index")
	public String order(HttpServletRequest request, HttpServletResponse response, Model model){
		SsoUserVo user = SsoSessionUtils.getUserInfo(request);
		try {
			model.addAttribute("entId",user.getEntId());
			model.addAttribute("userId", user.getUserId());
			model.addAttribute("userName", user.getUserName());
			model.addAttribute("mailAddr", user.getEmail());
			
			List<BaseFieldPo> fieldList=FieldUtil.getOrderCenterTable(user.getEntId());
			request.setAttribute("fieldList", fieldList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "order/index";
	}
	/**
	 * 新建工单
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("newwork/index")
	public String newwork(String selectEmail, String customId,String customName, String param, HttpServletRequest request, HttpServletResponse response, Model model){
		SsoUserVo user = SsoSessionUtils.getUserInfo(request);
		String entId = SsoSessionUtils.getUserInfo(request).getEntId();	
		
		try {
			param = URLDecoder.decode(param,"utf-8");
			if(StringUtils.isNotBlank(param)){
				JSONObject commJson=new JSONObject(param);
				String source = commJson.getString("source");
				
				if(commJson.has("userId")){
					customId=commJson.getString("userId");
				}
				if(commJson.has("userName")){
					customName=commJson.getString("userName");
				}		
				
				model.addAttribute("source", source);
				if(source.equals("5")||source.equals("6")){
					if(commJson.has("sessionId")){
						model.addAttribute("sessionId", commJson.getString("sessionId"));				
						
						model.addAttribute("ccodEntId", commJson.getString("ccodEntId"));	
						model.addAttribute("ccodAgentId", commJson.getString("ccodAgentId"));	
						
						if(StringUtils.isNotBlank(commJson.getString("sessionId"))){							
							JSONObject info=getInfo(user,commJson);							
							String title=info.getString("title");
							String content=info.getString("content");
							
							model.addAttribute("title", title);
							model.addAttribute("content",content);	
							model.addAttribute("source",source);	
						}					
					}else{
						model.addAttribute("sessionId", "");
					}
				}
			}else{
				model.addAttribute("source", "0");
			}

			
			/*由客户Id查询客户信息*/			
			DBObject queryObject=new BasicDBObject();
			queryObject.put("entId", entId);
			queryObject.put("userId", customId);
			List<DBObject>  list=userMongoService.queryUserDetail(queryObject, null);
			if(list.size()!=0){
				model.addAttribute("user", list.get(0));
			    //获取用户头像
			    PhotoUrlUtil.getPhotoUrl(request, model, entId, list.get(0).get("photoUrl")+"");
			    
			    String userStatus=list.get(0).get("userStatus")+"";		    
			    for(UserStatus e:UserStatus.values()){
			    	if(userStatus.equals(e.value)){
			    		userStatus=e.desc;
			    	}
			    }			    
			    model.addAttribute("userStatus", userStatus);
			}
			
		    /*启用的用户自定义字段*/
			List<UserDefinedFiedPo> activeFieldList=userFieldService.queryDefinedFiled(entId, "1", null);			
			model.addAttribute("activeFieldList", activeFieldList);	
			
			model.addAttribute("selectEmail",selectEmail);
			model.addAttribute("entId",user.getEntId());
			model.addAttribute("userId", user.getUserId());
			model.addAttribute("userName", user.getUserName());
			model.addAttribute("mailAddr", user.getEmail());
			model.addAttribute("customId", customId);
			model.addAttribute("customName", list.get(0).get("userName"));
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
		
		return "newwork/index";
	}
	

	/**
	 * 工单详情
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("order/detail")
	public String orderDetail(HttpServletRequest request, HttpServletResponse response,String workId,Model model){
		SsoUserVo user = SsoSessionUtils.getUserInfo(request);
			try {
				if(workId==null){
				throw new Exception();
				}
				String workInfo=EntClient.getWorkInfo(user.getEntId(), workId,user.getSessionKey());
				if(StringUtils.isBlank(workInfo)){
					return "error";
				}
				JSONObject infoAll=new JSONObject(workInfo);
				String rows=infoAll.getString("rows");
				JSONObject info=new JSONObject(rows.substring(1, rows.length()-1));
				
				String customId="";
				/*由客户Id查询客户信息*/				
				if(info.has("customId")){
					customId=info.getString("customId");
				}
				else{
					customId=request.getParameter("customId");
				}
				
				String entId = SsoSessionUtils.getUserInfo(request).getEntId();				
				DBObject queryObject=new BasicDBObject();
				queryObject.put("entId", entId);
				queryObject.put("userId", customId);
				List<DBObject>  list=userMongoService.queryUserDetail(queryObject, null);
			    
				if(list.size()!=0){
					model.addAttribute("user", list.get(0));
					
				    //获取用户头像
				    PhotoUrlUtil.getPhotoUrl(request, model, entId, list.get(0).get("photoUrl")+"");
				    
				    String userStatus=list.get(0).get("userStatus")+"";		    
				    for(UserStatus e:UserStatus.values()){
				    	if(userStatus.equals(e.value)){
				    		userStatus=e.desc;
				    	}
				    }				    
				    model.addAttribute("userStatus", userStatus);
				}
			    		    			    			    
			    /*启用的用户自定义字段*/
				List<UserDefinedFiedPo> activeFieldList=userFieldService.queryDefinedFiled(entId, "1", null);			
				model.addAttribute("activeFieldList", activeFieldList);	
			    
				String tempId="";
				if(info.has("tempId")){					
					tempId = info.getString("tempId");
				}else{
					//默认模板Id 
					List<TemplatePo> allTemplate=ParamUtils.getTemplateList(entId, CacheGroup.WORK_TEMPLATE);
					for(int i=0;i<allTemplate.size();i++){
						if(allTemplate.get(i).isDefault()){
							tempId = allTemplate.get(i).getTempId();
						}
					}					
				}
				
				TemplatePo templatePo=ParamUtils.getTemplate(entId, CacheGroup.WORK_TEMPLATE, tempId);
				model.addAttribute("tempId", templatePo.getTempId());
				
				model.addAttribute("creator",info.getString("creatorName"));
				model.addAttribute("creatorId",info.getString("creatorId"));
				model.addAttribute("status",info.getString("status"));
				model.addAttribute("email",user.getEmail());
				model.addAttribute("workId",workId);
				model.addAttribute("entId",user.getEntId());
				model.addAttribute("userId", user.getUserId());
				model.addAttribute("userName", user.getUserName());
				model.addAttribute("mailAddr", user.getEmail());
				model.addAttribute("avater", "agent");
			} catch (Exception e) {
				e.printStackTrace();
				return "error";
			}
		
		return "order/detail";
	}


	/**
	 * 用户组管理
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("userManage/index")
	public String userManage(HttpServletRequest request, HttpServletResponse response){
		return "userManage/index";
	}

	/**
	 * 帮组中心
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("help/index")
	public String help(HttpServletRequest request, HttpServletResponse response){
		return "help/index";
	}

    /**
     * 功能管理
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("functionManage/index")
	public String functionManage(HttpServletRequest request, HttpServletResponse response,Model model){
    	SsoUserVo user = SsoSessionUtils.getUserInfo(request);
		try {
			model.addAttribute("entId",user.getEntId());
			model.addAttribute("userId", user.getUserId());
			model.addAttribute("userName", user.getUserName());
			model.addAttribute("mailAddr", user.getEmail());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "functionManage/index";
	}

    /**
     * 数据统计
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("dataStatistics/index")
	public String dataStatistics(HttpServletRequest request, HttpServletResponse response){
		return "dataStatistics/index";
	}

    @RequestMapping("reportforms/index")
   	public String reportforms(HttpServletRequest request, HttpServletResponse response){
   		return "reportforms/statisticSurvey";
   	}
    /**
     * 系统设置
     * @param request
     * @param response
     * @return
     */
//    @RequestMapping("systemSetting/index")
//	public String systemSetting(HttpServletRequest request, HttpServletResponse response){
//		return "systemSetting/index";
//	}
    
    @RequestMapping("switchUser")
    public String switchUser(HttpServletRequest request, HttpServletResponse response,String loginName,String loginPwd,Model model){
    	try {
			String entId=DomainUtils.getEntId(request);
			if(StringUtils.isBlank(loginPwd)||StringUtils.isBlank(loginName)){
				throw new Exception("账户或密码不能为空");
			}
			DatEntUserPo userPo=userMongoService.getUserByLName(entId,loginName);
			if(userPo==null){
				throw new Exception("账户不存在");
			}
			String MD5Password=userPo.getLoginPwd();
			
			if(StringUtils.isBlank(MD5Password)){
				throw new Exception("账户还未激活");
			}else{
				if(!MD5Password.equals(loginPwd)){
					throw new Exception("密码不正确");
				}
			}
			//获取平台LOGO和Favicon
			SsoEntInfoVo logoEnt=datEntService.query(entId);
			LogoUrlUtil.getLogoUrl(request, model, entId, logoEnt.getLogoUrl(), logoEnt.getFaviconUrl());
			
			LoginResultVo result = ssoServerService.login(entId, loginName,
					MD5Password, SsoParamConstants.PLATFORM_ID, request);
			SsoSessionUtils.login(result, request);
			LoginLogUtils.LoginSuccess(loginName, "");
			SsoUserVo user = SsoSessionUtils.getUserInfo(request);
			user.setSessionKey(SsoSessionUtils.getSessionKey(request));
		    PhotoUrlUtil.getPhotoUrl(request, model, entId, userPo.getPhotoUrl());
			model.addAttribute("user", user);
			model.addAttribute("enterpriseid", entId);
			model.addAttribute("entName", user.getEntName());
			
			/*更新最新登录时间*/
		    String loginTime = usrManageService.queryLastLogin(entId, user.getUserId());
		    loginTime = loginTime.substring(0, 19);			    
		    DBObject dbo = new BasicDBObject();	
			dbo.put("loginTime", loginTime);			
			userMongoService.updateUser(dbo, entId, user.getUserId());
			
			logMongoService.add(request, LogTypeEnum.LOGIN, BusinessTypeEnum.ELSE, "", "", user);
			if(UserAccountType.ADMINISTRATOR.value.equals(user.getUserType())||UserAccountType.SERVICE.value.equals(user.getUserType())){
//			    return main(request,response,model);
				response.sendRedirect(request.getContextPath()+"/main");
			}
			else{
				response.sendRedirect(request.getContextPath()+"/activity/my");
			}
			model.addAttribute("IM_ROOT", WebappConfigUtil.getParameter("IM_ROOT"));
//			return "entIndex";
		} catch (ServiceException e) {
			e.printStackTrace();
			LoginLogUtils.LoginFail(loginName, SsoParamConstants.PLATFORM_ID, e);
			model.addAttribute("errMsg", e.getDesc());
			LogoUrlUtil.getLogoUrl(request, model, "", "", "");
		} catch (Exception e) {
			e.printStackTrace();
			ServiceException ex = new ServiceException(e.getMessage());
			LoginLogUtils.LoginFail(loginName, SsoParamConstants.PLATFORM_ID, ex);
			model.addAttribute("errMsg", ex.getDesc());
			LogoUrlUtil.getLogoUrl(request, model, "", "", "");
		}
		return "login";
    }
    
    
    /**
     * 提交新问题
     */
    @RequestMapping("newquestion/index")
    public String newQuestion(HttpServletRequest request, Model model,HttpServletResponse response){
    	SsoUserVo user = SsoSessionUtils.getUserInfo(request);
		user.setSessionKey(SsoSessionUtils.getSessionKey(request));
		
		String entId = user.getEntId();
		//获取平台LOGO和Favicon
		SsoEntInfoVo logoEnt=datEntService.query(DomainUtils.getEntId(request));
		LogoUrlUtil.getLogoUrl(request, model, entId, logoEnt.getLogoUrl(), logoEnt.getFaviconUrl());
		
		model.addAttribute("user", user);
		model.addAttribute("enterpriseid", entId);
		model.addAttribute("entName", user.getEntName());
		 //获取用户头像		
		DatEntUserPo user1=userMongoService.queryUserByLoginName(user.getLoginName(), entId);
		PhotoUrlUtil.getPhotoUrl(request, model, entId, user1.getPhotoUrl());
		
		logMongoService.add(request, LogTypeEnum.LOGIN, BusinessTypeEnum.ELSE, "", "", user);
		
		String workPath=WebappConfigUtil.getParameter("workPath");
		model.addAttribute("workPath", workPath);
    	
    	return "uploadNewQuestion";
    }
    
    
    private JSONObject getInfo(SsoUserVo user, JSONObject commJson) throws Exception {
    	JSONObject info=new JSONObject();
    	
    	try{  		
    		String source=commJson.getString("source");
        	
        	SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        	String startTime=commJson.getString("startTime");
        	startTime=(format.parse(startTime)).toLocaleString();
        	      	
        	String title="";
        	String  content="";
        	if(source.equals("6")){
        		String strDnis=commJson.getString("strDnis");
        		     		
        		title="呼出至"+strDnis+"的语音电话";	
        		String opName=commJson.getString("opName");
        		content="<ul><li>电话呼出至："+strDnis+"</li><li>时间:"+startTime+
        				"</li><li>呼出人："+opName+"</li></ul>";
        		   		
        		info.put("title", title);
        		info.put("content", content);  	   		
        	}else if(source.equals("5")){       		
        		String strAni=commJson.getString("strAni"); 
        		
        		title="来自"+strAni+"的语音电话";	
        		content="<ul><li>来自："+strAni+"的语音电话</li><li>时间:"+startTime+
        				"</li><li>呼出人："+strAni+"</li></ul>";
        		   		
        		info.put("title", title);
        		info.put("content", content);
        	}      	
        	info.put("issue", content);
            info.put("sessionId", commJson.getString("sessionId"));
            info.put("ccodEntId", commJson.getString("ccodEntId"));
            info.put("ccodAgentId", commJson.getString("ccodAgentId"));
            info.put("source", commJson.getString("source"));
            
    	}catch(Exception e){
    		e.printStackTrace();
    		throw new Exception("组装创建电话工单信息失败"+e.getMessage());
    	}    	
    	
		return info;
	}
}

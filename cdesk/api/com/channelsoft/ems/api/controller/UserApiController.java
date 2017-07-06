package com.channelsoft.ems.api.controller;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.channelsoft.cri.logger.SystemLogUtils;
import com.channelsoft.cri.util.JsonStrUtils;
import com.channelsoft.cri.util.WebappConfigUtil;
import com.channelsoft.ems.api.po.DatUserSimplePo;
import com.channelsoft.ems.api.po.ResultPo;
import com.channelsoft.ems.redis.util.ParamUtils;
import com.channelsoft.ems.ent.service.IDatEntService;
import com.channelsoft.ems.group.po.AgentPo;
import com.channelsoft.ems.group.po.GroupPo;
import com.channelsoft.ems.group.service.IGroupService;
import com.channelsoft.ems.log.constants.BusinessTypeEnum;
import com.channelsoft.ems.log.constants.LogTypeEnum;
import com.channelsoft.ems.log.service.ILogMongoService;
import com.channelsoft.ems.user.constant.LoginType;
import com.channelsoft.ems.user.po.DatEntUserPo;
import com.channelsoft.ems.user.service.IUserMongoService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;


@Controller
@RequestMapping("/userApi")
public class UserApiController {

	
	@Autowired
	IGroupService groupService;
	@Autowired
	IDatEntService entService;
	@Autowired
	ILogMongoService logMongoService;
	@Autowired
	IUserMongoService userMongoService;

	/**
	 * 查询用户信息,如果该用户不存在，则创建新用户,提供给邮件的专用接口
	 * @param request
	 * @return
	 * @throws JSONException
	 */
	@ResponseBody
	@RequestMapping(value = "/queryUser",produces="text/json;charset=UTF-8")
	public String queryUser(HttpServletRequest request) throws JSONException{
		String result="1";
		String desc="查询失败";
		String userAccount=request.getParameter("userAccount");
		String userAccountType=request.getParameter("userAccountType");
		String entId=request.getParameter("entId");
		SystemLogUtils.Debug("UserApiController.queryUser,userAccount="+userAccount+",userAccountType="+userAccountType+",entId="+entId);
		JSONObject json=new JSONObject();
		try {
			if(StringUtils.isBlank(userAccount)||StringUtils.isBlank(entId)){
				desc="账号或者企业不能为空";
				json.put("rows", "");
			}
			else{
				// 验证企业存在
	        	boolean isExist=ParamUtils.getEntInfo(entId)==null?false:true;
		    	if (!isExist) {
		    		desc="企业不存在";
					json.put("rows", "");
		    	}
		    	else{
		    		if(StringUtils.isBlank(userAccountType)){
						userAccountType=LoginType.MAILBOX.value;
					}
//					DatEntUserPo user=usrManageService.queryOrAddUser(entId, userAccount, userAccountType);
					DatEntUserPo user=userMongoService.queryOrAddUser(entId, userAccount, userAccountType);
					if(user!=null){
						result="0";
						desc="查询成功";
						String rows=JsonStrUtils.getJsonStr(user, "userId,userType,nickName,userName,telPhone");
						json.put("rows", rows);
					}
		    	}
				
			}	
	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		json.put("result", result);
		json.put("desc", desc);
		SystemLogUtils.Debug("UserApiController.queryUser,userAccount="+userAccount+",userAccountType="+userAccountType+",entId="+entId+",返回:"+json.toString());
		return json.toString();
	}
	
	/**
	 * 查询用户详情
	 * @param request
	 * @return
	 * @throws JSONException
	 */
	@ResponseBody
	@RequestMapping(value = "/queryUserDetail",produces="text/json;charset=UTF-8")
	public String queryUserDetail(HttpServletRequest request) throws JSONException{
		String result="1";
		String desc="查询失败";
		String userId=request.getParameter("userId");
		String entId=request.getParameter("entId");
		SystemLogUtils.Debug("UserApiController.queryUserDetail,userId="+userId+",entId="+entId);
		JSONObject json=new JSONObject();
		try {
			if(StringUtils.isBlank(entId)||StringUtils.isBlank(userId)){
				desc="企业或者用户编号不能为空";
				json.put("rows", "");
			}
			else{
				// 验证企业存在
	        	boolean isExist=ParamUtils.getEntInfo(entId)==null?false:true;
		    	if (!isExist) {
		    		desc="企业不存在";
		    	}
		    	else{
		    		DatEntUserPo user=userMongoService.queryUserById(entId, userId);
					String rows=JsonStrUtils.getJsonStr(user, "userId,userType,nickName,telPhone");
					json.put("rows", rows);
					result="0";
					desc="查询成功";
		    	}

			}	
			
	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		json.put("result", result);
		json.put("desc", desc);
		SystemLogUtils.Debug("UserApiController.queryUserDetail,userId="+userId+",entId="+entId+",返回:"+json.toString());
		return json.toString();
	}
	
	/**
	 * 查询用户组内坐席
	 * @param request
	 * @return
	 * @throws JSONException
	 * @throws UnsupportedEncodingException 
	 */
	@ResponseBody
	@RequestMapping(value = "/queryGroupAgent",produces="text/json;charset=UTF-8")
	public String queryGroupAgent(HttpServletRequest request) throws JSONException, UnsupportedEncodingException{
		String result="1";
		String desc="查询失败";
		String groupId=request.getParameter("groupId");
		String entId=request.getParameter("entId");
		SystemLogUtils.Debug("UserApiController.queryGroupAgent,groupId="+groupId+",entId="+entId);
		JSONObject json=new JSONObject();
		try {
			if(StringUtils.isBlank(entId)){
				desc="企业编号不能为空";
			}
			else{
				// 验证企业存在
	        	boolean isExist=ParamUtils.getEntInfo(entId)==null?false:true;
		    	if (!isExist) {
		    		desc="企业不存在";
		    	}
		    	else{
//		    		GroupPo po=new GroupPo();
//					po.setGroupId(groupId);
//					List<GroupPo> group=groupService.queryUserGroup(request, entId, po);			

		    		
					List<GroupPo> group=new ArrayList<>();
					if(StringUtils.isBlank(groupId)){
						group=ParamUtils.getEntGroupList(entId);
					}
					else{
						GroupPo p=ParamUtils.getGroup(entId, groupId);
						if(p!=null) group.add(p);
						
					}
					List<AgentPo> list = groupService.queryGroupAgentForMongo(entId, null);
					List<Object> groupList=new ArrayList<Object>();
					for(GroupPo g:group){
						List<Object> agentList=new ArrayList<Object>();
						for(AgentPo a:list){
							 if(g.getGroupId().equals(a.getGroupId())){
								 agentList.add(a);
							 }
						}
						 List<JSONObject> jList=JsonStrUtils.getJson(agentList, "userId,userName",",");
						JSONObject j=new JSONObject();
						j.put("groupId", g.getGroupId());
						j.put("groupName", g.getGroupName());
						j.put("agentList", jList);
						groupList.add(j);
					}

					json.put("rows", groupList);
					result="0";
					desc="查询成功";
		    	}
				
			}	
	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		json.put("result", result);
		json.put("desc", desc);
		SystemLogUtils.Debug("UserApiController.queryGroupAgent,groupId="+groupId+",entId="+entId+",返回:"+json.toString());
		return json.toString();
	}
	
	/**
	 * 查询用户组内坐席
	 * @param request
	 * @return
	 * @throws JSONException
	 */
	@ResponseBody
	@RequestMapping(value = "/isOnlyGroup",produces="text/json;charset=UTF-8")
	public String isOnlyGroup(HttpServletRequest request) throws JSONException{
		String result="1";
		String desc="查询失败";
		String entId=request.getParameter("entId");
		SystemLogUtils.Debug("UserApiController.isOnlyGroup,entId="+entId);
		JSONObject json=new JSONObject();
		try {
			if(StringUtils.isBlank(entId)){
				desc="企业编号不能为空";
			}
			else{
				// 验证企业存在
	        	boolean isExist=ParamUtils.getEntInfo(entId)==null?false:true;
		    	if (!isExist) {
		    		desc="企业不存在";
		    	}
		    	else{
		    		GroupPo po=new GroupPo();
					AgentPo agent=new AgentPo();
					
					List<GroupPo> group=ParamUtils.getEntGroupList(entId);
//					List<GroupPo> group=groupService.queryUserGroup(request, entId, po);
					if(group!=null &&group.size()==1){
						List<AgentPo> list=groupService.queryGroupAgentForMongo(entId, group.get(0).getGroupId());
						if(list!=null&&list.size()==1){
							agent=list.get(0);
						}
					}
				
				    if(StringUtils.isNotBlank(agent.getUserId())){
	     				String rows=JsonStrUtils.getJsonStr(agent, "userId,agentId,groupId,nickName");
						json.put("rows", rows);
				    }
				    else{
				    	json.put("rows", "");
				    }
					result="0";
					desc="查询成功";
		    	}
				

			}	
	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		json.put("result", result);
		json.put("desc", desc);
		SystemLogUtils.Debug("UserApiController.isOnlyGroup,entId="+entId+",返回:"+json.toString());
		return json.toString();
	}
	
	/**
	 * 查询坐席的分组
	 * @param request
	 * @return
	 * @throws JSONException
	 * @throws UnsupportedEncodingException 
	 */
	@ResponseBody
	@RequestMapping(value = "/queryAgentGroup",produces="text/json;charset=UTF-8")
	public String queryAgentGroup(HttpServletRequest request) throws JSONException, UnsupportedEncodingException{
		String result="1";
		String desc="查询失败";
		String userId=request.getParameter("userId");
		String entId=request.getParameter("entId");
		SystemLogUtils.Debug("UserApiController.queryAgentGroup,userId="+userId+",entId="+entId);
		JSONObject json=new JSONObject();
		try {
			if(StringUtils.isBlank(entId)||StringUtils.isBlank(userId)){
				desc="企业编号或用户编号不能为空";
			}
			else{
				// 验证企业存在
	        	boolean isExist=ParamUtils.getEntInfo(entId)==null?false:true;
		    	if (!isExist) {
		    		desc="企业不存在";
		    	}
		    	else{
					List<GroupPo> group=userMongoService.belongGroup("",userId,entId);
				    List<JSONObject> jList=JsonStrUtils.getJson(group, "groupId,groupName",",");
					json.put("rows", jList);
					result="0";
					desc="查询成功";
		    	}
	
			}	
	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		json.put("result", result);
		json.put("desc", desc);
		SystemLogUtils.Debug("UserApiController.queryAgentGroup,userId="+userId+",entId="+entId+",返回:"+json.toString());
		return json.toString();
	}
	
	
	 /**
	   * 添加用户，登陆帐号和邮箱不能同时为空
	   * @param request
	   * @return
	   */
	  @ResponseBody
	  @RequestMapping(value = "/addUser")
	  public ResultPo addUser(HttpServletRequest request){
			String entId=request.getParameter("entId");
			String email=request.getParameter("email");
			String userAccount=request.getParameter("userAccount");
			String userAccountType=request.getParameter("userAccountType");
			String telphone=request.getParameter("telphone");
			String userName=request.getParameter("userName");
			String signature=request.getParameter("signature");
			String creatorId=request.getParameter("creatorAccount");
			String weixin=request.getParameter("weixin");
			String qq=request.getParameter("qq");
			String weibo=request.getParameter("weibo");
		   	SystemLogUtils.Debug(String.format("添加用户, entId=%s,email=%s,telphone=%s,userAccount=%s,userAccountType=%s,userName=%s,weixin=%s,qq=%s,weibo=%s,signature=%s,creatorId=%s", entId, email,telphone,userAccount,userAccountType,userName,weixin,qq,weibo,signature,creatorId));
		   	
		 // 验证参数不为空
	    	if (StringUtils.isBlank(entId)) {
	    		return ResultPo.failed(new Exception("企业Id为空"));
	    	}
	    	if (StringUtils.isBlank(creatorId)) {
	    		return ResultPo.failed(new Exception("添加人登陆帐号为空"));
	    	}
	    	//登陆帐号和帐号类型就不能为空
	    	if(StringUtils.isBlank(userAccount)||StringUtils.isBlank(userAccountType)){
	    		return ResultPo.failed(new Exception("帐号和账号类型为空"));
	    	}
	    	if (StringUtils.isBlank(userName)) {
	    		return ResultPo.failed(new Exception("用户名为空"));
	    	}
	    	if (StringUtils.isBlank(signature)) {
	    		return ResultPo.failed(new Exception("签名为空"));
	    	}
	    	// 验证签名
	    	String skey = WebappConfigUtil.getParameter("ENT_WX_SKEY");
	    	String signComp = DigestUtils.md5Hex(entId + "_" + skey);
	    	if (!signComp.equals(signature)) {
	    		return ResultPo.failed(new Exception("签名错误"));
	    	}
    		if(StringUtils.isBlank(LoginType.getEnum(userAccountType).value)){
    			return ResultPo.failed(new Exception("帐号类型不存在"));
    		}
    		// 验证企业存在
        	boolean isExist=ParamUtils.getEntInfo(entId)==null?false:true;
	    	if (!isExist) {
	    		return ResultPo.failed(new Exception("企业不存在"));
	    	}

	    	try {
	    		
	        	DatEntUserPo po=new DatEntUserPo();
	        	po.setLoginName(userAccount);
	        	po.setLoginType(userAccountType);
	        	po.setEmail(email);
	        	po.setCreatorId(creatorId);
	        	/**
	        	 * 根据登陆帐号和帐号类型自动生成对应信息
	        	 */
	        	if(LoginType.MAILBOX.value.equals(userAccountType)&&StringUtils.isBlank(email)){
	        		po.setEmail(userAccount);
	        	}
	        	else if(LoginType.TELEPHONE.value.equals(userAccountType)&&StringUtils.isBlank(telphone)){
	        		po.setTelPhone(userAccount);
	        	}
	        	else if(LoginType.WECHAT.value.equals(userAccountType)&&StringUtils.isBlank(weixin)){
	        		po.setWeixin(userAccount);
	        	}
	        	else if(LoginType.QQ.value.equals(userAccountType)&&StringUtils.isBlank(qq)){
	        		po.setQq(userAccount);
	        	}
	        	else if(LoginType.MICROBLOG.value.equals(userAccountType)&&StringUtils.isBlank(weibo)){
	        		po.setWeibo(userAccount);
	        	}

//	        	
//	        	DatEntUserPo u=usrManageService.queryUser(entId, creatorId);
//      		    if(u==null)return ResultPo.failed(new Exception("添加人不存在"));
	        	
	        	po.setTelPhone(telphone);
	        	po.setEntId(entId);
	        	po.setUserName(userName);
	        	po.setNickName(userName);
	        	int num=userMongoService.addCustommer(po);
	        	if(num<1){
	        		SystemLogUtils.Debug(String.format("添加用户失败,数据无更新, entId=%s,email=%s,telphone=%s,userAccount=%s,userAccountType=%s,userName=%s,weixin=%s,qq=%s,weibo=%s,signature=%s,creatorId=%s", entId, email,telphone,userAccount,userAccountType,userName,weixin,qq,weibo,signature,creatorId));
	        	}
	        	DatUserSimplePo userSimplePo=new DatUserSimplePo();
      		    BeanUtils.copyProperties(po, userSimplePo);
      			DatEntUserPo u=po;
      		    SystemLogUtils.Debug(String.format("添加用户成功, entId=%s,email=%s,telphone=%s,userAccount=%s,userAccountType=%s,userName=%s,weixin=%s,qq=%s,weibo=%s,signature=%s,creatorId=%s", entId, email,telphone,userAccount,userAccountType,userName,weixin,qq,weibo,signature,creatorId));
      		    logMongoService.add(request.getRemoteAddr(), u, LogTypeEnum.ADD, BusinessTypeEnum.USER, po.getLoginName(), BusinessTypeEnum.USER.desc+"("+po.getLoginName()+")", po);
	        	return ResultPo.success("添加成功", num, userSimplePo);
	    	}
	    	 catch (Exception e) {
	 			e.printStackTrace();
	 			SystemLogUtils.Debug(String.format("添加用户异常, entId=%s,email=%s,telphone=%s,userAccount=%s,userAccountType=%s,userName=%s,weixin=%s,qq=%s,weibo=%s,signature=%s,creatorId=%s,msg=%s", entId, email,telphone,userAccount,userAccountType,userName,weixin,qq,weibo,signature,creatorId,e.getMessage()));
		 		return ResultPo.failed(new Exception(e.getMessage())); 
	 		}

	 }
	  
	  /**
	   * 修改用户，登陆帐号和邮箱不能同时为空
	   * @param request
	   * @return
	   */
	  @ResponseBody
	  @RequestMapping(value = "/updateUser")
	  public ResultPo updateUser(HttpServletRequest request){
			String entId=request.getParameter("entId");
			String loginName=request.getParameter("loginName");
			String email=request.getParameter("email");
			String telphone=request.getParameter("telphone");
			String userName=request.getParameter("userName");
			String nickName=request.getParameter("nickName");
			String signature=request.getParameter("signature");
			String updatorId=request.getParameter("updatorAccount");
			String weixin=request.getParameter("weixin");
			String qq=request.getParameter("qq");
			String weibo=request.getParameter("weibo");
		   	SystemLogUtils.Debug(String.format("修改用户, entId=%s,email=%s,telphone=%s,loginName=%s,nickName=%s,userName=%s,signature=%s,updatorId=%s,weixin=%s,qq=%s,weibo=%s", entId, email,telphone,loginName,nickName,userName,signature,updatorId,weixin,qq,weibo));
		   	
		   // 验证参数不为空
	    	if (StringUtils.isBlank(entId)) {
	    		return ResultPo.failed(new Exception("企业Id为空"));
	    	}
	    	if (StringUtils.isBlank(updatorId)) {
	    		return ResultPo.failed(new Exception("修改人登陆帐号为空"));
	    	}
	    	//登陆帐号不能为空
	    	if(StringUtils.isBlank(loginName)){
	    		return ResultPo.failed(new Exception("登陆账号为空"));
	    	}
	    	if (StringUtils.isBlank(signature)) {
	    		return ResultPo.failed(new Exception("签名为空"));
	    	}
	    	// 验证签名
	    	String skey = WebappConfigUtil.getParameter("ENT_WX_SKEY");
	    	String signComp = DigestUtils.md5Hex(entId + "_" + skey);
	    	if (!signComp.equals(signature)) {
	    		return ResultPo.failed(new Exception("签名错误"));
	    	}
	    	try {
	    		// 验证企业存在
	    	  	boolean isExist=ParamUtils.getEntInfo(entId)==null?false:true;
	        	if (!isExist) {
	        		return ResultPo.failed(new Exception("企业不存在"));
	        	}
	        	DatEntUserPo u=userMongoService.queryUserByLoginName(updatorId, entId);
//	        	DatEntUserPo u=usrManageService.queryUser(entId, updatorId);
      	    	if(u==null) return ResultPo.failed(new Exception("修改人不存在"));
      		
      	    	
	        	DatEntUserPo po=new DatEntUserPo();
	        	po.setLoginName(loginName);
	        	po.setEmail(email);
	        	po.setTelPhone(telphone);
	        	po.setEntId(entId);
	        	po.setUserName(userName);
	        	po.setNickName(nickName);
	        	po.setUpdatorId(updatorId);
	        	po.setWeixin(weixin);
	        	po.setQq(qq);
	        	po.setWeibo(weibo);
	        	int num=userMongoService.updateUser(po);
	        	if(num<1) {
	        		SystemLogUtils.Debug(String.format("修改用户失败，数据更新没成功, entId=%s,email=%s,telphone=%s,loginName=%s,userName=%s,signature=%s", entId, email,telphone,loginName,userName,signature));
	        		return new ResultPo(false,"修改失败,无该用户");
	        	}
	        	
	        	DatUserSimplePo userSimplePo=new DatUserSimplePo();
      		    BeanUtils.copyProperties(po, userSimplePo);
      		    SystemLogUtils.Debug(String.format("修改用户成功,: entId=%s,email=%s,telphone=%s,loginName=%s,userName=%s,signature=%s", entId, email,telphone,loginName,userName,signature));
      		    logMongoService.add(request.getRemoteAddr(), u, LogTypeEnum.UPDATE, BusinessTypeEnum.USER, po.getLoginName(), BusinessTypeEnum.USER.desc+"("+po.getLoginName()+")", po);
	        	return ResultPo.success("修改成功", num, userSimplePo);
	    	}
	    	 catch (Exception e) {
	 			e.printStackTrace();
	 			SystemLogUtils.Debug(String.format("修改用户异常, entId=%s,email=%s,telphone=%s,loginName=%s,userName=%s,signature=%s,message=%s", entId, email,telphone,loginName,userName,signature,e.getMessage()));
		 		return ResultPo.failed(new Exception(e.getMessage())); 
	 		}

	 }
	
	  
	  /**
	   * 修改用户，如果用户不存在则创建一个用户，存在则更新
	   * @param request
	   * @return
	   */
	  @ResponseBody
	  @RequestMapping(value = "/updateOrAddUser")
	  public void updateOrAddUser(HttpServletRequest request,HttpServletResponse response){
		  userMongoService.updateOrAddUserForSource(request, response);
	 }
	  
	  
	  /*查询用户登录方式*/
		@ResponseBody
		@RequestMapping(value = "/queryLoginType",produces="text/json;charset=UTF-8")
		public String queryLoginType(HttpServletRequest request ) throws JSONException{
			String result="1";
			String desc="查询失败";
			String userId=request.getParameter("userId");
			String entId=request.getParameter("entId");
			SystemLogUtils.Debug("UserApiController.queryLoginType,userId=" + userId + ",entId="+entId);
			JSONObject json=new JSONObject();
			try {
				if(StringUtils.isBlank(userId)||StringUtils.isBlank(entId)){
					desc="用户ID或者企业不能为空";
					json.put("rows", "");
				}
				else{
					// 验证企业存在
		        	boolean isExist=ParamUtils.getEntInfo(entId)==null?false:true;
			    	if (!isExist) {
			    		desc="企业不存在";
						json.put("rows", "");
			    	}
			    	else{
			    		DatEntUserPo user=userMongoService.queryUserById(entId, userId);
			    		
						if(user!=null){
							result="0";
							desc="查询成功";
							String rows=JsonStrUtils.getJsonStr(user, "userId,userType,nickName,userName,telPhone,loginType");
							json.put("rows", rows);
						}
			    	}
					
				}	
		
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			json.put("result", result);
			json.put("desc", desc);
			SystemLogUtils.Debug("UserApiController.queryLoginType,userId="+userId+",entId="+entId+",返回:"+json.toString());
			return json.toString();
		}
		
		
				
		   /*根据email查询客户Id和客户userName*/
			@ResponseBody
			@RequestMapping(value = "/queryUserByEmail",produces="text/json;charset=UTF-8")
			public String queryUserByEmail(HttpServletRequest request,String email,String entId ) throws JSONException{
				String result="1";
				String desc="查询失败";
				
				SystemLogUtils.Debug("UserApiController.queryUserByEmail,email=" + email + ",entId="+entId);
				JSONObject json=new JSONObject();
				try {	
				    DatEntUserPo user=userMongoService.queryUserByEmail(entId, email);
				    		
					if(user!=null){
						result="0";
						desc="查询成功";
						String rows=JsonStrUtils.getJsonStr(user, "userId,userName");
						json.put("rows", rows);
					}
			
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				json.put("result", result);
				json.put("desc", desc);
				SystemLogUtils.Debug("UserApiController.queryUserByEmail,email="+email+",entId="+entId+",返回:"+json.toString());
				return json.toString();
			}
}

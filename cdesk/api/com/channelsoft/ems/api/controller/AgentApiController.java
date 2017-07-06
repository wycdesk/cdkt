package com.channelsoft.ems.api.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;













import com.channelsoft.cri.logger.SystemLogUtils;
import com.channelsoft.cri.util.DBObjectUtils;
import com.channelsoft.cri.util.WebappConfigUtil;
import com.channelsoft.ems.api.po.AgentSimplePo;
import com.channelsoft.ems.api.po.DatUserSimplePo;
import com.channelsoft.ems.api.po.GroupSimplePo;
import com.channelsoft.ems.api.po.ResultPo;
import com.channelsoft.ems.redis.constant.CacheObjects;
import com.channelsoft.ems.redis.util.ParamUtils;
import com.channelsoft.ems.ent.service.IDatEntService;
import com.channelsoft.ems.group.po.AgentPo;
import com.channelsoft.ems.group.po.GroupPo;
import com.channelsoft.ems.group.service.IGroupService;
import com.channelsoft.ems.log.constants.BusinessTypeEnum;
import com.channelsoft.ems.log.constants.LogTypeEnum;
import com.channelsoft.ems.log.service.ILogMongoService;
import com.channelsoft.ems.sso.vo.SsoEntInfoVo;
import com.channelsoft.ems.user.constant.LoginType;
import com.channelsoft.ems.user.constant.UserStatus;
import com.channelsoft.ems.user.constant.UserType;
import com.channelsoft.ems.user.po.DatEntUserPo;
import com.channelsoft.ems.user.service.IUserMongoService;
import com.channelsoft.ems.user.service.IUsrManageService;
import com.mongodb.DBObject;

@Controller
@RequestMapping("/agentApi")
public class AgentApiController {

	@Autowired
	IDatEntService entService;

	@Autowired
	IUsrManageService usrManageService;
	@Autowired
	IGroupService groupService;

	@Autowired
	IUserMongoService userMongoService;
	/**
	 * 根据用户ID查询用户
	 * @param request
	 * @return
	 */
	/*@ResponseBody
	@RequestMapping(value = "/queryUserById",produces="text/json;charset=UTF-8")
	public String queryUserById(HttpServletRequest request,HttpServletResponse response) {
		String userId=request.getParameter("userId");
		String entId=request.getParameter("entId");
		String signature=request.getParameter("signature");
	   	SystemLogUtils.Debug(String.format("查询用户: entId=%s,userId=%s,signature=%s", entId, userId, signature));
	   // 验证参数不为空
    	if (StringUtils.isBlank(entId)) {
    		return ResultPo.failed(new Exception("企业Id为空")).returnJSONP(request);
    	}
    	if (StringUtils.isBlank(userId)) {
    		return ResultPo.failed(new Exception("用户编号为空")).returnJSONP(request);
    	}
    	if (StringUtils.isBlank(signature)) {
    		return ResultPo.failed(new Exception("签名为空")).returnJSONP(request);
    	}
    	// 验证签名
    	String skey = WebappConfigUtil.getParameter("ENT_WX_SKEY");
    	String signComp = DigestUtils.md5Hex(entId + "_" + skey);
    	if (!signComp.equals(signature)) {
    		return ResultPo.failed(new Exception("签名错误")).returnJSONP(request);
    	}
    	try {
    		// 验证企业存在
//        	boolean isExist=entService.existThisEntId(entId);
        	boolean isExist=ParamUtils.getEntInfo(entId)==null?false:true;
        	if (!isExist) {
        		return ResultPo.failed(new Exception("企业不存在")).returnJSONP(request);
        	}
        	
        	DatEntUserPo user=usrManageService.queryUserById(entId, userId);
    //          	DatEntUserPo user=userMongoService.queryUserById(entId, userId);
        	if(user==null){
        		return ResultPo.success("查询成功", 0, null).returnJSONP(request);
        	}
        	else{
        		DatUserSimplePo userSimplePo=new DatUserSimplePo();
        		BeanUtils.copyProperties(user, userSimplePo);
        		return ResultPo.success("查询成功", 1, userSimplePo).returnJSONP(request);
        	}

    	}
    	 catch (Exception e) {
 			e.printStackTrace();
 		}
    	return ResultPo.failed(new Exception("系统异常")).returnJSONP(request);
		 
	}*/	
	/**
	 * 根据登录帐号查询用户
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/queryUser",produces="text/json;charset=UTF-8")
	public String queryUser(HttpServletRequest request,HttpServletResponse response) {
		String userAccount=request.getParameter("userAccount");
		String userAccountType=request.getParameter("userAccountType");
		String entId=request.getParameter("entId");
		String signature=request.getParameter("signature");
	   	SystemLogUtils.Debug(String.format("查询用户: entId=%s,userAccount=%s,userAccountType=%s,signature=%s", entId, userAccount, userAccountType, signature));
	   // 验证参数不为空
    	if (StringUtils.isBlank(entId)) {
    		return ResultPo.failed(new Exception("企业Id为空")).returnJSONP(request);
    	}
    	if (StringUtils.isBlank(userAccountType)) {
    		return ResultPo.failed(new Exception("账号为空")).returnJSONP(request);
    	}
    	if (StringUtils.isBlank(userAccountType)) {
    		return ResultPo.failed(new Exception("账号类型为空")).returnJSONP(request);
    	}
    	if (StringUtils.isBlank(signature)) {
    		return ResultPo.failed(new Exception("签名为空")).returnJSONP(request);
    	}
    	// 验证签名
    	String skey = WebappConfigUtil.getParameter("ENT_WX_SKEY");
    	String signComp = DigestUtils.md5Hex(entId + "_" + skey);
    	if (!signComp.equals(signature)) {
    		 ResultPo.failed(new Exception("签名错误")).returnJSONP(request, response);
    	}
    	try {
    		// 验证企业存在
//        	boolean isExist=entService.existThisEntId(entId);
        	boolean isExist=ParamUtils.getEntInfo(entId)==null?false:true;
        	if (!isExist) {
        		return  ResultPo.failed(new Exception("企业不存在")).returnJSONP(request);
        	}
//        	List<DatEntUserPo> list=usrManageService.query(entId, userAccount, userAccountType);
//        	List<DatUserSimplePo> userList=new ArrayList<DatUserSimplePo>();
//        	for(DatEntUserPo u:list){
//        		DatUserSimplePo userSimplePo=new DatUserSimplePo();
//        		BeanUtils.copyProperties(u, userSimplePo);
//        		userList.add(userSimplePo);
//        	}
        	List<DBObject> list=userMongoService.query(entId, userAccount, userAccountType);
        	List<DatUserSimplePo> userList=new ArrayList<DatUserSimplePo>();
        	for(DBObject d:list){
        		DatUserSimplePo userSimplePo=new DatUserSimplePo();
           		DBObjectUtils.getObject(d, userSimplePo);
        		userList.add(userSimplePo);
        	}
        	return ResultPo.success("查询成功", userList.size(), userList).returnJSONP(request);
    	}
    	 catch (Exception e) {
 			e.printStackTrace();
 		}
    	return ResultPo.failed(new Exception("系统异常")).returnJSONP(request);
		 
	}	
	/**
	 * 查询所有正常状态坐席
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/queryAllAgent")
	public ResultPo queryAllAgent(HttpServletRequest request){
		String entId=request.getParameter("entId");
		String signature=request.getParameter("signature");
	   	SystemLogUtils.Debug(String.format("查询所有坐席: entId=%s, signature=%s", entId, signature));
	   	
	 // 验证参数不为空
    	if (StringUtils.isBlank(entId)) {
    		return ResultPo.failed(new Exception("企业Id为空"));
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
        	boolean isExist=ParamUtils.getEntInfo(entId)==null?false:true;
        	if (!isExist) {
        		return ResultPo.failed(new Exception("企业不存在"));
        	}
        	
        	Map<String, List<DatEntUserPo>> map = CacheObjects.getUserList().get(entId);
    		if (map == null) {
    			return null;
    		}
    		List<DatEntUserPo> list = map.get(UserType.SERVICE.value);
    		List<DatEntUserPo> adminList = map.get(UserType.ADMINISTRATOR.value);
    		list.addAll(adminList);
        	List<AgentSimplePo> agentList=new ArrayList<AgentSimplePo>();
        	for(DatEntUserPo u:list){
        		if(!UserStatus.NORMAL.value.equals(u.getUserStatus())) continue;
        		AgentSimplePo agentSimplePo=new AgentSimplePo();
        		BeanUtils.copyProperties(u, agentSimplePo);
        		agentList.add(agentSimplePo);
        	}
        	return ResultPo.success("查询成功", agentList.size(), agentList);
    	}
    	 catch (Exception e) {
 			e.printStackTrace();
 		}
 		return ResultPo.failed(new Exception("系统异常"));
	}
	/**
	 * 查询技能组（客服分组）
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/querySkillGroup")
	public ResultPo querySkillGroup(HttpServletRequest request){
		String entId=request.getParameter("entId");
		String groupId=request.getParameter("groupId");
		String signature=request.getParameter("signature");
	   	SystemLogUtils.Debug(String.format("查询所有客服组: entId=%s,groupId=%s, signature=%s", entId, groupId,signature));
	   	
	 // 验证参数不为空
    	if (StringUtils.isBlank(entId)) {
    		return ResultPo.failed(new Exception("企业Id为空"));
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
//        	boolean isExist=entService.existThisEntId(entId);
        	boolean isExist=ParamUtils.getEntInfo(entId)==null?false:true;
        	if (!isExist) {
        		return ResultPo.failed(new Exception("企业不存在"));
        	}
  
			List<GroupSimplePo> groupList=new  ArrayList<GroupSimplePo>();
			if(StringUtils.isBlank(groupId)){
				groupList=ParamUtils.getEntGroupAndAgent(entId);
			}
			else{

				groupList=ParamUtils.getEntGroupAndAgent(entId, groupId);
			}
			
	
        	return ResultPo.success("查询成功", groupList.size(), groupList);
    	}
    	 catch (Exception e) {
 			e.printStackTrace();
 		}
 		return ResultPo.failed(new Exception("系统异常"));
	}
	
	 
}

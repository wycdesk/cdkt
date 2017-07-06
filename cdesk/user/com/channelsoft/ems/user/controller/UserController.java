package com.channelsoft.ems.user.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.channelsoft.cri.exception.BaseException;
import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.logger.ManageLogUtils;
import com.channelsoft.cri.logger.SystemLogUtils;
import com.channelsoft.cri.util.DomainUtils;
import com.channelsoft.cri.vo.AjaxResultPo;
import com.channelsoft.ems.api.po.SendMainResponsePo;
import com.channelsoft.ems.log.constants.BusinessTypeEnum;
import com.channelsoft.ems.log.constants.LogTypeEnum;
import com.channelsoft.ems.log.service.ILogMongoService;
import com.channelsoft.ems.privilege.constant.RoleType;
import com.channelsoft.ems.register.po.RegisterInfoPo;
import com.channelsoft.ems.register.util.MD5Util;
import com.channelsoft.ems.sso.util.SsoSessionUtils;
import com.channelsoft.ems.sso.vo.SsoUserVo;
import com.channelsoft.ems.user.constant.LoginType;
import com.channelsoft.ems.user.constant.UserStatus;
import com.channelsoft.ems.user.constant.UserType;
import com.channelsoft.ems.user.po.DatEntUserPo;
import com.channelsoft.ems.user.service.IUserService;
import com.channelsoft.ems.user.util.PhotoUrlUtil;

@Controller
@RequestMapping("/user")
public class UserController {
	private Logger log=LoggerFactory.getLogger(UserController.class);
	@Autowired
	IUserService userService;
	
	@Autowired
	ILogMongoService logMongoService;
	
	
	
	/*@ResponseBody
	@RequestMapping("/registerBase")
	public AjaxResultPo registerBase(HttpServletRequest request,DatEntUserPo po,String picCode) throws Exception{
		String entId=DomainUtils.getEntId(request);
		if(entId.equals("")){
			SystemLogUtils.Debug("企业为空");
			return AjaxResultPo.failed(new Exception("企业为空 "));
		}
		RegisterInfoPo entPo=userService.getEntInfo(entId);
		String checkCode=(String)(request.getSession().getAttribute("checkcode"));
		String activeCode=MD5Util.MD5(po.getEmail()+new Date().getTime());
		if(picCode.equalsIgnoreCase(checkCode)){
			po.setActiveCode(activeCode);
			po.setEntId(entPo.getEntId());
			po.setEntName(entPo.getEntName());
			po.setUserType(UserType.CUSTOMER.value);
			po.setLoginType(LoginType.MAILBOX.value);
			po.setUserStatus(UserStatus.FORACTIVE.value);
			po.setLoginName(po.getEmail());
			po.setRoleId(RoleType.CUSTOMER.value);
			try {
				if(userService.existsEmails(po.getEntId(), po.getEmail())){
					ManageLogUtils.AddFail(request, new ServiceException("邮箱已经被注册"), "注册用户",
							po.getEmail(), "entId="+po.getEntId());
					return AjaxResultPo.failed(new Exception("邮箱已经被注册 "));
				}
				int add=userService.registerBase(request,po,activeCode);
				if(add>0){
					//用户注册成功日志
					ManageLogUtils.AddSucess(request, "注册用户", po.getEmail(), 
						"entId="+po.getEntId()+",userType="+po.getUserType()+
						",roleId="+po.getRoleId()+",nickName="+po.getNickName()+
						",userName="+po.getUserName());
					return AjaxResultPo.success("注册成功,请查看邮箱进行激活",0,activeCode);
				}else{
					//用户注册失败日志
					ManageLogUtils.AddFail(request, new ServiceException("注册失败"), "注册用户",po.getEmail(), 
							"entId="+po.getEntId()+",userType="+po.getUserType()+",roleId="+po.getRoleId()+
							",nickName="+po.getNickName()+",userName="+po.getUserName());
					return AjaxResultPo.failed(new Exception("注册失败"));
				}
			}catch (ServiceException e) {
				ManageLogUtils.AddFail(request, new BaseException(e.getMessage()), "注册用户",
					po.getEmail(), "entId="+po.getEntId()+",userType="+po.getUserType()+
					",roleId="+po.getRoleId()+",nickName="+po.getNickName()+",userName="+po.getUserName());
				e.printStackTrace();
				return AjaxResultPo.failed(e);
			} catch (Exception e){
				ManageLogUtils.AddFail(request, new BaseException(e.getMessage()), "注册用户",
					po.getEmail(), "entId="+po.getEntId()+",userType="+po.getUserType()+",roleId="+po.getRoleId()
					+",nickName="+po.getNickName()+",userName="+po.getUserName());
				e.printStackTrace();
				return AjaxResultPo.failed(e);
			}
		}else{
			return AjaxResultPo.failed(new Exception("验证码错误 "));
		}
	}*/
	/*@RequestMapping("/registerPwd")
	public String registerPwd(HttpServletRequest request,String code,Model model) throws ParseException{
		String entId=null;
		String email=null;
		try {
			String codeauto=request.getParameter("codeauto");
			boolean type=false;
			if(StringUtils.isBlank(code)&&StringUtils.isNotBlank(codeauto)){
				code=codeauto;
				 type=true;
			}
			entId=DomainUtils.getEntId(request);
			if(entId.equals("")){
				log.error("企业为空");
				return "error";
			}
			DatEntUserPo po=userService.getEntUserPo(entId,code,null);
			if(po.getLoginPwd()!=null){
				if(SsoSessionUtils.isHasLogin(request)&&SsoSessionUtils.getUserInfo(request).getLoginName().equals(po.getLoginName())){
					SsoUserVo user = SsoSessionUtils.getUserInfo(request);
					user.setSessionKey(SsoSessionUtils.getSessionKey(request));
					model.addAttribute("user", user);
					model.addAttribute("enterpriseid", entId);
    				DatEntUserPo userPo = userService.queryUser(entId, user.getUserId());
    			    PhotoUrlUtil.getPhotoUrl(request, model, entId, userPo.getPhotoUrl());
					return "entIndex";
				}else{
					return "login";
				}
				
			}
			String createStr=po.getCreateTime();
			
			Date create=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(createStr);
			Date end=new Date();
			long time=(end.getTime()-create.getTime())/1000;
			if(time>1800){
				email=userService.deleteUser(entId,code);
				model.addAttribute("email", email);
				SystemLogUtils.Fail("UserController", "用户激活", email, new BaseException("时间过期"));
				if(StringUtils.isNotBlank(email)){
					ManageLogUtils.DeleteSuccess(request, "删除未激活用户", email, "entId="+po.getEntId()+",userType="+
							po.getUserType()+",roleId="+po.getRoleId()+",nickName="+po.getNickName()+",userName="+
							po.getUserName());
				}
				return "user/timeOut";
			}else{
				model.addAttribute("code", code);
				model.addAttribute("entId", entId);
				model.addAttribute("loginName", po.getLoginName());
				SystemLogUtils.Success("UserController", "激活用户", email);
				if(!type){
//				if(StringUtils.isNotBlank(po.getNickName())){
					return "user/setPwd";
				}else{
					return "user/setPwdAndName";
				}
			}
		} catch (Exception e) {
			SystemLogUtils.Fail("UserController", "用户激活", email, new BaseException("激活异常"));
			e.printStackTrace();
			return "error";
		}
	}*/
	/*@ResponseBody
	@RequestMapping("/setPwd")
	public AjaxResultPo setPwd(HttpServletRequest request,String userName,String nickName,String password,String code){
		DatEntUserPo po=null;
		try {
			String entId=DomainUtils.getEntId(request);
			int suc=userService.registerPwd(userName,nickName,password,entId,code);
			po=userService.getEntUserPo(entId, code, null);
			if(po==null)
				return AjaxResultPo.failed(new Exception("激活码已过期 "));
			if(suc>0){
				ManageLogUtils.EditSuccess(request, "设置密码", po.getEmail(),"entId="+po.getEntId()+",userType="+
						po.getUserType()+",roleId="+po.getRoleId()+",nickName="+po.getNickName()+",userName="+
						po.getUserName());
				return AjaxResultPo.success("密码设置成功",0,null);
			}else{
				ManageLogUtils.EditFail(request, new BaseException("密码设置失败"), "设置密码", po.getEmail(),"entId="+
						po.getEntId()+",userType="+po.getUserType()+",roleId="+po.getRoleId()+",nickName="+
						po.getNickName()+",userName="+po.getUserName());
				return AjaxResultPo.failed(new Exception("设置失败 "));
			}
		}catch (ServiceException e) {
			ManageLogUtils.EditFail(request, new BaseException("密码设置失败"), "设置密码", po.getEmail(),"entId="+
					po.getEntId()+",userType="+po.getUserType()+",roleId="+po.getRoleId()+",nickName="+
					po.getNickName()+",userName="+po.getUserName());
			AjaxResultPo ajaxPo=AjaxResultPo.failed(new Exception("执行出错 "));
			ajaxPo.setRows("forRedirect");
			return ajaxPo;
		}
	}*/
	/**
	 * 忘记密码
	 * @param request
	 * @param email
	 * @param po
	 * @param picCode
	 * @return
	 * @throws Exception 
	 */
	/*@ResponseBody
	@RequestMapping("/forgetPwd")
	public AjaxResultPo forgetPwd(HttpServletRequest request,String email,DatEntUserPo po,String picCode) throws Exception{
		String checkCode=(String)(request.getSession().getAttribute("checkcode"));
		//判断验证码是否正确
		if(picCode.equalsIgnoreCase(checkCode)){
			String entId=DomainUtils.getEntId(request);
			//判断邮箱是否已经注册
			if(userService.existsEmails(entId, email)){
				try {
					String activeCode=MD5Util.MD5(email+new Date().getTime());
					DatEntUserPo userPo=userService.getEntUserPo(entId,null,email);
					userPo.setActiveCode(activeCode);
					userService.resetUser(userPo);
					if(userPo.getLoginPwd()==null){
						userService.sendMail(request,userPo,activeCode,false);
						SystemLogUtils.Success("UserController", "忘记密码", email);
						return AjaxResultPo.success("账号尚未激活，新的激活链接已经发到你的邮箱里了，请在30分钟内激活！",0,activeCode);
					}else{
						userService.sendMail(request,userPo,activeCode,true);
						SystemLogUtils.Success("UserController", "忘记密码", email);
						return AjaxResultPo.success("重置密码链接已发到邮箱，请在1个小时内完成重置！！",1,activeCode);
					}
				} catch (Exception e) {
					SystemLogUtils.Fail("UserController", "忘记密码",email, new BaseException(e.getMessage()));
					e.printStackTrace();
					return AjaxResultPo.failed(e);
				}
				
			}else{
				SystemLogUtils.Fail("UserController", "忘记密码", email, new BaseException("邮箱错误"));
				return AjaxResultPo.failed(new Exception("邮箱错误 "));
			}
		}else{
			return AjaxResultPo.failed(new Exception("验证码错误 "));
		}
	}*/
	/*@RequestMapping("/resetPwd")
	public String resetPwd(HttpServletRequest request,String code,Model model) throws ParseException{
		DatEntUserPo po=null;
		try {
			String entId=DomainUtils.getEntId(request);
			if(entId.equals("")){
				log.error("企业为空");
				return "error";
			}
			po=userService.getEntUserPo(entId,code,null);
			String createStr=po.getCreateTime();
			Date create=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(createStr);
			Date end=new Date();
			long time=(end.getTime()-create.getTime())/1000;
			if(time>3600){
				SystemLogUtils.Fail("UserController", "重置密码", po.getEmail(), new BaseException("密码重置超时"));
				return "user/resetTimeOut";
			}else{
				model.addAttribute("code", code);
				model.addAttribute("entId", entId);
				model.addAttribute("loginName", po.getLoginName());
				model.addAttribute("title", "重置密码");
				SystemLogUtils.Success("UserController", "重置密码", po.getEmail());
				return "user/setPwd";
			}
		} catch (Exception e) {
			SystemLogUtils.Fail("UserController", "重置密码", po.getEmail(), new BaseException("密码重置异常"));
			return "error";
		}
	}*/
	@RequestMapping("/regSuccess")
	public String registSuccess(HttpServletRequest request,String email,String code,String nickName,Model model){
		model.addAttribute("email", email);
		model.addAttribute("code", code);
		model.addAttribute("nickName", nickName);
		return "user/registerSuccess";
	}
	@ResponseBody
	@RequestMapping("/emailResend")
	public AjaxResultPo emailResend(HttpServletRequest request,String code,String email){
		String entId=DomainUtils.getEntId(request);
		DatEntUserPo po=new DatEntUserPo();
		po.setEmail(email);
		po.setEntId(entId);
		try {
			SendMainResponsePo resPo=userService.sendMail(request,po,code,false);
			if(!resPo.getResult().equals("0"))
				return AjaxResultPo.failed(new Exception(resPo.getDesc()));
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxResultPo.failed(new Exception(e.getMessage()));
		}
		return AjaxResultPo.success("邮件发送成功,请查看邮箱进行激活", 1, "");
	}
	/*@ResponseBody
	@RequestMapping("/queryUserById")
	public AjaxResultPo queryUserById(HttpServletRequest request,String userId,String loginType){
		try {
			String entId=DomainUtils.getEntId(request);
			DatEntUserPo po=userService.queryUser(entId, userId);
			if(Integer.valueOf(loginType)<Integer.valueOf(po.getUserType())){
				return AjaxResultPo.failed(new Exception("权限不够"));
			}
			if(StringUtils.isBlank(po.getLoginPwd())){
				return AjaxResultPo.failed(new Exception("用户尚未激活"));
			}
			return AjaxResultPo.success("获取用户成功", 1, po);
		} catch (ServiceException e) {
			e.printStackTrace();
			return AjaxResultPo.failed(e);
		}
	}*/
	
}

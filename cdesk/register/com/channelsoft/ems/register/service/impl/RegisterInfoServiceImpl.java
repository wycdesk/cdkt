package com.channelsoft.ems.register.service.impl;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.ems.api.client.MailSendClient;
import com.channelsoft.ems.register.dao.IRegisterInfoDao;
import com.channelsoft.ems.register.po.RegisterInfoPo;
import com.channelsoft.ems.register.service.IRegisterInfoService;
import com.channelsoft.ems.register.util.ConfigUtil;
import com.channelsoft.ems.register.util.MD5Util;
import com.channelsoft.ems.register.util.SendEmail;

public class RegisterInfoServiceImpl implements IRegisterInfoService{
    
	@Autowired 
	private IRegisterInfoDao registerDao;
	
	public void doRegister(RegisterInfoPo register,HttpServletRequest request) throws Exception {	
		/*激活码:邮箱+时间*/
		String activeCode = MD5Util.MD5(register.getEmail() + new Date().getTime());
		
		Serializable entid = null;
		/*向注册表添加信息*/
		register.setActiveCode(activeCode);
		register.setStatus("0");		
		try {
			 registerDao.addRegister(register);
		} catch (Exception e) {				
			throw new ServiceException("保存用户注册信息失败。。。。");
		}		
		/* 邮件的内容*/
		sendRegistEmail(register.getEmail(), activeCode, request);			
	}
	
	/*发送邮件*/
	private void sendRegistEmail(String email, String activeCode,HttpServletRequest request) throws Exception {
		String emailContent = ConfigUtil.getString("register.email.content");
		
	    String path1=request.getHeader("Host");
	    String path2=request.getContextPath();	
	    
	    String path="http://"+path1+path2+"/register/registerStep2?activeCode="+activeCode;		    
	    //System.out.println(path);
		
		Object[] os = new Object[] { path };
		emailContent = MessageFormat.format(emailContent, os);
		 /*发送邮件*/
//		SendEmail.send("CDESK企业账户激活", email,
//				emailContent);
		MailSendClient.sendMailForPlatoform(email, "CDESK企业账户激活", emailContent);
	}
	
   /*通过激活码获取邮箱*/
	public String queryEmail(String activeCode) throws Exception {
		try {
			RegisterInfoPo po = new RegisterInfoPo();
			po.setActiveCode(activeCode);
			List<RegisterInfoPo> list = registerDao.queryEmail(po, null);
			if(list.size()>0){
				return list.get(0).getEmail();
			}else{
				return "";
			}
		} catch (Exception e) {
        	e.printStackTrace();
        	throw new ServiceException("查询邮箱失败！");
		}
	} 			
	
	   /*通过激活码获取创建时间*/
		public String queryCreateTime(String activeCode) throws Exception {
			try {
				RegisterInfoPo po = new RegisterInfoPo();
				po.setActiveCode(activeCode);
				List<RegisterInfoPo> list = registerDao.queryCreateTime(po, null);
				if(list.size()>0){
					return list.get(0).getCreateTime();
				}else{
					return "";
				}
			} catch (Exception e) {
	        	e.printStackTrace();
	        	throw new ServiceException("查询激活码创建时间失败！");
			}
		} 			
		
		/*添加企业信息后更新注册表中信息(状态,更新时间)*/
		public void updateRegister(RegisterInfoPo registerPo) throws Exception {
			try {
				registerDao.updateRegister(registerPo);
			} catch (Exception e) {
		        e.printStackTrace();
		        throw new ServiceException("更新注册信息失败！");
			}
		}
		
		/*获取同一邮箱的最近一条激活码*/
		public String queryLastCode(String email) throws Exception {
			try {
				RegisterInfoPo po = new RegisterInfoPo();
				po.setEmail(email);
				List<RegisterInfoPo> list = registerDao.queryLastMsg(po, null);
				if(list.size()>0){
					return list.get(0).getActiveCode();
				}else{
					return "";
				}
			} catch (Exception e) {
	        	e.printStackTrace();
	        	throw new ServiceException("获取激活码失败！");
			}
		} 		
}

package com.channelsoft.ems.register.service;

import javax.servlet.http.HttpServletRequest;

import com.channelsoft.ems.register.po.RegisterInfoPo;

public interface IRegisterInfoService {

	public void doRegister(RegisterInfoPo register,HttpServletRequest request) throws Exception;
	
	public String queryEmail(String activeCode) throws Exception;
	
	public String queryCreateTime(String activeCode) throws Exception;
	
	public void updateRegister(RegisterInfoPo registerPo) throws Exception;
	
	public String queryLastCode(String email) throws Exception;
}

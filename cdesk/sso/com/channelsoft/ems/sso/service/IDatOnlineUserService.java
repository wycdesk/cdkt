package com.channelsoft.ems.sso.service;

import javax.servlet.http.HttpServletRequest;

import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.ems.sso.vo.SsoUserVo;

public interface IDatOnlineUserService {

	/**
	 * 1. 删除原有登陆信息
	 * 2. 生成SessionKey
	 * 3. 写入在线用户信息
	 * 4. 返回SessionKey
	 * @param userInfo
	 * @return
	 * @throws ServiceException
	 * @CreateDate: 2013-6-7 下午08:56:44
	 * @author 魏铭
	 */
	public String login(String enterpriseid,SsoUserVo userInfo, String platformId, HttpServletRequest request) throws ServiceException;
	/**
	 * 根据SessionKey获取在线用户
	 * @param sessionKey
	 * @return
	 * @throws ServiceException
	 * @CreateDate: 2013-6-7 下午08:58:09
	 * @author 魏铭
	 */
	public SsoUserVo getUserBySessionKey(String enterpriseid,String sessionKey) throws ServiceException;
	/**
	 * 用户登出
	 * @param sessionKey
	 * @throws ServiceException
	 * @CreateDate: 2013-6-13 上午12:05:01
	 * @author 魏铭
	 */
	public void logout(String enterpriseid,String sessionKey) throws ServiceException;

}

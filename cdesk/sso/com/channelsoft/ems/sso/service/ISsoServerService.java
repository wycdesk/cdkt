package com.channelsoft.ems.sso.service;

import javax.servlet.http.HttpServletRequest;

import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.ems.sso.vo.LoginResultVo;
import com.channelsoft.ems.sso.vo.SsoUserVo;

public interface ISsoServerService {
	/**
	 * 用户登陆，返回各种登陆信息 抛出异常由Controller处理并写日志。
	 * 
	 * @param loginName
	 * @param password
	 * @param platformId
	 * @return
	 * @throws ServiceException
	 * @CreateDate: 2013-6-7 下午07:56:32
	 * @author 魏铭
	 */
	public LoginResultVo login(String enterpriseid, String loginName,
			String password, String platformId, HttpServletRequest request)
			throws ServiceException;

	/**
	 * 切换平台：用户从子平台提交登陆验证请求。
	 * 
	 * @param sessionKey
	 * @param platformId
	 * @return
	 * @throws ServiceException
	 * @CreateDate: 2013-6-11 下午11:03:58
	 * @author 魏铭
	 */
	public LoginResultVo platformSwitch(String enterpriseid, String sessionKey,
			String platformId) throws ServiceException;

	/**
	 * 登出。
	 * 
	 * @param sessionKey
	 * @param platformId
	 * @return
	 * @CreateDate: 2013-6-11 下午11:08:58
	 * @author 魏铭
	 */
	public String logout(String enterpriseid, String sessionKey,
			String platformId) throws ServiceException;

	/**
	 * 子系统请求portal验证sessionkey以及企业id ,并获取用户信息
	 *
	 * @param enterpriseid
	 * 
	 * @param sessionKey
	 * @return
	 * @throws ServiceException
	 * @CreateDate: 2015-4-24 14:20
	 * @author 程立涛
	 */
	public SsoUserVo getLoginUserInfo(String enterpriseid, String sessionKey)
			throws ServiceException;
}

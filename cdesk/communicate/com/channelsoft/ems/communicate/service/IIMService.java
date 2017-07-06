package com.channelsoft.ems.communicate.service;

import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.ems.sso.vo.SsoUserVo;
import com.channelsoft.ems.user.po.DatEntUserPo;

public interface IIMService {

	/**
	 * 根据客户编号判断客户是否存在
	 * 根据webchat判断客户是否存在
	 * 不存在则创建用户，存在返回客户信息
	 * @param user
	 * @param userAccount
	 * @param startTime
	 * @param imSource
	 * @return
	 * @throws ServiceException
	 * @author wangjie
	 * @time 2016年3月15日下午7:03:35
	 */
	public DatEntUserPo queryOrAddUser(SsoUserVo user, String userAccount, String startTime, String imSource)throws ServiceException;
}

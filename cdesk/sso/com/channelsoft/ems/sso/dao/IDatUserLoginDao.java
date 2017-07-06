package com.channelsoft.ems.sso.dao;

import org.springframework.dao.DataAccessException;

import com.channelsoft.ems.sso.po.DatUserLoginPo;

public interface IDatUserLoginDao {
	/**
	 * 添加一条登录日志
	 * @param enterpriseid
	 * @param userLoginPo
	 * @throws DataAccessException
	 * @author zhangtie
	 */
	public void add(String enterpriseid, DatUserLoginPo userLoginPo) throws DataAccessException;
	/**
	 * 用户登出，修改登出时间
	 * @param enterpriseid
	 * @param sessionKey
	 * @throws DataAccessException
	 * @author zhangtie
	 */
	public void logout(String enterpriseid,String sessionKey) throws DataAccessException;

}

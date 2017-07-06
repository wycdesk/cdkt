package com.channelsoft.ems.sso.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.channelsoft.cri.vo.PageInfo;
import com.channelsoft.ems.sso.po.DatOnlineUserPo;
import com.channelsoft.ems.sso.vo.DatOnlineUserVo;

public interface IDatOnlineUserDao {

	/**
	 * 添加在线用户信息
	 * @param onlineUserPo
	 * @throws DataAccessException
	 * @CreateDate: 2013-6-11 下午11:57:50
	 * @author 魏铭
	 * @param enterpriseid 
	 */
	public void add(String enterpriseid, DatOnlineUserPo onlineUserPo) throws DataAccessException;
	/**
	 * 根据登陆名删除在线用户信息
	 * @param loginName
	 * @throws DataAccessException
	 * @CreateDate: 2013-6-11 下午11:57:59
	 * @author 魏铭
	 * @param enterpriseid 
	 */
	public void deleteByLoginName(String enterpriseid, String loginName) throws DataAccessException;
	/**
	 * 查询在线用户
	 * @param po
	 * @return
	 * @throws DataAccessException
	 * @CreateDate: 2013-6-12 下午02:21:46
	 * @author 魏铭
	 */
	public List<DatOnlineUserPo> query(DatOnlineUserPo po, PageInfo page) throws DataAccessException;
	/**
	 * 用户登出，删除数据
	 * @param sessionKey
	 * @throws DataAccessException
	 * @CreateDate: 2013-6-13 上午12:06:20
	 * @author 魏铭
	 */
	public void logout(String enterpriseid,String sessionKey) throws DataAccessException;
	/**
	 * 根据SessionKey查询在线用户及其用户信息
	 * @param sessionKey
	 * @return
	 * @throws DataAccessException
	 * @author 张铁
	 * @date 2013-7-13
	 */
	public List<DatOnlineUserVo> queryUser(String enterpriseid,String sessionKey) throws DataAccessException;
	/**
	 * 根据SessionKey查询在线用户及其用户信息
	 * @param enterpriseid
	 * @param sessionKey
	 * @return
	 * @throws DataAccessException
	 */
	public List<DatOnlineUserVo> queryUserForMongo(String enterpriseid,String sessionKey) throws DataAccessException;

}

package com.channelsoft.ems.ent.service;


import java.util.List;

import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.ems.ent.po.DatEntInfoPo;
import com.channelsoft.ems.sso.vo.SsoEntInfoVo;
import com.channelsoft.ems.user.po.DatEntUserPo;

public interface IDatEntService {

	/**
	 * 添加企业
	 * @param po
	 * @return
	 * @throws DataAccessException
	 */
	public int addEntInfo(DatEntInfoPo po) throws ServiceException;
	/**
	 * 查询该邮箱是否已经注册
	 * @param email
	 * @return
	 * @throws DataAccessException
	 */
	public boolean existThisMail(String email) throws ServiceException;
	/**
	 * 查询该企业编号是否已存在
	 * @param entId
	 * @return
	 * @throws DataAccessException
	 */
	public boolean existThisEntId(String entId) throws ServiceException;
	/*
	 * 查询是否已存在对应的ccod企业ID
	 */
	public boolean existThisCcodEntId(String ccodEntId) throws ServiceException;
	/**
	 * 初始化企业数据库
	 * 1:创建库
	 * 2：创建表
	 * 3：初始化数据
	 * @param entId
	 * @return
	 * @throws ServiceException
	 */
	public int initEntDatabase(DatEntInfoPo po) throws ServiceException;
	/**
	 * 查询企业信息
	 * @param po
	 * @return
	 * @throws ServiceException
	 * @author zhangtie
	 */
	public List<DatEntInfoPo> query(DatEntInfoPo po) throws ServiceException;
	/**
	 * 查询企业信息
	 * @param enterpriseid
	 * @return
	 * @throws ServiceException
	 * @author zhangtie
	 */
	public SsoEntInfoVo query(String entId) throws ServiceException;
	/**
	 * 查询所有企业
	 * @return
	 * @throws ServiceException
	 * @author zhangtie
	 */
	public List<DatEntInfoPo> queryAll() throws ServiceException;
	/**
	 * 更新公司信息
	 * @param po
	 * @return
	 * @throws ServiceException
	 * @author zhangtie
	 */
	public int update(DatEntInfoPo po) throws ServiceException;
	/**
	 * 为用户中心提供的添加企业信息
	 * 生成企业信息和企业库以及客服组和管理员信息
	 * @param po
	 * @param user
	 * @return
	 * @throws ServiceException
	 */
	public int addEntForUserCenter(DatEntInfoPo po,DatEntUserPo user) throws ServiceException;
}

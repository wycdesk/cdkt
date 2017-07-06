package com.channelsoft.ems.ent.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.channelsoft.ems.ent.po.DatEntInfoPo;

public interface IDatEntDao {

	/**
	 * 添加企业
	 * @param po
	 * @return
	 * @throws DataAccessException
	 */
	public int addEntInfo(DatEntInfoPo po) throws DataAccessException;
	/**
	 * 查询该邮箱是否已经注册
	 * @param email
	 * @return
	 * @throws DataAccessException
	 */
	public boolean existThisMail(String email) throws DataAccessException;
	/**
	 * 查询该企业编号(或域名)是否已存在
	 * @param entId
	 * @return
	 * @throws DataAccessException
	 */
	public boolean existThisEntId(String entId) throws DataAccessException;
	/**
	 * 创建企业数据库
	 * @param 数据名为：ent_企业编号
	 * @return
	 * @throws DataAccessException
	 */
	public int createDataBase(String entId) throws DataAccessException;
	/**
	 * 创建企业数据库中表
	 * @param 数据名为：ent_企业编号
	 * @return
	 * @throws DataAccessException
	 */
	public int createTableName(String entId) throws DataAccessException;
	/**
	 * 查询企业信息
	 * @param po
	 * @return
	 * @throws DataAccessException
	 * @author zhangtie
	 */
	public List<DatEntInfoPo> query(DatEntInfoPo po) throws DataAccessException;
	
	/**
	 * 系统设置-品牌设置-基本设置
	 * @param po
	 * @return
	 * @throws DataAccessException
	 * @author wangjie
	 * @time 2015年11月26日下午5:20:22
	 */
	public int brandSetting(DatEntInfoPo po) throws DataAccessException;
	
	/**
	 * 系统设置-品牌设置-基本设置
	 * 修改图片
	 * @param po
	 * @return
	 * @throws DataAccessException
	 * @author wangjie
	 * @time 2015年11月27日下午3:30:32
	 */
	public int changeimage(DatEntInfoPo po,String type) throws DataAccessException;
	/**
	 * 系统设置-品牌设置-基本设置
	 * 删除图片
	 * @param po
	 * @param type
	 * @return
	 * @throws DataAccessException
	 * @author wangjie
	 * @time 2015年11月30日上午9:47:34
	 */
	public int delimage(DatEntInfoPo po,String type) throws DataAccessException;
	/**
	 * 企业销户，删除企业信息
	 * @param entId
	 * @return
	 */
	public int deleteEntInfo(String entId) throws DataAccessException;
	/**
	 * 企业销户，删除对应企业的数据库
	 * @param entId
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteDB(String entId) throws DataAccessException;
	/**
	 * 修改公司信息
	 * @param po
	 * @return
	 * @throws DataAccessException
	 * @author zhangtie
	 */
	public int update(DatEntInfoPo po) throws DataAccessException;
}

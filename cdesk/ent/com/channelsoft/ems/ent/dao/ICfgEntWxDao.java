package com.channelsoft.ems.ent.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.channelsoft.cri.vo.PageInfo;
import com.channelsoft.ems.ent.po.CfgEntWxPo;

public interface ICfgEntWxDao {

	/**
	 * 查询企业微信号信息
	 * @param po
	 * @return
	 * @throws DataAccessException
	 */
	public List<CfgEntWxPo> query(String entId, CfgEntWxPo po, PageInfo page) throws DataAccessException;
	/**
	 * 添加企业微信号信息
	 * @param entId
	 * @param po
	 * @return
	 * @throws DataAccessException
	 * @author zhangtie
	 */
	public int add(String entId, CfgEntWxPo po) throws DataAccessException;
	/**
	 * 更新企业微信号信息
	 * @param entId
	 * @param po
	 * @return
	 * @throws DataAccessException
	 * @author zhangtie
	 */
	public int update(String entId, CfgEntWxPo po) throws DataAccessException;
	/**
	 * 删除企业微信号信息
	 * @param entId
	 * @param originalId
	 * @throws DataAccessException
	 * @author zhangtie
	 */
	public int delete(String entId, String originalId) throws DataAccessException;
}

package com.channelsoft.ems.ent.service;


import java.util.List;

import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.vo.PageInfo;
import com.channelsoft.ems.ent.po.CfgEntWxPo;
import com.channelsoft.ems.ent.po.DatEntInfoPo;
import com.channelsoft.ems.sso.vo.SsoEntInfoVo;

public interface ICfgEntWxService {

	/**
	 * 查询企业的所有微信号
	 * @param entId
	 * @return
	 * @throws ServiceException
	 * @author zhangtie
	 */
	public List<CfgEntWxPo> queryByEntId(String entId) throws ServiceException;
	/**
	 * 查询企业微信号
	 * @param po
	 * @return
	 * @throws ServiceException
	 * @author zhangtie
	 */
	public List<CfgEntWxPo> query(String entId, CfgEntWxPo po, PageInfo page) throws ServiceException;
	/**
	 * 添加企业微信号信息
	 * @param entId
	 * @param po
	 * @return
	 * @throws ServiceException
	 * @author zhangtie
	 */
	public int add(String entId, CfgEntWxPo po) throws ServiceException;
	/**
	 * 更新企业微信号信息
	 * @param entId
	 * @param po
	 * @return
	 * @throws ServiceException
	 * @author zhangtie
	 */
	public int update(String entId, CfgEntWxPo po) throws ServiceException;
	/**
	 * 删除企业微信号信息
	 * @param entId
	 * @param originalId
	 * @return
	 * @throws ServiceException
	 * @author zhangtie
	 */
	public int delete(String entId, String originalId) throws ServiceException;
}

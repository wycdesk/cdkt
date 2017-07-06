package com.channelsoft.ems.ent.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.vo.PageInfo;
import com.channelsoft.ems.ent.dao.ICfgEntWxDao;
import com.channelsoft.ems.ent.po.CfgEntWxPo;
import com.channelsoft.ems.ent.service.ICfgEntWxService;

public class CfgEntWxServiceImpl implements ICfgEntWxService {

	@Autowired
	ICfgEntWxDao entWxDao;
	
	
	@Override
	public List<CfgEntWxPo> query(String entId, CfgEntWxPo po, PageInfo page) throws ServiceException {
		try{
			return entWxDao.query(entId, po, page);
		} catch(DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException("企业微信号信息查询失败");
		}
	}

	@Override
	public List<CfgEntWxPo> queryByEntId(String entId) throws ServiceException {
		return this.query(entId, new CfgEntWxPo(), null);
	}

	@Override
	public int add(String entId, CfgEntWxPo po) throws ServiceException {
		try{
			return entWxDao.add(entId, po);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException("企业微信号信息添加失败");
		}
	}

	@Override
	public int update(String entId, CfgEntWxPo po) throws ServiceException {
		try{
			return entWxDao.update(entId, po);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException("企业微信号信息更新失败");
		}
	}

	@Override
	public int delete(String entId, String originalId) throws ServiceException {
		try{
			return entWxDao.delete(entId, originalId);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException("企业微信号信息删除失败");
		}
	}

}

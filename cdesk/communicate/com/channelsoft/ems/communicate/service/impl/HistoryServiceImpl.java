package com.channelsoft.ems.communicate.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.vo.PageInfo;
import com.channelsoft.ems.communicate.dao.IHistoryDao;
import com.channelsoft.ems.communicate.service.IHistoryService;
import com.mongodb.DBObject;

public class HistoryServiceImpl implements IHistoryService {

	@Autowired
	IHistoryDao historyDao;
	
	@Override
	public int add(DBObject dbo, String entId) throws ServiceException {
		try{
			return historyDao.add(dbo, entId);
		}catch(DataAccessException e){
			e.printStackTrace();
			throw new ServiceException("添加联络历史出错");
		}
	}

	@Override
	public List<DBObject> queryCommHistory(DBObject queryObject,
			PageInfo pageInfo)throws ServiceException {
		try{
			return historyDao.queryCommHistory(queryObject, pageInfo);
		}
		catch(DataAccessException e){
			e.printStackTrace();
			throw new ServiceException("查询联络历史出错");
		}
	}

}

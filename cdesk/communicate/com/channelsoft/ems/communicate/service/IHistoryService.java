package com.channelsoft.ems.communicate.service;

import java.util.List;

import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.vo.PageInfo;
import com.mongodb.DBObject;

public interface IHistoryService {

	public int add(DBObject dbo, String entId) throws ServiceException;

	/**
	 * 
	 * 
	 * @Description: 查询联络历史
	 * @author chenglitao
	 * @param    
	 * @date 2016年5月25日 下午5:14:53   
	 *
	 */
	public List<DBObject> queryCommHistory(DBObject queryObject,
			PageInfo pageInfo) throws ServiceException;
}

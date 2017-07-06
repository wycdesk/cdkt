package com.channelsoft.ems.androidApi.service;

import java.util.List;

import com.channelsoft.cri.exception.ServiceException;
import com.mongodb.DBObject;

public interface IAndroidUserApiService {

	/**
	 * Android---根据 ccodId 和账号，渠道查询客户资料
	 * @param entId
	 * @param account
	 * @param source
	 * @return
	 * @throws ServiceException
	 * @author dulei
	 * @time 2016年3月23日 下午8:03:31
	 */
	public List<DBObject> queryUser(String entId,String account, String source) throws ServiceException;
	
	public DBObject queryUserOrAdd(String entId,String account, String source) throws ServiceException;
}

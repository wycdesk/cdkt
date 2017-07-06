package com.channelsoft.ems.search.service;

import java.util.List;

import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.vo.PageInfo;
import com.mongodb.DBObject;

public interface ISearchMongoService {

	/*用户模糊搜索*/
	public List<DBObject> queryUserList(String keyword, String entId, String startTime, String endTime, String searchType, PageInfo pageInfo) throws  ServiceException;

	/*工单模糊搜索*/
	public List<DBObject> queryOrderList(String keyword, String entId, String startTime, String endTime, String searchType, PageInfo pageInfo) throws  ServiceException;
	
}

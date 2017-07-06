package com.channelsoft.ems.search.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.channelsoft.cri.vo.PageInfo;
import com.mongodb.DBObject;

public interface ISearchMongoDao {

	/*用户模糊搜索*/
	public List<DBObject> queryUserList(String keyword, String entId, String startTime, String endTime, String searchType, PageInfo pageInfo) throws DataAccessException;

	/*工单模糊搜索*/
	public List<DBObject> queryOrderList(String keyword, String entId, String startTime, String endTime, String searchType, PageInfo pageInfo) throws DataAccessException;
	
	/*获取用户表*/
	public String getUserTable(String entId)throws DataAccessException;
	
	/*获取工单表*/
	public String getOrderTable(String entId)throws DataAccessException;
	
	/*校验是否为数字字符串*/
	public boolean isNumber(String number)throws DataAccessException;  
}

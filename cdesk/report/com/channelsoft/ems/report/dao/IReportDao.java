package com.channelsoft.ems.report.dao;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.dao.DataAccessException;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public interface IReportDao {

	/**
	 * 查询用户列表，用于统计服务客户数量
	 * @param dbo
	 * @return
	 * @throws DataAccessException
	 * @author wangjie
	 * @time 2016年3月8日下午1:28:15
	 */
	public List<DBObject> queryUser(DBObject queryObject) throws DataAccessException;
	
	
	/**
	 * 查询工单表中的用户ID
	 * @param entId
	 * @return
	 * @throws DataAccessException
	 * @author wangjie
	 * @time 2016年3月8日下午2:59:24
	 */
	public List<DBObject> queryWorkOrder(String entId,DBObject queryObject) throws DataAccessException;
	
	/**
	 * 查询联络历史中的用户ID
	 * @param entId
	 * @return
	 * @throws DataAccessException
	 * @author wangjie
	 * @time 2016年3月8日下午3:00:35
	 */
	public List<DBObject> queryCommunicate(String entId,DBObject queryObject) throws DataAccessException;


	public List<DBObject> queryAllWork(String entId, BasicDBObject workObject) throws DataAccessException;


	public Iterator<DBObject> getBaseSortList(DBObject queryDB, String type) throws DataAccessException;


	public List<DBObject> getServiceByTime(Date beginTime, Date endTime, String entId, int type) throws DataAccessException;
	
}

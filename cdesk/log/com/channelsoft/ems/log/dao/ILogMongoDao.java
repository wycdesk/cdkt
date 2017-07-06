package com.channelsoft.ems.log.dao;

import java.util.List;

import com.channelsoft.cri.vo.PageInfo;
import com.channelsoft.ems.log.po.CfgUserOperateLogPo;
import com.mongodb.DBObject;
import com.mongodb.MongoException;

public interface ILogMongoDao {

	/**
	 * 添加日志
	 * @param dbo
	 * @param entId
	 * @return
	 * @throws MongoException
	 */
	public int add(DBObject dbo,String entId) throws MongoException;
	
	public List<CfgUserOperateLogPo> query(CfgUserOperateLogPo po,PageInfo pageInfo,String entId) throws MongoException;
}

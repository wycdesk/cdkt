package com.channelsoft.ems.report.dao.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.dao.DataAccessException;

import com.channelsoft.cri.logger.SystemLogUtils;
import com.channelsoft.cri.mongo.BaseMongoTemplate;
import com.channelsoft.cri.util.ColletionName;
import com.channelsoft.cri.util.GetCollectionFromEntInfo;
import com.channelsoft.ems.report.dao.IReportDao;
import com.channelsoft.ems.report.util.GetKeys;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class ReportDaoImpl extends BaseMongoTemplate  implements IReportDao {

	@Override
	public List<DBObject> queryUser(DBObject queryObject) throws DataAccessException {
		// TODO Auto-generated method stub
		// 要查出来的字段
		BasicDBObject queryField = new BasicDBObject();
		queryField.put("userId", 1);
		
		return this.findToDbObject(queryObject, queryField, getUserTable(queryObject.get("entId").toString()));
	}

	/**
	 * 获取用户表
	 * @param entId
	 * @return
	 * @author wangjie
	 * @time 2016年3月8日下午3:11:29
	 */
	private String getUserTable(String entId){
		// TODO Auto-generated method stub
		return "entId_"+entId+"_user";
	}
	
	/**
	 * 获取联络历史数据库表
	 * @param entId
	 * @return
	 * @author wangjie
	 * @time 2016年3月8日下午3:10:32
	 */
	private String getCommunicateTable(String entId) {
		return "entId_"+entId+"_communicate_history";
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DBObject> queryWorkOrder(String entId,DBObject queryObject) throws DataAccessException {
		// TODO Auto-generated method stub
		String collectionName = GetCollectionFromEntInfo.getColletionName(entId, ColletionName.WORKORDER.key);
		return this.getMongoTemplate().getCollection(collectionName).distinct("customId",queryObject);
		
		/*// 查询条件
		BasicDBObject queryObject = new BasicDBObject();
		
		// 要查出来的字段
		BasicDBObject queryField = new BasicDBObject();
		queryField.put("customId", 1);
		
		return this.findToDbObject(queryObject, queryField, collectionName);*/
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DBObject> queryCommunicate(String entId,DBObject queryObject) throws DataAccessException {
		// TODO Auto-generated method stub
		String collectionName = this.getCommunicateTable(entId);
		return this.getMongoTemplate().getCollection(collectionName).distinct("userId", queryObject);
		/*// 查询条件
		BasicDBObject queryObject = new BasicDBObject();
		// 要查出来的字段
		BasicDBObject queryField = new BasicDBObject();
		queryField.put("userId", 1);
		
		return this.findToDbObject(queryObject, queryField, collectionName);*/
	}

	@Override
	public List<DBObject> queryAllWork(String entId, BasicDBObject workObject) throws DataAccessException {
		String collectionName = GetCollectionFromEntInfo.getColletionName(entId, ColletionName.WORKORDER.key);
		
		return this.findToDbObject(workObject, collectionName);
	}

	@Override
	public Iterator<DBObject> getBaseSortList(DBObject queryDB, String type) throws DataAccessException {
		String collectionName = GetCollectionFromEntInfo.getColletionName((String)queryDB.get("entId"), ColletionName.WORKORDER.key);
		DBObject[] timeArr=(DBObject[])queryDB.get("$and");
		DBObject[] dbArr={new BasicDBObject(type,new BasicDBObject("$ne","")),new BasicDBObject(type,new BasicDBObject("$ne",null)),
				timeArr[0],timeArr[1]};
		queryDB.put("$and",dbArr);
		DBObject fields=new BasicDBObject(type,1);
		fields.put("status", 1);
		fields.put(type.replace("Id", "Name"), 1);
		//fields.put("sum", 1);
		DBObject groupFields=new BasicDBObject();
		DBObject arry=new BasicDBObject(type,"$"+type);
		arry.put("status", "$status");
		arry.put(type.replace("Id", "Name"),"$"+type.replace("Id", "Name"));
		
		groupFields.put("_id",arry);
		
		groupFields.put("sum", new BasicDBObject("$sum",1));
		//groupFields.put(type.replace("Id", "Name"),"$"+type.replace("Id", "Name"));
		//groupFields.put("sumN", new BasicDBObject("$sum","$status"));
		BasicDBObject match = new BasicDBObject("$match", queryDB);
		BasicDBObject project = new BasicDBObject("$project", fields);
		DBObject group = new BasicDBObject("$group", groupFields);
		BasicDBObject sort = new BasicDBObject("$sort",  new BasicDBObject("sum",-1));
		AggregationOutput ag=this.findByGroup(match, project, group, sort, collectionName);
		
		return ag.results().iterator();
	}

	@Override
	public List<DBObject> getServiceByTime(Date beginTime, Date endTime, String entId,int type) throws DataAccessException {
		String collection="";
		BasicDBObject basicDBObject = new BasicDBObject();
		if(type==0){
			collection = GetCollectionFromEntInfo.getColletionName(entId, ColletionName.WORKORDER.key);
			BasicDBObject[] array = { new BasicDBObject("createDate", new BasicDBObject("$gte",beginTime) ),
			        new BasicDBObject("createDate", new BasicDBObject("$lte",endTime) ) };
			basicDBObject.put("$and", array);
			
		}else{
			collection = GetCollectionFromEntInfo.getColletionName(entId, ColletionName.COMMUNICATE.key);
			BasicDBObject[] array = { new BasicDBObject("createTime", new BasicDBObject("$gte", beginTime)),
			        new BasicDBObject("createTime", new BasicDBObject("$lte", endTime)) };
			basicDBObject.put("$and", array);
		}
		return this.findToDbObject(basicDBObject, collection);
	}

}

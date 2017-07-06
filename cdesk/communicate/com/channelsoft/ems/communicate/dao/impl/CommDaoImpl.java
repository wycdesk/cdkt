package com.channelsoft.ems.communicate.dao.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.channelsoft.cri.mongo.BaseMongoTemplate;
import com.channelsoft.cri.util.ColletionName;
import com.channelsoft.cri.util.DBObjectUtils;
import com.channelsoft.cri.util.GetCollectionFromEntInfo;
import com.channelsoft.cri.vo.PageInfo;
import com.channelsoft.ems.communicate.constant.WorkSource;
import com.channelsoft.ems.communicate.dao.ICommDao;
import com.channelsoft.ems.communicate.po.CommPo;
import com.channelsoft.ems.communicate.utils.QueryUtils;
import com.channelsoft.ems.user.po.DatEntUserPo;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class CommDaoImpl extends BaseMongoTemplate implements ICommDao {

	@Override
	public int saveComm(String entId, DBObject commObj) throws DataAccessException {
		if(commObj.containsField("endTime")){
			commObj.put("createTime",new Date());
		}else{
			commObj.put("createTime",(Date)commObj.get("startTime"));
		}
		String collection=getCommCollection(entId);
		return this.intsertFromDbObject(commObj, collection);
	}

	@Override
	public List<DBObject> getComms(String entId, CommPo commPo, PageInfo pageInfo) throws DataAccessException {
		Query query=QueryUtils.getQuery(commPo);
		String collection=getCommCollection(entId);
		query.with(new Sort(new Order(Direction.DESC,"createTime")));
		return this.findByPage(query, collection, DBObject.class, pageInfo);
	 
	}

	private String getCommCollection(String entId) {
		return "entId_"+entId+"_communicate_history";
	}

	@Override
	public List<DBObject> queryTodayWorkOrderDetailNumber(String todayDate,
			String entId) {
		String collectioName = this.getCommCollection(entId);
		Iterator<DBObject> ito = null;
		
		// 查询条件
		BasicDBObject cond = new BasicDBObject();
		// 查询出来的字段
		BasicDBObject fields = new BasicDBObject("_id", 1);
		// 用来分组的字段
		BasicDBObject groupFields = new BasicDBObject();
		// 用来排序
		BasicDBObject sortBy = new BasicDBObject();
		
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date beginTime = sdf.parse(todayDate+" 00:00:00");
			
			Date endTime = sdf.parse(todayDate+" 23:59:59");
			
			BasicDBObject[] array = { new BasicDBObject("createTime", new BasicDBObject("$gte", beginTime)),
			        new BasicDBObject("createTime", new BasicDBObject("$lte", endTime)) };
			cond.put("$and", array);
			
			fields.put("_id", 0); 
			fields.put("source", 1);
			//fields.put("sum", 1);

			groupFields.put("_id", "$source");
			groupFields.put("sum", new BasicDBObject("$sum", 1));
			
			sortBy.put("sum", -1);
			
			BasicDBObject match = new BasicDBObject("$match", cond);
			BasicDBObject project = new BasicDBObject("$project", fields);
			DBObject group = new BasicDBObject("$group", groupFields);
			BasicDBObject sort = new BasicDBObject("$sort", sortBy);
			 
			// 处理查询到的结果
			AggregationOutput ag = this.findByGroup(match, project, group, sort, collectioName);
			//System.out.println(ag.getCommandResult());
			ito = ag.results().iterator();
		} catch (ParseException e) {
			
			e.printStackTrace();
		}
		List<DBObject> list=new ArrayList<DBObject>();
		while (ito.hasNext())
		{ 
			DBObject db = ito.next();
			DBObject dbo = new BasicDBObject();
			dbo.put("source", db.get("_id")+"");
			dbo.put("num", db.get("sum")+"");
			list.add(dbo);
		}
		
		return list;
	}

	@Override
	public int endComm(String entId, String sessionId, DBObject commObj) throws DataAccessException {
		DBObject query=new BasicDBObject("sessionId",sessionId);
		DBObject update=new BasicDBObject("$set",commObj);
		String collection=this.getCommCollection(entId);
		return this.updateFromDbObject(query, update, collection);
	}

	@Override
	public int saveContent(String entId, String content,String sessionId, String commId) throws DataAccessException {
		String collection=this.getCommCollection(entId);
		DBObject query=null;
		if(StringUtils.isNotBlank(sessionId)){
			query=new BasicDBObject("sessionId",sessionId);
		}else{
			query=new BasicDBObject("commId",commId);
		}
		DBObject po=new BasicDBObject("content",content);
		DBObject update=new BasicDBObject("$set",po);
		return this.updateFromDbObject(query, update, collection);
	}

	@Override
	public List<DBObject> querySource(String entId) {
		String collectioName = this.getCommCollection(entId);
		Iterator<DBObject> ito = null;
		
		// 查询条件
		BasicDBObject cond = new BasicDBObject();
		// 查询出来的字段
		BasicDBObject fields = new BasicDBObject("_id", 1);
		// 用来分组的字段
		BasicDBObject groupFields = new BasicDBObject();
		// 用来排序
		BasicDBObject sortBy = new BasicDBObject();
		
			
		fields.put("_id", 0); 
		fields.put("source", 1);
		//fields.put("sum", 1);

		groupFields.put("_id", "$source");
		groupFields.put("sum", new BasicDBObject("$sum", 1));
		
		sortBy.put("sum", -1);
		BasicDBObject match = new BasicDBObject("$match", cond);
		BasicDBObject project = new BasicDBObject("$project", fields);
		DBObject group = new BasicDBObject("$group", groupFields);
		BasicDBObject sort = new BasicDBObject("$sort", sortBy);
		 
		// 处理查询到的结果
		AggregationOutput ag = this.findByGroup(match, project, group, sort, collectioName);
		//System.out.println(ag.getCommandResult());
		ito = ag.results().iterator();
		List<DBObject> list=new ArrayList<DBObject>();
		while (ito.hasNext())
		{
			DBObject db = ito.next();
			DBObject dbo = new BasicDBObject();
			dbo.put("source", db.get("_id")+"");
			dbo.put("num", db.get("sum"));
			list.add(dbo);
		}
		
		return list;
	}

	@Override
	public long countSession(String entId,String sessionId) throws DataAccessException {
		Query query = new Query();
		String collection=this.getCommCollection(entId);
		query.addCriteria(Criteria.where("sessionId").is(sessionId));
		return this.count(query, collection);
	}

	@Override
	public DBObject getCommBySession(String entId, String sessionId) throws DataAccessException {
		Query query = new Query();
		String collection=this.getCommCollection(entId);
		query.addCriteria(Criteria.where("sessionId").is(sessionId));
		return this.findOne(query,collection,DBObject.class);
	}

	@Override
	public List<DBObject> getRecentServiceNum(String entId, String beginTime, String endTime)
			throws DataAccessException {
		String collectioName = this.getCommCollection(entId);
		Iterator<DBObject> ito = null;
		
		// 查询条件
		BasicDBObject cond = new BasicDBObject();
		// 查询出来的字段
		BasicDBObject fields = new BasicDBObject("_id", 1);
		// 用来分组的字段
		BasicDBObject groupFields = new BasicDBObject();
		// 用来排序
		BasicDBObject sortBy = new BasicDBObject();
		
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date beginTimeD = sdf.parse(beginTime+" 00:00:00");
			
			Date endTimeD = sdf.parse(endTime+" 23:59:59");
			
			BasicDBObject[] array = { new BasicDBObject("createTime", new BasicDBObject("$gte", beginTimeD)),
			        new BasicDBObject("createTime", new BasicDBObject("$lte", endTimeD)) };
			cond.put("$and", array);
			
			fields.put("_id", 0); 
			fields.put("source", 1);
			//fields.put("sum", 1);

			groupFields.put("_id", "$source");
			groupFields.put("sum", new BasicDBObject("$sum", 1));
			
			sortBy.put("sum", -1);
			
			BasicDBObject match = new BasicDBObject("$match", cond);
			BasicDBObject project = new BasicDBObject("$project", fields);
			DBObject group = new BasicDBObject("$group", groupFields);
			BasicDBObject sort = new BasicDBObject("$sort", sortBy);
			 
			// 处理查询到的结果
			AggregationOutput ag = this.findByGroup(match, project, group, sort, collectioName);
			//System.out.println(ag.getCommandResult());
			ito = ag.results().iterator();
		} catch (ParseException e) {
			
			e.printStackTrace();
		}
		List<DBObject> list=new ArrayList<DBObject>();
		boolean has5=false;
		int ii=0;
		int i5=-1,i6=-1;
		while (ito.hasNext())
		{ 
			DBObject db = ito.next();
			DBObject dbo = new BasicDBObject();
			String s=db.get("_id")+"";
			if(s.equals("5")){
				i5=ii;
			}
			if(s.equals("6")){
				i6=ii;
			}
			dbo.put("source",s);
			dbo.put("num", db.get("sum")+"");
			list.add(dbo);
			ii++;
		}
		if(i6>-1){
			if(i5>-1){
				list.get(i5).put("num", (Integer.valueOf((String)(list.get(i5).get("num"))).intValue()
						+Integer.valueOf((list.get(i6).get("num")+"")).intValue())+"");
				list.remove(i6);
			}else{
				DBObject dbo = new BasicDBObject("source","5");
				dbo.put("num",list.get(i6).get("num")+"");
				list.remove(i6);
				list.add(dbo);
			}
		}
		return list;
	}

	@Override
	public Iterator<DBObject> getBaseSortList(DBObject queryDB, String type) throws DataAccessException {
		String collectionName = this.getCommCollection((String)queryDB.get("entId"));
		String tp=type.indexOf("custom")>=0?"userId":"groupId";
		queryDB.put(tp, new BasicDBObject("$ne",""));
		queryDB.removeField("entId");
		DBObject fields=new BasicDBObject(tp,1);
		fields.put("source", 1);
		fields.put("sum", 1);
		
		DBObject groupFields=new BasicDBObject();
		DBObject arry=new BasicDBObject(tp,"$"+tp);
		arry.put("source", "$source");
		groupFields.put("_id",arry);
		groupFields.put("sum", new BasicDBObject("$sum",1));
		
		BasicDBObject match = new BasicDBObject("$match", queryDB);
		BasicDBObject project = new BasicDBObject("$project", fields);
		DBObject group = new BasicDBObject("$group", groupFields);
		BasicDBObject sort = new BasicDBObject("$sort",  new BasicDBObject("sum",-1));
		AggregationOutput ag=this.findByGroup(match, project, group, sort, collectionName);
		
		return ag.results().iterator();
	}

	
	
	/*给移动端提供的联络历史查询方法*/
	@Override
	public List<DBObject> getCommsForApi(String entId, CommPo commPo, PageInfo pageInfo) throws DataAccessException {
		
		String collection=getCommCollection(entId);
		
	    DBObject queryObject = new BasicDBObject();
		queryObject.put("commId", commPo.getCommId());
		
		// 要查出来的字段
		DBObject queryField = new BasicDBObject();
		
		queryField.put("_id", 1);
		queryField.put("userId", 1);
		queryField.put("loginName", 1);
		queryField.put("userName", 1);
		queryField.put("source", 1);
		queryField.put("content", 1);
		queryField.put("opId", 1);
		queryField.put("opName", 1);
		queryField.put("startTime", 1);
		queryField.put("endTime", 1);
 
		queryField.put("commId", 1);
		queryField.put("createTime", 1);
		
		queryField.put("ccodEntId", 1);
		queryField.put("ccodAgentId", 1);
		queryField.put("ccodAgentName", 1);
		queryField.put("commTime", 1);
		queryField.put("recordId", 1);
		queryField.put("strDnis", 1);
		queryField.put("sessionId", 1);
		queryField.put("isConnected", 1);
		queryField.put("serviceData", 1);
		queryField.put("remoteUrl", 1);
		queryField.put("strAni", 1);
		queryField.put("strOrigDnis", 1);
		queryField.put("requestType", 1);
		queryField.put("strOrigAni", 1);
		queryField.put("callType", 1);
		queryField.put("accountId", 1);
		queryField.put("accountType", 1);
		
		
		// 排序
		DBObject sort = new BasicDBObject();
		sort.put("createTime", -1);
		
		return this.findToDbObjectByPage(queryObject, queryField, collection, pageInfo, sort);

		
	}

	@Override
	public int updateComm(String entId, String commId, DBObject update) throws DataAccessException {
		String collection = GetCollectionFromEntInfo.getColletionName(entId,ColletionName.COMMUNICATE.key);
		DBObject set=new BasicDBObject("$set",update);
		DBObject query=new BasicDBObject("commId",commId);
		return this.updateFromDbObject(query, set, collection);
	}

	@Override
	public int mergeComm(String entId, String userMergeId, DatEntUserPo user) throws DataAccessException {
		String collection = GetCollectionFromEntInfo.getColletionName(entId,ColletionName.COMMUNICATE.key);
		DBObject update=new BasicDBObject("userId",user.getUserId());
		update.put("loginName", user.getLoginName());
		update.put("userName", user.getUserName());
		DBObject set=new BasicDBObject("$set",update);
		DBObject query=new BasicDBObject("userId",userMergeId);
		return getMongoTemplate().getCollection(collection).update(query, set, false, true).getN();
	}
}

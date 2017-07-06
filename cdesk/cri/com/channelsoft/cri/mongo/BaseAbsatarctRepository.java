package com.channelsoft.cri.mongo;

import java.util.ArrayList;
import java.util.List;



import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.channelsoft.cri.vo.PageInfo;
import com.mongodb.AggregationOutput;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;

public abstract class BaseAbsatarctRepository {


	public abstract MongoTemplate getMongoTemplate();
	
	public  void save(Object entity) {
		getMongoTemplate().save(entity);
	}
	
	public  void save(Object entity, String collectionName) {
		getMongoTemplate().save(entity, collectionName);
	}

	public void save(Iterable<?> entities, String collectionName) {
		getMongoTemplate().insert(entities, collectionName);
	}

	public <T> T findOne(Query query, Class<T> poClass) {
		return getMongoTemplate().findOne(query, poClass);
	}
	
	public <T> T findOne(Query query, String collectionName, Class<T> poClass) {
		return getMongoTemplate().findOne(query, poClass, collectionName);
	}
	
	public <T> List<T> findList(Query query, Class<T> poClass) {
		return getMongoTemplate().find(query, poClass);
	}
	
	public <T> List<T> findList(Query query, String collectionName, Class<T> poClass) {
	 List<T> list=null;
	try {
	list=	getMongoTemplate().find(query, poClass, collectionName);
	} catch (Exception e) {
		e.printStackTrace();
	}
		
		return list;
		
	}
	
	public <T> List<T> findByPage(Query query, String collectionName, Class<T> poClass, PageInfo pageInfo) {
		List<T> resultList = null;
		if (pageInfo == null) {
			resultList = findList(query, collectionName, poClass);
		} else {
			pageInfo.setTotalRecords(count(query, collectionName));
			resultList = getMongoTemplate().find(query.skip(pageInfo.getStartIndex()).limit(pageInfo.getResults()), poClass, collectionName);

		}
		return resultList;
	}
	
	public <T> List<T> findByPage(Query query, Class<T> poClass, PageInfo pageInfo) {
		List<T> resultList = null;
		if (pageInfo == null) {
			resultList = findList(query, poClass);
		} else {
			pageInfo.setTotalRecords(count(query, poClass));
			resultList = getMongoTemplate().find(query.skip(pageInfo.getStartIndex()).limit(pageInfo.getResults()), poClass);

		}
		return resultList;
	}
	
	public <T> long count(Query query, Class<T> poClass) {
		return getMongoTemplate().count(query, poClass);
	}
	
	public long count(Query query, String collectionName) {
		return getMongoTemplate().count(query, collectionName);
	}
	
	public <T> int delete(Query query, Class<T> poClass) {
		WriteResult result = getMongoTemplate().remove(query, poClass);
		return result.getN();
	}
	
	public <T> int delete(Query query, String collections) {
		WriteResult result = getMongoTemplate().remove(query, collections);
		return result.getN();
	}
	public <T> void drop(String collectionName) {
		getMongoTemplate().dropCollection(collectionName);
	}
	public <T> int update(Query query, Update update, Class<T> poClass) {
		return getMongoTemplate().updateMulti(query, update, poClass).getN();
	}
	
	public <T> int update(Query query, Update update, String collections) {
		return getMongoTemplate().updateMulti(query, update, collections).getN();
	}
	
	public List<DBObject> findToDbObject(DBObject queryObject, String collections) {
		DBCursor cursor = getMongoTemplate().getCollection(collections).find(queryObject);
		List<DBObject> DBObjectList = new ArrayList<DBObject>();
		while(cursor.hasNext()) {
			DBObject object = cursor.next();
			object.put("_id", object.get("_id").toString());
			DBObjectList.add(object);		
		}
		return DBObjectList;
	}
	
	public List<DBObject> findToDbObjectByPage(DBObject queryObject, String collections, PageInfo pageInfo) {
		List<DBObject> resultList = null;
		if (pageInfo == null) {
			resultList = findToDbObject(queryObject, collections);
		} else {
			pageInfo.setTotalRecords(getMongoTemplate().getCollection(collections).count(queryObject));
			DBCursor cursor = getMongoTemplate().getCollection(collections).find(queryObject).skip(pageInfo.getStartIndex()).limit(pageInfo.getResults());
			resultList = new ArrayList<DBObject>();
			while(cursor.hasNext()) {
				DBObject object = cursor.next();
				object.put("_id", object.get("_id").toString());
				resultList.add(object);
			}
		}
		return resultList;		
	}
	
	public int intsertFromDbObject(List<DBObject> dbObjectList, String collections) {
		return getMongoTemplate().getCollection(collections).insert(dbObjectList).getN();
	}
	
	public int intsertFromDbObject(DBObject dbObject, String collections) {
		return getMongoTemplate().getCollection(collections).insert(dbObject).getN();
	}

	public int updateFromDbObject(DBObject queryObject, DBObject updateObject, String collections) {
		return getMongoTemplate().getCollection(collections).update(queryObject, updateObject).getN();		
	}
	
	public int deleteFromDbObject(DBObject queryObject, String collections) {
		return getMongoTemplate().getCollection(collections).remove(queryObject).getN();		
	}
	
	//可以指定要查询的字段
	public List<DBObject> findToDbObjectByPage(DBObject queryObject,DBObject queryField, String collections, PageInfo pageInfo) {
		List<DBObject> resultList = null;
		if (pageInfo == null) {
			resultList = findToDbObject(queryObject, collections);
		} else {
			pageInfo.setTotalRecords(getMongoTemplate().getCollection(collections).count(queryObject));
			DBCursor cursor = getMongoTemplate().getCollection(collections).find(queryObject,queryField).skip(pageInfo.getStartIndex()).limit(pageInfo.getResults());
			resultList = new ArrayList<DBObject>();
			while(cursor.hasNext()) {
				DBObject object = cursor.next();
				object.put("_id", object.get("_id").toString());
				resultList.add(object);
			}
		}
		return resultList;		
	}
	
	public List<DBObject> findToDbObject(DBObject queryObject,DBObject queryField, String collections) {
		    List<DBObject> resultList = new ArrayList<DBObject>();
	 
			
			DBCursor cursor = getMongoTemplate().getCollection(collections).find(queryObject,queryField);
			
			while(cursor.hasNext()) {
				DBObject object = cursor.next();
				object.put("_id", object.get("_id").toString());
				resultList.add(object);
			}
		 
		return resultList;		
	}

	public AggregationOutput findByGroup(DBObject match, DBObject fields, DBObject group, DBObject sort,
	        String collections)
	{

		List<DBObject> pipeline = new ArrayList<DBObject>();
		pipeline.add(match);
		pipeline.add(fields);
		pipeline.add(group);
		pipeline.add(sort);
		AggregationOutput db = getMongoTemplate().getCollection(collections).aggregate(pipeline);

		return db;
	}
	
	
	
	
	// 可以指定要查询的字段
	public List<DBObject> findToDbObjectByPage(DBObject queryObject, DBObject queryField, String collections,
	        PageInfo pageInfo, DBObject sort)
	{
		List<DBObject> resultList = null;
		if (pageInfo == null)
		{
			resultList = findToDbObject(queryObject, collections);
		} else
		{
			pageInfo.setTotalRecords(getMongoTemplate().getCollection(collections).count(queryObject));
			DBCursor cursor = getMongoTemplate().getCollection(collections).find(queryObject, queryField).sort(sort)
			        .skip(pageInfo.getStartIndex()).limit(pageInfo.getResults());

			resultList = new ArrayList<DBObject>();
			while (cursor.hasNext())
			{
				DBObject object = cursor.next();
				object.put("_id", object.get("_id").toString());
				resultList.add(object);
			}
		}
		return resultList;
	}
}

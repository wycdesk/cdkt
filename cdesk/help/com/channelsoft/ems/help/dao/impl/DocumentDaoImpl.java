package com.channelsoft.ems.help.dao.impl;

import java.util.List;
import java.util.regex.Pattern;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.channelsoft.cri.mongo.BaseMongoTemplate;
import com.channelsoft.cri.vo.PageInfo;
import com.channelsoft.ems.help.dao.IDocumentDao;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class DocumentDaoImpl extends BaseMongoTemplate implements IDocumentDao{

	/*文档查询*/
	@Override
	public List<DBObject> queryDocument(String entId, String startTime, String endTime, String fastSearch, PageInfo pageInfo)
			throws DataAccessException {
		// TODO Auto-generated method stub
		Query query=new Query();
		Criteria cra = new Criteria();
		Pattern pattern = Pattern.compile("^.*" + fastSearch+ ".*$", Pattern.CASE_INSENSITIVE); 
		
		if(fastSearch!=""){
			query.addCriteria(cra.orOperator(Criteria.where("title").regex(pattern), Criteria.where("content").regex(pattern)));
		}
		
		if(startTime!="" && endTime!=""){
			query.addCriteria(Criteria.where("createTime").gte(startTime).lte(endTime));
		}
				
		// 按创建时间降序排序
		query.with(new Sort(Direction.DESC, "createTime"));
		return this.findByPage(query, getPublishCollection(entId), DBObject.class, pageInfo);		
	}
	
	public String getPublishCollection(String entId){
		return "entId_"+entId+"_publish";
	}
	
	public String getAttachmentCollection(String entId){
		return "entId_"+entId+"_attachment";
	}
	
	/*根据Id查询文档*/
	@Override
	public List<DBObject> queryDocById(String entId, String docId, PageInfo pageInfo)
			throws DataAccessException {
		// TODO Auto-generated method stub
		Query query=new Query();
		
		if(docId!=""){
			query.addCriteria(Criteria.where("docId").is(docId));
		}
		
		return this.findByPage(query, getPublishCollection(entId), DBObject.class, pageInfo);		
	}
	
	/*删除文档*/
	@Override
	public int deleteDocument(String entId, String[] ids)
	{
		String collectioName = this.getPublishCollection(entId);
		BasicDBList value = new BasicDBList();
		for (String docId : ids)
		{
			value.add((new BasicDBObject("docId", docId)));
		}
		BasicDBObject queryObject = new BasicDBObject();
		queryObject.append("$or", value);
		return this.deleteFromDbObject(queryObject, collectioName);
	}
	
	/*我发布的文档*/
	@Override
	public List<DBObject> queryDocBelongMe(String creatorId, String entId, PageInfo pageInfo)
			throws DataAccessException {
		// TODO Auto-generated method stub
		Query query=new Query();
		
		if(creatorId!=""){
			query.addCriteria(Criteria.where("creatorId").is(creatorId));
		}
						
		// 按创建时间降序排序
		query.with(new Sort(Direction.DESC, "createTime"));
		return this.findByPage(query, getPublishCollection(entId), DBObject.class, pageInfo);		
	}
	
	/*根据Id查询附件*/
	@Override
	public List<DBObject> queryAttachById(String entId, int id, PageInfo pageInfo)
			throws DataAccessException {
		// TODO Auto-generated method stub
		Query query=new Query();
		
	    query.addCriteria(Criteria.where("attachmentId").is(id));
		
		return this.findByPage(query, getAttachmentCollection(entId), DBObject.class, pageInfo);		
	}
}

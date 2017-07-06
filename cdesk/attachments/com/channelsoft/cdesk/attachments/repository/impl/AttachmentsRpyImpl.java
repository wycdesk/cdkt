package com.channelsoft.cdesk.attachments.repository.impl;


import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.channelsoft.cdesk.attachments.po.EntWorkOrderInfoPo;
import com.channelsoft.cdesk.attachments.po.FileAttrPo;
import com.channelsoft.cdesk.attachments.repository.IAttachementsRpy;
import com.channelsoft.cdesk.attachments.service.impl.AttachementsServiceImpl;
import com.channelsoft.cri.mongo.BaseMongoTemplate;
import com.channelsoft.cri.vo.PageInfo;
import com.channelsoft.ems.field.po.BaseFieldPo;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoException;

public class AttachmentsRpyImpl extends BaseMongoTemplate implements IAttachementsRpy{
   
	private static Logger log = LoggerFactory.getLogger(AttachmentsRpyImpl.class);
	
	@Override
	public int delete(String[] newFileName,String collection) throws MongoException {
		// TODO Auto-generated method stub
		DBObject query = new BasicDBObject();
		BasicDBList value = new BasicDBList();
		for (String attachment : newFileName)
		{   
			DBObject find = new BasicDBObject("fileNew", attachment);
			List<DBObject> findList = this.findToDbObject(find, collection);
			if(findList==null||findList.size()!=1){
				throw new MongoException("数据库不存在改文件！");
			}
			value.add(find);
		}
		
		query.put("$or", value);
		
		return this.deleteFromDbObject(query, collection);
		
	}

	@Override
	public int insert(DBObject dbo,String collections) throws MongoException {
		// TODO Auto-generated method stub
		log.info("-------进入AttachmentsRpyImpl.insert()方法,存储文件信息到mongodb中--------");
		return this.intsertFromDbObject(dbo, collections);
		
	}

	@Override
	public int getFileId(String entId) throws MongoException {
		// TODO Auto-generated method stub
		//设置查询条件
		Query query=new Query(Criteria.where("entId").is(entId));
		//设置更新条件
		Update update=new Update().inc("attachment", 1);
		EntWorkOrderInfoPo entWorkInf=this.getMongoTemplate().findAndModify(query, update,new FindAndModifyOptions().returnNew(true), EntWorkOrderInfoPo.class, "workseq");	
		return entWorkInf.getAttachment();
		
	}

	@Override
	public String getAttachmentsName(String newFileName,String collections) throws MongoException {
		// TODO Auto-generated method stub
		DBObject dbo = new BasicDBObject("fileNew",newFileName);
		List<DBObject> list = this.findToDbObject(dbo, collections);
		if(list == null || list.size() == 0){
			throw new MongoException("下载的文件不存在！");
		}
		return (String)list.get(0).get("originalName");
	}

	@Override
	public List<DBObject> query(String collectionName, String source,
			String type,PageInfo pageinfo) throws MongoException {
		// TODO Auto-generated method stub
		DBObject dbo = new BasicDBObject();
		if(StringUtils.isNotBlank(source)){
			if(source.equals("3")){
				dbo.put("source", new BasicDBObject("$exists",true));
			}else{
				dbo.put("source", source);	
			}
		}else{
			dbo.put("source",new BasicDBObject("$exists",false));
		}
		
		if(StringUtils.isNotBlank(type)){
			if(type.equals("4")){
				dbo.put("type", new BasicDBObject("$exists",true));
			}else{
				dbo.put("type", type);
			}
			
		}
		DBObject sort = new BasicDBObject();
		sort.put("createTime", -1);
		
		return this.findToDbObjectByPage(dbo, null, collectionName, pageinfo, sort);	
	}

	@Override
	public void deleteReply(String[] newFileName, String collectionName)
			throws MongoException {
		// TODO Auto-generated method stub
	
		for(String attachment : newFileName){
			DBObject query = new BasicDBObject();
			DBObject update = new BasicDBObject();
			query.put("event.attachment.fileNew", attachment);
			update.put("$pull", new BasicDBObject("event.$.attachment",new BasicDBObject("fileNew", attachment)));
			this.updateFromDbObject(query, update, collectionName);
		}
	}
}

package com.channelsoft.cdesk.attachments.repository;

import java.util.List;

import com.channelsoft.cri.vo.PageInfo;
import com.mongodb.DBObject;
import com.mongodb.MongoException;

public interface IAttachementsRpy {
   
	public int delete(String[] newFileName,String collection) throws MongoException;
	
	public int insert(DBObject dbo,String collections) throws MongoException;

	public int getFileId(String entId)throws MongoException;

	public String getAttachmentsName(String newFileName,String collection) throws MongoException;

	public List<DBObject> query(String collectionName,String source,String type,PageInfo pageinfo) throws MongoException;

	public void deleteReply(String[] newFileName, String collectionName) throws MongoException;
}

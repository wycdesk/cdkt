package com.channelsoft.ems.help.service;

import java.util.List;

import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.vo.PageInfo;
import com.mongodb.DBObject;

public interface IDocumentService {

	/*文档查询*/
	public List<DBObject> queryDocument(String entId, String startTime, String endTime, String fastSearch, PageInfo pageInfo) throws  ServiceException;
	
	/*根据Id查询文档*/
	public List<DBObject> queryDocById(String entId, String docId, PageInfo pageInfo) throws  ServiceException;

	/*删除文档*/
	public int deleteDocument(String entId, String[] ids)  throws ServiceException; 	
	
	/*我发布的文档*/
	public List<DBObject> queryDocBelongMe(String creatorId, String entId, PageInfo pageInfo) throws  ServiceException;
	
	/*根据Id查询附件*/
	public List<DBObject> queryAttachById(String entId, List<Integer> ids, PageInfo pageInfo) throws  ServiceException;
}

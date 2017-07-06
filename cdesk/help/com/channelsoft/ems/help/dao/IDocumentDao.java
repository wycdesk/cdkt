package com.channelsoft.ems.help.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.channelsoft.cri.vo.PageInfo;
import com.mongodb.DBObject;

public interface IDocumentDao {

	/*文档查询*/
	public List<DBObject> queryDocument(String entId, String startTime, String endTime, String fastSearch, PageInfo pageInfo) throws DataAccessException;
	
	/*根据Id查询文档*/
	public List<DBObject> queryDocById(String entId, String docId, PageInfo pageInfo) throws DataAccessException;

	/*删除文档*/
	public int deleteDocument(String entId, String[] ids) throws DataAccessException;
	
	/*我发布的文档*/
	public List<DBObject> queryDocBelongMe(String creatorId, String entId, PageInfo pageInfo) throws DataAccessException;
	
	/*根据Id查询附件*/
	public List<DBObject> queryAttachById(String entId, int id, PageInfo pageInfo) throws DataAccessException;
}

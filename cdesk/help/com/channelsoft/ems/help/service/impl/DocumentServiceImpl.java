package com.channelsoft.ems.help.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.vo.PageInfo;
import com.channelsoft.ems.help.dao.IDocumentDao;
import com.channelsoft.ems.help.service.IDocumentService;
import com.mongodb.DBObject;

public class DocumentServiceImpl implements IDocumentService{

	@Autowired
	IDocumentDao documentMongoDao;
	
	
	/*文档查询*/
	@Override
	public List<DBObject> queryDocument(String entId, String startTime, String endTime, String fastSearch, PageInfo pageInfo)
			throws ServiceException {
		// TODO Auto-generated method stub
		try{
			return documentMongoDao.queryDocument(entId, startTime, endTime, fastSearch, pageInfo);
		}
		catch(DataAccessException e){
			e.printStackTrace();
			throw new ServiceException("查询文档出错");
		}
	}
	
	/*根据Id查询文档*/
	@Override
	public List<DBObject> queryDocById(String entId, String docId, PageInfo pageInfo)
			throws ServiceException {
		// TODO Auto-generated method stub
		try{
			return documentMongoDao.queryDocById(entId, docId, pageInfo);
		}
		catch(DataAccessException e){
			e.printStackTrace();
			throw new ServiceException("查询文档出错");
		}
	}
	
	/*删除文档*/
	@Override
	public int deleteDocument(String entId, String[] ids)  throws ServiceException{
		int result = documentMongoDao.deleteDocument(entId, ids);
		return result;
	}
	
	/*我发布的文档*/
	@Override
	public List<DBObject> queryDocBelongMe(String creatorId, String entId, PageInfo pageInfo)
			throws ServiceException {
		// TODO Auto-generated method stub
		try{
			return documentMongoDao.queryDocBelongMe(creatorId, entId, pageInfo);
		}
		catch(DataAccessException e){
			e.printStackTrace();
			throw new ServiceException("查询文档出错");
		}
	}
		
	/*根据Id查询附件*/
	@Override
	public List<DBObject> queryAttachById(String entId, List<Integer> ids, PageInfo pageInfo)
			throws ServiceException {
		// TODO Auto-generated method stub
		try{
			List<DBObject> attachList=new ArrayList<DBObject>();
			
			for(int i=0;i<ids.size();i++){
				int id=ids.get(i);
				List<DBObject> list=documentMongoDao.queryAttachById(entId, id, pageInfo);
				if(list.size()!=0){
					attachList.add(list.get(0));
				}
			}
			return attachList;
		}
		catch(DataAccessException e){
			e.printStackTrace();
			throw new ServiceException("查询附件出错");
		}
	}
}

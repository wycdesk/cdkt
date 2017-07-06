package com.channelsoft.ems.communicate.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.vo.PageInfo;
import com.channelsoft.ems.communicate.constant.WorkSource;
import com.channelsoft.ems.communicate.dao.ICommDao;
import com.channelsoft.ems.communicate.po.CommPo;
import com.channelsoft.ems.communicate.service.ICommService;
import com.channelsoft.ems.user.dao.IUserDao;
import com.channelsoft.ems.user.dao.IUserMongoDao;
import com.channelsoft.ems.user.po.DatEntUserPo;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class CommServiceImpl implements ICommService {
	@Autowired
	ICommDao commDao;
	@Autowired
	IUserDao userDao;
	@Autowired
	IUserMongoDao userMongoDao;
	@Override
	public int saveComm(String entId, DBObject commObj) throws ServiceException {
		try {
			if(StringUtils.isBlank(entId)){
				throw new ServiceException("企业ID为空");
			}
			if(!commObj.containsField("source")||StringUtils.isBlank((String)commObj.get("source"))){
				commObj.put("source", WorkSource.WEBPAGE.key);
			}
			if(!commObj.containsField("commId")||StringUtils.isBlank((String)commObj.get("commId"))){
				commObj.put("commId",userDao.getDocId("SEQ_COMMUNICATE"));
			}
			if(!commObj.containsField("opId")||!commObj.containsField("opName")){
				throw new Exception("坐席信息不正确或不完整");
			}
			if(!commObj.containsField("userId")||!commObj.containsField("userName")||!commObj.containsField("loginName")){
				throw new Exception("客户信息不正确或不完整");
			}
			return commDao.saveComm(entId,commObj);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	@Override
	public List<DBObject> getComms(String entId, CommPo commPo,PageInfo pageInfo) throws ServiceException {
		try {
			return commDao.getComms(entId,commPo,pageInfo);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	@Override
	public List<DBObject> getCommsByUserId(String entId, String userId) throws ServiceException {
		if(StringUtils.isBlank(userId)){
			throw new ServiceException("用户ID为空");
		}
		CommPo commPo=new CommPo();
		commPo.setUserId(userId);
		try {
			return commDao.getComms(entId,commPo,null);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	@Override
	public List<DBObject> queryTodayWorkOrderDetailNumber(String entId)
			throws ServiceException {
		Date d = new Date();  
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
		String todayDate = sdf.format(d); 
		return commDao.queryTodayWorkOrderDetailNumber(todayDate, entId);
	}
	@Override
	public int endComm(String entId, String sessionId, DBObject commObj) throws ServiceException {
		try {
			return commDao.endComm(entId,sessionId,commObj);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	@Override
	public int saveContent(String entId, String content, String sessionId, String commId) throws ServiceException {
		try {
			return commDao.saveContent(entId,content,sessionId,commId);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	@Override
	public List<DBObject> querySource(String entId) throws ServiceException {
		try {
			return commDao.querySource(entId);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	@Override
	public long countSession(String entId,String sessionId) throws ServiceException {
		try {
			return commDao.countSession(entId,sessionId);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public DBObject getCommBySession(String entId, String sessionId) throws ServiceException {
		try {
			return commDao.getCommBySession(entId,sessionId);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	@Override
	public String saveIMComm(String entId, DBObject commObj) throws ServiceException {
		if(StringUtils.isBlank(entId)){
			throw new ServiceException("企业ID为空");
		}
		if(!commObj.containsField("source")||StringUtils.isBlank((String)commObj.get("source"))){
			commObj.put("source", WorkSource.WEBPAGE.key);
		}
		if(!commObj.containsField("commId")||StringUtils.isBlank((String)commObj.get("commId"))){
			commObj.put("commId",userDao.getDocId("SEQ_COMMUNICATE"));
		}
		if(!commObj.containsField("opId")||!commObj.containsField("opName")){
			throw new ServiceException("坐席信息不正确或不完整");
		}
		if(!commObj.containsField("userId")||!commObj.containsField("userName")||!commObj.containsField("loginName")){
			throw new ServiceException("客户信息不正确或不完整");
		}
		try {
			int save=commDao.saveComm(entId,commObj);
			if(save<0){
				throw new ServiceException("保存失败");
			}
			return (String)commObj.get("commId");
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	@Override
	public List<DBObject> getRecentServiceNum(String entId, String beginTime, String endTime) throws ServiceException {
		try {
			return commDao.getRecentServiceNum(entId,beginTime,endTime);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	@Override
	public List<DBObject> getBaseSortList(String entId, String beginTime, String endTime, String type)
			throws ServiceException {
		try {
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			Date sTime=sdf.parse(beginTime+" 00:00:00");
			Date eTime=sdf.parse(endTime+" 23:59:59");
			DBObject[] array={new BasicDBObject("createTime",new BasicDBObject("$gte",sTime)),
					new BasicDBObject("createTime",new BasicDBObject("$lte",eTime))};
			DBObject queryDB=new BasicDBObject("$and",array);
			queryDB.put("entId", entId);
			Iterator<DBObject> ito=commDao.getBaseSortList(queryDB,type);
			List<DBObject> rtn=new ArrayList<DBObject>();
			while(ito.hasNext()){
				rtn.add(ito.next());
			}
			return rtn;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	
	/*给移动端提供的联络历史查询接口*/
	@Override
	public List<DBObject> getCommsForApi(String entId, CommPo commPo,PageInfo pageInfo) throws ServiceException {
		try {
			return commDao.getCommsForApi(entId,commPo,pageInfo);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	@Override
	public int updateComm(String entId, String commId, DBObject update) throws ServiceException {
		try {
			return commDao.updateComm(entId,commId,update);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public int mergeComm(String entId, String userMergeId, String userTargetId) throws ServiceException {
		try {
			DatEntUserPo user = userMongoDao.getEntUserPoById(entId, userTargetId);
			if(user==null){
				throw new ServiceException("目标用户不存在");
			}
			return commDao.mergeComm(entId,userMergeId,user);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
}

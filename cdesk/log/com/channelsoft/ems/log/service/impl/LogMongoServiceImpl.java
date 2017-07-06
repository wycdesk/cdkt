package com.channelsoft.ems.log.service.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.vo.PageInfo;
import com.channelsoft.ems.log.constants.BusinessTypeEnum;
import com.channelsoft.ems.log.constants.LogTypeEnum;
import com.channelsoft.ems.log.dao.ILogMongoDao;
import com.channelsoft.ems.log.po.CfgUserOperateLogPo;
import com.channelsoft.ems.log.service.ILogMongoService;
import com.channelsoft.ems.log.util.LogUtils;
import com.channelsoft.ems.sso.util.SsoSessionUtils;
import com.channelsoft.ems.sso.vo.SsoUserVo;
import com.channelsoft.ems.user.po.DatEntUserPo;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoException;

public class LogMongoServiceImpl implements ILogMongoService {

	@Autowired
	ILogMongoDao logMongoDao;
	
	@Override
	public int add(HttpServletRequest request, LogTypeEnum oper,BusinessTypeEnum type, String operateObjectId,String operateObject, Object obj) throws ServiceException {
		// TODO Auto-generated method stub
		CfgUserOperateLogPo log=LogUtils.getLogInfo(request, oper, type, operateObjectId, operateObject,obj);
		DBObject logObject = this.getLogObject(log);
		return this.add(logObject, log.getEntId());
	}
    /**
     * 日志对象转化为DBObject
     * @param log
     * @return
     */
	public  DBObject getLogObject(CfgUserOperateLogPo log){
		DBObject logObject = new BasicDBObject();
		logObject.put("entId",log.getEntId());
		logObject.put("userId",log.getUserId());
		logObject.put("loginName",log.getLoginName());
		logObject.put("userName",log.getUserName());
		logObject.put("loginIp",log.getLoginIp());
		logObject.put("operation",log.getOperation());
		logObject.put("businessType",log.getBusinessType());
		logObject.put("operateObjectId",log.getOperateObjectId());
		logObject.put("operateObject",log.getOperateObject());
		logObject.put("sourceContent",log.getSourceContent());
		logObject.put("destContent",log.getDestContent());
		return logObject;
	}
	@Override
	public int add(DBObject dbo, String entId) throws ServiceException {
		// TODO Auto-generated method stub
		try{
			return logMongoDao.add(dbo, entId);
		}
		catch(MongoException e){
			e.printStackTrace();
			throw new ServiceException("添加日志失败");
		}
	}
	@Override
	public int add(HttpServletRequest request,SsoUserVo user, LogTypeEnum oper, BusinessTypeEnum type,String operateObjectId, String operateObject, Object obj)
			throws ServiceException {
		// TODO Auto-generated method stub
		SsoUserVo user1=SsoSessionUtils.getUserInfo(request);
		if(user1==null&&user==null){
			throw new ServiceException("用户信息不能为空");
		}
		CfgUserOperateLogPo log=LogUtils.getLogInfo(user, oper, type, operateObjectId, operateObject,request.getRemoteAddr());
		DBObject logObject = this.getLogObject(log);
		return this.add(logObject, log.getEntId());
	}
	@Override
	public List<CfgUserOperateLogPo> query(CfgUserOperateLogPo po,PageInfo pageInfo, String entId) throws ServiceException {
		// TODO Auto-generated method stub
		try{
			return logMongoDao.query(po, pageInfo, entId);
		}
		catch(MongoException e){
			e.printStackTrace();
			throw new ServiceException("查询日志失败");
		}
	}
	@Override
	public int add(String remoteIp,DatEntUserPo user, LogTypeEnum oper, BusinessTypeEnum type,String operateObjectId, String operateObject, Object obj)
			throws ServiceException {
		// TODO Auto-generated method stub
		SsoUserVo so=new SsoUserVo();
		so.setLoginName(user.getLoginName());
		so.setUserId(user.getUserId());
		so.setUserName(user.getUserName());
		so.setNickName(user.getNickName());
		so.setEntId(user.getEntId());
		CfgUserOperateLogPo log=LogUtils.getLogInfo(so, oper, type, operateObjectId, operateObject,remoteIp);
		
		DBObject logObject = this.getLogObject(log);
		return this.add(logObject, log.getEntId());
	}

}

package com.channelsoft.ems.log.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.vo.PageInfo;
import com.channelsoft.ems.log.constants.BusinessTypeEnum;
import com.channelsoft.ems.log.constants.LogTypeEnum;
import com.channelsoft.ems.log.po.CfgUserOperateLogPo;
import com.channelsoft.ems.sso.vo.SsoUserVo;
import com.channelsoft.ems.user.po.DatEntUserPo;
import com.mongodb.DBObject;

public interface ILogMongoService {

	/**
	 * 添加日志
	 * @param request
	 * @param oper
	 * @param type
	 * @param operateObjectId
	 * @param operateObject
	 * @param obj
	 * @return
	 * @throws ServiceException
	 */
	public int add(HttpServletRequest request,LogTypeEnum oper,BusinessTypeEnum type,String operateObjectId,String operateObject,Object obj) throws ServiceException;
	
	public int add(DBObject dbo,String entId) throws ServiceException;
	
	/**
	 * 添加日志
	 * @param request session里面没有user的时候需要传入 user对象
	 * @param user
	 * @param oper
	 * @param type
	 * @param operateObjectId
	 * @param operateObject
	 * @param obj
	 * @return
	 * @throws ServiceException
	 */
	public int add(HttpServletRequest request,SsoUserVo user,LogTypeEnum oper,BusinessTypeEnum type,String operateObjectId,String operateObject,Object obj) throws ServiceException;
	
	/**
	 * 添加日志
	 * @param user 添加人ip
	 * @param user 添加用户对象，用户对象里面必须含有企业ID、登陆账号、用户ID
	 * @param oper
	 * @param type
	 * @param operateObjectId
	 * @param operateObject
	 * @param obj
	 * @return
	 * @throws ServiceException
	 */
	public int add(String remoteIp,DatEntUserPo user,LogTypeEnum oper,BusinessTypeEnum type,String operateObjectId,String operateObject,Object obj) throws ServiceException;
	/**
	 * 查询日志
	 * @param po
	 * @param pageInfo
	 * @param entId
	 * @return
	 * @throws ServiceException
	 */
	public List<CfgUserOperateLogPo> query(CfgUserOperateLogPo po,PageInfo pageInfo,String entId) throws  ServiceException;
}

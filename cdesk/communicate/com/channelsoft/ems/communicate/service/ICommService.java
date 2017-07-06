package com.channelsoft.ems.communicate.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.vo.AjaxResultPo;
import com.channelsoft.cri.vo.PageInfo;
import com.channelsoft.ems.communicate.po.CommPo;
import com.mongodb.DBObject;

public interface ICommService {
	
	/**
	 * 保存一条联络历史
	 * @param entId
	 * @param commPo
	 * @return
	 * @throws ServiceException
	 */
	int saveComm(String entId, DBObject commObj) throws ServiceException;
	
	/**
	 * 保存一条联络历史
	 * @param entId
	 * @param commPo
	 * @return
	 * @throws ServiceException
	 */
	String saveIMComm(String entId, DBObject commObj) throws ServiceException;

	/**
	 * 分页查询联络历史（可带参数）
	 * @param entId
	 * @param commPo
	 * @param pageInfo
	 * @return
	 * @throws ServiceException
	 */
	List<DBObject> getComms(String entId, CommPo commPo, PageInfo pageInfo) throws ServiceException;

	/**
	 * 根据客户ID查询联络历史（主要用于客户详情页面）
	 * @param entId
	 * @param userId
	 * @return
	 * @throws ServiceException
	 */
	List<DBObject> getCommsByUserId(String entId, String userId) throws ServiceException;

	List<DBObject> queryTodayWorkOrderDetailNumber(String entId) throws ServiceException;

	/**
	 * 联络结束，保存结束时间和联络时长
	 * @param entId
	 * @param sessionId
	 * @param start
	 * @param commTime 
	 * @return
	 * @throws ServiceException
	 */
	int endComm(String entId, String sessionId, DBObject commObj) throws ServiceException;

	/**
	 * 保存联络的沟通小结
	 * @param entId
	 * @param content
	 * @param sessionId
	 * @param commId 
	 * @return
	 * @throws ServiceException
	 */
	int saveContent(String entId, String content, String sessionId, String commId) throws ServiceException;
	
	/**
	 * 查询请求渠道
	 * @param entId
	 * @return
	 * @throws ServiceException
	 * @author wangjie
	 * @time 2016年3月9日下午7:13:51
	 */
	List<DBObject> querySource(String entId) throws ServiceException;
	
	/**
	 * 判断数据库中是否存在此sessionId
	 * @param string
	 * @return
	 * @throws ServiceException
	 */
	long countSession(String entId,String sessionId) throws ServiceException;

	DBObject getCommBySession(String entId, String sessionId) throws ServiceException;

	List<DBObject> getRecentServiceNum(String entId, String beginTime, String endTime) throws ServiceException;

	List<DBObject> getBaseSortList(String entId, String beginTime, String endTime, String type) throws ServiceException;
	
	
	/*给移动端提供的联络历史查询接口*/
	List<DBObject> getCommsForApi(String entId, CommPo commPo, PageInfo pageInfo) throws ServiceException;

	int updateComm(String entId, String commId, DBObject update) throws ServiceException;
	
	/**
	 * 合并用户的时候调用此接口合并联络历史
	 * @param request
	 * @param userMergeId
	 * @param userTargetId
	 * @return
	 * @throws ServiceException
	 */
	int mergeComm(String entId,String userMergeId,String userTargetId) throws ServiceException;
}

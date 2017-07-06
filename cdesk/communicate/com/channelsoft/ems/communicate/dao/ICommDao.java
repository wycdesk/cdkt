package com.channelsoft.ems.communicate.dao;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.dao.DataAccessException;

import com.channelsoft.cri.vo.PageInfo;
import com.channelsoft.ems.communicate.po.CommPo;
import com.channelsoft.ems.user.po.DatEntUserPo;
import com.mongodb.DBObject;

public interface ICommDao {

	/**
	 * 保存一条联络历史
	 * @param entId
	 * @param commObj
	 * @return
	 * @throws DataAccessException
	 */
	int saveComm(String entId, DBObject commObj) throws DataAccessException;

	/**
	 * （分页）查询联络历史（可带参数）
	 * @param entId
	 * @param commPo
	 * @param pageInfo
	 * @return
	 * @throws DataAccessException
	 */
	List<DBObject> getComms(String entId, CommPo commPo, PageInfo pageInfo) throws DataAccessException;

	List<DBObject> queryTodayWorkOrderDetailNumber(String todayDate, String entId);

	/**
	 * 联络结束，保存结束时间和联络时长
	 * @param entId
	 * @param sessionId
	 * @param start
	 * @param commTime 
	 * @return
	 * @throws DataAccessException
	 */
	int endComm(String entId, String sessionId, DBObject commObj) throws DataAccessException;

	/**
	 * 保存沟通小结
	 * @param entId
	 * @param content
	 * @param sessionId
	 * @param commId 
	 * @return
	 * @throws DataAccessException
	 */
	int saveContent(String entId, String content, String sessionId, String commId) throws DataAccessException;
	
	/**
	 * 查询请求渠道
	 * @param entId
	 * @return
	 * @author wangjie
	 * @time 2016年3月9日下午7:04:34
	 */
	List<DBObject> querySource(String entId);

	/**
	 * 判断数据库中sessionId是否存在
	 * @param sessionId
	 * @return
	 * @throws DataAccessException
	 */
	long countSession(String entId,String sessionId) throws DataAccessException;

	DBObject getCommBySession(String entId, String sessionId) throws DataAccessException;

	List<DBObject> getRecentServiceNum(String entId, String beginTime, String endTime) throws DataAccessException;

	Iterator<DBObject> getBaseSortList(DBObject queryDB, String type) throws DataAccessException;
	
	
	/*给移动端提供的链路历史接口*/
	List<DBObject> getCommsForApi(String entId, CommPo commPo, PageInfo pageInfo) throws DataAccessException;

	int updateComm(String entId, String commId, DBObject update) throws DataAccessException;

	/**
	 * 合并联络历史
	 * @param entId
	 * @param userMergeId
	 * @param user
	 * @return
	 */
	int mergeComm(String entId, String userMergeId, DatEntUserPo user) throws DataAccessException;
}

package com.channelsoft.ems.communicate.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.channelsoft.cri.vo.PageInfo;
import com.mongodb.DBObject;

public interface IHistoryDao {

	public int add(DBObject dbo, String entId) throws DataAccessException;

	public String getUserTable(String entId) throws DataAccessException;

	/**
	 * 
	 * 
	 * @Description: 查询联络历史
	 * @author chenglitao
	 * @param    
	 * @date 2016年5月25日 下午5:20:37   
	 *
	 */
	public List<DBObject> queryCommHistory(DBObject queryObject,
			PageInfo pageInfo);
}

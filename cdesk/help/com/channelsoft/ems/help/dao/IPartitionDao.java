package com.channelsoft.ems.help.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.channelsoft.ems.help.po.PartitionPo;
import com.channelsoft.ems.help.po.PartitionSubPo;
import com.mongodb.DBObject;

public interface IPartitionDao {

	List<PartitionPo> getPartition(String entId, PartitionPo po, boolean isAll) throws DataAccessException;

	List<PartitionSubPo> getSubClass(String entId,PartitionSubPo subClass) throws DataAccessException;

	int addPartition(String entId, PartitionPo po) throws DataAccessException;

	String getPartitionId() throws DataAccessException;

	String getPartitionSubId() throws DataAccessException;

	int addPartitionSub(String entId, PartitionSubPo po) throws DataAccessException;

	int editPartition(String entId, PartitionPo po) throws DataAccessException;

	int editPartitionSub(String entId, PartitionSubPo po) throws DataAccessException;

	int deletePartition(String entId, PartitionPo po) throws DataAccessException;

	int deletePartitionSub(String entId, PartitionSubPo po) throws DataAccessException;

	int goPublish(String entId, DBObject dbo) throws DataAccessException;

	String getDocId() throws DataAccessException;
	
    /*编辑文档*/
	public int goEdit(String entId, DBObject dbo) throws DataAccessException;

}

package com.channelsoft.ems.help.service;

import java.util.List;

import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.ems.help.constant.PublishDocPo;
import com.channelsoft.ems.help.po.PartitionPo;
import com.channelsoft.ems.help.po.PartitionSubPo;

public interface IPartitionService {

	List<PartitionPo> getDetailPartition(String entId, PartitionPo po, boolean isAll) throws ServiceException;
	
	List<PartitionPo> getPartition(String entId, PartitionPo po, boolean isAll) throws ServiceException;
	
	List<PartitionSubPo> getSubClass(String entId,PartitionSubPo subClass) throws ServiceException;

	int addPartition(String entId, PartitionPo po) throws ServiceException;

	int addPartitionSub(String entId, PartitionSubPo po) throws ServiceException;

	int editPartition(String entId, PartitionPo po) throws ServiceException;

	int editPartitionSub(String entId, PartitionSubPo po) throws ServiceException;

	int deletePartition(String entId, PartitionPo po) throws ServiceException;

	int deletePartitionSub(String entId, PartitionSubPo po) throws ServiceException;

	int goPublish(String entId, PublishDocPo doc) throws ServiceException;
	
	/*编辑文档*/
	public int goEdit(String entId, PublishDocPo doc)  throws ServiceException;
}

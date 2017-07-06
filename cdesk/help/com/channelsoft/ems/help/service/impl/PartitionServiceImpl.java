package com.channelsoft.ems.help.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.util.DBObjectUtils;
import com.channelsoft.ems.help.constant.PublishDocPo;
import com.channelsoft.ems.help.dao.IPartitionDao;
import com.channelsoft.ems.help.po.PartitionPo;
import com.channelsoft.ems.help.po.PartitionSubPo;
import com.channelsoft.ems.help.service.IPartitionService;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

public class PartitionServiceImpl implements IPartitionService{
	@Autowired
	IPartitionDao partitionDao;
	

	@Override
	public List<PartitionPo> getDetailPartition(String entId, PartitionPo po, boolean isAll) 
			throws ServiceException {
		List<PartitionPo> partitions=this.getPartition(entId,po,isAll);
		if(partitions==null||partitions.size()<=0){
			return partitions;
		}
		for(int i=0;i<partitions.size();i++){
			PartitionPo partition=partitions.get(i);
			PartitionSubPo subPo=new PartitionSubPo();
			subPo.setPartitionId(partition.getId());
			List<PartitionSubPo> partSub=this.getSubClass(entId,subPo);
			partition.setSubClass(partSub);
		}
		return partitions;
	}

	@Override
	public List<PartitionPo> getPartition(String entId, PartitionPo po, boolean isAll) 
			throws ServiceException {
		try {
			return partitionDao.getPartition(entId,po,isAll);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<PartitionSubPo> getSubClass(String entId,PartitionSubPo subClass) throws ServiceException {
		try {
			return partitionDao.getSubClass(entId,subClass);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public int addPartition(String entId, PartitionPo po) throws ServiceException {
		if(po==null){
			throw new ServiceException("分区对象为空");
		}
		if(StringUtils.isBlank(po.getTitle())){
			throw new ServiceException("标题不能为空");
		}
		if(StringUtils.isNotBlank(po.getShowNum())&&Integer.valueOf(po.getShowNum())==0){
			throw new ServiceException("每个分类显示文档数量 数值太小 (最小值为 1).");
		}
		if(StringUtils.isBlank(po.getShowNum())){
			po.setShowNum("3");
		}
		try {
			po.setId(partitionDao.getPartitionId());
			return partitionDao.addPartition(entId,po);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
		
	}

	@Override
	public int addPartitionSub(String entId, PartitionSubPo po) throws ServiceException {
		if(po==null){
			throw new ServiceException("分区子类对象为空");
		}
		if(StringUtils.isBlank(po.getTitle())){
			throw new ServiceException("标题不能为空");
		}
		if(StringUtils.isBlank(po.getPartitionId())){
			throw new ServiceException("分区不能为空");
		}
		if(StringUtils.isBlank(po.getUserPermission())){
			throw new ServiceException("查看权限不能为空");
		}
		try {
			po.setId(partitionDao.getPartitionSubId());
			return partitionDao.addPartitionSub(entId,po);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public int editPartition(String entId, PartitionPo po) throws ServiceException {
		if(po==null){
			throw new ServiceException("分区对象为空");
		}
		if(StringUtils.isBlank(po.getTitle())){
			throw new ServiceException("标题不能为空");
		}
		if(StringUtils.isBlank(po.getId())){
			throw new ServiceException("id不能为空");
		}
		if(StringUtils.isNotBlank(po.getShowNum())&&Integer.valueOf(po.getShowNum())==0){
			throw new ServiceException("每个分类显示文档数量 数值太小 (最小值为 1).");
		}
		if(StringUtils.isBlank(po.getShowNum())){
			po.setShowNum("3");
		}
		try {
			return partitionDao.editPartition(entId,po);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public int editPartitionSub(String entId, PartitionSubPo po) throws ServiceException {
		if(po==null){
			throw new ServiceException("分区子类对象为空");
		}
		if(StringUtils.isBlank(po.getTitle())){
			throw new ServiceException("标题不能为空");
		}
		if(StringUtils.isBlank(po.getId())){
			throw new ServiceException("id不能为空");
		}
		if(StringUtils.isBlank(po.getPartitionId())){
			throw new ServiceException("分区不能为空");
		}
		if(StringUtils.isBlank(po.getUserPermission())){
			throw new ServiceException("查看权限不能为空");
		}
		try {
			return partitionDao.editPartitionSub(entId,po);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public int deletePartition(String entId, PartitionPo po) throws ServiceException {
		try {
			return partitionDao.deletePartition(entId,po);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public int deletePartitionSub(String entId, PartitionSubPo po) throws ServiceException {
		try {
			return partitionDao.deletePartitionSub(entId,po);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public int goPublish(String entId, PublishDocPo doc) throws ServiceException {
		try {
			String tm=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			doc.setCreateTime(tm);
			doc.setUpdateTime(doc.getCreateTime());
			doc.setPartition("默认分类");
			String docId=partitionDao.getDocId();
			doc.setDocId(docId);
			DBObject dbo=DBObjectUtils.getDBObject(doc);
			return partitionDao.goPublish(entId,dbo);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}

	
	/*编辑文档*/
	@Override
	public int goEdit(String entId, PublishDocPo doc) throws ServiceException {
		// TODO Auto-generated method stub
		try{			
			String tm=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			doc.setUpdateTime(tm);
			
			DBObject dbo=DBObjectUtils.getDBObject(doc);
			return partitionDao.goEdit(entId,dbo);			
		}
		catch(DataAccessException e){
			e.printStackTrace();
			throw new ServiceException("修改用户出错");
		}
	}
}

package com.channelsoft.ems.field.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.vo.PageInfo;
import com.channelsoft.ems.ent.po.DatEntInfoPo;
import com.channelsoft.ems.ent.service.IDatEntService;
import com.channelsoft.ems.field.dao.IFieldMongoDao;

import com.channelsoft.ems.field.po.BaseFieldPo;
import com.channelsoft.ems.field.service.IFieldService;


public class FieldServiceImpl implements IFieldService{

	@Autowired
	IFieldMongoDao fieldMongoDao;
	@Autowired
	IDatEntService entService;
	
	/*由字段id查询自定义字段*/
	@Override
	public BaseFieldPo queryFieldByKey(String entId, String key) throws ServiceException {
		BaseFieldPo po=new BaseFieldPo();
		po.setKey(key);

		List<BaseFieldPo> list=null;
		try {
			list = this.query(entId, po, null);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}
	

   /* 根据查询条件查询自定义字段*/
	@Override
	public List<BaseFieldPo> query(String entId, BaseFieldPo po,
			PageInfo page) throws ServiceException {
		try{
			return fieldMongoDao.query(entId, po, page);
		}
		catch(DataAccessException e){
			e.printStackTrace();
			throw new ServiceException("查询出错");
		}
	}
	
	/*编辑自定义字段*/
	@Override
	public int goEdit(String entId, BaseFieldPo po) throws ServiceException {
		try {
			return fieldMongoDao.goEdit(entId,po);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	/*删除自定义字段（逻辑删除）*/
	@Override
	public int deleteField(String entId, String key) throws ServiceException {
		try {
			return fieldMongoDao.deleteField(entId,key);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	/*修改自定义字段状态*/
	@Override
	public int changeMode(String entId, String type, String key) throws ServiceException {
		try {
			return fieldMongoDao.changeMode(entId,type,key);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	/*自定义字段移动排序*/
	@Override
	public int sortField(String entId, String tempId, String ids) throws ServiceException {
		String[] sortKey=ids.trim().split(",");
		try {
			int[] sortNums=fieldMongoDao.getSortNums(entId, tempId);
			return fieldMongoDao.sortField(entId,tempId,sortKey,sortNums);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
		
	}

	@Override
	public Map<String, Map<String, List<BaseFieldPo>>> queryAllField()
			throws ServiceException {
		// TODO Auto-generated method stub
		List<DatEntInfoPo> entList = entService.queryAll();
		Map<String, Map<String,List<BaseFieldPo>>> map=new HashMap<String, Map<String,List<BaseFieldPo>>>();
		for (DatEntInfoPo ent : entList) {
			String entId = ent.getEntId();
			List<BaseFieldPo> list=this.query(entId, new BaseFieldPo(), null);
			 Map<String,List<BaseFieldPo>> typeMap=new HashMap<String,List<BaseFieldPo>>();
			for(BaseFieldPo t:list){
				 List<BaseFieldPo> typeList=typeMap.get(t.getTempType());
				  if(typeList==null||typeList.size()==0){
					  typeList=new ArrayList<BaseFieldPo>();
				  }
				 typeList.add(t);
				 typeMap.put(t.getTempType(),typeList);
			}
			map.put(entId, typeMap);
		}
		return map;
	}
}

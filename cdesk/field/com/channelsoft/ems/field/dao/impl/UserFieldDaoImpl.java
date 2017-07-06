package com.channelsoft.ems.field.dao.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.Assert;

import com.channelsoft.cri.constant.DateConstants;
import com.channelsoft.cri.mongo.BaseMongoTemplate;
import com.channelsoft.cri.vo.PageInfo;
import com.channelsoft.ems.field.constants.UserFieldStatusEnum;
import com.channelsoft.ems.field.dao.IUserFieldDao;
import com.channelsoft.ems.field.po.UserDefinedFiedPo;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class UserFieldDaoImpl  extends BaseMongoTemplate  implements IUserFieldDao {

	@Override
	public String getUserFieldTableName(String entId)
			throws DataAccessException {
		// TODO Auto-generated method stub
		String collectionName="entId_"+entId+"_userfield";
		return collectionName;
	}
	
	@Override
	public List<UserDefinedFiedPo> query(String entId,UserDefinedFiedPo po,PageInfo page)throws DataAccessException {
		// TODO Auto-generated method stub
		Assert.notNull(entId, "企业id不能为空！");
		String collectionName=getUserFieldTableName(entId);
		// TODO Auto-generated method stub
		Query query = new Query();
		if(StringUtils.isNotBlank(po.getKey())){
			query.addCriteria(Criteria.where("key").is(po.getKey()));
		}
		if(po.getIsDefault()){
			query.addCriteria(Criteria.where("isDefault").is(po.getIsDefault()));
		}
		if(po.isShow()){
			query.addCriteria(Criteria.where("isShow").is(po.isShow()));
		}
		if(StringUtils.isNotBlank(po.getStatus())){
			query.addCriteria(Criteria.where("status").is(po.getStatus()));
		}
		Sort sort=new Sort("sortNum");
		return this.findByPage(query.with(sort),collectionName,UserDefinedFiedPo.class, page);
	}

	@Override
	public int add(UserDefinedFiedPo po, String entId) throws DataAccessException {
		// TODO Auto-generated method stub
		/**
		 * 组装mongdb字段属性
		 */
		DBObject dbo = new BasicDBObject();
		dbo.put("key", po.getKey());
		dbo.put("isDefault", false);
		dbo.put("isShow", true);
		dbo.put("isReadonly", false);
		dbo.put("name", po.getName());
		dbo.put("type", po.getType());
		dbo.put("remark", po.getRemark());
		dbo.put("componentType", po.getComponentType());
		dbo.put("candidateValue", po.getCandidateValue());
		dbo.put("defaultValue", po.getDefaultValue());
		dbo.put("status", UserFieldStatusEnum.NORMAL.value);
		dbo.put("sortNum", Integer.valueOf(po.getKey().substring(5)).intValue());
		Assert.notNull(entId, "企业id不能为空！");
		Assert.notNull(dbo.get("key"), "key不能为空！");
		Assert.notNull(dbo.get("name"), "字段名称不能为空！");
		String collectionName=getUserFieldTableName(entId);
		dbo.put("createTime",DateConstants.DATE_FORMAT().format(new Date()));
		return this.intsertFromDbObject(dbo, collectionName);
	}

	@Override
	public int goEdit(String entId, UserDefinedFiedPo po) throws DataAccessException {
		DBObject query = new BasicDBObject();
		DBObject update = new BasicDBObject();
		DBObject set=new BasicDBObject();
		query.put("key", po.getKey());
		update.put("name", po.getName());
		update.put("remark", po.getRemark());
		update.put("candidateValue", po.getCandidateValue());
		set.put("$set", update);
		String collectionName=getUserFieldTableName(entId);
		return this.updateFromDbObject(query, set, collectionName);
	}

	@Override
	public int deleteField(String entId, String key) throws DataAccessException {
		String collectionName=getUserFieldTableName(entId);
		Query query=new Query();
		query.addCriteria(Criteria.where("key").is(key));
		return this.delete(query, collectionName);
	}

	@Override
	public int changeMode(String entId, String type, String key) throws DataAccessException {
		String collectionName=getUserFieldTableName(entId);
		DBObject query = new BasicDBObject();
		DBObject update = new BasicDBObject();
		DBObject set=new BasicDBObject();
		query.put("key", key);
		if(type.equals("active")){
			update.put("status", "1");
		}else{
			update.put("status", "0");
		}
		set.put("$set", update);
		return this.updateFromDbObject(query, set, collectionName);
	}

	@Override
	public int dropUserFieldTable(String entId) throws DataAccessException {
		// TODO Auto-generated method stub
		drop(this.getUserFieldTableName(entId));
		return 1;
	}

	@Override
	public int[] getSortNums(String entId) throws DataAccessException{
		UserDefinedFiedPo po=new UserDefinedFiedPo();
		po.setStatus("1");
		List<UserDefinedFiedPo> list=this.query(entId, po, null);
		int[] sortNums=new int[list.size()];
		for(int i=0;i<list.size();i++){
			sortNums[i]=Integer.valueOf(list.get(i).getSortNum());
		}
		return sortNums;
	}

	@Override
	public int sortUserField(String entId,String[] sortKey, int[] sortNums) throws DataAccessException {
		DBObject queryObject=null;
		DBObject update=null;
		DBObject set=null;
		int rtn=-1;
		String collection=this.getUserFieldTableName(entId);
		for(int i=0;i<sortKey.length;i++){
			queryObject=new BasicDBObject("key",sortKey[i]);
			update=new BasicDBObject("sortNum",sortNums[i]);
			set=new BasicDBObject("$set",update);
			rtn=this.updateFromDbObject(queryObject, set, collection);
			queryObject=null;
			update=null;
			set=null;
		}
		return rtn;
	}


}

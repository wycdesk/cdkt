package com.channelsoft.ems.field.dao.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.Assert;

import com.channelsoft.cri.constant.DateConstants;
import com.channelsoft.cri.mongo.BaseMongoTemplate;
import com.channelsoft.cri.vo.PageInfo;
import com.channelsoft.ems.field.constants.FieldStatusEnum;
import com.channelsoft.ems.field.dao.IFieldMongoDao;
import com.channelsoft.ems.field.po.BaseFieldPo;
import com.channelsoft.ems.template.constants.TemplateTypeEnum;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class FieldMongoDaoImpl extends BaseMongoTemplate implements IFieldMongoDao{

	/*添加自定义字段*/
	@Override
	public int add(BaseFieldPo po, String entId) throws DataAccessException {
		/**
		 * 组装mongdb字段属性
		 */
		DBObject dbo = new BasicDBObject();
		dbo.put("tempId", po.getTempId());
		dbo.put("tempType", TemplateTypeEnum.WORK.value);
		dbo.put("key", po.getKey());
		dbo.put("name", po.getName());
		dbo.put("componentType", po.getComponentType());		
		dbo.put("isShow", true);
		dbo.put("editable", true);		
		dbo.put("isDefault", false);
		dbo.put("statusChange", true);		
		dbo.put("status", FieldStatusEnum.NORMAL.value);
		dbo.put("remark", po.getRemark());		
	    dbo.put("candidateValue", po.getCandidateValue());
		dbo.put("sortNum", Integer.valueOf(po.getKey().substring(5)).intValue());
		dbo.put("createTime",DateConstants.DATE_FORMAT().format(new Date()));
		
		String collectionName=getFieldTableName(entId);
		
		return this.intsertFromDbObject(dbo, collectionName);
	}
	
	/*获取字段表*/
	@Override
	public String getFieldTableName(String entId)
			throws DataAccessException {
		// TODO Auto-generated method stub
		String collectionName="entId_"+entId+"_field";
		return collectionName;
	}
	
	/*由查询条件查询自定义字段*/
	@Override
	public List<BaseFieldPo> query(String entId,BaseFieldPo po,PageInfo page)throws DataAccessException {
		Assert.notNull(entId, "企业id不能为空！");
		String collectionName=getFieldTableName(entId);

		Query query = new Query();
		if(StringUtils.isNotBlank(po.getTempId())){
			query.addCriteria(Criteria.where("tempId").is(po.getTempId()));
		}
		if(StringUtils.isNotBlank(po.getTempType())){
			query.addCriteria(Criteria.where("tempType").is(po.getTempType()));
		}	
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
		return this.findByPage(query.with(sort),collectionName,BaseFieldPo.class, page);
	}
	
	/*编辑自定义字段*/
	@Override
	public int goEdit(String entId, BaseFieldPo po) throws DataAccessException {
		DBObject query = new BasicDBObject();
		DBObject update = new BasicDBObject();
		DBObject set=new BasicDBObject();
		query.put("key", po.getKey());
		update.put("name", po.getName());
		update.put("remark", po.getRemark());
		update.put("candidateValue", po.getCandidateValue());
		set.put("$set", update);
		String collectionName=getFieldTableName(entId);
		return this.updateFromDbObject(query, set, collectionName);
	}
	
	/*删除自定义字段（逻辑删除）*/
	@Override
	public int deleteField(String entId, String key) throws DataAccessException {
		String collectionName=getFieldTableName(entId);
		Query query=new Query();
		query.addCriteria(Criteria.where("key").is(key));
        Update update = new Update();
        update.set("status", FieldStatusEnum.DELETE.value);
        
		return this.update(query, update, collectionName);
	}
	
	/*修改自定义字段状态*/
	@Override
	public int changeMode(String entId, String type, String key) throws DataAccessException {
		String collectionName=getFieldTableName(entId);
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
	
	/*获取全部启用状态自定义字段的排序值*/
	@Override
	public int[] getSortNums(String entId, String tempId) throws DataAccessException{
		BaseFieldPo po=new BaseFieldPo();
		po.setStatus("1");
		po.setTempId(tempId);
		List<BaseFieldPo> list=this.query(entId, po, null);
		int[] sortNums=new int[list.size()];
		for(int i=0;i<list.size();i++){
			sortNums[i]=Integer.valueOf(list.get(i).getSortNum());
		}
		return sortNums;
	}
	
	/*排序某一模板内所有启用的字段*/
	@Override
	public int sortField(String entId, String tempId, String[] sortKey, int[] sortNums) throws DataAccessException {
		DBObject queryObject=null;
		DBObject update=null;
		DBObject set=null;
		int rtn=-1;
		String collection=this.getFieldTableName(entId);
		for(int i=0;i<sortKey.length;i++){
			queryObject=new BasicDBObject();
			queryObject.put("key", sortKey[i]);
			queryObject.put("tempId", tempId);
			
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

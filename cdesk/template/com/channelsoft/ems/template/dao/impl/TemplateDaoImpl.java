package com.channelsoft.ems.template.dao.impl;

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
import com.channelsoft.cri.util.DBObjectUtils;
import com.channelsoft.cri.vo.PageInfo;
import com.channelsoft.ems.field.constants.FieldStatusEnum;
import com.channelsoft.ems.field.po.BaseFieldPo;
import com.channelsoft.ems.template.constants.TemplateStatusEnum;
import com.channelsoft.ems.template.constants.TemplateTypeEnum;
import com.channelsoft.ems.template.dao.ITemplateDao;
import com.channelsoft.ems.template.po.TemplatePo;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class TemplateDaoImpl extends BaseMongoTemplate implements ITemplateDao{

	/*获取模板表*/
	@Override
	public String getTemplateTableName(String entId)
			throws DataAccessException {
		// TODO Auto-generated method stub
		String collectionName="entId_"+entId+"_template";
		return collectionName;
	}
	
	/*获取字段表*/
	@Override
	public String getFieldTableName(String entId)
			throws DataAccessException {
		// TODO Auto-generated method stub
		String collectionName="entId_"+entId+"_field";
		return collectionName;
	}
	
	/*按条件查询模板*/
	@Override
	public List<TemplatePo> query(String entId,TemplatePo po,PageInfo page)throws DataAccessException {
		// TODO Auto-generated method stub
		Assert.notNull(entId, "企业id不能为空！");
		String collectionName=getTemplateTableName(entId);
		// TODO Auto-generated method stub
		Query query = new Query();
		if(StringUtils.isNotBlank(po.getTempId())){
			query.addCriteria(Criteria.where("tempId").is(po.getTempId()));
		}
		if(po.isDefault()){
			query.addCriteria(Criteria.where("isDefault").is(po.isDefault()));
		}
		if(po.isEditable()){
			query.addCriteria(Criteria.where("editable").is(po.isEditable()));
		}
		if(StringUtils.isNotBlank(po.getStatus())){
			query.addCriteria(Criteria.where("status").is(po.getStatus()));
		}
		if(StringUtils.isNotBlank(po.getTempType())){
			query.addCriteria(Criteria.where("tempType").is(po.getTempType()));
		}
		Sort sort=new Sort("sortNum");
		return this.findByPage(query.with(sort),collectionName,TemplatePo.class, page);
	}
	
	/*添加工单模板*/
	@Override
	public int addTemp(TemplatePo po, String entId) throws DataAccessException {
		// TODO Auto-generated method stub
		/**
		 * 组装mongdb字段属性
		 */
		DBObject dbo = new BasicDBObject();
		dbo.put("tempId", po.getTempId());
		dbo.put("tempName", po.getTempName());
		dbo.put("tempType", TemplateTypeEnum.WORK.value);
		dbo.put("status", TemplateStatusEnum.NORMAL.value);	
		dbo.put("isDefault", false);
		dbo.put("editable", true);
		dbo.put("statusChange", true);		
		dbo.put("sortNum", Integer.valueOf(po.getTempId()).intValue());
		
		Assert.notNull(entId, "企业id不能为空！");
		Assert.notNull(dbo.get("tempId"), "Id不能为空！");
		Assert.notNull(dbo.get("tempName"), "字段名称不能为空！");
		
		String collectionName=getTemplateTableName(entId);
		dbo.put("createTime",DateConstants.DATE_FORMAT().format(new Date()));
		return this.intsertFromDbObject(dbo, collectionName);
	}

	/*删除模板（逻辑删除）*/
	@Override
	public int deleteTemplate(String entId, String key) throws DataAccessException {
		String collectionName=getTemplateTableName(entId);	
		DBObject queryObject = new BasicDBObject();
		queryObject.put("tempId", key);	
		DBObject updateObject = new BasicDBObject("$set",new BasicDBObject().append("status", TemplateStatusEnum.DELETE.value));

		return this.updateFromDbObject(queryObject, updateObject, collectionName);
	}
	
	/*模板启停用*/
	@Override
	public int changeMode(String entId, String type, String key) throws DataAccessException {
		String collectionName=getTemplateTableName(entId);
		DBObject query = new BasicDBObject();
		DBObject update = new BasicDBObject();
		DBObject set=new BasicDBObject();
		query.put("tempId", key);
		if(type.equals("active")){
			update.put("status", TemplateStatusEnum.NORMAL.value);
		}else{
			update.put("status", TemplateStatusEnum.STOPPED.value);
		}
		set.put("$set", update);
		return this.updateFromDbObject(query, set, collectionName);
	}
	
	/*获取全部启用模板的排序值*/
	@Override
	public int[] getSortNums(String entId) throws DataAccessException{
		TemplatePo po=new TemplatePo();
		po.setStatus(TemplateStatusEnum.NORMAL.value);
		List<TemplatePo> list=this.query(entId, po, null);
		int[] sortNums=new int[list.size()];
		for(int i=0;i<list.size();i++){
			sortNums[i]=Integer.valueOf(list.get(i).getSortNum());
		}
		return sortNums;
	}
	
	/*排序所有启用的模板*/
	@Override
	public int sortTemplate(String entId,String[] sortKey, int[] sortNums) throws DataAccessException {
		DBObject queryObject=null;
		DBObject update=null;
		DBObject set=null;
		int rtn=-1;
		String collection=this.getTemplateTableName(entId);
		for(int i=0;i<sortKey.length;i++){
			queryObject=new BasicDBObject("tempId",sortKey[i]);
			update=new BasicDBObject("sortNum",sortNums[i]);
			set=new BasicDBObject("$set",update);
			rtn=this.updateFromDbObject(queryObject, set, collection);
			queryObject=null;
			update=null;
			set=null;
		}
		return rtn;
	}
		
	/*添加工单默认字段*/
	@Override
	public void addDefaultField(List<BaseFieldPo> list, String entId) throws DataAccessException {
		/**
		 * 组装mongdb字段属性
		 */
		String collectionName=getFieldTableName(entId);
		for(int i=0;i<list.size();i++){				
			DBObject dbo=DBObjectUtils.getDBObject(list.get(i));
			dbo.put("tempType", TemplateTypeEnum.WORK.value);
			dbo.put("status", FieldStatusEnum.NORMAL.value);
			dbo.put("editable", false);
			dbo.put("isDefault", true);
			dbo.put("isShow", true);
			dbo.put("statusChange", false);		
			dbo.put("sortNum", Integer.valueOf(dbo.get("sortNum").toString()).intValue());
			dbo.put("createTime", DateConstants.DATE_FORMAT().format(new Date()));		
			
			this.intsertFromDbObject(dbo, collectionName);
		}					
	}
	
	/*按模板Id删除字段（逻辑删除）*/
	@Override
	public int deleteFieldByTempId(String entId, String key) throws DataAccessException {
		String collectionName=getFieldTableName(entId);			
		Query query = new Query();
		query.addCriteria(Criteria.where("tempId").is(key));	
		Update update = new Update();
		update.set("status", FieldStatusEnum.DELETE.value);
		
		return this.update(query, update, collectionName);
	}
	
	/*按条件查询字段*/
	@Override
	public List<BaseFieldPo> queryFields(String entId,BaseFieldPo po,PageInfo page)throws DataAccessException {
		Assert.notNull(entId, "企业id不能为空！");
		
		String collectionName=getFieldTableName(entId);
		Query query = new Query();
		if(StringUtils.isNotBlank(po.getTempId())){
			query.addCriteria(Criteria.where("tempId").is(po.getTempId()));
		}
		if(StringUtils.isNotBlank(po.getStatus())){
			query.addCriteria(Criteria.where("status").is(po.getStatus()));
		}

		Sort sort=new Sort("sortNum");
		return this.findByPage(query.with(sort),collectionName,BaseFieldPo.class, page);
	}
	
	/*编辑模板信息（标题）*/
	@Override
	public int goEdit(String entId, TemplatePo po) throws DataAccessException {
		DBObject query = new BasicDBObject();
		DBObject update = new BasicDBObject();
		DBObject set=new BasicDBObject();
		query.put("tempId", po.getTempId());
		update.put("tempName", po.getTempName());
		set.put("$set", update);
		String collectionName=getTemplateTableName(entId);
		return this.updateFromDbObject(query, set, collectionName);
	}
	
	
	/*添加工单默认模板*/
	@Override
	public int addDefaultTemp(TemplatePo po, String entId) throws DataAccessException {
		
		DBObject dbo = new BasicDBObject();
		dbo.put("tempId", po.getTempId());
		dbo.put("tempName", "默认工单自定义分类");
		dbo.put("tempType", TemplateTypeEnum.WORK.value);
		dbo.put("status", TemplateStatusEnum.NORMAL.value);	
		dbo.put("isDefault", true);
		dbo.put("editable", false);
		dbo.put("statusChange", false);		
		dbo.put("sortNum", Integer.valueOf(po.getTempId()).intValue());
		
		Assert.notNull(entId, "企业id不能为空！");
		Assert.notNull(dbo.get("tempId"), "模板Id不能为空！");
		
		String collectionName=getTemplateTableName(entId);
		dbo.put("createTime",DateConstants.DATE_FORMAT().format(new Date()));
		return this.intsertFromDbObject(dbo, collectionName);
	}

}

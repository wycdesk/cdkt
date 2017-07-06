package com.channelsoft.ems.log.dao.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.Assert;

import com.channelsoft.cri.constant.DateConstants;
import com.channelsoft.cri.mongo.BaseMongoTemplate;
import com.channelsoft.cri.vo.PageInfo;
import com.channelsoft.ems.log.dao.ILogMongoDao;
import com.channelsoft.ems.log.po.CfgUserOperateLogPo;
import com.mongodb.DBObject;
import com.mongodb.MongoException;

public class LogMongoDaoImpl extends BaseMongoTemplate  implements ILogMongoDao {


	@Override
	public int add(DBObject dbo, String entId) throws MongoException {
		// TODO Auto-generated method stub
		Assert.notNull(entId, "企业id不能为空！");
		Assert.notNull(dbo.get("userId"), "操作用户编号不能为空！");
		Assert.notNull(dbo.get("loginName"), "操作用户账号不能为空！");
		String collectionName="entId_"+entId+"_userlog";
		dbo.put("optTime",DateConstants.DATE_FORMAT().format(new Date()));
		return this.intsertFromDbObject(dbo, collectionName);
	}

	@Override
	public List<CfgUserOperateLogPo> query(CfgUserOperateLogPo po,PageInfo pageInfo,String entId) throws MongoException {
		Assert.notNull(entId, "企业id不能为空！");
		String collectionName="entId_"+entId+"_userlog";
		// TODO Auto-generated method stub
		Query query = new Query();
		if(StringUtils.isNotBlank(po.getBusinessType())){
			query.addCriteria(Criteria.where("businessType").is(po.getBusinessType()));
		}
		if(StringUtils.isNotBlank(po.getOperation())){
			query.addCriteria(Criteria.where("operation").is(po.getOperation()));
		}
		query.with(new Sort(new Sort.Order(Sort.Direction.DESC,"optTime")));
		
		return this.findByPage(query, collectionName, CfgUserOperateLogPo.class,pageInfo);
	}

}

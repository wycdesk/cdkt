package com.channelsoft.ems.communicate.dao.impl;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.Assert;

import com.channelsoft.cri.mongo.BaseMongoTemplate;
import com.channelsoft.cri.vo.PageInfo;
import com.channelsoft.ems.communicate.dao.IHistoryDao;
import com.channelsoft.ems.communicate.utils.QueryUtils;
import com.mongodb.DBObject;

public class HistoryDaoImpl extends BaseMongoTemplate implements IHistoryDao {

	@Override
	public int add(DBObject dbo, String entId) throws DataAccessException {
		Assert.notNull(entId, "企业id不能为空！");

		return this.intsertFromDbObject(dbo, getUserTable(entId));
	}

	@Override
	public String getUserTable(String entId) throws DataAccessException {

		return "entId_"+entId+"_communicate_history";

	}
	
	@Override
	public List<DBObject> queryCommHistory(DBObject queryObject,
			PageInfo pageInfo) {

		Query query = new Query();

		if (queryObject.get("userType") != null) {
			query.addCriteria(Criteria.where("userType").is(
					queryObject.get("userType")));
		}
		 

		// 按创建时间降序排序
		query.with(new Sort(Direction.DESC, "createTime"));
		return this.findByPage(query, getUserTable(queryObject.get("entId")
				+ ""), DBObject.class, pageInfo);
	}
}

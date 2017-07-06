package com.channelsoft.ems.help.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.channelsoft.cri.mongo.BaseMongoTemplate;
import com.channelsoft.ems.help.dao.IPartitionDao;
import com.channelsoft.ems.help.po.PartitionPo;
import com.channelsoft.ems.help.po.PartitionSubPo;
import com.channelsoft.ems.user.dao.IUserDao;
import com.mongodb.DBObject;

public class PartitionDaoImpl extends BaseMongoTemplate implements IPartitionDao{

	@Autowired
	IUserDao userDao;
	
	@Override
	public List<PartitionPo> getPartition(String entId, PartitionPo po, boolean isAll) 
			throws DataAccessException {
		/*StringBuffer sql=new StringBuffer();
		sql.append("SELECT * FROM ").append(this.getEntDbName(entId))
		.append(".DAT_PARTITION where 1=1 ");
		if(!isAll){
			
		}
		return this.queryForList(sql.toString(), PartitionPo.class);*/
		return null;
	}

	@Override
	public List<PartitionSubPo> getSubClass(String entId,PartitionSubPo subClass) throws DataAccessException {
		StringBuffer sql=new StringBuffer();
		/*sql.append("SELECT * FROM ").append(this.getEntDbName(entId))
		.append(".DAT_PARTITION_SUB where 1=1 ");
		if(StringUtils.isNotBlank(subClass.getPartitionId())){
			sql.append("AND PARTITION_ID = '"+subClass.getPartitionId()+"'");
		}
		return this.queryForList(sql.toString(), PartitionSubPo.class);*/
		return null;
	}

	@Override
	public int addPartition(String entId, PartitionPo po) throws DataAccessException {
		StringBuffer sql=new StringBuffer();
		/*sql.append("INSERT INTO ").append(this.getEntDbName(entId))
		.append(".DAT_PARTITION VALUES(?,?,?,?)");
		Object[] params=new Object[]{po.getId(),po.getTitle(),po.getDescription(),Integer.valueOf(po.getShowNum())};
		int[] types=new int[]{Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.INTEGER};
		return this.update(sql.toString(), params, types);*/
		return 0;
	}

	@Override
	public int addPartitionSub(String entId, PartitionSubPo po) throws DataAccessException {
		/*StringBuffer sql=new StringBuffer();
		sql.append("INSERT INTO ").append(this.getEntDbName(entId))
		.append(".DAT_PARTITION_SUB VALUES(?,?,?,?,?,?)");
		Object[] params=new Object[]{po.getId(),po.getPartitionId(),po.getTitle(),po.getDescription(),
				po.getUserPermission(),po.getOrganization()};
		int[] types=new int[]{Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR};
		return this.update(sql.toString(), params, types);*/
		return 0;
	}

	@Override
	public int editPartition(String entId, PartitionPo po) throws DataAccessException {
		StringBuffer sql=new StringBuffer();
		/*sql.append("UPDATE ").append(this.getEntDbName(entId))
		.append(".DAT_PARTITION SET TITLE='"+po.getTitle())
		.append("',SHOW_NUM="+po.getShowNum());
		if(StringUtils.isNotBlank(po.getDescription())){
			sql.append(",DESCRIPTION='"+po.getDescription()+"'");
		}
		sql.append(" WHERE ID='"+po.getId()+"'");
		return this.update(sql.toString());*/
		return 0;
	}

	@Override
	public int editPartitionSub(String entId, PartitionSubPo po) throws DataAccessException {
		StringBuffer sql=new StringBuffer();
		/*sql.append("UPDATE ").append(this.getEntDbName(entId))
		.append(".DAT_PARTITION_SUB SET PARTITION_ID='"+po.getPartitionId())
		.append("',TITLE='"+po.getTitle()).append("',USER_PERMISSION='"+po.getUserPermission()+"'");
		if(StringUtils.isNotBlank(po.getDescription())){
			sql.append(",DESCRIPTION='"+po.getDescription()+"'");
		}
		if(StringUtils.isNotBlank(po.getOrganization())){
			sql.append(",ORGANIZATION='"+po.getOrganization()+"'");
		}
		sql.append(" WHERE ID='"+po.getId()+"'");
		return this.update(sql.toString());*/
		return 0;
	}

	@Override
	public int deletePartition(String entId, PartitionPo po) throws DataAccessException {
		/*StringBuffer sql=new StringBuffer();
		sql.append("DELETE FROM ").append(this.getEntDbName(entId))
		.append(".DAT_PARTITION WHERE 1=1");
		if(StringUtils.isNotBlank(po.getId())){
			sql.append(" AND ID='"+po.getId()+"'");
		}
		return this.update(sql.toString());*/
		return 0;
	}

	@Override
	public int deletePartitionSub(String entId, PartitionSubPo po) throws DataAccessException {
		/*StringBuffer sql=new StringBuffer();
		sql.append("DELETE FROM ").append(this.getEntDbName(entId))
		.append(".DAT_PARTITION_SUB WHERE 1=1");
		if(StringUtils.isNotBlank(po.getId())){
			sql.append(" AND ID='"+po.getId()+"'");
		}
		return this.update(sql.toString());*/
		return 0;
	}
	
	@Override
	public String getPartitionId() throws DataAccessException {
		//return this.getSequenceStr("SEQ_DAT_PARTITION");
		return null;
	}

	@Override
	public String getPartitionSubId() throws DataAccessException {
		//return this.getSequenceStr("SEQ_DAT_PARTITION_SUB");
		return null;
	}

	@Override
	public int goPublish(String entId, DBObject dbo) throws DataAccessException {
		String collection=this.getPublishCollection(entId);
		return this.intsertFromDbObject(dbo, collection);
	}
	
	public String getPublishCollection(String entId){
		return "entId_"+entId+"_publish";
	}

	@Override
	public String getDocId() throws DataAccessException {
		return userDao.getDocId("SEQ_DAT_ENT_DOC");
	}

    /*编辑文档*/
	@Override
	public int goEdit(String entId, DBObject dbo) throws DataAccessException {
		String collection=this.getPublishCollection(entId);
		Query query=new Query();
		query.addCriteria(Criteria.where("docId").is(dbo.get("docId")));
		Update update=new Update();
		for(String key:dbo.keySet()){
			update.set(key, dbo.get(key));
		}
		
		this.update(query, update, collection);
		return 1;
	}



}

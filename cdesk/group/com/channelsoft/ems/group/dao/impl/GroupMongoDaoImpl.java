package com.channelsoft.ems.group.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.mapreduce.GroupBy;
import org.springframework.data.mongodb.core.mapreduce.GroupByResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.channelsoft.cri.mongo.BaseMongoTemplate;
import com.channelsoft.cri.util.DBObjectUtils;
import com.channelsoft.cri.vo.PageInfo;
import com.channelsoft.ems.group.dao.IGroupMongoDao;
import com.channelsoft.ems.group.po.AgentPo;
import com.channelsoft.ems.group.po.AgentReturnPo;
import com.channelsoft.ems.group.po.GroupPo;
import com.channelsoft.ems.group.po.GroupReturnPo;
import com.channelsoft.ems.user.po.DatEntUserPo;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class GroupMongoDaoImpl extends BaseMongoTemplate implements IGroupMongoDao {
	@Autowired
	GroupDaoImpl groupDao;
	
	@Override
	public List<GroupReturnPo> getGroups(String entId,PageInfo pageInfo) throws DataAccessException {
		String collection=this.getGroupTable(entId);
		if(pageInfo==null){
			return this.findList(null, collection, GroupReturnPo.class);
		}else{
			return this.findByPage(null, collection,  GroupReturnPo.class, pageInfo);
		}
	}

	@Override
	public int addUserGroup(String entId, GroupPo po) throws DataAccessException {
		String collection=getGroupTable(entId);
		Date time=new Date();
		String createTime=time.toLocaleString();
		po.setCreateTime(createTime);
		DBObject dbo=DBObjectUtils.getDBObject(po);
		return this.intsertFromDbObject(dbo, collection);
	}
	
	@Override
	public int addAgents(String entId, List<AgentPo> agentPos) throws DataAccessException {
		String collections=getGroupAgentTable(entId);
		List<DBObject> list=new ArrayList<DBObject>();
		Date time=new Date();
		String createTime=time.toLocaleString();
		for(int i=0;i<agentPos.size();i++){
			AgentPo poI=agentPos.get(i);
			poI.setCreateTime(createTime);
			DBObject dbo=DBObjectUtils.getDBObject(poI);
			list.add(dbo);
		}
		return this.intsertFromDbObject(list, collections);
	}
	
	@Override
	public List<AgentReturnPo> getMembers(String entId, String groupId) throws DataAccessException {
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT * FROM ").append(groupDao.getEntDbName(entId))
			.append(".DAT_AGENT_ASSIGN ").append(" WHERE 1=1")
			.append(" AND GROUP_ID='"+groupId+"'");
		List<AgentReturnPo> list=groupDao.queryForList(sql.toString(),AgentReturnPo.class);
		String userCollection=this.getUserTable(entId);
		Query user=new Query();
		if(list==null||list.size()<=0){
			return list;
		}
		String[] array=new String[list.size()];
		for(int i=0;i<list.size();i++){
			array[i]=list.get(i).getUserId();
		}
		user.addCriteria(Criteria.where("userId").in(array)).addCriteria(new Criteria()
				.orOperator(Criteria.where("userType").is("2"),Criteria.where("userType").is("3")));
		user.addCriteria(Criteria.where("userStatus").is("1"));
		List<AgentReturnPo> list1=this.findList(user, userCollection, AgentReturnPo.class);
		if(list1==null||list1.size()<=0){
			return list1;
		}
		for(int i=0;i<list.size();i++){
			int exist=0;
			for(int j=0;j<list1.size();j++){
				if(list1.get(j).getLoginName().equals(list.get(i).getAgentId())){
					list.get(i).setLoginName(list1.get(j).getLoginName());
					list.get(i).setNickName(list1.get(j).getNickName());
					list1.remove(j);
					exist=1;
					break;
				}else{
					continue;
				}
			}
			if(exist==0){
				list.remove(i);
				i--;
			}
		}
		return list;
	}
	
	
	@Override
	public List<AgentReturnPo> getAgents(String entId,String groupId) throws DataAccessException {
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT * FROM ").append(groupDao.getEntDbName(entId))
			.append(".DAT_AGENT_ASSIGN ").append(" WHERE 1=1")
			.append(" AND GROUP_ID='"+groupId+"'");
		List<AgentReturnPo> agentList=groupDao.queryForList(sql.toString(),AgentReturnPo.class);
		Query user=new Query();
		String userCollection=this.getUserTable(entId);
		String args[]=new String[2];
		args[0]="2";
		args[1]="3";
		user.addCriteria(Criteria.where("userType").in(args));
		user.addCriteria(Criteria.where("userStatus").is("1"));
		List<AgentReturnPo> userList=this.findList(user, userCollection, AgentReturnPo.class);
		if(userList!=null&&userList.size()>0){
			if(agentList!=null&&agentList.size()>0){
				for(int i=0;i<userList.size();i++){
					for(int j=0;j<agentList.size();j++){
						if(userList.get(i).getUserId().equals(agentList.get(j).getUserId())){
							userList.get(i).setAgentId(agentList.get(j).getAgentId());
							userList.get(i).setGroupId(groupId);
							userList.get(i).setId(agentList.get(j).getId());
							agentList.remove(j);
							break;
						}else{
							userList.get(i).setId("-1");
						}
					}
					if(agentList.size()<=0)
						break;
				}
			}
		}
		return userList;
		
	}
	
	@Override
	public String getGroupName(String entId, String groupId) throws DataAccessException {
		Query query = new Query();
		query.addCriteria(Criteria.where("groupId").is(groupId));
		return this.findOne(query, this.getGroupTable(entId), GroupReturnPo.class).getGroupName();
	}
	
	@Override
	public int deleteAgents(String entId, String groupId, String agents,boolean delAll) throws DataAccessException {
		StringBuffer sql=new StringBuffer();
		Query query=new Query();
		String collection=this.getGroupAgentTable(entId);
		query.addCriteria(Criteria.where("groupId").is(groupId));
		if(delAll==true){
			return this.delete(query, collection);
		}else{
			if(StringUtils.isNotBlank(agents)){
				String[] args=agents.split(",");
				query.addCriteria(Criteria.where("agentId").in(args));
				return this.delete(query, collection);
			}
		}
		return -1;
	}
	
	@Override
	public int updateUserGroup(String entId, GroupPo po) throws DataAccessException {
		DBObject update=new BasicDBObject();
		DBObject query=new BasicDBObject();
		DBObject set=new BasicDBObject();
		String collection=this.getGroupTable(entId);
		String time=new Date().toLocaleString();
		query.put("groupId", po.getGroupId());
		update.put("groupName", po.getGroupName());
		update.put("updatorId", po.getUpdatorId());
		update.put("updatorName", po.getUpdatorName());
		update.put("updateTime", time);
		set.put("$set", update);
		return this.updateFromDbObject(query, set, collection);
		
	}
	
	@Override
	public int deleteUserGroup(String entId, String groupId) throws DataAccessException {
		Query query=new Query();
		String collection=this.getGroupTable(entId);
		query.addCriteria(Criteria.where("groupId").is(groupId));
		return this.delete(query, collection);
	}
	
	@Override
	public List<DatEntUserPo> getDetailMembers(String entId, String groupId,PageInfo pageInfo,String userStatus)
			throws DataAccessException {
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT aa.LOGIN_NAME,aa.USER_ID,MAX(aa.LOGIN_TIME) LOGIN_TIME FROM")
			.append(" (SELECT agent.AGENT_ID LOGIN_NAME,agent.USER_ID,login.LOGIN_TIME FROM ")
			.append(groupDao.getEntDbName(entId))
			.append(".DAT_AGENT_ASSIGN agent LEFT JOIN ").append(groupDao.getEntDbName(entId))
			.append(".DAT_USER_LOGIN login ")
			.append(" ON agent.USER_ID=login.USER_ID")
			.append(" WHERE 1=1").append(" AND agent.GROUP_ID='"+groupId+"') aa GROUP BY aa.LOGIN_NAME");
		List<DatEntUserPo> userList1=groupDao.queryByPage(sql.toString(),pageInfo,DatEntUserPo.class);
		String userCollection=this.getUserTable(entId);
		Query user=new Query();
		String args[]=null;
		List<DatEntUserPo> userList=null;
		if(userList1!=null&&userList1.size()>0){
			args=new String[userList1.size()];
			for(int i=0;i<userList1.size();i++){
				args[i]=userList1.get(i).getUserId();
			}
			user.addCriteria(Criteria.where("userId").in(args)).addCriteria(new Criteria()
					.orOperator(Criteria.where("userType").is("2"),Criteria.where("userType").is("3")));
			user.addCriteria(Criteria.where("userStatus").is("1"));
			if(StringUtils.isNotBlank(userStatus)){
				user.addCriteria(Criteria.where("userStatus").is(userStatus));
			}
			user.with(new Sort(Direction.DESC, "createTime"));
			userList=this.findList(user, userCollection, DatEntUserPo.class);
		}else{
			return userList1;
		}
		if(userList==null||userList.size()<=0){
			return userList;
		}
		/*GroupBy gp=GroupBy.key("loginName");
		gp.initialDocument("{loginTime:\"\"}");
		gp.reduceFunction("function(a,b){ b.loginTime=a.optTime;}");
		GroupByResults<DatEntUserPo> list=this.getMongoTemplate()
				.group(Criteria.where("loginName").in(args), loginCollection, gp, DatEntUserPo.class);
		Iterator<DatEntUserPo> iterator=list.iterator();
		List<DatEntUserPo> results=new ArrayList<DatEntUserPo>();
		while(iterator.hasNext()){
			results.add(iterator.next());
		}*/
		for(int i=0;i<userList.size();i++){
			for(int j=0;j<userList1.size();j++){
				if(userList.get(i).getLoginName().equals(userList1.get(j).getLoginName())){
					userList.get(i).setLoginTime(userList1.get(j).getLoginTime());
					userList1.remove(j);
					break;
				}
			}
			if(userList1.size()<=0)
				break;
		}
		return userList;
	}
	
	
	
	private String getUserLoginTable(String entId) {
		return "entId_"+entId+"_userlog";
	}

	public String getGroupTable(String entId){
		return "entId_"+entId+"_group";
	}
	public String getGroupAgentTable(String entId){
		return "entId_"+entId+"_group_agent";
	}
	public String getUserTable(String entId){
		return "entId_"+entId+"_user";
	}
}

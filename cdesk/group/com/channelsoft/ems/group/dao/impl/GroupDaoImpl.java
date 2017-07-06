package com.channelsoft.ems.group.dao.impl;

import java.sql.Types;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;

import com.channelsoft.cri.dao.jdbc.BaseJdbcMysqlDao;
import com.channelsoft.cri.vo.PageInfo;
import com.channelsoft.ems.group.dao.IGroupDao;
import com.channelsoft.ems.group.po.AgentReturnPo;
import com.channelsoft.ems.group.po.AgentPo;
import com.channelsoft.ems.group.po.GroupReturnPo;
import com.channelsoft.ems.group.po.GroupPo;
import com.channelsoft.ems.user.po.DatEntUserPo;

public class GroupDaoImpl extends BaseJdbcMysqlDao implements IGroupDao {

	@Override
	public String getGroupId() throws DataAccessException {
		return this.getSequenceStr("SEQ_DAT_USER_GROUP");
	}
	
	@Override
	public List<GroupPo> queryUserGroup(String entId,GroupPo po) throws DataAccessException {
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT * FROM ").append(getEntDbName(entId)).append(".DAT_USER_GROUP WHERE STATUS=1");
		if(po!=null)
			if(StringUtils.isNotBlank(po.getGroupId())){
				sql.append(" and GROUP_ID='").append(po.getGroupId()).append("'");
			}
		
		return this.queryForList(sql.toString(),GroupPo.class);
	}

	@Override
	public List<AgentPo> queryGroupAgent(String entId, String groupId) throws DataAccessException {
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT A.*,U.USER_ID,U.USER_NAME, U.LOGIN_NAME FROM ").append(getEntDbName(entId)).append(".DAT_AGENT_ASSIGN  A inner join ").append(getEntDbName(entId)).append(".DAT_ENT_USER U  ON A.AGENT_ID=U.LOGIN_NAME WHERE 1=1");
		if(StringUtils.isNotBlank(groupId))
			sql.append(" AND A.GROUP_ID='"+groupId+"'");
		return this.queryForList(sql.toString(),AgentPo.class);
	}

	@Override
	public List<AgentPo> queryGroupAgentForMongo(String entId, String groupId)
			throws DataAccessException {
		// TODO Auto-generated method stub
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT A.* FROM ").append(getEntDbName(entId)).append(".DAT_AGENT_ASSIGN  A WHERE 1=1");
		if(StringUtils.isNotBlank(groupId))
			sql.append(" AND A.GROUP_ID='"+groupId+"'");
		return this.queryForList(sql.toString(),AgentPo.class);
	}
	@Override
	public int deleteUserGroup(String entId, String groupId) throws DataAccessException {
		StringBuffer sql=new StringBuffer();
		sql.append("UPDATE ").append(getEntDbName(entId))
			.append(".DAT_USER_GROUP SET STATUS='0' WHERE GROUP_ID='").append(groupId+"'");
		
		return this.update(sql.toString());
	}
	
	@Override
	public int addUserGroup(String entId, GroupPo po) throws DataAccessException {
		StringBuffer sql=new StringBuffer();
		sql.append("INSERT INTO ").append(getEntDbName(entId))
			.append(".DAT_USER_GROUP(GROUP_ID,GROUP_NAME,GROUP_TYPE,CREATOR_ID"
					+ ",CREATOR_NAME,CREATE_TIME,STATUS)")
			.append(" VALUES(?,?,?,?,?,NOW(),'1')");
		Object args[]={po.getGroupId(),po.getGroupName(),po.getGroupType(),
				po.getCreatorId(),po.getCreatorName()};
		int argTypes[]=new int[args.length];
		for(int i=0;i<args.length;i++){
			argTypes[i]=Types.VARCHAR;
		}
		return this.update(sql.toString(), args, argTypes);
	}

	@Override
	public int updateUserGroup(String entId, GroupPo po) throws DataAccessException {
		StringBuffer sql=new StringBuffer();
		sql.append("UPDATE ").append(getEntDbName(entId))
			.append(".DAT_USER_GROUP SET GROUP_NAME=?,UPDATOR_ID=?,UPDATOR_NAME=?,UPDATE_TIME=NOW()")
			.append(" WHERE GROUP_ID='").append(po.getGroupId()+"'");
		Object args[]={po.getGroupName(),po.getUpdatorId(),po.getUpdatorName()};
		int argTypes[]=new int[args.length];
		for(int i=0;i<args.length;i++){
			argTypes[i]=Types.VARCHAR;
		}
		return this.update(sql.toString(), args, argTypes);
	}

	@Override
	public List<GroupReturnPo> getGroups(String entId,PageInfo pageInfo) throws DataAccessException {
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT * FROM ").append(getEntDbName(entId))
			.append(".DAT_USER_GROUP WHERE 1=1 AND STATUS='1'");
		if(pageInfo!=null)
			return this.queryByPage(sql.toString(), pageInfo, GroupReturnPo.class);
		else
			return this.queryForList(sql.toString(), GroupReturnPo.class);
	}

	@Override
	public List<AgentReturnPo> getMembers(String entId, String groupId) throws DataAccessException {
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT agent.ID,agent.AGENT_ID,user.NICK_NAME FROM ").append(getEntDbName(entId))
			.append(".DAT_AGENT_ASSIGN agent,").append(getEntDbName(entId))
			.append(".DAT_ENT_USER user WHERE agent.AGENT_ID=user.LOGIN_NAME")
			.append(" AND agent.GROUP_ID='"+groupId+"'")
			.append(" AND user.USER_TYPE IN ('2','3')");
		return  this.queryForList(sql.toString(),AgentReturnPo.class);
	}

	@Override
	public List<AgentReturnPo> getAgents(String entId,String groupId) throws DataAccessException {
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT user.LOGIN_NAME,user.NICK_NAME,agent.GROUP_ID FROM ")
			.append("(SELECT * FROM ").append(getEntDbName(entId))
			.append(".DAT_ENT_USER WHERE USER_TYPE IN ('2','3')) user LEFT JOIN ( SELECT * FROM ")
			.append(getEntDbName(entId)).append(".DAT_AGENT_ASSIGN WHERE GROUP_ID='"+groupId+"') agent")
			.append(" ON user.LOGIN_NAME=agent.AGENT_ID");
		
		return this.queryForList(sql.toString(), AgentReturnPo.class);
	}

	@Override
	public int addAgents(String entId, List<AgentPo> agentPos) throws DataAccessException {
		StringBuffer sql=new StringBuffer();
		sql.append("INSERT INTO ").append(getEntDbName(entId)).append(".DAT_AGENT_ASSIGN")
			.append("(USER_ID,AGENT_ID,GROUP_ID,CREATE_TIME,CREATOR_ID,CREATOR_NAME) VALUES");
		for(int i=0;i<agentPos.size();i++){
			AgentPo poI=agentPos.get(i);
			sql.append("('"+poI.getUserId()+"','"+poI.getAgentId()+"','"+poI.getGroupId()+"',NOW(),'"+poI.getCreatorId()+"','"+poI.getCreatorName()+"')");
			if(i<agentPos.size()-1)
				sql.append(",");
		}
		
		return this.update(sql.toString());
	}

	@Override
	public int deleteAgents(String entId, String groupId, String userIds,boolean delAll) throws DataAccessException {
		StringBuffer sql=new StringBuffer();
		sql.append("DELETE FROM ").append(getEntDbName(entId)).append(".DAT_AGENT_ASSIGN")
			.append(" WHERE 1=1 ");
		if(delAll==true){
			sql.append(" AND GROUP_ID='"+groupId+"'");
			return this.update(sql.toString());
		}else{
			if(StringUtils.isNotBlank(userIds)){
				if(StringUtils.isNotBlank(groupId)){
					sql.append(" AND GROUP_ID='"+groupId+"'");
				}
				sql.append(" AND USER_ID IN ('"+userIds.replaceAll(",", "','")+"')");
				return this.update(sql.toString());
			}
		}
		return 0;
	}

	@Override
	public String getGroupName(String entId, String groupId) throws DataAccessException {
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT * FROM ").append(getEntDbName(entId)).append(".DAT_USER_GROUP")
			.append(" WHERE GROUP_ID='"+groupId+"'");
		return this.queryForObject(sql.toString(), GroupPo.class).getGroupName();
	}

	@Override
	public List<DatEntUserPo> getDetailMembers(String entId, String groupId,PageInfo pageInfo,String userStatus) throws DataAccessException {
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT t.USER_ID,t.USER_NAME,t.USER_TYPE,t.USER_STATUS,t.NICK_NAME,t.PHOTO_URL,t.EMAIL,t.TELPHONE,t.CREATE_TIME,LOG_GROUP.LOGIN_TIME FROM ")
		.append("(SELECT user.* FROM ").append(getEntDbName(entId)).append(".DAT_ENT_USER user,")
		.append(getEntDbName(entId))
		.append(".DAT_AGENT_ASSIGN agent WHERE agent.AGENT_ID=user.LOGIN_NAME ");
		if(StringUtils.isNotBlank(userStatus)){
			sql.append("AND user.USER_STATUS='"+userStatus+"'");
		}
		sql.append(" AND agent.GROUP_ID='"+groupId+"' AND user.USER_TYPE IN ('2','3')) t")
		.append(" LEFT JOIN (SELECT d.LOGIN_NAME,max(d.LOGIN_TIME) LOGIN_TIME FROM ")
		.append(getEntDbName(entId)).append(".DAT_USER_LOGIN d GROUP BY d.LOGIN_NAME) LOG_GROUP ")
		.append("ON t.LOGIN_NAME=LOG_GROUP.LOGIN_NAME");
		return this.queryByPage(sql.toString(), pageInfo, DatEntUserPo.class);
	}

	@Override
	public List<GroupPo> getAgentGroups(String entId,DatEntUserPo user)	throws DataAccessException {
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT G.GROUP_ID,G.GROUP_NAME  FROM ")
			.append(getEntDbName(entId)).append(".DAT_AGENT_ASSIGN  A inner join ")
			.append(getEntDbName(entId)).append(".DAT_USER_GROUP G ON A.GROUP_ID=G.GROUP_ID WHERE 1=1");
        /* if(StringUtils.isNotBlank(user.getUserId())){
        	 sql.append(" AND U.USER_ID='").append(user.getUserId()).append("'");
         }*/
         if(StringUtils.isNotBlank(user.getLoginName())){
        	 sql.append(" AND A.AGENT_ID='").append(user.getLoginName()).append("'");
         }
		return this.queryForList(sql.toString(), GroupPo.class);
	}

	@Override
	public List<GroupPo> getGroupsByIds(String entId, String sqlGroupIds) throws DataAccessException {
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT * FROM ").append(getEntDbName(entId)).append(".DAT_USER_GROUP ")
			.append(" WHERE GROUP_ID IN ("+sqlGroupIds+")");
		return this.queryForList(sql.toString(), GroupPo.class);
	}


}

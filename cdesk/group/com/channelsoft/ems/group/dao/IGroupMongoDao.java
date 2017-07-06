package com.channelsoft.ems.group.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.channelsoft.cri.vo.PageInfo;
import com.channelsoft.ems.group.po.AgentPo;
import com.channelsoft.ems.group.po.AgentReturnPo;
import com.channelsoft.ems.group.po.GroupPo;
import com.channelsoft.ems.group.po.GroupReturnPo;
import com.channelsoft.ems.user.po.DatEntUserPo;

public interface IGroupMongoDao {

	int addUserGroup(String entId, GroupPo po) throws DataAccessException;

	int addAgents(String entId, List<AgentPo> agentPos) throws DataAccessException;

	List<GroupReturnPo> getGroups(String entId, PageInfo pageInfo) throws DataAccessException;

	List<AgentReturnPo> getMembers(String entId, String groupId) throws DataAccessException;

	List<AgentReturnPo> getAgents(String entId, String groupId) throws DataAccessException;

	String getGroupName(String entId, String groupId) throws DataAccessException;

	int deleteAgents(String entId, String groupId, String agents, boolean delAll) throws DataAccessException;

	int updateUserGroup(String entId, GroupPo po) throws DataAccessException;

	int deleteUserGroup(String entId, String groupId) throws DataAccessException;

	List<DatEntUserPo> getDetailMembers(String entId, String groupId, PageInfo pageInfo, String userStatus) throws DataAccessException;

}

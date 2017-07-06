package com.channelsoft.ems.group.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.channelsoft.ems.group.po.AgentReturnPo;
import com.channelsoft.cri.vo.PageInfo;
import com.channelsoft.ems.group.po.AgentPo;
import com.channelsoft.ems.group.po.GroupReturnPo;
import com.channelsoft.ems.group.po.GroupPo;
import com.channelsoft.ems.user.po.DatEntUserPo;

public interface IGroupDao {
	/**
	 * 查询所有用户组信息
	 * @param entId
	 * @param po
	 * @return
	 * @throws DataAccessException
	 */
	public List<GroupPo> queryUserGroup(String entId, GroupPo po) throws DataAccessException;
	
	/**
	 * 查询所有客服信息
	 * @param entId
	 * @param groupId
	 * @return
	 * @throws DataAccessException
	 */
	public List<AgentPo> queryGroupAgent(String entId, String groupId) throws DataAccessException;
	
	/**
	 * 查询所有客服信息
	 * @param entId
	 * @param groupId
	 * @return
	 * @throws DataAccessException
	 */
	public List<AgentPo> queryGroupAgentForMongo(String entId, String groupId) throws DataAccessException;
	
	/**
	 * 删除指定客服组
	 * @param entId
	 * @param groupId
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteUserGroup(String entId, String groupId) throws DataAccessException;

	/**
	 * 添加新客服组信息到客服组表中
	 * @param entId
	 * @param po
	 * @return
	 * @throws DataAccessException
	 */
	public int addUserGroup(String entId, GroupPo po) throws DataAccessException;
	
	/**
	 * 从数据库中获取唯一标示的客服组ID
	 * @return
	 * @throws DataAccessException
	 */
	public String getGroupId() throws DataAccessException;

	/**
	 * 更新客服组表中对应的客服组的信息
	 * @param entId
	 * @param po
	 * @return
	 * @throws DataAccessException
	 */
	public int updateUserGroup(String entId, GroupPo po) throws DataAccessException;

	/**
	 * 获取指定企业下的所有客服组信息
	 * @param entId
	 * @param pageInfo 
	 * @return
	 * @throws DataAccessException
	 */
	public List<GroupReturnPo> getGroups(String entId, PageInfo pageInfo) throws DataAccessException;

	/**
	 * 根据客服组编号获取此客服组的所有坐席信息
	 * @param entId
	 * @param groupId
	 * @return
	 * @throws DataAccessException
	 */
	public List<AgentReturnPo> getMembers(String entId, String groupId) throws DataAccessException;

	/**
	 * 根据客服组ID查询所有客服，包括属于和不属于这个客服组的
	 * @param entId
	 * @param groupId
	 * @return
	 * @throws DataAccessException
	 */
	public List<AgentReturnPo> getAgents(String entId, String groupId) throws DataAccessException;

	/**
	 * 在客服列表中添加指定客服组ID下的新客服
	 * @param entId
	 * @param agentPos
	 * @return
	 * @throws DataAccessException
	 */
	public int addAgents(String entId, List<AgentPo> agentPos) throws DataAccessException;

	/**
	 * 移除客服组下的指定的客服
	 * @param entId
	 * @param groupId
	 * @param agents
	 * @param delAll
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteAgents(String entId, String groupId, String userIds, boolean delAll) throws DataAccessException;

	/**
	 * 根据客服组ID查询客服组名称
	 * @param entId
	 * @param groupId
	 * @return
	 * @throws DataAccessException
	 */
	public String getGroupName(String entId, String groupId) throws DataAccessException;

	/**
	 * 查询指定客服组的客服的详细信息
	 * @param entId
	 * @param groupId
	 * @param pageInfo
	 * @param userStatus 
	 * @return
	 * @throws DataAccessException
	 */
	public List<DatEntUserPo> getDetailMembers(String entId, String groupId,PageInfo pageInfo, String userStatus) throws DataAccessException;
	/**
	 * 获取客服的分组
	 * @param entId
	 * @param user
	 * @return
	 * @throws DataAccessException
	 */
	public List<GroupPo> getAgentGroups(String entId,DatEntUserPo user) throws DataAccessException;

	public List<GroupPo> getGroupsByIds(String entId, String sqlGroupIds) throws DataAccessException;

	



	

	


}

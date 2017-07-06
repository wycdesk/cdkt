package com.channelsoft.ems.group.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.vo.PageInfo;
import com.channelsoft.ems.group.po.AgentReturnPo;
import com.channelsoft.ems.group.po.AgentPo;
import com.channelsoft.ems.group.po.GroupReturnPo;
import com.channelsoft.ems.sso.vo.SsoUserVo;
import com.channelsoft.ems.group.po.GroupPo;
import com.channelsoft.ems.user.po.DatEntUserPo;

public interface IGroupService {
	/**
	 * 查询所有用户组信息
	 * @param request
	 * @param entId
	 * @param po
	 * @return
	 * @throws ServiceException
	 */
	public List<GroupPo> queryUserGroup(HttpServletRequest request, String entId, GroupPo po) throws ServiceException;
	
	/**
	 * 查询所有客服信息
	 * @param entId
	 * @param groupId
	 * @return
	 * @throws ServiceException
	 */
	public List<AgentPo> queryGroupAgent(String entId, String groupId) throws ServiceException;
	
	/**
	 * 删除指定客服组
	 * @param ccodEntId
	 * @param entId
	 * @param groupId
	 * @return
	 * @throws ServiceException
	 */
	public int deleteUserGroup(String ccodEntId,String entId,String groupId) throws ServiceException;
	
	/**
	 * 添加新客服组信息到客服组表中
	 * 先调用CCOD添加技能组接口，成功后入库
	 * @param entId
	 * @param po
	 * @param ccodEntId 
	 * @return
	 * @throws ServiceException
	 */
	public int addUserGroup(String entId,GroupPo po,String ccodEntId) throws ServiceException;
	
	/**
	 * 更新客服组表中对应的客服组的信息,
	 * 先调用ccod技能组修改，成功后本地修改
	 * @param ccodEntId
	 * @param entId
	 * @param po
	 * @return
	 * @throws ServiceException
	 */
	public int updateUserGroup(String ccodEntId,String entId,GroupPo po) throws ServiceException;
	
	/**
	 * 获取指定企业下的所有客服组信息
	 * @param entId
	 * @param pageInfo 
	 * @return
	 * @throws ServiceException
	 */
	public List<GroupReturnPo> getGroups(String entId, PageInfo pageInfo) throws ServiceException;

	/**
	 * 根据客服组编号获取此客服组的所有坐席信息
	 * @param entId
	 * @param groupId
	 * @return
	 * @throws ServiceException
	 */
	public List<AgentReturnPo> getMembers(String entId, String groupId) throws ServiceException;

	/**
	 * 根据客服组ID查询所有客服，包括属于和不属于这个客服组的
	 * @param request
	 * @param entId
	 * @param groupId
	 * @return
	 * @throws ServiceException
	 */
	public List<AgentReturnPo> getAgents(HttpServletRequest request, String entId, String groupId) throws ServiceException;

	/**
	 * 在客服列表中添加指定客服组ID下的新客服
	 * 先调用CCOD接口绑定，然后入库cdesk绑定
	 * @param user
	 * @param entId
	 * @param groupId
	 * @param agents
	 * @return
	 * @throws ServiceException
	 */
	public int addAgents(SsoUserVo user, String entId, String groupId, String userIds) throws ServiceException;
	
	/**
	 * 移除客服组下的指定的客服
	 * @param entId
	 * @param groupId
	 * @param existAll 
	 * @param agents
	 * @param delAll
	 * @return
	 * @throws ServiceException
	 */
	public int deleteAgents(String ccodEntId,String entId, String groupId, String userIds, boolean delAll) throws ServiceException;
	
	/**
	 * 根据客服组ID，将后台查询到的客服与前台传入的客服比较，得出添加和移除的客服
	 * @param all
	 * @param exists
	 * @param delete
	 * @return
	 * @throws ServiceException
	 */
	public String compare(List<String> all, List<AgentReturnPo> exists,List<String> delete) throws ServiceException;

	/**
	 * 根据客服组ID查询客服组名称
	 * @param entId
	 * @param groupId
	 * @return
	 * @throws ServiceException
	 */
	public String getGroupName(String entId, String groupId) throws ServiceException;

	/**
	 * 查询指定客服组的客服的详细信息
	 * @param entId
	 * @param groupId
	 * @param pageInfo
	 * @param userStatus 
	 * @return
	 * @throws ServiceException
	 */
	public List<DatEntUserPo> getDetailMembers(String entId, String groupId, PageInfo pageInfo, String userStatus) throws ServiceException;
    /**
     * 获取客服的分组
     * @param entId
     * @param user
     * @return
     * @throws ServiceException
     */
	public List<GroupPo> getAgentGroups(String entId,DatEntUserPo user)	throws  ServiceException;
	/**
	 * 获取客服分组的字符串，逗号分隔
	 * @param entId
	 * @param loginName
	 * @return
	 * @throws ServiceException
	 * @author zhangtie
	 */
	public String getAgentGroupsStr(String entId, String loginName) throws ServiceException;
	/**
	 * 查询所有企业的所有客服组
	 * @return
	 * @throws ServiceException
	 * @author zhangtie
	 */
	public Map<String, List<GroupPo>> queryAll() throws ServiceException;
	/**
	 * 查询一个企业的所有客服组
	 * @param entId
	 * @return
	 * @throws ServiceException
	 * @author zhangtie
	 */
	public List<GroupPo> queryAll(String entId) throws ServiceException;

	public List<GroupPo> getGroupsByIds(String entId, String groupIds) throws ServiceException;
	
	/**
	 * 查询所有客服信息
	 * @param entId
	 * @param groupId
	 * @return
	 * @throws ServiceException
	 */
	public List<AgentPo> queryGroupAgentForMongo(String entId, String groupId) throws ServiceException;

	public String getGroupId() throws ServiceException;
}

package com.channelsoft.ems.group.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.vo.PageInfo;
import com.channelsoft.ems.group.po.AgentReturnPo;
import com.channelsoft.ems.group.po.GroupPo;
import com.channelsoft.ems.group.po.GroupReturnPo;
import com.channelsoft.ems.user.po.DatEntUserPo;

public interface IGroupMongoService {

	int addUserGroup(HttpServletRequest request, String entId, GroupPo po) throws ServiceException;

	int addAgents(HttpServletRequest request, String entId, String groupId, String agents) throws ServiceException;

	List<GroupReturnPo> getGroups(HttpServletRequest request, String entId, PageInfo pageInfo) throws ServiceException;

	List<AgentReturnPo> getMembers(String entId, String groupId) throws ServiceException;

	List<AgentReturnPo> getAgents(String entId, String groupId) throws ServiceException;

	String getGroupName(HttpServletRequest request, String entId, String groupId) throws ServiceException;

	int deleteAgents(HttpServletRequest request, String entId, String groupId, String agents,boolean delAll) throws ServiceException;

	String compare(List<String> all, List<AgentReturnPo> exists, List<String> delete) throws ServiceException;

	int updateUserGroup(HttpServletRequest request, String entId, GroupPo gPo) throws ServiceException;

	int deleteUserGroup(HttpServletRequest request, String entId, String groupId) throws ServiceException;

	List<DatEntUserPo> getDetailMembers(String entId, String groupId, PageInfo pageInfo, String userStatus) throws ServiceException;

}

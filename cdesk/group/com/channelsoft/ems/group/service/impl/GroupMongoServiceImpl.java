package com.channelsoft.ems.group.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import com.channelsoft.cri.constant.BaseErrCode;
import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.vo.PageInfo;
import com.channelsoft.ems.ent.service.IDatEntService;
import com.channelsoft.ems.group.constant.GroupType;
import com.channelsoft.ems.group.dao.IGroupDao;
import com.channelsoft.ems.group.dao.IGroupMongoDao;
import com.channelsoft.ems.group.po.AgentPo;
import com.channelsoft.ems.group.po.AgentReturnPo;
import com.channelsoft.ems.group.po.GroupPo;
import com.channelsoft.ems.group.po.GroupReturnPo;
import com.channelsoft.ems.group.service.IGroupMongoService;
import com.channelsoft.ems.sso.util.SsoSessionUtils;
import com.channelsoft.ems.sso.vo.SsoUserVo;
import com.channelsoft.ems.user.po.DatEntUserPo;

public class GroupMongoServiceImpl implements IGroupMongoService {
	@Autowired
	IGroupDao groupDao;
	@Autowired
	IGroupMongoDao groupMongoDao;
	@Autowired
	IDatEntService entService;

	@Override
	public List<GroupReturnPo> getGroups(HttpServletRequest request, String entId,PageInfo pageInfo) throws ServiceException {
		try {
			return groupMongoDao.getGroups(entId,pageInfo);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR,e.getMessage());
		}
	}
	
	@Override
	public int addUserGroup(HttpServletRequest request,String entId, GroupPo po) throws ServiceException {
		try {
			po.setGroupId(groupDao.getGroupId());
			po.setGroupType(GroupType.CustomerService.value);
			po.setCreatorId(SsoSessionUtils.getUserInfo(request).getLoginName());
			po.setCreatorName(SsoSessionUtils.getUserInfo(request).getNickName());
			return groupMongoDao.addUserGroup(entId,po);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public int addAgents(HttpServletRequest request, String entId, String groupId, String agents)
			throws ServiceException {
		try {
			SsoUserVo user=SsoSessionUtils.getUserInfo(request);
			List<AgentPo> agentPos=new ArrayList<AgentPo>();
			strToPos(agents,agentPos,user,groupId);
			if(agentPos.size()>0)
				return groupMongoDao.addAgents(entId,agentPos);
			else
				return 0;
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR,e.getMessage());
		}
	}
	
	@Override
	public List<AgentReturnPo> getMembers(String entId, String groupId) throws ServiceException {
		try {
			return groupMongoDao.getMembers(entId,groupId);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR,e.getMessage());
		}
	}
	
	@Override
	public List<AgentReturnPo> getAgents(String entId, String groupId) throws ServiceException {
		try {
			return groupMongoDao.getAgents(entId,groupId);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR,e.getMessage());
		}
	}
	
	@Override
	public String getGroupName(HttpServletRequest request, String entId, String groupId) throws ServiceException {
		try {
			return groupMongoDao.getGroupName(entId,groupId);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public int deleteAgents(HttpServletRequest request, String entId, String groupId, String agents,boolean delAll)
			throws ServiceException {
		try {
			if(delAll==true)
				return groupMongoDao.deleteAgents(entId,groupId,null,delAll);
			else{
				if(StringUtils.isBlank(agents))
					return 0;
				else
					return groupMongoDao.deleteAgents(entId,groupId,agents.replaceAll(" ", ""),delAll);
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR,e.getMessage());
		}
	}
	
	@Override
	public int updateUserGroup(HttpServletRequest request, String entId, GroupPo po) throws ServiceException {
		try {
			po.setUpdatorId(SsoSessionUtils.getUserInfo(request).getLoginName());
			po.setUpdatorName(SsoSessionUtils.getUserInfo(request).getNickName());
			return groupMongoDao.updateUserGroup(entId,po);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@Override
	public int deleteUserGroup(HttpServletRequest request,String entId, String groupId) throws ServiceException {
		try {
			return groupMongoDao.deleteUserGroup(entId,groupId);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public List<DatEntUserPo> getDetailMembers(String entId, String groupId,PageInfo pageInfo,String userStatus) throws ServiceException {
		try {
			return groupMongoDao.getDetailMembers(entId,groupId,pageInfo,userStatus);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR,e.getMessage());
		}
	}
	
	
	@Override
	public String compare(List<String> all, List<AgentReturnPo> exists, List<String> delete) throws ServiceException {
		String rtn="";
		for(int i=0;i<exists.size();i++){
			boolean rpt=false;
			if(all.size()>0){
				for(int j=0;j<all.size();j++){
					if(exists.get(i).getUserId().equals(all.get(j))){
						exists.remove(i);
						all.remove(j);
						i--;
						rpt=true;
						break;
					}
				}
			}
			if(rpt==false)
				delete.add(exists.get(i).getUserId());
		}
		if(all.size()>0){
			rtn=all.toString().substring(1, all.toString().length()-1);
		}
		return rtn.replaceAll(" ", "");
	}
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 将字符串形式的客服信息转换成PO
	 * @param agents
	 * @param agentPos
	 * @param user
	 * @param groupId
	 */
	private void strToPos(String agents, List<AgentPo> agentPos, SsoUserVo user, String groupId) {
		String agentIds[]=agents.split(",");
		if(agentIds.length<=0)
			return;
		for(int i=0;i<agentIds.length;i++){
			if(StringUtils.isBlank(agentIds[i]))
				continue;
			AgentPo po=new AgentPo();
			po.setGroupId(groupId);
			po.setAgentId(agentIds[i].trim());
			po.setCreatorId(user.getLoginName());
			po.setCreatorName(user.getNickName());
			agentPos.add(po);
		}
		
	}

}

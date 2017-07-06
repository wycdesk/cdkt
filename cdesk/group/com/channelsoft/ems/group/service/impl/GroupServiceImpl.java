package com.channelsoft.ems.group.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import com.channelsoft.cri.constant.BaseErrCode;
import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.logger.SystemLogUtils;
import com.channelsoft.cri.vo.PageInfo;
import com.channelsoft.ems.api.client.CCODClient;
import com.channelsoft.ems.api.po.CCODResponsePo;
import com.channelsoft.ems.ent.po.DatEntInfoPo;
import com.channelsoft.ems.ent.service.IDatEntService;
import com.channelsoft.ems.group.constant.GroupType;
import com.channelsoft.ems.group.dao.IGroupDao;
import com.channelsoft.ems.group.po.AgentPo;
import com.channelsoft.ems.group.po.AgentReturnPo;
import com.channelsoft.ems.group.po.GroupPo;
import com.channelsoft.ems.group.po.GroupReturnPo;
import com.channelsoft.ems.group.service.IGroupService;
import com.channelsoft.ems.sso.vo.SsoUserVo;
import com.channelsoft.ems.user.po.DatEntUserPo;
import com.channelsoft.ems.user.service.IUserMongoService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class GroupServiceImpl implements IGroupService {
	@Autowired
	IGroupDao groupDao;
	@Autowired
	IDatEntService entService;

	@Autowired
	IUserMongoService userMongoService;

	@Override
	public List<GroupPo> queryUserGroup(HttpServletRequest request,String entId,GroupPo po) throws ServiceException {
		try {
			return groupDao.queryUserGroup(entId,po);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<AgentPo> queryGroupAgent(String entId, String groupId) throws ServiceException {
		try {
			return groupDao.queryGroupAgent(entId,groupId);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<AgentPo> queryGroupAgentForMongo(String entId, String groupId) throws ServiceException {
		// TODO Auto-generated method stub
		GroupPo po=new GroupPo();
		po.setGroupId(groupId);
		List<AgentPo> list=groupDao.queryGroupAgentForMongo(entId, groupId);
		List<DatEntUserPo> agentList=userMongoService.queryAgentAndAdmin(entId);
		List<AgentPo> aList=new ArrayList<AgentPo>();
		for(AgentPo a:list){
			for(DatEntUserPo u:agentList){
				if(StringUtils.isNotBlank(a.getUserId())&&a.getUserId().equals(u.getUserId())){
					a.setUserId(u.getUserId());
					a.setUserName(u.getUserName());
					a.setNickName(u.getNickName());
					a.setEmail(u.getEmail());
					a.setLoginName(u.getLoginName());
					aList.add(a);
					break;
				}
			}
		}
		return aList;
	}
	@Override
	public int deleteUserGroup(String ccodEntId,String entId, String groupId) throws ServiceException {
		try {
			//ccod和本地 删除客服组
			CCODClient.deleteGroup(ccodEntId, groupId);
			SystemLogUtils.Debug(String.format("ccod删除客服组成功，groupId=%s",groupId));
			
			groupDao.deleteUserGroup(entId,groupId);
			return this.deleteAgents(ccodEntId, entId, groupId, null, true);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public int addUserGroup(String entId, GroupPo po,String ccodEntId) throws ServiceException {
		try {
			
			if(StringUtils.isBlank(po.getGroupName())){
				throw new ServiceException("客服组名称为空");
			}
			List<GroupReturnPo> groups=this.getGroups(entId,null);
			for(int i=0;i<groups.size();i++){
				if(groups.get(i).getGroupName().equals(po.getGroupName())){
					throw new ServiceException("客服组名称已经存在");
				}
			}
			//调用CCOD技能组添加接口
			CCODResponsePo groupR=CCODClient.addGroup(ccodEntId, po);
			po.setGroupId(groupR.getGroupId());
			if(StringUtils.isBlank(po.getGroupType())){
				po.setGroupType(GroupType.CustomerService.value);
			}
			

			return groupDao.addUserGroup(entId,po);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public int updateUserGroup(String ccodEntId, String entId, GroupPo po) throws ServiceException {
		try {
			//调用ccod技能组修改接口
			CCODClient.updateGroup(ccodEntId, po);
			SystemLogUtils.Debug(String.format("ccod客服组修改成功，groupId=%s,groupName=%s",po.getGroupId(), po.getGroupName()));
			return groupDao.updateUserGroup(entId,po);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public List<GroupReturnPo> getGroups(String entId,PageInfo pageInfo) throws ServiceException {
		try {
			return groupDao.getGroups(entId,pageInfo);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR,e.getMessage());
		}
	}
	
	@Override
	public List<AgentReturnPo> getMembers(String entId, String groupId) throws ServiceException {
		try {
			return groupDao.getMembers(entId,groupId);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR,e.getMessage());
		}
	}
	
	@Override
	public List<AgentReturnPo> getAgents(HttpServletRequest request, String entId, String groupId) throws ServiceException {
		try {
			return groupDao.getAgents(entId,groupId);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR,e.getMessage());
		}
	}
	
	@Override
	public int addAgents(SsoUserVo user, String entId, String groupId, String userIds)
			throws ServiceException {
		try {
			//调用ccod坐席绑定接口
			CCODClient.bindAgents(user.getCcodEntId(), groupId, userIds);
			SystemLogUtils.Debug(String.format("ccod客服组添加坐席成功，groupId=%s,agentIds=%s",groupId, userIds));
			
			List<AgentPo> agentPos=new ArrayList<AgentPo>();
			strToPos(userIds,agentPos,user,groupId,entId);
			if(agentPos.size()>0)
				return groupDao.addAgents(entId,agentPos);
			else 
				return 0;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public int deleteAgents(String ccodEntId,String entId, String groupId, String userIds,boolean delAll)
			throws ServiceException {
		try {
			if(ccodEntId!=null&&userIds!=null){
				//调用ccod坐席解绑接口
				CCODClient.disbindAgents(ccodEntId, groupId, userIds);
				SystemLogUtils.Debug(String.format("ccod客服组解绑坐席成功，groupId=%s,agentIds=%s",groupId, userIds));
			}
			return groupDao.deleteAgents(entId,groupId,userIds,delAll);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public String compare(List<String> all, List<AgentReturnPo> exists, List<String> delete) throws ServiceException {
		String rtn="";
		for(int i=0;i<exists.size();i++){
			boolean rpt=false;
			if(all.size()>0)
				for(int j=0;j<all.size();j++){
					if(exists.get(i).getAgentId().equals(all.get(j))){
						exists.remove(i);
						all.remove(j);
						i--;
						rpt=true;
						break;
					}
				}
			if(rpt==false)
				delete.add(exists.get(i).getAgentId());
		}
		if(all.size()>0)
			rtn=all.toString().substring(1, all.toString().length()-1);
		return rtn.replaceAll(" ", "");
	}

	@Override
	public String getGroupName(String entId, String groupId) throws ServiceException {
		try {
			return groupDao.getGroupName(entId,groupId);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * 将字符串形式的客服信息转换成PO
	 * @param agents
	 * @param agentPos
	 * @param user
	 * @param groupId
	 */
	private void strToPos(String userIds, List<AgentPo> agentPos, SsoUserVo user, String groupId,String entId) {
		String UIds[]=userIds.split(",");
		if(UIds.length<=0)
			return;
		for(int i=0;i<UIds.length;i++){
			
		}
		DBObject dbo=new BasicDBObject();
		DBObject dbIns=new BasicDBObject();
		dbIns.put("$in", UIds);
		dbo.put("userId",dbIns);
		dbo.put("entId", entId);
		List<DBObject> users=null;
		try {
			users=userMongoService.queryUserList(dbo, null);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		for(int i=0;i<users.size();i++){
			AgentPo po=new AgentPo();
			dbo=users.get(i);
			po.setUserId((String)dbo.get("userId"));
			po.setGroupId(groupId);
			po.setAgentId((String)dbo.get("loginName"));
			po.setCreatorId(user.getLoginName());
			po.setCreatorName(user.getNickName());
			agentPos.add(po);
		}
		
	}

	@Override
	public List<DatEntUserPo> getDetailMembers(String entId, String groupId,PageInfo pageInfo,String userStatus) throws ServiceException {
		try {
			return groupDao.getDetailMembers(entId,groupId,pageInfo,userStatus);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR,e.getMessage());
		}
	}

	@Override
	public List<GroupPo> getAgentGroups(String entId, DatEntUserPo user)
			throws ServiceException {
		// TODO Auto-generated method stub
		try {
			return groupDao.getAgentGroups(entId, user);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR,e.getMessage());
		}
	}
	
	@Override
	public String getAgentGroupsStr(String entId, String loginName) throws ServiceException {
		DatEntUserPo user = new DatEntUserPo();
		user.setLoginName(loginName);
		List<GroupPo> list = this.getAgentGroups(entId, user);
		String groupStr = "";
		for (int i = 0; i < list.size(); i++) {
			GroupPo group = list.get(i);
			if (i < list.size() - 1) {
				groupStr += group.getGroupId() + ",";
			} else {
				groupStr += group.getGroupId();
			}
		}
		return groupStr;
	}
	
	@Override
	public Map<String, List<GroupPo>> queryAll() throws ServiceException {
		try {
			Map<String, List<GroupPo>> map = new HashMap<String, List<GroupPo>>();
			List<DatEntInfoPo> entList = entService.queryAll();
			for (DatEntInfoPo ent : entList) {
				String entId = ent.getEntId();
				List<GroupPo> groupList = this.queryUserGroup(null, entId, new GroupPo());
				map.put(entId, groupList);
			}
			return map;
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR, "数据库异常");
		}
	}
	
	@Override
	public List<GroupPo> queryAll(String entId) throws ServiceException {
		return this.queryUserGroup(null, entId, new GroupPo());
	}

	@Override
	public List<GroupPo> getGroupsByIds(String entId, String groupIds) throws ServiceException {
		String sqlGroupIds="'"+groupIds.replaceAll(",", "','")+"'";
		List<GroupPo> groups=groupDao.getGroupsByIds(entId,sqlGroupIds);
		return groups;
	}

	@Override
	public String getGroupId() throws ServiceException {
		try {
			return groupDao.getGroupId();
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}

}

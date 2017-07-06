package com.channelsoft.ems.ent.service.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.util.CdeskEncrptDes;
import com.channelsoft.cri.util.WebappConfigUtil;
import com.channelsoft.ems.api.client.CCODClient;
import com.channelsoft.ems.api.client.EntClient;
import com.channelsoft.ems.api.client.MailSendClient;
import com.channelsoft.ems.api.constants.AgentRoleType;
import com.channelsoft.ems.api.po.CCODRequestPo;
import com.channelsoft.ems.ent.constant.EntStatus;
import com.channelsoft.ems.ent.dao.IDatEntDao;
import com.channelsoft.ems.ent.po.DatEntInfoPo;
import com.channelsoft.ems.ent.service.IDatEntService;
import com.channelsoft.ems.group.constant.GroupType;
import com.channelsoft.ems.group.dao.IGroupDao;
import com.channelsoft.ems.group.po.AgentPo;
import com.channelsoft.ems.group.po.GroupPo;
import com.channelsoft.ems.group.service.IGroupService;
import com.channelsoft.ems.privilege.constant.RoleType;
import com.channelsoft.ems.redis.constant.CacheGroup;
import com.channelsoft.ems.redis.service.IParamCacheService;
import com.channelsoft.ems.redis.util.ParamUtils;
import com.channelsoft.ems.register.dao.IRegisterInfoDao;
import com.channelsoft.ems.register.po.RegisterInfoPo;
import com.channelsoft.ems.register.util.ConfigUtil;
import com.channelsoft.ems.register.util.MD5Util;
import com.channelsoft.ems.sso.vo.SsoEntInfoVo;
import com.channelsoft.ems.sso.vo.SsoUserVo;
import com.channelsoft.ems.systemSetting.service.ICancelSettingService;
import com.channelsoft.ems.template.service.ICommHistoryTemplateService;
import com.channelsoft.ems.template.service.ITemplateService;
import com.channelsoft.ems.user.constant.FounderType;
import com.channelsoft.ems.user.constant.LoginType;
import com.channelsoft.ems.user.constant.UserStatus;
import com.channelsoft.ems.user.constant.UserType;
import com.channelsoft.ems.user.dao.IUserDao;
import com.channelsoft.ems.user.po.DatEntUserPo;
import com.channelsoft.ems.user.service.IUserMongoService;
import com.channelsoft.ems.user.service.IUserService;
import com.channelsoft.ems.user.util.UserUtil;

public class DatEntServiceImp implements IDatEntService {

	@Autowired
	IDatEntDao datEntDao;
	@Autowired
	IUserService userService;
	@Autowired
	IGroupDao groupDao;
	@Autowired
	IUserMongoService userMongoService;
	@Autowired
	IUserDao userDao;
	@Autowired 
	private IRegisterInfoDao registerDao;
	
	@Autowired
	ICancelSettingService cancelService;
	@Autowired
	IGroupService groupService;
	@Autowired
	IParamCacheService cacheService;
	@Autowired
	ITemplateService templateService;
	
	@Autowired
	ICommHistoryTemplateService commHistoryTemplateService;
	
	@Override
	@Transactional
	public int addEntInfo(DatEntInfoPo po) throws ServiceException {
		// TODO Auto-generated method stub
		try{
			datEntDao.addEntInfo(po);
			return this.initEntDatabase(po);
		}
		catch(DataAccessException e){
			e.printStackTrace();
			throw new ServiceException("企业信息入库失败");
		}

	}

	@Override
	public boolean existThisMail(String email) throws ServiceException {
		// TODO Auto-generated method stub
		try{
			return datEntDao.existThisMail(email);
		}
		catch(DataAccessException e){
			e.printStackTrace();
			throw new ServiceException("查询企业信息失败");
		}
	}

	@Override
	public boolean existThisEntId(String entId) throws ServiceException {
		// TODO Auto-generated method stub
		try{
			return datEntDao.existThisEntId(entId);
		}
		catch(DataAccessException e){
			e.printStackTrace();
			throw new ServiceException("查询企业信息失败");
		}
	}

	@Override
	@Transactional
	public int initEntDatabase(DatEntInfoPo po) throws ServiceException {
		// TODO Auto-generated method stub
		try{
			//初始化mongdb数据库

			EntClient.initMongDb(po.getEntId());
		
			//初始化mysql数据库
			datEntDao.createDataBase(po.getEntId());
			datEntDao.createTableName(po.getEntId());
			/**
			 * 生成管理员用户
			 */
			DatEntUserPo user=new DatEntUserPo();
			user.setLoginType(LoginType.MAILBOX.value);
			user.setLoginName(po.getEmail());
			String pwd= CdeskEncrptDes.encryptST(po.getPassword());
			user.setEntId(po.getEntId());
			user.setEntName(po.getEntName());
			user.setLoginPwd(pwd);
			user.setEmail(po.getEmail());
			user.setEmailPwd(pwd);
			user.setNickName(po.getRegister());
			user.setUserName(po.getRegister());
			user.setCreatorId(po.getEmail());
			user.setCreatorName(po.getRegister());
			user.setUserStatus(UserStatus.NORMAL.value);
			user.setUserType(UserType.ADMINISTRATOR.value);
			user.setRoleId(RoleType.ADMINISTRATOR.value);
		    user.setUserId(userDao.getUserId());
			
			/**
			 * 通知Chat添加客服
			 */
			//CchatClient.addAgent(user.getEntId(), user.getUserId(), user.getLoginName(), user.getLoginPwd());
			/**
			 * 通知CCOD添加坐席
			 */
			CCODRequestPo ccodPo = new CCODRequestPo();
			ccodPo.setEnterpriseId(user.getCcodEntId());
			ccodPo.setAgentId(user.getUserId());
			ccodPo.setAgentName(user.getUserName());
			ccodPo.setAgentPassword(CdeskEncrptDes.decryptST(user.getLoginPwd()));
			ccodPo.setAgentRole(AgentRoleType.MANAGER.value);
			CCODClient.addAgent(ccodPo);
			
			userMongoService.add(user, user.getEntId());
			/**
			 * 生产客服分组
			 */
			GroupPo group=new GroupPo();
			group.setGroupName(po.getEntName()+"支持组");
			group.setGroupDesc(po.getEntName()+"支持组");
			group.setGroupId(groupDao.getGroupId());
			group.setGroupType(GroupType.IM.value);
			group.setCreatorId(po.getEmail());
			group.setCreatorName(po.getRegister());
			groupDao.addUserGroup(po.getEntId(), group);
			List<AgentPo> agentPos=new ArrayList<AgentPo>();
			AgentPo a=new AgentPo();
			a.setAgentId(po.getEmail());
			a.setGroupId(group.getGroupId());
			a.setCreatorId(po.getEmail());
			a.setCreatorName(po.getRegister());
			agentPos.add(a);
			groupDao.addAgents(po.getEntId(), agentPos);

			/**
			 * 刷新缓存
			 */
			ParamUtils.refreshCache(CacheGroup.ENT_INFO, po.getEntId());
			ParamUtils.refreshCache(CacheGroup.ENT_USER, po.getEntId());
			ParamUtils.refreshCache(CacheGroup.GROUP, po.getEntId());
			ParamUtils.refreshCache(CacheGroup.GROUP_AGENT, po.getEntId());
			CCODClient.addGroup(user.getCcodEntId(), group);
			CCODClient.bindAgents(user.getCcodEntId(), group.getGroupId(), user.getUserId());
			return  1;
		}
		catch(DataAccessException e){
			e.printStackTrace();
			throw new ServiceException("生成企业信息失败");
		}
	}
	
	@Override
	public List<DatEntInfoPo> query(DatEntInfoPo po) throws ServiceException {
		try{
			return datEntDao.query(po);
		}
		catch(DataAccessException e){
			e.printStackTrace();
			throw new ServiceException("企业信息查询失败");
		}
	}

	@Override
	public SsoEntInfoVo query(String entId) throws ServiceException {
		DatEntInfoPo po = new DatEntInfoPo();
		po.setEntId(entId);
		List<DatEntInfoPo> list = this.query(po);
		if (list.size() == 0) {
			throw new ServiceException("不存在企业" + entId);
		}
		SsoEntInfoVo vo = new SsoEntInfoVo();
		BeanUtils.copyProperties(list.get(0), vo);
		return vo;
	}

	@Override
	public List<DatEntInfoPo> queryAll() throws ServiceException {
		return this.query(new DatEntInfoPo());
	}

	@Override
	public int update(DatEntInfoPo po) throws ServiceException {
		try{
			return datEntDao.update(po);
		} catch(DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException("企业信息更新失败");
		}
	}

	@Override
	public int addEntForUserCenter(DatEntInfoPo po,DatEntUserPo user) throws ServiceException {
		String entId=po.getEntId();
		// TODO Auto-generated method stub
		try{

			//初始化mongdb数据库
			EntClient.initMongDb(po.getEntId());
			//初始化mysql数据库
			datEntDao.createDataBase(po.getEntId());
			datEntDao.createTableName(po.getEntId());
			
			po.setStatus(EntStatus.NORMAL.value);
			datEntDao.addEntInfo(po);


			/**
			 * 纪录企业注册
			 */
			String activeCode = MD5Util.MD5(user.getEmail() + new Date().getTime());
			RegisterInfoPo register=new RegisterInfoPo();
			register.setEmail(user.getEmail());
			register.setIp(user.getIp());
			register.setTelephone(po.getTelphone());
			register.setActiveCode(activeCode);
			register.setStatus("0");
			registerDao.addRegister(register);
			
			/**
			 * 生成管理员用户
			 */
            String pwd=po.getPassword();
			user.setEntId(po.getEntId());
			user.setEntName(po.getEntName());
			user.setLoginPwd(CdeskEncrptDes.encryptST(pwd));
            UserUtil.setUserAccountForAdd(user, user.getLoginName(), user.getLoginType(),pwd);
			user.setUserStatus(UserStatus.NORMAL.value);
			user.setUserType(UserType.ADMINISTRATOR.value);
			user.setRoleId(RoleType.ADMINISTRATOR.value);
		    user.setUserId(userDao.getUserId());
		    user.setCcodEntId(po.getCcodEntId());
		    user.setFounder(FounderType.FOUNDER.value);

			/**
			 * 通知CCOD添加坐席
			 */
			CCODRequestPo ccodPo = new CCODRequestPo();
			ccodPo.setEnterpriseId(user.getCcodEntId());
			ccodPo.setAgentId(user.getUserId());
			ccodPo.setAgentName(user.getUserName());
			ccodPo.setAgentPassword(CdeskEncrptDes.decryptST(user.getLoginPwd()));
			ccodPo.setAgentRole(AgentRoleType.MANAGER.value);
			CCODClient.addAgent(ccodPo);
			
			userMongoService.add(user, user.getEntId());
			/**
			 * 生产客服分组
			 */
			GroupPo group=new GroupPo();
			group.setGroupName(po.getEntName()+"支持组");
			group.setGroupDesc(po.getEntName()+"支持组");
			group.setGroupType(GroupType.IM.value);
			group.setCreatorId(user.getLoginName());
			group.setCreatorName(user.getUserName());

			groupService.addUserGroup(entId, group, po.getCcodEntId());

			SsoUserVo u=new SsoUserVo();
			BeanUtils.copyProperties(user,u);
			groupService.addAgents(u, entId, group.getGroupId(), user.getUserId());
			/**
			 * 生成默认工单摸板
			 */
			templateService.addDefaultWorkTemp(entId);
			/**
			 * 生成默认联络历史 摸板
			 */
			commHistoryTemplateService.addDefaultCommHistoryTemp(entId);
			
			/**
			 * 刷新缓存
			 */
	
			cacheService.refreshFullCache(CacheGroup.ENT_INFO, null);
			cacheService.refreshFullCache(CacheGroup.ENT_USER, null);
			cacheService.refreshFullCache(CacheGroup.GROUP, null);
			cacheService.refreshFullCache(CacheGroup.GROUP_AGENT, null);
			cacheService.refreshFullCache(CacheGroup.TEMPLATE, entId);
			cacheService.refreshFullCache(CacheGroup.TEMPLATE_FIELD, entId);
	
			//发送邮件
			if(StringUtils.isNotBlank(user.getEmail())){
				String path=WebappConfigUtil.getParameter("CDESK_ROOT").replace("entId", po.getDomainName());
		    	Object[] os = new Object[] { path };
				String emailContent = ConfigUtil.getString("success.email.content");		
		    	emailContent = MessageFormat.format(emailContent, os);    
		       MailSendClient.sendEntRegisterMail(po.getDomainName(), user.getEmail(), "欢迎加入青牛云客服，改善你的客户服务", emailContent);
			}
		
			return 1;
		}
		catch(Exception e){
			e.printStackTrace();
			cancelService.deleteEnterprise(entId);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public boolean existThisCcodEntId(String ccodEntId) throws ServiceException {
		// TODO Auto-generated method stub
		if(StringUtils.isBlank(ccodEntId)) return false;
		DatEntInfoPo po=new DatEntInfoPo();
		po.setCcodEntId(ccodEntId);
		List<DatEntInfoPo> list=datEntDao.query(po);
		if(list!=null&&list.size()>0) return true;
		return false;
	}
}

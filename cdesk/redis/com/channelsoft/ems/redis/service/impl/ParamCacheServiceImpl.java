package com.channelsoft.ems.redis.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.tuckey.web.filters.urlrewrite.utils.StringUtils;

import com.channelsoft.cri.constant.DateConstants;
import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.service.BaseService;
import com.channelsoft.cri.util.WebappConfigUtil;
import com.channelsoft.ems.api.po.AgentSimplePo;
import com.channelsoft.ems.ent.po.DatEntInfoPo;
import com.channelsoft.ems.ent.service.IDatEntService;
import com.channelsoft.ems.field.po.BaseFieldPo;
import com.channelsoft.ems.field.po.UserDefinedFiedPo;
import com.channelsoft.ems.field.service.IFieldService;
import com.channelsoft.ems.field.service.IUserFieldService;
import com.channelsoft.ems.group.po.AgentPo;
import com.channelsoft.ems.group.po.GroupPo;
import com.channelsoft.ems.group.service.IGroupService;
import com.channelsoft.ems.redis.constant.CacheGroup;
import com.channelsoft.ems.redis.service.IParamCacheService;
import com.channelsoft.ems.redis.util.EntInfoUtils;
import com.channelsoft.ems.redis.util.EntUserUtils;
import com.channelsoft.ems.redis.util.GroupAgentUtils;
import com.channelsoft.ems.redis.util.GroupUtils;
import com.channelsoft.ems.redis.util.TemplateFieldUtils;
import com.channelsoft.ems.redis.util.TemplateUtils;
import com.channelsoft.ems.template.constants.TemplateTypeEnum;
import com.channelsoft.ems.template.po.TemplatePo;
import com.channelsoft.ems.template.service.ITemplateService;
import com.channelsoft.ems.user.constant.UserType;
import com.channelsoft.ems.user.po.DatEntUserPo;
import com.channelsoft.ems.user.service.IUserMongoService;
import com.channelsoft.ems.user.service.IUserService;

public class ParamCacheServiceImpl extends BaseService implements IParamCacheService {
	@Autowired
	IUserService userService;
	@Autowired
	IDatEntService entService;
	@Autowired
	IGroupService groupService;

	@Autowired
	IUserFieldService userFieldService;
	@Autowired
	IUserMongoService userMongoService;
	@Autowired
	ITemplateService templateService;
	@Autowired
	IFieldService fieldService;
	
//	public void init() {
//			(new InitCacheThread()).start();
//	}
	
//	public class InitCacheThread extends Thread {
//		public void run() {
//			try {
//				Thread.sleep(3000);
//				ParamCacheServiceImpl.this.refreshFullCache(CacheGroup.ENT_INFO, null);
//				ParamCacheServiceImpl.this.refreshFullCache(CacheGroup.GROUP, null);
//				ParamCacheServiceImpl.this.refreshFullCache(CacheGroup.GROUP_AGENT, null);
//				ParamCacheServiceImpl.this.refreshFullCache(CacheGroup.ENT_USER, null);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	}


	@Override
	public void flushFullCache(CacheGroup group, String entId) {
		/*CacheStatusVo status = CacheStatusUtils.flush(group.name);
		if (status != null) {*/
			try {
				switch (group) {
					case ENT_USER:
						if (StringUtils.isBlank(entId)) {
							this.cacheUser();
						} else {
							this.cacheUser(entId);
						}
						break;
					case ENT_INFO:
						this.cacheEnt();
						break;
					case GROUP:
						if (StringUtils.isBlank(entId)) {
							this.cacheGroup();
						} else {
							this.cacheGroup(entId);
						}
						break;
					case GROUP_AGENT:
						if (StringUtils.isBlank(entId)) {
							this.cacheGroupAgent();
						} else {
							this.cacheGroupAgent(entId);
						}
						break;
					case TEMPLATE:
						if (StringUtils.isBlank(entId)) {
							this.cacheTemplate();
						} else {
							this.cacheTemplate(entId);
						}
						break;
					case WORK_TEMPLATE:
						if (StringUtils.isBlank(entId)) {
							this.cacheTemplate(CacheGroup.WORK_TEMPLATE);
						} else {
							this.cacheTemplate(entId, CacheGroup.WORK_TEMPLATE);
						}
						break;
					case CONTACT_HISTORY_TEMPLATE:
						if (StringUtils.isBlank(entId)) {
							this.cacheTemplate(CacheGroup.CONTACT_HISTORY_TEMPLATE);
						} else {
							this.cacheTemplate(entId, CacheGroup.CONTACT_HISTORY_TEMPLATE);
						}
						break;
						
						
					case TEMPLATE_FIELD:
						if (StringUtils.isBlank(entId)) {
							this.cacheTemplateField();
						} else {
							this.cacheTemplateField(entId);
						}
						break;
					case WORK_TEMPLATE_FIELD:
						if (StringUtils.isBlank(entId)) {
							this.cacheTemplateField(entId);
						} else {
							this.cacheTemplateField(entId, CacheGroup.WORK_TEMPLATE_FIELD);
						}
						break;
					case CONTACT_HISTORY_TEMPLATE_FIELD:
						if (StringUtils.isBlank(entId)) {
							this.cacheTemplateField(entId);
						} else {
							this.cacheTemplateField(entId, CacheGroup.CONTACT_HISTORY_TEMPLATE_FIELD);
						}
						break;
				}
				/*CacheStatusUtils.success(status);*/
			} catch (ServiceException e) {
				this.logFail("EMS", "定期全量缓存", group.desc, e);
				//CacheStatusUtils.fail(group.name);
			}
		/*} else {
			*//**
			 * 缓存为空，无需刷新，因此只需要更新对应的缓存
			 *//*
			switch (group) {
				case ENT_USER:
					EntUserUtils.getMemFromCache();
					break;
				case ENT_INFO:
					EntInfoUtils.getMemFromCache();
					break;
				case GROUP:
					GroupUtils.getMemFromCache();
					break;
				case GROUP_AGENT:
					GroupAgentUtils.getMemFromCache();
					break;
			}
		}*/
	}
	
	@Override
	public void refreshFullCache(CacheGroup group, String entId) {
		/*CacheStatusVo status = CacheStatusUtils.refresh(group.name);
		if (status != null) {*/
			try {
				switch (group) {
					case ENT_USER:
						if (StringUtils.isBlank(entId)) {
							this.cacheUser();
						} else {
							this.cacheUser(entId);
						}
						break;
					case ENT_INFO:
						this.cacheEnt();
						break;
					case GROUP:
						if (StringUtils.isBlank(entId)) {
							this.cacheGroup();
						} else {
							this.cacheGroup(entId);
						}
						break;
					case GROUP_AGENT:
						if (StringUtils.isBlank(entId)) {
							this.cacheGroupAgent();
						} else {
							this.cacheGroupAgent(entId);
						}
						break;
					case TEMPLATE:
						if (StringUtils.isBlank(entId)) {
							this.cacheTemplate();
						} else {
							this.cacheTemplate(entId);
						}
						break;
					case WORK_TEMPLATE:
						if (StringUtils.isBlank(entId)) {
							this.cacheTemplate(CacheGroup.WORK_TEMPLATE);
						} else {
							this.cacheTemplate(entId, CacheGroup.WORK_TEMPLATE);
						}
						break;
					case CONTACT_HISTORY_TEMPLATE:
						if (StringUtils.isBlank(entId)) {
							this.cacheTemplate(CacheGroup.CONTACT_HISTORY_TEMPLATE);
						} else {
							this.cacheTemplate(entId, CacheGroup.CONTACT_HISTORY_TEMPLATE);
						}
						break;
						
					case TEMPLATE_FIELD:
						if (StringUtils.isBlank(entId)) {
							this.cacheTemplateField();
						} else {
							this.cacheTemplateField(entId);
						}
						break;
					case WORK_TEMPLATE_FIELD:
						if (StringUtils.isBlank(entId)) {
							this.cacheTemplateField(entId);
						} else {
							this.cacheTemplateField(entId, CacheGroup.WORK_TEMPLATE_FIELD);
						}
						break;
					case CONTACT_HISTORY_TEMPLATE_FIELD:
						if (StringUtils.isBlank(entId)) {
							this.cacheTemplateField(entId);
						} else {
							this.cacheTemplateField(entId, CacheGroup.CONTACT_HISTORY_TEMPLATE_FIELD);
						}
						break;
				}
				/*CacheStatusUtils.success(status);*/
			} catch (ServiceException e) {
				this.logFail("EMS", "刷新全量缓存", group.desc, e);
				//CacheStatusUtils.fail(group.name);
			}
		/*}*/
	}
	
	private String getFlag() {
		String instanceId = WebappConfigUtil.getParameter("INSTANCE_ID");
		String updateTimeStr = DateConstants.DATE_FORMAT().format(new Date());
		return updateTimeStr + "_" + instanceId;
	}

	/**
	 * 缓存用户信息
	 * @throws ServiceException
	 * @author zhangtie
	 */
	private void cacheUser() throws ServiceException{
//		Map<String, List<DatEntUserPo>> allList = userService.queryAgentAndAdmin();
		Map<String, List<DatEntUserPo>> allList = userMongoService.queryAgentAndAdmin();
		Map<String, Map<String, DatEntUserPo>> map = new HashMap<String, Map<String, DatEntUserPo>>();
		Map<String, Map<String, List<DatEntUserPo>>> list = new HashMap<String, Map<String, List<DatEntUserPo>>>();
		
		Map<String, String> flags = new HashMap<String, String>();
		String flag = getFlag();
		
		for(String entId: allList.keySet()) {
			List<DatEntUserPo> entUserList = allList.get(entId);
			// 处理生成map
			Map<String, DatEntUserPo> entUserMap = new HashMap<String, DatEntUserPo>();
			for (DatEntUserPo user : entUserList) {
				entUserMap.put(user.getUserId(), user);
			}
			map.put(entId, entUserMap);
			// 处理生成list
			List<DatEntUserPo> customerList = new ArrayList<DatEntUserPo>();
			List<DatEntUserPo> serviceList = new ArrayList<DatEntUserPo>();
			List<DatEntUserPo> adminList = new ArrayList<DatEntUserPo>();
			for (DatEntUserPo user : entUserList) {
				if (UserType.CUSTOMER.value.equals(user.getUserType())) {
					customerList.add(user);
				} else if (UserType.SERVICE.value.equals(user.getUserType())) {
					serviceList.add(user);
				} else if (UserType.ADMINISTRATOR.value.equals(user.getUserType())) {
					adminList.add(user);
				}
			}
			Map<String, List<DatEntUserPo>> entUserTypeMap = new HashMap<String, List<DatEntUserPo>>();
			entUserTypeMap.put(UserType.CUSTOMER.value, customerList);
			entUserTypeMap.put(UserType.SERVICE.value, serviceList);
			entUserTypeMap.put(UserType.ADMINISTRATOR.value, adminList);
			list.put(entId, entUserTypeMap);
			
			flags.put(entId, flag);
		}
		if (list!=null && list.size()>0) {
			EntUserUtils.getCacheFromDB(list, map, flags);
		}
	}
	
	/**
	 * 缓存指定企业的用户信息
	 * @param entId
	 * @throws ServiceException
	 * @author zhangtie
	 */
	private void cacheUser(String entId) throws ServiceException{
		String flag = getFlag();
		
//		List<DatEntUserPo> entUserList = userService.queryAgentAndAdmin(entId);
		List<DatEntUserPo> entUserList = userMongoService.queryAgentAndAdmin(entId);
		// 处理生成map
		Map<String, DatEntUserPo> entUserMap = new HashMap<String, DatEntUserPo>();
		for (DatEntUserPo user : entUserList) {
			entUserMap.put(user.getUserId(), user);
		}
		// 处理生成list
		List<DatEntUserPo> customerList = new ArrayList<DatEntUserPo>();
		List<DatEntUserPo> serviceList = new ArrayList<DatEntUserPo>();
		List<DatEntUserPo> adminList = new ArrayList<DatEntUserPo>();
		for (DatEntUserPo user : entUserList) {
			if (UserType.CUSTOMER.value.equals(user.getUserType())) {
				customerList.add(user);
			} else if (UserType.SERVICE.value.equals(user.getUserType())) {
				serviceList.add(user);
			} else if (UserType.ADMINISTRATOR.value.equals(user.getUserType())) {
				adminList.add(user);
			}
		}
		Map<String, List<DatEntUserPo>> entUserTypeMap = new HashMap<String, List<DatEntUserPo>>();
		entUserTypeMap.put(UserType.CUSTOMER.value, customerList);
		entUserTypeMap.put(UserType.SERVICE.value, serviceList);
		entUserTypeMap.put(UserType.ADMINISTRATOR.value, adminList);
			
		if (entUserMap!=null && entUserMap.size()>0) {
			EntUserUtils.getCacheFromDB(entId, entUserTypeMap, entUserMap, flag);
		}
	}
	
	/**
	 * 缓存企业信息
	 * @throws ServiceException
	 * @author zhangtie
	 */
	private void cacheEnt() throws ServiceException{
		String flag = getFlag();
		
		List<DatEntInfoPo> list = entService.queryAll();
		Map<String, DatEntInfoPo> map = new HashMap<String, DatEntInfoPo>();
		for(DatEntInfoPo po: list) {
			map.put(po.getEntId(), po);
		}
		if (list!=null && list.size()>0) {
			EntInfoUtils.getCacheFromDB(list, map, flag);
		}
	}

	/**
	 * 缓存客服组信息
	 * @throws ServiceException
	 * @author zhangtie
	 */
	private void cacheGroup() throws ServiceException {
		Map<String, List<GroupPo>> list = groupService.queryAll();
		Map<String, Map<String, GroupPo>> map = new HashMap<String, Map<String, GroupPo>>();
		
		Map<String, String> flags = new HashMap<String, String>();
		String flag = getFlag();
		
		for(String entId : list.keySet()) {
			Map<String, GroupPo> entGroupMap = new HashMap<String, GroupPo>();
			List<GroupPo> entGroupList = list.get(entId);
			for (GroupPo po : entGroupList) {
				entGroupMap.put(po.getGroupId(), po);
			}
			map.put(entId, entGroupMap);
			flags.put(entId, flag);
		}
		if (list!=null && list.size()>0) {
			GroupUtils.getCacheFromDB(list, map, flags);
		}
	}
	/**
	 * 缓存指定企业的客服组信息
	 * @param entId
	 * @throws ServiceException
	 * @author zhangtie
	 */
	private void cacheGroup(String entId) throws ServiceException {
		List<GroupPo> entGroupList = groupService.queryAll(entId);
		String flag = getFlag();
		
		Map<String, GroupPo> entGroupMap = new HashMap<String, GroupPo>();
		for (GroupPo po : entGroupList) {
			entGroupMap.put(po.getGroupId(), po);
		}
		
		GroupUtils.getCacheFromDB(entId, entGroupList, entGroupMap, flag);
	}
	
	/**
	 * 缓存客服组-客服信息
	 * @throws ServiceException
	 * @author zhangtie
	 */
	private void cacheGroupAgent() throws ServiceException {
		Map<String, List<GroupPo>> allList = groupService.queryAll();
		Map<String, Map<String, List<AgentSimplePo>>> list = new HashMap<String, Map<String, List<AgentSimplePo>>>();
		
		Map<String, String> flags = new HashMap<String, String>();
		String flag = getFlag();
		
		for(String entId : allList.keySet()) {
			List<GroupPo> entGroupList = allList.get(entId);
			Map<String, List<AgentSimplePo>> entGroupAgentList = new HashMap<String, List<AgentSimplePo>>();
			// 查出该企业的所有客服组-客服关系
//			List<AgentPo> agentList = groupService.queryGroupAgent(entId, null);
			List<AgentPo> agentList = groupService.queryGroupAgentForMongo(entId, null);
			for (GroupPo po : entGroupList) {
				List<AgentSimplePo> agentSimpleList = new ArrayList<AgentSimplePo>();
				for (AgentPo agentPo : agentList) {
					if (po.getGroupId().equals(agentPo.getGroupId())) {
						AgentSimplePo simplePo = new AgentSimplePo();
						BeanUtils.copyProperties(agentPo, simplePo);
						agentSimpleList.add(simplePo);
					}
				}
				entGroupAgentList.put(po.getGroupId(), agentSimpleList);
			}
			list.put(entId, entGroupAgentList);
			flags.put(entId, flag);
		}
		
		GroupAgentUtils.getCacheFromDB(list, flags);
	}
	/**
	 * 缓存指定企业的客服组-客服信息
	 * @param entId
	 * @throws ServiceException
	 * @author zhangtie
	 */
	private void cacheGroupAgent(String entId) throws ServiceException {
		List<GroupPo> entGroupList = groupService.queryAll(entId);
		String flag = getFlag();
		
		Map<String, List<AgentSimplePo>> entGroupAgentList = new HashMap<String, List<AgentSimplePo>>();
//		List<AgentPo> agentList = groupService.queryGroupAgent(entId, null);
		List<AgentPo> agentList = groupService.queryGroupAgentForMongo(entId, null);
		for (GroupPo po : entGroupList) {
			List<AgentSimplePo> agentSimpleList = new ArrayList<AgentSimplePo>();
			for (AgentPo agentPo : agentList) {
				if (po.getGroupId().equals(agentPo.getGroupId())) {
					AgentSimplePo simplePo = new AgentSimplePo();
					BeanUtils.copyProperties(agentPo, simplePo);
					agentSimpleList.add(simplePo);
				}
			}
			entGroupAgentList.put(po.getGroupId(), agentSimpleList);
		}
		
		GroupAgentUtils.getCacheFromDB(entId, entGroupAgentList, flag);
	}
	
	private void cacheEntUserField(String entId) throws ServiceException {
		List<UserDefinedFiedPo> entUserFieldList = userFieldService.query(entId, new UserDefinedFiedPo(), null);
		String flag = getFlag();
		
		Map<String, UserDefinedFiedPo> entUserFieldMap = new HashMap<String, UserDefinedFiedPo>();
		for (UserDefinedFiedPo po : entUserFieldList) {
			entUserFieldMap.put(po.getKey(), po);
		}
		
		
	}
	/**
	 * 根据模版类型获取模版对应的缓存组
	 * @param templateType
	 * @return
	 */
	CacheGroup getCacheGroupForTemplate(String templateType){
		CacheGroup cacheGroup=CacheGroup.ELSE;
		if(TemplateTypeEnum.WORK.value.equals(templateType)){
			 cacheGroup=CacheGroup.WORK_TEMPLATE;
		}
		else if(TemplateTypeEnum.USER.value.equals(templateType)){
			cacheGroup=CacheGroup.USER_TEMPLATE;
		}   
		else if(TemplateTypeEnum.COMMHISTORY.value.equals(templateType)){
			cacheGroup=CacheGroup.CONTACT_HISTORY_TEMPLATE;
		}   
		return cacheGroup;
	}
	/**
	 * 根据模版类型获取模版字段对应的缓存组
	 * @param templateType
	 * @return
	 */
	CacheGroup getCacheGroupForTemplateField(String templateType){
		CacheGroup cacheGroup=CacheGroup.ELSE;
		if(TemplateTypeEnum.WORK.value.equals(templateType)){
			 cacheGroup=CacheGroup.WORK_TEMPLATE_FIELD;
		}
		else if(TemplateTypeEnum.USER.value.equals(templateType)){
			cacheGroup=CacheGroup.USER_TEMPLATE_FIELD;
		}   
		else if(TemplateTypeEnum.COMMHISTORY.value.equals(templateType)){
			cacheGroup=CacheGroup.CONTACT_HISTORY_TEMPLATE_FIELD;
		}   
		return cacheGroup;
	}
	
	String getTemplateType(CacheGroup cacheGroup){
		String templateType="";
		switch (cacheGroup) {
		   case WORK_TEMPLATE:
		   case WORK_TEMPLATE_FIELD:
			   templateType=TemplateTypeEnum.WORK.value;
		       break;
		   case USER_TEMPLATE:
		   case USER_TEMPLATE_FIELD:
			   templateType=TemplateTypeEnum.USER.value;
		    	break;
		   case CONTACT_HISTORY_TEMPLATE:
		   case CONTACT_HISTORY_TEMPLATE_FIELD:
			   templateType=TemplateTypeEnum.COMMHISTORY.value;
		    	break;
		}
		return templateType;
	}
	/**
	 * 全量刷新所有模版缓存
	 * @throws ServiceException
	 */
	private void cacheTemplate() throws ServiceException {
		Map<String, Map<String,List<TemplatePo>>> map=templateService.queryAll();
		for(String entId : map.keySet()) {
			Map<String,List<TemplatePo>> entTemplateMap=map.get(entId);
			List<TemplatePo> telmplateList=new ArrayList<TemplatePo>();
			for(String templateType:entTemplateMap.keySet()){
				CacheGroup cacheGroup=CacheGroup.ELSE;
				telmplateList=entTemplateMap.get(templateType);
				cacheGroup=getCacheGroupForTemplate(templateType); 
				Map<String, TemplatePo> templateMap = new HashMap<String, TemplatePo>();
				for(TemplatePo temp:telmplateList){
					templateMap.put(temp.getTempId(), temp);
				}
				String flag = getFlag();
				TemplateUtils.getCacheFromDB(cacheGroup, entId, telmplateList, templateMap, flag);
			}
			
		}
	}
	/**
	 * 按模版类型全量刷新缓存
	 * @param cacheGroup
	 * @throws ServiceException
	 */
	private void cacheTemplate(CacheGroup cacheGroup) throws ServiceException {
		Map<String, Map<String,List<TemplatePo>>> map=templateService.queryAll();
		for(String entId : map.keySet()) {
			Map<String,List<TemplatePo>> entTemplateMap=map.get(entId);
			List<TemplatePo> telmplateList=new ArrayList<TemplatePo>();
			telmplateList=entTemplateMap.get(getTemplateType(cacheGroup));
			Map<String, TemplatePo> templateMap = new HashMap<String, TemplatePo>();
			for(TemplatePo temp:telmplateList){
				templateMap.put(temp.getTempId(), temp);
			}
			String flag = getFlag();
			TemplateUtils.getCacheFromDB(cacheGroup, entId, telmplateList, templateMap, flag);
		}
		
	}
	/**
	 * 按企业全量刷新缓存
	 * @param cacheGroup
	 * @throws ServiceException
	 */
	private void cacheTemplate(String entId) throws ServiceException {
	
		TemplatePo po=new TemplatePo();
		 List<TemplatePo> list=templateService.query(entId, po, null);
		 Map<String,List<TemplatePo>> typeMap=new HashMap<String,List<TemplatePo>>();
		 for(TemplatePo t:list){
			 List<TemplatePo> typeList=typeMap.get(t.getTempType());
			  if(typeList==null||typeList.size()==0){
				  typeList=new ArrayList<TemplatePo>();
			  }
			  else{
				  typeList.add(t);
			  }
			  typeMap.put(t.getTempType(),typeList);
		}
			
		for(String templateType:typeMap.keySet()){
			CacheGroup cacheGroup=CacheGroup.ELSE;
			List<TemplatePo> telmplateList=new ArrayList<TemplatePo>();
			telmplateList=typeMap.get(templateType);
			cacheGroup=getCacheGroupForTemplate(templateType); 
			
			Map<String, TemplatePo> templateMap = new HashMap<String, TemplatePo>();
			for(TemplatePo temp:list){
				templateMap.put(temp.getTempId(), temp);
			}
			String flag = getFlag();
			TemplateUtils.getCacheFromDB(cacheGroup, entId, telmplateList, templateMap, flag);
		}
		
	}
	/**
	 * 按企业、模版类型全量刷新缓存
	 * @param cacheGroup
	 * @throws ServiceException
	 */
	private void cacheTemplate(String entId,CacheGroup cacheGroup) throws ServiceException {
		TemplatePo po=new TemplatePo();
		 po.setTempType(getTemplateType(cacheGroup));
		List<TemplatePo> list=templateService.query(entId, po, null);
		Map<String, TemplatePo> templateMap = new HashMap<String, TemplatePo>();
		for(TemplatePo temp:list){
			templateMap.put(temp.getTempId(), temp);
		}
		String flag = getFlag();
		TemplateUtils.getCacheFromDB(cacheGroup, entId, list, templateMap, flag);
	}
	
	/**
	 * 全量刷新所有模版缓存
	 * @throws ServiceException
	 */
	private void cacheTemplateField() throws ServiceException {
		Map<String, Map<String,List<BaseFieldPo>>> map=fieldService.queryAllField();

		for(String entId : map.keySet()) {
			Map<String,List<BaseFieldPo>> entFieldMap=map.get(entId);
		
			List<BaseFieldPo> fieldList=new ArrayList<BaseFieldPo>();
			for(String templateType:entFieldMap.keySet()){
				CacheGroup cacheGroup=CacheGroup.ELSE;
				fieldList=entFieldMap.get(templateType);
				cacheGroup=getCacheGroupForTemplateField(templateType);
				
				Map<String,List<BaseFieldPo>> tempFieldMap=new HashMap<String,List<BaseFieldPo>>();
	            for(BaseFieldPo field:fieldList){
	            	 List<BaseFieldPo> tempList=tempFieldMap.get(field.getTempId());
					  if(tempList==null||tempList.size()==0){
						  tempList=new ArrayList<BaseFieldPo>();
					  }
					  tempList.add(field);
					  tempFieldMap.put(field.getTempId(),tempList);
	            }
				String flag = getFlag();
				TemplateFieldUtils.getCacheFromDB(cacheGroup, entId, fieldList, tempFieldMap, flag);
			}
			
		}
	}
	
	/**
	 * 全量刷新指定企业所有模版缓存
	 * @param entId
	 * @throws ServiceException
	 */
	private void cacheTemplateField(String entId) throws ServiceException {
		List<BaseFieldPo>  list=fieldService.query(entId, new BaseFieldPo(), null);
		 Map<String,List<BaseFieldPo>> typeMap=new HashMap<String,List<BaseFieldPo>>();
		 for(BaseFieldPo t:list){
			 List<BaseFieldPo> typeList=typeMap.get(t.getTempType());
			  if(typeList==null||typeList.size()==0){
				  typeList=new ArrayList<BaseFieldPo>();
			  }
			  else{
				  typeList.add(t);
			  }
			  typeMap.put(t.getTempType(),typeList);
		}
			
		for(String templateType:typeMap.keySet()){
			CacheGroup cacheGroup=CacheGroup.ELSE;
			List<BaseFieldPo> telmplateFieldList=new ArrayList<BaseFieldPo>();
			telmplateFieldList=typeMap.get(templateType);
			cacheGroup=getCacheGroupForTemplateField(templateType);
	
			
			Map<String,List<BaseFieldPo>> tempFieldMap=new HashMap<String,List<BaseFieldPo>>();
            for(BaseFieldPo field:telmplateFieldList){
            	 List<BaseFieldPo> tempList=tempFieldMap.get(field.getTempId());
				  if(tempList==null||tempList.size()==0){
					  tempList=new ArrayList<BaseFieldPo>();
				  }
				  tempList.add(field);
				  tempFieldMap.put(field.getTempId(),tempList);
            }

			String flag = getFlag();
			TemplateFieldUtils.getCacheFromDB(cacheGroup, entId, telmplateFieldList, tempFieldMap, flag);
		}
		
	}
	
	/**
	 * 全量刷新指定企业、模版类型所有模版缓存
	 * @param entId
	 * @param cacheGroup
	 * @throws ServiceException
	 */
	private void cacheTemplateField(String entId,CacheGroup cacheGroup) throws ServiceException {
		BaseFieldPo po=new BaseFieldPo();
		po.setTempType(getTemplateType(cacheGroup));
		List<BaseFieldPo>  telmplateFieldList=fieldService.query(entId, po, null);
		Map<String,List<BaseFieldPo>> tempFieldMap=new HashMap<String,List<BaseFieldPo>>();
        for(BaseFieldPo field:telmplateFieldList){
      	 List<BaseFieldPo> tempList=tempFieldMap.get(field.getTempId());
			  if(tempList==null||tempList.size()==0){
				  tempList=new ArrayList<BaseFieldPo>();
			  }
			  tempList.add(field);
			  tempFieldMap.put(field.getTempId(),tempList);
        }
		
		String flag = getFlag();
		TemplateFieldUtils.getCacheFromDB(cacheGroup, entId, telmplateFieldList, tempFieldMap, flag);
		
	}
}

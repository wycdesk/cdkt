package com.channelsoft.ems.redis.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;



import org.apache.commons.lang.StringUtils;

import com.channelsoft.ems.api.po.AgentSimplePo;
import com.channelsoft.ems.api.po.GroupSimplePo;
import com.channelsoft.ems.ent.po.DatEntInfoPo;
import com.channelsoft.ems.field.constants.FieldStatusEnum;
import com.channelsoft.ems.field.po.BaseFieldPo;
import com.channelsoft.ems.group.po.GroupPo;
import com.channelsoft.ems.redis.constant.CacheGroup;
import com.channelsoft.ems.redis.task.RefreshCacheThread;
import com.channelsoft.ems.template.constants.TemplateStatusEnum;
import com.channelsoft.ems.template.po.TemplatePo;
import com.channelsoft.ems.user.constant.UserType;
import com.channelsoft.ems.user.po.DatEntUserPo;

public class ParamUtils {
	/**
	 * 强制刷新缓存时调用此方法。
	 * @param group
	 * @param entId 企业id，如果为null则刷新所有企业的数据
	 * @CreateDate: 2013-8-6 下午06:27:57
	 * @author 魏铭
	 */
	public static void refreshCache(CacheGroup group, String entId) {
		RefreshCacheThread thread = new RefreshCacheThread(group, entId);
		thread.start();
	}
	/**
	 * 获取企业的用户列表
	 * @param entId 企业id
	 * @return 用户列表
	 * @author zhangtie
	 */
	public static List<DatEntUserPo> getEntUserList(String entId) {
		Map<String, List<DatEntUserPo>> map =  EntUserUtils.getList(entId);
		if (map == null) {
			return null;
		}
		List<DatEntUserPo> customerList = map.get(UserType.CUSTOMER.value);
		List<DatEntUserPo> serviceList = map.get(UserType.SERVICE.value);
		List<DatEntUserPo> adminList = map.get(UserType.ADMINISTRATOR.value);
		List<DatEntUserPo> allList = new ArrayList<DatEntUserPo>();
		allList.addAll(customerList);
		allList.addAll(serviceList);
		allList.addAll(adminList);
		return allList;
	}
	/**
	 * 获取指定企业的指定用户
	 * @param entId 企业id
	 * @param userId 用户ID
	 * @return 用户对象
	 * @author zhangtie
	 */
	public static DatEntUserPo getUser(String entId, String userId) {
		Map<String, DatEntUserPo> map = EntUserUtils.getMap(entId);
		if (map == null) {
			return null;
		}
		return map.get(userId);
	}
	/**
	 * 获取所有的企业信息
	 * @return 企业列表
	 * @author zhangtie
	 */
	public static List<DatEntInfoPo> getEntInfoList() {
		return EntInfoUtils.getList();
	}
	/**
	 * 根据ccodEntId获取指定的企业信息
	 * @return 企业对象
	 * @author jzft
	 */
	public static DatEntInfoPo getDatEntInfoPo(String ccodEntId){
		List<DatEntInfoPo> entList=EntInfoUtils.getList();
		for(int i=0;i<entList.size();i++){
			String ccodEntIdTmp=entList.get(i).getCcodEntId();
			if(StringUtils.isBlank(ccodEntIdTmp)){
				continue;
			}
			if(ccodEntIdTmp.equals(ccodEntId)){
				return entList.get(i);
			}
		}
		return null;
	}
	/**
	 * 获取指定企业的信息
	 * @param id 企业id
	 * @return 企业对象
	 * @author zhangtie
	 */
	public static DatEntInfoPo getEntInfo(String id) {
		return EntInfoUtils.getMap().get(id);
	}
	/**
	 * 获取指定企业的客服组列表
	 * @param entId 企业id
	 * @return 客服组列表
	 * @author zhangtie
	 */
	public static List<GroupPo> getEntGroupList(String entId) {
		return GroupUtils.getGroupMap().get(entId);
	}
	/**
	 * 获取指定企业的指定客服组
	 * @param entId 企业id
	 * @param groupId 客服组id
	 * @return 客服组对象
	 * @author zhangtie
	 */
	public static GroupPo getGroup(String entId, String groupId) {
		Map<String, GroupPo> map = GroupUtils.getMap(entId);
		if (map == null) {
			return null;
		}
		return map.get(groupId);
	}
	/**
	 * 获取指定企业的客服组-客服关系
	 * @param entId 企业id
	 * @return 客服组-客服关系映射 Map&lt;groupId, 客服列表&gt;
	 * @author zhangtie
	 */
	public static Map<String, List<AgentSimplePo>> getEntGroupAgentList(String entId) {
		return GroupAgentUtils.getList(entId);
	}
	
	/**
	 * 获取指定企业、指定客服组id的客服列表
	 * @param entId 企业id
	 * @param groupId 客服组id
	 * @return 客服列表
	 * @author zhangtie
	 */
	public static List<AgentSimplePo> getEntGroupAgentList(String entId, String groupId) {
		Map<String, List<AgentSimplePo>> map = GroupAgentUtils.getList(entId);
		if (map == null) {
			return null;
		}
		return map.get(groupId);
	}

	
	/**
	 * 该方法提供给子系统的定时任务调用，从缓存中获取数据放入内存
	 * 
	 * @CreateDate: 2013-8-8 下午05:48:01
	 * @author 魏铭
	 */
	public static void getAllFromCache() {

	}
	
	/**
	 * 获取指定企业的客服组，客服组里面包含对应客服列表
	 * @param entId
	 * @return
	 */
	public static List<GroupSimplePo> getEntGroupAndAgent(String entId) {
		Map<String, List<AgentSimplePo>> map = GroupAgentUtils.getList(entId);
		List<GroupSimplePo> list=new ArrayList<GroupSimplePo>();
		if (map == null) {
		}
		else{
			for(String groupId :map.keySet()){
				GroupPo group=ParamUtils.getGroup(entId, groupId);
				GroupSimplePo g=new GroupSimplePo();
				g.setGroupId(groupId);
				g.setGroupName(group.getGroupName());
				g.setAgentList(map.get(groupId));
				list.add(g);
			}
		}
		return list;
	}
	/**
	 * 获取指定企业的客服组，客服组里面包含对应客服列表
	 * @param entId
	 * @return
	 */
	public static List<GroupSimplePo> getEntGroupAndAgent(String entId, String groupId) {
		Map<String, List<AgentSimplePo>> map = GroupAgentUtils.getList(entId);
		List<GroupSimplePo> list=new ArrayList<GroupSimplePo>();
		if (map == null) {
		}
		else{
			GroupPo group=ParamUtils.getGroup(entId, groupId);
			GroupSimplePo g=new GroupSimplePo();
			g.setGroupId(groupId);
			g.setGroupName(group.getGroupName());
			g.setAgentList(map.get(groupId));
			list.add(g);
		}
		return list;
	}
	
	
	/**
	 * 获取所有座席
	 * @param entId
	 * @return
	 */
	public static List<DatEntUserPo> getEntAgentList(String entId) {
		Map<String, List<DatEntUserPo>> map = EntUserUtils.getList(entId);
		if (map == null) {
			return null;
		}
		List<DatEntUserPo> serviceList = map.get(UserType.SERVICE.value);
		List<DatEntUserPo> adminList = map.get(UserType.ADMINISTRATOR.value);
		List<DatEntUserPo> allList = new ArrayList<DatEntUserPo>();
		allList.addAll(serviceList);
		allList.addAll(adminList);
		return allList;
	}
    /**
     * 根据ID获取客服
     * @param userId
     * @param entId
     * @return
     */
	public static DatEntUserPo getAgentById(String userId,String entId){
		List<DatEntUserPo>  list=getEntUserList(entId);
		for(DatEntUserPo a:list){
			if(a.getUserId().equals(userId)){
				return a;	
			}
		}
		return null;
	}
    /**
     * 查询所有的模版，包括停用和删除的
     * @param entId
     * @param cacheGroup
     * @return
     */
	public static List<TemplatePo> getAllTemplateList(String entId,CacheGroup cacheGroup){
		return TemplateUtils.getList(entId, cacheGroup);
	}
	/**
	 * 查询指定企业指定模版类型启用的模版
	 * @param entId
	 * @param cacheGroup
	 * @return
	 */
	public static List<TemplatePo> getTemplateList(String entId,CacheGroup cacheGroup){
		List<TemplatePo> list=TemplateUtils.getList(entId, cacheGroup);
		if(list==null){
			list=new ArrayList<TemplatePo>();
		}
		List<TemplatePo> normalList=new ArrayList<TemplatePo>();
		for(TemplatePo t:list){
			if(TemplateStatusEnum.NORMAL.value.equals(t.getStatus())){
				normalList.add(t);
			}
		}
		return normalList;
	}
	/**
	 * 获取指定企业的指定模版类型的指定模版编号模版
	 * @param entId
	 * @param cacheGroup
	 * @param templateId
	 * @return
	 */
	public static TemplatePo getTemplate(String entId,CacheGroup cacheGroup,String templateId){
		Map<String, TemplatePo> map=TemplateUtils.getMap(entId, cacheGroup);
		if(map.containsKey(templateId)){
			return map.get(templateId);
		}
		return null;
	}
	/**
	 * 获取指定企业的指定模版类型的默认模版
	 * @param entId
	 * @param cacheGroup
	 * @return
	 */
	public static TemplatePo getDefaultTemplate(String entId,CacheGroup cacheGroup){
		Map<String, TemplatePo> map=TemplateUtils.getMap(entId, cacheGroup);
		for(String templateId:map.keySet()){
			if(map.get(templateId).isDefault()){
				return map.get(templateId);
			}
		}
		return null;
	}
	/**
	 * 获取指定企业指定模版所有字段,包括删除和停用的字段
	 * @param entId
	 * @param cacheGroup
	 * @param templateId
	 * @return
	 */
	public static List<BaseFieldPo> getTemplateAllFieldList(String entId,CacheGroup cacheGroup,String templateId){
		Map<String, List<BaseFieldPo>> map=TemplateFieldUtils.getMap(entId, cacheGroup);
		if(map.containsKey(templateId)){
			return map.get(templateId);
		}
		return new ArrayList<BaseFieldPo>();
	}
	/**
	 * 获取指定企业指定模版启用字段
	 * @param entId
	 * @param cacheGroup
	 * @param templateId
	 * @return
	 */
	public static List<BaseFieldPo> getTemplateFieldList(String entId,CacheGroup cacheGroup,String templateId){
		List<BaseFieldPo> list=getTemplateAllFieldList(entId,cacheGroup,templateId);
		List<BaseFieldPo> mormalList=new ArrayList<BaseFieldPo>();
		for(BaseFieldPo t:list){
			if(FieldStatusEnum.NORMAL.value.equals(t.getStatus())){
				mormalList.add(t);
			}
		}
		return mormalList;
	}
	/**
	 * 获取指定企业指定模版字段
	 * @param entId
	 * @param cacheGroup
	 * @param templateId
	 * @param isDefault 默认字段还是自定义
	 * @return
	 */
	public static List<BaseFieldPo> getTemplateFieldList(String entId,CacheGroup cacheGroup,String templateId,boolean isDefault){
		List<BaseFieldPo> list=getTemplateAllFieldList(entId,cacheGroup,templateId);
		List<BaseFieldPo> mormalList=new ArrayList<BaseFieldPo>();
		for(BaseFieldPo t:list){
			if(FieldStatusEnum.NORMAL.value.equals(t.getStatus())&&(isDefault==t.getIsDefault())){
				mormalList.add(t);
			}
		}
		return mormalList;
	}
	
}

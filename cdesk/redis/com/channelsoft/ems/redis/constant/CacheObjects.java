package com.channelsoft.ems.redis.constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.data.mongodb.core.query.Query;

import com.channelsoft.cri.logger.constant.LoggerType;
import com.channelsoft.cri.mongo.BaseMongoTemplate;
import com.channelsoft.cri.util.ColletionName;
import com.channelsoft.cri.util.GetCollectionFromEntInfo;
import com.channelsoft.ems.api.po.AgentSimplePo;
import com.channelsoft.ems.ent.po.DatEntInfoPo;
import com.channelsoft.ems.group.po.GroupPo;
import com.channelsoft.ems.redis.util.EntInfoUtils;
import com.channelsoft.ems.redis.util.EntUserUtils;
import com.channelsoft.ems.redis.util.GroupAgentUtils;
import com.channelsoft.ems.redis.util.GroupUtils;
import com.channelsoft.ems.redis.vo.CustomServicePo;
import com.channelsoft.ems.redis.vo.ServiceGroupPo;
import com.channelsoft.ems.search.po.FieldInfoPo;
import com.channelsoft.ems.user.po.DatEntUserPo;


public class CacheObjects {
	/**
	 * 用户
	 */
	private static Map<String, Map<String, List<DatEntUserPo>>> userList=getUserList(); // Map<entId, Map<userType, List<UserPo>>>
	private static Map<String, Map<String, DatEntUserPo>> userMap=getUserMap(); // Map<entId, Map<loginName, UserPo>>
	private static Map<String, String> userFlag=getUserFlag();
	public static Map<String, Map<String, List<DatEntUserPo>>> getUserList() {
		Map<String, Map<String, List<DatEntUserPo>>> userList=new HashMap<String, Map<String, List<DatEntUserPo>>>();
		Map<String, String> flags = EntUserUtils.getFlags();
		for (String entId : flags.keySet()) {
			userList.put(entId, EntUserUtils.getList(entId));
		}
		return userList;
	}
	public static void setUserList(Map<String, Map<String, List<DatEntUserPo>>> userList) {
		CacheObjects.userList = userList;
	}
	public static Map<String, Map<String, DatEntUserPo>> getUserMap() {
		Map<String, Map<String, DatEntUserPo>>  userMap=new HashMap<String, Map<String, DatEntUserPo>>();
		Map<String, String> flags = EntUserUtils.getFlags();
		for (String entId : flags.keySet()) {
			userMap.put(entId, EntUserUtils.getMap(entId));
		}
		return userMap;
	}
	public static void setUserMap(Map<String, Map<String, DatEntUserPo>> userMap) {
		CacheObjects.userMap = userMap;
	}
	public static Map<String, String> getUserFlag() {
		Map<String, String> userFlag=new HashMap<String, String>();
		userFlag=EntUserUtils.getFlags();
		return userFlag;
	}
	public static void setUserFlag(Map<String, String> userFlag) {
		CacheObjects.userFlag = userFlag;
	}
	/**
	 * 企业
	 */
	private static List<DatEntInfoPo> entList=getEntList();
	private static Map<String, DatEntInfoPo> entMap=getEntMap();
	private static String entFlag=getEntFlag();
	public static List<DatEntInfoPo> getEntList() {
		List<DatEntInfoPo> entList=new ArrayList<DatEntInfoPo>();
		entList=EntInfoUtils.getList();
		return entList;
	}
	public static void setEntList(List<DatEntInfoPo> entList) {
		CacheObjects.entList = entList;
	}
	public static Map<String, DatEntInfoPo> getEntMap() {
		Map<String, DatEntInfoPo>  entMap=new HashMap<String, DatEntInfoPo>();
		entMap=EntInfoUtils.getMap();
		return entMap;
	}
	public static void setEntMap(Map<String, DatEntInfoPo> entMap) {
		CacheObjects.entMap = entMap;
	}
	public static String getEntFlag() {
		String entFlag=EntInfoUtils.getFlag();
		return entFlag;
	}
	public static void setEntFlag(String entFlag) {
		CacheObjects.entFlag = entFlag;
	}
	/**
	 * 客服组
	 */
	private static Map<String, List<GroupPo>> groupList=getGroupList(); // Map<entId, List<GroupPo>>
	private static Map<String, Map<String, GroupPo>> groupMap=getGroupMap() ; // Map<entId, Map<groupId, GroupPo>>
	private static Map<String, String> groupFlag=getGroupFlag();
	public static Map<String, List<GroupPo>> getGroupList() {
		Map<String, List<GroupPo>> groupList=new HashMap<String, List<GroupPo>>();
		groupList=GroupUtils.getGroupMap();
		return groupList;
	}
	public static void setGroupList(Map<String, List<GroupPo>> groupList) {
		CacheObjects.groupList = groupList;
	}
	public static Map<String, Map<String, GroupPo>> getGroupMap() {
		Map<String, Map<String, GroupPo>> groupMap=new HashMap<String, Map<String, GroupPo>>();
		Map<String, String> flags = GroupUtils.getFlags();
		for (String entId : flags.keySet()) {
			groupMap.put(entId, GroupUtils.getMap(entId));
		}
		return groupMap;
	}
	public static void setGroupMap(Map<String, Map<String, GroupPo>> groupMap) {
		CacheObjects.groupMap = groupMap;
	}
	public static Map<String, String> getGroupFlag() {
		Map<String, String> groupFlag = GroupUtils.getFlags();
		return groupFlag;
	}
	public static void setGroupFlag(Map<String, String> groupFlag) {
		CacheObjects.groupFlag = groupFlag;
	}
	/**
	 * 客服组的客服
	 */
	private static Map<String, Map<String, List<AgentSimplePo>>> groupAgentList= getGroupAgentList(); // Map<entId, Map<groupId, List<DatUserSimplePo>>>
	private static Map<String, String> groupAgentFlag=getGroupAgentFlag();
	public static Map<String, Map<String, List<AgentSimplePo>>> getGroupAgentList() {
		Map<String, Map<String, List<AgentSimplePo>>> groupAgentList=new HashMap<String, Map<String, List<AgentSimplePo>>>();
		Map<String, String> flags = GroupAgentUtils.getFlags();
		for (String entId : flags.keySet()) {
			groupAgentList.put(entId, GroupAgentUtils.getList(entId));
		}
		return groupAgentList;
	}
	public static void setGroupAgentList(Map<String, Map<String, List<AgentSimplePo>>> groupAgentList) {
		CacheObjects.groupAgentList = groupAgentList;
	}
	public static Map<String, String> getGroupAgentFlag() {
		Map<String, String> groupAgentFlag = GroupAgentUtils.getFlags();
		return groupAgentFlag;
	}
	public static void setGroupAgentFlag(Map<String, String> groupAgentFlag) {
		CacheObjects.groupAgentFlag = groupAgentFlag;
	}
	
	
	/*工单*/
	private static BaseMongoTemplate template = new BaseMongoTemplate(); 
	private static final Logger logger = Logger.getLogger(LoggerType.SYSTEM.name);
	
	public static Map<String, HashMap<String,FieldInfoPo>> EntFieldMap = new HashMap<String,  HashMap<String,FieldInfoPo>>();
	public static Map<String, HashMap<String,CustomServicePo>> EntCustomMap= new HashMap<String, HashMap<String,CustomServicePo>>();
	public static Map<String, HashMap<String,ServiceGroupPo>> EntGroupMap = new HashMap<String, HashMap<String,ServiceGroupPo>>();
	
	public static HashMap<String,FieldInfoPo> getFieldMap(String entId){
		HashMap<String,FieldInfoPo> fieldMap = EntFieldMap.get(entId);
		if(fieldMap == null){
			fieldMap = new HashMap<String,FieldInfoPo>();
			getEntWorkFiledsFromDb(entId, fieldMap);
		}
		return fieldMap;
	}

	public static void getEntWorkFiledsFromDb(String entId, HashMap<String, FieldInfoPo> fieldMap) {
		logger.debug("初始化自定义字段查询....");
		Query query=new Query();
		String collectionName = GetCollectionFromEntInfo.getColletionName(entId, ColletionName.WORKFIELD.key);
		logger.debug("集合名称="+collectionName);
		List<FieldInfoPo> list=template.findList(query, collectionName,FieldInfoPo.class);
		if(list==null){
			return;
		}
		for(int i=0;i<list.size();i++){
			fieldMap.put(list.get(i).getKey(), list.get(i));
		}
		logger.info("初始化自定义字段结束....");
	}
}

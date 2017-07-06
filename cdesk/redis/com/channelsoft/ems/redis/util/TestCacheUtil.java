package com.channelsoft.ems.redis.util;

import java.util.List;
import java.util.Map;

import com.channelsoft.ems.api.po.AgentSimplePo;
import com.channelsoft.ems.ent.po.DatEntInfoPo;
import com.channelsoft.ems.field.po.BaseFieldPo;
import com.channelsoft.ems.group.po.GroupPo;
import com.channelsoft.ems.redis.constant.CacheGroup;
import com.channelsoft.ems.redis.constant.CacheObjects;
import com.channelsoft.ems.template.po.TemplatePo;
import com.channelsoft.ems.user.po.DatEntUserPo;

public class TestCacheUtil {

	public static void getCache(){

//		List<DatEntInfoPo>  entList=CacheObjects.getEntList();
//		Map<String, DatEntInfoPo>  entMap=CacheObjects.getEntMap();
//		Map<String, Map<String, List<DatEntUserPo>>> userList=CacheObjects.getUserList();
//		Map<String, Map<String, DatEntUserPo>> userMap=CacheObjects.getUserMap();
//		Map<String, List<GroupPo>> groupList= CacheObjects.getGroupList();
//	    Map<String, Map<String, GroupPo>> groupMap=CacheObjects.getGroupMap();
//	    Map<String, Map<String, List<AgentSimplePo>>>  groupAgentList=CacheObjects.getGroupAgentList();

		List<TemplatePo> allList=ParamUtils.getAllTemplateList("20160322", CacheGroup.WORK_TEMPLATE);
		List<TemplatePo> tempList=ParamUtils.getTemplateList("20160322", CacheGroup.WORK_TEMPLATE);
		TemplatePo t=ParamUtils.getTemplate("20160322", CacheGroup.WORK_TEMPLATE,"35");
		List<BaseFieldPo>  allFieldList=ParamUtils.getTemplateAllFieldList("20160322", CacheGroup.WORK_TEMPLATE_FIELD,"35");
		List<BaseFieldPo>  fieldList=ParamUtils.getTemplateAllFieldList("20160322", CacheGroup.WORK_TEMPLATE_FIELD,"35");
		List<BaseFieldPo>  defaultFieldList=ParamUtils.getTemplateFieldList("20160322", CacheGroup.WORK_TEMPLATE_FIELD,"35", true);
        System.out.println();
	}

}

package com.channelsoft.ems.field.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.channelsoft.ems.field.po.BaseFieldPo;
import com.channelsoft.ems.redis.constant.CacheGroup;
import com.channelsoft.ems.redis.util.ParamUtils;
import com.channelsoft.ems.template.po.TemplatePo;

public class FieldUtil {

	/**
	 * 获取工单中心默认模版需要展示字段
	 * @param entId
	 * @return
	 */
	public static List<BaseFieldPo> getOrderCenterTable(String entId) {
		TemplatePo template = ParamUtils.getDefaultTemplate(entId,
				CacheGroup.WORK_TEMPLATE);
		String templateId = "";
		if (template != null) {
			templateId = ParamUtils.getDefaultTemplate(entId,
					CacheGroup.WORK_TEMPLATE).getTempId();
		}
		return getOrderCenterTable(entId, templateId);

	}

	/**
	 * 获取工单中心指定模版需要展示字段
	 * @param entId
	 * @param templateId 工单编号可以为空，兼容历史数据，如果为空，字段描述定义写死
	 * @return
	 */
	public static List<BaseFieldPo> getOrderCenterTable(String entId,
			String templateId) {
		List<BaseFieldPo> list = ParamUtils.getTemplateFieldList(entId,
				CacheGroup.WORK_TEMPLATE_FIELD, templateId);
		Map<String, BaseFieldPo> map = new HashMap<String, BaseFieldPo>();
		for (BaseFieldPo t : list) {
			map.put(t.getKey(), t);
		}

		List<BaseFieldPo> fieldList = new ArrayList<BaseFieldPo>();
		BaseFieldPo workIdFiled = new BaseFieldPo();
		workIdFiled.setKey("workId");
		workIdFiled.setName("编号");
		fieldList.add(workIdFiled);

		BaseFieldPo titleFiled = new BaseFieldPo();
		titleFiled.setKey("title");
		if (StringUtils.isBlank(templateId)) {
			titleFiled.setName("标题");
		} else {
			titleFiled.setName(map.get("title").getName());
		}
		fieldList.add(titleFiled);

		BaseFieldPo statusFiled = new BaseFieldPo();
		statusFiled.setKey("status");
		if (StringUtils.isBlank(templateId)) {
			statusFiled.setName("工单状态");
		} else {
			statusFiled.setName(map.get("status").getName());
		}
		fieldList.add(statusFiled);

		BaseFieldPo customNameFiled = new BaseFieldPo();
		customNameFiled.setKey("customName");
		customNameFiled.setName("工单发起人");
		fieldList.add(customNameFiled);

		BaseFieldPo createDateFiled = new BaseFieldPo();
		createDateFiled.setKey("createDate");
		createDateFiled.setName("工单创建日期");
		fieldList.add(createDateFiled);

		BaseFieldPo serviceGroupNameFiled = new BaseFieldPo();
		serviceGroupNameFiled.setKey("serviceGroupName");
		if (StringUtils.isBlank(templateId)) {
			serviceGroupNameFiled.setName("受理客服组");
		} else {
			serviceGroupNameFiled
					.setName(map.get("serviceGroupName").getName());
		}
		fieldList.add(serviceGroupNameFiled);

		BaseFieldPo customServiceNameFiled = new BaseFieldPo();
		customServiceNameFiled.setKey("customServiceName");
		if (StringUtils.isBlank(templateId)) {
			customServiceNameFiled.setName("受理客服");
		} else {
			customServiceNameFiled.setName(map.get("customServiceName")
					.getName());
		}
		fieldList.add(customServiceNameFiled);

		BaseFieldPo templateNameFiled = new BaseFieldPo();
		templateNameFiled.setKey("tempName");
		templateNameFiled.setName("工单自定义字段");
		fieldList.add(templateNameFiled);

		return fieldList;
	}

	/**
	 * 
	 * 
	 * @Description: 获得默认的联络历史模版需要展示 的字段
	 * @author chenglitao
	 * @param    
	 * @date 2016年5月27日 上午9:54:44   
	 *
	 */
	public static List<BaseFieldPo> getCommHistoryTable(String entId) {
		TemplatePo template = ParamUtils.getDefaultTemplate(entId,
				CacheGroup.CONTACT_HISTORY_TEMPLATE);
		String templateId = "";
		if (template != null) {
			templateId = ParamUtils.getDefaultTemplate(entId,
					CacheGroup.CONTACT_HISTORY_TEMPLATE).getTempId();
		}
		return getCommHistoryTable(entId, templateId);

	}

	/**
	 * 
	 * 
	 * @Description: TODO 
	 * @author chenglitao
	 * @param    templateId 模版编号可以为空，兼容历史数据，如果为空，字段描述定义写死
	 * @date 2016年5月27日 下午2:32:38   
	 *
	 */
	public static List<BaseFieldPo> getCommHistoryTable(String entId,
			String templateId) {
		List<BaseFieldPo> list = ParamUtils.getTemplateFieldList(entId,
				CacheGroup.CONTACT_HISTORY_TEMPLATE_FIELD, templateId);
		Map<String, BaseFieldPo> map = new HashMap<String, BaseFieldPo>();
		for (BaseFieldPo t : list) {
			map.put(t.getKey(), t);
		}

		List<BaseFieldPo> fieldList = new ArrayList<BaseFieldPo>();
		BaseFieldPo workIdFiled = new BaseFieldPo();
		workIdFiled.setKey("workId");
		workIdFiled.setName("编号");
		fieldList.add(workIdFiled);

		BaseFieldPo titleFiled = new BaseFieldPo();
		titleFiled.setKey("title");
		if (StringUtils.isBlank(templateId)) {
			titleFiled.setName("标题");
		} else {
			titleFiled.setName(map.get("title").getName());
		}
		fieldList.add(titleFiled);

		BaseFieldPo statusFiled = new BaseFieldPo();
		statusFiled.setKey("status");
		if (StringUtils.isBlank(templateId)) {
			statusFiled.setName("工单状态");
		} else {
			statusFiled.setName(map.get("status").getName());
		}
		fieldList.add(statusFiled);

		BaseFieldPo customNameFiled = new BaseFieldPo();
		customNameFiled.setKey("customName");
		customNameFiled.setName("工单发起人");
		fieldList.add(customNameFiled);

		BaseFieldPo createDateFiled = new BaseFieldPo();
		createDateFiled.setKey("createDate");
		createDateFiled.setName("工单创建日期");
		fieldList.add(createDateFiled);

		BaseFieldPo serviceGroupNameFiled = new BaseFieldPo();
		serviceGroupNameFiled.setKey("serviceGroupName");
		if (StringUtils.isBlank(templateId)) {
			serviceGroupNameFiled.setName("受理客服组");
		} else {
			serviceGroupNameFiled
					.setName(map.get("serviceGroupName").getName());
		}
		fieldList.add(serviceGroupNameFiled);

		BaseFieldPo customServiceNameFiled = new BaseFieldPo();
		customServiceNameFiled.setKey("customServiceName");
		if (StringUtils.isBlank(templateId)) {
			customServiceNameFiled.setName("受理客服");
		} else {
			customServiceNameFiled.setName(map.get("customServiceName")
					.getName());
		}
		fieldList.add(customServiceNameFiled);

		BaseFieldPo templateNameFiled = new BaseFieldPo();
		templateNameFiled.setKey("tempName");
		templateNameFiled.setName("工单自定义字段");
		fieldList.add(templateNameFiled);

		return fieldList;
	}
}

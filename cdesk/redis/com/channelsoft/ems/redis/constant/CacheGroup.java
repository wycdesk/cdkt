package com.channelsoft.ems.redis.constant;

/**
 * 节点类型枚举类，提供getEnum方法
 * <dl>
 * <dt>demo</dt>
 * <dd>Description:</dd>
 * <dd>Copyright: Copyright (C) 2013</dd>
 * <dd>Company: 青牛(北京)技术有限公司 成都研究所</dd>
 * <dd>CreateDate: 2013-7-18</dd>
 * </dl>
 * @author 魏铭
 */
public enum CacheGroup {
	ENT_USER("21", "ENT_USER", "用户"),
	ENT_INFO("22", "ENT_INFO", "企业"),
	GROUP("23", "GROUP", "客服组"),
	GROUP_AGENT("24", "GROUP_AGENT", "客服组的客服"),
	ENT_USER_FIELD("25", "ENT_USER_FIELD", "用户自定义字段"),
	TEMPLATE("30", "TEMPLATE", "模版"),
	USER_TEMPLATE("31", "USER_TEMPLATE", "用户模版"),
	WORK_TEMPLATE("32", "WORK_TEMPLATE", "工单模版"),
	CONTACT_HISTORY_TEMPLATE("33", "CONTACT_HISTORY_TEMPLATE", "联络历史模版"),
	TEMPLATE_FIELD("60", "TEMPLATE_FIELD", "模版字段"),
	USER_TEMPLATE_FIELD("61", "USER_TEMPLATE_FIELD", "用户模版字段"),
	WORK_TEMPLATE_FIELD("62", "WORK_TEMPLATE_FIELD", "工单模版字段"),
	CONTACT_HISTORY_TEMPLATE_FIELD("63", "CONTACT_HISTORY_TEMPLATE_FIELD", "联络历史模版字段"),
	ELSE("", "", "未知");
	public String id;
	public String name;
	public String desc;
	private CacheGroup(String id, String name, String desc){
		this.id = id;
		this.name = name;
		this.desc = desc;
	}
	
	public static CacheGroup getEnum(String id){
		if (id == null) return null;
		for(CacheGroup type: CacheGroup.values()) {
			if (id.equals(type.id)){
				return type;
			}
		}
		return null;
	}
}

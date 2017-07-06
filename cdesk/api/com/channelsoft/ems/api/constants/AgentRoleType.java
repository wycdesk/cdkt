package com.channelsoft.ems.api.constants;

import org.apache.commons.lang.StringUtils;

public enum AgentRoleType {

	/**
	 * 班长坐席
	 */
	MANAGER("2", "班长坐席"),
	/**
	 * 普通坐席
	 */
	NORMAL("1", "普通坐席"),
	/**
	 * 无终端坐席
	 */
	NO_TERMINALS("3","无终端坐席"),
	/**
	 * 未知类型
	 */
	ELSE("","未知类型");
	public String value;
	public String desc;
	AgentRoleType(String value, String desc){
		this.value = value;
		this.desc = desc;
	}
	public static AgentRoleType getEnum(String value){
		if (value!=null)
		for(AgentRoleType e:values()){
			if(StringUtils.equalsIgnoreCase(e.value, value)){
				return e;
			}
		}
		return ELSE;
	}
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
}

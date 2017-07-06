package com.channelsoft.ems.template.constants;

import org.apache.commons.lang.StringUtils;

public enum TemplateTypeEnum {

	WORK("1","工单模板"),
	USER("2","用户模板"),
	COMMHISTORY("3","联络历史模板"),

	ELSE("","未知状态");
	public String value;
	public String desc;
	
	TemplateTypeEnum(String value, String desc){
		this.value = value;
		this.desc = desc;
	}
	public static TemplateTypeEnum getEnum(String value){
		if (value!=null)
		for(TemplateTypeEnum e:values()){
			if(StringUtils.equalsIgnoreCase(e.value, value)){
				return e;
			}
		}
		return ELSE;
	}
	
	public static TemplateTypeEnum getEnumByDesc(String desc){
		if (desc!=null)
		for(TemplateTypeEnum e:values()){
			if(StringUtils.equalsIgnoreCase(e.desc, desc)){
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

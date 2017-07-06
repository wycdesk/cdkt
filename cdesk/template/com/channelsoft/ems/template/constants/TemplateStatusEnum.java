package com.channelsoft.ems.template.constants;

import org.apache.commons.lang.StringUtils;

public enum TemplateStatusEnum {

	NORMAL("1","启用"),
	STOPPED("0","停用"),
	DELETE("9","删除"),

	ELSE("","未知状态");
	public String value;
	public String desc;
	
	TemplateStatusEnum(String value, String desc){
		this.value = value;
		this.desc = desc;
	}
	public static TemplateStatusEnum getEnum(String value){
		if (value!=null)
		for(TemplateStatusEnum e:values()){
			if(StringUtils.equalsIgnoreCase(e.value, value)){
				return e;
			}
		}
		return ELSE;
	}
	
	public static TemplateStatusEnum getEnumByDesc(String desc){
		if (desc!=null)
		for(TemplateStatusEnum e:values()){
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

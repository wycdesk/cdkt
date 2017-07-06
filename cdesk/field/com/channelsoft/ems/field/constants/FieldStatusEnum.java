package com.channelsoft.ems.field.constants;

import org.apache.commons.lang.StringUtils;

public enum FieldStatusEnum {

	NORMAL("1","启用"),
	STOPPED("0","停用"),
    DELETE("9","删除"),
	
	ELSE("","未知状态");
	public String value;
	public String desc;
	
	FieldStatusEnum(String value, String desc){
		this.value = value;
		this.desc = desc;
	}
	public static FieldStatusEnum getEnum(String value){
		if (value!=null)
		for(FieldStatusEnum e:values()){
			if(StringUtils.equalsIgnoreCase(e.value, value)){
				return e;
			}
		}
		return ELSE;
	}
	
	public static FieldStatusEnum getEnumByDesc(String desc){
		if (desc!=null)
		for(FieldStatusEnum e:values()){
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

package com.channelsoft.ems.department.constans;

import org.apache.commons.lang.StringUtils;

public enum DepartmentStatusEnum {
	NORMAL("1","正常"),
	DELETE("0","删除"),

	ELSE("","未知状态");
	public String value;
	public String desc;
	
	DepartmentStatusEnum(String value, String desc){
		this.value = value;
		this.desc = desc;
	}
	public static DepartmentStatusEnum getEnum(String value){
		if (value!=null)
		for(DepartmentStatusEnum e:values()){
			if(StringUtils.equalsIgnoreCase(e.value, value)){
				return e;
			}
		}
		return ELSE;
	}
	
	public static DepartmentStatusEnum getEnumByDesc(String desc){
		if (desc!=null)
		for(DepartmentStatusEnum e:values()){
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

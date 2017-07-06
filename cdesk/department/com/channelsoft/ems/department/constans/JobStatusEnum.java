package com.channelsoft.ems.department.constans;

import org.apache.commons.lang.StringUtils;

public enum JobStatusEnum {
	NORMAL("1","正常"),
	DELETE("0","删除"),

	ELSE("","未知状态");
	public String value;
	public String desc;
	
	JobStatusEnum(String value, String desc){
		this.value = value;
		this.desc = desc;
	}
	public static JobStatusEnum getEnum(String value){
		if (value!=null)
		for(JobStatusEnum e:values()){
			if(StringUtils.equalsIgnoreCase(e.value, value)){
				return e;
			}
		}
		return ELSE;
	}
	
	public static JobStatusEnum getEnumByDesc(String desc){
		if (desc!=null)
		for(JobStatusEnum e:values()){
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

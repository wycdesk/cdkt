package com.channelsoft.ems.log.constants;



import org.apache.commons.lang.StringUtils;


public enum LogTypeEnum {

	QUERY("1","查询"),
	ADD("2","创建"),
	UPDATE("3","修改"),
	DELETE("4","删除"),
	LOGIN("5","登录"),
	LOGOUT("6","登出"),
	ELSE("","未知状态");
	public String value;
	public String desc;
	
	LogTypeEnum(String value, String desc){
		this.value = value;
		this.desc = desc;
	}
	public static LogTypeEnum getEnum(String value){
		if (value!=null)
		for(LogTypeEnum e:values()){
			if(StringUtils.equalsIgnoreCase(e.value, value)){
				return e;
			}
		}
		return ELSE;
	}
	
	public static LogTypeEnum getEnumByDesc(String desc){
		if (desc!=null)
		for(LogTypeEnum e:values()){
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

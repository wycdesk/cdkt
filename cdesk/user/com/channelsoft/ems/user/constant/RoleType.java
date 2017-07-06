package com.channelsoft.ems.user.constant;

import org.apache.commons.lang.StringUtils;

public enum RoleType {
	NORMAL("4","普通客服"),MONITOR("5","客服班长"),ELSE("","未知");
	public String value;
	public String desc;
	private RoleType(String value,String desc){
		this.value=value;
		this.desc=desc;
	}
	public String getvalue() {
		return value;
	}
	public void setvalue(String value) {
		this.value = value;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public static RoleType getEnum(String value){
		if (value!=null)
			for(RoleType e:values()){
				if(StringUtils.equalsIgnoreCase(e.value, value)){
					return e;
				}
			}
			return ELSE;
	}
	
}


package com.channelsoft.ems.user.constant;

import org.apache.commons.lang.StringUtils;

public enum UserAccountType {
	CUSTOMER("1","客户"),SERVICE("2","客服"),ADMINISTRATOR("3","管理员"),ELSE("","未知");
	public String value;
	public String desc;
	private UserAccountType(String value,String desc){
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
	public static UserAccountType getEnum(String value){
		if (value!=null)
			for(UserAccountType e:values()){
				if(StringUtils.equalsIgnoreCase(e.value, value)){
					return e;
				}
			}
			return ELSE;
	}
	
}

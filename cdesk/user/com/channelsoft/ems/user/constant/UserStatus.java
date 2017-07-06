package com.channelsoft.ems.user.constant;

import org.apache.commons.lang.StringUtils;

public enum UserStatus {
	FORACTIVE("0","待激活"),NORMAL("1","正常"),PAUSED("2","暂停"),FORAUDIT("3","未核审"),
	STOPPED("4","停用"),DELETED("9","删除"),
	ELSE("","未知");
	public String value;
	public String desc;
	private UserStatus(String value,String desc){
		this.value=value;
		this.desc=desc;
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
	public static UserStatus getEnum(String value){
		if (value!=null)
			for(UserStatus e:values()){
				if(StringUtils.equalsIgnoreCase(e.value, value)){
					return e;
				}
			}
			return ELSE;
	}
}

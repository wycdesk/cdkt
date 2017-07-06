package com.channelsoft.ems.sso.constant;

import org.apache.commons.lang.StringUtils;

public enum Terminal {
	WEB("1","WEB端"),ANDROID_APP("2","ANDROID端"),IOS_APP("3","IOS端"),
	ELSE("","未知");
	public String value;
	public String desc;
	private Terminal(String value,String desc){
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
	public static Terminal getEnum(String value){
		if (value!=null)
			for(Terminal e:values()){
				if(StringUtils.equalsIgnoreCase(e.value, value)){
					return e;
				}
			}
			return ELSE;
	}
}

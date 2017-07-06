package com.channelsoft.ems.user.constant;

import org.apache.commons.lang.StringUtils;

public enum FounderType {
	FOUNDER("1","创始人"),ELSE("","未知");
	public String value;
	public String desc;
	private FounderType(String value,String desc){
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
	public static FounderType getEnum(String value){
		if (value!=null)
			for(FounderType e:values()){
				if(StringUtils.equalsIgnoreCase(e.value, value)){
					return e;
				}
			}
			return ELSE;
	}
	
}

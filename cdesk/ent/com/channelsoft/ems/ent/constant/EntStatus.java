package com.channelsoft.ems.ent.constant;

import org.apache.commons.lang.StringUtils;

public enum EntStatus {
	FORACTIVE("0","待激活"),NORMAL("1","商用"),TRY_USE("2","试用"),
	ELSE("","未知");
	public String value;
	public String desc;
	private EntStatus(String value,String desc){
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
	public static EntStatus getEnum(String value){
		if (value!=null)
			for(EntStatus e:values()){
				if(StringUtils.equalsIgnoreCase(e.value, value)){
					return e;
				}
			}
			return ELSE;
	}
}

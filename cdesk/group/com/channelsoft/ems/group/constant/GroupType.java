package com.channelsoft.ems.group.constant;

import org.apache.commons.lang.StringUtils;

public enum GroupType {
	CustomerService("1","客服分组"),IM("2","IM客服组"),ELSE("","未知");
	public String value;
	public String desc;
	GroupType(String value,String desc){
		this.value=value;
		this.desc=desc;
	}
	public static GroupType getEnum(String value){
		if (value!=null)
			for(GroupType e:values()){
				if(StringUtils.equalsIgnoreCase(e.value, value)){
					return e;
				}
			}
			return ELSE;
	}
}

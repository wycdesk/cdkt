package com.channelsoft.ems.communicate.constant;

import org.apache.commons.lang.StringUtils;

public enum ResultType {
	CONNECTED("1","是"),
	UNCONNECTED("0","否"),
	ELSE("","");
	
	public String value;
	public String desc;
	
	ResultType(String value,String desc){
		this.value=value;
		this.desc=desc;
	}
	
	public static ResultType getEnum(String value){
		if(StringUtils.isNotBlank(value)){
			for(ResultType e:values()){
				if(e.value.equalsIgnoreCase(value)){
					return e;
				}
			}
		}
		return ELSE;
	}

}

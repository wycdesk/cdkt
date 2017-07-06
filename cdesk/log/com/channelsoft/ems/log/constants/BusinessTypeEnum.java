package com.channelsoft.ems.log.constants;



import org.apache.commons.lang.StringUtils;


public enum BusinessTypeEnum {

	USER("1","用户"),
	USERGROUP("2","用户组"),
	WORK("3","工单"),
	SYSTEM("4","系统设置"),
	ROLE("5","角色"),
	WX("6","微信号"),
	ELSE("","未知状态");
	public String value;
	public String desc;
	
	BusinessTypeEnum(String value, String desc){
		this.value = value;
		this.desc = desc;
	}
	public static BusinessTypeEnum getEnum(String value){
		if (value!=null)
		for(BusinessTypeEnum e:values()){
			if(StringUtils.equalsIgnoreCase(e.value, value)){
				return e;
			}
		}
		return ELSE;
	}
	
	public static BusinessTypeEnum getEnumByDesc(String desc){
		if (desc!=null)
		for(BusinessTypeEnum e:values()){
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

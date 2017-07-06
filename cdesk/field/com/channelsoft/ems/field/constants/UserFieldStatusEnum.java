package com.channelsoft.ems.field.constants;



import org.apache.commons.lang.StringUtils;

/**
 * 自定义字段状态
 * @author wangyong
 *
 */
public enum UserFieldStatusEnum {

	NORMAL("1","启用"),
	STOPPED("0","停用"),

	ELSE("","未知状态");
	public String value;
	public String desc;
	
	UserFieldStatusEnum(String value, String desc){
		this.value = value;
		this.desc = desc;
	}
	public static UserFieldStatusEnum getEnum(String value){
		if (value!=null)
		for(UserFieldStatusEnum e:values()){
			if(StringUtils.equalsIgnoreCase(e.value, value)){
				return e;
			}
		}
		return ELSE;
	}
	
	public static UserFieldStatusEnum getEnumByDesc(String desc){
		if (desc!=null)
		for(UserFieldStatusEnum e:values()){
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

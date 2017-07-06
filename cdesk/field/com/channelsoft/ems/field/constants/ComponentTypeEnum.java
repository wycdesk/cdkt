package com.channelsoft.ems.field.constants;



import org.apache.commons.lang.StringUtils;


public enum ComponentTypeEnum {

	TEXT("1","文本框"),
	TEXT_AREA("2","文本区域"),
	SELECT("3","下拉菜单"),
	CHEKC_BOX("4","复选框"),
	RADIO("5","单选框"),
    NUMBER_INT("6","数字"),
    NUMBER_FLOAT("7","小数"),
    CUSTOMIZED("8","正则匹配字段"),
	
    PHONENUM("9","电话号"),
    
	ELSE("","未知状态");
	public String value;
	public String desc;
	
	ComponentTypeEnum(String value, String desc){
		this.value = value;
		this.desc = desc;
	}
	public static ComponentTypeEnum getEnum(String value){
		if (value!=null)
		for(ComponentTypeEnum e:values()){
			if(StringUtils.equalsIgnoreCase(e.value, value)){
				return e;
			}
		}
		return ELSE;
	}
	
	public static ComponentTypeEnum getEnumByDesc(String desc){
		if (desc!=null)
		for(ComponentTypeEnum e:values()){
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

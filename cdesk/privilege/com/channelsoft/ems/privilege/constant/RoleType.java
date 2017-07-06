package com.channelsoft.ems.privilege.constant;

import org.apache.commons.lang.StringUtils;
/**
 * 角色类型，分三大类
 * 
 * @author 王勇
 */
public enum RoleType
{
	CUSTOMER("1","客户"),
	SERVICE("2","客服"),
	ADMINISTRATOR("3","管理员"),
	ELSE("","未知");
	public String value;
	public String desc;
	RoleType(String value, String desc){
		this.value = value;
		this.desc = desc;
	}
	public static RoleType getEnum(String value){
		if (value!=null)
		for(RoleType e:values()){
			if(StringUtils.equalsIgnoreCase(e.value, value)){
				return e;
			}
		}
		return ELSE;
	}
}

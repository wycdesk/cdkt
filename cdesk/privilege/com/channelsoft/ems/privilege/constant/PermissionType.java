package com.channelsoft.ems.privilege.constant;

import org.apache.commons.lang.StringUtils;
/**
 * 权限类型
 * <dl>0=菜单，1=权限
 * <dt>portal</dt>
 * <dd>Description:</dd>
 * <dd>Copyright: Copyright (C) 2013</dd>
 * <dd>Company: 青牛(北京)技术有限公司 成都研究所</dd>
 * <dd>CreateDate: 2013-6-7</dd>
 * </dl>
 * @author 魏铭
 */
public enum PermissionType
{
	MENU("0", "菜单"),
	PRIVILAGE("1", "权限"),
	ELSE("","未知");
	public String value;
	public String desc;
	PermissionType(String value, String desc){
		this.value = value;
		this.desc = desc;
	}
	public static PermissionType getEnum(String value){
		if (value!=null)
		for(PermissionType e:values()){
			if(StringUtils.equalsIgnoreCase(e.value, value)){
				return e;
			}
		}
		return ELSE;
	}
}

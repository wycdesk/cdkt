package com.channelsoft.ems.privilege.constant;

import org.apache.commons.lang.StringUtils;
/**
 * 菜单可见标记
 * <dl>0=不可见，1=可见
 * <dt>portal</dt>
 * <dd>Description:</dd>
 * <dd>Copyright: Copyright (C) 2013</dd>
 * <dd>Company: 青牛(北京)技术有限公司 成都研究所</dd>
 * <dd>CreateDate: 2013-6-7</dd>
 * </dl>
 * @author 魏铭
 */
public enum VisibleFlag
{
	VISIBLE("1", "可见"),
	INVISIBLE("0", "不可见"),
	ELSE("","未知");
	public String value;
	public String desc;
	VisibleFlag(String value, String desc){
		this.value = value;
		this.desc = desc;
	}
	public static VisibleFlag getEnum(String value){
		if (value!=null)
		for(VisibleFlag e:values()){
			if(StringUtils.equalsIgnoreCase(e.value, value)){
				return e;
			}
		}
		return ELSE;
	}
}

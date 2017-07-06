package com.channelsoft.cri.logger.constant;

import org.apache.commons.lang.StringUtils;

public enum OperationType
{
	//运维日志以0开头
	DB_QUERY("0001", "查询数据"),
	DB_INSERT("0002", "插入数据"),
	DB_UPDATE("0003", "更新数据"),
	DB_DELETE("0004", "删除数据"),
	
	SYSTEM("0000", "系统后台操作"),
	API_REQUEST("0005", "发起接口请求"),
	API_RECEIVE("0006", "收到接口请求"),
	API_RESPONSE("0006", "处理接口请求"),
	//管理日志以1开头
	MANAGE_QUERY("1101", "查询"),
	MANAGE_ADD("1102", "添加"),
	MANAGE_EDIT("1103", "修改"),
	MANAGE_DELETE("1104", "删除"),
	
	LOG_IN("1001", "登入"),
	LOG_OUT("1002", "登出"),
	
	ELSE("","未知");
	public String value;
	public String desc;
	OperationType(String value, String desc){
		this.value = value;
		this.desc = desc;
	}
	public static OperationType getEnum(String value){
		if (value!=null)
		for(OperationType e:values()){
			if(StringUtils.equalsIgnoreCase(e.value, value)){
				return e;
			}
		}
		return ELSE;
	}
}

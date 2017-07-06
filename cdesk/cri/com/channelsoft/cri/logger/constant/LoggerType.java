package com.channelsoft.cri.logger.constant;

import org.apache.commons.lang.StringUtils;
/**
 * 基本日志类型枚举类
 * <dl>
 * <dt>portal</dt>
 * <dd>Description:</dd>
 * <dd>Copyright: Copyright (C) 2013</dd>
 * <dd>Company: 青牛(北京)技术有限公司 成都研究所</dd>
 * <dd>CreateDate: 2013-4-26</dd>
 * </dl>
 * @author 魏铭
 */
public enum LoggerType {
	//管理界面日志
	/**
	 * 管理平台登陆日志
	 */
	LOG_IN("1001", "com.channelsoft.portal.logger.login", "系统登陆日志", "login.log"),
	/**
	 * 管理员操作日志
	 */
	MANAGE("1002", "com.channelsoft.portal.logger.manage", "管理员操作日志", "manage.log"),
	
	
	//系统内部日志，每个子系统都会有
	/**
	 * 数据库操作日志
	 */
	DB("0001", "com.channelsoft.portal.logger.db", "数据库日志", "db.log"),
	/**
	 * 缓存操作日志
	 */
	CACHE("0004", "com.channelsoft.portal.logger.cache", "缓存日志", "cache.log"),
	/**
	 * 接口交互日志
	 */
	API("0003", "com.channelsoft.portal.logger.api", "接口日志", "cache.log"),
	/**
	 * 错误日志
	 */
	ERROR("0002", "com.channelsoft.portal.logger.error", "异常日志", "error.log"),
	/**
	 * 系统日志
	 */
	SYSTEM("9999", "com.channelsoft.portal.logger.system", "系统日志", "system.log");
	
    public String value;
    public String name;
	public String desc;
	public String file;
	LoggerType(String value, String name, String desc, String file){
		this.value = value;
		this.name = name;
		this.desc = desc;
		this.file = file;
	}
	
	public static LoggerType getEnum(String value){
		if (value!=null)
		for(LoggerType e:values()){
			if(StringUtils.equalsIgnoreCase(e.value, value)){
				return e;
			}
		}
		return SYSTEM;
	}
	
	public static LoggerType getEnumByFileName(String fileName){
		if (fileName!=null)
		for(LoggerType e:values()){
			if(e.file.startsWith(fileName)){
				return e;
			}
		}
		return SYSTEM;
	}
}

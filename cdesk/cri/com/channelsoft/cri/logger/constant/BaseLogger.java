package com.channelsoft.cri.logger.constant;

import org.apache.log4j.Logger;



/**
 * 系统运维日志操作类，其他日志类可继承此类。
 * <dl>
 * <dt>portal</dt>
 * <dd>Description:</dd>
 * <dd>Copyright: Copyright (C) 2013</dd>
 * <dd>Company: 青牛(北京)技术有限公司 成都研究所</dd>
 * <dd>CreateDate: 2013-4-26</dd>
 * </dl>
 * @author 魏铭
 */
public class BaseLogger {
	protected static Logger manage = Logger.getLogger(LoggerType.MANAGE.name);
	protected static Logger login = Logger.getLogger(LoggerType.LOG_IN.name);
	
	protected static Logger db = Logger.getLogger(LoggerType.DB.name);
	protected static Logger cache = Logger.getLogger(LoggerType.CACHE.name);
	protected static Logger api = Logger.getLogger(LoggerType.API.name);
	protected static Logger error = Logger.getLogger(LoggerType.ERROR.name);
	protected static Logger system = Logger.getLogger(LoggerType.SYSTEM.name);
	
	public void logTime(String message, long startTime) {
		if (system.isDebugEnabled()) {
			long timeCost = System.currentTimeMillis() - startTime;
			system.debug("操作耗时：" + message + (timeCost) + "毫秒");
		}
	}
}

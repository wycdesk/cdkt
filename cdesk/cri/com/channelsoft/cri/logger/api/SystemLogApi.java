package com.channelsoft.cri.logger.api;

import com.channelsoft.cri.exception.BaseException;
import com.channelsoft.cri.logger.logger.SystemLogger;
import com.channelsoft.cri.logger.vo.LogCaller;

/**
 * 系统日志接口类
 * <dl>
 * <dt>portal</dt>
 * <dd>Description:提供系统管理平台的管理日志接口</dd>
 * <dd>Copyright: Copyright (C) 2013</dd>
 * <dd>Company: 青牛(北京)技术有限公司 成都研究所</dd>
 * <dd>CreateDate: 2013-4-27</dd>
 * </dl>
 * @author 魏铭
 */
public class SystemLogApi {
	/**
	 * 系统操作成功
	 * @param platform
	 * @param operation
	 * @param objective
	 * @param caller
	 * @CreateDate: 2013-5-2 上午11:43:16
	 * @author 魏铭
	 */
	public static void Success(String platform, String operation, String objective, LogCaller caller)
	{
		SystemLogger.success(platform, operation, objective, caller);
	}
	/**
	 * 系统后台操作失败
	 * @param platform 平台
	 * @param operation 操作
	 * @param objective 操作对象
	 * @param exception 错误
	 * @param caller
	 * @CreateDate: 2013-5-2 下午01:00:49
	 * @author 魏铭
	 */
	public static void Fail(String platform, String operation, String objective, BaseException exception, LogCaller caller)
	{
		SystemLogger.fail(platform, operation, objective, exception, caller);
	}
	
	public static void Debug(String message, LogCaller caller) {
		SystemLogger.debug(message, caller);
	}
	
	public static void Info(String message, LogCaller caller) {
		SystemLogger.info(message, caller);
	}
}

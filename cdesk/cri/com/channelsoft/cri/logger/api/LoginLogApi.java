package com.channelsoft.cri.logger.api;

import com.channelsoft.cri.exception.BaseException;
import com.channelsoft.cri.logger.constant.OperationType;
import com.channelsoft.cri.logger.logger.LoginLogger;
import com.channelsoft.cri.logger.vo.LogCaller;

/**
 * 管理日志接口类
 * <dl>
 * <dt>portal</dt>
 * <dd>Description:提供系统管理平台的管理日志接口</dd>
 * <dd>Copyright: Copyright (C) 2013</dd>
 * <dd>Company: 青牛(北京)技术有限公司 成都研究所</dd>
 * <dd>CreateDate: 2013-4-27</dd>
 * </dl>
 * @author 魏铭
 */
public class LoginLogApi {
	/**
	 * 登陆成功
	 * @param operator 登陆人员
	 * @param platform 登陆平台
	 * @param caller 调用者
	 * @CreateDate: 2013-4-27 下午01:47:51
	 * @author 魏铭
	 */
	public static void LoginSuccess(String operator, String platform, LogCaller caller)
	{
		LoginLogger.success(operator, OperationType.LOG_IN, platform, caller);
	}
	/**
	 * @param operator 登陆人员
	 * @param platform 登陆平台
	 * @param exception 错误
	 * @param caller 调用者
	 * @CreateDate: 2013-4-27 下午01:47:51
	 * @author 魏铭
	 */
	public static void LoginFail(String operator, String platform, BaseException exception, LogCaller caller)
	{
		LoginLogger.fail(operator, OperationType.LOG_IN, platform, exception, caller);
	}
	
	/**
	 * 登出成功
	 * @param operator 登陆人员
	 * @param platform 登陆平台
	 * @param caller 调用者
	 * @CreateDate: 2013-4-27 下午01:47:51
	 * @author 魏铭
	 */
	public static void LogoutSuccess(String operator, String platform, LogCaller caller)
	{
		LoginLogger.success(operator, OperationType.LOG_OUT, platform, caller);
	}
	/**
	 * 登出失败
	 * @param operator 登陆人员
	 * @param platform 登陆平台
	 * @param exception 错误
	 * @param caller 调用者
	 * @CreateDate: 2013-4-27 下午01:47:51
	 * @author 魏铭
	 */
	public static void LogoutFail(String operator, String platform, BaseException exception, LogCaller caller)
	{
		LoginLogger.fail(operator, OperationType.LOG_OUT, platform, exception, caller);
	}
}

package com.channelsoft.cri.logger;

import com.channelsoft.cri.exception.BaseException;
import com.channelsoft.cri.logger.api.LoginLogApi;
import com.channelsoft.cri.logger.vo.LogCaller;

public class LoginLogUtils {
	/**
	 * 登陆成功
	 * @param operator
	 * @param platform
	 * @CreateDate: 2013-4-27 下午06:51:18
	 * @author 魏铭
	 */
	public static void LoginSuccess(String operator, String platform) {
		LoginLogApi.LoginSuccess(operator, platform, new LogCaller());
	}
	/**
	 * 登陆失败
	 * @param operator
	 * @param platform
	 * @param exception
	 * @CreateDate: 2013-4-27 下午06:51:13
	 * @author 魏铭
	 */
	public static void LoginFail(String operator, String platform, BaseException exception) {
		LoginLogApi.LoginFail(operator, platform, exception, new LogCaller());
	}
	/**
	 * 登出成功
	 * @param operator
	 * @param platform
	 * @CreateDate: 2013-4-27 下午06:51:09
	 * @author 魏铭
	 */
	public static void LogoutSuccess(String operator, String platform) {
		LoginLogApi.LogoutSuccess(operator, platform, new LogCaller());
	}
	/**
	 * 登出失败
	 * @param operator
	 * @param platform
	 * @param exception
	 * @CreateDate: 2013-4-27 下午06:51:05
	 * @author 魏铭
	 */
	public static void LogoutFail(String operator, String platform, BaseException exception) {
		LoginLogApi.LogoutFail(operator, platform, exception, new LogCaller());
	}
}

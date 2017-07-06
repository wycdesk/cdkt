package com.channelsoft.cri.logger;

import com.channelsoft.cri.constant.BaseConstants;
import com.channelsoft.cri.exception.BaseException;
import com.channelsoft.cri.logger.api.SystemLogApi;
import com.channelsoft.cri.logger.vo.LogCaller;

public class SystemLogUtils {
	/**
	 * 操作成功日志
	 * @param operator
	 * @param operation
	 * @param objective
	 * @CreateDate: 2013-5-2 上午11:59:58
	 * @author 魏铭
	 */
	public static void Success(String operator, String operation, String objective) {
		SystemLogApi.Success(BaseConstants.PLATFORM_ID_UPPER(), operation, objective, new LogCaller());
	}
	/**
	 * 操作失败日志
	 * @param operator
	 * @param operation
	 * @param objective
	 * @param exception
	 * @CreateDate: 2013-5-2 下午12:00:04
	 * @author 魏铭
	 */
	public static void Fail(String operator, String operation, String objective, BaseException exception) {
		SystemLogApi.Fail(operator, operation, objective, exception, new LogCaller());
	}
	
	public static void Debug(String message) {
		SystemLogApi.Debug(message, new LogCaller());
	}
}

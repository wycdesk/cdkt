package com.channelsoft.cri.logger.logger;

import com.channelsoft.cri.exception.BaseException;
import com.channelsoft.cri.logger.api.SystemLogApi;
import com.channelsoft.cri.logger.constant.BaseLogger;
import com.channelsoft.cri.logger.constant.OperationType;
import com.channelsoft.cri.logger.vo.LogCaller;
import com.channelsoft.cri.logger.vo.BaseLogVo;

public class SystemLogger extends BaseLogger{
	/**
	 * 操作成功
	 * @param source 请求方
	 * @param operation 操作类型
	 * @param dest 被请求方
	 * @param caller 调用者
	 * @CreateDate: 2013-4-26 下午06:03:42
	 * @author 魏铭
	 */
	public static void success(String source, String operation, String dest, LogCaller caller)
	{
		BaseLogVo vo = new BaseLogVo();
		vo.setClassName(caller.getClassName());
		vo.setMethod(caller.getMethodName());
		vo.setSource(source);
		vo.setOperation(OperationType.SYSTEM.value);
		vo.setDest(dest);
		vo.setResultCode("0");
		try {
			if (system.isDebugEnabled()) {
				system.debug(caller.getHeader() + "[" + source + "]" +  operation + "[" + dest + "]成功。");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 操作成功
	 * @param source
	 * @param operation
	 * @param dest
	 * @param caller
	 * @CreateDate: 2013-5-2 下午01:06:51
	 * @author 魏铭
	 */
	public static void success(String source, OperationType operation, String dest, String objective, LogCaller caller)
	{
		try {
			if (system.isDebugEnabled()) {
				system.debug(caller.getHeader() + "[" + source + "]" +  operation.desc + "[" + dest + "]["+ objective +"]成功。");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 操作失败
	 * @param source 请求方
	 * @param operation 操作类型
	 * @param dest 被请求方
	 * @param exception 错误
	 * @param caller 调用者
	 * @CreateDate: 2013-4-26 下午06:16:16
	 * @author 魏铭
	 */
	public static void fail(String source, OperationType operation, String dest, String objective, BaseException exception, LogCaller caller)
	{
		BaseLogVo vo = new BaseLogVo();
		
		vo.setClassName(caller.getClassName());
		vo.setMethod(caller.getMethodName());
		vo.setSource(source);
		vo.setOperation(operation.value);
		vo.setDest(dest);
		vo.setResultCode(exception.getInnerCode());
		vo.setDescription(exception.getMessage());
		try {
			error.error(caller.getHeader() + "[" + source + "]" +  operation.desc + "[" + dest + "]["+ objective +"]失败："+ exception.getErrorLog());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 系统操作失败
	 * @param source 请求方
	 * @param operation 操作类型
	 * @param dest 被请求方
	 * @param exception 错误
	 * @param caller 调用者
	 * @CreateDate: 2013-4-26 下午06:16:16
	 * @author 魏铭
	 */
	public static void fail(String source, String operation, String dest, BaseException exception, LogCaller caller)
	{
		BaseLogVo vo = new BaseLogVo();
		
		vo.setClassName(caller.getClassName());
		vo.setMethod(caller.getMethodName());
		vo.setSource(source);
		vo.setOperation(OperationType.SYSTEM.value);
		vo.setDest(dest);
		vo.setResultCode(exception.getInnerCode());
		vo.setDescription(exception.getMessage());
		try {
			error.error(caller.getHeader() + "[" + source + "]" +  operation + "[" + dest + "]失败："+ exception.getErrorLog());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void debug(String message, LogCaller caller) {
		if (system.isDebugEnabled()) {
			system.debug(caller.getHeader() + message);
		}
	}
	
	public static void info(String message, LogCaller caller) {
		if (system.isInfoEnabled()) {
			system.info(caller.getHeader() + message);
		}
	}
	public static void error(String message, LogCaller caller) {

		error.error(caller.getHeader() + message);
	}
	public static void error(String message) {
		error.error(message);

	}

}

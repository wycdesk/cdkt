package com.channelsoft.cri.logger.logger;

import com.channelsoft.cri.exception.BaseException;
import com.channelsoft.cri.logger.constant.BaseLogger;
import com.channelsoft.cri.logger.constant.OperationType;
import com.channelsoft.cri.logger.vo.LogCaller;
import com.channelsoft.cri.logger.vo.LoginLogVo;
import com.channelsoft.cri.util.JsonUtils;

public class LoginLogger extends BaseLogger{
	/**
	 * 登陆操作成功
	 * @param source 请求方
	 * @param operation 操作类型
	 * @param dest 被请求方
	 * @param caller 调用者
	 * @CreateDate: 2013-4-26 下午06:03:42
	 * @author 魏铭
	 */
	public static void success(String source, OperationType operation, String dest, LogCaller caller)
	{
		LoginLogVo vo = new LoginLogVo();
		vo.setClassName(caller.getClassName());
		vo.setMethod(caller.getMethodName());
		vo.setSource(source);
		vo.setOperation(operation.value);
		vo.setDest(dest);
		vo.setResultCode("0");
		try {
			if (system.isDebugEnabled()) {
				system.debug(caller.getHeader() + "系统用户[" + source + "]" +  operation.desc + "[" + dest + "]平台成功。");
			}
			if (login.isInfoEnabled()) {
				login.info(JsonUtils.toJson(vo));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 登陆操作失败
	 * @param source 请求方
	 * @param operation 操作类型
	 * @param dest 被请求方
	 * @param exception 错误
	 * @param caller 调用者
	 * @CreateDate: 2013-4-26 下午06:16:16
	 * @author 魏铭
	 */
	public static void fail(String source, OperationType operation, String dest, BaseException exception, LogCaller caller)
	{
		LoginLogVo vo = new LoginLogVo();
		
		vo.setClassName(caller.getClassName());
		vo.setMethod(caller.getMethodName());
		vo.setSource(source);
		vo.setOperation(operation.value);
		vo.setDest(dest);
		vo.setResultCode(exception.getInnerCode());
		vo.setDescription(exception.getMessage());
		try {
			error.error(caller.getHeader() + "系统用户[" + source + "]" +  operation.desc + "[" + dest + "]平台失败："+ exception.getErrorLog());
			if (login.isInfoEnabled()) {
				login.info(JsonUtils.toJson(vo));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

/**
 * FileName: BaseObject.java
 */
package com.channelsoft.cri.common;

import com.channelsoft.cri.exception.BaseException;
import com.channelsoft.cri.logger.api.SystemLogApi;
import com.channelsoft.cri.logger.vo.LogCaller;

/**
 * 基类对象
 * <dl>
 * <dt>portal</dt>
 * <dd>Description:</dd>
 * <dd>Copyright: Copyright (C) 2013</dd>
 * <dd>Company: 青牛(北京)技术有限公司 成都研究所</dd>
 * <dd>CreateDate: 2013-4-26</dd>
 * </dl>
 * @author 魏铭
 */
public class BaseObject
{
	/**
	 * 操作成功日志
	 * @param operator
	 * @param operation
	 * @param objective
	 * @CreateDate: 2013-5-2 上午11:59:58
	 * @author 魏铭
	 */
	protected void logSuccess(String operator, String operation, String objective) {
		SystemLogApi.Success(operator, operation, objective, new LogCaller());
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
	protected void logFail(String operator, String operation, String objective, BaseException exception) {
		SystemLogApi.Fail(operator, operation, objective, exception, new LogCaller());
	}
	
	protected void logDebug(String message) {
		SystemLogApi.Debug(message, new LogCaller());
	}
}

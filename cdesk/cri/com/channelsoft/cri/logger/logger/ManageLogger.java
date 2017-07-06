package com.channelsoft.cri.logger.logger;

import com.channelsoft.cri.exception.BaseException;
import com.channelsoft.cri.logger.constant.BaseLogger;
import com.channelsoft.cri.logger.constant.OperationType;
import com.channelsoft.cri.logger.vo.LogCaller;
import com.channelsoft.cri.logger.vo.ManageLogVo;
import com.channelsoft.cri.util.JsonUtils;

public class ManageLogger extends BaseLogger{
	/**
	 * 操作成功
	 * @param source 请求方
	 * @param operation 操作类型
	 * @param dest 被请求方
	 * @param business 业务类型
	 * @param objective 被操作对象
	 * @param remark 对象描述，格式为：aaa=a|bbb=b
	 * @param caller 调用者
	 * @CreateDate: 2013-4-26 下午06:03:42
	 * @author 魏铭
	 */
	public static void success(String source, OperationType operation, String dest, String business, String objective, String remark, LogCaller caller)
	{
		ManageLogVo vo = new ManageLogVo();
		vo.setClassName(caller.getClassName());
		vo.setMethod(caller.getMethodName());
		vo.setSource(source);
		vo.setOperation(operation.value);
		vo.setDest(dest);
		vo.setResultCode("0");
		
		vo.setBusiness(business);
		vo.setObjective(objective);
		vo.setRemark(remark);
		try {
			if (system.isDebugEnabled()) {
				system.debug(caller.getHeader() + "系统用户[" + source + "]在[" + dest + "]平台" + operation.desc + "[" + business + "]成功。");
			}
			if (OperationType.MANAGE_QUERY.equals(operation))
			{
				if (manage.isDebugEnabled()) {
					manage.debug(JsonUtils.toJson(vo));
				}
			}
			else
			{
				if (manage.isInfoEnabled()) {
					manage.info(JsonUtils.toJson(vo));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 管理操作失败
	 * @param source 请求方
	 * @param operation 操作类型
	 * @param dest 被请求方
	 * @param exception 错误
	 * @param business 业务类型
	 * @param objective 被操作对象
	 * @param remark 对象描述，格式为：aaa=a|bbb=b
	 * @param caller 调用者
	 * @CreateDate: 2013-4-26 下午06:16:16
	 * @author 魏铭
	 */
	public static void fail(String source, OperationType operation, String dest, BaseException exception, String business, String objective, String remark, LogCaller caller)
	{
		ManageLogVo vo = new ManageLogVo();
		
		vo.setClassName(caller.getClassName());
		vo.setMethod(caller.getMethodName());
		vo.setSource(source);
		vo.setOperation(operation.value);
		vo.setDest(dest);
		vo.setResultCode(exception.getInnerCode());
		vo.setDescription(exception.getMessage());
		
		vo.setBusiness(business);
		vo.setObjective(objective);
		vo.setRemark(remark);
		try {
			error.error(caller.getHeader()  + "系统用户[" + source + "]在[" + dest + "]平台" + operation.desc + "[" + business + "]失败，对象:" + objective + "，"+ exception.getErrorLog());
			if (manage.isInfoEnabled()) {
				manage.info(JsonUtils.toJson(vo));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

package com.channelsoft.cri.logger.logger;

import com.channelsoft.cri.logger.constant.BaseLogger;
import com.channelsoft.cri.logger.constant.OperationType;
import com.channelsoft.cri.logger.vo.DBLogVo;
import com.channelsoft.cri.logger.vo.LogCaller;
import com.channelsoft.cri.util.JsonUtils;

public class DBLogger extends BaseLogger{
	/**
	 * 数据库操作成功日志
	 * @param operation 操作类型
	 * @param sql sql语句
	 * @param type sql查询方式
	 * @param resultCode 查询结果数 或 查询的数字结果
	 * @param caller 调用者
	 * @CreateDate: 2013-4-26 下午04:45:41
	 * @author 魏铭
	 */
	public static void success(OperationType operation, String sql, String type, String resultCode, LogCaller caller)
	{
		DBLogVo vo = new DBLogVo();
		vo.setClassName(caller.getClassName());
		vo.setMethod(caller.getMethodName());
		vo.setOperation(operation.value);
		vo.setSql(sql);
		vo.setType(type);
		vo.setResultCode(resultCode);
		try {
			if (system.isDebugEnabled()) {
				system.debug(caller.getHeader() + operation.desc +"[method="+ type +"]成功，结果=" + resultCode);
			}
			if (db.isInfoEnabled()) {
				db.info(JsonUtils.toJson(vo));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 数据库操作失败日志
	 * @param operation 操作类型
	 * @param sql sql语句
	 * @param type sql查询方式
	 * @param caller 调用者
	 * @CreateDate: 2013-4-26 下午04:36:57
	 * @author 魏铭
	 */
	public static void fail(OperationType operation, String sql, String type, LogCaller caller)
	{
		DBLogVo vo = new DBLogVo();
		vo.setClassName(caller.getClassName());
		vo.setMethod(caller.getMethodName());
		vo.setOperation(operation.value);
		vo.setSql(sql);
		vo.setType(type);
		vo.setResultCode("-1");
		try {
			error.error(caller.getHeader() + operation.desc +"[method="+ type +"]失败，sql=[" + sql + "]");
			if (db.isInfoEnabled()) {
				db.info(JsonUtils.toJson(vo));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

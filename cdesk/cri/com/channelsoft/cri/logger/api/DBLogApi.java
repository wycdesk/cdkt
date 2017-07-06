package com.channelsoft.cri.logger.api;

import com.channelsoft.cri.logger.constant.OperationType;
import com.channelsoft.cri.logger.logger.DBLogger;
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
public class DBLogApi {
	/**
	 * 插入成功
	 * @param sql
	 * @param resultCode
	 * @param caller 调用者
	 * @CreateDate: 2013-4-27 下午02:00:17
	 * @author 魏铭
	 */
	public static void InsertSuccess(String sql, int resultCode, LogCaller caller)
	{
		DBLogger.success(OperationType.DB_INSERT, sql, "update", String.valueOf(resultCode), caller);
	}
	/**
	 * 插入失败
	 * @param sql
	 * @param caller 调用者
	 * @CreateDate: 2013-4-27 下午02:03:40
	 * @author 魏铭
	 */
	public static void InsertFail(String sql, LogCaller caller)
	{
		DBLogger.fail(OperationType.DB_INSERT, sql, "update", caller);
	}
	
	/**
	 * 更新成功
	 * @param sql
	 * @param resultCode
	 * @param caller 调用者
	 * @CreateDate: 2013-4-27 下午02:00:17
	 * @author 魏铭
	 */
	public static void UpdateSuccess(String sql, int resultCode, LogCaller caller)
	{
		DBLogger.success(OperationType.DB_UPDATE, sql, "update", String.valueOf(resultCode), caller);
	}
	/**
	 * 更新失败
	 * @param sql
	 * @param caller 调用者
	 * @CreateDate: 2013-4-27 下午02:03:40
	 * @author 魏铭
	 */
	public static void UpdateFail(String sql, LogCaller caller)
	{
		DBLogger.fail(OperationType.DB_UPDATE, sql, "update", caller);
	}
	/**
	 * 删除成功
	 * @param sql
	 * @param resultCode
	 * @param caller 调用者
	 * @CreateDate: 2013-4-27 下午02:00:17
	 * @author 魏铭
	 */
	public static void DeleteSuccess(String sql, int resultCode, LogCaller caller)
	{
		DBLogger.success(OperationType.DB_DELETE, sql, "update", String.valueOf(resultCode), caller);
	}
	/**
	 * 删除失败
	 * @param sql
	 * @param caller 调用者
	 * @CreateDate: 2013-4-27 下午02:03:40
	 * @author 魏铭
	 */
	public static void DeleteFail(String sql, LogCaller caller)
	{
		DBLogger.fail(OperationType.DB_DELETE, sql, "update", caller);
	}
	/**
	 * 查询成功
	 * @param sql
	 * @param jdbcMethod
	 * @param resultCode
	 * @param caller 调用者
	 * @CreateDate: 2013-4-27 下午02:00:17
	 * @author 魏铭
	 */
	public static void QuerySuccess(String sql, String jdbcMethod, long resultCode, LogCaller caller)
	{
		DBLogger.success(OperationType.DB_QUERY, sql, jdbcMethod, String.valueOf(resultCode), caller);
	}
	/**
	 * 查询失败
	 * @param sql
	 * @param jdbcMethod
	 * @param caller 调用者
	 * @CreateDate: 2013-4-27 下午02:03:40
	 * @author 魏铭
	 */
	public static void QueryFail(String sql, String jdbcMethod, LogCaller caller)
	{
		DBLogger.fail(OperationType.DB_QUERY, sql, jdbcMethod, caller);
	}
}

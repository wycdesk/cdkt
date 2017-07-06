package com.channelsoft.cri.logger;

import com.channelsoft.cri.logger.api.DBLogApi;
import com.channelsoft.cri.logger.vo.LogCaller;

public class DBLogUtils {
	
	/**
	 * 插入记录成功
	 * @param sql
	 * @param resultCode
	 * @CreateDate: 2013-4-27 下午04:08:04
	 * @author 魏铭
	 */
	public static void InsertSuccess(String sql, int resultCode)
	{
		DBLogApi.InsertSuccess(sql, resultCode, new LogCaller());
	}
	/**
	 * 插入记录失败
	 * @param sql
	 * @CreateDate: 2013-4-27 下午04:34:17
	 * @author 魏铭
	 */
	public static void InsertFail(String sql) {
		DBLogApi.InsertFail(sql, new LogCaller());
	}
	/**
	 * 修改记录成功
	 * @param sql
	 * @CreateDate: 2013-4-27 下午04:34:17
	 * @author 魏铭
	 */
	public static void UpdateSuccess(String sql, int resultCode) {
		DBLogApi.UpdateSuccess(sql, resultCode, new LogCaller());
	}
	/**
	 *修改记录失败
	 * @param sql
	 * @CreateDate: 2013-4-27 下午04:34:17
	 * @author 魏铭
	 */
	public static void UpdateFail(String sql) {
		DBLogApi.UpdateFail(sql, new LogCaller());
	}
	
	/**
	 * 删除记录成功
	 * @param sql
	 * @CreateDate: 2013-4-27 下午04:34:17
	 * @author 魏铭
	 */
	public static void DeleteSuccess(String sql, int resultCode) {
		DBLogApi.DeleteSuccess(sql, resultCode, new LogCaller());
	}
	/**
	 *删除记录失败
	 * @param sql
	 * @CreateDate: 2013-4-27 下午04:34:17
	 * @author 魏铭
	 */
	public static void DeleteFail(String sql) {
		DBLogApi.DeleteFail(sql, new LogCaller());
	}
	
	/**
	 * 查询记录成功
	 * @param sql
	 * @CreateDate: 2013-4-27 下午04:34:17
	 * @author 魏铭
	 */
	public static void QuerySuccess(String sql, int resultCode) {
		DBLogApi.QuerySuccess(sql, new LogCaller().getMethodName(), resultCode, new LogCaller(1));
	}
	/**
	 * 查询记录失败
	 * @param sql
	 * @CreateDate: 2013-4-27 下午04:34:17
	 * @author 魏铭
	 */
	public static void QueryFail(String sql) {
		DBLogApi.QueryFail(sql, new LogCaller().getMethodName(), new LogCaller(1));
	}
}

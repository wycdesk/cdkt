package com.channelsoft.cri.logger.api;

import com.channelsoft.cri.exception.BaseException;
import com.channelsoft.cri.logger.constant.OperationType;
import com.channelsoft.cri.logger.logger.ManageLogger;
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
public class ManageLogApi {
	/**
	 * 添加成功
	 * @param operator 操作人员
	 * @param platform 操作平台
	 * @param business 操作业务
	 * @param objective 操作对象
	 * @param remark 对象关键属性
	 * @param caller 调用者
	 * @CreateDate: 2013-4-27 下午01:47:51
	 * @author 魏铭
	 */
	public static void AddSucess(String operator, String platform, String business, String objective, String remark, LogCaller caller)
	{
		ManageLogger.success(operator, OperationType.MANAGE_ADD, platform, business, objective, remark, caller);
	}
	/**
	 * 添加失败
	 * @param operator 操作人员
	 * @param platform 操作平台
	 * @param exception 错误
	 * @param business 操作业务
	 * @param objective 操作对象
	 * @param remark 对象关键属性
	 * @param caller 调用者
	 * @CreateDate: 2013-4-27 下午01:47:51
	 * @author 魏铭
	 */
	public static void AddFail(String operator, String platform, BaseException exception, String business, String objective, String remark, LogCaller caller)
	{
		ManageLogger.fail(operator, OperationType.MANAGE_ADD, platform, exception, business, objective, remark, caller);
	}
	
	/**
	 * 修改成功
	 * @param operator 操作人员
	 * @param platform 操作平台
	 * @param business 操作业务
	 * @param objective 操作对象
	 * @param remark 对象关键属性
	 * @param caller 调用者
	 * @CreateDate: 2013-4-27 下午01:47:51
	 * @author 魏铭
	 */
	public static void EditSuccess(String operator, String platform, String business, String objective, String remark, LogCaller caller)
	{
		ManageLogger.success(operator, OperationType.MANAGE_EDIT, platform, business, objective, remark, caller);
	}
	
	/**
	 * 修改失败
	 * @param operator 操作人员
	 * @param platform 操作平台
	 * @param exception 错误
	 * @param business 操作业务
	 * @param objective 操作对象
	 * @param remark 对象关键属性
	 * @param caller 调用者
	 * @CreateDate: 2013-4-27 下午01:47:51
	 * @author 魏铭
	 */
	public static void EditFail(String operator, String platform, BaseException exception, String business, String objective, String remark, LogCaller caller)
	{
		ManageLogger.fail(operator, OperationType.MANAGE_EDIT, platform, exception, business, objective, remark, caller);
	}
	
	/**
	 * 删除成功
	 * @param operator 操作人员
	 * @param platform 操作平台
	 * @param business 操作业务
	 * @param objective 操作对象
	 * @param remark 对象关键属性
	 * @param caller 调用者
	 * @CreateDate: 2013-4-27 下午01:47:51
	 * @author 魏铭
	 */
	public static void DeleteSuccess(String operator, String platform, String business, String objective, String remark, LogCaller caller)
	{
		ManageLogger.success(operator, OperationType.MANAGE_DELETE, platform, business, objective, remark, caller);
	}
	
	/**
	 * 删除失败
	 * @param operator 操作人员
	 * @param platform 操作平台
	 * @param exception 错误
	 * @param business 操作业务
	 * @param objective 操作对象
	 * @param remark 对象关键属性
	 * @param caller 调用者
	 * @CreateDate: 2013-4-27 下午01:47:51
	 * @author 魏铭
	 */
	public static void DeleteFail(String operator, String platform, BaseException exception, String business, String objective, String remark, LogCaller caller)
	{
		ManageLogger.fail(operator, OperationType.MANAGE_DELETE, platform, exception, business, objective, remark, caller);
	}
	
	/**
	 * 删除成功
	 * @param operator 操作人员
	 * @param platform 操作平台
	 * @param business 操作业务
	 * @param objective 操作对象
	 * @param remark 对象关键属性
	 * @param caller 调用者
	 * @CreateDate: 2013-4-27 下午01:47:51
	 * @author 魏铭
	 */
	public static void QuerySuccess(String operator, String platform, String business, String objective, String remark, LogCaller caller)
	{
		ManageLogger.success(operator, OperationType.MANAGE_QUERY, platform, business, objective, remark, caller);
	}
	
	/**
	 * 查询失败
	 * @param operator 操作人员
	 * @param platform 操作平台
	 * @param exception 错误
	 * @param business 操作业务
	 * @param objective 操作对象
	 * @param remark 对象关键属性
	 * @param caller 调用者
	 * @CreateDate: 2013-4-27 下午01:47:51
	 * @author 魏铭
	 */
	public static void QueryFail(String operator, String platform, BaseException exception, String business, String objective, String remark, LogCaller caller)
	{
		ManageLogger.fail(operator, OperationType.MANAGE_QUERY, platform, exception, business, objective, remark, caller);
	}
}

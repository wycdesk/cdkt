package com.channelsoft.cri.exception;

import com.channelsoft.cri.constant.BaseErrCode;

/**
 * 项目自定义错误类
 * <dl>
 * <dt>portal</dt>
 * <dd>Description:</dd>
 * <dd>Copyright: Copyright (C) 2013</dd>
 * <dd>Company: 青牛(北京)技术有限公司 成都研究所</dd>
 * <dd>CreateDate: 2013-4-27</dd>
 * </dl>
 * @author 魏铭
 */
public class ServiceException extends BaseException {
	private static final long serialVersionUID = -6847124382876768676L;
	public ServiceException(BaseErrCode result)
	{
		super( result.code, result.desc, result.innerCode, result.message);
	}
	/**
	 * 定义异常
	 * @param result 错误代码枚举
	 * @param desc 对外显示的错误描述
	 */
	public ServiceException(BaseErrCode result, String desc)
	{
		super(result.code, desc, result.innerCode, result.message);
	}
	/**
	 * 定义异常
	 * @param result 错误代码枚举
	 * @param message 内部错误信息
	 * @param desc 对外显示的错误描述
	 */
	public ServiceException(BaseErrCode result, String message, String desc)
	{
		super(result.code, desc, result.innerCode, message);
	}
	/**
	 * 定义异常
	 * @param errorCode 错误代码
	 * @param desc 对外显示的错误描述
	 */
	public ServiceException(String errorCode, String desc)
	{
		super(errorCode, desc);
	}
	/**
	 * 定义异常
	 * @param errorCode
	 * @param message 内部错误信息
	 * @param desc 对外显示的错误描述
	 */
	public ServiceException(String errorCode, String message, String desc)
	{
		super(errorCode, desc, errorCode, message);
	}
	/**
	 * 定义异常
	 * @param desc 对外显示的错误描述
	 */
	public ServiceException(String desc)
	{
		super(desc);
	}
}

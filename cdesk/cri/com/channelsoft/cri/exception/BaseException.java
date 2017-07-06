package com.channelsoft.cri.exception;

import com.channelsoft.cri.constant.BaseErrCode;

/**
 * 自定义错误基类，其他错误类都应继承此类。
 * <dl>
 * <dt>portal</dt>
 * <dd>Description:</dd>
 * <dd>Copyright: Copyright (C) 2013</dd>
 * <dd>Company: 青牛(北京)技术有限公司 成都研究所</dd>
 * <dd>CreateDate: 2013-4-26</dd>
 * </dl>
 * @author 魏铭
 */
public class BaseException extends RuntimeException {
	private static final long serialVersionUID = -6847124382876768676L;
	public String code;
	public String innerCode;
	public String desc;
	/**
	 * 初始化错误对象
	 * @param error
	 * @CreateDate: 2013-4-26 下午01:06:10
	 * @author 魏铭
	 */
	public BaseException()
	{
		super(BaseErrCode.GENERAL_ERROR.message);
		this.code = BaseErrCode.GENERAL_ERROR.code;
		this.desc = BaseErrCode.GENERAL_ERROR.desc;
		this.innerCode = BaseErrCode.GENERAL_ERROR.innerCode;
	}
	/**
	 * 根据错误代码枚举对象初始化错误对象
	 * @param error
	 * @CreateDate: 2013-4-26 下午01:06:10
	 * @author 魏铭
	 */
	public BaseException(BaseErrCode error)
	{
		super(error.message);
		this.code = error.code;
		this.desc = error.desc;
		this.innerCode = error.innerCode;
	}
	/**
	 * 初始化错误对象
	 * @param code
	 * @param desc
	 * @param innerCode
	 * @param message
	 * @CreateDate: 2013-4-26 下午01:07:33
	 * @author 魏铭
	 */
	public BaseException(String code, String desc, String innerCode, String message)
	{
		super(message);
		this.code = code;
		this.desc = desc;
		this.innerCode = innerCode;
	}
	/**
	 * 只有错误代码和描述时，内外一致。
	 * @param code
	 * @param message
	 * @CreateDate: 2013-4-26 下午01:08:04
	 * @author 魏铭
	 */
	public BaseException(String code, String desc)
	{
		super(desc);
		this.innerCode = code;
		this.code = code;
		this.desc = desc;
	}
	/**
	 * 只有一个错误消息时，默认GeneralError
	 * @param message
	 * @CreateDate: 2013-4-26 下午01:08:31
	 * @author 魏铭
	 */
	public BaseException(String desc)
	{
		super(desc);
		this.desc = desc;
		this.innerCode = BaseErrCode.GENERAL_ERROR.innerCode;
		this.code = BaseErrCode.GENERAL_ERROR.code;
	}
	/**
	 * 后台打印的日志
	 * @return
	 * @CreateDate: 2012-12-26 下午05:41:59
	 * @author 魏铭
	 */
	public String getErrorLog()
	{
		return "[" + this.innerCode + "]-[" + this.getMessage() + "]";
	}
	/**
	 * 对外显示的错误代码
	 * @return
	 * @CreateDate 2013-1-10 下午01:22:40
	 *
	 * @author 魏铭
	 */
	public String getCode()
	{
		return this.code;
	}
	/**
	 * 对外显示的错误代码（数字格式）
	 * @return
	 * @CreateDate 2013-1-10 下午01:22:53
	 *
	 * @author 魏铭
	 */
	public int getCodeInt()
	{
		try {
			return Integer.parseInt(code);
		} catch (Exception e) {
			return -1;
		}
	}
	/**
	 * 内部错误代码
	 * @return
	 * @CreateDate 2013-1-10 下午01:23:05
	 *
	 * @author 魏铭
	 */
	public String getInnerCode()
	{
		return this.innerCode;
	}
	/**
	 * 对外显示的错误描述
	 * @return
	 * @CreateDate 2013-1-10 下午01:23:11
	 *
	 * @author 魏铭
	 */
	public String getDesc()
	{
		return this.desc;
	}
}

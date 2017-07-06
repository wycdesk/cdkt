package com.channelsoft.cri.constant;

import org.apache.commons.lang.StringUtils;
/**
 * 基本错误代码类，仅供BaseException使用
 * <dl>
 * <dt>portal</dt>
 * <dd>Description:其他项目自定义错误类按照本类规范进行定义内部错误代码。</dd>
 * <dd>1. 系统级异常：从9999开始倒退</dd>
 * <dd>2. </dd>
 * <dd>Copyright: Copyright (C) 2013</dd>
 * <dd>Company: 青牛(北京)技术有限公司 成都研究所</dd>
 * <dd>CreateDate: 2013-4-26</dd>
 * </dl>
 * @author 魏铭
 */
public enum BaseErrCode {
	
	//=============================系统级异常==========================
	/**
	 * 【9999】系统错误
	 */
	GENERAL_ERROR("9999", "系统错误", "9999", "系统繁忙，请稍候再试!"),
	/**
	 * 【9998】数据库异常
	 */
	DB_ERROR("9998", "数据库异常", "9998", "系统繁忙，请稍后再试"),
	/**
	 * 【9997】网络异常
	 */
	NETWORK_ERROR("9997", "网络异常", "9997", "系统繁忙，请稍后再试"),
	/**
	 * 【9996】参数非法
	 */
	PARAM_ERROR("9996", "参数非法", "9996", "参数非法!"),
	
	//=============================基本级业务异常==========================
	/**
	 * 基本业务异常以ERR_BASE前缀，错误编码以0开头。
	 */
	ERR_BASE("0001", "业务异常", "0001", "业务异常"),
	
	//=============================表现层业务异常==========================
	/**
	 * 表现层业务异常以ERR_VIEW前缀，错误编码以1开头。
	 */
	ERR_VIEW("1000", "业务异常", "1000", "业务异常"),
	/**
	 * 1101-无效用户:登陆时用
	 */
	ERR_INVALID_USER("1101", "用户无效", "1101", "请检查账户密码是否正确！"),
	ERR_INVALID_UNIT("1102", "单位无效", "1102", "您的归属单位无效！"),
	ERR_INVALID_PLATFORM("1103", "平台无效", "1103", "无效的登陆平台！"),
	ERR_INVALID_ENTERPRISEDB("1104", "租户无效", "1104", "请检查租户是否正确！"),
	
	//=============================服务层业务异常==========================
	/**
	 * 服务层业务异常以ERR_SERVICE前缀，错误编码以2开头。
	 */
	ERR_SERVICE("2000", "业务异常", "2000", "业务异常"),
	
	//=============================数据层业务异常==========================
	/**
	 * 数据层业务异常以ERR_DATA前缀，错误编码以30开头。
	 */
	ERR_DATA("3000", "数据异常", "3000", "数据异常"),
	ERR_DATA_ID_REPEAT("3101", "主键重复", "3000", "主键重复"),
	/**
	 * FTP操作异常以ERR_FTP前缀，错误编码以31开头
	 */
	ERR_FTP("3100", "FTP异常", "3100", "FTP异常"),
	/**
	 * 文件操作异常以ERR_FILE前缀，错误编码以32开头
	 */
	ERR_FILE("3200", "文件异常", "3200", "文件异常"),
	
	//=============================接口层业务异常==========================
	/**
	 * 接口级异常以ERR_API前缀，错误编码以4开头
	 */
	ERR_API("4000", "接口异常", "4000", "接口异常"),
	
	//=============================缓存数据层异常==========================
	/**
	 * 缓存异常以ERR_CACHE前缀，错误编码以5开头
	 */
	ERR_CACHE("5000", "缓存异常", "5000", "缓存异常"),
	//=============================管理操作异常============================
	/**
	 * 表现层业务异常以ERR_MNG前缀，错误编码以6开头。
	 */
	ERR_MNG("6000", "业务异常", "1000", "业务异常"),
	
	//=============================成功==========================
	/**
	 * 成功
	 */
	SUCCESS("0", "成功", "0", "成功");
	/**
	 * 内部错误代码
	 */
	public String innerCode;
	/**
	 * 内部错误描述
	 */
	public String message;
	/**
	 * 对外显示的错误代码(表现层用)
	 */
	public String code;
	/**
	 * 对外显示的错误描述(表现层用)
	 */
	public String desc;
	
	/**
	 * 初始化错误
	 * @param innerCode 内部错误代码
	 * @param errMessage 内部错误描述
	 * @param code 对外显示的错误代码
	 * @param desc 对外显示的错误描述
	 * @CreateDate: 2013-4-26 下午12:33:38
	 * @author 魏铭
	 */
	BaseErrCode(String innerCode, String message, String code, String desc){
		this.code = code;
		this.innerCode = innerCode;
		this.desc = desc;
		this.message = message;
	}
	/**
	 * 根据内部错误代码获取错误枚举
	 * @param innerCode
	 * @return
	 * @CreateDate: 2013-4-26 下午12:59:23
	 * @author 魏铭
	 */
	public static BaseErrCode getEnum(String innerCode){
		if (innerCode!=null)
		for(BaseErrCode e:values()){
			if(StringUtils.equalsIgnoreCase(e.innerCode, innerCode)){
				return e;
			}
		}
		return GENERAL_ERROR;
	}
	/**
	 * 前端错误展示消息
	 * @return
	 * @CreateDate: 2013-4-26 下午01:00:30
	 * @author 魏铭
	 */
	public String getViewMessage()
	{
		return this.code + "-" + this.desc;
	}
}

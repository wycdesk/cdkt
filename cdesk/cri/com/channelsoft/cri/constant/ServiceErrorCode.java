package com.channelsoft.cri.constant;

import org.apache.commons.lang.StringUtils;
/**
 * 业务错误代码类，仅供ServiceException使用
 * <dl>
 * <dt>smartDialer</dt>
 * <dd>Description:系统级异常从9999开始倒退</dd>
 * <dd>Copyright: Copyright (C) 2013</dd>
 * <dd>Company: 青牛(北京)技术有限公司 成都研究所</dd>
 * <dd>CreateDate: 2013年11月2日</dd>
 * </dl>
 * @author 魏铭
 */
public enum ServiceErrorCode {
	//=============================系统级异常==========================

	
	  /**
	  * 【9999】系统错误
	  */
	   CALLRESULT_CODE_DUMPLICATE("10001", "CALLRESULT_CODE_DUMPLICATE", "编码已存在!"),
		/**
		 * 【9999】系统错误
		 */
		GENERAL_ERROR("9999", "GENERAL_ERROR", "系统繁忙，请稍候再试!"),
		/**
		 * 【9998】数据库异常
		 */
		DB_ERROR("9998", "DB_ERROR", "数据库异常，请稍后再试"),
		/**
		 * 【9997】网络异常
		 */
		NETWORK_ERROR("9997", "NETWORK_ERROR", "系统繁忙，请稍后再试"),
		/**
		 * 【9996】参数非法
		 */
		PARAM_ERROR("9996", "PARAM_ERROR", "参数非法!"),
		/**
		 * 【9995】APG连接异常
		 */
		APG_ERROR("9995", "APG_ERROR", "APG连接异常"),
		
		
		//=============================业务异常==========================
		/**
		 * 系统级业务异常以ERR_SYS前缀，错误编码以1开头。
		 */
		ERR_SYS("1000", "ERR_SYS", "系统级业务异常"),
		/**
		 * 1000-无效用户:用户信息异常
		 */
		ERR_SYS_INVALID_USER("1001", "ERR_SYS_INVALID_USER", "请检查账户密码是否正确！"),
		ERR_SYS_INVALID_COMPANY("1002", "ERR_SYS_INVALID_COMPANY", "您的归属企业无效！"),
		ERR_SYS_INVALID_CAS("1003", "ERR_SYS_INVALID_CAS", "CAS返回的消息异常！"),
		ERR_SYS_LOGIN_TIMEOUT("1004", "ERR_SYS_LOGIN_TIMEOUT", "登陆超时!"),
		//=============================服务层业务异常==========================
		/**
		 * 服务层业务异常以ERR_SERVICE前缀，错误编码以2开头。
		 */
		ERR_SERVICE("2000", "ERR_SERVICE", "业务异常"),
		ERR_SERVICE_PRODUCT_SHORT("2001","ERR_SERVICE_PRODUCT_SHORT","产品库存不足"),
		ERR_CALLBACKTIME("2002","ERR_CALLBACKTIME","时间格式不正确"),
		//=============================数据层业务异常==========================
		/**
		 * 数据层业务异常以ERR_DATA前缀，错误编码以30开头。
		 */
		ERR_DATA("3000", "ERR_DATA", "数据异常"),
		ERR_DATA_ID_REPEAT("3001", "ERR_DATA_ID_REPEAT", "主键重复"),
		ERR_DATA_ID_ADD("3002", "ERR_DATA_ID_ADD", "添加失败"),
		ERR_DATA_ID_UPDATE("3003", "ERR_DATA_ID_UPDATE", "修改失败"),
		ERR_DATA_ID_DELETE("3004", "ERR_DATA_ID_DELETE", "删除失败"),
		ERR_DATA_ID_QUERY("3005", "ERR_DATA_ID_QUERY", "查询失败"),
		ERR_DATA_INVALID_KEY("3006", "ERR_DATA_INVALID_KEY", "该数据不存在"),
		ERR_DATA_HAVE_NAME("3007","ERR_DATA_HAVE_NAME","名字已存在"),
		ERR_DATA_EMPTY_NAME("3009","ERR_DATA_EMPTY_NAME","名字为空"),
		ERR_DATA_NO_FIELD_ROOM("3008","ERR_DATA_NO_FIELD_ROOM","没有空余字段可分配"),
		ERR_DATA_RENAME("3010","ERR_DATA_RENAME","模板名已经存在"),
		ERR_DATA_LONG("3011","ERR_DATA_LONG","数据过长"),
		
		/**
		 * FTP操作异常以ERR_FTP前缀，错误编码以31开头
		 */
		ERR_FTP("3100", "ERR_FTP", "FTP异常"),
		/**
		 * 文件操作异常以ERR_FILE前缀，错误编码以32开头
		 */
		ERR_FILE("3200", "ERR_FILE", "文件异常"),
		ERR_FILE_UPLOAD("3201", "ERR_FILE_UPLOAD", "文件上传失败"),
		ERR_FILE_INVALID("3202", "ERR_FILE_INVALID", "上传的文件非法"),
		ERR_FILE_EXISTED("3203", "ERR_FILE_EXISTED", "上传的文件已经存在"),
		ERR_FILE_NOT_EXIST("3204", "ERR_FILE_NOT_EXIST", "上传的文件不存在"),
		ERR_FILE_INVALID_TYPE("3205", "ERR_FILE_INVALID_TYPE", "非法的文件类型"),
		ERR_FILE_NO_BIND_TMP("3206", "ERR_FILE_NO_BIND_TMP", "文件和模板没有关联字段"),
		ERR_FILE_EXPORT("3207","ERR_FILE_EXPORT","文件导出失败"),
		ERR_FILE_NAMELIST_IS_EMPTY("3208","ERR_FILE_NAMELIST_IS_EMPTY","名单数据为空"),
		ERR_FILE_INVALID_NO_UTF8("3209", "ERR_FILE_INVALID_NO_UTF8", "上传的文件不是UTF-8编码"),
		//=============================接口层业务异常==========================
		/**
		 * 接口级异常以ERR_API前缀，错误编码以4开头
		 */
		ERR_API("4000", "ERR_API", "接口异常"),
		ERR_API_APG_ADD_AGENT_REDIALRECORD("4001", "ERR_API_APG_ADD_AGENT_REDIALRECORD", "名单呼叫结果未返回，不能设置重呼"),
		ERR_API_APG_NO_SKILLGROUP("4002", "ERR_API_APG_NO_SKILLGROUP", "没有查询到技能组"),
		ERR_API_APG_NO_CAMPAIGN("4003", "ERR_API_APG_NO_CAMPAIGN", "不存在对应活动"),
		ERR_API_APG_NO_BLACKLISTGROUP("5002", "ERR_API_APG_NO_BLACKLISTGROUP", "不存在对应黑名单组"),
		ERR_API_APG_NO_PARTNER("5003", "ERR_API_APG_NO_PARTNER", "不存在对应合作伙伴"),
		
		ERR_API_APG_batchImportAdd("4004", "ERR_API_APG_batchImportAdd", "批次批量添加失败"),
		ERR_API_APG_batchImportUPDATE("4005", "ERR_API_APG_batchImportUPDATE", "批次批量更新失败"),
		ERR_API_APG_ApgDeleteBatch("4006", "ERR_API_APG_ApgDeleteBatch", "批次批量删除失败"),
		//=============================管理操作异常============================
		/**
		 * 表现层业务异常以ERR_MNG前缀，错误编码以6开头。
		 */
		ERR_MNG("6000", "ERR_MNG", "业务异常"),
		
		//=============================企业开户同步信息==========================
		/**
		 * 企业开户同步信息，错误编码以7开头。
		 */
		ENT_API_ACTION_IVALID("7000","","action为无效操作"),
		ENT_API_ENT_NULL("7001","","企业ID不能为空"),
		ENT_NOT_EXIST("7002","","企业ID不存在"),
		GLS_ENT_SYN("7003","","查询该企业信息失败"),
		GLS_DB_ERR("7004","","查询GLS数据库失败"),
		ENT_API_ERR("7005","","ENT-API更新失败"),
		
		//=============================成功==========================
		/**
		 * 成功
		 */
		SUCCESS("0", "SUCCESS", "成功"),
		/**
		 * 无活动信息
		 */
		ERR_NO_CAMPAIGN("1001", "ERR_NO_CAMPAIGN", "无活动信息!"),
		/**
		 * 无坐席信息
		 */
		ERR_NO_AGENT("1002", "ERR_NO_AGENT", "该坐席没有分配技能组!"),
		
		/**
		 * 更新呼叫名单信息失败
		 */
		ERR_UPDATE_CUSTOMMER("1003", "ERR_UPDATE_CUSTOMMER", "更新呼叫名单信息失败!"),
		ERR_UPDATE_AGENTLOGIN_PARAM("1004", "ERR_UPDATE_AGENTLOGIN_PARAM", "更新呼叫更新活动登陆出错，参数不对!"),
		
		ERR_UPDATE_AGENTLOGIN_ID("1005", "ERR_UPDATE_AGENTLOGIN_ID", "更新活动登陆出错，没有登录标识!"),
		/**
		 * 分机不存在
		 */
		ERR_NO_SIP("1002", "ERR_NO_SIP", "分机不存在!"),
		ERR_INSERT_JOB_CUSTOMMER("11001", "ERR_INSERT_JOB_CUSTOMMER", "活动未关联订单导出模板!"),
		
		ERR_INSERT_PRODUCT_NUM("12001", "ORDER.DETAIL.ERROR.NUMBER", "订单中产品数量超出库存量!"),
		ERR_PRODUCT_NUM_FORMAT("12002", "ERR_PRODUCT_NUM_FORMAT", "产品数量必须为整数！"),
		ERR_PRODUCT_PRICE_FORMAT("12003", "ERR_PRODUCT_PRICE_FORMAT", "价格必须为数字！"),
		FIND_EXPORT_FILE_FORMAT_FAIL("12004", "FIND_EXPORT_FILE_FORMAT_FAIL", "查找导出文件格式失败！"),
		ERR_CAM_EXPORT("12005", "ERR_CAM_EXPORT", "活动结果导出失败"),
		CAMP_EXPORT_DELETE_FAIL("12006","CAMP_EXPORT_DELETE_FAIL","导出结果删除失败"),
		ERR_BLACKLISTGROUP_IN_USE("12007","ERR_BLACKLISTGROUP_IN_USE","活动正在使用，不能删除"),
		ERR_ORDER_CALL_ERRO("12008","ERR_ORDER_CALL_ERRO","通话异常，不能保存订单");
		
	   
		/**
		 * 错误代码（字母，页面国际化用）
		 */
		public String key;
		/**
		 * 错误代码（数字，内部和接口用）
		 */
		public String code;
		/**
		 * 错误描述(中文，系统内部用)
		 */
		public String message;
		
		/**
		 * 初始化错误
		 * @param innerCode 内部错误代码
		 * @param errMessage 内部错误描述
		 * @param code 对外显示的错误代码
		 * @param desc 对外显示的错误描述
		 * @CreateDate: 2013-4-26 下午12:33:38
		 * @author 魏铭
		 */
		ServiceErrorCode(String code, String key, String message){
			this.code = code;
			this.key = key;
			this.message = message;
		}
		/**
		 * 根据错误代码获取错误枚举
		 * @param innerCode
		 * @return
		 * @CreateDate: 2013-4-26 下午12:59:23
		 * @author 魏铭
		 */
		public static ServiceErrorCode getEnum(String code){
			if (code!=null)
			for(ServiceErrorCode e:values()){
				if(StringUtils.equalsIgnoreCase(e.code, code)){
					return e;
				}
			}
			return GENERAL_ERROR;
		}
}

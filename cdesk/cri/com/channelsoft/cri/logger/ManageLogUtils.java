package com.channelsoft.cri.logger;

import javax.servlet.http.HttpServletRequest;

import com.channelsoft.cri.constant.BaseConstants;
import com.channelsoft.cri.exception.BaseException;
import com.channelsoft.cri.logger.api.ManageLogApi;
import com.channelsoft.cri.logger.vo.LogCaller;
import com.channelsoft.ems.sso.util.SsoSessionUtils;

public class ManageLogUtils {
	/**
	 * 添加成功
	 * @param operator
	 * @param business
	 * @param objective
	 * @param remark
	 * @CreateDate: 2013-5-10 下午01:38:38
	 * @author 魏铭
	 */
	public static void AddSucess(HttpServletRequest request, String business, String objective, String remark) {
		ManageLogApi.AddSucess(SsoSessionUtils.getLoginName(request), BaseConstants.PLATFORM_ID_UPPER(), business, objective, remark, new LogCaller());
	}
	/**
	 * 添加失败
	 * @param operator
	 * @param exception
	 * @param business
	 * @param objective
	 * @param remark
	 * @CreateDate: 2013-5-10 下午01:39:25
	 * @author 魏铭
	 */
	public static void AddFail(HttpServletRequest request, BaseException exception, String business, String objective, String remark) {
		ManageLogApi.AddFail(SsoSessionUtils.getLoginName(request), BaseConstants.PLATFORM_ID_UPPER(), exception, business, objective, remark, new LogCaller());
	}
	
	/**
	 * 修改成功
	 * @param operator
	 * @param business
	 * @param objective
	 * @param remark
	 * @CreateDate: 2013-5-10 下午01:38:38
	 * @author 魏铭
	 */
	public static void EditSuccess(HttpServletRequest request, String business, String objective, String remark) {
		ManageLogApi.EditSuccess(SsoSessionUtils.getLoginName(request), BaseConstants.PLATFORM_ID_UPPER(), business, objective, remark, new LogCaller());
	}
	/**
	 * 修改失败
	 * @param operator
	 * @param exception
	 * @param business
	 * @param objective
	 * @param remark
	 * @CreateDate: 2013-5-10 下午01:39:25
	 * @author 魏铭
	 */
	public static void EditFail(HttpServletRequest request, BaseException exception, String business, String objective, String remark) {
		ManageLogApi.EditFail(SsoSessionUtils.getLoginName(request), BaseConstants.PLATFORM_ID_UPPER(), exception, business, objective, remark, new LogCaller());
	}
	
	/**
	 * 删除成功
	 * @param operator
	 * @param business
	 * @param objective
	 * @param remark
	 * @CreateDate: 2013-5-10 下午01:38:38
	 * @author 魏铭
	 */
	public static void DeleteSuccess(HttpServletRequest request, String business, String objective, String remark) {
		ManageLogApi.DeleteSuccess(SsoSessionUtils.getLoginName(request), BaseConstants.PLATFORM_ID_UPPER(), business, objective, remark, new LogCaller());
	}
	/**
	 * 修改失败
	 * @param operator
	 * @param exception
	 * @param business
	 * @param objective
	 * @param remark
	 * @CreateDate: 2013-5-10 下午01:39:25
	 * @author 魏铭
	 */
	public static void DeleteFail(HttpServletRequest request, BaseException exception, String business, String objective, String remark) {
		ManageLogApi.DeleteFail(SsoSessionUtils.getLoginName(request), BaseConstants.PLATFORM_ID_UPPER(), exception, business, objective, remark, new LogCaller());
	}
}

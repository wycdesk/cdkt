package com.channelsoft.cri.constant;

import com.channelsoft.cri.util.WebappConfigUtil;
import com.channelsoft.cri.vo.AjaxResultPo;

public class BaseConstants {
	public static final AjaxResultPo SUCCESS = AjaxResultPo.successDefault();
	public static final String HOST_IP = WebappConfigUtil.getParameter("HOST_IP");
	public static final String HOST_PORT = WebappConfigUtil.getParameter("HOST_PORT");
	public static String getLoggerName()
	{
		return WebappConfigUtil.getParameter("LOGGER_NAME");
	}
	public static String PLATFORM_ID()
	{
		return WebappConfigUtil.getParameter("PLATFORM_ID");
	}
	public static String PLATFORM_ID_UPPER()
	{
		return PLATFORM_ID().toUpperCase();
	}
}

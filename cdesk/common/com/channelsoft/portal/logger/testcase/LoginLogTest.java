package com.channelsoft.portal.logger.testcase;


import junit.framework.TestCase;

import com.channelsoft.cri.constant.BaseErrCode;
import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.logger.LoginLogUtils;

public class LoginLogTest extends TestCase{
	public void testLoginSuccess()
	{
		LoginLogUtils.LoginSuccess("root", "usportal");
	}
	
	public void testLoginFail()
	{
		LoginLogUtils.LoginFail("root", "usportal", new ServiceException(BaseErrCode.ERR_CACHE));
	}
	
	public void testLogoutSuccess()
	{
		LoginLogUtils.LogoutSuccess("root", "usportal");
	}
	
	public void testLogoutFail()
	{
		LoginLogUtils.LogoutFail("root", "usportal", new ServiceException(BaseErrCode.ERR_FTP));
	}
}

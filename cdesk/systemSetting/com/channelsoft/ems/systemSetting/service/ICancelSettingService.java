package com.channelsoft.ems.systemSetting.service;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;

import com.channelsoft.cri.exception.ServiceException;

public interface ICancelSettingService {

	int goCancel(HttpServletRequest request, String entId, String userType) throws ServiceException;
	/**
	 * 删除cdesk中需要用到的mongodb中的表
	 * @param entId
	 * @return
	 * @throws ServiceException
	 */
	int dropEntInfoForCdesk(String entId) throws ServiceException;
	
	/**
	 * 删除企业
	 * @param entId
	 * @return
	 * @throws ServiceException
	 */
	int deleteEnterprise(String entId) throws ServiceException;

}

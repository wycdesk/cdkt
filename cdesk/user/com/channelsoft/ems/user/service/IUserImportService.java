package com.channelsoft.ems.user.service;

import javax.servlet.http.HttpServletRequest;

import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.ems.user.po.DatEntUserPo;
import com.channelsoft.ems.user.vo.UserImportResultVo;

public interface IUserImportService {

	/**
	 * 批量导入用户
	 * @param request
	 * @return
	 * @throws ServiceException
	 * @author wangjie
	 * @time 2015年11月20日下午4:39:39
	 */
	//public UserImportResultVo upload(HttpServletRequest request, String addUserFlag, String updateUserFlag) throws ServiceException;
	
	/**
	 * 添加用户信息
	 * @param po
	 * @return
	 * @throws ServiceException
	 * @author wangjie
	 * @time 2015年12月21日下午5:41:22
	 */
	//public int userImport(DatEntUserPo po) throws ServiceException;
	
}

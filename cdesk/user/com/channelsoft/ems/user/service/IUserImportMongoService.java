package com.channelsoft.ems.user.service;

import javax.servlet.http.HttpServletRequest;

import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.ems.user.po.DatEntUserPo;
import com.channelsoft.ems.user.vo.UserImportResultVo;

public interface IUserImportMongoService {

	/**
	 * 批量导入用户-mongodb
	 * @param request
	 * @return
	 * @throws ServiceException
	 * @author wangjie
	 * @time 2015年11月20日下午4:39:39
	 */
	public UserImportResultVo upload(HttpServletRequest request, String addUserFlag, String updateUserFlag) throws ServiceException;
	
	/**
	 * 批量导入用户-导入excel文件
	 * @param request
	 * @param addUserFlag
	 * @param updateUserFlag
	 * @return
	 * @throws ServiceException
	 * @author wangjie
	 * @time 2016年3月23日上午9:37:22
	 */
	public UserImportResultVo uploadExcel(HttpServletRequest request, String addUserFlag, String updateUserFlag) throws ServiceException;
	
	/**
	 * 下载企业批量导入用户,excel模板文件
	 * @param request
	 * @return
	 * @throws ServiceException
	 * @author wangjie
	 * @time 2016年3月23日下午8:05:35
	 */
	public String downloadExcel(HttpServletRequest request,String realPath) throws ServiceException;
	
	/**
	 * 添加用户信息-mongodb
	 * @param po
	 * @return
	 * @throws ServiceException
	 * @author wangjie
	 * @time 2015年12月21日下午5:41:22
	 */
	public int userImport(DatEntUserPo po) throws ServiceException;
}

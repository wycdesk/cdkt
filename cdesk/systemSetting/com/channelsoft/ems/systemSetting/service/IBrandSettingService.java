package com.channelsoft.ems.systemSetting.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.ems.ent.po.DatEntInfoPo;

public interface IBrandSettingService {

	/**
	 * 系统设置-品牌设置-基本设置
	 * @param po
	 * @return
	 * @throws ServiceException
	 * @author wangjie
	 * @time 2015年11月27日上午11:12:06
	 */
	public int brandSetting(HttpServletRequest request, DatEntInfoPo po) throws ServiceException;
	
	/**
	 * 图片上传
	 * @param request
	 * @param response
	 * @param type
	 * @return
	 * @throws ServiceException
	 * @author wangjie
	 * @time 2015年11月27日下午1:39:24
	 */
	public String changeimage(HttpServletRequest request,HttpServletResponse response,String type) throws ServiceException;
	
	/**
	 * 删除图片
	 * @param request
	 * @param type
	 * @return
	 * @throws ServiceException
	 * @author wangjie
	 * @time 2015年11月29日下午2:11:08
	 */
	public int delimage(HttpServletRequest request, String type) throws ServiceException;
	
}

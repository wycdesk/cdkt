package com.channelsoft.ems.field.service;

import java.util.List;

import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.vo.PageInfo;
import com.channelsoft.ems.field.po.UserDefinedFiedPo;

public interface IUserFieldService {

	public List<UserDefinedFiedPo> query(String entId,UserDefinedFiedPo po,PageInfo page)throws ServiceException;
	/**
	 * 查询自定义字段
	 * @param entId
	 * @param status 1:启用，0：停用
	 * @param page
	 * @return
	 * @throws ServiceException
	 */
	public List<UserDefinedFiedPo> queryDefinedFiled(String entId,String status,PageInfo page)throws ServiceException;
	/**
	 * 查询默认字段
	 * @param entId
	 * @param status 1:启用，0：停用
	 * @param page
	 * @return
	 * @throws ServiceException
	 */
	public List<UserDefinedFiedPo> queryDefultFiled(String entId,String status, PageInfo page)throws ServiceException;
	/**
	 * 添加自定义字段
	 * @param po
	 * @param entId
	 * @return
	 * @throws ServiceException
	 */
	public int addDefinedField(UserDefinedFiedPo po, String entId) throws ServiceException;
	/*
	 * 获取自定义字段的key值
	 */
	public String getKey(String entId) throws ServiceException;
	public UserDefinedFiedPo queryFieldByKey(String entId,String key, String status) throws ServiceException;
	public int goEdit(String entId, UserDefinedFiedPo po) throws ServiceException;
	public int deleteField(String entId, String key) throws ServiceException;
	public int changeMode(String entId, String type, String key) throws ServiceException;
	public int sortUserField(String entId, String ids) throws ServiceException;
}

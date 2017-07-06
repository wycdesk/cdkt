package com.channelsoft.ems.field.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.channelsoft.cri.vo.PageInfo;
import com.channelsoft.ems.field.po.UserDefinedFiedPo;


public interface IUserFieldDao {
	/**
	 * 根据查询条件查询用户自定义字段
	 * @param entId
	 * @param po
	 * @param page
	 * @return
	 * @throws DataAccessException
	 */
   List<UserDefinedFiedPo> query(String entId,UserDefinedFiedPo po,PageInfo page) throws DataAccessException;
    /**
     * 添加自定义字段
     * @param po
     * @param entId
     * @return
     * @throws DataAccessException
     */
	public int add(UserDefinedFiedPo po,String entId) throws DataAccessException;
	/**
	 * 获取用户自定义表
	 * @param entId
	 * @return
	 * @throws DataAccessException
	 */
	public String getUserFieldTableName(String entId) throws DataAccessException;
	/*
	 * 编辑用户自定义字段
	 */
	int goEdit(String entId, UserDefinedFiedPo po) throws DataAccessException;
	int deleteField(String entId, String key) throws DataAccessException;
	int changeMode(String entId, String type, String key) throws DataAccessException;
	/**
	 * 删除用户字段自定义集合
	 * @param entId
	 * @return
	 * @throws DataAccessException
	 */
	
	int dropUserFieldTable(String entId) throws DataAccessException;
	
	int[] getSortNums(String entId) throws DataAccessException;
	
	int sortUserField(String entId,String[] sortKey, int[] sortNums) throws DataAccessException;
}

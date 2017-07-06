package com.channelsoft.ems.field.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.channelsoft.cri.vo.PageInfo;
import com.channelsoft.ems.field.po.BaseFieldPo;

public interface IFieldMongoDao {

	/*添加自定义字段*/
	public int add(BaseFieldPo po,String entId) throws DataAccessException;
	
	/*获取字段表*/
	public String getFieldTableName(String entId) throws DataAccessException;
	
	/**
	 * 根据查询条件查询自定义字段
	 * @param entId
	 * @param po
	 * @param page
	 * @return
	 * @throws DataAccessException
	 */
   List<BaseFieldPo> query(String entId,BaseFieldPo po,PageInfo page) throws DataAccessException;

  /* 编辑自定义字段*/
   public int goEdit(String entId, BaseFieldPo po) throws DataAccessException;
   
  /* 删除自定义字段（逻辑删除）*/
   public int deleteField(String entId, String key) throws DataAccessException;
   
  /* 修改自定义字段状态*/
   public int changeMode(String entId, String type, String key) throws DataAccessException;
   
   /*获取某个模板全部启用状态自定义字段的排序值*/
   public int[] getSortNums(String entId, String tempId) throws DataAccessException;
   
   /*排序某一模板内所有启用的字段*/
   public int sortField(String entId, String tempId, String[] sortKey, int[] sortNums) throws DataAccessException;
}

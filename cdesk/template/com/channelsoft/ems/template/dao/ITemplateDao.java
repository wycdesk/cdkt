package com.channelsoft.ems.template.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.channelsoft.cri.vo.PageInfo;
import com.channelsoft.ems.field.po.BaseFieldPo;
import com.channelsoft.ems.template.po.TemplatePo;

public interface ITemplateDao {

	/**
	 * 获取模板表
	 * @param entId
	 * @return
	 * @throws DataAccessException
	 */
	public String getTemplateTableName(String entId) throws DataAccessException;
	
	/*获取字段表*/
	public String getFieldTableName(String entId) throws DataAccessException;
	/**
	 * 根据查询条件查询模板
	 * @param entId
	 * @param po
	 * @param page
	 * @return
	 * @throws DataAccessException
	 */
   public List<TemplatePo> query(String entId,TemplatePo po,PageInfo page) throws DataAccessException;
  
   /**
    * 添加工单模板
    * @param po
    * @param entId
    * @return
    * @throws DataAccessException
    */
	public int addTemp(TemplatePo po,String entId) throws DataAccessException;
	
	/*删除模板（逻辑删除）*/
	public int deleteTemplate(String entId, String key) throws DataAccessException;
	
	/*模板启停用*/
	public int changeMode(String entId, String type, String key) throws DataAccessException;
	
	/*获取全部启用模板的排序值*/
	public int[] getSortNums(String entId) throws DataAccessException;
	
	/*排序所有启用的模板*/
	public int sortTemplate(String entId,String[] sortKey, int[] sortNums) throws DataAccessException;
	
	/*添加工单默认字段*/
	public void addDefaultField(List<BaseFieldPo> list,String entId) throws DataAccessException;

	/*按模板Id删除字段（逻辑删除）*/
	public int deleteFieldByTempId(String entId, String key) throws DataAccessException;
	
	/*按条件查询字段*/
	public List<BaseFieldPo> queryFields(String entId,BaseFieldPo po,PageInfo page) throws DataAccessException;
	
	/* 编辑模板信息（标题）*/
	public int goEdit(String entId, TemplatePo po) throws DataAccessException;
	
	
	/**
	* 添加工单默认模板
	* @param po
	* @param entId
	* @return
	* @throws DataAccessException
	*/
	public int addDefaultTemp(TemplatePo po,String entId) throws DataAccessException;
}

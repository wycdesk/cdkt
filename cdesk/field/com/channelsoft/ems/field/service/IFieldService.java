package com.channelsoft.ems.field.service;

import java.util.List;
import java.util.Map;

import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.vo.PageInfo;
import com.channelsoft.ems.field.po.BaseFieldPo;

public interface IFieldService {

	/*由字段id查询自定义字段*/
	public BaseFieldPo queryFieldByKey(String entId, String key) throws ServiceException;
	
	/*编辑自定义字段*/
	public int goEdit(String entId, BaseFieldPo po) throws ServiceException;
	
	/*删除自定义字段（逻辑删除）*/
	public int deleteField(String entId, String key) throws ServiceException;
	
	/*修改自定义字段状态*/
	public int changeMode(String entId, String type, String key) throws ServiceException;
	
	/*自定义字段移动排序*/
	public int sortField(String entId, String tempId, String ids) throws ServiceException;
	/**
	 * 按企业、模版类型查询所有字段
	 * @return
	 * @throws ServiceException
	 */
	public Map<String, Map<String,List<BaseFieldPo>>> queryAllField() throws ServiceException;
	/**
	 * 根据查询条件查询工单自定义字段
	 * @param entId
	 * @param po
	 * @param page
	 * @return
	 * @throws ServiceException
	 */
	public List<BaseFieldPo> query(String entId, BaseFieldPo po,PageInfo page) throws ServiceException;
}

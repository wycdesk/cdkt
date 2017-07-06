package com.channelsoft.ems.template.service;

import java.util.List;
import java.util.Map;

import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.vo.PageInfo;
import com.channelsoft.ems.field.po.BaseFieldPo;
import com.channelsoft.ems.template.po.TemplatePo;

public interface ITemplateService {

	/**
	 * 查询工单模板
	 * @param entId
	 * @param status 1:启用，0：停用
	 * @param page
	 * @return
	 * @throws ServiceException
	 */
	public List<TemplatePo> queryWorkTemplates(String entId,String status,PageInfo page)throws ServiceException;

	
	/*按条件查询模板*/
	public List<TemplatePo> query(String entId,TemplatePo po,PageInfo page)throws ServiceException;
	/**
	 * 按企业、模版类型查询所有模版
	 * @return
	 * @throws ServiceException
	 */
	public  Map<String, Map<String,List<TemplatePo>>> queryAll() throws ServiceException;

	/**
	 * 添加工单模板
	 * @param po
	 * @param entId
	 * @return
	 * @throws ServiceException
	 */
	public int addWorkTemp(TemplatePo po, String entId) throws ServiceException;
	
	/*
	 * 获取工单模板的Id
	 */
	public String getTempId(String entId) throws ServiceException;
	
	/*删除模板（逻辑删除）*/
	public int deleteTemplate(String entId, String key) throws ServiceException;
	
	/*模板启停用*/
	public int changeMode(String entId, String type, String key) throws ServiceException;
	
	/*模板排序*/
	public int sortTemplate(String entId, String ids) throws ServiceException;
	
	/*获取字段Id*/
	public String getKey(String entId) throws ServiceException;
	
	/*按模板Id删除字段（逻辑删除）*/
	public int deleteFieldByTempId(String entId, String key) throws ServiceException;
	
	/*按模板Id查询字段*/
	public List<BaseFieldPo> queryFieldsByTempId(String entId,String tempId,PageInfo page)throws ServiceException;
	
	public List<BaseFieldPo> queryFields(String entId, BaseFieldPo po, PageInfo page) throws ServiceException;

	/**
	 * 添加自定义字段
	 * @param po
	 * @param entId
	 * @return
	 * @throws ServiceException
	 */
	public int addDefinedField(BaseFieldPo po, String entId) throws ServiceException;
	
	/*编辑模板信息（标题）*/
	public int goEdit(String entId, TemplatePo po) throws ServiceException;
	
	
	/*企业开户调用接口，根据企业编号生成该企业的默认模板（包括模板数据和字段数据）*/
	public int addDefaultWorkTemp(String entId) throws ServiceException;
	
}

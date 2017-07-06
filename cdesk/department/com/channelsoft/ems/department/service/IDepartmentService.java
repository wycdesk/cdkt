package com.channelsoft.ems.department.service;

import java.util.List;

import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.ems.department.po.DepartmentPo;
import com.channelsoft.ems.department.po.JobPo;
import com.channelsoft.ems.department.vo.DepartmentVo;

public interface IDepartmentService {

	/*查询部门*/
	public List<DepartmentVo> query(String enterpriseid) throws ServiceException;
	
	/*查询部门*/
	public List<DepartmentVo> query(DepartmentPo po) throws ServiceException; 
	
	/*修改部门*/
	public int update(DepartmentPo po) throws ServiceException;
	
	/*删除部门（逻辑删除）*/
	public int delete(String enterpriseid, String id) throws ServiceException;
	
	/*添加部门*/
	public int add(DepartmentPo po) throws ServiceException;
	
	/*删除岗位（逻辑删除）*/
	public int deleteJobById(String enterpriseid, String id) throws ServiceException;
	
	/*修改岗位*/
	public int updateJob(JobPo po) throws ServiceException;
	
	/*添加岗位*/
	public int addJob(JobPo po) throws ServiceException;
	
	/*根据岗位Id查询岗位*/
	public List<DepartmentVo> queryJobById(JobPo po) throws ServiceException;
	
	/*生成部门编号*/
	public String getDepartmentId(String entId, String parentId) throws ServiceException;
	
	/*生成岗位编号*/
	public String getJobId(String entId, String parentId) throws ServiceException;
	
	/*查询岗位*/
	public List<DepartmentVo> queryJob(JobPo po) throws ServiceException; 
	
	/*查询所有部门并组成树状链表*/
	public List<DepartmentVo> queryAllDpt(String enterpriseid) throws ServiceException;
	
	/*查询所有岗位并组成树状链表*/
	public List<DepartmentVo> queryAllJob(String enterpriseid) throws ServiceException;
}

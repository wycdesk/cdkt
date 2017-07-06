package com.channelsoft.ems.department.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.channelsoft.ems.department.po.DepartmentPo;
import com.channelsoft.ems.department.po.JobPo;
import com.channelsoft.ems.department.vo.DepartmentVo;

public interface IDepartmentDao {

	/*查询部门*/
	public List<DepartmentVo> query(DepartmentPo po) throws DataAccessException;
	
	/*查询岗位*/
	public List<DepartmentVo> queryJob(JobPo po) throws DataAccessException;
	
	/*修改部门*/
	public int update(DepartmentPo po) throws DataAccessException;
	
	/*删除部门（逻辑删除）*/
	public int delete(String enterpriseid, String id) throws DataAccessException;
	
	/*由parentId查询子部门节点*/
	public List<DepartmentPo> queryForChildren(String enterpriseid, String parentId) throws DataAccessException;
	
	/*由部门Id查询部门包含的岗位*/
	public List<JobPo> queryForJobChildren(String enterpriseid, String dptId) throws DataAccessException;

	/*由parentId查询部子岗位节点*/
	public List<JobPo> queryJobChildren(String enterpriseid, String id) throws DataAccessException;
	
    /*添加部门*/
	public int add(DepartmentPo po) throws DataAccessException;
	
	/*根据岗位Id删除岗位（逻辑删除）*/
	public int deleteJob(String enterpriseid, String id) throws DataAccessException;
	
	/*根据部门Id删除岗位（逻辑删除）*/
	public int deleteByDptId(String enterpriseid, String dptId) throws DataAccessException;
	
	/*修改岗位*/
	public int updateJob(JobPo po) throws DataAccessException;
	
    /*添加岗位*/
	public int addJob(JobPo po) throws DataAccessException;
	
	/*查询上级部门的子部门,按Id升序*/
	public List<DepartmentVo> queryDptByParentId(DepartmentPo po) throws DataAccessException;
	
	/*查询上级岗位的子岗位，按Id升序*/
	public List<DepartmentVo> queryJobByParentId(JobPo po) throws DataAccessException;
}

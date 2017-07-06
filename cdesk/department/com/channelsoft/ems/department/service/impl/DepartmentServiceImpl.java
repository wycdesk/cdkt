package com.channelsoft.ems.department.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import com.channelsoft.cri.constant.BaseErrCode;
import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.ems.department.constans.DepartmentStatusEnum;
import com.channelsoft.ems.department.constans.JobStatusEnum;
import com.channelsoft.ems.department.dao.IDepartmentDao;
import com.channelsoft.ems.department.po.DepartmentPo;
import com.channelsoft.ems.department.po.JobPo;
import com.channelsoft.ems.department.service.IDepartmentService;
import com.channelsoft.ems.department.vo.DepartmentVo;

public class DepartmentServiceImpl implements IDepartmentService{

	@Autowired
	IDepartmentDao departmentDao;
	
	/*查询部门和岗位共同组成树状链表*/
	@Override
	public List<DepartmentVo> query(String enterpriseid) throws ServiceException {
		DepartmentPo po = new DepartmentPo();
		po.setEntId(enterpriseid);
		po.setStatus(DepartmentStatusEnum.NORMAL.value);
		List<DepartmentVo> menuList = this.query(po);		
		
		return getTopMenu(enterpriseid, menuList, "0");
	}
	
	/*查询部门*/
	@Override
	public List<DepartmentVo> query(DepartmentPo po) throws ServiceException {
		try {		
			return departmentDao.query(po);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR, e.getMessage());
		}
	}
			
	/*查询menu列表中所有顶级节点，并组成树状链表 */
	private List<DepartmentVo> getTopMenu(String enterpriseid, List<DepartmentVo> menuList, String rootId) {
		List<DepartmentVo> topList = new ArrayList<DepartmentVo>();
		
		/*查询岗位列表*/
		JobPo job = new JobPo();
		job.setEntId(enterpriseid);	
		job.setStatus(JobStatusEnum.NORMAL.value);
		List<DepartmentVo> jobList = departmentDao.queryJob(job);
		
		for (DepartmentVo menu : menuList) {	
			List<DepartmentVo> childrenMenu = new ArrayList<DepartmentVo>();		
			/**
			 * 处理岗位父子结构，生成链表
			 */				
			for(DepartmentVo vo : jobList){
				if(StringUtils.equals(vo.getDptId(), menu.getId())){
					menu.setHasChildren(true);
					menu.setState("open");
					
					childrenMenu.add(vo);
				}
			}				
			/**
			 * 处理部门父子结构，生成链表
			 */
			for (int j = 0; j < menuList.size(); j++) {
				DepartmentVo menu2 = menuList.get(j);
				if (StringUtils.equals(menu2.getParentId(), menu.getId())) {
					menu.setHasChildren(true);
					menu.setState("open");
					// 设定父节点名称
					menu2.setParentName(menu.getName());
					childrenMenu.add(menu2);
				}
			}	
			menu.setChildren(childrenMenu);			
			/**
			 * 父节点为0的，加入topList
			 */
			if (rootId.equals(menu.getParentId()) && !rootId.equals(menu.getId())) {
				topList.add(menu);
			}
		}
		return topList;
	}
	
	/*修改部门*/
	@Override
	public int update(DepartmentPo po) throws ServiceException {
		try {
			/*查询parentId的一级子部门*/
			List<DepartmentPo> children = departmentDao.queryForChildren(po.getEntId(), po.getId());
			
			/*更新该部门信息，并递归更新其子部门的level*/
			for (DepartmentPo po1 : children) {
				
				if(StringUtils.isNotBlank(po.getLevel())){
					po1.setLevel(Integer.toString(Integer.parseInt(po.getLevel())+1));
				}			
				this.update(po1);
			}	
			
			return departmentDao.update(po);			
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR, e.getMessage(), "修改失败");
		}
	}
	
	/*删除部门（逻辑删除）*/
	@Override
	@Transactional
	public int delete(String enterpriseid, String id) throws ServiceException {
		try {	
			/*查询parentId的一级子部门*/
			List<DepartmentPo> children = departmentDao.queryForChildren(enterpriseid, id);
			
			/*递归删除部门*/
			for (DepartmentPo po : children) {
				this.delete(enterpriseid, po.getId());
			}		
			/*删除部门和关联的岗位（逻辑删除）*/
			return departmentDao.delete(enterpriseid, id);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR, e.getMessage(), "删除失败");
		}
	}
	
	/*添加部门*/
	@Override
	public int add(DepartmentPo po) throws ServiceException {
		try {
			if (po.getSortWeight() == null) {
				po.setSortWeight("0");
			}
			if (po.getStatus() == null) {
				po.setStatus("1");
			}
			try {
				po.setLevelInt(Integer.parseInt(po.getLevel()));
				po.setSortWeightInt(Integer.parseInt(po.getSortWeight()));
			} catch (NumberFormatException e) {
				po.setSortWeightInt(0);
				po.setLevelInt(1);
			}
			return departmentDao.add(po);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR, e.getMessage(), "添加失败");
		}
	}
	
	/*删除岗位（逻辑删除）*/
	@Override
	@Transactional
	public int deleteJobById(String enterpriseid, String id) throws ServiceException {
		try {	
			/*查询parentId的一级子岗位*/
			List<JobPo> children = departmentDao.queryJobChildren(enterpriseid, id);
			
			/*递归删除岗位*/
			for (JobPo po : children) {
				this.deleteJobById(enterpriseid, po.getId());
			}		
			return departmentDao.deleteJob(enterpriseid, id);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR, e.getMessage(), "删除失败");
		}
	}
	
	/*修改岗位*/
	@Override
	public int updateJob(JobPo po) throws ServiceException {
		try {	
			/*查询parentId的一级子岗位*/
			List<JobPo> children = departmentDao.queryJobChildren(po.getEntId(), po.getId());
			
			/*更新该岗位信息，并递归更新其子岗位的所属部门和level*/
			for (JobPo po1 : children) {
				if(StringUtils.isNotBlank(po.getDptId())){
					po1.setDptId(po.getDptId());
				}				
				if(StringUtils.isNotBlank(po.getLevel())){
					po1.setLevel(Integer.toString(Integer.parseInt(po.getLevel())+1));
				}			
				this.updateJob(po1);
			}	
			
			return departmentDao.updateJob(po);			
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR, e.getMessage(), "修改失败");
		}
	}
	
	/*添加岗位*/
	@Override
	public int addJob(JobPo po) throws ServiceException {
		try {
			if (po.getSortWeight() == null) {
				po.setSortWeight("0");
			}
			if (po.getStatus() == null) {
				po.setStatus("1");
			}
			try {
				po.setLevelInt(Integer.parseInt(po.getLevel()));
				po.setSortWeightInt(Integer.parseInt(po.getSortWeight()));
			} catch (NumberFormatException e) {
				po.setSortWeightInt(0);
				po.setLevelInt(1);
			}
			return departmentDao.addJob(po);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR, e.getMessage(), "添加失败");
		}
	}
	
	/*根据岗位Id查询岗位*/
	public List<DepartmentVo> queryJobById(JobPo po) throws ServiceException{
		try {			
			return departmentDao.queryJob(po);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR, e.getMessage());
		}
	}
	
	/*生成部门编号*/
	@Override
	public String getDepartmentId(String entId, String parentId) throws ServiceException{
		DepartmentPo po = new DepartmentPo();
		po.setEntId(entId);
		po.setParentId(parentId);	
		/*根据parentId查询子部门，Id升序排列*/
		List<DepartmentVo> list = departmentDao.queryDptByParentId(po);
		
		String id = "";
		String maxId = "";
		/*上级部门下存在子部门*/
		if(list.size() > 0){
			/*插空赋值id*/
			maxId = list.get(list.size()-1).getId();	
			for(int i=0;i<list.size()-1;i++){
				int value = Integer.parseInt(list.get(i+1).getId())-Integer.parseInt(list.get(i).getId());				
				if(value>1){
					id = Integer.toString(Integer.parseInt(list.get(i).getId())+1);					
					break;
				}
			}
			/*id若为空，则id为最大值加1*/
			if(StringUtils.isBlank(id)){
				id = Integer.toString(Integer.parseInt(maxId)+1); 
			}
		}
		/*上级部门下没有子部门*/
		else{
			id=po.getParentId()+"001";
		}	
		return id;		
	}
	
	/*生成岗位编号*/
	@Override
	public String getJobId(String entId, String parentId) throws ServiceException{
		JobPo po = new JobPo();
		po.setEntId(entId);
		po.setParentId(parentId);	
		/*根据parenId查询子岗位，Id升序排列*/
		List<DepartmentVo> list = departmentDao.queryJobByParentId(po);
		
		String id = "";
		String maxId = "";
		/*上级岗位下存在子岗位*/
		if(list.size() > 0){
			/*插空赋值id*/
			maxId = list.get(list.size()-1).getId();	
			for(int i=0;i<list.size()-1;i++){
				int value = Integer.parseInt(list.get(i+1).getId())-Integer.parseInt(list.get(i).getId());				
				if(value>1){
					id = Integer.toString(Integer.parseInt(list.get(i).getId())+1);					
					break;
				}
			}
			/*id若为空，则id为最大值加1*/
			if(StringUtils.isBlank(id)){
				id = Integer.toString(Integer.parseInt(maxId)+1); 
			}
		}
		/*上级岗位下没有子岗位*/
		else{
			id=po.getParentId()+"001";
		}	
		return id;		
	}
	
	/*查询岗位*/
	@Override
	public List<DepartmentVo> queryJob(JobPo po) throws ServiceException {
		try {		
			return departmentDao.queryJob(po);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR, e.getMessage());
		}
	}
	
	/*查询所有部门并组成树状链表*/
	@Override
	public List<DepartmentVo> queryAllDpt(String enterpriseid) throws ServiceException {
		DepartmentPo po = new DepartmentPo();
		po.setEntId(enterpriseid);
		po.setStatus(DepartmentStatusEnum.NORMAL.value);
		List<DepartmentVo> menuList = this.query(po);		
		
		return getTopMenuList(enterpriseid, menuList, "0");
	}
	
	/*查询所有岗位并组成树状链表*/
	@Override
	public List<DepartmentVo> queryAllJob(String enterpriseid) throws ServiceException {
		JobPo po = new JobPo();
		po.setEntId(enterpriseid);
		po.setStatus(JobStatusEnum.NORMAL.value);	
		List<DepartmentVo> menuList = departmentDao.queryJob(po);
		
		return getTopMenuList(enterpriseid, menuList, "0");
	}
	
	/*查询menu列表中所有顶级节点，并组成树状链表 */
	private List<DepartmentVo> getTopMenuList(String enterpriseid, List<DepartmentVo> menuList, String rootId) {
		List<DepartmentVo> topList = new ArrayList<DepartmentVo>();
		
		for (DepartmentVo menu : menuList) {	
			List<DepartmentVo> childrenMenu = new ArrayList<DepartmentVo>();				
			/**
			 * 处理部门父子结构，生成链表
			 */
			for (int j = 0; j < menuList.size(); j++) {
				DepartmentVo menu2 = menuList.get(j);
				if (StringUtils.equals(menu2.getParentId(), menu.getId())) {
					menu.setHasChildren(true);
					menu.setState("open");
					// 设定父节点名称
					menu2.setParentName(menu.getName());
					childrenMenu.add(menu2);
				}
			}	
			menu.setChildren(childrenMenu);			
			/**
			 * 父节点为0的，加入topList
			 */
			if (rootId.equals(menu.getParentId()) && !rootId.equals(menu.getId())) {
				topList.add(menu);
			}
		}
		return topList;
	}
	
}

package com.channelsoft.ems.department.dao.impl;

import java.sql.Types;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;

import com.channelsoft.cri.dao.jdbc.BaseJdbcMysqlDao;
import com.channelsoft.ems.department.constans.DepartmentStatusEnum;
import com.channelsoft.ems.department.constans.JobStatusEnum;
import com.channelsoft.ems.department.dao.IDepartmentDao;
import com.channelsoft.ems.department.po.DepartmentPo;
import com.channelsoft.ems.department.po.JobPo;
import com.channelsoft.ems.department.vo.DepartmentVo;

public class DepartmentDaoImpl extends BaseJdbcMysqlDao implements IDepartmentDao{

	/*查询部门*/
	@Override
	public List<DepartmentVo> query(DepartmentPo po) throws DataAccessException {
		StringBuffer sqlBuffer = new StringBuffer();		
		sqlBuffer.append("SELECT CPD.* FROM ent_").append(po.getEntId()).append(".CFG_DEPARTMENT CPD WHERE 1=1");
		
		if (StringUtils.isNotBlank(po.getId())) {
			sqlBuffer.append(" AND CPD.ID = '").append(po.getId()).append("'");
		}
		if (StringUtils.isNotBlank(po.getName())) {
			sqlBuffer.append(" AND CPD.NAME = '").append(po.getName()).append("'");
		}
		if (StringUtils.isNotBlank(po.getEntId())) {
			sqlBuffer.append(" AND CPD.ENT_ID = '").append(po.getEntId()).append("'");
		}
		if (StringUtils.isNotBlank(po.getLevel())) {
			sqlBuffer.append(" AND CPD.LEVEL = '").append(po.getLevel()).append("'");
		}
		if (StringUtils.isNotBlank(po.getParentId())) {
			sqlBuffer.append(" AND CPD.PARENT_ID = '").append(po.getParentId()).append("'");
		}
		if (StringUtils.isNotBlank(po.getReamrk())) {
			sqlBuffer.append(" AND CPD.REAMRK = '").append(po.getReamrk()).append("'");
		}
		if (StringUtils.isNotBlank(po.getStatus())) {
			sqlBuffer.append(" AND CPD.STATUS = '").append(po.getStatus()).append("'");
		}
		sqlBuffer.append(" ORDER BY SORT_WEIGHT");
		return this.queryForList(sqlBuffer.toString(), DepartmentVo.class);
	}
	
	/*查询岗位*/
	@Override
	public List<DepartmentVo> queryJob(JobPo po) throws DataAccessException {
		StringBuffer sqlBuffer = new StringBuffer();		
		sqlBuffer.append("SELECT CJ.* FROM ent_").append(po.getEntId()).append(".CFG_JOB CJ WHERE 1=1");
		
		if (StringUtils.isNotBlank(po.getId())) {
			sqlBuffer.append(" AND CJ.ID = '").append(po.getId()).append("'");
		}
		if (StringUtils.isNotBlank(po.getName())) {
			sqlBuffer.append(" AND CJ.NAME = '").append(po.getName()).append("'");
		}
		if (StringUtils.isNotBlank(po.getEntId())) {
			sqlBuffer.append(" AND CJ.ENT_ID = '").append(po.getEntId()).append("'");
		}
		if (StringUtils.isNotBlank(po.getLevel())) {
			sqlBuffer.append(" AND CJ.LEVEL = '").append(po.getLevel()).append("'");
		}
		if (StringUtils.isNotBlank(po.getParentId())) {
			sqlBuffer.append(" AND CJ.DPT_ID = '").append(po.getDptId()).append("'");
		}
		if (StringUtils.isNotBlank(po.getParentId())) {
			sqlBuffer.append(" AND CJ.PARENT_ID = '").append(po.getParentId()).append("'");
		}
		if (StringUtils.isNotBlank(po.getReamrk())) {
			sqlBuffer.append(" AND CJ.REAMRK = '").append(po.getReamrk()).append("'");
		}
		if (StringUtils.isNotBlank(po.getStatus())) {
			sqlBuffer.append(" AND CJ.STATUS = '").append(po.getStatus()).append("'");
		}
		sqlBuffer.append(" ORDER BY SORT_WEIGHT");
		return this.queryForList(sqlBuffer.toString(), DepartmentVo.class);
	}
		
	/*修改部门*/
	@Override
	public int update(DepartmentPo po) throws DataAccessException {
		StringBuffer sqlBuffer = new StringBuffer();		
		sqlBuffer.append("UPDATE ent_").append(po.getEntId()).append(".CFG_DEPARTMENT SET");
		
		if (StringUtils.isNotBlank(po.getName())) {
			sqlBuffer.append(" NAME = '").append(po.getName()).append("',");
		}
		if (StringUtils.isNotBlank(po.getParentId())) {
			sqlBuffer.append(" PARENT_ID = '").append(po.getParentId()).append("',");
		}
		
		if (StringUtils.isNotBlank(po.getReamrk())) {
			sqlBuffer.append(" REAMRK = '").append(po.getReamrk()).append("',");
		}else{
			sqlBuffer.append(" REAMRK = '").append("").append("',");
		}	
		
		if (StringUtils.isNotBlank(po.getLevel())) {
			sqlBuffer.append(" LEVEL = '").append(po.getLevel()).append("',");
		}	
		sqlBuffer.append(" UPDATE_TIME=NOW() WHERE ID=?");
		Object[] params = new Object[] { po.getId() };
		int[] types = new int[] { Types.VARCHAR };

		return this.update(sqlBuffer.toString(), params, types);
	}
		
	/*删除部门（逻辑删除）*/
	@Override
	public int delete(String enterpriseid, String id) throws DataAccessException {
		String sql = "UPDATE ent_" + enterpriseid + ".CFG_DEPARTMENT SET STATUS = "+ DepartmentStatusEnum.DELETE.value +", UPDATE_TIME = NOW() WHERE ID=?";		
		/*删除部门关联的岗位（逻辑删除）*/
		String sql1 = "UPDATE ent_" + enterpriseid + ".CFG_JOB SET STATUS = "+ JobStatusEnum.DELETE.value +", UPDATE_TIME = NOW() WHERE DPT_ID=?";
		
		Object[] params = new Object[] { id };
		Object[] params1 = new Object[] { id };
		int[] types = new int[] { Types.VARCHAR };
		int[] types1 = new int[] { Types.VARCHAR };
		
		this.update(sql1, params1, types1);
		
		return this.update(sql, params, types);
	}
		
	/*由parentId查询子部门节点*/
	@Override
	public List<DepartmentPo> queryForChildren(String enterpriseid, String parentId) throws DataAccessException {
		String sql = "SELECT * FROM ent_"+ enterpriseid +".CFG_DEPARTMENT WHERE PARENT_ID=? ORDER BY SORT_WEIGHT";
		Object[] params = new Object[] { parentId };
		int[] types = new int[] { Types.VARCHAR };
		return this.queryForList(sql, params, types, DepartmentPo.class);
	}
		
	/*由parentId查询子岗位节点*/
	@Override
	public List<JobPo> queryJobChildren(String enterpriseid, String parentId) throws DataAccessException {
		String sql = "SELECT * FROM ent_"+ enterpriseid +".CFG_JOB WHERE PARENT_ID=? ORDER BY SORT_WEIGHT";
		Object[] params = new Object[] { parentId };
		int[] types = new int[] { Types.VARCHAR };
		return this.queryForList(sql, params, types, JobPo.class);
	}
	
	/*添加部门*/
	@Override
	public int add(DepartmentPo po) throws DataAccessException {
		String sql = "INSERT INTO ent_"+ po.getEntId() +".CFG_DEPARTMENT (ID, NAME, ENT_ID, LEVEL, PARENT_ID, SORT_WEIGHT, REAMRK, STATUS, CREATE_TIME, UPDATE_TIME) VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";
		Object[] params = new Object[] { po.getId(), po.getName(), po.getEntId(), po.getLevelInt(),
				po.getParentId(), po.getSortWeightInt(), po.getReamrk(), po.getStatus() };
		int[] types = new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.INTEGER,
				Types.VARCHAR, Types.INTEGER, Types.VARCHAR, Types.VARCHAR };
		return this.update(sql, params, types);
	}
		
	/*由部门Id查询部门包含的岗位*/
	@Override
	public List<JobPo> queryForJobChildren(String enterpriseid, String dptId) throws DataAccessException {
		String sql = "SELECT * FROM ent_"+ enterpriseid +".CFG_JOB WHERE DPT_ID=? ORDER BY SORT_WEIGHT";
		Object[] params = new Object[] { dptId };
		int[] types = new int[] { Types.VARCHAR };
		return this.queryForList(sql, params, types, JobPo.class);
	}
		
	/*根据岗位Id删除岗位（逻辑删除）*/
	@Override
	public int deleteJob(String enterpriseid, String id) throws DataAccessException {
		String sql = "UPDATE ent_" + enterpriseid + ".CFG_JOB SET STATUS = "+ JobStatusEnum.DELETE.value +", UPDATE_TIME = NOW() WHERE ID=?";
		Object[] params = new Object[] { id };
		int[] types = new int[] { Types.VARCHAR };
		return this.update(sql, params, types);
	}
	
	/*根据部门Id删除岗位（逻辑删除）*/
	@Override
	public int deleteByDptId(String enterpriseid, String dptId) throws DataAccessException {
		String sql = "UPDATE ent_" + enterpriseid + ".CFG_JOB SET STATUS = "+ JobStatusEnum.DELETE.value +", UPDATE_TIME = NOW() WHERE DPT_ID=?";
		Object[] params = new Object[] { dptId };
		int[] types = new int[] { Types.VARCHAR };
		return this.update(sql, params, types);
	}
	
	/*修改岗位*/
	@Override
	public int updateJob(JobPo po) throws DataAccessException {
		StringBuffer sqlBuffer = new StringBuffer();		
		sqlBuffer.append("UPDATE ent_").append(po.getEntId()).append(".CFG_JOB SET");
		
		if (StringUtils.isNotBlank(po.getName())) {
			sqlBuffer.append(" NAME = '").append(po.getName()).append("',");
		}
		if (StringUtils.isNotBlank(po.getDptId())) {
			sqlBuffer.append(" DPT_ID = '").append(po.getDptId()).append("',");
		}
		if (StringUtils.isNotBlank(po.getParentId())) {
			sqlBuffer.append(" PARENT_ID = '").append(po.getParentId()).append("',");
		}
		
		if (StringUtils.isNotBlank(po.getReamrk())) {
			sqlBuffer.append(" REAMRK = '").append(po.getReamrk()).append("',");
		}else{
			sqlBuffer.append(" REAMRK = '").append("").append("',");
		}
		
		if (StringUtils.isNotBlank(po.getLevel())) {
			sqlBuffer.append(" LEVEL = '").append(po.getLevel()).append("',");
		}	
		sqlBuffer.append(" UPDATE_TIME=NOW() WHERE ID=?");
		Object[] params = new Object[] { po.getId() };
		int[] types = new int[] { Types.VARCHAR };

		return this.update(sqlBuffer.toString(), params, types);
	}
	
	/*添加岗位*/
	@Override
	public int addJob(JobPo po) throws DataAccessException {
		String sql = "INSERT INTO ent_"+ po.getEntId() +".CFG_JOB (ID, NAME, ENT_ID, LEVEL, DPT_ID ,PARENT_ID, SORT_WEIGHT, REAMRK, STATUS, CREATE_TIME, UPDATE_TIME) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";
		Object[] params = new Object[] { po.getId(), po.getName(), po.getEntId(), po.getLevelInt(), po.getDptId(),
				po.getParentId(), po.getSortWeightInt(), po.getReamrk(), po.getStatus() };
		int[] types = new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.VARCHAR,
				Types.VARCHAR, Types.INTEGER, Types.VARCHAR, Types.VARCHAR };
		return this.update(sql, params, types);
	}
	
	/*查询上级部门的子部门,按Id升序*/
	@Override
	public List<DepartmentVo> queryDptByParentId(DepartmentPo po) throws DataAccessException {
		StringBuffer sqlBuffer = new StringBuffer();		
		sqlBuffer.append("SELECT CPD.* FROM ent_").append(po.getEntId()).append(".CFG_DEPARTMENT CPD WHERE 1=1");
		
		if (StringUtils.isNotBlank(po.getEntId())) {
			sqlBuffer.append(" AND CPD.ENT_ID = '").append(po.getEntId()).append("'");
		}
		if (StringUtils.isNotBlank(po.getParentId())) {
			sqlBuffer.append(" AND CPD.PARENT_ID = '").append(po.getParentId()).append("'");
		}
		sqlBuffer.append(" ORDER BY ID");
		return this.queryForList(sqlBuffer.toString(), DepartmentVo.class);
	}
	
	/*查询上级岗位的子岗位，按Id升序*/
	@Override
	public List<DepartmentVo> queryJobByParentId(JobPo po) throws DataAccessException {
		StringBuffer sqlBuffer = new StringBuffer();		
		sqlBuffer.append("SELECT CJ.* FROM ent_").append(po.getEntId()).append(".CFG_JOB CJ WHERE 1=1");
		
		if (StringUtils.isNotBlank(po.getEntId())) {
			sqlBuffer.append(" AND CJ.ENT_ID = '").append(po.getEntId()).append("'");
		}
		if (StringUtils.isNotBlank(po.getParentId())) {
			sqlBuffer.append(" AND CJ.PARENT_ID = '").append(po.getParentId()).append("'");
		}
		sqlBuffer.append(" ORDER BY ID");
		return this.queryForList(sqlBuffer.toString(), DepartmentVo.class);
	}
}

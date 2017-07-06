package com.channelsoft.ems.privilege.dao.impl;

import java.sql.Types;
import java.util.List;

import org.springframework.dao.DataAccessException;

import com.channelsoft.cri.dao.jdbc.BaseJdbcMysqlDao;
import com.channelsoft.cri.vo.PageInfo;
import com.channelsoft.ems.privilege.dao.ICfgRoleDao;
import com.channelsoft.ems.privilege.po.CfgRolePo;
import com.channelsoft.ems.sso.util.SsoSessionUtils;

public class CfgRoleDaoImpl extends BaseJdbcMysqlDao implements ICfgRoleDao {
	
	@Override
	public List<CfgRolePo> query(String enterpriseid, CfgRolePo po) throws DataAccessException {
		StringBuffer sqlBuffer = new StringBuffer("SELECT * FROM  ");
		sqlBuffer.append(SsoSessionUtils.getEntDB(enterpriseid));
		sqlBuffer.append(".CFG_ROLE WHERE 1=1");
		if (po.getId() != null) {
			sqlBuffer.append(" AND ID=").append(po.getId());
		}
		return this.queryForList(sqlBuffer.toString(), CfgRolePo.class);
	}

	@Override
	public List<CfgRolePo> getAll(String enterpriseid, PageInfo page)
			throws DataAccessException {
		StringBuffer sqlBuffer = new StringBuffer("SELECT * FROM  ");
		sqlBuffer.append(SsoSessionUtils.getEntDB(enterpriseid));
		sqlBuffer.append(".CFG_ROLE");
		return this.queryByPage(sqlBuffer.toString(), page, CfgRolePo.class);
	}

	@Override
	public int add(String enterpriseid, CfgRolePo po)
			throws DataAccessException {
		String sql = "INSERT INTO " + SsoSessionUtils.getEntDB(enterpriseid)
				+ ".CFG_ROLE (ID, NAME, DESCRIPTION, PARENT_ID, IS_CUSTOM) VALUES (?, ?, ?, ?, ?)";
		Object[] params = new Object[] { po.getId(), po.getName(), po.getDescription(), po.getParentId(), po.getIsCustom() };
		int[] types = new int[] { Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.VARCHAR };
		return this.update(sql, params, types);
	}

	@Override
	public int update(String enterpriseid, CfgRolePo po)
			throws DataAccessException {
		StringBuffer sqlBuffer = new StringBuffer("UPDATE ");
		sqlBuffer.append(SsoSessionUtils.getEntDB(enterpriseid));
		sqlBuffer.append(".CFG_ROLE SET");
		if (po.getName() != null) {
			sqlBuffer.append(" NAME = '").append(po.getName()).append("',");
		}
		if (po.getDescription() != null) {
			sqlBuffer.append(" DESCRIPTION = '").append(po.getDescription())
					.append("',");
		}
		if (po.getParentId() != null) {
			sqlBuffer.append(" PARENT_ID = '").append(po.getParentId())
					.append("',");
		}
		sqlBuffer.append(" UPDATE_TIME=NOW() WHERE ID=?");
		Object[] params = new Object[] { po.getId() };
		int[] types = new int[] { Types.INTEGER };

		return this.update(sqlBuffer.toString(), params, types);
	}

	@Override
	public int delete(String enterpriseid, String id)
			throws DataAccessException {
		String sql = "DELETE FROM " + SsoSessionUtils.getEntDB(enterpriseid)
				+ ".CFG_ROLE WHERE ID=?";
		Object[] params = new Object[] { id };
		int[] types = new int[] { Types.INTEGER };
		return this.update(sql, params, types);
	}

	@Override
	public boolean isExist(String enterpriseid, String roleName) {
		String sql = "SELECT COUNT(1) FROM "
				+ SsoSessionUtils.getEntDB(enterpriseid)
				+ ".CFG_ROLE WHERE NAME='" + roleName + "'";
		return this.queryForInt(sql) > 0;
	}

	@Override
	public List<CfgRolePo> queryForChildren(String enterpriseid, String id) {
		String sql = "SELECT * FROM " + SsoSessionUtils.getEntDB(enterpriseid)
				+ ".CFG_ROLE WHERE PARENT_ID=?";
		Object[] params = new Object[] { id };
		int[] types = new int[] { Types.INTEGER };
		return this.queryForList(sql, params, types, CfgRolePo.class);
	}
	
	@Override
	public String getSequence() throws DataAccessException {
		return this.getSequenceStr("SEQ_CFG_ROLE");
	}

}

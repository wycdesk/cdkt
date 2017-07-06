package com.channelsoft.ems.privilege.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import com.channelsoft.cri.dao.jdbc.BaseJdbcMysqlDao;
import com.channelsoft.ems.privilege.dao.ICfgRolePermissionDao;
import com.channelsoft.ems.privilege.po.CfgRolePermissionPo;
import com.channelsoft.ems.sso.util.SsoSessionUtils;

public class CfgRolePermissionDaoImpl extends BaseJdbcMysqlDao implements
		ICfgRolePermissionDao {

	@Override
	public void assignPermission(final String enterpriseid,final String roleId, final String menuIdList[])
			throws DataAccessException {
		String sql = "INSERT INTO "+SsoSessionUtils.getEntDB(enterpriseid)+".CFG_ROLE_PERMISSION (ROLE_ID, PERMISSION_ID) VALUES (?, ? )";
		getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
			public void setValues(PreparedStatement ps, int i)
					throws SQLException {
				String menuId = menuIdList[i];
				ps.setString(1, roleId);
				ps.setString(2, menuId);
			}

			public int getBatchSize() {
				return menuIdList.length;
			}
		});
	}

	@Override
	public List<CfgRolePermissionPo> queryForRolePermission(String roleId)
			throws DataAccessException {
		String sql = "SELECT * FROM CFG_ROLE_PERMISSION WHERE ROLE_ID=?";
		Object[] params = new Object[] { roleId };
		int[] types = new int[] { Types.INTEGER };
		return this.queryForList(sql, params, types, CfgRolePermissionPo.class);
	}

	@Override
	public int deleteRolePermission(String enterpriseid,String roleId) throws DataAccessException {
		String sql = "DELETE FROM "+SsoSessionUtils.getEntDB(enterpriseid)+".CFG_ROLE_PERMISSION WHERE ROLE_ID=?";
		Object[] params = new Object[] { roleId };
		int[] types = new int[] { Types.INTEGER };
		return this.update(sql, params, types);
	}

	@Override
	public void assignEntPermission(String enterpriseid, final String roleId,
			final String[] menuIdList) throws DataAccessException {

		StringBuffer sql = new StringBuffer("INSERT INTO");
		sql.append(SsoSessionUtils.getEntDB(enterpriseid));
		sql.append(".CFG_ROLE_PERMISSION (ROLE_ID, PERMISSION_ID) VALUES (?, ? )");

		getJdbcTemplate().batchUpdate(sql.toString(),
				new BatchPreparedStatementSetter() {
					public void setValues(PreparedStatement ps, int i)
							throws SQLException {
						String menuId = menuIdList[i];
						ps.setString(1, roleId);
						ps.setString(2, menuId);
					}

					public int getBatchSize() {
						return menuIdList.length;
					}
				});

	}

	@Override
	public int deleteEntRolePermission(String enterpriseid, String roleId)
			throws DataAccessException {

		StringBuffer sql = new StringBuffer("DELETE FROM ");
		sql.append(SsoSessionUtils.getEntDB(enterpriseid));
		sql.append(".CFG_ROLE_PERMISSION WHERE ROLE_ID=?");
		Object[] params = new Object[] { roleId };
		int[] types = new int[] { Types.INTEGER };
		return this.update(sql.toString(), params, types);

	}

	@Override
	public List<CfgRolePermissionPo> queryForRolePermission(
			String enterpriseid, String roleId) throws DataAccessException {
		StringBuffer sql = new StringBuffer("SELECT * FROM ");
		sql.append(SsoSessionUtils.getEntDB(enterpriseid));
		sql.append(".CFG_ROLE_PERMISSION WHERE ROLE_ID=?");

		Object[] params = new Object[] { roleId };
		int[] types = new int[] { Types.INTEGER };
		return this.queryForList(sql.toString(), params, types,
				CfgRolePermissionPo.class);
	}
}

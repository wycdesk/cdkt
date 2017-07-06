package com.channelsoft.ems.privilege.dao.impl;

import java.sql.Types;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;

import com.channelsoft.cri.dao.jdbc.BaseJdbcMysqlDao;
import com.channelsoft.ems.privilege.dao.ICfgPermissionDao;
import com.channelsoft.ems.privilege.po.CfgPermissionPo;
import com.channelsoft.ems.privilege.vo.CfgPermissionVo;
import com.channelsoft.ems.sso.util.SsoSessionUtils;

public class CfgPermissionDaoImpl extends BaseJdbcMysqlDao implements
		ICfgPermissionDao {

	@Override
	public List<CfgPermissionVo> query(String entId, CfgPermissionPo po)
			throws DataAccessException {
		StringBuffer sqlBuffer = new StringBuffer("SELECT CPE.* FROM CFG_PERMISSION CPE, CFG_PLATFORM CPL WHERE 1=1");
		if (StringUtils.isNotBlank(po.getActionUrl())) {
			sqlBuffer.append(" AND CPE.ACTION_URL LIKE '%")
					.append(po.getActionUrl()).append("%'");
		}
		if (StringUtils.isNotBlank(po.getId())) {
			sqlBuffer.append(" AND CPE.ID = ").append(po.getId());
		}
		if (StringUtils.isNotBlank(po.getName())) {
			sqlBuffer.append(" AND CPE.NAME LIKE '%").append(po.getName())
					.append("%'");
		}
		if (StringUtils.isNotBlank(po.getParentId())) {
			sqlBuffer.append(" AND CPE.PARENT_ID = ").append(po.getParentId());
		}
		if (StringUtils.isNotBlank(po.getPlatformId())) {
			sqlBuffer.append(" AND CPE.PLATFORM_ID = '")
					.append(po.getParentId()).append("'");
		}
		if (StringUtils.isNotBlank(po.getType())) {
			sqlBuffer.append(" AND CPE.TYPE = '").append(po.getType())
					.append("'");
		}
		if (StringUtils.isNotBlank(po.getVisible())) {
			sqlBuffer.append(" AND CPE.VISIBLE = '").append(po.getVisible())
					.append("'");
		}
		sqlBuffer.append(" AND CPE.PLATFORM_ID=CPL.ID ORDER BY SORT_WEIGHT");
		return this.queryForList(sqlBuffer.toString(), CfgPermissionVo.class);
	}

	@Override
	public List<CfgPermissionVo> queryByRole(String enterpriseid,
			String roleId, String type) throws DataAccessException {
		StringBuffer sqlBuffer = new StringBuffer("SELECT CP.*, CPF.ROOT_URL, CRP.ROLE_ID FROM CFG_PERMISSION CP, ");
		sqlBuffer.append(SsoSessionUtils.getEntDB(enterpriseid));
		sqlBuffer.append(".CFG_ROLE_PERMISSION CRP, CFG_PLATFORM CPF WHERE CP.ID=CRP.PERMISSION_ID AND CP.PLATFORM_ID=CPF.ID");
		sqlBuffer.append(" AND CRP.ROLE_ID=").append(roleId);

		if (StringUtils.isNotBlank(type)) {
			sqlBuffer.append(" AND CP.TYPE = '").append(type).append("'");
		}
		sqlBuffer.append(" AND CP.VISIBLE = '1'");
		sqlBuffer.append(" ORDER BY CP.SORT_WEIGHT");
		System.out.println("==========="+sqlBuffer.toString());
		return this.queryForList(sqlBuffer.toString(), CfgPermissionVo.class);
	}

	@Override
	public int add(CfgPermissionPo po) throws DataAccessException {
		String sql = "INSERT INTO CFG_PERMISSION (NAME, PLATFORM_ID, PARENT_ID, TYPE, VISIBLE, "
				+ "ACTION_URL, PARAMETER, DESCRIPTION, SORT_WEIGHT) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		Object[] params = new Object[] { po.getName(), po.getPlatformId(),
				po.getParentId(), po.getType(), po.getVisible(),
				po.getActionUrl(), po.getParameter(), po.getDescription(),
				po.getSortWeightInt() };
		int[] types = new int[] { Types.VARCHAR, Types.VARCHAR, Types.INTEGER,
				Types.CHAR, Types.CHAR, Types.VARCHAR, Types.VARCHAR,
				Types.VARCHAR, Types.INTEGER };
		return this.update(sql, params, types);
	}

	@Override
	public int update(CfgPermissionPo po) throws DataAccessException {
		StringBuffer sqlBuffer = new StringBuffer("UPDATE CFG_PERMISSION SET");
		if (po.getName() != null) {
			sqlBuffer.append(" NAME = '").append(po.getName()).append("',");
		}
		if (po.getPlatformId() != null) {
			sqlBuffer.append(" PLATFORM_ID = '").append(po.getPlatformId())
					.append("',");
		}
		if (po.getParentId() != null) {
			sqlBuffer.append(" PARENT_ID = '").append(po.getParentId())
					.append("',");
		}
		if (po.getType() != null) {
			sqlBuffer.append(" TYPE = '").append(po.getType()).append("',");
		}
		if (po.getVisible() != null) {
			sqlBuffer.append(" VISIBLE = '").append(po.getVisible())
					.append("',");
		}
		if (po.getActionUrl() != null) {
			sqlBuffer.append(" ACTION_URL = '").append(po.getActionUrl())
					.append("',");
		}
		if (po.getParameter() != null) {
			sqlBuffer.append(" PARAMETER = '").append(po.getParameter())
					.append("',");
		}
		if (po.getDescription() != null) {
			sqlBuffer.append(" DESCRIPTION = '").append(po.getDescription())
					.append("',");
		}
		if (po.getSortWeight() != null) {
			sqlBuffer.append(" SORT_WEIGHT = '").append(po.getSortWeightInt())
					.append("',");
		}
		sqlBuffer.append(" UPDATE_TIME=NOW() WHERE ID=?");
		Object[] params = new Object[] { po.getId() };
		int[] types = new int[] { Types.INTEGER };

		return this.update(sqlBuffer.toString(), params, types);
	}

	@Override
	public int delete(String id) throws DataAccessException {
		String sql = "DELETE FROM `portal1d3_crm1`.CFG_PERMISSION WHERE ID=?";
		Object[] params = new Object[] { id };
		int[] types = new int[] { Types.INTEGER };
		return this.update(sql, params, types);
	}

	@Override
	public List<CfgPermissionPo> queryForChildren(String parentId)
			throws DataAccessException {
		String sql = "SELECT * FROM `portal1d3_crm1`.CFG_PERMISSION WHERE PARENT_ID=? ORDER BY SORT_WEIGHT";
		Object[] params = new Object[] { parentId };
		int[] types = new int[] { Types.INTEGER };
		return this.queryForList(sql, params, types, CfgPermissionPo.class);
	}

	@Override
	public boolean hasPlatform(String id) throws DataAccessException {
		String sql = "SELECT COUNT(1) FROM CFG_PERMISSION WHERE PLATFORM_ID=?";
		Object[] params = new Object[] { id };
		int[] types = new int[] { Types.VARCHAR };
		return this.queryForInt(sql, params, types) > 0;
	}
}

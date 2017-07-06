package com.channelsoft.ems.ent.dao.impl;

import java.sql.Types;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;

import com.channelsoft.cri.dao.jdbc.BaseJdbcMysqlDao;
import com.channelsoft.cri.vo.PageInfo;
import com.channelsoft.ems.ent.dao.ICfgEntWxDao;
import com.channelsoft.ems.ent.po.CfgEntWxPo;
import com.channelsoft.ems.sso.util.SsoSessionUtils;

public class CfgEntWxDaoImpl extends BaseJdbcMysqlDao implements ICfgEntWxDao {

	@Override
	public List<CfgEntWxPo> query(String entId, CfgEntWxPo po, PageInfo page) throws DataAccessException {
		StringBuffer sqlBuffer = new StringBuffer("SELECT * FROM ");
		sqlBuffer.append(getEntDbName(entId));
		sqlBuffer.append(".CFG_ENT_WX WHERE 1=1");
		if (po.getOriginalId() != null) {
			sqlBuffer.append(" AND ORIGINAL_ID=").append(po.getOriginalId());
		}
		return this.queryByPage(sqlBuffer.toString(), page, CfgEntWxPo.class);
	}

	@Override
	public int add(String entId, CfgEntWxPo po) throws DataAccessException {
		String sql = "INSERT INTO " + getEntDbName(entId)
				+ ".CFG_ENT_WX (ORIGINAL_ID, WX_ACCOUNT, APP_ID, APP_SECRET, ENT_ID, CREATOR_ID, CREATOR_NAME, CREATE_TIME, REMARK)" +
				" VALUES (?, ?, ?, ?, ?, ?, ?, NOW(), ?)";
		Object[] params = new Object[] { po.getOriginalId(), po.getWxAccount(), po.getAppId(), po.getAppSecret(), po.getEntId(), po.getCreatorId(),
				po.getCreatorName(), po.getRemark()};
		int[] types = new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR };
		return this.update(sql, params, types);
	}

	@Override
	public int update(String entId, CfgEntWxPo po) throws DataAccessException {
		StringBuffer sqlBuffer = new StringBuffer("UPDATE ");
		sqlBuffer.append(getEntDbName(entId));
		sqlBuffer.append(".CFG_ENT_WX SET");
		if (po.getWxAccount() != null) {
			sqlBuffer.append(" WX_ACCOUNT = '").append(po.getWxAccount()).append("',");
		}
		if (po.getAppId() != null) {
			sqlBuffer.append(" APP_ID = '").append(po.getAppId()).append("',");
		}
		if (po.getAppSecret() != null) {
			sqlBuffer.append(" APP_SECRET = '").append(po.getAppSecret()).append("',");
		}
		if (po.getUpdatorId() != null) {
			sqlBuffer.append(" UPDATOR_ID = '").append(po.getUpdatorId()).append("',");
		}
		if (po.getUpdatorName() != null) {
			sqlBuffer.append(" UPDATOR_NAME = '").append(po.getUpdatorName()).append("',");
		}
		if (po.getRemark() != null) {
			sqlBuffer.append(" REMARK = '").append(po.getRemark()).append("',");
		}
		sqlBuffer.append(" UPDATE_TIME=NOW() WHERE ORIGINAL_ID=?");
		Object[] params = new Object[] { po.getOriginalId() };
		int[] types = new int[] { Types.VARCHAR };

		return this.update(sqlBuffer.toString(), params, types);
	}

	@Override
	public int delete(String entId, String originalId) throws DataAccessException {
		String sql = "DELETE FROM " + getEntDbName(entId) + ".CFG_ENT_WX WHERE ORIGINAL_ID=?";
		Object[] params = new Object[] { originalId };
		int[] types = new int[] { Types.VARCHAR };
		return this.update(sql, params, types);
	}


}

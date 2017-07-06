package com.channelsoft.ems.sso.dao.impl;

import java.sql.Types;

import org.springframework.dao.DataAccessException;

import com.channelsoft.cri.dao.jdbc.BaseJdbcMysqlDao;
import com.channelsoft.ems.sso.dao.IDatUserLoginDao;
import com.channelsoft.ems.sso.po.DatUserLoginPo;
import com.channelsoft.ems.sso.util.SsoSessionUtils;

public class DatUserLoginDaoImpl extends BaseJdbcMysqlDao implements
		IDatUserLoginDao {

	@Override
	public void add(String enterpriseid, DatUserLoginPo po)
			throws DataAccessException {
		StringBuffer sqlBuffer = new StringBuffer()
				.append("INSERT INTO ")
				.append(SsoSessionUtils.getEntDB(enterpriseid))
				.append(".DAT_USER_LOGIN(LOGIN_NAME, USER_NAME, USER_ID, LOGIN_IP, NICK_NAME, LOGIN_TIME, SESSION_KEY)")
				.append(" VALUES(?, ?, ?, ?, ?, NOW(), ?)");
		Object[] params = new Object[] { po.getLoginName(), po.getUserName(), po.getUserId(), po.getLoginIp(), po.getNickName(), po.getSessionKey() };
		int[] types = new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR };
		this.update(sqlBuffer.toString(), params, types);
	}

	@Override
	public void logout(String enterpriseid, String sessionKey)
			throws DataAccessException {
		String sql = "UPDATE " + SsoSessionUtils.getEntDB(enterpriseid)
				+ ".DAT_USER_LOGIN SET LOGOUT_TIME=NOW() WHERE SESSION_KEY = ?";
		Object[] params = new Object[] { sessionKey };
		int[] types = new int[] { Types.VARCHAR };
		this.update(sql, params, types);
	}


}

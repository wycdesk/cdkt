package com.channelsoft.ems.sso.dao.impl;

import java.sql.Types;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import com.channelsoft.cri.dao.jdbc.BaseJdbcMysqlDao;
import com.channelsoft.cri.vo.PageInfo;
import com.channelsoft.ems.sso.dao.IDatOnlineUserDao;
import com.channelsoft.ems.sso.po.DatOnlineUserPo;
import com.channelsoft.ems.sso.util.SsoSessionUtils;
import com.channelsoft.ems.sso.vo.DatOnlineUserVo;
import com.channelsoft.ems.user.po.DatEntUserPo;
import com.channelsoft.ems.user.service.IUserMongoService;

public class DatOnlineUserDaoImpl extends BaseJdbcMysqlDao implements IDatOnlineUserDao {

	@Autowired
	IUserMongoService userMongoService;
	
	@Override
	public void add(String enterpriseid, DatOnlineUserPo po)
			throws DataAccessException {
		StringBuffer sqlBuffer = new StringBuffer()
				.append("INSERT INTO ")
				.append(SsoSessionUtils.getEntDB(enterpriseid))
				.append(".DAT_ONLINE_USER(LOGIN_NAME, USER_NAME, USER_ID, LOGIN_IP, NICK_NAME, LOGIN_TIME, SESSION_KEY)")
				.append(" VALUES(?, ?, ?, ?, ?, NOW(), ?)");
		Object[] params = new Object[] { po.getLoginName(), po.getUserName(), po.getUserId(), po.getLoginIp(), po.getNickName(), po.getSessionKey() };
		int[] types = new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR };
		this.update(sqlBuffer.toString(), params, types);
	}

	@Override
	public void deleteByLoginName(String enterpriseid, String loginName)
			throws DataAccessException {
	 
		StringBuffer sqlBuffer = new StringBuffer("DELETE FROM ");
		sqlBuffer.append(SsoSessionUtils.getEntDB(enterpriseid));
		sqlBuffer.append(".DAT_ONLINE_USER WHERE LOGIN_NAME = ?");
		Object[] params = new Object[] { loginName };
		int[] types = new int[] { Types.VARCHAR };
		this.update(sqlBuffer.toString(), params, types);
	}

	@Override
	public List<DatOnlineUserPo> query(DatOnlineUserPo po, PageInfo page)
			throws DataAccessException {
		StringBuffer sqlBuffer = new StringBuffer(
				"SELECT * FROM DAT_ONLINE_USER WHERE 1=1");
		if (StringUtils.isNotBlank(po.getLoginName())) {
			sqlBuffer.append(" AND LOGIN_NAME='").append(po.getLoginName())
					.append("'");
		}
		if (StringUtils.isNotBlank(po.getSessionKey())) {
			sqlBuffer.append(" AND SESSION_KEY = '").append(po.getSessionKey())
					.append("'");
		}
		return this.queryByPage(sqlBuffer.toString(), page,
				DatOnlineUserPo.class);
	}

	@Override
	public void logout(String enterpriseid, String sessionKey)
			throws DataAccessException {
		String sql = "DELETE FROM " + SsoSessionUtils.getEntDB(enterpriseid)
				+ ".DAT_ONLINE_USER WHERE SESSION_KEY = ?";
		Object[] params = new Object[] { sessionKey };
		int[] types = new int[] { Types.VARCHAR };
		this.update(sql, params, types);
	}

	@Override
	public List<DatOnlineUserVo> queryUser(String enterpriseid, String sessionKey) throws DataAccessException {
		StringBuffer sqlBuffer = new StringBuffer("SELECT U.*, OU.SESSION_KEY, OU.LOGIN_TIME FROM ")
			.append(SsoSessionUtils.getEntDB(enterpriseid))
			.append(".DAT_ENT_USER U, ")
			.append(SsoSessionUtils.getEntDB(enterpriseid))
			.append(".DAT_ONLINE_USER OU WHERE U.USER_ID=OU.USER_ID")
			.append(" AND SESSION_KEY = ?");
		Object[] params = new Object[] { sessionKey };
		int[] types = new int[] { Types.VARCHAR };
		return this.queryForList(sqlBuffer.toString(), params, types,
				DatOnlineUserVo.class);
	}

	@Override
	public List<DatOnlineUserVo> queryUserForMongo(String enterpriseid,
			String sessionKey) throws DataAccessException {
		// TODO Auto-generated method stub
		StringBuffer sqlBuffer = new StringBuffer("SELECT OU.* FROM ")
		.append(SsoSessionUtils.getEntDB(enterpriseid))
		.append(".DAT_ONLINE_USER OU WHERE  SESSION_KEY = ?");
	   Object[] params = new Object[] { sessionKey };
	   int[] types = new int[] { Types.VARCHAR };
	   List<DatOnlineUserVo> list=this.queryForList(sqlBuffer.toString(), params, types,DatOnlineUserVo.class);
	   for(DatOnlineUserVo u:list){
		   String loginTime=u.getLoginTime();
		   DatEntUserPo user=userMongoService.queryUserById(enterpriseid, u.getUserId());
		   BeanUtils.copyProperties(user, u);
		   u.setSessionKey(sessionKey);
		   u.setLoginTime(loginTime);
	   }
	   
	   return list;
	}

}

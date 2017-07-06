package com.channelsoft.ems.register.dao.impl;

import java.sql.Types;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;

import com.channelsoft.cri.dao.jdbc.BaseJdbcMysqlDao;
import com.channelsoft.cri.util.SqlParamCheckUtil;
import com.channelsoft.cri.vo.PageInfo;
import com.channelsoft.ems.register.dao.IRegisterInfoDao;
import com.channelsoft.ems.register.po.RegisterInfoPo;


public class RegisterInfoDaoImpl extends BaseJdbcMysqlDao implements IRegisterInfoDao{

	@Override
	public List<RegisterInfoPo> query(RegisterInfoPo po, PageInfo page) throws DataAccessException {
		StringBuffer sqlBuffer = new StringBuffer("SELECT * FROM DAT_ENT_REGISTER WHERE 1=1 AND EMAIL='" + po.getEmail() + "'");
		
		System.out.println(sqlBuffer);
		return this.queryByPage(sqlBuffer.toString(), page, RegisterInfoPo.class);
	}
	
	
	@Override
	public void addRegister(RegisterInfoPo po) throws DataAccessException {
		
		//添加注册者信息		 
		StringBuffer sql = new StringBuffer();
		sql.append("insert into DAT_ENT_REGISTER (EMAIL,PIC_CODE,IP,ACTIVE_CODE,CREATE_TIME,STATUS,UPDATE_TIME) values ");		
		sql.append("(?,?,?,?,NOW(),?,NOW());");
		
		Object[] params = new Object[] {
				po.getEmail(),po.getPicCode(),po.getIp(),po.getActiveCode(),po.getStatus()
		};
		int[] types = new int[]{Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR
		};
		 
		this.update(sql.toString(), params, types);
	}
	
	@Override
	public void updateRegister(RegisterInfoPo registerPo) throws DataAccessException {		
		//更新注册者信息		 
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE DAT_ENT_REGISTER SET STATUS='1', UPDATE_TIME=NOW() WHERE ACTIVE_CODE=?");		
		Object[] params = new Object[] { registerPo.getActiveCode() };
		int[] types = new int[] { Types.VARCHAR };
		
		String check=SqlParamCheckUtil.check(sql.toString(), params, types) ;
		System.out.println(check);

		this.update(sql.toString(), params, types);
	}
	
	@Override
	public List<RegisterInfoPo> queryEmail(RegisterInfoPo po, PageInfo page) throws DataAccessException {
		StringBuffer sqlBuffer = new StringBuffer("SELECT EMAIL FROM  DAT_ENT_REGISTER WHERE 1=1 AND ACTIVE_CODE='"+po.getActiveCode()+"'");
		
		return this.queryByPage(sqlBuffer.toString(), page, RegisterInfoPo.class);
	}
	
	@Override
	public List<RegisterInfoPo> queryCreateTime(RegisterInfoPo po, PageInfo page) throws DataAccessException {
		StringBuffer sqlBuffer = new StringBuffer("SELECT CREATE_TIME FROM  DAT_ENT_REGISTER WHERE 1=1 AND ACTIVE_CODE='"+po.getActiveCode()+"'");
		
		return this.queryByPage(sqlBuffer.toString(), page, RegisterInfoPo.class);
	}
	
	@Override
	public List<RegisterInfoPo> queryLastMsg(RegisterInfoPo po, PageInfo page) throws DataAccessException {
		StringBuffer sqlBuffer = new StringBuffer("SELECT * FROM (SELECT * FROM DAT_ENT_REGISTER WHERE 1=1 AND EMAIL='"+po.getEmail()+"'");
		sqlBuffer.append(" ORDER BY CREATE_TIME DESC) AS a GROUP BY EMAIL");
		
		return this.queryByPage(sqlBuffer.toString(), page, RegisterInfoPo.class);
	}
	
}

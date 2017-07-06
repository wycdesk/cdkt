package com.channelsoft.ems.field.dao.impl;

import org.springframework.dao.DataAccessException;

import com.channelsoft.cri.dao.jdbc.BaseJdbcMysqlDao;
import com.channelsoft.ems.field.dao.IFieldDao;

public class FieldDaoImpl extends BaseJdbcMysqlDao implements IFieldDao{

	/*获取工单模板Id*/
	@Override
	public String getKey() throws DataAccessException {
		// TODO Auto-generated method stub
		return this.getSequenceStr("SEQ_FIELD");
	}

	 
}

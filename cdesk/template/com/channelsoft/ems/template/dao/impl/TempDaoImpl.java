package com.channelsoft.ems.template.dao.impl;

import org.springframework.dao.DataAccessException;

import com.channelsoft.cri.dao.jdbc.BaseJdbcMysqlDao;
import com.channelsoft.ems.template.dao.ITempDao;

public class TempDaoImpl extends BaseJdbcMysqlDao implements ITempDao{

	/*获取工单模板Id*/
	@Override
	public String getWorkTemplateId() throws DataAccessException {
		// TODO Auto-generated method stub
		return this.getSequenceStr("SEQ_TEMPLATE");
	}
 
}

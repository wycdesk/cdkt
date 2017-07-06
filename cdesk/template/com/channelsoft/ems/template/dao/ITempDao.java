package com.channelsoft.ems.template.dao;

import org.springframework.dao.DataAccessException;

public interface ITempDao {

	/*获取工单模板Id*/
	public String getWorkTemplateId() throws DataAccessException;
	
	 
}

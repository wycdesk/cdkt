package com.channelsoft.ems.field.dao;

import org.springframework.dao.DataAccessException;

public interface IFieldDao  {

	/*获取字段Id*/
	public String getKey() throws DataAccessException;
	
	 
}

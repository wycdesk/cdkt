package com.channelsoft.portal.logger.testcase;

import com.channelsoft.cri.logger.DBLogUtils;

import junit.framework.TestCase;

public class DBLogTest extends TestCase{
	public void testInsertSuccess()
	{
		DBLogUtils.InsertSuccess("INSERT INTO DAT_USER_INFO(ID, NAME) VALUES ('1', '测试');", 1);
	}
	
	public void testInsertFail()
	{
		DBLogUtils.InsertFail("INSERT INTO DAT_USER_INFO(ID, NAME) VALUES ('1', '测试';");
	}
	
	public void testUpdateSuccess()
	{
		DBLogUtils.UpdateSuccess("UPDATE DAT_USER_INFO SET NAME='测试' WHERE ID=1", 1);
	}
	
	public void testUpdateFail()
	{
		DBLogUtils.UpdateFail("UPDATE DAT_USER_INFO SET NAME='测试' WHERE ID=1)");
	}
	
	public void testDeleteSuccess()
	{
		DBLogUtils.DeleteSuccess("DELETE FROM DAT_USER_INFO SET NAME='测试' WHERE ID=1", 1);
	}
	
	public void testDeleteFail()
	{
		DBLogUtils.DeleteFail("DELETE FROM DAT_USER_INFO WHERE ID=");
	}
	
	public void testQuerySuccess()
	{
		DBLogUtils.QuerySuccess("SELECT * FROM DAT_USER_INFO", 100);
	}
	
	public void testQueryFail()
	{
		DBLogUtils.QueryFail("SELECT & FROM DAT_USER_INFO");
	}
}

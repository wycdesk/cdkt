package com.channelsoft.cri.test;

import org.springframework.transaction.annotation.Transactional;
/**
 * 涉及到处理数据前需要到数据库判断条件的操作
 * 需要按照此流程：
 * 开启同步锁（静态变量方式）=> 判断条件 => try => 开启事务 => 多个数据库操作 =>完成事务 => catch => 结束同步锁
 * @author Administrator
 *
 */
public class SyncExample {
	/**
	 * 某个Control层的入口方法
	 * 首先进入service层提供的方法，该方法不能加transactional
	 * @param p1
	 */
	public void ControllerMethod1(String p1)
	{
		this.doOperation(p1);
	}
	
	/**
	 * 同步锁，被该变量锁定的代码和方法都会进行同步锁定
	 * 必须要用static
	 */
	private static Object syncObject = new Object();
	/**
	 * service层的方法，不能加transactional
	 * 同步锁必须在transational外面
	 * @param p1
	 */
	private void doOperation(String p1) {
		synchronized (syncObject)
		{
			if (this.isExist(p1))
			{
				try {
					deal(p1);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 接下来才是需要事务处理的数据库变更操作。
	 * 在transational的首层方法，不能进行处理catch到的Exception
	 * (抓住了也要抛出去，不然操作不会回滚）
	 * @param p1
	 */
	@Transactional
	private void deal(String p1) {
		
	}
	/**
	 * 数据库查询判断的方法也不能加入transactional
	 * @param p1
	 * @return
	 */
	private boolean isExist(String p1) {
		return true;
	}
}

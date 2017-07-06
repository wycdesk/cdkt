package com.channelsoft.ems.redis.util;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.channelsoft.ems.redis.task.ParamCacheQuartzTask;


public class EmsInitListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		ParamCacheQuartzTask task=new ParamCacheQuartzTask();
		task.initCache();

	}

}

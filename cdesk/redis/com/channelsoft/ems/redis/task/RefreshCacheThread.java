package com.channelsoft.ems.redis.task;

import com.channelsoft.cri.util.BeanFactoryUtil;
import com.channelsoft.ems.redis.constant.CacheGroup;
import com.channelsoft.ems.redis.service.IParamCacheService;

public class RefreshCacheThread extends Thread {
	private static IParamCacheService paramCacheService;
	
	public RefreshCacheThread(CacheGroup group, String entId) {
		this.group = group;
		this.entId = entId;
	}
	private CacheGroup group;
	private String entId;
	
	public void run() {
		try {
			if (paramCacheService == null) {
				paramCacheService = (IParamCacheService)BeanFactoryUtil.getBean("paramRedisService");
			}
			paramCacheService.refreshFullCache(group, entId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

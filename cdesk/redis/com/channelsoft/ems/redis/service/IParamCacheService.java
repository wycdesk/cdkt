package com.channelsoft.ems.redis.service;

import com.channelsoft.ems.redis.constant.CacheGroup;


public interface IParamCacheService {
	public void flushFullCache(CacheGroup group, String entId);

	public void refreshFullCache(CacheGroup group, String entId);
}

package com.channelsoft.ems.redis.service;

public interface IMemCachedService {

	public boolean set(String key, Object value, String group, long time);

	public Object get(String key, String group);

	public boolean flushAll();

}

package com.channelsoft.ems.redis.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.channelsoft.cri.service.BaseService;
import com.channelsoft.ems.redis.service.IMemCachedService;
import com.danga.MemCached.MemCachedClient;

public class MemCachedServiceImpl extends BaseService implements IMemCachedService {
	@Autowired
	MemCachedClient memcachedClient;
	
	private static String setGroupKey(String key, String group){
		key += "_GROUP_" + group;
		return key;
	}
	@Override
	public boolean set(String key, Object value, String group, long time)
	{
		try {
			key = setGroupKey(key, group);
			return memcachedClient.set(key, value, new Date(time));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	@Override
	public Object get(String key, String group)
	{
		try {
			key = setGroupKey(key, group);
			return memcachedClient.get(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public boolean flushAll()
	{
		try {
			return memcachedClient.flushAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}

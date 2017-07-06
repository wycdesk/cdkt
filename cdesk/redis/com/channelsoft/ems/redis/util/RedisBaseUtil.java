package com.channelsoft.ems.redis.util;
import com.channelsoft.cri.cache.redis.JedisTemplate;
import com.channelsoft.cri.util.BeanFactoryUtil;

public class RedisBaseUtil {

	
	private static JedisTemplate jedisTemplate;
	/**
	 * 获取实例
	 */
	public static JedisTemplate getJedisInstance(){
		if(jedisTemplate == null){
			jedisTemplate = (JedisTemplate) BeanFactoryUtil.getBean("jedisTemplate");
		}
		return jedisTemplate;
	}
}

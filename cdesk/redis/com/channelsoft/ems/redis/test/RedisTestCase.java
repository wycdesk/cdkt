package com.channelsoft.ems.redis.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.JsonMappingException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.channelsoft.cri.cache.redis.JedisTemplate;
import com.channelsoft.cri.test.SpringTestCase;
import com.channelsoft.ems.group.po.GroupPo;
import com.channelsoft.ems.redis.constant.CacheGroup;


public class RedisTestCase extends SpringTestCase {
	private static final Logger logger = LoggerFactory.getLogger(RedisTestCase.class);
	@Autowired
	JedisTemplate jedisTemplate;

	@Test
	public void testHash() throws JsonMappingException, IOException {
		Map<String,GroupPo> obj = jedisTemplate.hgetAll("322_MAP", GroupPo.class, CacheGroup.GROUP.name);
		Map<String, String> hash = new HashMap<String, String>();
		hash.put("atesta", "111");

		String aa=jedisTemplate.hmset("atesta", hash);
		String bb=jedisTemplate.get("111");
		logger.debug("testHash：[key=" + "atesta" + ", aa=" +aa + "]");


		
	}

}
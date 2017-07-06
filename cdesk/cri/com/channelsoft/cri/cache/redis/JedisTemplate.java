package com.channelsoft.cri.cache.redis;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.channelsoft.cri.cache.redis.utils.SerializeUtils;
import com.channelsoft.cri.util.JsonUtils;
import com.channelsoft.cri.util.WebappConfigUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisException;
/**
 * Jedis Spring Template封装类
 * <dl>
 * <dt>Redis_0.2</dt>
 * <dd>Description:</dd>
 * <dd>Copyright: Copyright (C) 2013</dd>
 * <dd>Company: 青牛(北京)技术有限公司 成都研究所</dd>
 * <dd>CreateDate: 2016年3月23日</dd>
 * </dl>
 * @author 魏铭
 */
public class JedisTemplate {
	
	private static Logger log = LoggerFactory.getLogger(JedisTemplate.class);	
	private static String redis_instance_id=WebappConfigUtil.getParameter("redis_group_id");
	
	/**
	 * 支持单实例的Redis
	 */
//	private static JedisPool jedisPool;
//	public void setJedisPool(JedisPool jedisPool) {
//		JedisTemplate.jedisPool = jedisPool;
//	}
//	/**
//	 * 通过观察者模式实现事件的回调处理（使用范型）
//	 * 其目的是从连接池中获取一个存活的jedis实例，然后向该实例执行缓存操作。
//	 * @param jedisAction
//	 * @return
//	 * @throws JedisException
//	 * @CreateDate: 2016年3月23日 下午6:11:30
//	 * @author 魏铭
//	 */
//	public static <T> T execute(JedisAction<T> jedisAction) throws JedisException {
//		Jedis jedis = null;
//		try {
//			jedis = jedisPool.getResource();
//			return jedisAction.action(jedis);
//		} catch (JedisConnectionException e) {
//			log.error("Redis connection lost.", e);
//			throw e;
//		} finally {
//			//查看jedis源码，现在jedis可以自动判断连接是否是broken状态
//			if(jedis!=null) {
//				jedis.close();
//			}
//		}
//	}
//	/**
//	 * 通过观察者模式实现事件的回调处理
//	 * 其目的是从连接池中获取一个存活的jedis实例，然后向该实例执行缓存操作。
//	 * @param jedisAction
//	 * @throws JedisException
//	 * @CreateDate: 2016年3月23日 下午6:13:53
//	 * @author 魏铭
//	 */
//	public static void execute(JedisActionNoResult jedisAction) throws JedisException {
//		Jedis jedis = null;
//		try {
//			jedis = jedisPool.getResource();
//			jedisAction.action(jedis);
//		} catch (JedisConnectionException e) {
//			log.error("Redis connection lost.", e);
//			throw e;
//		} finally {
//			//查看jedis源码，现在jedis可以自动判断连接是否是broken状态
//			if(jedis!=null) {
//				jedis.close();
//			}
//		}
//	}
	/**
	 * applicationContext_jedis.xml中配置的Redis连接池
	 * 支持多个Redis实例地址。
	 */
	private static JedisSentinelPool sentinelPool;
	public void setJedisSentinelPool(JedisSentinelPool sentinelPool) {
		JedisTemplate.sentinelPool = sentinelPool;
	}
	public JedisTemplate() {
		
	}
	public static <T> T execute(JedisAction<T> jedisAction) throws JedisException {
		Jedis jedis = null;
		try {
			jedis = sentinelPool.getResource();
			return jedisAction.action(jedis);
		} catch (JedisConnectionException e) {
			log.error("Redis connection lost.", e);
			throw e;
		} finally {
			//查看jedis源码，现在jedis可以自动判断连接是否是broken状态
			if(jedis!=null) {
				jedis.close();
			}
		}
	}
	
	/**
	 * 通过观察者模式实现事件的回调处理
	 * 其目的是从连接池中获取一个存活的jedis实例，然后向该实例执行缓存操作。
	 * @param jedisAction
	 * @throws JedisException
	 * @CreateDate: 2016年3月23日 下午6:13:53
	 * @author 魏铭
	 */
	public static void execute(JedisActionNoResult jedisAction) throws JedisException {
		Jedis jedis = null;
		try {
			jedis = sentinelPool.getResource();
			jedisAction.action(jedis);
		} catch (JedisConnectionException e) {
			log.error("Redis connection lost.", e);
			throw e;
		} finally {
			//查看jedis源码，现在jedis可以自动判断连接是否是broken状态
			if(jedis!=null) {
				jedis.close();
			}
		}
	}
	private static String setGroupKey(String key, String group){
		key += redis_instance_id+"_GROUP_"+group;
		return key;
	}
	
	/**
	 * 以管道模式批量写入Redis数据
	 * 限制为最多1W条。
	 */
	private static int pipelineMax = 10000;
	/**
	 * 有返回结果的回调接口定义。
	 */
	public static interface JedisAction<T> {
		T action(Jedis jedis);
	}
	/**
	 * 无返回结果的回调接口定义。
	 */
	private static interface JedisActionNoResult {
		void action(Jedis jedis);
	}
	
	

	
	
	/*
	 * 以下为jedis提供的基本写入、获取、删除方法
	 * 通过观察者模式进行封装
	 */
	/**
	 * 基本方法：设置某个key的值(String)
	 * @param key
	 * @param value
	 * @return OK=成功
	 * @CreateDate: 2016年3月23日 下午9:50:40
	 * @author 魏铭
	 */
	public String set(final String key,final String value) {
		return execute(new JedisAction<String>() {
			public String action(Jedis jedis) {
				return jedis.set(key, value);
			}
		});
	}
	public String set(final String key,final String value,final String group) {
		return execute(new JedisAction<String>() {
			public String action(Jedis jedis) {
				return jedis.set(setGroupKey(key,group), value);
			}
		});
	}
	/**
	 * 基本方法：获取某个key的值(String)
	 * @param key
	 * @return
	 * @CreateDate: 2016年3月23日 下午10:06:20
	 * @author 魏铭
	 */
	public String get(final String key) {
		return execute(new JedisAction<String>() {
			public String action(Jedis jedis) {
				return jedis.get(key);
			}
		});
	}
	public String get(final String key,final String group) {
		return execute(new JedisAction<String>() {
			public String action(Jedis jedis) {
				return jedis.get(setGroupKey(key,group));
			}
		});
	}
	/**
	 * 基本方法：啥拿出某个key的值(String)
	 * @param key
	 * @return 删除的条数
	 * @CreateDate: 2016年3月23日 下午10:06:20
	 * @author 魏铭
	 */
	public Long del(final String key) {
		return execute(new JedisAction<Long>() {
			public Long action(Jedis jedis) {
				return jedis.del(key);
			}
		});
	}
	
	/**
	 * 基本方法：设置某个key的值(二进制)
	 * @param key
	 * @param value
	 * @return OK=成功
	 * @CreateDate: 2016年3月23日 下午9:50:40
	 * @author 魏铭
	 */
	public String set(final byte[] key, final byte[] value) {
		return execute(new JedisAction<String>() {
			public String action(Jedis jedis) {
				return jedis.set(key, value);
			}
		});
	}
	
	/**
	 * 基本方法：获取某个key的值(二进制)
	 * @param key
	 * @return 
	 * @CreateDate: 2016年3月23日 下午10:06:20
	 * @author 魏铭
	 */
	public byte[] get(final byte[] key) {
		return execute(new JedisAction<byte[]>() {
			public byte[] action(Jedis jedis) {
				return jedis.get(key);
			}
		});
	}
	
	/**
	 * 基本方法：删除某个key的值(二进制)
	 * @param key
	 * @return
	 * @CreateDate: 2016年3月23日 下午10:06:20
	 * @author 魏铭
	 */
	public Long del(final byte[] key) {
		return execute(new JedisAction<Long>() {
			public Long action(Jedis jedis) {
				return jedis.del(key);
			}
		});
	}
	
	/**
	 * 基础方法：写入整个HashMap对象(String)
	 * @param key
	 * @param map
	 * @return OK=成功
	 * @CreateDate: 2016年3月23日 下午8:37:47
	 * @author 魏铭
	 */
	public String hmset(final String key, final Map<String, String> map) {
		if(map==null ||map.isEmpty()) return "OK";

		return execute(new JedisAction<String>() {
			public String action(Jedis jedis) {
				jedis.del(key);
				return jedis.hmset(key, map);
			}
		});
	}
	
	public String hmset(final String key, final Map<String, String> map,final String group) {
		return execute(new JedisAction<String>() {
			public String action(Jedis jedis) {
				jedis.del(key);
				return jedis.hmset(setGroupKey(key,group), map);
			}
		});
	}
	/**
	 * 基础方法：写入整个HashMap对象(二进制)
	 * @param key
	 * @param map
	 * @return OK=成功
	 * @CreateDate: 2016年3月23日 下午9:34:12
	 * @author 魏铭
	 */
	public String hmset(final byte[] key, final Map<byte[], byte[]>map) {
		return execute(new JedisAction<String>() {
			public String action(Jedis jedis) {
				jedis.del(key);
				return jedis.hmset(key, map);
			}
		});
	}
	/**
	 * 基础方法：设置map对象中某个field的值(String)
	 * @param key set对象的键值
	 * @param field 要操作的对象在map中的键值(filed值)
	 * @param value 要设置的值
	 * @return 0=成功
	 * @CreateDate: 2016年3月23日 下午6:14:50
	 * @author 魏铭
	 */
	public Long hset(final String key, final String field, final String value) {
		return execute(new JedisAction<Long>() {
			public Long action(Jedis jedis) {
				return jedis.hset(key, field, value);
			}
		});
	}
	/**
	 * 基础方法：设置Set对象中某个field的值(二进制)
	 * @param key
	 * @param field
	 * @param value
	 * @return
	 * @CreateDate: 2016年3月23日 下午9:54:59
	 * @author 魏铭
	 */
	public Long hset(final byte[] key, final byte[] field, final byte[] value) {
		return execute(new JedisAction<Long>() {
			public Long action(Jedis jedis) {
				return jedis.hset(key, field, value);
			}
		});
	}
	/**
	 * 基础方法：获取map对象中某个field的值(String)
	 * @param key
	 * @param field
	 * @return
	 * @CreateDate: 2016年3月23日 下午10:15:33
	 * @author 魏铭
	 */
	public String hget(final String key, final String field) {
		return execute(new JedisAction<String>() {
			public String action(Jedis jedis) {
				return jedis.hget(key, field);
			}
		});
	}
	/**
	 * 基础方法：获取map对象中某个field的值(二进制)
	 * @param key
	 * @param field
	 * @return
	 * @CreateDate: 2016年3月23日 下午10:15:33
	 * @author 魏铭
	 */
	public byte[] hget(final byte[] key, final byte[] field) {
		return execute(new JedisAction<byte[]>() {
			public byte[] action(Jedis jedis) {
				return jedis.hget(key, field);
			}
		});
	}
	
	/**
	 * 基础方法：删除map对象中指定的key对应的数据
	 * @param key map对应的键值
	 * @param fields数组
	 * @return
	 * @CreateDate: 2016年3月23日 下午7:29:41
	 * @author 魏铭
	 */
	public long hdel(final String key, final String... fields) {
		return execute(new JedisAction<Long>() {
			public Long action(Jedis jedis) {
				return jedis.hdel(key, fields);
			}
		});
	}
	/**
	 * 基础方法：获取整个map对象(String)
	 * @param key
	 * @param field
	 * @return
	 * @CreateDate: 2016年3月23日 下午10:15:33
	 * @author 魏铭
	 */
	public Map<String, String> hgetAll(final String key) {
		return execute(new JedisAction<Map<String, String>>() {
			public Map<String, String> action(Jedis jedis) {
				return jedis.hgetAll(key);
			}
		});
	}
	public Map<String, String> hgetAll(final String key,final String group) {
		return execute(new JedisAction<Map<String, String>>() {
			public Map<String, String> action(Jedis jedis) {
				return jedis.hgetAll(setGroupKey(key,group));
			}
		});
	}
	/**
	 * 基础方法：获取整个map对象(二进制)
	 * @param key
	 * @param field
	 * @return
	 * @CreateDate: 2016年3月23日 下午10:15:33
	 * @author 魏铭
	 */
	public Map<byte[], byte[]> hgetAll(final byte[] key) {
		return execute(new JedisAction<Map<byte[], byte[]>>() {
			public Map<byte[], byte[]> action(Jedis jedis) {
				return jedis.hgetAll(key);
			}
		});
	}
	
	/*
	 * 以下为封装的扩展方法。 
	 */
	/**
	 * 扩展方法：设置某个key的值(Json)
	 * @param key
	 * @param t
	 * @return
	 * @throws JsonMappingException
	 * @throws IOException
	 * @CreateDate: 2016年3月23日 下午9:59:41
	 * @author 魏铭
	 */
	public <T> String set(final String key, final T t) throws JsonMappingException, IOException {
		return this.set(key, JsonUtils.toJson(t));
	}
	/**
	 *扩展方法： 获取某个key的值(Json)
	 * @param key
	 * @param clazz
	 * @return
	 * @throws JsonMappingException
	 * @throws IOException
	 * @CreateDate: 2016年3月23日 下午10:28:37
	 * @author 魏铭
	 */
	public <T> T get(final String key, final Class<T> clazz) throws JsonMappingException, IOException {
		String json = this.get(key);
		if (StringUtils.isEmpty(json)) {
			return null;
		}
		@SuppressWarnings("unchecked")
		T t = (T)JSON.toJSONString(json);
		return t;
	}
	
	/**
	 * 扩展方法：设置某个key的值(序列化)
	 * @param key
	 * @param value
	 * @return
	 * @CreateDate: 2016年3月23日 下午9:50:40
	 * @author 魏铭
	 */
	public <T> String setSerial(final String key, final T t,final String group) {
		return this.set(key.getBytes(), SerializeUtils.serialize(t));
	}
	/**
	 * 扩展方法：获取某个key的值(序列化)
	 * @param key
	 * @param clazz
	 * @return
	 * @throws JsonMappingException
	 * @throws IOException
	 * @CreateDate: 2016年3月23日 下午10:28:37
	 * @author 魏铭
	 */
	public <T> T getSerial(final String key, final Class<T> clazz) throws JsonMappingException, IOException {
		byte[] bytes = this.get(key.getBytes());
		@SuppressWarnings("unchecked")
		T t = (T)SerializeUtils.deserialize(bytes);
		return t;
	}
	/**
	 * 扩展方法：将HashMap对象转化为散列数据以管道方式进行写入。
	 * 每条数据的键值 = 整个map的主键 _对象在map中的键值
	 * 此方法暂时不完整，建议和前面的hmset进行性能比较
	 * TODO:缺少全部获取的方法
	 * @param key
	 * @param map
	 * @return
	 * @CreateDate: 2016年3月23日 下午8:48:58
	 * @author 魏铭
	 */
	public void hmsetPipe(final String key, final Map<String, String>map) {
		execute(new JedisActionNoResult() {
			 public void action(Jedis jedis) {
				Pipeline p = jedis.pipelined();
				int j=0;
				for(Entry<String, String> e : map.entrySet()) {
					String childKey = key + "_" + e.getKey();
					p.set(childKey, e.getValue());
					j++;
					if(j%pipelineMax==0) {
						p.sync();
					}
				}
				if(j%pipelineMax!=0) {
					p.sync();
				}
			}
		});
	}
	
	/**
	 * 扩展方法：写入整个HashMap对象(JSON)
	 * 建议和后面的hmsetSerial以及Pipe方法进行性能比较
	 * @param key 整个map对象的KEY值
	 * @param map
	 * @return
	 * @CreateDate: 2016年3月23日 下午8:37:47
	 * @author 魏铭
	 * @throws IOException 
	 * @throws JsonMappingException 
	 */
	public <T> String hmsetJson(final String key, final Map<String, T>map,final String group) throws JsonMappingException, IOException {
		Map<String, String> hash = new HashMap<String, String>();
		for (Map.Entry<String, T> entry : map.entrySet()) {
			String value = JSON.toJSONString(entry.getValue());
			hash.put(entry.getKey(), value);
		}
		return this.hmset(setGroupKey(key, group), hash);
	}
	/**
	 * 扩展方法：设置map对象中某个field的值(Json)
	 * @param key map对应的主键
	 * @param field 对象对应的field
	 * @param value
	 * @return 0表示成功。
	 * @CreateDate: 2016年3月23日 下午9:46:39
	 * @author 魏铭
	 * @throws IOException 
	 * @throws JsonMappingException 
	 */
	public <T> Long hsetJson(final String key, final String field, final T t) throws JsonMappingException, IOException {
		String value = JSON.toJSONString(t);
		return this.hset(key, field, value);
	}
	/**
	 * 扩展方法：获取map对象中某个field的值(Json)
	 * @param key map对应的主键
	 * @param field 对象对应的field
	 * @param clazz
	 * @return
	 * @throws JsonMappingException
	 * @throws IOException
	 * @CreateDate: 2016年3月23日 下午10:40:23
	 * @author 魏铭
	 */
	public <T> T hget(final String key, final String field, final Class<T> clazz) throws JsonMappingException, IOException {
		String json = this.hget(key, field);
		if (StringUtils.isEmpty(json)) {
			return null;
		}
		@SuppressWarnings("unchecked")
		T t = (T)JSON.parseObject(json, clazz);
		return t;
	}
	/**
	 * 扩展方法：删除map中某个field对应的值(Json、二进制均可)
	 * @param key map对应的主键
	 * @param field
	 * @return
	 * @throws JsonMappingException
	 * @throws IOException
	 * @CreateDate: 2016年3月23日 下午10:43:53
	 * @author 魏铭
	 */
	public Long hdel(final String key, final String field) throws JsonMappingException, IOException {
		return this.hdel(key, new String[] {field});
	}
	/**
	 * 扩展方法：获取整个map对象(Json)
	 * @param key map对应的主键
	 * @param clazz
	 * @return
	 * @throws JsonMappingException
	 * @throws IOException
	 * @CreateDate: 2016年3月23日 下午10:40:23
	 * @author 魏铭
	 */
	public <T> Map<String, T> hgetAll(final String key, final Class<T> clazz,final String group) throws JsonMappingException, IOException {
		Map<String, String> hash = this.hgetAll(setGroupKey(key, group));
		if (hash == null) {
			return null;
		}
		Map<String, T> map = new HashMap<String, T>();
		for (Map.Entry<String, String> entry : hash.entrySet()) {
			String json=entry.getValue();
			if (StringUtils.isEmpty(json)) {
				continue;
			}
			@SuppressWarnings("unchecked")
			T t = (T)JSON.parseObject(json, clazz);
			map.put(entry.getKey(), t);
		}

		return map;
	}
	
	/**
	 * 将HashMap对象转化为散列Pipe写入(Json+散列)
	 * @param key
	 * @param map
	 * @throws JsonMappingException
	 * @throws IOException
	 * @CreateDate: 2016年3月23日 下午9:27:16
	 * @author 魏铭
	 */
	public <T> void hmsetJsonPipe(final String key, final Map<String, T>map) throws JsonMappingException, IOException {
		Map<String, String> hash = new HashMap<String, String>();
		for (Map.Entry<String, T> entry : map.entrySet()) {
			String value = JSON.toJSONString(entry.getValue());
			hash.put(entry.getKey(), value);
		}
		this.hmsetPipe(key, hash);
	}
	/**
	 * 扩展方法：设置管道模式写入的map对象中某个field的值(Json+散列)
	 * @param key
	 * @param field
	 * @param t
	 * @return
	 * @throws JsonMappingException
	 * @throws IOException
	 * @CreateDate: 2016年3月23日 下午9:51:23
	 * @author 魏铭
	 */
	public <T> String hsetJsonPipe(final String key, final String field, final T t) throws JsonMappingException, IOException {
		String childKey = key+ "_" + field;
		String value = JSON.toJSONString(t);
		return this.set(childKey, value);
	}
	/**
	 * 扩展方法：获取管道模式写入的map对象中某个field的值(Json+散列)
	 * @param key
	 * @param field
	 * @param clazz
	 * @return
	 * @throws JsonMappingException
	 * @throws IOException
	 * @CreateDate: 2016年3月23日 下午11:03:11
	 * @author 魏铭
	 */
	public <T> T hgetJsonPipe(final String key, final String field, final Class<T> clazz) throws JsonMappingException, IOException {
		String childKey = key+ "_" + field;
		String value = this.get(childKey);
		if (StringUtils.isEmpty(value)) {
			return null;
		}
		@SuppressWarnings("unchecked")
		T t = (T)JSON.parseObject(value,clazz);
		return t;
	}
	/**
	 *  扩展方法：删除管道模式写入的map对象中某个field的值(Json+散列)
	 * @param key
	 * @param field
	 * @return
	 * @throws JsonMappingException
	 * @throws IOException
	 * @CreateDate: 2016年3月23日 下午11:04:42
	 * @author 魏铭
	 */
	public Long hdelJsonPipe(final String key, final String field) throws JsonMappingException, IOException {
		String childKey = key+ "_" + field;
		return this.del(childKey);
	} 

	/**
	 * 扩展方法写入整个HashMap对象(序列化)
	 * 建议和hmsetJson、Pipe进行性能比较
	 * @param key
	 * @param map
	 * @return
	 * @throws JsonMappingException
	 * @throws IOException
	 * @CreateDate: 2016年3月23日 下午9:03:16
	 * @author 魏铭
	 */
	public <T> String hmsetSerial(final String key, final Map<String, T>map) {
		Map<byte[], byte[]> hash = new HashMap<byte[], byte[]>();
		for (Map.Entry<String, T> entry : map.entrySet()) {
			T t = entry.getValue();
			hash.put(entry.getKey().getBytes(), SerializeUtils.serialize(t));
		}
		return this.hmset(key.getBytes(), hash);
	}
	/**
	 * 扩展方法：设置map对象中某个field的值(序列化)
	 * @param key map对应的主键
	 * @param field 对象对应的field
	 * @param value
	 * @return
	 * @CreateDate: 2016年3月23日 下午9:46:39
	 * @author 魏铭
	 * @throws IOException 
	 * @throws JsonMappingException 
	 */
	public <T> Long hsetSerial(final String key, final String field, final T t) {
		return this.hset(key.getBytes(), field.getBytes(), SerializeUtils.serialize(t));
	}
	/**
	 * 扩展方法：获取map对象中某个field的值(序列化)
	 * @param key map对应的主键
	 * @param field 对象对应的field
	 * @param clazz
	 * @return
	 * @throws JsonMappingException
	 * @throws IOException
	 * @CreateDate: 2016年3月23日 下午10:40:23
	 * @author 魏铭
	 */
	public <T> T hgetSerial(final String key, final String field) {
		byte[] bytes = this.hget(key.getBytes(), field.getBytes());
		@SuppressWarnings("unchecked")
		T t = (T)SerializeUtils.deserialize(bytes);
		return t;
	}
	
	/**
	 * 扩展方法：获取整个map对象(序列化)
	 * @param key map对应的主键
	 * @param clazz
	 * @return
	 * @throws JsonMappingException
	 * @throws IOException
	 * @CreateDate: 2016年3月23日 下午10:40:23
	 * @author 魏铭
	 */
	public <T> Map<String, T> hgetAllSerial(final String key) {
		Map<byte[], byte[]> hash = this.hgetAll(key.getBytes());
		Map<String, T> map = new HashMap<String, T>();
		for (Map.Entry<byte[], byte[]> entry : hash.entrySet()) {
			@SuppressWarnings("unchecked")
			T t = (T)SerializeUtils.deserialize(entry.getValue());
			map.put(new String(entry.getKey()), t);
		}
		return map;
	}
	
	/**
	 * 扩展方法：将HashMap对象转化为散列Pipe写入(序列化+散列)
	 * @param key
	 * @param map
	 * @CreateDate: 2016年3月23日 下午9:34:42
	 * @author 魏铭
	 */
	public <T> void hmsetSerialPipe(final String key, final Map<String, T>map) {
		execute(new JedisActionNoResult() {
			 public void action(Jedis jedis) {
				Pipeline p = jedis.pipelined();
				int j=0;
				for(Entry<String, T> e : map.entrySet()) {
					String childKey = key + "_" + e.getKey();
					T t = e.getValue();
					p.set(childKey.getBytes(), SerializeUtils.serialize(t));
					j++;
					if(j%pipelineMax==0) {
						p.sync();
					}
				}
				if(j%pipelineMax!=0) {
					p.sync();
				}
			}
		});
	}
	/**
	 * 扩展方法：设置管道模式写入的map对象中某个field的值(序列化+散列)
	 * @param key
	 * @param field
	 * @param t
	 * @return
	 * @throws JsonMappingException
	 * @throws IOException
	 * @CreateDate: 2016年3月23日 下午9:51:23
	 * @author 魏铭
	 */
	public <T> String hsetSerialPipe(final String key, final String field, final T t) {
		String childKey = key+ "_" + field;
		return this.set(childKey.getBytes(), SerializeUtils.serialize(t));
	}
	/**
	 * 扩展方法：获取管道模式写入的map对象中某个field的值(序列化+散列)
	 * @param key
	 * @param field
	 * @param clazz
	 * @return
	 * @throws JsonMappingException
	 * @throws IOException
	 * @CreateDate: 2016年3月23日 下午11:03:11
	 * @author 魏铭
	 */
	public <T> T hgetSerialPipe(final String key, final String field) {
		String childKey = key+ "_" + field;
		byte[] bytes = this.get(childKey.getBytes());
		@SuppressWarnings("unchecked")
		T t = (T)SerializeUtils.deserialize(bytes);
		return t;
	}
	
	/**
	 * 存储二层set：Map<String, Map>对象，实际上是用来存储HashMap的
	 * 不建议使用此方法，会另外进行封装
	 * @param mainMap
	 * @CreateDate: 2016年3月23日 下午7:24:10
	 * @author 魏铭
	 */
	@SuppressWarnings("rawtypes")
	public void hset(final Map<String, Map> mainMap) {
		execute(new JedisActionNoResult() {
			@SuppressWarnings("unchecked")
			public void action(Jedis jedis) {
				for (Map.Entry<String, Map> entry : mainMap.entrySet()) {
					String key = entry.getKey();
					Map<String, Object> paramMap = entry.getValue();
					for (Map.Entry<String, Object> paramEntry : paramMap.entrySet()) {
						jedis.hset(key, paramEntry.getKey(),
								String.valueOf(paramEntry.getValue()));
					}
				}
			}
		});
	}
	
	/**
	 * (暂时不用)存储Map<String, List<T>>类型数据
	 * @param map
	 * @param field
	 * @CreateDate: 2016年3月23日 下午11:43:04
	 * @author 肖锋
	 */
	public <T> void hmsetPipe(final Map<String, List<T>> map, final Field field) {
		execute(new JedisActionNoResult() {
			@Override
			public void action(Jedis jedis){
				try {
					Pipeline p = jedis.pipelined();
					field.setAccessible(true);//开放字段访问权限
					int j=0;//控制pipeline发送条数
					//获取每个key与list
					for(Entry<String, List<T>> e : map.entrySet()) {
						String key = e.getKey();//散列的key
						List<T> list = e.getValue();//list生成field和value
						//将list拼装成指定的field和value
						Map<byte[], byte[]> hash = new HashMap<byte[], byte[]>();//每个list的field1 value1 field2 value2……
						for(T t : list) {
							Object o = field.get(t);
							byte[] bfield = (field.getName()+o).getBytes();
							byte[] bvalue = SerializeUtils.serialize(t);
							hash.put(bfield, bvalue);
						}
						p.hmset(key.getBytes(), hash);
						j++;
						if(j%pipelineMax==0) {
							p.sync();
						}
					}
					if(j%pipelineMax!=0) {
						p.sync();
					}
				} catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		});
	}
			

	/**
	 * (暂时不用)以管道形式批量添加Map<String,po>类型数据，数据在redis中以字符串形式存储，SET KEY VALUE
	 * @param map
	 * @CreateDate: 2016年3月23日 下午11:43:22
	 * @author 肖锋
	 */
	public <T> void setPipeline(final Map<String, T> map) {
		execute(new JedisActionNoResult() {
			@Override
			public void action(Jedis jedis) {
				Pipeline p = jedis.pipelined();
				int j=0;
				for(Entry<String, T> e : map.entrySet()) {
					String key = e.getKey();
					T t = e.getValue();
					p.set(key.getBytes(), SerializeUtils.serialize(t));
					j++;
					if(j%pipelineMax==0) {
						p.sync();
					}
				}
				if(j%pipelineMax!=0) {
					p.sync();
				}
			}
		});
	}
	
	//返回一个key的ttl
	public long ttl(final String key) {
		return execute(new JedisAction<Long>() {
			@Override
			public Long action(Jedis jedis) {
				return jedis.ttl(key);
			}
		}); 
	}
	
	public String setex(final String key, final int seconds,final String value) {
		return execute(new JedisAction<String>(){
			@Override
			public String action(Jedis jedis) {
				return jedis.setex(key, seconds, value);
			}
		});
	}
	
	
	/**
	 * 复杂结构：Map<String, List<T>>
	 */
	/**
	 * 复杂方法：写入整个Map<String, List<T>>对象(JSON)
	 * @param key 整个map对象的KEY值
	 * @param map
	 * @return
	 * @CreateDate: 2016年3月23日 下午8:37:47
	 * @author 魏铭
	 * @throws IOException 
	 * @throws JsonMappingException 
	 */
	public <T> String setListMap(final String key, final Map<String, List<T>>map,final String group) throws JsonMappingException, IOException {
		Map<String, String> hash = new HashMap<String, String>();
		for (Map.Entry<String, List<T>> entry : map.entrySet()) {
//			String value = JsonUtils.toJson(entry.getValue());
			String value = JSON.toJSONString(entry.getValue());
			hash.put(entry.getKey(), value);
		}
		return this.hmset(setGroupKey(key, group), hash);
	}
	/**
	 * 扩展方法：设置map对象中某个field的值(Json)
	 * @param key map对应的主键
	 * @param field 对象对应的field
	 * @param value
	 * @return 0表示成功。
	 * @CreateDate: 2016年3月23日 下午9:46:39
	 * @author 魏铭
	 * @throws IOException 
	 * @throws JsonMappingException 
	 */
	public <T> Long setListMap(final String key, final String field, final List<T> t) throws JsonMappingException, IOException {
//		String value = JsonUtils.toJson(t);
		String value = JSON.toJSONString(t);
		return this.hset(key, field, value);
	}
	/**
	 * 扩展方法：获取map对象中某个field的值(Json)
	 * @param key map对应的主键
	 * @param field 对象对应的field
	 * @param clazz
	 * @return
	 * @throws JsonMappingException
	 * @throws IOException
	 * @CreateDate: 2016年3月23日 下午10:40:23
	 * @author 魏铭
	 */
	public <T> List<T> getListMap(final String key, final String field, final Class<T> clazz) throws JsonMappingException, IOException {
		String json = this.hget(key, field);
		if (StringUtils.isEmpty(json)) {
			return null;
		}
//		@SuppressWarnings("unchecked")
//		List<T> t = (List<T>)JsonUtils.fromJson(json, clazz);
		
		List<T> t = JSON.parseArray(json,clazz);
		return t;
	}
	/**
	 * 扩展方法：获取整个map对象(Json)
	 * @param key map对应的主键
	 * @param clazz
	 * @return
	 * @throws JsonMappingException
	 * @throws IOException
	 * @CreateDate: 2016年3月23日 下午10:40:23
	 * @author 魏铭
	 */
	public <T> Map<String, List<T>> getListMap(final String key, final Class<T> clazz,final String group) throws JsonMappingException, IOException {
		Map<String, String> hash = this.hgetAll(setGroupKey(key, group));
		if (hash == null) {
			return null;
		}
		Map<String, List<T>> map = new HashMap<String, List<T>>();
		for (Map.Entry<String, String> entry : hash.entrySet()) {
			String json=entry.getValue();
			if (StringUtils.isEmpty(json)) {
				continue;
			}
			@SuppressWarnings("unchecked")
//			List<T> t = (List<T>)JsonUtils.fromJson(json, clazz);
			
			List<T> t = JSON.parseArray(json,clazz);
			map.put(entry.getKey(), t);
		}
		return map;
	}
	/**
	 * set List对象
	 * @param key
	 * @param list
	 * @return
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public <T> String setList(final String key, final  List<T> list,final String group) throws JsonMappingException, IOException {
		 String value = JSON.toJSONString(list);
		 return this.set(setGroupKey(key, group), value);
	}
	/**
	 * 获取List List对象
	 * @param key
	 * @return
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public <T> List<T> getList(final String key,final String group,final Class<T> clazz) throws JsonMappingException, IOException {
		String json = this.get(setGroupKey(key, group));
		if (StringUtils.isEmpty(json)) {
			return null;
		}

		List<T> t = JSON.parseArray(json,clazz);
		return t;
	}
	
	
	/**
	 * 设置对象
	 * @param key
	 * @param t
	 * @return
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public <T> String setObject(final String key,final Object t) throws JsonMappingException, IOException {
		 String value = JSON.toJSONString(t);
		 return this.set(key, value);
	}
	/**
	 * 获取对象
	 * @param key
	 * @param clazz
	 * @return
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public <T> Object  getObject(final String key, final Class<T> clazz) throws JsonMappingException, IOException {
		String json = this.get(key);
		if (StringUtils.isEmpty(json)) {
			return null;
		}
		T t = JSON.parseObject(json, clazz);
		return t;
	}
}

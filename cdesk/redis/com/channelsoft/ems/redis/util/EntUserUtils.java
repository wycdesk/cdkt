package com.channelsoft.ems.redis.util;

import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


import com.channelsoft.cri.cache.constant.CacheConstants;
import com.channelsoft.cri.logger.SystemLogUtils;
import com.channelsoft.ems.redis.constant.CacheGroup;
import com.channelsoft.ems.user.po.DatEntUserPo;

public class EntUserUtils extends RedisBaseUtil{
	private static final String LIST_KEY = "LIST";
	private static final String MAP_KEY = "MAP";
	private static final String FLAG_KEY = "FLAG";
	private static final String GROUP_MAP_KEY = "GROUP_MAP";

	private static final ReadWriteLock dataLock = new ReentrantReadWriteLock();
	

	/**
	 * 存入原始数据，主要用于判断是否需要刷新缓存
	 * @param list
	 * @CreateDate: 2013-3-26 上午01:58:01
	 * @author 魏铭
	 */
	private static void setList(String entId, Map<String, List<DatEntUserPo>> list) {
		if (CacheConstants.CACHE_DATA_STATUS())
		{
			dataLock.writeLock().lock();
			try {
				getJedisInstance().hmsetJson(entId + "_" + LIST_KEY, list,CacheGroup.ENT_USER.name);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {  
				dataLock.writeLock().unlock();  
			}  
		}
	}
	
	private static void setMap(String entId, Map<String, DatEntUserPo> map) {
		if (CacheConstants.CACHE_DATA_STATUS())
		{
			dataLock.writeLock().lock();
			try {
				getJedisInstance().hmsetJson(entId + "_" + MAP_KEY, map,CacheGroup.ENT_USER.name);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {  
				dataLock.writeLock().unlock();  
			}  
		}
	}
	
	private static void setFlags(Map<String, String> flags) {
		if (CacheConstants.CACHE_DATA_STATUS())
		{
			dataLock.writeLock().lock();
			try {
				getJedisInstance().hmset(FLAG_KEY, flags, CacheGroup.ENT_USER.name);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				dataLock.writeLock().unlock();  
			}  
		}
	}
	
	private static void setGroupMap(Map<String, List<DatEntUserPo>> groupMap) {
		if (CacheConstants.CACHE_DATA_STATUS())
		{
			dataLock.writeLock().lock();
			try {
				getJedisInstance().hmsetJson(GROUP_MAP_KEY, groupMap,CacheGroup.ENT_USER.name);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {  
				dataLock.writeLock().unlock();  
			}  
		}
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, List<DatEntUserPo>> getList(String entId) {
		if (CacheConstants.CACHE_DATA_STATUS())
		{
			dataLock.readLock().lock();  
			try {
				Map<String,  List<DatEntUserPo>> obj = getJedisInstance().getListMap(entId + "_" + LIST_KEY, DatEntUserPo.class,CacheGroup.ENT_USER.name);
				if (obj==null) return null;
				return obj;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {  
				dataLock.readLock().unlock();  
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, DatEntUserPo> getMap(String entId) {
		if (CacheConstants.CACHE_DATA_STATUS())
		{
			dataLock.readLock().lock();  
			try {
				Map<String,DatEntUserPo> obj = getJedisInstance().hgetAll(entId + "_" + MAP_KEY, DatEntUserPo.class, CacheGroup.ENT_USER.name);
				if (obj==null) return null;
				return (Map<String, DatEntUserPo>)obj;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {  
				dataLock.readLock().unlock();  
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, String> getFlags() {
		if (CacheConstants.CACHE_DATA_STATUS())
		{
			dataLock.readLock().lock();  
			try {
				Map<String,String> obj = getJedisInstance().hgetAll(FLAG_KEY, CacheGroup.ENT_USER.name);
				return obj;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {  
				dataLock.readLock().unlock();  
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, List<DatEntUserPo>> getGroupMap() {
		if (CacheConstants.CACHE_DATA_STATUS())
		{
			dataLock.readLock().lock();  
			try {
				Map<String,  List<DatEntUserPo>> obj = getJedisInstance().getListMap(GROUP_MAP_KEY, DatEntUserPo.class,CacheGroup.ENT_USER.name);
				return obj;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {  
				dataLock.readLock().unlock();  
			}
		}
		return null;
	}
	

	/** 
	 * 查询数据库后调用此方法：将数据放入缓存，并放入静态变量。
	 * @param list
	 * @param map
	 * @CreateDate: 2013-8-2 下午01:20:06
	 * @author 魏铭
	 */
	public static void getCacheFromDB(Map<String, Map<String, List<DatEntUserPo>>> list, Map<String, Map<String, DatEntUserPo>> map, Map<String, String> flags) {
		if (list!=null && list.size()>0 ) {
			SystemLogUtils.Debug("将数据库UserList存入缓存");
			for (String entId : list.keySet()) {
				setList(entId, list.get(entId));
			}
		}
		if (map!=null && map.size()>0) {
			SystemLogUtils.Debug("将数据库UserMap存入缓存");
			for (String entId : map.keySet()) {
				setMap(entId, map.get(entId));
			}
		}
		if (flags != null && flags.size() > 0) {
			SystemLogUtils.Debug("将UserFlags存入缓存");
			setFlags(flags);
		}
	}

	/**
	 * 取出所有企业的缓存，更新指定企业的数据，然后将数据放入缓存，并放入静态变量
	 * @param entId
	 * @param list
	 * @param map
	 * @author zhangtie
	 */
	public static void getCacheFromDB(String entId, Map<String, List<DatEntUserPo>> list, Map<String, DatEntUserPo> map, String flag) {
		if (list!=null && list.size()>0 ) {
			SystemLogUtils.Debug("将企业" + entId + "的数据库UserList存入缓存");
			setList(entId, list);
		}
		if (map!=null && map.size()>0) {
			SystemLogUtils.Debug("将企业" + entId + "的数据库UserMap存入缓存");
			setMap(entId, map);
		}
		if (flag != null) {
			SystemLogUtils.Debug("将企业" + entId + "的UserFlag存入缓存");
			Map<String, String> flags = getFlags();
			flags.put(entId, flag);
			setFlags(flags);
		}
	}
}

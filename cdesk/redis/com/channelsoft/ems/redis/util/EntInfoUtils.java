package com.channelsoft.ems.redis.util;

import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.channelsoft.cri.cache.constant.CacheConstants;
import com.channelsoft.cri.logger.SystemLogUtils;
import com.channelsoft.ems.ent.po.DatEntInfoPo;
import com.channelsoft.ems.redis.constant.CacheGroup;

public class EntInfoUtils extends RedisBaseUtil{
	private static final String LIST_KEY = "LIST";
	private static final String MAP_KEY = "MAP";
	private static final String FLAG_KEY = "FLAG";
	private static final String GROUP_MAP_KEY = "GROUP_MAP";
	
	private static final ReadWriteLock dataLock = new ReentrantReadWriteLock();
//	


	/**
	 * 存入原始数据，主要用于判断是否需要刷新缓存
	 * @param list
	 * @CreateDate: 2013-3-26 上午01:58:01
	 * @author 魏铭
	 */
	private static void setList(List<DatEntInfoPo> list) {
		if (CacheConstants.CACHE_DATA_STATUS())
		{
			dataLock.writeLock().lock();
			try {
				getJedisInstance().setList(LIST_KEY, list, CacheGroup.ENT_INFO.name);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {  
				dataLock.writeLock().unlock();  
			}  
		}
	}
	
	private static void setMap(Map<String, DatEntInfoPo> map) {
		if (CacheConstants.CACHE_DATA_STATUS())
		{
			dataLock.writeLock().lock();
			try {
				getJedisInstance().hmsetJson(MAP_KEY, map,CacheGroup.ENT_INFO.name);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {  
				dataLock.writeLock().unlock();  
			}  
		}
	}
	
	private static void setFlag(String flag) {
		if (CacheConstants.CACHE_DATA_STATUS())
		{
			dataLock.writeLock().lock();
			try {
				getJedisInstance().set(FLAG_KEY, flag,  CacheGroup.ENT_INFO.name);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {  
				dataLock.writeLock().unlock();  
			}  
		}
	}
	
	private static void setGroupMap(Map<String, List<DatEntInfoPo>> groupMap) {
		if (CacheConstants.CACHE_DATA_STATUS())
		{
			dataLock.writeLock().lock();
			try {
				getJedisInstance().setListMap(GROUP_MAP_KEY, groupMap, CacheGroup.ENT_INFO.name);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {  
				dataLock.writeLock().unlock();  
			}  
		}
	}
	
	@SuppressWarnings("unchecked")
	public  static List<DatEntInfoPo> getList() {
		if (CacheConstants.CACHE_DATA_STATUS())
		{
			dataLock.readLock().lock();  
			try {
				List<DatEntInfoPo> obj = getJedisInstance().getList(LIST_KEY, CacheGroup.ENT_INFO.name,DatEntInfoPo.class);
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
	public static Map<String, DatEntInfoPo> getMap() {
		if (CacheConstants.CACHE_DATA_STATUS())
		{
			dataLock.readLock().lock();  
			try {
				Map<String,DatEntInfoPo> obj = getJedisInstance().hgetAll(MAP_KEY, DatEntInfoPo.class, CacheGroup.ENT_INFO.name);
				return obj;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {  
				dataLock.readLock().unlock();  
			}
		}
		return null;
	}
	
	public static String getFlag() {
		if (CacheConstants.CACHE_DATA_STATUS())
		{
			dataLock.readLock().lock();  
			try {
				String obj = getJedisInstance().get(FLAG_KEY,  CacheGroup.ENT_INFO.name);
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
	public static Map<String, List<DatEntInfoPo>> getGroupMap() {
		if (CacheConstants.CACHE_DATA_STATUS())
		{
			dataLock.readLock().lock();  
			try {
				Map<String, List<DatEntInfoPo>> obj = getJedisInstance().getListMap(GROUP_MAP_KEY, DatEntInfoPo.class,CacheGroup.ENT_INFO.name);
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
	public static void getCacheFromDB(List<DatEntInfoPo> list, Map<String, DatEntInfoPo> map, String flag) {
		if (list!=null && list.size()>0 ) {
			SystemLogUtils.Debug("将数据库EntList存入缓存");
			setList(list);
		}
		if (map!=null && map.size()>0) {
			SystemLogUtils.Debug("将数据库EntMap存入缓存");
			setMap(map);
		}
		if (flag != null) {
			SystemLogUtils.Debug("将EntFlag存入缓存");
			setFlag(flag);
		}
	}

}

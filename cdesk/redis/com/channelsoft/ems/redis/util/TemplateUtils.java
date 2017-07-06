package com.channelsoft.ems.redis.util;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import com.channelsoft.cri.cache.constant.CacheConstants;
import com.channelsoft.cri.logger.SystemLogUtils;
import com.channelsoft.ems.redis.constant.CacheGroup;
import com.channelsoft.ems.template.po.TemplatePo;

public class TemplateUtils extends RedisBaseUtil{
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
	private static void setList(String entId, CacheGroup cacheGroup,List<TemplatePo> list) {
		if (CacheConstants.CACHE_DATA_STATUS())
		{
			dataLock.writeLock().lock();
			try {
				getJedisInstance().setList(entId + "_" + LIST_KEY, list, cacheGroup.name);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {  
				dataLock.writeLock().unlock();  
			}  
		}
	}
	
	private static void setMap(String entId, CacheGroup cacheGroup, Map<String, TemplatePo> map) {
		if (CacheConstants.CACHE_DATA_STATUS())
		{
			dataLock.writeLock().lock();
			try {
				getJedisInstance().hmsetJson(entId + "_" + MAP_KEY, map,cacheGroup.name);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {  
				dataLock.writeLock().unlock();  
			}  
		}
	}
	
	private static void setFlags(Map<String, String> flags,CacheGroup cacheGroup) {
		if (CacheConstants.CACHE_DATA_STATUS())
		{
			dataLock.writeLock().lock();
			try {
				getJedisInstance().hmset(FLAG_KEY, flags, cacheGroup.name);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				dataLock.writeLock().unlock();  
			}  
		}
	}
	
	private static void setGroupMap(Map<String, List<TemplatePo>> groupMap,CacheGroup cacheGroup) {
		if (CacheConstants.CACHE_DATA_STATUS())
		{
			dataLock.writeLock().lock();
			try {
				getJedisInstance().hmsetJson(GROUP_MAP_KEY, groupMap,cacheGroup.name);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {  
				dataLock.writeLock().unlock();  
			}  
		}
	}
	
	@SuppressWarnings("unchecked")
	public static List<TemplatePo> getList(String entId,CacheGroup cacheGroup) {
		if (CacheConstants.CACHE_DATA_STATUS())
		{
			dataLock.readLock().lock();  
			try {
				List<TemplatePo> obj = getJedisInstance().getList(entId + "_" + LIST_KEY, cacheGroup.name,TemplatePo.class);
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
	public static Map<String, TemplatePo> getMap(String entId,CacheGroup cacheGroup) {
		if (CacheConstants.CACHE_DATA_STATUS())
		{
			dataLock.readLock().lock();  
			try {
				Map<String,TemplatePo> obj = getJedisInstance().hgetAll(entId + "_" + MAP_KEY, TemplatePo.class, cacheGroup.name);
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
	public static Map<String, String> getFlags(CacheGroup cacheGroup) {
		if (CacheConstants.CACHE_DATA_STATUS())
		{
			dataLock.readLock().lock();  
			try {
				Map<String,String> obj = getJedisInstance().hgetAll(FLAG_KEY, cacheGroup.name);
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
	public static Map<String, List<TemplatePo>> getGroupMap(CacheGroup cacheGroup) {
		if (CacheConstants.CACHE_DATA_STATUS())
		{
			dataLock.readLock().lock();  
			try {
				Map<String,List<TemplatePo>> obj = getJedisInstance().getListMap(GROUP_MAP_KEY, TemplatePo.class,cacheGroup.name);
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
	 * @param cacheGroup
	 * @param list
	 * @param map
	 * @param flags
	 */
	public static void getCacheFromDB(CacheGroup cacheGroup,Map<String, List<TemplatePo>> list, Map<String, Map<String, TemplatePo>> map, Map<String, String> flags) {
		if (list!=null && list.size()>0 ) {
			SystemLogUtils.Debug("将数据库"+cacheGroup.name+" List存入缓存");
			 setGroupMap(list,cacheGroup);
			for (String entId : list.keySet()) {
				setList(entId,cacheGroup, list.get(entId));
			}
		}
		if (map!=null && map.size()>0) {
			SystemLogUtils.Debug("将数据库"+cacheGroup.name+" 存入缓存");
			for (String entId : map.keySet()) {
				setMap(entId,cacheGroup, map.get(entId));
			}
		}
		if (flags != null && flags.size() > 0) {
			SystemLogUtils.Debug("将"+cacheGroup.name+" 存入缓存");
			setFlags(flags,cacheGroup);
		}
	}

	/**
	 * 取出所有企业的缓存，更新指定企业的数据，然后将数据放入缓存，并放入静态变量
	 * @param cacheGroup
	 * @param entId
	 * @param list
	 * @param map
	 * @param flag
	 */
	public static void getCacheFromDB(CacheGroup cacheGroup,String entId, List<TemplatePo> list, Map<String, TemplatePo> map, String flag) {
		if (list!=null && list.size()>0 ) {
			SystemLogUtils.Debug("将企业" + entId + "的数据库"+cacheGroup.name+" List存入缓存");
			setList(entId,cacheGroup, list);
		}
		if (map!=null && map.size()>0) {
			SystemLogUtils.Debug("将企业" + entId + "的数据库"+cacheGroup.name+" 存入缓存");
			setMap(entId,cacheGroup, map);
		}
		if (flag != null) {
			SystemLogUtils.Debug("将企业" + entId + "的"+cacheGroup.name+" Flag存入缓存");
			Map<String, String> flags = getFlags(cacheGroup);
			flags.put(entId, flag);
			setFlags(flags,cacheGroup);
		}
	}
}

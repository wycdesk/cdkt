package com.channelsoft.cri.cache.constant;


public class CacheConstants {
	/**
	 * DATA缓存生效标记，1生效，0不生效。
	 * @return
	 */
	private static String CACHE_DATA_FLAG()
	{
		//return ConfigUtil.getString("CACHE_DATA_FLAG", "1");
		return "1";
	}
	/**
	 * 参数缓存是否开启
	 * @return
	 * @CreateDate: 2013-8-1 下午01:44:04
	 * @author 魏铭
	 */
	public static boolean CACHE_DATA_STATUS()
	{
		if("1".equals(CACHE_DATA_FLAG()))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * 缓存有效期，暂定120分钟：必须大于5
	 * @return
	 * @CreateDate: 2013-4-27 上午10:42:32
	 * @author 魏铭
	 */
	private static int CACHE_DATA_MINUTE() {
		//return ConfigUtil.getInt("CACHE_DATA_MINUTE", 120);
		return 120;
	}
	/**
	 * 缓存有效期（毫秒）
	 * @return
	 * @CreateDate: 2013-8-1 下午01:46:10
	 * @author 魏铭
	 */
	public static int CACHE_DATA_TIME() {
		return CACHE_DATA_MINUTE()*60*1000;
	}
	/**
	 * 缓存更新时间，必须小于缓存有效期的一半，再减去2分钟
	 */
	public static int FLUSH_DATA_TIME() {
		return (CACHE_DATA_MINUTE()/2 - 2)*60*1000;
	}
	public static final String STATUS_KEY = "STATUS";
	/**
	 * 缓存锁定时间，需要大于查询数据库的时间。默认5分钟
	 * 当锁定后如果没有成功，以该时间为基准失效。
	 * @return
	 * @CreateDate: 2013-8-1 下午03:19:24
	 * @author 魏铭
	 */
	private static int LOCK_DATA_MINUTE() {
		//return ConfigUtil.getInt("LOCK_DATA_MINUTE", 5);
		return 5;
	}
	/**
	 * 缓存锁定时间（毫秒），需要大于查询数据库的时间。默认5分钟
	 * 当锁定后如果没有成功，以该时间为基准失效。
	 * @return
	 * @CreateDate: 2013-8-1 下午03:21:33
	 * @author 魏铭
	 */
	public static int LOCK_DATA_TIME() {
		return LOCK_DATA_MINUTE()*60*1000;
	}
}

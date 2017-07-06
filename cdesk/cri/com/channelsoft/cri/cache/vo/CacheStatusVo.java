package com.channelsoft.cri.cache.vo;

import java.io.Serializable;
import java.util.Date;

import com.channelsoft.cri.cache.constant.CacheStatus;
import com.channelsoft.cri.constant.BaseConstants;
/**
 * 缓存标记对象。用于判断缓存是否需要刷新。
 * <dl>
 * <dt>portal</dt>
 * <dd>Description:</dd>
 * <dd>Copyright: Copyright (C) 2013</dd>
 * <dd>Company: 青牛(北京)技术有限公司 成都研究所</dd>
 * <dd>CreateDate: 2013-8-1</dd>
 * </dl>
 * @author 魏铭
 */
public class CacheStatusVo implements Serializable {
	private static final long serialVersionUID = -7737378143248131859L;
	private Date cacheTime;
	private String groupName;
	private String cacheOperator;
	private String cacheStatus;
	public Date getCacheTime() {
		return cacheTime;
	}
	public void setCacheTime(Date cacheTime) {
		this.cacheTime = cacheTime;
	}
	public String getCacheOperator() {
		return cacheOperator;
	}
	public void setCacheOperator(String cacheOperator) {
		this.cacheOperator = cacheOperator;
	}
	public String getCacheStatus() {
		return cacheStatus;
	}
	public void setCacheStatus(String cacheStatus) {
		this.cacheStatus = cacheStatus;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getGroupName() {
		return groupName;
	}
	/**
	 * 创建一个新的缓存状态对象
	 * @param group
	 * @CreateDate: 2013-8-1 下午02:41:02
	 * @author 魏铭
	 */
	public CacheStatusVo(String groupName) {
		this.groupName = groupName;
		this.cacheOperator = BaseConstants.getLoggerName();
	}

	/**
	 * 将当前缓存对象的状态置为锁定。
	 * @param flush_PARAM_MINUTE
	 * @return
	 * @CreateDate: 2013-8-1 下午02:31:56
	 * @author 魏铭
	 */
	public void lock() {
		this.cacheStatus = CacheStatus.LOCKED.value;
		this.cacheTime = new Date();
	}
	/**
	 * 缓存成功，更新状态
	 * 
	 * @CreateDate: 2013-8-1 下午02:41:45
	 * @author 魏铭
	 */
	public void success() {
		this.cacheStatus = CacheStatus.NORMAL.value;
		this.cacheTime = new Date();
	}
}

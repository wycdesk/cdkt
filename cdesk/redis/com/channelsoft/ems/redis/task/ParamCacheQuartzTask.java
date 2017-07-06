package com.channelsoft.ems.redis.task;


import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.channelsoft.cri.cache.redis.JedisTemplate;
import com.channelsoft.cri.exception.BaseException;
import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.logger.SystemLogUtils;
import com.channelsoft.cri.util.BeanFactoryUtil;
import com.channelsoft.ems.redis.constant.CacheGroup;
import com.channelsoft.ems.redis.service.IParamCacheService;

public class ParamCacheQuartzTask extends QuartzJobBean{
	
	@Autowired
	IParamCacheService cacheService;
	@Autowired
	JedisTemplate jedisTemplate;
	private static final String CDESK_KEY = "CDESK_CACHE";//redis中AAA缓存的key值
	private static final String CDESK_LOCK = "CDESK_LOCK";
	private static int aa = 0;
	private static final Logger logger = LoggerFactory.getLogger(ParamCacheQuartzTask.class); 
	
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		try {

			if(jedisTemplate == null){
				jedisTemplate = (JedisTemplate) BeanFactoryUtil.getBean("jedisTemplate");
			}

			//判断缓存是否有效，如果有效则退出，无效则启动刷新任务
			if(cacheAlive()) {
				logger.debug("【ParamCacheQuartzTask】缓存有效ttl={}，定时任务退出……",jedisTemplate.ttl(CDESK_KEY));
				return;
			}
			logger.debug("【ParamCacheQuartzTask】缓存无效，开始获取锁……");
			if (cacheService == null) {
				cacheService = (IParamCacheService)BeanFactoryUtil.getBean("paramRedisService");
			}
	
			if (aa==0) {
				initCache();
			} else {
				cacheService.flushFullCache(CacheGroup.ENT_USER, null);
				cacheService.flushFullCache(CacheGroup.ENT_INFO, null);
				cacheService.flushFullCache(CacheGroup.GROUP, null);
				cacheService.flushFullCache(CacheGroup.GROUP_AGENT, null);
				//刷新所有模版类型
				cacheService.refreshFullCache(CacheGroup.TEMPLATE, null);
				//刷新所有模版类型字段
				cacheService.refreshFullCache(CacheGroup.TEMPLATE_FIELD, null);
			}
			jedisTemplate.setex(CDESK_KEY, 1*60,"1");
		} catch (Exception e) {
			e.printStackTrace();
			SystemLogUtils.Fail("PMS", "刷新", "缓存", new BaseException("刷新缓存出错"));
		}
	}

	public void initCache(){
		refreshCache();
		aa=1;
	}
	
	private void  refreshCache(){
		if (cacheService == null) {
			cacheService = (IParamCacheService)BeanFactoryUtil.getBean("paramRedisService");
		}
		cacheService.refreshFullCache(CacheGroup.ENT_USER, null);
		cacheService.refreshFullCache(CacheGroup.ENT_INFO, null);
		cacheService.refreshFullCache(CacheGroup.GROUP, null);
		cacheService.refreshFullCache(CacheGroup.GROUP_AGENT, null);
		//刷新所有模版类型
		cacheService.refreshFullCache(CacheGroup.TEMPLATE, null);
		//刷新所有模版类型字段
		cacheService.refreshFullCache(CacheGroup.TEMPLATE_FIELD, null);
	}
	
	private boolean cacheAlive() {
		long ttl = jedisTemplate.ttl(CDESK_KEY);
		if(ttl>=0) {
			return true;
		}
		if(ttl==-1) {
			throw new ServiceException("AAA缓存没有设置过期时间……");
		}
		if(ttl==-2) {
			return false;
		}
		return false;
	}
}

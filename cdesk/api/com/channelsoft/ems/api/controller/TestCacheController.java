package com.channelsoft.ems.api.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.channelsoft.ems.redis.constant.CacheGroup;
import com.channelsoft.ems.redis.util.ParamUtils;
import com.channelsoft.ems.redis.util.TestCacheUtil;


@Controller
@RequestMapping("/api/cache")
public class TestCacheController {
	
	@ResponseBody
	@RequestMapping(value = "/getCache")
	public void getCache(HttpServletRequest request){
		TestCacheUtil.getCache();
	}
	/**
	 * 刷新缓存
	 * @param request
	 */
	@ResponseBody
	@RequestMapping(value = "/refreshCache")
	public void refreshCache(HttpServletRequest request){
		String entId=request.getParameter("entId");
		if(StringUtils.isNotBlank(entId)){
			/**
			 * 刷新缓存
			 */
			ParamUtils.refreshCache(CacheGroup.ENT_INFO, entId);
			ParamUtils.refreshCache(CacheGroup.ENT_USER, entId);
			ParamUtils.refreshCache(CacheGroup.GROUP, entId);
			ParamUtils.refreshCache(CacheGroup.GROUP_AGENT, entId);
		}
		else{
			ParamUtils.refreshCache(CacheGroup.ENT_INFO, "");
			ParamUtils.refreshCache(CacheGroup.ENT_USER, "");
			ParamUtils.refreshCache(CacheGroup.GROUP, "");
			ParamUtils.refreshCache(CacheGroup.GROUP_AGENT, "");
		}

	}
}

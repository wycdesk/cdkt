package com.channelsoft.ems.api.client;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.logger.SystemLogUtils;
import com.channelsoft.cri.util.HttpPostUtils;
import com.channelsoft.cri.util.WebappConfigUtil;
import com.channelsoft.ems.redis.constant.CacheGroup;

public class EntClient {

	//工单服务地址
	static String initMongDbUrl = WebappConfigUtil.getParameter("WORKFLOW_ROOT") + "/workorder/init";
	static String workInfoUrl = WebappConfigUtil.getParameter("WORKFLOW_ROOT") + "/workorder/detail";
	static String destroyEntUrl = WebappConfigUtil.getParameter("WORKFLOW_ROOT") + "/workorder/destroyEnt";
	//	static String refreshCacheUrl = "";
	static String refreshCacheUrl = WebappConfigUtil.getParameter("WORKFLOW_ROOT") + "/cacheNotice/refresh";
	static String deleteWfUrl = WebappConfigUtil.getParameter("WORKFLOW_ROOT") + "/workapi/deleteWf";
	static String userJoinUrl = WebappConfigUtil.getParameter("WORKFLOW_ROOT") + "/workorder/userJoin";
	/*电话创建工单地址*/
	static String phoneCallCreateUrl = WebappConfigUtil.getParameter("WORKFLOW_ROOT") + "/workorder/phoneCallCreate";
	
	/*更新工单模板名的地址*/
	static String updateWorkTempNameUrl = WebappConfigUtil.getParameter("WORKFLOW_ROOT") + "/workorder/updateWorkTempName";
	
	public static String  initMongDb(String entId) throws ServiceException{
		String data="";
		try {
			Map<String, String> param = new HashMap<String, String>();

			param.put("entId", entId);
			System.out.println("初始化mongdb数据库,entId="+entId);
			data = HttpPostUtils.httpPost(initMongDbUrl, param);
			System.out.println("初始化mongdb数据库,entId="+entId+",返回:"+data);
			JSONObject json;
			try {
				json = new JSONObject(data);
				if(json.has("flag")){
					if(json.getInt("flag")==1){
						
					}
					else{
						throw new ServiceException("初始化mongdb失败");
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			throw new ServiceException("初始化mongdb失败");
		}

		return data;
	}
	public static String destroyEnt(String entId) throws ServiceException{
		String data="";
		try {
			Map<String, String> param = new HashMap<String, String>();
			param.put("entId", entId);
			System.out.println("注销企业,entId="+entId);
			data = HttpPostUtils.httpPost(destroyEntUrl, param);
			System.out.println("注销企业,entId="+entId+",返回:"+data);
		} catch (IOException e) {
			e.printStackTrace();
			throw new ServiceException("删除mongo企业信息失败");
		}
		return data;
	}
	/**
	 * 
	 * @param entId
	 * @param type 使用CacheGroup的id
	 * @return
	 * @throws ServiceException
	 */
	public static String refresh(String entId,String cacheGroupId) throws ServiceException{
		String data="";
		try {
			if(StringUtils.isBlank(refreshCacheUrl)) return data;
			if(CacheGroup.getEnum(cacheGroupId)==null) {
				SystemLogUtils.Debug(String.format("========调用work刷新缓存,cacheGroupId为空：=======,entId=%s,cacheGroupId=%s",entId, cacheGroupId));
			}
			Map<String, String> param = new HashMap<String, String>();
			param.put("entId", entId);
			param.put("type", CacheGroup.getEnum(cacheGroupId).name);
			SystemLogUtils.Debug(String.format("========准备调用work刷新缓存：=======,entId=%s,cacheGroupId=%s",entId, cacheGroupId));
			data = HttpPostUtils.httpPost(refreshCacheUrl, param);
			SystemLogUtils.Debug(String.format("========调用work刷新缓存返回：=======,entId=%s,cacheGroupId=%s,返回:%s",entId, cacheGroupId,data));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}
	
	/*删除用户名下的所有工单*/
	public static String deleteWf(String entId,String[] userIdArray) throws ServiceException{
		String data="";
		String userId="";
		try {			
			System.out.println("****deleteWfUrl="+deleteWfUrl);
			if(StringUtils.isBlank(deleteWfUrl)) return data;
			
			if(userIdArray==null) {
				SystemLogUtils.Debug(String.format("========调用删除用户名下的所有工单,userIdArray为空：=======,entId=%s,userIdArray=%s",entId, userIdArray));
			}else{
				userId=StringUtils.join(userIdArray, ",");
			}		 	
			Map<String, String> param = new HashMap<String, String>();
			param.put("entId", entId);
			param.put("user", userId);
			SystemLogUtils.Debug(String.format("========准备调用删除用户名下的所有工单：=======,entId=%s,userId=%s",entId, userId));
			data = HttpPostUtils.httpPost(deleteWfUrl, param);
			SystemLogUtils.Debug(String.format("========调用删除用户名下的所有工单返回：=======,entId=%s,userId=%s,返回:%s",entId, userId,data));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}
	
	/*获取工单信息*/
	public static String getWorkInfo(String entId,String workId,String sessionKey) throws ServiceException{
		String data="";
		try {			
			System.out.println("****workInfoUrl="+workInfoUrl);
			if(StringUtils.isBlank(workInfoUrl)) return data;
			Map<String, String> param = new HashMap<String, String>();
			param.put("entId", entId);
			param.put("info", "{\"workId\":"+workId+"}");
			param.put("sessionKey", sessionKey);
			data = HttpPostUtils.httpPost(workInfoUrl.replace("http://www", "http://"+entId)+"?sessionKey="+sessionKey+"&entId="+entId, param);
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
		int ft=data.indexOf("(")+1;
		data=data.substring(ft, data.length()-1);
		return data;
	}
	
	/**
	 * 用户合并中工单处理
	 * @param entId
	 * @param fromUserId 合并前用户ID
	 * @param toUserInfo 合并后用户信息
	 * @param sessionKey
	 * @return
	 * @throws ServiceException
	 */
	public static String userJoinForwork(String entId,String fromUserId,String toUserInfo,String sessionKey) throws ServiceException{
		String data="";
		try {			
		 	SystemLogUtils.Debug("用户合并后，处理工单,entId="+entId+",fromUserId="+fromUserId+",fromUserId="+fromUserId+",toUserInfo="+toUserInfo);
			if(StringUtils.isBlank(userJoinUrl)) return data;
			Map<String, String> param = new HashMap<String, String>();
			param.put("entId", entId);
			param.put("fromUserId", fromUserId);
			param.put("toUserInfo", URLEncoder.encode(URLEncoder.encode(toUserInfo, "UTF-8"), "UTF-8"));
			data = HttpPostUtils.httpPost(userJoinUrl.replace("http://www", "http://"+entId)+"?sessionKey="+sessionKey+"&entId="+entId, param);
		} catch (IOException e) {
			e.printStackTrace();
			SystemLogUtils.Debug("用户合并后，处理工单,异常,entId="+entId+",fromUserId="+fromUserId+",fromUserId="+fromUserId+",toUserInfo="+toUserInfo);
			return "";
		}
		SystemLogUtils.Debug("用户合并后，处理工单,正常返回,entId="+entId+",fromUserId="+fromUserId+",fromUserId="+fromUserId+",toUserInfo="+toUserInfo+",返回值:"+data);
		int ft=data.indexOf("(")+1;
		data=data.substring(ft, data.length()-1);
		return data;
	}
	
	
	
	public static String phoneCallCreate(String entId, String info,String sessionKey) throws ServiceException{
		String data="";
		try {			
			System.out.println("****phoneCallCreateUrl="+phoneCallCreateUrl);
			Map<String, String> param = new HashMap<String, String>();
			param.put("info", info);
			param.put("sessionKey", sessionKey);
			data = HttpPostUtils.httpPost(phoneCallCreateUrl.replace("http://www", "http://"+entId)+"?sessionKey="+sessionKey+"&entId="+entId, param);
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
		int ft=data.indexOf("(")+1;
		data=data.substring(ft, data.length()-1);
		return data;
	}
	
	
	/*根据模板Id更新工单的模板名*/
	public static String updateWorkTempName(String tempId, String tempName, String entId, String sessionKey) throws ServiceException{
		String data="";
		try {			
			System.out.println("****updateWorkTempNameUrl="+updateWorkTempNameUrl);
			if(StringUtils.isBlank(updateWorkTempNameUrl)) return data;
				 	
			Map<String, String> param = new HashMap<String, String>();
			param.put("tempId", tempId);
			param.put("tempName", URLEncoder.encode(URLEncoder.encode(tempName, "utf-8"), "utf-8"));
			param.put("entId", entId);
			param.put("sessionKey", sessionKey);
			SystemLogUtils.Debug(String.format("========准备调用依据模板编号修改工单的模板名：=======,tempId=%s,tempName=%s,entId=%s",tempId, tempName, entId));
			data = HttpPostUtils.httpPost(updateWorkTempNameUrl.replace("http://www", "http://"+entId), param);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}
}

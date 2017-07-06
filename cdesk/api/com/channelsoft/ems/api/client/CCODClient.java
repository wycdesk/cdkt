package com.channelsoft.ems.api.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.logger.SystemLogUtils;
import com.channelsoft.cri.util.BuildParamMapUtils;
import com.channelsoft.cri.util.HttpPostUtils;
import com.channelsoft.cri.util.JsonUtils;
import com.channelsoft.cri.util.WebappConfigUtil;
import com.channelsoft.ems.api.po.CCODRequestPo;
import com.channelsoft.ems.api.po.CCODResponsePo;
import com.channelsoft.ems.group.po.GroupPo;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

/**
 * CCOD接口工具类
 * @author wangjie
 * @time 2016年3月11日下午5:28:53
 */
public class CCODClient {
	
	public static void main(String[] args) {
		CCODRequestPo po = new CCODRequestPo();
		po.setEnterpriseId("SSS");
		po.setAgentId("458");
		po.setAgentName("test");
		po.setAgentPassword("123456");
		po.setAgentRole("1");
		
		CCODResponsePo rpo = deleteAgent("","553");
		System.out.println(rpo.getResult());
		System.out.println(rpo.getDesc());
	}

	private static String addAgentUrl = WebappConfigUtil.getParameter("CCOD_ROOT")+"/agent/add";
	
	private static String updateAgentUrl = WebappConfigUtil.getParameter("CCOD_ROOT")+"/agent/update";
	
	private static String deleteAgentUrl = WebappConfigUtil.getParameter("CCOD_ROOT")+"/agent/delete";
	
	private static String addGroupUrl = WebappConfigUtil.getParameter("CCOD_ROOT")+"/skill/add";
	
	private static String updateGroupUrl = WebappConfigUtil.getParameter("CCOD_ROOT")+"/skill/update";
	
	private static String deleteGroupUrl = WebappConfigUtil.getParameter("CCOD_ROOT")+"/skill/delete";
	
	private static String bindAgentUrl = WebappConfigUtil.getParameter("CCOD_ROOT")+"/agent/bind";
	
	private static String disbindAgentUrl = WebappConfigUtil.getParameter("CCOD_ROOT")+"/agent/disbind";
	
	/**
	 * 调用CCOD添加坐席
	 * @param po
	 * @return
	 * @author wangjie
	 * @time 2016年3月11日下午6:44:54
	 */
	public static CCODResponsePo addAgent(CCODRequestPo po){
		String result = "-1";
		String desc = "调用CCOD添加坐席失败";
		CCODResponsePo resultPo = new CCODResponsePo();
		String data="";
		try{
			Map<String, String> param = BuildParamMapUtils.getStringParams(po, CCODRequestPo.class);
			String s = JsonUtils.toJson(param);
			
			SystemLogUtils.Debug("========准备调用CCOD添加坐席接口：=======param="+param);
			data = HttpPostUtils.httpPost(addAgentUrl, s);
			SystemLogUtils.Debug("========调用CCOD添加坐席接口：=======param="+param+",返回:"+data);
			JSONObject json=new JSONObject(data);
			if(json.has("result")){
				result = json.getString("result");
			}
			if(json.has("desc")){
				desc = json.getString("desc");
			}
			if("0".equals(result)){
				SystemLogUtils.Debug("========调用CCOD添加坐席接口：=======param="+param+",添加坐席成功,result="+result+",desc="+json.getString("desc"));
			}
			else if("-40004".equals(result)){
				SystemLogUtils.Debug("========调用CCOD添加坐席接口：=======param="+param+",该坐席已存在,result="+result+",desc="+json.getString("desc"));
			}
			else{
				SystemLogUtils.Debug("========调用CCOD添加坐席接口：=======param="+param+",添加坐席失败,result="+result+",desc="+json.getString("desc"));
				throw new ServiceException("调用CCOD添加坐席失败,异常:"+desc);
			}
			resultPo.setResult(result);
			resultPo.setDesc(desc);
			SystemLogUtils.Debug("========调用CCOD添加坐席接口：=======param="+param+",添加成功,result="+result+",desc="+desc);
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException("调用CCOD添加坐席失败,异常:"+e.getMessage());
		}
		return resultPo;
	}
	
	/**
	 * 调用CCOD修改坐席信息
	 * @param po
	 * @return
	 * @author wangjie
	 * @time 2016年3月15日下午5:57:25
	 */
	public static CCODResponsePo updateAgent(CCODRequestPo po){
		String result = "-1";
		String desc = "调用CCOD修改坐席失败";
		CCODResponsePo resultPo = new CCODResponsePo();
		String data="";
		try{
			Map<String, String> param = BuildParamMapUtils.getStringParams(po, CCODRequestPo.class);
			String s = JsonUtils.toJson(param);
			
			SystemLogUtils.Debug("========准备调用CCOD修改坐席接口：=======param="+param);
			data = HttpPostUtils.httpPost(updateAgentUrl, s);
			SystemLogUtils.Debug("========调用CCOD修改坐席接口：=======param="+param+",返回:"+data);
			JSONObject json=new JSONObject(data);
			if(json.has("result")){
				result = json.getString("result");
			}
			if(json.has("desc")){
				desc = json.getString("desc");
			}
			if(!"0".equals(result)){
				SystemLogUtils.Debug("========调用CCOD修改坐席接口：=======param="+param+",修改坐席失败,result="+result+",desc="+desc);
				throw new ServiceException("调用CCOD修改坐席失败,异常:"+desc);
			}
			resultPo.setResult(result);
			resultPo.setDesc(desc);
			SystemLogUtils.Debug("========调用CCOD修改坐席接口：=======param="+param+",修改成功,返回:"+data);
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException("调用CCOD修改坐席失败,异常:"+e.getMessage());
		}
		return resultPo;
	}
	
	/**
	 * 调用CCOD删除坐席
	 * @param enterpriseId
	 * @param agentId
	 * @return
	 * @author wangjie
	 * @time 2016年3月16日上午10:23:18
	 */
	public static CCODResponsePo deleteAgent(String enterpriseId, String agentId){
		String result = "-1";
		String desc = "调用CCOD删除坐席失败";
		CCODResponsePo resultPo = new CCODResponsePo();
		String data="";
		try{
			Map<String, String> param = new HashMap<String, String>();
			param.put("enterpriseId", enterpriseId);
			param.put("agentId", agentId);
			String s = JsonUtils.toJson(param);
			
			SystemLogUtils.Debug("========准备调用CCOD删除坐席接口：=======param="+param);
			data = HttpPostUtils.httpPost(deleteAgentUrl, s);
			SystemLogUtils.Debug("========调用CCOD删除坐席接口：=======param="+param+",返回:"+data);
			JSONObject json=new JSONObject(data);
			if(json.has("result")){
				result = json.getString("result");
			}
			if(json.has("desc")){
				desc = json.getString("desc");
			}
			if(!"0".equals(result)){
				SystemLogUtils.Debug("========调用CCOD删除坐席接口：=======param="+param+",删除坐席失败,result="+result+",desc="+desc);
				throw new ServiceException("调用CCOD删除坐席失败,异常:"+desc);
			}
			resultPo.setResult(result);
			resultPo.setDesc(desc);
			SystemLogUtils.Debug("========调用CCOD删除坐席接口：=======param="+param+",删除成功,返回:"+data);
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException("调用CCOD删除坐席失败,异常:"+e.getMessage());
		}
		return resultPo;
	}
	
	/**
	 * 调用ccod添加技能组
	 * @param enterpriseId
	 * @param group
	 * @return
	 */
	public static CCODResponsePo addGroup(String enterpriseId, GroupPo group) throws ServiceException{
		CCODResponsePo resultPo = new CCODResponsePo();
		String result = "-1";
		String desc = "调用CCOD添加技能组失败";
		String groupId="";
		String data="";
		JSONObject param=new JSONObject();
		JSONObject queueStrategy=new JSONObject();
		JSONObject distrabute=new JSONObject();
		try {
			queueStrategy.put("queueTimeout", "10");
			distrabute.put("strategyTag", "1003");
			param.put("enterpriseId", enterpriseId);
			//param.put("skillGroupId", group.getGroupId());
			param.put("skillGroupDesc", group.getGroupName());
			param.put("skillType", "2");
			param.put("ucdsQueueStrategyScript", queueStrategy);
			param.put("ucdsDistrabuteStrategyScript", distrabute);
			param.put("serviceId", "0000");
			/*param.put("isRecord", "1");
			param.put("recordPath", "");
			param.put("recordStatus", "");
			param.put("handleJoinFailure", "");*/
			
			SystemLogUtils.Debug("========准备调用CCOD技能组添加接口：=======param="+param);
			data=HttpPostUtils.httpPost(addGroupUrl,param.toString());
			SystemLogUtils.Debug("========调用CCOD技能组添加接口：=======param="+param+",返回:"+data);
			JSONObject json=new JSONObject(data);
			if(json.has("result")){
				result = json.getString("result");
			}
			if(json.has("desc")){
				desc = json.getString("desc");
			}
			if(json.has("skillGroupId")){
				groupId = json.getString("skillGroupId");
			}
			else{
				throw new ServiceException("调用CCOD技能组异常,技能组编号不能为空");
			}
			if(!"0".equals(result)){
				SystemLogUtils.Debug("========调用CCOD技能组添加接口：=======param="+param+",添加技能组失败,result="+result+",desc="+desc);
				throw new ServiceException("调用CCOD技能组添加失败,异常:"+desc);
			}
			resultPo.setResult(result);
			resultPo.setDesc(desc);
			resultPo.setGroupId(groupId);
			SystemLogUtils.Debug("========调用CCOD技能组添加接口：=======param="+param+",添加成功");
		} catch (JSONException | IOException |ServiceException e) {
			e.printStackTrace();
			throw new ServiceException("调用CCOD添加技能组失败,异常:"+e.getMessage());
		}
		return resultPo;
	}
	/**
	 * 调用ccod修改技能组
	 * @param enterpriseId
	 * @param group
	 * @return
	 */
	public static CCODResponsePo updateGroup(String enterpriseId, GroupPo group) throws ServiceException{
		CCODResponsePo resultPo = new CCODResponsePo();
		String result = "-1";
		String desc = "调用CCOD修改技能组失败";
		String data="";
		JSONObject param=new JSONObject();
		JSONObject queueStrategy=new JSONObject();
		JSONObject distrabute=new JSONObject();
		try {
			queueStrategy.put("queueTimeout", "10");
			distrabute.put("strategyTag", "1003");
			param.put("enterpriseId", enterpriseId);
			param.put("skillGroupId", group.getGroupId());
			param.put("skillGroupDesc", group.getGroupName());
			param.put("skillType", "2");
			param.put("ucdsQueueStrategyScript", queueStrategy);
			param.put("ucdsDistrabuteStrategyScript", distrabute);
			param.put("serviceId", "0000");
			/*param.put("isRecord", "1");
			param.put("recordPath", "");
			param.put("recordStatus", "");
			param.put("handleJoinFailure", "");*/
			
			SystemLogUtils.Debug("========准备调用CCOD技能组修改接口：=======param="+param);
			data=HttpPostUtils.httpPost(updateGroupUrl,param.toString());
			SystemLogUtils.Debug("========调用CCOD技能组修改接口：=======param="+param+",返回:"+data);
			JSONObject json=new JSONObject(data);
			if(json.has("result")){
				result = json.getString("result");
			}
			if(json.has("desc")){
				desc = json.getString("desc");
			}
			if(!"0".equals(result)){
				SystemLogUtils.Debug("========调用CCOD技能组修改接口：=======param="+param+",修改技能组失败,result="+result+",desc="+desc);
				throw new ServiceException("调用CCOD技能组修改失败,异常:"+desc);
			}
			resultPo.setResult(result);
			resultPo.setDesc(desc);
			SystemLogUtils.Debug("========调用CCOD技能组修改接口：=======param="+param+",修改成功");
		} catch (JSONException | IOException e) {
			e.printStackTrace();
			throw new ServiceException("调用CCOD修改技能组失败,异常:"+e.getMessage());
		}
		return resultPo;
	}
	/**
	 *  调用ccod删除技能组
	 * @param enterpriseId
	 * @param group
	 * @return
	 */
	public static CCODResponsePo deleteGroup(String enterpriseId, String  groupId) throws ServiceException{
		CCODResponsePo resultPo = new CCODResponsePo();
		String result = "-1";
		String desc = "调用CCOD删除技能组失败";
		String data="";
		JSONObject param=new JSONObject();
		try {
			param.put("enterpriseId", enterpriseId);
			param.put("skillGroupId",groupId);
			
			SystemLogUtils.Debug("========准备调用CCOD技能组删除接口：=======param="+param);
			data=HttpPostUtils.httpPost(deleteGroupUrl,param.toString());
			SystemLogUtils.Debug("========调用CCOD技能组删除接口：=======param="+param+",返回:"+data);
			JSONObject json=new JSONObject(data);
			if(json.has("result")){
				result = json.getString("result");
			}
			if(json.has("desc")){
				desc = json.getString("desc");
			}
			if(!"0".equals(result)){
				SystemLogUtils.Debug("========调用CCOD技能组删除接口：=======param="+param+",删除技能组失败,result="+result+",desc="+desc);
				throw new ServiceException("调用CCOD技能组删除失败,异常:"+desc);
			}
			resultPo.setResult(result);
			resultPo.setDesc(desc);
			SystemLogUtils.Debug("========调用CCOD技能组删除接口：=======param="+param+",删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("调用CCOD删除技能组失败,异常:"+e.getMessage());
		}
		return resultPo;
	}
	/**
	 * 调用ccod绑定技能组坐席接口
	 * @param enterpriseId
	 * @param groupId
	 * @param userIds
	 * @return
	 */
	public static CCODResponsePo bindAgents(String enterpriseId,String groupId,String userIds)throws ServiceException{
		CCODResponsePo resultPo = new CCODResponsePo();
		String result = "1";
		String data="";
		String agentIds=userIds.replaceAll(",", "|");
		List<JSONObject> agentList=new ArrayList<JSONObject>();
		try {
			JSONObject agent=new JSONObject();
			agent.put("enterpriseId", enterpriseId);
			agent.put("skillGroupId", groupId);
			agent.put("agentId", agentIds);
			agent.put("skillLevel", "1");
			agentList.add(agent);
			
			JSONObject param=new JSONObject();
			param.put("bindList",agentList);
			String jsonParam=param.toString();
			SystemLogUtils.Debug("========准备调用CCOD技能组绑定坐席接口：=======param="+jsonParam);
		
			data=HttpPostUtils.httpPost(bindAgentUrl,jsonParam);
			SystemLogUtils.Debug("========调用CCOD技能组绑定坐席接口：=======param="+jsonParam+",返回:"+data);
			JSONObject json=new JSONObject(data);
			result = json.getString("result");
			if(!"0".equals(result)){
				SystemLogUtils.Debug("========调用CCOD技能组绑定坐席接口：=======param="+param+",技能组绑定坐席组失败,result="+result+",desc="+json.getString("desc"));
				throw new ServiceException("调用CCOD技能组绑定坐席失败,"+json.getString("desc"));
			}
			resultPo.setResult(result);
			resultPo.setDesc(json.getString("desc"));
			SystemLogUtils.Debug("========调用CCOD技能组绑定坐席接口：=======param="+param+",绑定成功");
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
		return resultPo;
	}
	public static CCODResponsePo changeGroups(String enterpriseId,String groupIds,String userId,String type) throws ServiceException{
		CCODResponsePo resultPo = new CCODResponsePo();
		String result = "1";
		String data="";
		String[] groupId=groupIds.split(",");
		List<JSONObject> agentList=new ArrayList<JSONObject>();
		try {
			for(int i=0;i<groupId.length;i++){
				JSONObject agent=new JSONObject();
				agent.put("enterpriseId", enterpriseId);
				agent.put("skillGroupId", groupId[i]);
				agent.put("agentId", userId);
				agent.put("skillLevel", "1");
				agentList.add(agent);
			}
			JSONObject param=new JSONObject();
			param.put(type,agentList);
			String jsonParam=param.toString();
			SystemLogUtils.Debug(String.format("========准备调用CCOD技能组%s接口：=======param=%s", type,jsonParam));
		
			if(type.startsWith("dis")){
				data=HttpPostUtils.httpPost(disbindAgentUrl,jsonParam);
			}else{
				data=HttpPostUtils.httpPost(bindAgentUrl,jsonParam);
			}
			SystemLogUtils.Debug(String.format("========调用CCOD技能组%s接口：=======param=%s,返回：", type,jsonParam));
			JSONObject json=new JSONObject(data);
			result = json.getString("result");
			if(!"0".equals(result)){
				SystemLogUtils.Debug(String.format("========调用CCOD技能组%s接口失败：=======param=%s，result=%s，desc=%s",type,param,result,json.getString("desc")));
				throw new ServiceException("调用CCOD技能组"+type+"失败,"+json.getString("desc"));
			}
			resultPo.setResult(result);
			resultPo.setDesc(json.getString("desc"));
			SystemLogUtils.Debug("========调用CCOD技能组"+type+"接口：=======param="+param+",成功");
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
		return resultPo;
	}
	/**
	 * ccod技能组解绑坐席
	 * @param enterpriseId
	 * @param groupId
	 * @param userIds
	 * @return
	 */
	public static CCODResponsePo disbindAgents(String enterpriseId,String groupId,String userIds) throws ServiceException{
		CCODResponsePo resultPo = new CCODResponsePo();
		String result = "1";
		String data="";
		String agentIds=userIds.replaceAll(",", "|");
		List<DBObject> agentList=new ArrayList<DBObject>();
		try {
			DBObject agent=new BasicDBObject();
			agent.put("enterpriseId", enterpriseId);
			agent.put("skillGroupId", groupId);
			agent.put("agentId", agentIds);
			agent.put("skillLevel", "1");
			agentList.add(agent);
			DBObject param=new BasicDBObject("disbindList",agentList);
			String jsonParam=JSON.serialize(param);
			SystemLogUtils.Debug("========准备调用CCOD技能组解除绑定坐席接口：=======param="+jsonParam);
			
			data=HttpPostUtils.httpPost(disbindAgentUrl,jsonParam);
			SystemLogUtils.Debug("========调用CCOD技能组解除绑定坐席接口：=======param="+jsonParam+",返回:"+data);
			JSONObject json=new JSONObject(data);
			result = json.getString("result");
			if(!"0".equals(result)){
				SystemLogUtils.Debug("========调用CCOD技能组解除绑定坐席接口：=======param="+param+",技能组解除绑定坐席组失败,result="+result+",desc="+json.getString("desc"));
				throw new ServiceException("调用CCOD技能组解除绑定坐席失败,"+json.getString("desc"));
			}
			resultPo.setResult(result);
			resultPo.setDesc(json.getString("desc"));
			SystemLogUtils.Debug("========调用CCOD技能组解除绑定坐席接口：=======param="+param+",解除绑定成功");
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
		return resultPo;
	}
	
	public static CCODResponsePo changeUserGroups(String[] groupId,String[] newGroupId,String userId,String entId) throws Exception{
		
		try {
			String bindGroup="";
			String disbindGroup="";
			for(int i=0;i<groupId.length;i++){
				boolean has=false;
				for(int j=0;j<newGroupId.length;j++){
					if(groupId[i].equals(newGroupId[j])){
						newGroupId[j]="-1";
						has=true;
						break;
					}else{
						continue;
					}
				}
				if(!has){
					if(disbindGroup.equals("")){
						disbindGroup=groupId[i];
					}else{
						disbindGroup+=","+groupId[i];
					}
				}
			}
			for(int j=0;j<newGroupId.length;j++){
				if(newGroupId[j].equals("-1")){
					continue;
				}
				if(bindGroup.equals("")){
					bindGroup=newGroupId[j];
				}else{
					bindGroup+=","+newGroupId[j];
				}
			}
			if(StringUtils.isNotEmpty(bindGroup)){
				CCODResponsePo returnPo=CCODClient.changeGroups(entId, bindGroup, userId, "bindList");
				if(StringUtils.isNotEmpty(disbindGroup)){
					CCODResponsePo returnPo0=CCODClient.changeGroups(entId, disbindGroup, userId, "disbindList");
					returnPo.setDesc(returnPo.getDesc()+"++++"+returnPo0.getDesc());
				}
				return returnPo;
			}
			return new CCODResponsePo("0","不需要调用ccod接口");
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
		
	}
	
}

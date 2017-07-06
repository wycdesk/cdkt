package com.channelsoft.ems.api.test;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.channelsoft.cri.util.HttpPostUtils;
import com.channelsoft.cri.util.WebappConfigUtil;
import com.channelsoft.ems.api.client.CCODClient;
import com.channelsoft.ems.api.client.EntClient;
import com.channelsoft.ems.api.constants.EventNameConstants;
import com.channelsoft.ems.api.po.CCODResponsePo;
import com.channelsoft.ems.group.po.GroupPo;
import com.channelsoft.ems.register.util.MD5Util;
import com.channelsoft.ems.user.constant.LoginType;


public class TestUtil {
//{"total":1,"rows":"{ \"pwd\" : \"29289242\" , \"cdeskUrl\" : \"http://test1.cdesk.com:8080/ems\"}","success":true,"msg":"添加成功"}
	public static void testAddEnt(){

		Map<String, String> paramMap = new HashMap<String, String>();
//		String urlAddress="http://10.130.41.172:8080/ems/api/ent/add";
		String urlAddress="http://127.0.0.1:8080/ems/api/ent/add";
//		String urlAddress="http://115.28.16.107:8080/api/ent/add";
		//		String urlAddress="http://10.130.29.2:8080/ems/api/ent/add";
		try {
			JSONObject rows=new JSONObject();
			String entId="111116";
			String loginName="150184455";
			rows.put("entId", entId);
			rows.put("domainName", "84455");
			rows.put("loginName", loginName);
			rows.put("contactUser", "测试1");
			rows.put("contactWay", "18708160600");
			rows.put("email", "360641441@qq.com");
			rows.put("entName", "qn1");
			rows.put("address", "");
			
			rows.put("accountType", LoginType.TELEPHONE.value);
		  	String skey = WebappConfigUtil.getParameter("USER_CENTER_SKEY");
			String signKey=MD5Util.MD5(entId+"_"+loginName+"_"+skey);
			rows.put("signKey", signKey);
			String ent=rows.toString();
			String entInfo=URLEncoder.encode(ent, "UTF-8");
			paramMap.put("entInfo", entInfo);
			String res=HttpPostUtils.httpPost(urlAddress, paramMap);
			System.out.println(res);
			try {
				JSONObject ja=new JSONObject(res);
				boolean success=ja.getBoolean("success");
				if(success){
					JSONObject data=ja.getJSONObject("rows");
					String cdeskUrl=data.getString("cdeskUrl");
					System.out.println("success="+success+",cdeskUrl="+cdeskUrl);
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public static void testSaveComm(){
		Map<String,String> param=new HashMap<String,String>();
		String urlAdd="http://localhost:8080/ems/api/communicate/saveComm";
		
		try{
			JSONObject rows=new JSONObject();
			rows.put("sessionId", "asa1111156010:SSS:1001");
			rows.put("ccodEntId", "SSS");
			rows.put("ccodAgentId", "1001");
			//rows.put("ccodAgentName", "名字3");
			rows.put("accountId", "13101012401");
			rows.put("accountType", "1");
			rows.put("source", LoginType.TELEPHONE.value);
			rows.put("isConnected", "1");
			rows.put("eventName", EventNameConstants.EVENT_TP_DISCONNECT);
			rows.put("optTime", new Date().getTime()+"");
			String commInfo=rows.toString();
			commInfo=URLEncoder.encode(commInfo, "UTF-8");
			param.put("commInfo", commInfo);
			
			String res=HttpPostUtils.httpPost(urlAdd, param);
			System.out.println(res);
			/*try{
				JSONObject result=new JSONObject(res);
				boolean success=result.getBoolean("success");
				if(success){
					
				}
			}*/
		}catch(IOException e){
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public static void  addGroup(){
		
		String enterpriseId="SSS";
		GroupPo group=new GroupPo();
		//从一万开始未测试ID
		group.setGroupId("10000");
		group.setGroupName("ccodGorupname1");
		CCODResponsePo resultPo=CCODClient.addGroup(enterpriseId, group);
		System.out.println("result="+resultPo.getResult()+"\ndesc="+resultPo.getDesc());
	}
	public static void  updateGroup(){
		
		String enterpriseId="SSS";
		GroupPo group=new GroupPo();
		group.setGroupId("10000");
		group.setGroupName("ccodGorupname2");
		CCODResponsePo resultPo=CCODClient.updateGroup(enterpriseId, group);
		System.out.println("result="+resultPo.getResult()+"\ndesc="+resultPo.getDesc());
	}
	
	public static void  deleteGroup(){
		String enterpriseId="SSS";
		CCODResponsePo resultPo=CCODClient.deleteGroup(enterpriseId, "127");
		System.out.println("result="+resultPo.getResult()+"\ndesc="+resultPo.getDesc());
	}
	
	public static void bindAgent(){
		String enterpriseId="SSS";
		String groupId="10000";
		String userIds="557,558,111";
		CCODResponsePo resultPo=CCODClient.bindAgents(enterpriseId, groupId, userIds);
		System.out.println("result="+resultPo.getResult()+"\ndesc="+resultPo.getDesc());
	}
	
	public static void disbindAgent(){
		String enterpriseId="SSS";
		String groupId="129";
		String userIds="557,558";
		CCODResponsePo resultPo=CCODClient.disbindAgents(enterpriseId, groupId, userIds);
		System.out.println("result="+resultPo.getResult()+"\ndesc="+resultPo.getDesc());
	}
	
	public static void initEnt(){
		String enterpriseId="SSS";
		String resultPo=EntClient.initMongDb(enterpriseId);
		System.out.println("result="+resultPo);
	}
	public static void destroyEnt(){
		String enterpriseId="SSS";
		String resultPo=EntClient.destroyEnt(enterpriseId);
		System.out.println("result="+resultPo);
	}
	
	
	public static void main(String[] args){
		//deleteGroup();
		//addGroup();
		//updateGroup();
		//testSaveComm();
		
		testAddEnt();
		
		//bindAgent();
//		disbindAgent();
		
//		testUrl();
        
		
//		String encode="%7B%22title%22:%22%E5%91%BC%E5%87%BA%E8%87%B3%2013183890106%20%E7%9A%84%E8%AF%AD%E9%9F%B3%E7%94%B5%E8"+
//"%AF%9D%22,%22content%22:%22%3Cul%3E%3Cli%3E%E7%94%B5%E8%AF%9D%E5%91%BC%E5%87%BA%E8%87%B3:13183890106"+
//"%3C/li%3E%3Cli%3E%E6%97%B6%E9%97%B4:2016%E5%B9%B403%E6%9C%8827%E6%97%A5%2016:23%3C/li%3E%3Cli%3E%E5%91"+
//"%BC%E5%87%BA%E4%BA%BA:wangyong@channelsoft.com%3C/li%3E%3C/ul%3E%22,%22entId%22:%22channelsoft%22,%22issue"+
//"%22:%22%22,%22serviceGroupId%22:%2270%22,%22serviceGroupName%22:%22%E7%AD%94%E7%96%91%E7%BB%84%22,%22customServiceId"+
//"%22:%22324%22,%22customServiceName%22:%22%E7%8E%8B%E5%8B%87%22,%22creatorId%22:%22324%22,%22creatorName"+
//"%22:%22%E7%8E%8B%E5%8B%87%22,%22creatorEmail%22:%22wangyong@channelsoft.com%22,%22status%22:%221%22,"+
//"%22source%22:%225%22,%22isAgent%22:%221%22%7D";
//		
//		String test;
//		try {
//			test = URLDecoder.decode(encode,"utf-8");
//			
//			System.out.println(test);
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
		testDate();
		
		
	}
	
	public static void  testUrl(){
		System.out.println(new Date().getTime());
	}
	public static void  testDate(){
		String jsonStr="[{\"text\":\"系统消息：新用户到来\",\"direction\":\"recv\",\"type\":\"text\",\"dateObj\":\"2016-03-27T03:21:07.306Z\"},{\"text\":\"哈喽\",\"direction\":\"recv\",\"type\":\"text\",\"dateObj\":\"2016-03-27T03:21:26.628Z\"},{\"text\":\"你是谁啊\",\"direction\":\"send\",\"type\":\"text\",\"dateObj\":\"2016-03-27T03:21:39.376Z\"},{\"text\":\"我是电脑\",\"direction\":\"recv\",\"type\":\"text\",\"dateObj\":\"2016-03-27T03:21:49.323Z\"},{\"text\":\"哈哈哈\",\"direction\":\"send\",\"type\":\"text\",\"dateObj\":\"2016-03-27T03:21:55.436Z\"},{\"text\":\"呵呵呵\",\"direction\":\"recv\",\"type\":\"text\",\"dateObj\":\"2016-03-27T03:22:03.722Z\"}]";
		try {
			JSONArray jsonL=new JSONArray(jsonStr);
			SimpleDateFormat fmt=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			String dateStr=jsonL.getJSONObject(3).getString("dateObj");
			Date dt=fmt.parse(dateStr);
			System.out.println(dt.toLocaleString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

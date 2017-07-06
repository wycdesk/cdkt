package com.channelsoft.ems.api.client;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.List;

import org.apache.axis.AxisFault;
import org.json.JSONException;
import org.json.JSONObject;

import localhost.dcms.webservice.QueryRecord.QueryRecordServiceLocator;
import localhost.dcms.webservice.QueryRecord.QueryRecordSoapBindingStub;

import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.logger.SystemLogUtils;
import com.channelsoft.cri.util.HttpPostUtils;
import com.channelsoft.cri.util.WebappConfigUtil;
import com.channelsoft.ems.api.po.CCODResponsePo;
import com.channelsoft.ems.api.util.QueryRecordXmlUtils;

/**
 * 获取录音
 * @author wangjie
 * @time 2016年3月12日下午7:24:10
 */
public class QueryRecordClient {

	private static String queryRecordWebserviceUrl = WebappConfigUtil.getParameter("RECORD_ROOT")+"/webservice/QueryRecord?wsdl";
	private static String queryRecordUrl = WebappConfigUtil.getParameter("CCOD_ROOT")+"/record/searchByRecordId";
	
	/**
	 * 查询录音文件URL
	 * @param enterpriseID CCOD企业ID
	 * @param agentId 坐席ID
	 * @param password  坐席密码
	 * @param sessionID
	 * @return
	 * @author wangjie
	 * @time 2016年3月12日下午8:34:33
	 */
	public static String getRecordUrl(String ccodEntId, String agentId, String password, String sessionID){
		return getRecordUrlForwebservice(ccodEntId,agentId,password,sessionID);
		
	}
	public static String getRecordUrlForHttp(String ccodEntId, String agentId, String password, String sessionID){
		CCODResponsePo resultPo = new CCODResponsePo();
		String result = "1";
		String data="";
		JSONObject param=new JSONObject();
        String url="";
		try {
			param.put("entId", ccodEntId);
			param.put("agentId",agentId);
			param.put("password",password);
			param.put("sessionID", sessionID);
			SystemLogUtils.Debug("========准备调用CCOD查询录音接口：=======param="+param);
			data=HttpPostUtils.httpPost(queryRecordUrl,param.toString());
			SystemLogUtils.Debug("========准备调用CCOD查询录音接口：=======param="+param+",返回:"+data);
			JSONObject json=new JSONObject(data);
			result = json.getString("result");
			if(!"0".equals(result)){
				SystemLogUtils.Debug("========调用CCOD查询录音接口：=======param="+param+",查询失败,result="+result+",desc="+json.getString("desc"));
				throw new ServiceException("调用CCOD查询录音接口失败");
			}
			resultPo.setResult(result);
			resultPo.setDesc(json.getString("desc"));
			url=json.getString("url");
			SystemLogUtils.Debug("========调用CCOD查询录音接口：=======param="+param+",查询成功,url="+url);
		} catch (JSONException | IOException e) {
			e.printStackTrace();
			SystemLogUtils.Debug("========调用CCOD查询录音接口：=======param="+param+",查询失败");
			throw new ServiceException("调用CCOD查询录音接口失败");
		}
		return url;
	}
	public static String getRecordUrlForwebservice(String ccodEntId, String agentId, String password, String sessionID){
//		String s = "http://dcms.ccod.cn/group1/M01/1F/05/CmYABFaWFiqAA1TmAAAqFRBAsVA527.mp3";
//		return s;
		String recordUrl = "";
		String data="";
		try {
			QueryRecordServiceLocator service = new QueryRecordServiceLocator();
			URL url = new URL(queryRecordWebserviceUrl);
			QueryRecordSoapBindingStub stub = new QueryRecordSoapBindingStub(url,service);
			SystemLogUtils.Debug("========准备调用CCOD webservice查询录音接口：=======ccodEntId="+ccodEntId+",sessionID="+sessionID+",agentId="+agentId+",password="+password);
			data = stub.queryRecordAddr(ccodEntId, agentId, password, sessionID);
			if("null".equals(data)||data==null) data="";
			SystemLogUtils.Debug("========调用CCOD webservice查询录音接口返回：=======ccodEntId="+ccodEntId+",sessionID="+sessionID+",agentId="+agentId+",data="+data);
			/*if(data.startsWith("<recordFiles>")){
				List<String> list = QueryRecordXmlUtils.parse(data);
				if(list.size()>0){
					recordUrl = list.get(0);
				}
			}else{
				SystemLogUtils.Debug("========调用CCOD webservice查询录音接口查询失败：=======ccodEntId="+ccodEntId+",sessionID="+sessionID+",agentId="+agentId+",data="+data);
			}*/
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}catch (AxisFault e) {
			e.printStackTrace();
		}catch (RemoteException e) {
			e.printStackTrace();
		}
		return data;
	}
}

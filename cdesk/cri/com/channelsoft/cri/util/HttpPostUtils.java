package com.channelsoft.cri.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * <dl>
 * <dt>HttpConnectionUtils</dt>
 * <dd>Description:Http请求的POST提交</dd>
 * <dd>Copyright: Copyright (C) </dd>
 * <dd>Company: 青牛（北京）技术有限公司</dd>
 * <dd>CreateDate: 2009-12-11</dd>
 * </dl>
 * 
 * @author 胡海波
 */
public class HttpPostUtils {
	/**
	 * Http 的 post请求
	 * @param urlAddress 请求消息的URL
	 * @param paramMap   参数的MAP （KEY：VALUE）
	 * @return
	 * @author 胡海波   
	 * 2009-12-11
	 */
	public static String httpPost(String urlAddress,Map<String, String> paramMap, int timeout){
		if(paramMap==null){
			paramMap = new HashMap<String, String>();
		}
		String [] params = new String[paramMap.size()];
		int i = 0;
		for(String paramKey:paramMap.keySet()){
			String param = paramKey+"="+paramMap.get(paramKey);
			params[i] = param;
			i++;
		}
		return httpPost(urlAddress, params, timeout);
	}
	/**
	 * Http 的 post请求
	 * @param urlAddress 请求消息的URL
	 * @param paramMap   参数的MAP （KEY：VALUE）
	 * @return
	 * @author 胡海波   
	 * 2009-12-11
	 */
	public static String httpPost(String urlAddress,List<String> paramList, int timeout){
		if(paramList==null){
			paramList = new ArrayList<String>();
		}
		return httpPost(urlAddress, paramList.toArray(new String[0]), timeout);
	}
	/**
	 * Http 的 post请求
	 * @param urlAddress 请求消息的URL
	 * @param params     参数数组
	 * @return
	 * @author 胡海波   
	 * 2009-12-11
	 */
	public static String httpPost(String urlAddress,String []params, int timeout){
		URL url = null;
		HttpURLConnection con  =null;
		BufferedReader in = null;
		StringBuffer result = new StringBuffer();
	    try {
		    url = new URL(urlAddress);
			con  = (HttpURLConnection) url.openConnection();
			con.setConnectTimeout(timeout);
		    con.setUseCaches(false);
		    con.setDoOutput(true);
		    con.setRequestMethod("POST");
		    String paramsTemp = "";
		    for(String param:params){
		    	if(param!=null&&!"".equals(param.trim())){
		    		paramsTemp+="&"+param;
		    	}
		    }
		    byte[] b = paramsTemp.getBytes("utf8");
		    con.getOutputStream().write(b, 0, b.length);
		    con.getOutputStream().flush();
		    con.getOutputStream().close();
		    in = new BufferedReader(new InputStreamReader(con.getInputStream(),Charset.forName("utf-8")));
		    while (true) {
		      String line = in.readLine();
		      if (line == null) {
		        break;
		      }
		      else {
		    	  result.append(line);
		      }
		    }
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(in!=null){
					in.close();
				}
				if(con!=null){
					con.disconnect();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result.toString();
	}
	
	/**
	 * Http 的 post请求
	 * @param urlAddress 请求消息的URL
	 * @param paramMap   参数的MAP （KEY：VALUE）
	 * @return
	 * @author 胡海波   
	 * 2009-12-11
	 */
	public static String httpPost(String urlAddress,Map<String, String> paramMap) throws IOException {
		if(paramMap==null){
			paramMap = new HashMap<String, String>();
		}
		String [] params = new String[paramMap.size()];
		int i = 0;
		for(String paramKey:paramMap.keySet()){
			String param = paramKey+"="+paramMap.get(paramKey);
			params[i] = param;
			i++;
		}
		return httpPost(urlAddress, params);
	}
	/**
	 * Http 的 post请求
	 * @param urlAddress 请求消息的URL
	 * @param paramMap   参数的MAP （KEY：VALUE）
	 * @return
	 * @author 胡海波   
	 * 2009-12-11
	 */
	public static String httpPost(String urlAddress,List<String> paramList) throws IOException {
		if(paramList==null){
			paramList = new ArrayList<String>();
		}
		return httpPost(urlAddress, paramList.toArray(new String[0]));
	}
	/**
	 * Http 的 post请求
	 * @param urlAddress 请求消息的URL
	 * @param params     参数数组
	 * @return
	 * @author 胡海波   
	 * 2009-12-11
	 */
	public static String httpPost(String urlAddress,String []params) throws IOException {
		URL url = null;
		HttpURLConnection con  =null;
		BufferedReader in = null;
		StringBuffer result = new StringBuffer();
	    try {
		    url = new URL(urlAddress);
			con  = (HttpURLConnection) url.openConnection();
			con.setConnectTimeout(1000);
		    con.setUseCaches(false);
		    con.setDoOutput(true);
		    con.setRequestMethod("POST");
		    String paramsTemp = "";
		    for(String param:params){
		    	if(param!=null&&!"".equals(param.trim())){
		    		paramsTemp+="&"+param;
		    	}
		    }
		    byte[] b = paramsTemp.getBytes("utf8");
		    con.getOutputStream().write(b, 0, b.length);
		    con.getOutputStream().flush();
		    con.getOutputStream().close();
		    in = new BufferedReader(new InputStreamReader(con.getInputStream(),Charset.forName("utf-8")));
		    while (true) {
		      String line = in.readLine();
		      if (line == null) {
		        break;
		      }
		      else {
		    	  result.append(line);
		      }
		    }
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}finally{
			try {
				if(in!=null){
					in.close();
				}
				if(con!=null){
					con.disconnect();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result.toString();
	}
	
	public static String httpPost(String urlAddress,String params) throws IOException {
		URL url = null;
		HttpURLConnection con  =null;
		BufferedReader in = null;
		StringBuffer result = new StringBuffer();
	    try {
		    url = new URL(urlAddress);
			con  = (HttpURLConnection) url.openConnection();
			con.setConnectTimeout(1000);
			con.setRequestProperty("Content-Type", "application/json");
		    con.setUseCaches(false);
		    con.setDoOutput(true);
		    con.setRequestMethod("POST");
		    byte[] b = params.getBytes("utf8");
		    con.getOutputStream().write(b, 0, b.length);
		    con.getOutputStream().flush();
		    con.getOutputStream().close();
		    in = new BufferedReader(new InputStreamReader(con.getInputStream(),Charset.forName("utf-8")));
		    while (true) {
		      String line = in.readLine();
		      if (line == null) {
		        break;
		      }
		      else {
		    	  result.append(line);
		      }
		    }
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}finally{
			try {
				if(in!=null){
					in.close();
				}
				if(con!=null){
					con.disconnect();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result.toString();
	}
}

package com.channelsoft.cri.util;

/**
 * @Author wangyong
 */


import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author wangyong
 *
 */
public class JsonStrUtils {

	/**
	 * list指定属性映射到json字符串中
	 * @param list
	 * @param str
	 * @return
	 * Author:wangyong
	 * 2014年7月31日
	 * String
	 */
	public static String getJsonStr(List<? extends Object> list,String[] property){
		String jsonStr="";
		if(property==null){
			jsonStr=new JSONArray(list).toString();
		}
		else if(list==null||list.size()==0){
			jsonStr="[]";
		}
		else{
			JSONArray jsonArr=new JSONArray();
			for(int k=0;k<list.size();k++){
				Object o=list.get(k);
				JSONObject jo=getJsonObject(o,property);
				jsonArr.put(jo);
			}  
			jsonStr=jsonArr.toString();
		}
		
		return jsonStr;
	}
	/**
	 * 
	 * @param list
	 * @param propertyStr 要显示的属性以regex分隔
	 * @param regex 分割符
	 * @return
	 * Author:wangyong
	 * 2014年8月1日
	 * String
	 */
	public static String getJsonStr(List<? extends Object> list,String propertyStr,String regex){
		String[] property=propertyStr.split(regex);
		return getJsonStr(list,property);
	}
	/**
	 * 
	 * @param list
	 * @param propertyStr 要显示的属性以逗号分隔
	 * @return
	 * Author:wangyong
	 * 2014年8月1日
	 * String
	 */
	public static String jsonStr(List<? extends Object> list,String propertyStr){
		return getJsonStr(list,propertyStr,",");
	}
	/**
	 * Object指定属性映射到json字符串中
	 * @param o
	 * @param propertyStr,以逗号为分隔符
	 * @return
	 * Author:wangyong
	 * 2014年8月1日
	 * String
	 */
	public static String getJsonStr(Object o,String propertyStr){
		return getJsonStr(o,propertyStr,",");
	}
	/**
	 * Object指定属性映射到json字符串中
	 * @param o
	 * @param propertyStr ,以regex为分隔符
	 * @param regex 分隔符
	 * @return
	 * Author:wangyong
	 * 2014年8月1日
	 * String
	 */
	public static String getJsonStr(Object o,String propertyStr,String regex){
		String[] property=propertyStr.split(regex);
		return getJsonStr(o,property);
		
	}
	/**
	 * 对象转化为含有指定属性的json字符串
	 * @param o
	 * @param property
	 * @return
	 * Author:wangyong
	 * 2014年8月1日
	 * String
	 */
	public static String getJsonStr(Object o,String[] property){
		JSONObject jo=getJsonObject(o,property);
		String jsonStr="";
		try {
			jsonStr=JSONObject.valueToString(jo);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonStr;
	}
	
	public static String getJsonStr(Object o){
		JSONObject jo=new JSONObject(o);
		String jsonStr="";
		try {
			jsonStr=JSONObject.valueToString(jo);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonStr;
	}
	/**
	 * Object 转化为有指定属性的json对象
	 * @param o
	 * @param property
	 * @return
	 * Author:wangyong
	 * 2014年8月1日
	 * JSONObject
	 */
	public static JSONObject getJsonObject(Object o,String[] property){
		JSONObject jo=new JSONObject();
		for(int i=0;i<property.length;i++){
		      String firstLetter = property[i].substring(0, 1).toUpperCase();    
	           String getter = "get" + firstLetter + property[i].substring(1); 
			 try {
				Method method = o.getClass().getMethod(getter, new Class[] {});
				Object value = method.invoke(o, new Object[] {}); 
				jo.put(property[i], value);
			 }  catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			 } 
		}
		return jo;
	}
	/**
	 * 将list对象转化为list json对象
	 * @param list
	 * @param propertyStr
	 * @param regex
	 * @return
	 */
	public static List<JSONObject> getJson(List<? extends Object> list,String propertyStr,String regex){
		String[] property=propertyStr.split(regex);
		List<JSONObject> jlist=new ArrayList<JSONObject>();
		for(int i=0;i<list.size();i++){
			JSONObject json=getJsonObject(list.get(i),property);
			jlist.add(json);
		}
		return jlist;
	}
	

}

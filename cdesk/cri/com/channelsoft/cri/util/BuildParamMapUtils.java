package com.channelsoft.cri.util;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BuildParamMapUtils {
	
	public static String getPoString(Object obj,Class c){
		Map<String,Object> params = getParams(obj,c);
		StringBuffer sb = new StringBuffer(c.getName().substring(c.getName().lastIndexOf("."))+"[");
		
		for(String s:params.keySet()){
			sb.append(s).append("=").append(params.get(s)).append(",");
		}
		sb.append("]");
		return sb.toString();
	}
	public static String getMapString(Object obj){
		Map<String,Object> params = (HashMap)obj;
		StringBuffer sb = new StringBuffer("Map[");
		
		for(String s:params.keySet()){
			sb.append(s).append("=").append(params.get(s)).append(",");
		}
		sb.append("]");
		return sb.toString();
		
	}
	
	public static Map<String,Object> getParams(Object obj){
		Map<String,Object> params = new HashMap<String,Object>();
		Class c = obj.getClass();
        while(null!=c){
        	buildSuperFields(params,obj,c);
        	c=c.getSuperclass();
        }
		return params;
	}
	
	public static Map<String,Object> getParams(Object obj,Class c){
		Map<String,Object> params = new HashMap<String,Object>();
		
        while(null!=c){
        	buildSuperFields(params,obj,c);
        	c=c.getSuperclass();
        }
		return params;
	}
	
	private static Map<String,Object>  buildSuperFields(Map<String,Object> params,Object obj,Class c){
		Field[]	 fields = c.getDeclaredFields(); 
		for (Field fi : fields) {
			fi.setAccessible(true);
			if (fi.getName().equalsIgnoreCase("serialVersionUID")) {
				continue;
			}
			try {
				Object value = fi.get(obj);
				if (value != null&&value!="") {
					params.put(fi.getName(), value);
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return params;
	}
	
	
	public static Map<String,String> getQueryParams(Object obj,Class c,String page,String rows){

		Map<String, String> params = new HashMap<String, String>();
		while (null != c) {
			params = getStringParams(params, obj, c);
			c = c.getSuperclass();
		}
		 
		params.put("page", page);
		params.put("rows", rows);
		return params;
	}
	
	public static Map<String, String> getStringParams(Object obj, Class c) {

		Map<String, String> params = new HashMap<String, String>();
		while (null != c) {
			params = getStringParams(params, obj, c);
			c = c.getSuperclass();
		}

		return params;
	}

	public static Map<String,String> getStringParams(Map<String,String> params,Object obj,Class c){
		
        Field[] fields = c.getDeclaredFields(); 
        for(Field fi:fields){
        	fi.setAccessible(true); 
        	if(fi.getName().equalsIgnoreCase("serialVersionUID")){
        		continue;
        	}
			try {
				Object value =  fi.get(obj);
				if(value!=null&&value!=""){
					params.put(fi.getName(), value.toString());
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
		return params;
	}

	public static Map buildList(List list) {
		// TODO Auto-generated method stub
		Map<String,String> params = new HashMap<String,String>();
		try {
			String json = JsonUtils.toJson(list);
			params.put("content", json);
		} catch ( IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return params;
	}
}

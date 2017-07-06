package com.channelsoft.cri.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;


public class DomainUtils {

	/**
	 * 采用域名方式请求,企业访问返回企业编号，否则返回空
	 * @param request
	 * @return
	 */
	public static String getEntId(HttpServletRequest request){
		String sName=request.getServerName();
		String entId="";
		//用域名方式
    	if(sName.indexOf(".")>0&&!StringUtils.isNumeric(sName.replace(".", ""))){
    		String firstName=sName.substring(0, sName.indexOf("."));
    		//平台访问
    		if("www".equalsIgnoreCase(firstName)){
    			
    		}
    		else{
    			entId=firstName.trim().toLowerCase();
    		}
    	}
    	if(StringUtils.isBlank(entId)){
    		entId=request.getParameter("entId");
    	}
    	return entId;
	}
}

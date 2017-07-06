/**
 * FileName: UrlUtils.java
 * @author weiss  
 */
package com.channelsoft.cri.util;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <dl>
 * <dt>UrlUtils</dt>
 * <dd>Description:</dd>
 * <dd>Copyright: Copyright (C) 2009</dd>
 * <dd>Company: 青牛（北京）技术有限公司</dd>
 * <dd>CreateDate: 2009-12-11</dd>
 * </dl>
 * 
 * @author weiss
 */
public class UrlUtils {
	public static String convertParameterMapToString(Map<String, String[]> params ) {
		StringBuffer _buffer = new StringBuffer();
		for (Map.Entry<String, String[]> e : params.entrySet()) {
				String[] v = e.getValue();
				if (v != null) {
					for (String kv : v) {
						_buffer.append("&");
						_buffer.append(e.getKey());
						_buffer.append('=');
						_buffer.append(kv);
					}
				}
		}
		return _buffer.toString();
	}
	
	public static StringBuffer getServerUrl(HttpServletRequest request) {
		StringBuffer b = new StringBuffer(1024);
		b.append("http://");
		b.append(request.getServerName());
		int port = request.getServerPort();
		if (port != 80) {
			b.append(":");
			b.append(port);
		}
		b.append(request.getContextPath());
		return b;
	}
	public static void setCookieValue(HttpServletResponse response,String cookieName,String cookieValue){
		Cookie c = new Cookie(cookieName,cookieValue);
		c.setPath("/");
		response.addCookie(c);
	}
	
	
	public static String getCookieValue(HttpServletRequest request,String cookieName){
		Cookie[] cookies = request.getCookies();
		if(cookies == null) return "";
		
		for(Cookie c : cookies){
			if(c.getName().equalsIgnoreCase(cookieName)){
				return c.getValue();				
			}
		}
		return "";
	}
}

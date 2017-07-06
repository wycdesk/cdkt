package com.channelsoft.ems.sso.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SsoCookieUtils {
	/**
	 * COOKIE参数名
	 */
	public static final String CLIENT_COOKIE_ENT_ID = "COOKIE_ENT_ID";
	
	public static final String CLIENT_COOKIE_LOGIN_NAME = "COOKIE_LOGIN_NAME";

	public static final String CLIENT_COOKIE_PASSWORD = "COOKIE_PASSWORD";

	public static void saveCookie(String entId, String loginName, String password, HttpServletRequest request, HttpServletResponse response) {
		String host = request.getServerName();
		int age = 60 * 60 * 24 * 30; // 30天的有效期
		
		Cookie entIdCookie = new Cookie(CLIENT_COOKIE_ENT_ID, entId); // 保存企业ID到Cookie  
		entIdCookie.setPath("/");
		entIdCookie.setDomain(host);
		entIdCookie.setMaxAge(age);
		response.addCookie(entIdCookie);
		
		Cookie loginNameCookie = new Cookie(CLIENT_COOKIE_LOGIN_NAME, loginName); // 保存用户名到Cookie  
		loginNameCookie.setPath("/");
		loginNameCookie.setDomain(host);
		loginNameCookie.setMaxAge(age);
		response.addCookie(loginNameCookie);
		
		Cookie passwordCookie = new Cookie(CLIENT_COOKIE_PASSWORD, password); // 保存密码到Cookie  
		passwordCookie.setPath("/");
		passwordCookie.setDomain(host);
		passwordCookie.setMaxAge(age);
		response.addCookie(passwordCookie);
	}
	
	public static String getCookieEntId(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {  
				if (CLIENT_COOKIE_ENT_ID.equals(cookie.getName())) {  
			    	return cookie.getValue(); // 得到cookie的用户名  
				}
			}
		}
		return null;
	}

	public static String getCookieLoginName(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {  
				if (CLIENT_COOKIE_LOGIN_NAME.equals(cookie.getName())) {  
			    	return cookie.getValue(); // 得到cookie的用户名  
				}
			}
		}
		return null;
	}
	
	public static String getCookiePassword(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (CLIENT_COOKIE_PASSWORD.equals(cookie.getName())) {
			    	return cookie.getValue(); // 得到cookie的密码  
				}
			}
		}
		return null;
	}
	
	public static void deleteCookies(HttpServletRequest request, HttpServletResponse response) {
		String host = request.getServerName();
		
		Cookie entIdCookie = new Cookie(CLIENT_COOKIE_ENT_ID, null); // 删除企业ID Cookie  
		entIdCookie.setPath("/");
		entIdCookie.setDomain(host);
		entIdCookie.setMaxAge(0);
		response.addCookie(entIdCookie);
		
		Cookie loginNameCookie = new Cookie(CLIENT_COOKIE_LOGIN_NAME, null); // 删除用户名Cookie  
		loginNameCookie.setPath("/");
		loginNameCookie.setDomain(host);
		loginNameCookie.setMaxAge(0);
		response.addCookie(loginNameCookie);
		
		Cookie passwordCookie = new Cookie(CLIENT_COOKIE_PASSWORD, null); // 删除密码Cookie  
		passwordCookie.setPath("/");
		passwordCookie.setDomain(host);
		passwordCookie.setMaxAge(0);
		response.addCookie(passwordCookie);
	}
}

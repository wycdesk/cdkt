package com.channelsoft.cri.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
/**
 * IP地址工具类
 * <dl>
 * <dt>portal</dt>
 * <dd>Description:</dd>
 * <dd>Copyright: Copyright (C) 2013</dd>
 * <dd>Company: 青牛(北京)技术有限公司 成都研究所</dd>
 * <dd>CreateDate: 2013-4-26</dd>
 * </dl>
 * @author 肖峰
 */
public class IpUtils {
	
	/**
	 * 将IP地址转换正long型
	 * @param ip
	 * @return
	 * @CreateDate: 2013-4-26 下午01:17:41
	 * @author 魏铭
	 */
	public static long ip2Long(String ip) {
		String[] a = ip.split("\\.");
		
		if (a.length == 4) {
			return (Long.parseLong(a[0]) << 24) + (Long.parseLong(a[1]) << 16)
					+ (Long.parseLong(a[2]) << 8) + (Long.parseLong(a[3]));
		}
		return 0;
	}
	
	/**
	 * 验证到指定ip的网络是否通畅
	 * @param ip
	 * @return
	 * @CreateDate: 2013-4-26 下午01:14:36
	 * @author 肖峰
	 */
	public static boolean ipRedirectTest(String ip) {
		GetMethod method = new GetMethod(ip);
		HttpClient httpClient = new HttpClient();
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(3000);
		try {
			int status = httpClient.executeMethod(method);
			if(status == 200){
				return true;
			}
		} catch (HttpException e) {
		} catch (IOException e) {
		}
		return false;
	}
	
	/**
	 * 获取客户端的IP地址
	 * @param request
	 * @return
	 * @CreateDate: 2012-12-13 上午09:21:09
	 * @author 魏铭
	 */
	public static String getClientIp(HttpServletRequest request)
	{
		String ip = "";
		if(request.getHeader("x-forwarded-for") == null){
			ip = request.getRemoteAddr();
		}else{
			ip = request.getHeader("x-forwarded-for");
		}
		return ip;
	}
	/**
	 * 获取客户端端口
	 * @param request
	 * @return
	 * @CreateDate: 2013-4-27 下午04:02:59
	 * @author 魏铭
	 */
	public static String getClientPort(HttpServletRequest request)
	{
		return String.valueOf(request.getRemotePort());
	}
	/**
	 * 获取客户端Ip:Port
	 * @param request
	 * @return
	 * @CreateDate: 2013-4-27 下午04:03:08
	 * @author 魏铭
	 */
	public static String getClientIpPort(HttpServletRequest request)
	{
		return getClientIp(request) + ":" + getClientPort(request);
	}
	/**
	 * 获取获取服务端IP
	 * @param request
	 * @return
	 * @CreateDate: 2013-4-27 下午04:03:08
	 * @author 魏铭
	 */
	public static String getServerIp(HttpServletRequest request)
	{
		return request.getLocalAddr();
	}
	/**
	 * 获取服务端端口
	 * @param request
	 * @return
	 * @CreateDate: 2013-4-27 下午04:03:37
	 * @author 魏铭
	 */
	public static String getServerPort(HttpServletRequest request)
	{
		return "" + request.getLocalPort();
	}
	/**
	 * 获取服务端Ip:Port
	 * @param request
	 * @return
	 * @CreateDate: 2013-4-27 下午04:03:54
	 * @author 魏铭
	 */
	public static String getServerIpPort(HttpServletRequest request)
	{
		return getServerIp(request) + ":" + getServerPort(request);
	}
	
	  /*   获取当前网络ip  */  
    public static String getIpAddr(HttpServletRequest request){  
        String ipAddress = request.getHeader("x-forwarded-for");  
            if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {  
                ipAddress = request.getHeader("Proxy-Client-IP");  
            }  
            if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {  
                ipAddress = request.getHeader("WL-Proxy-Client-IP");  
            }  
            if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {  
                ipAddress = request.getRemoteAddr();  
                if(ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")){  
                    //根据网卡取本机配置的IP  
                    InetAddress inet=null;  
                    try {  
                        inet = InetAddress.getLocalHost();  
                    } catch (UnknownHostException e) {  
                        e.printStackTrace();  
                    }  
                    ipAddress= inet.getHostAddress();  
                }  
            }  
            //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割  
            if(ipAddress!=null && ipAddress.length()>15){ //"***.***.***.***".length() = 15  
                if(ipAddress.indexOf(",")>0){  
                    ipAddress = ipAddress.substring(0,ipAddress.indexOf(","));  
                }  
            }  
            return ipAddress;   
    } 
}

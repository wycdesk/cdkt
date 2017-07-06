package com.channelsoft.cri.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式验证邮箱格式
 * @author wangjie
 * @time 2015年11月21日下午1:06:05
 */
public class EMailCheckUtil {

	/**
	 * 验证邮箱格式
	 * 正确返回true，错误返回false
	 * @param email
	 * @return
	 * @author wangjie
	 * @time 2015年11月21日下午1:07:03
	 */
	public static boolean check(String email){
		boolean flag = false;
		try{
			String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(email);
			flag = matcher.matches();
		}catch(Exception e){
			flag = false;
		}
		return flag;
	}
}

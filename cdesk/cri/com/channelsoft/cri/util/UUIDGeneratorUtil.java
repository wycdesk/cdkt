package com.channelsoft.cri.util;

import java.util.UUID;

/**
 * UUID(Universally Unique Identifier) 全局唯一标识符,是指在一台机器上生成的数字，
 * 它保证对在同一时空中的所有机器都是唯一的。 UUID是由一个十六位的数字组成,表现出来的形式例如
 * 550E8400-E29B-11D4-A716-446655440000 本方法是用于JDK1.5以上版本
 * 
 * @author 刘江宁
 * 
 */
public class UUIDGeneratorUtil {
	public UUIDGeneratorUtil() {
	}

	/**
	 * 获得一个UUID
	 * 
	 * @return String UUID
	 */
	public static String getUUID() {
		String s = UUID.randomUUID().toString();
		// 去掉“-”符号
		// return
		// s.substring(0,8)+s.substring(9,13)+s.substring(14,18)+s.substring(19,23)+s.substring(24);
		return s.replaceAll("-", "");
	}

	/**
	 * 获得指定数目的UUID
	 * 
	 * @param number
	 *            int 需要获得的UUID数量
	 * @return String[] UUID数组
	 */
	public static String[] getUUID(int number) {
		if (number < 1) {
			return null;
		}
		String[] ss = new String[number];
		for (int i = 0; i < number; i++) {
			ss[i] = getUUID();
		}
		return ss;
	}

	public static void main(String[] args) {
		String[] ss = getUUID(10);
		for (int i = 0; i < ss.length; i++) {
			System.out.println(ss[i]);
		}
	}

}

package com.channelsoft.ems.sso.constant;

public class SsoParamConstants {

	public static final String BASE_DOMAIN_CODE = "0";
	/**
	 * 统一管理门户登录页面，启动时读入
	 */
	public static String PORTAL_LOGIN_INDEX = "";

	/**
	 * 租户登陆页面
	 */
	public static String ENT_LOGIN_INDEX = "";

	/**
	 * /** SSO登录接口地址，可为内网地址或本机
	 */
	public static String SSO_LOGIN_URL = "";
	
	public static String SSO_AUTOLOGIN_FILTER_URL = "";
	/**
	 * SSO平台切换接口地址，可为内网地址或本机
	 */
	public static String SSO_PLATFORMSWITCH_URL = "";
	/**
	 * SSO登陆地址，可为内网地址或本机
	 */
	public static String SSO_LOGOUT_URL = "";
	/**
	 * 更新用户的unitName接口地址
	 */
	public static String SSO_UNIT_NAME_URL = "";
	/**
	 * 子系统平台标识，启动时读入
	 */
	public static String PLATFORM_ID = "";
	/**
	 * 忽略鉴权的地址列表
	 */
	public static String IGNORE_PRIVILEGE_URL = "";
	/**
	 * 忽略鉴权的后缀列表
	 */
	public static String IGNORE_SUFFIX_URL = "css,gif,png,jpg,jpeg,js,swf,txt,ico";
	/**
	 * 重定向脚本
	 */
	public static String REDIRECT_SCRIPT = "";

	/**
	 * SSO登陆，查询用户的信息
	 */
	public static String SSO_LOGIN_USERINFO;
	/**
	 * SSO登陆，查询企业信息
	 */
	public static String SSO_LOGIN_ENT;
	/**
	 * SSO登陆，查询菜单
	 */
	public static String SSO_LOGIN_MENULIST;
	/**
	 * SSO登陆，查询权限
	 */
	public static String SSO_LOGIN_PRIVILEGELIST;
	/**
	 * SSO登陆，查询地域列表
	 */
	public static String SSO_LOGIN_AREA;

	/**
	 * SSO登陆，查询全部管理域列表
	 */
	public static String SSO_LOGIN_ALLDOMAIN;
	/**
	 * SSO登陆，查询字管理域列表
	 */
	public static String SSO_LOGIN_DOMAIN;

	/**
	 * SSO登陆，查询角色列表
	 */
	public static String SSO_LOGIN_ROLE;
	/**
	 * SSO登陆，查询部门列表
	 */
	public static String SSO_LOGIN_DEPARTMENT;

}

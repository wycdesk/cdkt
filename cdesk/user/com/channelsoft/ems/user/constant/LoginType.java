package com.channelsoft.ems.user.constant;

import org.apache.commons.lang.StringUtils;


public enum LoginType {
	MAILBOX("0","邮箱"),TELEPHONE("1","电话号码"),WECHAT("2","微信"),
	QQ("3","扣扣"),MICROBLOG("4","微博"),IM("5","IM"),VIDEO("6","视频"),USER_DEFINE("8","用户输入"),RANDOM("9","系统生成"),ELSE("","未知"),;
	public String value;
	public String desc;
	private LoginType(String value,String desc){
		this.value=value;
		this.desc=desc;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public static LoginType getEnum(String value){
		if (value!=null)
		for(LoginType e:values()){
			if(StringUtils.equalsIgnoreCase(e.value, value)){
				return e;
			}
		}
		return ELSE;
	}
	
}

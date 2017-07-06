package com.channelsoft.ems.api.constants;



import org.apache.commons.lang.StringUtils;


/**
 * 邮箱处理类型
 * @author wupeng
 * @CreateDate：2015年11月12日下午2:40:56
 */
public enum MailDealType {
	/**
	 * 不含其它的业务逻辑,单纯发送邮件
	 */
	NORMAL("0", "邮件一般发送"),
	/**
	 * 企业注册成功后，发送邮件
	 * 包含了域名注册和自动生成企业支持邮箱(生成的企业支持邮箱都是support用户，不用设置自动转发)
	 */
	ENT_REGISTER("1", "企业注册"),
	/**
	 * 未知类型
	 */
	ELSE("","未知类型");
	public String value;
	public String desc;
	MailDealType(String value, String desc){
		this.value = value;
		this.desc = desc;
	}
	public static MailDealType getEnum(String value){
		if (value!=null)
		for(MailDealType e:values()){
			if(StringUtils.equalsIgnoreCase(e.value, value)){
				return e;
			}
		}
		return ELSE;
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
}

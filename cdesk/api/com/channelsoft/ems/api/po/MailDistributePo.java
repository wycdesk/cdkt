package com.channelsoft.ems.api.po;

public class MailDistributePo {
	
	//目标邮箱地址（邮件即将发往的邮箱地址）
	private String targetAddr = "";
	//原始发件人邮箱地址
	private String sendAddr = "";
	//原始发件人密码
	private String senderPw = "";
	//邮件标题
	private String title = "";
	//邮件内容
	private String content = "";
	//邮件处理类型（1：企业注册;0: 一般的发送接口）
	private String dealType = "";
	//企业ID
	private String entId = "";
	//工单ID
	private String wfId = "";
	//邮件回复人
	private String replyTo = "";
	
//	//返回值（0：成功，1：失败）
//	private String result = "";
//	//返回值描述
//	private String desc = "";
	/**
	 * @param sendAddr 发送者
	 * @param targetAddr 接收者
	 * @param title 邮件标题
	 * @param content 邮件内容
	 */
	public MailDistributePo(String sendAddr,String targetAddr,String title,String content){
		this.sendAddr=sendAddr;
		this.targetAddr = targetAddr;
		this.title = title;
		this.content = content;
	}
	public MailDistributePo(){
		
	}
	public String getTargetAddr() {
		return targetAddr;
	}
	public void setTargetAddr(String targetAddr) {
		this.targetAddr = targetAddr;
	}
	public String getSendAddr() {
		return sendAddr;
	}
	public void setSendAddr(String sendAddr) {
		this.sendAddr = sendAddr;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getDealType() {
		return dealType;
	}
	public void setDealType(String dealType) {
		this.dealType = dealType;
	}
	public String getEntId() {
		return entId;
	}
	public void setEntId(String entId) {
		this.entId = entId;
	}
	public String getWfId() {
		return wfId;
	}
	public void setWfId(String wfId) {
		this.wfId = wfId;
	}
	public String getSenderPw() {
		return senderPw;
	}
	public void setSenderPw(String senderPw) {
		this.senderPw = senderPw;
	}
	public String getReplyTo() {
		return replyTo;
	}
	public void setReplyTo(String replyTo) {
		this.replyTo = replyTo;
	}
	
}

package com.channelsoft.ems.api.po;

public class CommentPo {

	private String title;      //标题
	
	private String textDesc;  //内容
	
	private String attachment;   //附件
	
	private String language;     //语言
	 
	private String Sender;         //发送者
	
	private String Accepter;       //接收者

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTextDesc() {
		return textDesc;
	}

	public void setTextDesc(String textDesc) {
		this.textDesc = textDesc;
	}

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}


	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getSender() {
		return Sender;
	}

	public void setSender(String sender) {
		Sender = sender;
	}

	public String getAccepter() {
		return Accepter;
	}

	public void setAccepter(String accepter) {
		Accepter = accepter;
	}
	
	
}

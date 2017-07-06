package com.channelsoft.ems.api.po;

public class CchatResponsePo {

	private String code;
	private String msg;
	private Object obj;
	
	public CchatResponsePo(){
		
	}
	public CchatResponsePo(String code, String msg, Object obj) {
		super();
		this.code = code;
		this.msg = msg;
		this.obj = obj;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Object getObj() {
		return obj;
	}
	public void setObj(Object obj) {
		this.obj = obj;
	}
	
	
}

package com.channelsoft.cri.vo;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
/**
 * json 接口成功返回对象
  *<dl>
  *<dt>类名：AjaxResultPo</dt>
  *<dd>描述: </dd>
  *<dd>公司: 青牛（北京）技术有限公司</dd>
  *<dd>创建时间：2010-7-23  下午01:48:13</dd>
  *<dd>创建人： 韦水生 </dd>
  *</dl>
 */
public class AjaxResultPo implements Serializable {
	
	private static final long serialVersionUID = -4148768233386711389L;
	private boolean success;
	private String msg;
	private long total = -1;
	private Object rows;
	/**
	 * 消息串的最大长度
	 */
	public static Integer maxMassgeLength = 100;
	
	/**
	 * 返回一个失败的消息对象
	 */
	public static AjaxResultPo failed(Exception e) {
		return new AjaxResultPo(false, e.getMessage());
	}
	/**
	 * 返回一个成功的消息对象
	 * @param message
	 * @param data
	 * @return
	 * @CreateDate: 2013年11月7日 下午5:20:31
	 * @author 魏铭
	 */
	public static AjaxResultPo success(String message, long total, Object rows){
		return new AjaxResultPo(true, message, total, rows);
	}
	
	/**
	 * 缺省的成功消息对象
	 * @param data
	 * @return
	 * @CreateDate: 2013年11月7日 下午5:20:20
	 * @author 魏铭
	 */
	public static AjaxResultPo successDefault (){
		return new AjaxResultPo(true, "操作成功", 0, null);
	}


	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public Object getRows() {
		return rows;
	}
	public void setRows(Object rows) {
		this.rows = rows;
	}

	public AjaxResultPo(boolean b){
		setSuccess(b);
		setMsg("");
	}
	
	public AjaxResultPo(boolean success, String meassage) {
		super();
		this.success = success;
		this.msg = meassage;
	}
	
	public AjaxResultPo(boolean success, Object rows) {
		super();
		this.success = success;
		this.rows = rows;
	}
	public AjaxResultPo(boolean b, String msg, long total, Object rows){
		setSuccess(b);
		setMsg(msg);
		setTotal(total);
		setRows(rows);
	}
	
	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return StringUtils.substring(msg, 0, maxMassgeLength);
	}
	
	public String getMessage() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}

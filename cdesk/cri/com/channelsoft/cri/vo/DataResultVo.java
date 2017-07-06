package com.channelsoft.cri.vo;

import java.io.Serializable;
/**
 * 页面查询请求返回对象
 * <dl>
 * <dt>portal</dt>
 * <dd>Description:</dd>
 * <dd>Copyright: Copyright (C) 2013</dd>
 * <dd>Company: 青牛(北京)技术有限公司 成都研究所</dd>
 * <dd>CreateDate: 2013-7-26</dd>
 * </dl>
 * @author 魏铭
 */
public class DataResultVo implements Serializable{
	private static final long serialVersionUID = -3379147257223919323L;
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
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	private long total = -1;
	private Object rows;
	private String message;
}

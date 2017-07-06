package com.channelsoft.cri.logger.vo;

public class DBLogVo extends BaseLogVo {
	private static final long serialVersionUID = -5157387637353237240L;
	private String sql;
	private String type;
	public void setSql(String sql) {
		this.sql = sql;
	}
	public String getSql() {
		return sql;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getType() {
		return type;
	}
}

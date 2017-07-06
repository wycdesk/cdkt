package com.channelsoft.cri.logger.vo;

public class ManageLogVo extends BaseLogVo{
	private static final long serialVersionUID = -4398549799618902771L;
	private String business;
	private String objective;
	private String remark;
	public String getBusiness() {
		return business;
	}
	public void setBusiness(String business) {
		this.business = business;
	}
	public String getObjective() {
		return objective;
	}
	public void setObjective(String objective) {
		this.objective = objective;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}

package com.channelsoft.ems.api.po;

import java.util.Date;


public class WorkInfoPo {
	
	//调用创建接口必填字段,由外部系统提供
    private String title;    //工单标题
	
	private String content;  //工单内容
	
	private String serviceGroupId;    //受理客服组id，如果企业只有一个客服组和一个客服，则填写；有多个则为空等待分配
	
	private String serviceGroupName;	//受理客服组名称
	
	private String customServiceId;   //受理客服id
	
	private String customServiceName;   //受理客服名称
	
	private String copyFor;	//抄送副本
	
	private String status;     //工单状态，当客服组id和客服id不为空，工单状态为受理中，如果没有则为未分配
	
	private String creatorId;  //工单发起人id

	private String creatorEmail;  	//发件人邮箱
	
	private String source;       //工单来源
	
	private String organizationId;  //关联公司组织id

	//工单创建必填字段
	private String workId;         //工单编号
	
	private String type;           //工单类型
	
	private String language;    //使用语言
	
	private String priority;     //工单优先级
	
	private int reminder;        //催单次数

	private String customStatisfied; //客户满意度
	
	
	
    private Date createDate; //创建时间
	
	private Date acceptDate; //受理时间
	
	private String acceptState; //受理状态
	
	private Date creatorUpdateDate;  //工单发起人更新时间
	
	private Date customUpdateDate;    //客服更新时间
	
	private Date resolveDate;            //问题解决时间
	
	private Date endTime;                //任务到期时间
	
	private Date usageTime;              //使用时间
	    
	private Replay rencentReplay;         //最近回复
	
	//下期实现字段
	private String starLevel;  //工单星级别
	        	
	private String label;  //工单标签
	


	public String getCustomStatisfied() {
		return customStatisfied;
	}

	public void setCustomStatisfied(String customStatisfied) {
		this.customStatisfied = customStatisfied;
	}

	public int getReminder() {
		return reminder;
	}

	public void setReminder(int reminder) {
		this.reminder = reminder;
	}

	public Replay getRencentReplay() {
		return rencentReplay;
	}

	public void setRencentReplay(Replay rencentReplay) {
		this.rencentReplay = rencentReplay;
	}

	public String getStarLevel() {
		return starLevel;
	}

	public void setStarLevel(String starLevel) {
		this.starLevel = starLevel;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}


	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}


	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Date getAcceptDate() {
		return acceptDate;
	}

	public void setAcceptDate(Date acceptDate) {
		this.acceptDate = acceptDate;
	}


	public Date getCustomUpdateDate() {
		return customUpdateDate;
	}

	public void setCustomUpdateDate(Date customUpdateDate) {
		this.customUpdateDate = customUpdateDate;
	}

	public Date getResolveDate() {
		return resolveDate;
	}

	public void setResolveDate(Date resolveDate) {
		this.resolveDate = resolveDate;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getUsageTime() {
		return usageTime;
	}

	public void setUsageTime(Date usageTime) {
		this.usageTime = usageTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getServiceGroupId() {
		return serviceGroupId;
	}

	public void setServiceGroupId(String serviceGroupId) {
		this.serviceGroupId = serviceGroupId;
	}

	public String getServiceGroupName() {
		return serviceGroupName;
	}

	public void setServiceGroupName(String serviceGroupName) {
		this.serviceGroupName = serviceGroupName;
	}

	public String getCustomServiceId() {
		return customServiceId;
	}

	public void setCustomServiceId(String customServiceId) {
		this.customServiceId = customServiceId;
	}

	public String getCustomServiceName() {
		return customServiceName;
	}

	public void setCustomServiceName(String customServiceName) {
		this.customServiceName = customServiceName;
	}

	public String getCopyFor() {
		return copyFor;
	}

	public void setCopyFor(String copyFor) {
		this.copyFor = copyFor;
	}

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public String getCreatorEmail() {
		return creatorEmail;
	}

	public void setCreatorEmail(String creatorEmail) {
		this.creatorEmail = creatorEmail;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public String getWorkId() {
		return workId;
	}

	public void setWorkId(String workId) {
		this.workId = workId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getAcceptState() {
		return acceptState;
	}

	public void setAcceptState(String acceptState) {
		this.acceptState = acceptState;
	}

	public Date getCreatorUpdateDate() {
		return creatorUpdateDate;
	}

	public void setCreatorUpdateDate(Date creatorUpdateDate) {
		this.creatorUpdateDate = creatorUpdateDate;
	}
}

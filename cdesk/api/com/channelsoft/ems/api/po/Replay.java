package com.channelsoft.ems.api.po;

import java.util.Date;

public class Replay extends CommentPo{

	private Date replayDate;       //工单回复日期
	
	private String replayUserId; //工单回复用户id


	public Date getReplayDate() {
		return replayDate;
	}

	public void setReplayDate(Date replayDate) {
		this.replayDate = replayDate;
	}

	public String getReplayUserId() {
		return replayUserId;
	}

	public void setReplayUserId(String replayUserId) {
		this.replayUserId = replayUserId;
	}

	
	
	
}

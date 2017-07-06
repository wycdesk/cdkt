package com.channelsoft.ems.help.constant;

public class PublishDocPo {

	private String docId;
	private String title;
	private String content;
	private String attachment;
	private String author;
	private String partition;
	private String createTime;
	private String updateTime;
	private int check;
	private int comment;
	private int vote;
	private String creatorId;
	
	public PublishDocPo(){
		this.title="";
		this.content="";
		this.attachment="";
		this.author="";
		this.partition="";
		this.createTime="";
		this.updateTime="";
		this.check=0;
		this.comment=0;
		this.vote=0;
		this.docId="0";
		this.creatorId="";
	}
	public PublishDocPo(String title,String content,String attachment,String author,String partition
			,String createTime,String updateTime,int check,int comment,int vote){
		this.title=title;
		this.content=content;
		this.attachment=attachment;
		this.author=author;
		this.partition=partition;
		this.createTime=createTime;
		this.updateTime=updateTime;
		this.check=check;
		this.comment=comment;
		this.vote=vote;
	}
	public String getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}
	public String getDocId() {
		return docId;
	}
	public void setDocId(String docId) {
		this.docId = docId;
	}
	public int getCheck() {
		return check;
	}
	public void setCheck(int check) {
		this.check = check;
	}
	public int getComment() {
		return comment;
	}
	public void setComment(int comment) {
		this.comment = comment;
	}
	public int getVote() {
		return vote;
	}
	public void setVote(int vote) {
		this.vote = vote;
	}
	
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getPartition() {
		return partition;
	}
	public void setPartition(String partition) {
		this.partition = partition;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
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
	public String getAttachment() {
		return attachment;
	}
	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}
	
}

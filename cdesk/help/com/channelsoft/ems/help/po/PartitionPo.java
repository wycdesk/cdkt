package com.channelsoft.ems.help.po;

import java.util.List;

public class PartitionPo {

	private String id;
	private String title;
	private String description;
	private String showNum;
	private List<PartitionSubPo> subClass;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getShowNum() {
		return showNum;
	}
	public void setShowNum(String showNum) {
		this.showNum = showNum;
	}
	public List<PartitionSubPo> getSubClass() {
		return subClass;
	}
	public void setSubClass(List<PartitionSubPo> subClass) {
		this.subClass = subClass;
	}
	
}

package com.channelsoft.ems.privilege.po;

import java.io.Serializable;
/**
 * 角色信息对象。
 * <dl>
 * <dt>portal</dt>
 * <dd>Description:</dd>
 * <dd>Copyright: Copyright (C) 2013</dd>
 * <dd>Company: 青牛(北京)技术有限公司 成都研究所</dd>
 * <dd>CreateDate: 2013-6-7</dd>
 * </dl>
 * @author 魏铭
 */
public class CfgRolePo implements Serializable{
	private static final long serialVersionUID = -1542389850982726244L;
	private String id;
	private String name;
	private String description;
	private String parentId;
	private String isCustom;
	private String createTime; //创建时间
	private String updateTime; //更新时间
	/**
	 * 用于treeGrid显示节点
	 * @return
	 */
	public String getText(){
		return this.name;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getIsCustom() {
		return isCustom;
	}

	public void setIsCustom(String isCustom) {
		this.isCustom = isCustom;
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
}

package com.channelsoft.ems.privilege.po;

import java.io.Serializable;
/**
 * 权限对象数据结构，对应数据库字段。
 * <dl>
 * <dt>portal</dt>
 * <dd>Description:</dd>
 * <dd>Copyright: Copyright (C) 2013</dd>
 * <dd>Company: 青牛(北京)技术有限公司 成都研究所</dd>
 * <dd>CreateDate: 2013-6-7</dd>
 * </dl>
 * @author 魏铭
 */
public class CfgPermissionPo implements Serializable{
	private static final long serialVersionUID = 9087195601417086073L;
	private String id; //主键
	private String name; //菜单/权限名
	private String platformId; //平台标识
	private String parentId; //父节点ID
	private String type; //类型：对应PrivilegeType
	private String visible; //是否可见：对应VisibleFlag
	private String actionUrl; //菜单相对路径
	private String parameter; //参数，拼地址时用。格式为a=a&b=b
	private String description;//描述，备注
	private String isTop; //一级权限是否靠上排
	private String groupId; //二级权限分组
	private String isAjax; //二级权限是否是ajax请求
	private String sortWeight; //排序
	private int sortWeightInt; //排序权重值，在service层由sortWeight转化传给DAO层
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
	public String getPlatformId() {
		return platformId;
	}
	public void setPlatformId(String platformId) {
		this.platformId = platformId;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getVisible() {
		return visible;
	}
	public void setVisible(String visible) {
		this.visible = visible;
	}
	public String getActionUrl() {
		return actionUrl;
	}
	public void setActionUrl(String actionUrl) {
		this.actionUrl = actionUrl;
	}
	public String getParameter() {
		return parameter;
	}
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getIsTop() {
		return isTop;
	}
	public void setIsTop(String isTop) {
		this.isTop = isTop;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getIsAjax() {
		return isAjax;
	}
	public void setIsAjax(String isAjax) {
		this.isAjax = isAjax;
	}
	public String getSortWeight() {
		return sortWeight;
	}
	public void setSortWeight(String sortWeight) {
		this.sortWeight = sortWeight;
	}
	public int getSortWeightInt() {
		return sortWeightInt;
	}
	public void setSortWeightInt(int sortWeightInt) {
		this.sortWeightInt = sortWeightInt;
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

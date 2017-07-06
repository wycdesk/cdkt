package com.channelsoft.ems.privilege.vo;

import org.apache.commons.lang.StringUtils;

import com.channelsoft.cri.vo.BaseTreeVo;
import com.channelsoft.ems.privilege.constant.PermissionType;
import com.channelsoft.ems.privilege.constant.VisibleFlag;
/**
 * 权限对象VO，树状结构，继承BaseTreeVo，直接返回可形成树状列表
 * <dl>
 * <dt>portal</dt>
 * <dd>Description:</dd>
 * <dd>Copyright: Copyright (C) 2013</dd>
 * <dd>Company: 青牛(北京)技术有限公司 成都研究所</dd>
 * <dd>CreateDate: 2013-7-9</dd>
 * </dl>
 * @author 魏铭
 */
public class CfgPermissionVo extends BaseTreeVo {
	private static final long serialVersionUID = -7376583326132968955L;
	private String platformId;
	private String rootUrl;
	private String actionUrl;
	private String parameter;
	private String visible;
	private String isTop;
	private String groupId;
	private String isAjax;
	private String sortWeight;
	private String type;
	private boolean check;
	private String parentName;
	
	/**
	 * 添加了sessionKey的url，根据getPermissionUrl进行封装
	 */
	private String url;
	public String getPlatformId() {
		return platformId;
	}
	public void setPlatformId(String platformId) {
		this.platformId = platformId;
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
	public String getVisible() {
		return visible;
	}
	public void setVisible(String visible) {
		this.visible = visible;
	}
	public String getSortWeight() {
		return sortWeight;
	}
	public void setSortWeight(String sortWeight) {
		this.sortWeight = sortWeight;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUrl() {
		return url;
	}
	/**
	 * 重写setHasChildren方法，不同的树这里逻辑可能各不相同
	 * @see com.channelsoft.cri.vo.BaseTreeVo#setHasChildren(boolean)
	 */
	@Override
	public void setHasChildren(boolean hasChildren) {
		super.setHasChildren(hasChildren);
		if (hasChildren){ 
			super.setState("closed");
	    } else{
	    	super.setState("open");
	    }
	}
	
	public String getVisibleStr()
	{
		return VisibleFlag.getEnum(this.getVisible()).desc;
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
	public String getTypeStr()
	{
		return PermissionType.getEnum(this.getType()).desc;
	}
	public void setRootUrl(String rootUrl) {
		this.rootUrl = rootUrl;
	}
	public String getRootUrl() {
		return rootUrl;
	}
	/**
	 * 实际地址，根据actionUrl和rootUrl进行拼装
	 * @return
	 * @CreateDate: 2013-7-9 下午05:00:49
	 * @author 魏铭
	 */
	public String getPermissionUrl() {
		if(StringUtils.isNotBlank(rootUrl)){
			if(StringUtils.isBlank(this.getActionUrl())){
				return rootUrl;
			}
			return rootUrl + this.getActionUrl();
		}else{
			if(StringUtils.isBlank(this.getActionUrl())){
				return "";
			}
			return this.getActionUrl();
		}
	}
	public boolean isCheck() {
		return check;
	}
	public void setCheck(boolean check) {
		this.check = check;
	}
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
}

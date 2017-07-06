package com.channelsoft.ems.privilege.vo;


import com.channelsoft.cri.vo.BaseTreeVo;
/**
 * 角色对象VO，树状结构，继承BaseTreeVo，直接返回可形成树状列表
 * <dl>
 * <dt>portal</dt>
 * <dd>Description:</dd>
 * <dd>Copyright: Copyright (C) 2013</dd>
 * <dd>Company: 青牛(北京)技术有限公司 成都研究所</dd>
 * <dd>CreateDate: 2013-7-9</dd>
 * </dl>
 * @author 魏铭
 */
public class CfgRoleVo extends BaseTreeVo {
	private static final long serialVersionUID = -7376583326132968955L;
	private String parentName;
	private String isCustom;
	private int userCount;
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
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	public String getIsCustom() {
		return isCustom;
	}
	public void setIsCustom(String isCustom) {
		this.isCustom = isCustom;
	}
	public int getUserCount() {
		return userCount;
	}
	public void setUserCount(int userCount) {
		this.userCount = userCount;
	}
	public String getUserCountStr() {
		return String.valueOf(userCount);
	}
}

package com.channelsoft.cri.cache.constant;
/**
 * 节点类型枚举类，提供getEnum方法
 * <dl>
 * <dt>portal</dt>
 * <dd>Description:</dd>
 * <dd>Copyright: Copyright (C) 2013</dd>
 * <dd>Company: 青牛(北京)技术有限公司 成都研究所</dd>
 * <dd>CreateDate: 2013-7-18</dd>
 * </dl>
 * @author 魏铭
 */
public enum CacheStatus {
	NO_DATA("0", "无数据"),
	NORMAL("1", "正常"),
	LOCKED("2", "锁定");
	public String value;
	public String desc;
	private CacheStatus(String value, String desc){
		this.value = value;
		this.desc = desc;
	}
	
	public static CacheStatus getEnum(String value){
		if (value == null) return NO_DATA;
		for(CacheStatus type: CacheStatus.values()) {
			if (value.equals(type.value)){
				return type;
			}
		}
		return NO_DATA;
	}
}

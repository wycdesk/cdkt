package com.channelsoft.cri.util;

public enum ColletionName {
	//所有企业共用的集合
	WORKSEQ("workId","所有企业工单自增序列"),
	WORKFIELD("workfield","所有企业工单自定义字段"),
	//每个企业拥有一个集合
	WORKORDER("work","工单信息"),
	WORKQUERY("workquery","工单查看分类"),
	WORKREPLY("workreply","工单回复"),
	ATTACHMENT("attachment","附件"),
	COMMUNICATE("communicate_history","联络历史"),
	ELSE("","");
	
	public String key;
	public String desc;
	
	ColletionName(String key, String desc) {
		this.key = key;
		this.desc = desc;
	}
	
	public static ColletionName getEnum(String key){
    	if(key!=null){
    		for(ColletionName e:values()){
    			if(e.key.equalsIgnoreCase(key)){
    				return e;
    			}
    		}
    	}
    	return ELSE;
    }
}

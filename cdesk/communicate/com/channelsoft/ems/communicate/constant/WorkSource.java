package com.channelsoft.ems.communicate.constant;

public enum WorkSource {
	    WEBPAGE("0","网页表单"),
	    WEBCHAT("1","IM"),
	    API("2","API接口"),
	    EMAIL("3","邮件"),
	    PHONE("4","手机端"),
	    PHONEIN("5","电话呼入"),
	    PHONEOUT("6","电话呼出"),
	    WECHAT("7","微信"),
	    VIDEO("8","视频"),
	    ELSE("","");
	    
	    public String key;
	    public String desc;
	    
	    WorkSource(String key,String desc){
	    	this.key=key;
	    	this.desc=desc;
	    }
	    
	    public static WorkSource getEnum(String key){
	    	
	    	if(key!=null){
	    		for(WorkSource e:values()){
	    			if(e.key.equalsIgnoreCase(key)){
	    				return e;
	    			}
	    		}
	    	}
	    	return ELSE;
	    }
}

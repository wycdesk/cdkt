package com.channelsoft.ems.communicate.constant;

public enum IMSource {

	WEBCHAT("webim","WEBCHAT"),
	WECHAT("weixin","微信"),
    VIDEO("butel","视频"),
    ELSE("","");
    
    public String key;
    public String desc;
    
    private IMSource(String key,String desc){
    	this.key=key;
    	this.desc=desc;
    }
    
    public static IMSource getEnum(String key){
    	
    	if(key!=null){
    		for(IMSource e:values()){
    			if(e.key.equalsIgnoreCase(key)){
    				return e;
    			}
    		}
    	}
    	return ELSE;
    }
}

package com.channelsoft.cri.cache.constant;

public enum WorkStatus {
	ALL("-1","全部"),
    NOACCEPT("0","尚未受理"),
    ACCEPT("1","受理中"),
    WAITINGFORCUSTOM("2","等待回复"),
    RESOLVE("3","已解决"),
    CLOSE("4","已关闭"),
    ELSE("5","其它");
    
    public String key;
    public String desc;
    
    WorkStatus(String key,String desc){
    	this.key=key;
    	this.desc=desc;
    }
    
    public static WorkStatus getEnum(String key){
    	
    	if(key!=null){
    		for(WorkStatus e:values()){
    			if(e.key.equalsIgnoreCase(key)){
    				return e;
    			}
    		}
    	}
    	return ELSE;
    	
    }
    
}

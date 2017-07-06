package com.channelsoft.cri.cache.constant;

public enum WorkType {

    PROBLEM("1","问题"),
    TRASCTION("2","事务"),
    FAULT("3","故障"),
    TASK("4","任务"),
    ELSE("","-");
    
    public String key;
    public String desc;
    
    WorkType(String key,String desc){
    	this.key=key;
    	this.desc=desc;
    }
    
    public static WorkType getEnum(String key){
    	if(key!=null){
    		for(WorkType e:values()){
    			if(e.key.equalsIgnoreCase(key)){
    				return e;
    			}
    		}
    	}
    	return ELSE;
    }
    
    public static WorkType getEnum1(String desc){
    	if(desc!=null){
    		for(WorkType e:values()){
    			if(e.desc.equalsIgnoreCase(desc)){
    				return e;
    			}
    		}
    	}
    	return ELSE;
    }
}

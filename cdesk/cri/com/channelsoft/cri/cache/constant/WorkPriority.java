package com.channelsoft.cri.cache.constant;

public enum WorkPriority {
	EMERGENCE("4","紧急"),
	HIGHT("3","高"),
    MIDDLE("2","中"),
    LOW("1","低"),
    ELSE("","-");
    
    public String key;
    public String desc;
    
    WorkPriority(String key,String desc){
    	this.key=key;
    	this.desc=desc;
    }
    
    public static WorkPriority getEnum(String key){
    	
    	if(key!=null){
    		for(WorkPriority e:values()){
    			if(e.key.equalsIgnoreCase(key)){
    				return e;
    			}
    		}
    	}
    	return ELSE;
    	
    }
    
    public static WorkPriority getEnum1(String desc){
    	
    	if(desc!=null){
    		for(WorkPriority e:values()){
    			if(e.desc.equalsIgnoreCase(desc)){
    				return e;
    			}
    		}
    	}
    	return ELSE;
    	
    }
}

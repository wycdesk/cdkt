package com.channelsoft.cdesk.attachments.constant;

public enum Source {
  WORK("0","工单"),
  DOCUMENT("1","文档"),
  EDITOR("2","编辑器"),
  ALL("3","全部"),
  ELSE("","尚未使用");
  
  public String key;
  public String desc;
  
  Source(String key,String desc){
	  this.key = key;
	  this.desc = desc;
  }
  
  public static Source getEumn(String key){
	  if(key != null){
		  for(Source e:values()){
			  if(e.key.equals(key)){
				  return e;
			  }
		  }
	  }
	   return ELSE;
  }
}

package com.channelsoft.cdesk.attachments.constant;

import org.apache.commons.lang.StringUtils;

public enum Type {
  
	EXCEL("0","表格",0),
	PICTURE("1","图片",1),
	DOCUMENT("2","文档",2),
	ZIPFILE("3","压缩包",3),
	ALL("4","全部",4),
	ELSE("","未定义",-1);
	
	public String key;
	public String desc;
	public int value;
	public static String[][]  type= {{".cvs",".xls",".xlsx"},{".jpeg",".jpg",".png",".gif"},{".txt",".doc",".docx"},
	                                 {".zip",".rar"}};
   Type(String key,String desc,int value){
	   this.key = key;
	   this.desc= desc;
	   this.value= value;
   }
	
   public static Type getEnumFromType(String fileType){
	   if(StringUtils.isNotBlank(fileType)){
		   for(int i=0;i<type.length;i++){
			   for(int j=0;j<type[i].length;j++){
				   if(fileType.equalsIgnoreCase(type[i][j])){
					   for(Type e:values()){
						   if(e.value==i)
							   return e;
					   }
				   }
			   }
		   }
	   }
	   return ELSE;
   }
   
   public static Type getEnumFromKey(String key){
	   if(StringUtils.isNotBlank(key)){
		   for(Type e:values()){
			   if(e.key.equals(key)){
				   return e;
			   }
		   }
	   }
	   return ELSE;
   }
}

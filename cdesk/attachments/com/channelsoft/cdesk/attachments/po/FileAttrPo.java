package com.channelsoft.cdesk.attachments.po;

public class FileAttrPo {
  private String id;             //附件id
  
  private String fileNew;        //附件新名
  
  private String originalName;       //附件旧名
  
  private String source;        //附件来源
  
  private String size;          //附件的大小
  
  private String sourceId;      //附件源id
  
  private String sourceTitle;    //附件源标题
  
  private String type;              //文件类型             
  
  private String userId;             //文件上传用户id
  
  private String userName;          //文件上传用户名
  
  private String createTime;      //文件上传时间

  public String getId() {
		return id;
   }
	
   public void setId(String id) {
		this.id = id;
	}
	
   public String getSource() {
		return source;
	}
	
   public void setSource(String source) {
		this.source = source;
	}
	
   public String getSize() {
		return size;
	}
	
   public void setSize(String size) {
		this.size = size;
	}
	
   public String getSourceId() {
		return sourceId;
	}
	
   public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}
	
   public String getSourceTitle() {
		return sourceTitle;
	}
	
   public void setSourceTitle(String sourceTitle) {
		this.sourceTitle = sourceTitle;
	}
	
   public String getType() {
		return type;
	}
	
   public void setType(String type) {
		this.type = type;
	}
	
	
   public String getCreateTime() {
		return createTime;
	}
	
   public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	

public String getUserId() {
	return userId;
}

public void setUserId(String userId) {
	this.userId = userId;
}

public String getUserName() {
	return userName;
}

public void setUserName(String userName) {
	this.userName = userName;
}

public String getFileNew() {
	return fileNew;
}

public void setFileNew(String fileNew) {
	this.fileNew = fileNew;
}

public String getOriginalName() {
	return originalName;
}

public void setOriginalName(String originalName) {
	this.originalName = originalName;
}
    
}

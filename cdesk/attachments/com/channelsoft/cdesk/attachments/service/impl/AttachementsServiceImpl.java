package com.channelsoft.cdesk.attachments.service.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import com.channelsoft.cdesk.attachments.constant.Type;
import com.channelsoft.cdesk.attachments.repository.IAttachementsRpy;
import com.channelsoft.cdesk.attachments.service.IAttachementsService;
import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.vo.PageInfo;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class AttachementsServiceImpl implements IAttachementsService {
  
	private static Logger log = LoggerFactory.getLogger(AttachementsServiceImpl.class);
	@Autowired
	IAttachementsRpy attachmentRpy;
	
	@Override
	public DBObject uploadFileHandle(MultipartFile file,HttpServletRequest request,String entId) throws ServiceException, Exception {
		// TODO Auto-generated method stub
		log.info("-----------进入AttachementsServiceImpl.uploadFileHandle()方法-------------------");
		DBObject dbo = new BasicDBObject();
		int attachmentId = attachmentRpy.getFileId(entId);
		dbo.put("attachmentId", attachmentId);
		dbo.put("size", file.getSize());
		dbo.put("originalName", file.getOriginalFilename());
		dbo.put("userId", request.getParameter("userId"));
		dbo.put("userName", request.getParameter("userName"));
		dbo.put("createTime", new Date());
		writeFileToLocal(file,request,dbo,entId);
		//将数据存入到数据库
		attachmentRpy.insert(dbo,"entId_"+entId+"_attachment");
		return dbo;
	}
    /*
     * 将上传的文件存储到指定的文件夹
     */
	private void writeFileToLocal(MultipartFile file,HttpServletRequest request,DBObject dbo,String entId) throws Exception{
		//对象初始化
		log.info("------进入AttachementsServiceImpl.writeFileToLocal()方法,将上传文件存入磁盘--------------");
				OutputStream fop=null;
				InputStream fis = null;
				//获取文件
			    File newfile = creatFile(file,request,dbo,entId);
			    
			    try {
			    	fop = new BufferedOutputStream(new FileOutputStream(newfile));
			    	fis = new BufferedInputStream(file.getInputStream());
			    	byte[] buffer = new byte[fis.available()];
			    	fis.read(buffer);
					fop.write(buffer);
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw new Exception("内部异常");
				}finally{
					if(fis != null){
						fis.close();
					}
					if(fop != null){
						fop.close();
					}
				}
	}
	/*
	 * 获取到存储文件
	 */
	private File creatFile(MultipartFile file,HttpServletRequest request,DBObject dbo,String entId){
		String path=request.getSession().getServletContext().getRealPath("/")+"/attachments/"+entId;
		log.info("------进入AttachementsServiceImpl.creatFile()方法,获取存储文件的路径:"+path+"------------");
		String fileId = new ObjectId().toString();
		
		File f = new File(path);
		if(!f.exists()){
			f.mkdirs();
		}
		String fileType = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
		
		String fileTypeKey = Type.getEnumFromType(fileType).key;
		if(StringUtils.isBlank(fileTypeKey)){
			throw new ServiceException("文件格式不支持,请将文件压缩成rar或zip格式！"); 
		}
		dbo.put("type", fileTypeKey);
		File outFile = new File(f,fileId+fileType);
		dbo.put("fileNew", fileId+fileType);
		
		try {
				outFile.createNewFile();
				return outFile;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new ServiceException("创建文件失败！");
		}
	}
    /*
     *获取到用户上传的文件原名
     */
	@Override
	public String getAttachmentsName(String newFileName,String entId) throws ServiceException {
		// TODO Auto-generated method stub
		 String originalName = attachmentRpy.getAttachmentsName(newFileName,"entId_"+entId+"_attachment");
		 
		 if(originalName == null){
			 throw new ServiceException("下载文件不存在！");
		 }
		 
		 return originalName;
	}
    /*
     * 删除上传文件
     */
	@Override
	public void delete(HttpServletRequest request,String[] newFileName, String entId)
			throws ServiceException {
		// TODO Auto-generated method stub
		String basePath=request.getSession().getServletContext().getRealPath("/")+"/attachments/"+entId;
		//1.删除数据库中的数据
		attachmentRpy.delete(newFileName, "entId_"+entId+"_attachment");
	   //2.删除关联库的附件,下期功能
		attachmentRpy.deleteReply(newFileName,"entId_"+entId+"_work");
		//3.删除磁盘上的文件
		for(String fileName:newFileName){
			boolean flag = false;
			File file = new File(basePath,fileName);
			if (file.isFile() && file.exists()) {  
		        file.delete();
		        flag = true;
			}
			if(!flag){
				throw new ServiceException("删除磁盘上的文件失败！");
			}
		}
		
	}
	@Override
	public List<DBObject> query(String entId, String source, String type,PageInfo pageinfo)
			throws ServiceException {
		// TODO Auto-generated method stub
		return attachmentRpy.query("entId_"+entId+"_attachment",source,type,pageinfo);
		
	}
}

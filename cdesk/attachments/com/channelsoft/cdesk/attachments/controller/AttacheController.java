package com.channelsoft.cdesk.attachments.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.channelsoft.cdesk.attachments.service.IAttachementsService;
import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.logger.SystemLogUtils;
import com.channelsoft.cri.util.DomainUtils;
import com.channelsoft.cri.vo.AjaxResultPo;
import com.channelsoft.cri.vo.PageInfo;
import com.mongodb.DBObject;


@Controller
@RequestMapping("/attachments")
public class AttacheController {
  
	private static Logger log = LoggerFactory.getLogger(AttacheController.class);

	@Autowired
	private IAttachementsService attachmentService;	
	
	
/*
 * 上传附件
 * @param request,response
 * @return AjaxResultPo
 */
 @RequestMapping("/upload")
 @ResponseBody
 public AjaxResultPo upload(HttpServletRequest request,HttpServletResponse response){
	 log.info("-----------进入AttacheController.upload()方法-------------------");
	 //设置返回信息的格式
	 response.setContentType("text/html;charset=UTF-8");
	 List<DBObject> list = new ArrayList<DBObject>();
	 try {
		 String entId=DomainUtils.getEntId(request);
		 if(entId.equals("")){
			SystemLogUtils.Debug("企业为空");
			return AjaxResultPo.failed(new Exception("企业为空 "));
		  }
		//处理请求的信息
		 CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		 //判断是多部分请求
		 if(multipartResolver.isMultipart(request)){
			 //装换为多部分请求
			 MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
			 //获取上传的文件名
			 Iterator<String> ite = multiRequest.getFileNames();
			 //循环处理每一个文件
			 while(ite.hasNext()){
				 //通过文件名取得上传的文件
				 MultipartFile file = multiRequest.getFile(ite.next());
				 if(!file.isEmpty()){
					 //获取上传文件的名称
					 String fileName = file.getOriginalFilename();
					 //判断该文件是否存在
					 if(!fileName.trim().equals("")){
						 //获取上传文件的类型
						 String fileType = fileName.substring(fileName.lastIndexOf("."));

						 //判断上传文件的大小是否超过规定，上传文件必须小于2M(<=2M)
						 float fileSize = (float)file.getSize()/1024/1024;
						 if(fileSize>2){
							 return AjaxResultPo.failed(new Exception("文件大小不能超过2M！"));
						 }
						 //2.将上传的文件交给服务层，对文件进行处理
						 DBObject dbo = attachmentService.uploadFileHandle(file,request,entId);
						 dbo.removeField("_id");
						 list.add(dbo);
					 }
				 }
			 }
		 }
		 //对单文件进行处理
		 else{
			 MultipartHttpServletRequest multiRequest = multipartResolver.resolveMultipart(request);
		     MultipartFile file =multiRequest.getFile("fileImport");
		     //2.将上传文件交个服务层进行处理
		     DBObject dbo = attachmentService.uploadFileHandle(file,request,entId);
		     list.add(dbo);
		     //服务层处理完毕,返回信息
		 }
	} catch(ServiceException e){
		e.printStackTrace();
		return AjaxResultPo.failed(e);
	}
	 catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return AjaxResultPo.failed(new Exception("内部异常！"));
	}
	 return AjaxResultPo.success("上传成功", list.size(), list);
 }
 
 
 
 /*
  * 删除附件
  * @param request,response,newFileName
  * @return AjaxResultPo
  */
 @RequestMapping("/delete")
 @ResponseBody
 public AjaxResultPo delete(HttpServletRequest request,HttpServletResponse response,String[] newFileName){
	 log.info("-----------进入AttacheController.delete()方法-------------------");
	 try {
		 String entId=DomainUtils.getEntId(request);
		 if(entId==null || entId.equals("")){
			SystemLogUtils.Debug("企业为空");
			return AjaxResultPo.failed(new Exception("企业为空 "));
			}
		attachmentService.delete(request,newFileName,entId);
		return AjaxResultPo.successDefault();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return AjaxResultPo.failed(e);
	}

	
 }
 
 
 
 /*
  * 下载附件
  * @param request,response,newFileName
  * @return void
  */
 @RequestMapping("/download")
 @ResponseBody
 public void download(HttpServletRequest request,HttpServletResponse response,String newFileName) throws IOException{
	
	 //初始化变量
	 InputStream fis = null;
	 OutputStream toClient = null;
	 try {
		 
	String entId=DomainUtils.getEntId(request);
	if(entId.equals("")){
			SystemLogUtils.Debug("企业为空");
	   }
	 //获取文件资源存放地址
	 String ResourceBasePath = request.getSession().getServletContext().getRealPath("/")+"/attachments/"+entId;
	 
	 //通过文件id获取文件名
	 String fileName = attachmentService.getAttachmentsName(newFileName,entId);
	 //根据资源路径获取文件
	 File file= new File(ResourceBasePath,newFileName);
	
	 Long fileSize = file.length();
	 //以流的形式下载文件
		fis = new BufferedInputStream(new FileInputStream(file));
		byte[] buffer = new byte[fis.available()];
	 //将文件全部读入到buffer中
		fis.read(buffer);
	//重置response
		response.reset();
		response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("gb2312" ),"ISO8859-1"));
		response.addHeader("Content-Length", "" + fileSize);
		response.setContentType("application/x-download;charset=UTF-8");
	 //获取客户端的输入流
		toClient = new BufferedOutputStream(response.getOutputStream());
		toClient.write(buffer);
		toClient.flush();
		toClient.close();
	 } catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}finally{
		if(fis != null){
			fis.close();
		}
		if(toClient != null){
			toClient.flush();
			toClient.close();
		}
	}
 }
 
 
 
 /*
  * 查询附件
  */
 @RequestMapping("/query")
 @ResponseBody
 public AjaxResultPo query(HttpServletRequest request,String source,String type,int page,int rows){
	 //调用服务层查询附件
	 String entId=DomainUtils.getEntId(request);
		if(entId.equals("")){
				SystemLogUtils.Debug("企业为空");
		   }
	 PageInfo pageInfo = new PageInfo((page - 1) * rows, rows);
	 List<DBObject> list=attachmentService.query(entId,source,type,pageInfo);
	 return AjaxResultPo.success("查询成功！", pageInfo.getTotalRecords(), list);
 }
}

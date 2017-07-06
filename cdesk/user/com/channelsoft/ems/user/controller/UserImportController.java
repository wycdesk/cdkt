package com.channelsoft.ems.user.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.channelsoft.cri.constant.DateConstants;
import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.vo.AjaxResultPo;
import com.channelsoft.ems.sso.util.SsoSessionUtils;
import com.channelsoft.ems.sso.vo.SsoUserVo;
import com.channelsoft.ems.user.service.IUserImportMongoService;
import com.channelsoft.ems.user.service.IUserImportService;
import com.channelsoft.ems.user.vo.UserImportResultVo;

@Controller
@RequestMapping("/userImport")
public class UserImportController {

	private Logger logger = Logger.getLogger(UserImportController.class);
	
	@Autowired
	IUserImportService userImportService;
	@Autowired
	IUserImportMongoService userImportMongoService;
	
	/**
	 * 进入批量导入页面
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/index")
	public String index(HttpServletRequest request, HttpServletResponse response, Model model){
		return "userManage/importIndex";
    }
	/**
	 * 帮助文档页面
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/help")
	public String help(HttpServletRequest request, HttpServletResponse response, Model model){
		return "userManage/importHelp";
    }
	@ResponseBody
	@RequestMapping("/upload")
	public AjaxResultPo upload(HttpServletRequest request,HttpServletResponse response, String addUserFlag, String updateUserFlag){
		try{
			long t1 = System.currentTimeMillis();
			//mysql数据库
//			UserImportResultVo vo = userImportService.upload(request, addUserFlag, updateUserFlag);
			//使用mongodb数据库
//			UserImportResultVo vo = userImportMongoService.upload(request, addUserFlag, updateUserFlag);
			
			UserImportResultVo vo = userImportMongoService.uploadExcel(request, addUserFlag, updateUserFlag);
			
			long t2 = System.currentTimeMillis();
			logger.info("导入花费时间：" + (t2-t1) + "毫秒");
			vo.setTime((double)(t2-t1)/1000.0);
			return AjaxResultPo.success("成功", 0, vo);
		}catch(ServiceException e){
			e.printStackTrace();
			return AjaxResultPo.failed(e);
		}
	}
	
	@RequestMapping("/download")
	public void download(HttpServletRequest request,HttpServletResponse response, Model mode) throws Exception {
		
		try {
			File file = new File(request.getSession().getServletContext().getRealPath("") + "/download/userImport/demo.zip");
			if (file != null && file.exists()) {
				BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
				byte[] buffer = new byte[1024];
				response.setCharacterEncoding("utf-8");
				response.setContentType("multipart/form-data");// 不同类型的文件对应不同的MIME类型
				response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
				OutputStream os = response.getOutputStream();
				int len = 0;
				while ((len = bis.read(buffer)) > 0){
					os.write(buffer, 0, len);
				}
				bis.close();
				os.close();
			}
			else {
			    throw new RuntimeException("模板文件不存在");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("导出错误");
		} 
	}
	
	/**
	 * 下载excel模板
	 * @param request
	 * @param response
	 * @author wangjie
	 * @time 2016年3月22日下午8:28:36
	 */
	@RequestMapping("/downModelExcel")
	@ResponseBody
	public AjaxResultPo downModelExcel(HttpServletRequest request,HttpServletResponse response){
		try{
			SsoUserVo user = SsoSessionUtils.getUserInfo(request);
			String realPath = request.getSession().getServletContext().getRealPath("/");
			String basePath=realPath+"download/";
			String path = userImportMongoService.downloadExcel(request, basePath);
			String fileName=user.getEntId()+"_demo_"+DateConstants.DATE_FORMAT_NUM().format(new Date())+".xls";
			logger.info("企业:"+user.getEntId()+","+user.getUserName()+",下载批量导入模板文件,path="+path+",fileName="+fileName);
			
			OutputStream os = null;
		    try {  
		    	os= response.getOutputStream();
		    	response.reset();  
		        response.addHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(fileName, "UTF-8"));
		        File file = new File(basePath+path);
		        os.write(FileUtils.readFileToByteArray(file));  
		        os.flush();
		    }catch(Exception e){
		    	e.printStackTrace();
		    	throw new RuntimeException("导出错误");
		    }finally {  
		        if (os != null) {  
		            try {
						os.close();
					} catch (IOException e) {
						e.printStackTrace();
					}  
		        }  
		    }  
		}
		catch(ServiceException e){
			e.printStackTrace();
			return AjaxResultPo.failed(e);
		}
		return AjaxResultPo.successDefault();
	}
	
	/**
	 * 下载导入结果
	 * @param request
	 * @param response
	 * @return
	 * @author wangjie
	 * @time 2016年4月15日下午5:55:39
	 */
	@RequestMapping("/downResultExcel")
	@ResponseBody
	public AjaxResultPo downResultExcel(HttpServletRequest request,HttpServletResponse response){
		try{
			SsoUserVo user = SsoSessionUtils.getUserInfo(request);
			String realPath = request.getParameter("resultFilePath");
			File file = new File(realPath);
			logger.info("企业:"+user.getEntId()+","+user.getUserName()+",下载导入结果文件,path="+realPath+",fileName="+file.getName());
			
			OutputStream os = null;
		    try {  
		    	os= response.getOutputStream();
		    	response.reset();  
		        response.addHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(file.getName(), "UTF-8"));
		        
		        os.write(FileUtils.readFileToByteArray(file));  
		        os.flush();
		    }catch(Exception e){
		    	e.printStackTrace();
		    	throw new RuntimeException("下载错误");
		    }finally {  
		        if (os != null) {  
		            try {
						os.close();
					} catch (IOException e) {
						e.printStackTrace();
					}  
		        }  
		    }  
		}
		catch(ServiceException e){
			e.printStackTrace();
			return AjaxResultPo.failed(e);
		}
		return AjaxResultPo.successDefault();
	}
	
}

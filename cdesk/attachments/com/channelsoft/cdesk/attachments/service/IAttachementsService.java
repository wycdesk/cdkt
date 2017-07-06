package com.channelsoft.cdesk.attachments.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.vo.PageInfo;
import com.mongodb.DBObject;

public interface IAttachementsService {

	public DBObject uploadFileHandle(MultipartFile file,HttpServletRequest request,String entId) throws ServiceException, Exception;

	public String getAttachmentsName(String newFileName,String entId) throws ServiceException;

	public void delete(HttpServletRequest request,String[] newFileName,String entId) throws ServiceException;

	public List<DBObject> query(String entId, String source, String type,PageInfo pageinfo) throws ServiceException;

	
}

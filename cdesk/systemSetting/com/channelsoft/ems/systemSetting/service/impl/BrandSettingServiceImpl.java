package com.channelsoft.ems.systemSetting.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.channelsoft.cri.constant.DateConstants;
import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.util.WebappConfigUtil;
import com.channelsoft.ems.ent.dao.IDatEntDao;
import com.channelsoft.ems.ent.po.DatEntInfoPo;
import com.channelsoft.ems.sso.util.SsoSessionUtils;
import com.channelsoft.ems.sso.vo.SsoUserVo;
import com.channelsoft.ems.systemSetting.service.IBrandSettingService;

public class BrandSettingServiceImpl implements IBrandSettingService{

	@Autowired
	IDatEntDao datEntDao;
	
	@Override
	public int brandSetting(HttpServletRequest request, DatEntInfoPo po) throws ServiceException {
		// TODO Auto-generated method stub
		try{
			return datEntDao.brandSetting(po);
		}catch(DataAccessException e){
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public String changeimage(HttpServletRequest request, HttpServletResponse response, String type) throws ServiceException {
		// TODO Auto-generated method stub
		String path=request.getSession().getServletContext().getRealPath("/");
		SsoUserVo user = SsoSessionUtils.getUserInfo(request);
		MultipartHttpServletRequest multipartHttpservletRequest = (MultipartHttpServletRequest) request;
		MultipartFile file = multipartHttpservletRequest.getFile("image");
		String origName = file.getOriginalFilename();
		String ext = FilenameUtils.getExtension(origName).toLowerCase();
		
		/*if(!("JPG".equalsIgnoreCase(ext)||"PNG".equalsIgnoreCase(ext)||"GIF".equalsIgnoreCase(ext)||"jpeg".equalsIgnoreCase(ext))){
			throw new ServiceException("仅支持JPG, PNG 或 GIF 格式文件上传");
		}*/
		if(file.getSize()>300*1024){
			throw new ServiceException("图片大小超过网站限制");
		}
		
		String datePath =DateConstants.DATE_FORMAT_NUM_SHORT().format(new Date());
		path=path+ WebappConfigUtil.getParameter("imgURL").replace("entId", user.getEntId()) +datePath;
		
		File dir = new File(path);
		if(!dir.exists()){
			dir.mkdirs();
		}
		
		String fileName= user.getEntId()+"_"+DateConstants.DATE_FORMAT_NUM().format(new Date())+ new Random().nextInt(10)+"."+ext;
		String destPath = path+"/"+fileName;
		String resultPath = WebappConfigUtil.getParameter("imgURL").replace("entId", user.getEntId())+datePath+"/"+fileName;
		File destFile = new File(destPath);
		try {
			file.transferTo(destFile);
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
			throw new ServiceException("上传文件失败");
		}
		//存入数据库的相对路径
		//数据库存储了/upload/entID/image/之后的路径
		String url = datePath+"/"+fileName;
		DatEntInfoPo po = new DatEntInfoPo();
		po.setEntId(user.getEntId());
		po.setUpdatorId(user.getLoginName());
		po.setUpdatorName(user.getNickName());
		if("logo".equals(type)){
			po.setLogoUrl(url);
		}else if("favicon".equals(type)){
			po.setFaviconUrl(url);
		}
		try{
			datEntDao.changeimage(po, type);
			return resultPath;
		}catch(DataAccessException e){
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public int delimage(HttpServletRequest request, String type)
			throws ServiceException {
		// TODO Auto-generated method stub
		SsoUserVo user = SsoSessionUtils.getUserInfo(request);
		DatEntInfoPo po = new DatEntInfoPo();
		po.setEntId(user.getEntId());
		po.setUpdatorId(user.getLoginName());
		po.setUpdatorName(user.getNickName());
		try{
			return datEntDao.delimage(po, type);
		}catch(DataAccessException e){
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}

}

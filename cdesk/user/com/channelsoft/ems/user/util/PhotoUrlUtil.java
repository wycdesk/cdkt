package com.channelsoft.ems.user.util;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.ui.Model;

import com.channelsoft.cri.util.DomainUtils;
import com.channelsoft.cri.util.WebappConfigUtil;

/**
 * 获取用户头像URL地址
 * @author wangjie
 * @time 2015年12月4日上午10:23:40
 */
public class PhotoUrlUtil {

	/**
	 * 获取用户头像URL地址
	 * @param request
	 * @param model
	 * @param entId
	 * @param photoUrl 数据库内存储的地址
	 * @author wangjie
	 * @time 2015年12月4日上午10:25:16
	 */
	public static void getPhotoUrl(HttpServletRequest request, Model model, String entId, String photoUrl){
		if(StringUtils.isBlank(entId)){
			entId=DomainUtils.getEntId(request);
		}
		if(StringUtils.isNotBlank(photoUrl)){
			photoUrl = WebappConfigUtil.getParameter("userPhotoURL").replace("entId", entId)+ photoUrl;
			String photoPath = request.getSession().getServletContext().getRealPath("/")+photoUrl;
			File photoFile = new File(photoPath);
			if(!photoFile.exists()){
				//图片不存在,显示默认图片
				photoUrl=WebappConfigUtil.getParameter("userDefaultPhoto");
			}
		}else{
			photoUrl=WebappConfigUtil.getParameter("userDefaultPhoto");
		}
		
		model.addAttribute("photoUrl", photoUrl);
	}
	
	/**
	 * 获取用户头像路径
	 * @param request
	 * @param model
	 * @param entId
	 * @param photoUrl
	 * @return
	 * @author wangjie
	 * @time 2015年12月4日上午11:19:35
	 */
	public static String getPhotoPath(HttpServletRequest request, String entId, String photoUrl){
		if(StringUtils.isBlank(entId)){
			entId=DomainUtils.getEntId(request);
		}
		if(StringUtils.isNotBlank(photoUrl)){
			photoUrl = WebappConfigUtil.getParameter("userPhotoURL").replace("entId", entId)+ photoUrl;
			String photoPath = request.getSession().getServletContext().getRealPath("/")+photoUrl;
			File photoFile = new File(photoPath);
			if(!photoFile.exists()){
				//图片不存在,显示默认图片
				photoUrl=WebappConfigUtil.getParameter("userDefaultPhoto");
			}
		}else{
			photoUrl=WebappConfigUtil.getParameter("userDefaultPhoto");
		}
		
		return photoUrl;
	}
	
}

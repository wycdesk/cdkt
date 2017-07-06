package com.channelsoft.ems.register.util;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.ui.Model;

import com.channelsoft.cri.util.DomainUtils;
import com.channelsoft.cri.util.WebappConfigUtil;

/**
 * 获取平台LOGO和Favicon的URL路径
 * @author wangjie
 * @time 2015年12月1日下午2:24:58
 */
public class LogoUrlUtil {

	/**
	 * 获取平台LOGO和Favicon的URL路径
	 * 参数为空时，显示默认图片
	 * @param request
	 * @param model
	 * @param entId
	 * @param logoUrl
	 * @param faviconUrl
	 * @author wangjie
	 * @time 2015年12月1日下午2:35:21
	 */
	public static void getLogoUrl(HttpServletRequest request, Model model, String entId, String logoUrl, String faviconUrl){
		if(StringUtils.isBlank(entId)){
			entId=DomainUtils.getEntId(request);
		}
		if(StringUtils.isNotBlank(logoUrl)){
			logoUrl = WebappConfigUtil.getParameter("imgURL").replace("entId", entId)+ logoUrl;
			String logoPath = request.getSession().getServletContext().getRealPath("/")+logoUrl;
			File logoFile = new File(logoPath);
			if(!logoFile.exists()){
				//图片不存在,显示默认图片
				logoUrl=WebappConfigUtil.getParameter("logoURL");
			}
		}else{
			logoUrl=WebappConfigUtil.getParameter("logoURL");
		}
		if(StringUtils.isNotBlank(faviconUrl)){
			faviconUrl = WebappConfigUtil.getParameter("imgURL").replace("entId", entId)+ faviconUrl;
			String faviconPath = request.getSession().getServletContext().getRealPath("/")+faviconUrl;
			File faviconFile = new File(faviconPath);
			if(!faviconFile.exists()){
				//图片不存在,显示默认图片
				faviconUrl=WebappConfigUtil.getParameter("faviconURL");
			}
		}else{
			faviconUrl=WebappConfigUtil.getParameter("faviconURL");
		}
		model.addAttribute("logoUrl", logoUrl);
		model.addAttribute("faviconUrl", faviconUrl);
		
	}
}

package com.channelsoft.ems.systemSetting.controller;


import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.logger.ManageLogUtils;
import com.channelsoft.cri.util.WebappConfigUtil;
import com.channelsoft.cri.vo.AjaxResultPo;
import com.channelsoft.ems.ent.po.DatEntInfoPo;
import com.channelsoft.ems.ent.service.IDatEntService;
import com.channelsoft.ems.log.constants.BusinessTypeEnum;
import com.channelsoft.ems.log.constants.LogTypeEnum;
import com.channelsoft.ems.log.service.ILogMongoService;
import com.channelsoft.ems.sso.util.SsoSessionUtils;
import com.channelsoft.ems.sso.vo.SsoUserVo;
import com.channelsoft.ems.systemSetting.service.IBrandSettingService;


@Controller
@RequestMapping("/brandSetting")
public class BrandSettingController {

	private Logger logger = Logger.getLogger(BrandSettingController.class);
	
	@Autowired
	IBrandSettingService brandSettingService;
	@Autowired
	IDatEntService datEntService;
	@Autowired
	ILogMongoService logMongoService;
	
    /**
     * 系统设置-品牌设置
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/index")
	public String index(HttpServletRequest request, HttpServletResponse response,Model model){
    	SsoUserVo user = SsoSessionUtils.getUserInfo(request);
    	DatEntInfoPo po = new DatEntInfoPo();
    	po.setEntId(user.getEntId());
    	List<DatEntInfoPo> list = datEntService.query(po);
    	String entName="";
    	String entDesc="";
    	String seo="";
    	String logoUrl="";
    	String faviconUrl="";
    	if(list.size()!=0){
    		entName = list.get(0).getEntName();
    		entDesc = list.get(0).getEntDesc();
    		seo = list.get(0).getSeo();
    		if(StringUtils.isNotBlank(list.get(0).getLogoUrl())){
    			logoUrl = WebappConfigUtil.getParameter("imgURL").replace("entId", user.getEntId())+list.get(0).getLogoUrl();
    			String logoPath = request.getSession().getServletContext().getRealPath("/")+logoUrl;
    			File logoFile = new File(logoPath);
    			if(!logoFile.exists()){
    				//图片不存在,显示空
    				logoUrl="";
    			}
    		}
    		if(StringUtils.isNotBlank(list.get(0).getFaviconUrl())){
    			faviconUrl = WebappConfigUtil.getParameter("imgURL").replace("entId", user.getEntId())+list.get(0).getFaviconUrl();
    			String faviconPath = request.getSession().getServletContext().getRealPath("/")+faviconUrl;
    			File faviconFile = new File(faviconPath);
    			if(!faviconFile.exists()){
    				//图片不存在,显示空
    				faviconUrl="";
    			}
    		}
    	}
    	model.addAttribute("entName", entName);
    	model.addAttribute("entDesc", entDesc);
    	model.addAttribute("seo", seo);
    	model.addAttribute("logoUrl", logoUrl);
    	model.addAttribute("faviconUrl", faviconUrl);
		return "systemSetting/brand_baseSetting";
	}
    
    @ResponseBody
	@RequestMapping("/brandSetting")
    public AjaxResultPo brandSetting(HttpServletRequest request,HttpServletResponse response,DatEntInfoPo po){
    	try{
    		logger.info("品牌设置-基本设置");
    		SsoUserVo user = SsoSessionUtils.getUserInfo(request);
    		po.setEntId(user.getEntId());
    		po.setUpdatorId(user.getLoginName());
    		po.setUpdatorName(user.getNickName());
    		int num = brandSettingService.brandSetting(request, po);
    		if(num > 0){
    			ManageLogUtils.EditSuccess(request, "基本设置", po.getEntId(), "entName="+po.getEntName()+",entDesc="+po.getEntDesc()+",seo="+po.getSeo()+",updatorId="+po.getUpdatorId());
    			logMongoService.add(request, LogTypeEnum.ADD, BusinessTypeEnum.SYSTEM, po.getUpdatorId(), BusinessTypeEnum.SYSTEM.desc+"("+po.getEntId()+")", po);
    		}else{
    			ManageLogUtils.EditFail(request, new ServiceException("修改失败"), "基本设置", po.getEntId(), "entName="+po.getEntName()+",entDesc="+po.getEntDesc()+",seo="+po.getSeo()+",updatorId="+po.getUpdatorId());
    		}
			return AjaxResultPo.successDefault();
		}catch(ServiceException e){
			e.printStackTrace();
			ManageLogUtils.EditFail(request, new ServiceException("修改失败,"+e.getMessage()), "基本设置", po.getEntId(), "entName="+po.getEntName()+",entDesc="+po.getEntDesc()+",seo="+po.getSeo()+",updatorId="+po.getUpdatorId());
			return AjaxResultPo.failed(e);
		}
    }
    
    @ResponseBody
	@RequestMapping("/changeimage")
    public AjaxResultPo changeimage(HttpServletRequest request,HttpServletResponse response,String type){
    	try{
    		logger.info("品牌设置-基本设置-上传图片");
    		String path = brandSettingService.changeimage(request, response, type);
			return AjaxResultPo.success("上传成功!", 0, path);
		}catch(ServiceException e){
			e.printStackTrace();
			return AjaxResultPo.failed(e);
		}
    }
    
    @ResponseBody
	@RequestMapping("/delimage")
    public AjaxResultPo delimage(HttpServletRequest request,HttpServletResponse response,String type){
    	try{
    		logger.info("品牌设置-基本设置-删除图片");
    		int num = brandSettingService.delimage(request, type);
			return AjaxResultPo.success("删除成功!", 0, num);
		}catch(ServiceException e){
			e.printStackTrace();
			return AjaxResultPo.failed(e);
		}
    }
    
}

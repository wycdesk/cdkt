package com.channelsoft.ems.ent.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.channelsoft.cri.constant.BaseConstants;
import com.channelsoft.cri.constant.BaseErrCode;
import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.logger.ManageLogUtils;
import com.channelsoft.cri.vo.AjaxResultPo;
import com.channelsoft.ems.ent.po.DatEntInfoPo;
import com.channelsoft.ems.ent.service.IDatEntService;
import com.channelsoft.ems.log.constants.BusinessTypeEnum;
import com.channelsoft.ems.log.constants.LogTypeEnum;
import com.channelsoft.ems.log.service.ILogMongoService;
import com.channelsoft.ems.sso.util.SsoSessionUtils;
import com.channelsoft.ems.sso.vo.SsoUserVo;

@Controller
@RequestMapping("/ent")
public class DatEntInfoController {
	
	@Autowired
	IDatEntService entService;
	@Autowired
	ILogMongoService logMongoService;

	/**
     * 进入企业首页
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="")
    public String index(HttpServletRequest request, HttpServletResponse response){
        return "entIndex";
    }
    
    @ResponseBody
	@RequestMapping("/update")
	public AjaxResultPo update(DatEntInfoPo po, HttpServletRequest request) {
		try {
			SsoUserVo userInfo = SsoSessionUtils.getUserInfo(request);
			po.setUpdatorId(userInfo.getLoginName());
			po.setUpdatorName(userInfo.getNickName());
			entService.update(po);
			logMongoService.add(request, LogTypeEnum.UPDATE, BusinessTypeEnum.SYSTEM, po.getEntId(), BusinessTypeEnum.SYSTEM.desc+"(公司信息)", po);
			ManageLogUtils.EditSuccess(request, "企业", po.getEntId(), "");
			return BaseConstants.SUCCESS;
		} catch (ServiceException e) {
			ManageLogUtils.EditFail(request, e, "企业", po.getEntId(), "");
			return new AjaxResultPo(false, e.getDesc());
		} catch (Exception e) {
			e.printStackTrace();
			ManageLogUtils.EditFail(request, new ServiceException(BaseErrCode.GENERAL_ERROR), "企业", po.getEntId(), e.getMessage());
			return new AjaxResultPo(false, "修改失败！");
		}
	}
}

package com.channelsoft.ems.ent.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.channelsoft.cri.constant.BaseConstants;
import com.channelsoft.cri.constant.BaseErrCode;
import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.logger.ManageLogUtils;
import com.channelsoft.cri.vo.AjaxResultPo;
import com.channelsoft.cri.vo.DataResultVo;
import com.channelsoft.cri.vo.PageInfo;
import com.channelsoft.ems.ent.po.CfgEntWxPo;
import com.channelsoft.ems.ent.service.ICfgEntWxService;
import com.channelsoft.ems.log.constants.BusinessTypeEnum;
import com.channelsoft.ems.log.constants.LogTypeEnum;
import com.channelsoft.ems.log.service.ILogMongoService;
import com.channelsoft.ems.privilege.po.CfgRolePo;
import com.channelsoft.ems.privilege.vo.CfgRoleVo;
import com.channelsoft.ems.sso.util.SsoSessionUtils;
import com.channelsoft.ems.sso.vo.SsoEntInfoVo;
import com.channelsoft.ems.sso.vo.SsoUserVo;

@Controller
@RequestMapping("/entWx")
public class CfgEntWxController {
	
	@Autowired
	ICfgEntWxService entWxService;
	@Autowired
	ILogMongoService logMongoService;
	
	@RequestMapping(value = "/list")
	public String list(HttpServletRequest request, Model model) {
		return "systemSetting/wxList";
	}

    @RequestMapping(value="query")
    @ResponseBody
    public DataResultVo query(int rows, int page, HttpServletRequest request, HttpServletResponse response) {
		DataResultVo res = new DataResultVo();

		PageInfo pageInfo = new PageInfo((page-1)*rows, rows);
		SsoEntInfoVo entiInfo = SsoSessionUtils.getEntInfo(request);
		String enterpriseid = entiInfo.getEntId();
		try {
			List<CfgEntWxPo> list = entWxService.query(enterpriseid, new CfgEntWxPo(), pageInfo);
			res.setTotal(list.size());
			res.setRows(list);
		} catch (ServiceException e) {
			e.printStackTrace();
			res.setMessage(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			res.setMessage("查询微信号失败");
		}
		return res;
    }
    
    @ResponseBody
	@RequestMapping("/add")
	public AjaxResultPo add(CfgEntWxPo po, HttpServletRequest request) {
		try {
			SsoUserVo vo = SsoSessionUtils.getUserInfo(request);
			po.setCreatorId(vo.getLoginName());
			po.setCreatorName(vo.getNickName());
			SsoEntInfoVo entiInfo = SsoSessionUtils.getEntInfo(request);
			String enterpriseid = entiInfo.getEntId();
			po.setEntId(enterpriseid);
			entWxService.add(enterpriseid,po);
			ManageLogUtils.AddSucess(request, "微信号", po.getOriginalId(), "");
			logMongoService.add(request, LogTypeEnum.ADD, BusinessTypeEnum.WX, po.getOriginalId(), BusinessTypeEnum.WX.desc+"("+po.getOriginalId()+")", po);
			return BaseConstants.SUCCESS;
		} catch (ServiceException e) {
			ManageLogUtils.AddFail(request, e, "微信号", po.getOriginalId(), "");
			return new AjaxResultPo(false, e.getDesc());
		} catch (Exception e) {
			e.printStackTrace();
			ManageLogUtils.AddFail(request, new ServiceException(BaseErrCode.GENERAL_ERROR), "微信号", po.getOriginalId(), e.getMessage());
			return new AjaxResultPo(false, "添加失败！");
		}
	}
    
    @ResponseBody
	@RequestMapping("/delete")
	public AjaxResultPo delete(String originalId, HttpServletRequest request) {
		try {
			SsoEntInfoVo entiInfo = SsoSessionUtils.getEntInfo(request);
			String enterpriseid = entiInfo.getEntId();
			
			entWxService.delete(enterpriseid, originalId);
			ManageLogUtils.AddSucess(request, "微信号", originalId, "");
			logMongoService.add(request, LogTypeEnum.DELETE, BusinessTypeEnum.WX, originalId, BusinessTypeEnum.WX.desc+"("+originalId+")", originalId);
			return new AjaxResultPo(true, "删除成功！");
		} catch (ServiceException e) {
			ManageLogUtils.AddFail(request, e, "微信号", originalId, "");
			return new AjaxResultPo(false, e.getDesc());
		} catch (Exception e) {
			e.printStackTrace();
			ManageLogUtils.AddFail(request, new ServiceException(BaseErrCode.GENERAL_ERROR), "微信号", originalId, e.getMessage());
			return new AjaxResultPo(false, "删除失败！");
		}
	}
    
    @ResponseBody
	@RequestMapping("/update")
	public AjaxResultPo update(CfgEntWxPo po, HttpServletRequest request) {
		try {
			SsoUserVo vo = SsoSessionUtils.getUserInfo(request);
			po.setUpdatorId(vo.getLoginName());
			po.setUpdatorName(vo.getNickName());
			
			SsoEntInfoVo entiInfo = SsoSessionUtils.getEntInfo(request);
			String enterpriseid = entiInfo.getEntId();
			
			entWxService.update(enterpriseid,po);
			ManageLogUtils.AddSucess(request, "微信号", po.getOriginalId(), "");
			logMongoService.add(request, LogTypeEnum.UPDATE, BusinessTypeEnum.WX, po.getOriginalId(), BusinessTypeEnum.WX.desc+"("+po.getOriginalId()+")", po);
			return BaseConstants.SUCCESS;
		} catch (ServiceException e) {
			ManageLogUtils.AddFail(request, e, "微信号", po.getOriginalId(), "");
			return new AjaxResultPo(false, e.getDesc());
		} catch (Exception e) {
			e.printStackTrace();
			ManageLogUtils.AddFail(request, new ServiceException(BaseErrCode.GENERAL_ERROR), "微信号", po.getOriginalId(), e.getMessage());
			return new AjaxResultPo(false, "修改失败！");
		}
	}
}

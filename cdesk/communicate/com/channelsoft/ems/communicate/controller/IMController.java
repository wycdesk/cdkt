package com.channelsoft.ems.communicate.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.tuckey.web.filters.urlrewrite.utils.StringUtils;

import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.logger.SystemLogUtils;
import com.channelsoft.cri.util.DomainUtils;
import com.channelsoft.cri.vo.AjaxResultPo;
import com.channelsoft.ems.communicate.service.IIMService;
import com.channelsoft.ems.sso.util.SsoSessionUtils;
import com.channelsoft.ems.sso.vo.SsoUserVo;
import com.channelsoft.ems.user.po.DatEntUserPo;
import com.channelsoft.ems.user.service.IUserMongoService;

@Controller
@RequestMapping("/IM")
public class IMController {
	
	@Autowired
	IIMService imService;
	@Autowired
	IUserMongoService userMongoService;

	@RequestMapping("/index")
	public String index(HttpServletRequest request, HttpServletResponse response,Model model){
    	SsoUserVo user = SsoSessionUtils.getUserInfo(request);
    	model.addAttribute("userId", user.getUserId());
    	model.addAttribute("userName", user.getUserName());
    	
		return "im/index";
	}
	
	/**
	 * 查询用户信息，如果用户不存在则创建用户
	 * customerId客户编号
	 * webchatId IM编号
	 * userAccountType 用户类型
	 * @param request
	 * @param response
	 * @return
	 * @author wangjie
	 * @time 2016年3月15日下午7:58:15
	 */
	//该方法只能在IM里面调用，其他渠道需要更改source
	@ResponseBody
	@RequestMapping("/queryOrAddUser")
	public AjaxResultPo queryOrAddUser(HttpServletRequest request, HttpServletResponse response){
		try{
			SsoUserVo user = SsoSessionUtils.getUserInfo(request);
			String webchatId = request.getParameter("webchatId");
			//String userAccountType = request.getParameter("userAccountType");
			String startTime = request.getParameter("startTime");
			String imSource = request.getParameter("IMSource");
			SystemLogUtils.Debug("IM查询用户信息,entId="+user.getEntId()+",userAccount="+webchatId+",IMSource="+imSource+",startTime="+startTime);
			DatEntUserPo po = imService.queryOrAddUser(user,webchatId, startTime, imSource);
			return AjaxResultPo.success("查询用户信息成功", 1, po);
		}catch(ServiceException e){
			e.printStackTrace();
			return AjaxResultPo.failed(e);
		}
		
	}
	
	/**
	 * 查询客户信息
	 * @param request
	 * @return
	 * @author wangjie
	 * @time 2016年3月16日上午10:47:37
	 */
	@ResponseBody
	@RequestMapping("/queryCustomerById")
	public AjaxResultPo queryCustomerById(HttpServletRequest request){
		try {
			String entId=DomainUtils.getEntId(request);
			String customerId = request.getParameter("customerId");
			DatEntUserPo po=userMongoService.queryUserById(entId, customerId);
			return AjaxResultPo.success("获取用户成功", 1, po);
		} catch (ServiceException e) {
			e.printStackTrace();
			return AjaxResultPo.failed(e);
		}
	}
	
	/**
	 * 查询关联客户
	 * @param userName
	 * @param request
	 * @return
	 * @author wangjie
	 * @time 2016年3月27日下午1:15:39
	 */
	@ResponseBody
	@RequestMapping("/queryRelationUser")
	public AjaxResultPo queryRelationUser(String userName, HttpServletRequest request){
		try {
			if(StringUtils.isBlank(userName)){
				return new AjaxResultPo(false,"查询失败,用户名为空");
			}
			String entId=DomainUtils.getEntId(request);
			SystemLogUtils.Debug("IM查询关联用户,entId="+entId+",userName="+userName);
			List<DatEntUserPo> list = userMongoService.queryRelationUser(entId, userName);
			return AjaxResultPo.success("获取关联用户成功", 1, list);
		} catch (ServiceException e) {
			e.printStackTrace();
			return AjaxResultPo.failed(e);
		}
	}
	
}

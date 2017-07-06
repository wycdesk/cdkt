package com.channelsoft.ems.communicate.controller;

import java.net.URLDecoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.util.DBObjectUtils;
import com.channelsoft.cri.vo.AjaxResultPo;
import com.channelsoft.cri.vo.PageInfo;
import com.channelsoft.ems.communicate.po.CommPo;
import com.channelsoft.ems.communicate.service.ICommService;
import com.channelsoft.ems.communicate.service.IHistoryService;
 
import com.channelsoft.ems.field.po.BaseFieldPo;
 
 
import com.channelsoft.ems.field.util.FieldUtil;
import com.channelsoft.ems.sso.util.SsoSessionUtils;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

@Controller
@RequestMapping("/history")
public class HistoryController {

/*	@Autowired
	ICommHistoryFieldService commHistoryFieldService;*/
	@Autowired
	IHistoryService historyService;
	@Autowired
	ICommService commService;

	/**
	 * 
	 * 
	 * @Description: TODO 
	 * @author chenglitao
	 * @param    
	 * @date 2016年5月23日 上午10:35:18   
	 *
	 */
	@RequestMapping("/index")
	public String index(HttpServletRequest request,
			HttpServletResponse response, Model model) {

		String enterpriseid = SsoSessionUtils.getUserInfo(request).getEntId();
		List<BaseFieldPo> fieldList=FieldUtil.getCommHistoryTable(enterpriseid);
		request.setAttribute("historyList", fieldList);
		return "communicateHistory/index";
	}

	@ResponseBody
	@RequestMapping(value = "/queryCommHistory")
	public AjaxResultPo queryCommHistory(String entId,int rows, int page, HttpServletRequest request,Model model){
		PageInfo pageInfo = new PageInfo((page-1)*rows, rows);
		CommPo  po  =new CommPo();
		
		DBObject queryObject=DBObjectUtils.getDBObject(po);
		List<DBObject> list=historyService.queryCommHistory(queryObject, pageInfo);
 
 
		request.setAttribute("list", list);
		
		return AjaxResultPo.success("查询联络历史成功", pageInfo.getTotalRecords(), list);
	}
	
	
	@RequestMapping("/add")
	public String addHistoryPage(HttpServletRequest request,HttpServletResponse response, Model model) {
		String enterpriseid = SsoSessionUtils.getUserInfo(request).getEntId();
		model.addAttribute("entId", enterpriseid);
 /*
		List<CommHistoryDefinedFieldPo> fieldList = commHistoryFieldService.queryDefinedFiled(enterpriseid,CommHistoryFieldStatusEnum.NORMAL.value, null);
		request.setAttribute("historyFieldList", fieldList);
 */

		return "communicateHistory/addHistory";
	}

	@RequestMapping("/phone")
	public String modifyPhonePage(String commId,HttpServletRequest request,HttpServletResponse response, Model model) {
		String enterpriseid = SsoSessionUtils.getUserInfo(request).getEntId();
		model.addAttribute("entId", enterpriseid);
		/*List<CommHistoryDefinedFieldPo> fieldList = commHistoryFieldService.queryDefinedFiled(enterpriseid,CommHistoryFieldStatusEnum.NORMAL.value, null);
		request.setAttribute("historyFieldList", fieldList);
		*/
		CommPo po = new CommPo();
		po.setCommId(commId);
		List<DBObject> InfoList = commService.getComms(enterpriseid, po, null);
		CommPo commPo = new CommPo();
		if(InfoList.size()==1){
			DBObjectUtils.getObject(InfoList.get(0), commPo);
		}
		request.setAttribute("historyInfo", commPo);
		return "communicateHistory/historyPhone";
	}

	@RequestMapping("/chat")
	public String modifyChatPage(String commId,HttpServletRequest request,HttpServletResponse response, Model model) {
		String enterpriseid = SsoSessionUtils.getUserInfo(request).getEntId();
		model.addAttribute("entId", enterpriseid);
		/*List<CommHistoryDefinedFieldPo> fieldList = commHistoryFieldService.queryDefinedFiled(enterpriseid,CommHistoryFieldStatusEnum.NORMAL.value, null);
		request.setAttribute("historyFieldList", fieldList);
		*/
		CommPo po = new CommPo();
		po.setCommId(commId);
		List<DBObject> InfoList = commService.getComms(enterpriseid, po, null);
		CommPo commPo = new CommPo();
		if(InfoList.size()==1){
			DBObjectUtils.getObject(InfoList.get(0), commPo);
		}
		request.setAttribute("historyInfo", commPo);
		return "communicateHistory/historyChat";
	}

	/**
	 * 手动新增联络历史
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/handAdd")
	public AjaxResultPo handAdd(String entId, String historyInfo,
			HttpServletRequest request) {
		try {
			historyInfo = URLDecoder.decode(historyInfo, "utf-8");
			JSONObject json = null;
			DBObject dbo = (DBObject) JSON.parse(historyInfo);
			// System.out.println(dbo);
			historyService.add(dbo, entId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("添加联络历史出错");
		}
		return new AjaxResultPo(true);
	}

	/**
	 * 修改联络历史--电话
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/modifyPhone")
	public AjaxResultPo modifyPhone() {

		return new AjaxResultPo(true);
	}

	/**
	 * 修改联络历史--chat
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/modifyChat")
	public AjaxResultPo modifyChat() {

		return new AjaxResultPo(true);
	}
}

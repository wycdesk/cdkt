package com.channelsoft.ems.api.controller;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.channelsoft.cri.vo.AjaxResultPo;
import com.channelsoft.ems.api.po.WorkInfoPo;

@Controller
@RequestMapping("/wfInfoController")
public class WfInfoController {
	
	private Logger logger = Logger.getLogger(WfInfoController.class);
	
	@RequestMapping("/turnToLeftInfo")
	public String turnToLeftInfo(HttpServletRequest request, HttpServletResponse response){
		return "order/detail";
	}
	
	/**
	 * 查询工单页面左则数据
	 * 通过json的方式，返回给页面，供其加载显示。
	 */
	@RequestMapping("/queryWfInfoLeft")
	@ResponseBody
	public AjaxResultPo queryWfInfoLeft(String wfId, HttpServletRequest request) {
		logger.info("WfInfoController.queryWfInfoLeft被调用，查询工单页面左则数据开始...");
		//此处为根据工单ID（编号）查询数据库数据，应要求，先作预留。
		//返回数据暂以模拟写死的方式进行。
		
		System.out.println("工单ID为：" + wfId);
		
		List<WorkInfoPo> resultList = new ArrayList<WorkInfoPo>();
		//这里进行设值
		WorkInfoPo wip = new WorkInfoPo();
		//受理客服组ID：serviceGroupId
		//wip.setServiceGroupId("kfz1001");
		//受理客服组名称：serviceGroupName
		wip.setServiceGroupName("4");
		//受理客服ID：customServiceId
		//wip.setCustomServiceId("kf1001");
		//受理客服名称：customServiceName
		wip.setCustomServiceName("2");
		//抄送副本：copyFor
		//工单状态：status
		wip.setStatus("2");
		//工单类型：type
		wip.setType("3");
		//优先级：priority
		wip.setPriority("4");
		//标签：label
		
		resultList.add(wip);
		
		logger.info("WfInfoController.queryWfInfoLeft调用结束。");
		return AjaxResultPo.success("查询成功", resultList.size(), resultList);
	}
	
}

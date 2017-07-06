package com.channelsoft.ems.report.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.channelsoft.cri.logger.SystemLogUtils;
import com.channelsoft.cri.util.DomainUtils;
import com.channelsoft.cri.vo.AjaxResultPo;
import com.channelsoft.ems.communicate.service.ICommService;
import com.channelsoft.ems.report.service.IReportService;
import com.channelsoft.ems.report.util.GetKeys;
import com.channelsoft.ems.sso.util.SsoSessionUtils;
import com.channelsoft.ems.sso.vo.SsoUserVo;
import com.mongodb.DBObject;

@Controller
@RequestMapping("/report")
public class ReportController {

	@Autowired
	IReportService reportService;
	@Autowired
	ICommService commService;
	
	private static Logger log = LoggerFactory.getLogger(ReportController.class);
	/**
	 * 查询客户数量
	 * 服务客户数
	 * 请求渠道
	 * @param request
	 * @param response
	 * @author wangjie
	 * @time 2016年3月8日下午5:36:35
	 */
	@RequestMapping("/queryCustomer")
	@ResponseBody
	public AjaxResultPo queryCustomer(HttpServletRequest request, HttpServletResponse response) {
		try {
			SsoUserVo user = SsoSessionUtils.getUserInfo(request);

			if (user == null)
			{
				throw new Exception("sso为空！");
			}
			if (user.getGroup() == null || user.getUserId() == null || user.getEntId() == null)
			{
				throw new Exception("参数为空！");
			}
			log.info("进入查询客户数量方法：客服组ID=" + user.getGroup() + "用户ID=" + user.getUserId() + "企业ID=" + user.getEntId());

			Map<String, Object> map = reportService.query(user.getEntId());
			
			//查询请求渠道
			List<DBObject> sourceList = commService.querySource(user.getEntId());
			long total = 0;
			for(DBObject s: sourceList){
				long n = Long.parseLong(s.get("num")+"");
				total += n;
			}
			map.put("sourceList", sourceList);
			map.put("sourceTotal", total);
			
			return AjaxResultPo.success("查询成功", 1, map);
		} catch (Exception e)
		{
			e.printStackTrace();
			return AjaxResultPo.failed(e);
		}
	}
	
	@RequestMapping("/queryServedCustomer")
	@ResponseBody
	public AjaxResultPo queryServedCustomer(HttpServletRequest request,String beginTime,String endTime) {
		try {
			if(beginTime==null||endTime==null){
				throw new Exception("时间参数不能为空");
			}
			SsoUserVo user = SsoSessionUtils.getUserInfo(request);

			if (user == null)
			{
				throw new Exception("sso为空！");
			}
			if (user.getGroup() == null || user.getUserId() == null || user.getEntId() == null)
			{
				throw new Exception("参数为空！");
			}
			log.info("进入查询服务客户数量方法：客服组ID=" + user.getGroup() + "用户ID=" + user.getUserId() + "企业ID=" + user.getEntId());

			Map<String, Object> map = reportService.queryServedCm(user.getEntId(),beginTime,endTime);
			
			return AjaxResultPo.success("查询成功", 1, map);
		} catch (Exception e)
		{
			e.printStackTrace();
			return AjaxResultPo.failed(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/queryPerformanceSort")
	public AjaxResultPo queryPerformanceSort(HttpServletRequest request,String beginTime,
			String endTime,String type){
		String entId=DomainUtils.getEntId(request);
		
		//List<Map<String,Object>> workList=new ArrayList<Map<String,Object>>();
		Map<String,Map<String,Object>> workList=new HashMap<String,Map<String,Object>>();
		
		List<Entry<String, Map<String, Object>>> list=new ArrayList<Entry<String, Map<String, Object>>>();
		SystemLogUtils.Debug(String.format("===========准备查询业绩排行榜=========== beginTime=%s,endTime=%s,type=%s",
				beginTime,endTime,type));
		try {
			//以客服（客服组）ID和status（状态）进行分组来查询工单表
			List<DBObject> baseSortList=reportService.getBaseSortList(entId,beginTime,endTime,type);
			
			//以客服（客服组）ID和source（渠道）进行分组来查询联络历史表
			List<DBObject> commList=commService.getBaseSortList(entId,beginTime,endTime,type);
			
			if(baseSortList.size()==0&&commList.size()==0){
				return AjaxResultPo.success("查询成功", list.size(), list);
			}
			//将数据进行整合，将工单表查到的一个客服（技能组）的多条数据整合为
			for(int i=0;i<baseSortList.size();i++){
				Map<String,Object> dbI=((Map<String,Object>)baseSortList.get(i).get("_id"));
				if(((String)dbI.get("status")).equals("4")){
					continue;
				}
				int sum=(int)baseSortList.get(i).get("sum");
				if(workList.containsKey(dbI.get(type))){
					Map<String,Object> tmpObj=workList.get(dbI.get(type));
					if(((String)dbI.get("status")).equals("3")){
						tmpObj.put("solved",(int)(tmpObj.get("solved"))+sum);
						//tmpObj.put("solved",sum);
						tmpObj.put("servNum",(int)tmpObj.get("servNum")+sum);
					}else{
						tmpObj.put("unsolved",(int)(tmpObj.get("unsolved"))+sum);
						tmpObj.put("servNum",(int)tmpObj.get("servNum")+sum);
					}
				}else{
					if(((String)dbI.get("status")).equals("3")){
						dbI.put("solved",sum);
						dbI.put("unsolved",0);
						dbI.put("servNum",sum);
						dbI.remove("status");
						workList.put((String)dbI.get(type), dbI);
					}else{
						dbI.put("unsolved",sum);
						dbI.put("solved",0);
						dbI.put("servNum",sum);
						dbI.remove("status");
						workList.put((String)dbI.get(type),dbI);
					}
				}
			}
			//将联络历史的查询结果整合到工单查询结果里面
			String tp=type.indexOf("custom")>=0?"userId":"groupId";
			for(int i=0;i<commList.size();i++){
				Map<String,Object> dbI=((Map<String,Object>)commList.get(i).get("_id"));
				int sum=(int)commList.get(i).get("sum");
				if(workList.containsKey(dbI.get(tp))){
					Map<String,Object> tmp=workList.get(dbI.get(tp));
					tmp.put("servNum", (int)tmp.get("servNum")+sum);
					if(((String)dbI.get("source")).equals("6")){
						if(tmp.containsKey("source5")){
							tmp.put("source5",(int)tmp.get("source5")+sum);
						}else{
							tmp.put("source5",sum);
						}
					}else{
						tmp.put("source"+(String)dbI.get("source"),sum);
					}
				}
			}
			
			//进行排序
			list = new ArrayList<Entry<String,Map<String,Object>>>(workList.entrySet());
			Collections.sort(list, new Comparator<Map.Entry<String,Map<String,Object>>>() {
				public int compare(Map.Entry<String, Map<String,Object>> o1,
						Map.Entry<String, Map<String,Object>> o2) {
					return ((int)(o2.getValue().get("solved"))- (int)(o1.getValue().get("solved")));
				}
			});
			return AjaxResultPo.success("查询成功", list.size(), list);
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxResultPo.failed(e);
		}
	}
	
	@ResponseBody
	@RequestMapping("/getServiceStatistic")
	public AjaxResultPo getServiceStatistic(HttpServletRequest request,String beginTime,String endTime){
		String entId=DomainUtils.getEntId(request);
		Map<String, Object> serviceMap = new HashMap<String, Object>();
		beginTime+=" 00:00:00";
		endTime+=" 23:59:59";
		List<String> keysList = GetKeys.getKeys(beginTime, endTime);
		serviceMap.put("keys", keysList);
		List<Long> valuesList = reportService.getServiceByTime(beginTime, endTime, entId);
		serviceMap.put("values", valuesList);
		return AjaxResultPo.success("服务量查询成功", valuesList.size(), serviceMap);
	}
}

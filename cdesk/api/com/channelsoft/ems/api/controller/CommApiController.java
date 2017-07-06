package com.channelsoft.ems.api.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.logger.SystemLogUtils;
import com.channelsoft.ems.api.constants.EventNameConstants;
import com.channelsoft.ems.api.po.ResultPo;
import com.channelsoft.ems.redis.util.ParamUtils;
import com.channelsoft.ems.communicate.service.ICommService;
import com.channelsoft.ems.ent.po.DatEntInfoPo;
import com.channelsoft.ems.user.po.DatEntUserPo;
import com.channelsoft.ems.user.service.IUserMongoService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

@Controller
@RequestMapping("api/communicate")
public class CommApiController {
	
	@Autowired
	IUserMongoService userMongoService;
	@Autowired
	ICommService commService;

	/**
	 * 向外提供的保存联络历史的接口
	 * @param request
	 * @param accountId
	 * @param accountType
	 * @param commPo
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/saveComm")
	public ResultPo saveComms(HttpServletRequest request,String commInfo){
		if(StringUtils.isBlank(commInfo)){
			return ResultPo.failed(new Exception("没有指定参数"));
		}
		DBObject dbo=null;
		DatEntUserPo user=null;
		String accountId=null,accountType=null;
		String eventName=null;
		DatEntInfoPo entInfo=null;
		String entId=null;
		Date optTime=null;
		SystemLogUtils.Debug(String.format("==============准备保存联络历史: 参数commInfo=%s",commInfo));
		try {
			commInfo=URLDecoder.decode(commInfo, "UTF-8");
			dbo=(DBObject)JSON.parse(commInfo);

			//判断参数信息是否完整
			if(!(dbo.containsField("accountId")&&dbo.containsField("accountType")&&dbo.containsField("sessionId")
					&&dbo.containsField("ccodAgentId")&&dbo.containsField("eventName")&&dbo.containsField("ccodEntId")&&dbo.containsField("optTime"))){
				throw new Exception("参数信息不完整或为空");
			}
			entInfo=ParamUtils.getDatEntInfoPo((String)dbo.get("ccodEntId"));
			if(entInfo==null){
				throw new Exception("企业不存在");
			}
			entId=entInfo.getEntId();
			
			//判断此sessionId的联络历史是否存在
			DBObject exist=commService.getCommBySession(entId,(String)dbo.get("sessionId"));
			
			eventName =(String)dbo.get("eventName");
			if(!(eventName.equals(EventNameConstants.EVENT_OUTBOUND_CONNECTED_OP)||eventName.equals(EventNameConstants.EVENT_TP_DISCONNECT)||eventName.equals(EventNameConstants.EVENT_OP_DISCONNECT)||eventName.equals(EventNameConstants.EVENT_OUTBOUND_ALERTING_OP))){
				throw new Exception("保存联络历史参数eventName="+eventName+"格式不正确");
			}
			optTime=new Date(Long.valueOf((String)dbo.get("optTime")));
			
			if(exist!=null&&!(eventName.equals(EventNameConstants.EVENT_OUTBOUND_ALERTING_OP))){
				if(eventName.equals(EventNameConstants.EVENT_OUTBOUND_CONNECTED_OP)){
					DBObject answer=new BasicDBObject();
					answer.put("isConnected", "1");
					answer.put("startTime",optTime);
					int ansup=commService.endComm(entId, (String)dbo.get("sessionId"), answer);
					if(ansup<0){
						throw new Exception("保存联络历史失败");
					}
				}else if(eventName.equals(EventNameConstants.EVENT_OP_DISCONNECT)||eventName.equals(EventNameConstants.EVENT_TP_DISCONNECT)){
					DBObject drop=new BasicDBObject("endTime",optTime);
					String commTime="0";
					commTime=(optTime.getTime()-((Date)exist.get("startTime")).getTime())/1000+"";
					drop.put("commTime", commTime);
					int dropup=commService.endComm(entId, (String)dbo.get("sessionId"), drop);
					if(dropup<0){
						throw new Exception("保存联络历史失败");
					}
				}
			}else{
				if(eventName.equals(EventNameConstants.EVENT_OUTBOUND_ALERTING_OP)||eventName.equals(EventNameConstants.EVENT_OUTBOUND_CONNECTED_OP)){
					dbo.put("startTime", optTime);	
				}else{
					dbo.put("endTime", optTime);
				}
				dbo.removeField("optTime");
				dbo.removeField("eventName");
				try {
					SystemLogUtils.Debug(String.format("查询或添加用户: accountId=%s,accountType=%s",accountId,accountType));
					accountId=(String)dbo.get("accountId");
					accountType=(String)dbo.get("accountType");
					user=userMongoService.queryOrAddUser(entId, accountId, accountType);
				} catch (ServiceException e) {
					e.printStackTrace();
					throw new Exception(String.format("用户账户accountId=%s,accountType=%s查询或添加用户异常",accountId,accountType));
				}
				if(user==null){
					SystemLogUtils.Debug(String.format("==============查询或添加用户: accountId=%s,accountType=%s 失败",accountId,accountType));
					throw new Exception(String.format("用户账户accountId=%s,accountType=%s查询或添加用户失败",accountId,accountType));
				}
				DatEntUserPo agent=ParamUtils.getUser(entId, (String)dbo.get("ccodAgentId"));
				if(agent==null){
					throw new Exception(String.format("agentId=%s的坐席不存在",(String)dbo.get("ccodAgentId")));
				}
				String ccodAgentName=agent.getUserName();
				dbo.put("ccodAgentName",ccodAgentName);
				dbo.put("userId", user.getUserId());
				dbo.put("loginName", user.getLoginName());
				dbo.put("userName", user.getUserName());
				dbo.put("opId", dbo.get("ccodAgentId"));
				dbo.put("opName", dbo.get("ccodAgentName"));
				SystemLogUtils.Debug(String.format("==============准备保存联络历史,传入参数commInfo=%s",commInfo));
				int save=-1;
				if(exist==null){
					save=commService.saveComm(entId, dbo);
				}else{
					save=commService.endComm(entId, (String)dbo.get("sessionId"), dbo);
				}
				if(save<0){
					throw new Exception("保存联络历史失败");
				}
			}
			
			SystemLogUtils.Debug(String.format("=====success======保存联络历史sessionId=%s成功,参数commInfo=%s",(String)dbo.get("sessionId"),commInfo));
			return ResultPo.success("保存联络历史成功", 1, "sessionId="+(String)dbo.get("sessionId"));
		} catch (Exception e) {
			e.printStackTrace();
			SystemLogUtils.Debug(String.format("保存联络历史异常,commInfo=%s",commInfo));
			return ResultPo.failed(new Exception("异常,"+ e.getMessage()));
		}
		
	}
}

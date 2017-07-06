package com.channelsoft.ems.api.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import com.channelsoft.cri.logger.SystemLogUtils;
import com.channelsoft.cri.util.HttpPostUtils;
import com.channelsoft.cri.util.WebappConfigUtil;
import com.channelsoft.ems.api.constants.MailDealType;
import com.channelsoft.ems.api.po.MailDistributePo;
import com.channelsoft.ems.api.po.SendMainResponsePo;
import com.channelsoft.ems.ent.util.EntAbIlityUtlis;

public class MailSendClient {

	static String domainName=WebappConfigUtil.getParameter("DOMAIN_NAME");
	//邮箱服务地址
	static String url = WebappConfigUtil.getParameter("MAIL_SERVICE_ROOT") + "/sendApi/sendMail";
	//域名注册服务地址
	static String registerDnsUrl = WebappConfigUtil.getParameter("MAIL_SERVICE_ROOT") + "/jamesManagerApi/registerdns";
	//企业注销服务地址
	static String cancelDnsUrl = WebappConfigUtil.getParameter("MAIL_SERVICE_ROOT") + "/jamesManagerApi/removedns";
	//平台邮箱
	static String platformMailAddress="support@"+domainName;
	//企业邮箱
	static String entMailAddress="support@entId."+domainName;
	static String domainAddress="entId."+domainName;
	public static  SendMainResponsePo sendMail(MailDistributePo po){
		SendMainResponsePo sr=new SendMainResponsePo();
		if(!EntAbIlityUtlis.hasMailAbility(po.getEntId())){
			SystemLogUtils.Debug(String.format("========该企业不具有邮件发送能力：=======,entId=%s", po.getEntId()));
			sr.setResult("0");
			return sr;
		}
		Map<String, String> param = new HashMap<String, String>();

		param.put("dealType", po.getDealType());
		param.put("sendAddr", po.getSendAddr());
		param.put("targetAddr", po.getTargetAddr());
		param.put("title", po.getTitle());
		param.put("content", po.getContent());
		param.put("entId", po.getEntId());
		String data = "";

		String result="1";
		String desc="发送失败";
		try {
			if(StringUtils.isBlank(po.getTargetAddr())){
				desc="发送失败,收件人不能为空";
			}
			else if(StringUtils.isBlank(po.getSendAddr())){
				desc="发送失败,发件人不能为空";
			}
			else if(StringUtils.isBlank(po.getTitle())){
				desc="发送失败,标题不能为空";
			}
			else{
				SystemLogUtils.Debug(String.format("========准备发送邮件：=======,entId=%s,dealType=%s,sendAddr=%s,targetAddr=%s,title=%s,content=%s", po.getEntId(), po.getDealType(),po.getSendAddr(),po.getTargetAddr(),po.getTitle(),po.getContent()));
				data = HttpPostUtils.httpPost(url, param);
				SystemLogUtils.Debug("========发送邮件返回：======="+data);
				SystemLogUtils.Debug(String.format("========发送邮件返回：=======,entId=%s,dealType=%s,sendAddr=%s,targetAddr=%s,title=%s,data=%s", po.getEntId(), po.getDealType(),po.getSendAddr(),po.getTargetAddr(),po.getTitle(),data));
				JSONObject json=new JSONObject(data);
				if(json.has("result")) result=json.getString("result");
				if(json.has("desc")) desc=json.getString("desc");
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		sr.setResult(result);
		sr.setDesc(desc);

       return sr;
	}
	/**
	 * 企业注册成功邮件发送
	 * @param entId 企业Id
	 * @param targetAddr
	 * @param title
	 * @param content
	 */
	public static SendMainResponsePo sendEntRegisterMail(String entId,String targetAddr,String title,String content){
		String result="1";
		String desc="";
		SendMainResponsePo sr=new SendMainResponsePo();
		try{
			String data=registerDns(entId);
			JSONObject json=new JSONObject(data);
		    result=json.getString("result");
		    desc=json.getString("desc");
		}
		catch(Exception e){
			e.printStackTrace();
		}
		if("0".equals(result)){
			MailDistributePo po=new MailDistributePo();
			po.setDealType(MailDealType.ENT_REGISTER.value);
			po.setSendAddr(entMailAddress.replace("entId", entId));
			po.setTargetAddr(targetAddr);
			po.setTitle(title);
			po.setContent(content);
			po.setEntId(entId);
			sendMail(po);
		}
		sr.setResult(result);
		sr.setDesc(desc);
		return sr;
	}
	/**
	 * 普通邮件发送
	 * @param sendAddress
	 * @param targetAddr
	 * @param title
	 * @param content
	 */
	public static SendMainResponsePo sendMail(String sendAddress,String targetAddr,String title,String content){

		MailDistributePo po=new MailDistributePo();
		po.setDealType(MailDealType.NORMAL.value);
		po.setSendAddr(sendAddress);
		po.setTargetAddr(targetAddr);
		po.setTitle(title);
		po.setContent(content);
		return sendMail(po);
	}
	/**
	 * 企业发送普通邮件
	 * @param entId
	 * @param targetAddr
	 * @param title
	 * @param content
	 */
	public static SendMainResponsePo sendMailForEnt(String entId,String targetAddr,String title,String content){
        
		MailDistributePo po=new MailDistributePo();
		po.setDealType(MailDealType.NORMAL.value);
		po.setSendAddr(entMailAddress.replace("entId", entId));
		po.setTargetAddr(targetAddr);
		po.setTitle(title);
		po.setContent(content);
		po.setEntId(entId);
		return sendMail(po);
	}
	/**
	 * 平台发送普通邮件
	 * @param entId
	 * @param targetAddr
	 * @param title
	 * @param content
	 */
	public static SendMainResponsePo  sendMailForPlatoform(String targetAddr,String title,String content){
		MailDistributePo po=new MailDistributePo();
		po.setDealType(MailDealType.NORMAL.value);
		po.setSendAddr(platformMailAddress);
		po.setTargetAddr(targetAddr);
		po.setTitle(title);
		po.setContent(content);
		 return sendMail(po);
	}
	
	public static String  registerDns(String entId){
		Map<String, String> param = new HashMap<String, String>();

		param.put("dnsName", domainAddress.replace("entId", entId));
		String data = "";
		JSONObject json=new JSONObject();
		try {
			json.put("result", "1");
			json.put("desc", "开通域名失败");
			data = HttpPostUtils.httpPost(registerDnsUrl, param);
		} catch (Exception e) {
			e.printStackTrace();
			data = json.toString();
		}
		System.out.println("========接口调用方：======="+data);
		return data;

	}
	public static String removeDns(String entId){
		Map<String,String> param = new HashMap<String,String>();
		param.put("dnsName", domainAddress.replace("entId", entId));
		String data = "";
		JSONObject json=new JSONObject();
		try {
			json.put("result", "1");
			json.put("desc", "注销域名失败");
			data = HttpPostUtils.httpPost(cancelDnsUrl, param);
		} catch (Exception e) {
			e.printStackTrace();
			data = json.toString();
		}
		System.out.println("========接口调用方：======="+data);
		return data;
	}
}

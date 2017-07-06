package com.channelsoft.ems.api.controller;

import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.channelsoft.cri.logger.SystemLogUtils;
import com.channelsoft.cri.util.IpUtils;
import com.channelsoft.cri.util.WebappConfigUtil;
import com.channelsoft.ems.api.po.ResultPo;
import com.channelsoft.ems.redis.util.ParamUtils;
import com.channelsoft.ems.ent.po.DatEntInfoPo;
import com.channelsoft.ems.ent.service.IDatEntService;
import com.channelsoft.ems.register.util.MD5Util;
import com.channelsoft.ems.user.constant.LoginType;
import com.channelsoft.ems.user.po.DatEntUserPo;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

@Controller
@RequestMapping("/api/ent")
public class EntInfoController {

	@Autowired
	IDatEntService datEntService;
	/**
	 * 企业开户接口,提供给用户坐席
	 * @param request
	 * @param response
	 * @return
	 * @throws JSONException 
	 */
	@ResponseBody
	@RequestMapping(value = "/add")
	public ResultPo addEnt(HttpServletRequest request,HttpServletResponse response){
		String entInfo=request.getParameter("entInfo");
		boolean success=true;
		String desc="添加成功";
		DatEntInfoPo po=new DatEntInfoPo();
		String accountType="";
		String loginName="";
		String ccodEntId="";
		ResultPo result=new ResultPo(false,"添加失败");
		JSONObject res=new JSONObject();
	   	SystemLogUtils.Debug(String.format("添加企业信息: entInfo=%s", entInfo));
		try {
			entInfo=URLDecoder.decode(entInfo, "UTF-8");
		 	SystemLogUtils.Debug(String.format("添加企业信息,解析后, entInfo=%s", entInfo));
			JSONObject ent=new JSONObject(entInfo);
			ccodEntId=ent.getString("entId");//ccod企业id
			String domainName=ent.getString("domainName");//域名
			loginName=ent.getString("loginName");//用户名
	        String  signKey="";
			if(ent.has("accountType")){
				accountType=ent.getString("accountType");
			}
			if(ent.has("entName")){
				po.setEntName(ent.getString("entName"));
			}
			else{
				po.setEntName("");
			}
			if(ent.has("address")){
				po.setAddress(ent.getString("address"));
			}
			if(ent.has("contactUser")){
				po.setContactUser(ent.getString("contactUser"));
			}
			if(ent.has("contactWay")){
				po.setContactWay(ent.getString("contactWay"));
			}
			if(ent.has("email")){
				po.setEmail(ent.getString("email"));
			}
			if(ent.has("signKey")){
				signKey=ent.getString("signKey");
			}
			
			String ip=IpUtils.getIpAddr(request);
		 	SystemLogUtils.Debug(String.format("添加企业信息,解析后,ccodEntId=%s,domainName=%s,loginName=%s,accountType=%s,entName=%s,email=%s,ip=%s,signKey=%s", ccodEntId,domainName,loginName,accountType,po.getEntName(),po.getEmail(),ip,signKey));
			String sk=MD5Util.MD5(ccodEntId+"_"+loginName+"_"+ WebappConfigUtil.getParameter("USER_CENTER_SKEY"));
		 	if(StringUtils.isBlank(ccodEntId)||StringUtils.isBlank(domainName)||StringUtils.isBlank(loginName)){
				success=false;
				desc="企业ID、域名、用户名均不能为空";
			}
			else if(StringUtils.isBlank(LoginType.getEnum(accountType).value)){
				success=false;
				desc="帐号类型为空";
			}
			else if(!sk.equals(signKey)){
				success=false;
				desc="签名错误";
			}
            
		 	if(domainName.indexOf(".")>=0){
		 		success=false;
				desc="域名非法";

		 	}
		 	//手机号码验证规则，和CCOD验证规则保持一致，需要和CCOD同步修改
			if(LoginType.TELEPHONE.value.equals(accountType)){
				if(!(StringUtils.isNumeric(loginName)&&loginName.startsWith("1"))){
					success=false;
					desc=loginName+"不是手机号";
				}
			}
		 	boolean isExist=datEntService.existThisEntId(domainName.trim());
        	if (isExist) {
        		success=false;
				desc="域名"+domainName+"已存在";
        	}
        	
        	boolean isExistCcodEntId=datEntService.existThisCcodEntId(ccodEntId);
        	if (isExistCcodEntId) {
        		success=false;
				desc="企业"+ccodEntId+"已存在";
        	}
			if(success){
				po.setCcodEntId(ccodEntId);
				po.setDomainName(domainName.trim());
				po.setEntId(domainName.trim());
				po.setRegister(po.getContactUser());
				DatEntUserPo user=new DatEntUserPo();
				user.setIp(ip);
				user.setLoginType(accountType);
				user.setLoginName(loginName);
				user.setUserName(po.getRegister());
				user.setNickName(po.getRegister());
				user.setEmail(po.getEmail());
				String pwd="";
		        for(int i=0;i<8;i++){
		            pwd+=(int)(Math.random()*10)+"";
		         }
		        po.setPassword(pwd);
				datEntService.addEntForUserCenter(po, user);
				DBObject rows=new BasicDBObject();
				res.put("success", success);
				res.put("desc", desc);
				rows.put("pwd", po.getPassword());
				rows.put("cdeskUrl", WebappConfigUtil.getParameter("CDESK_ROOT").replace("entId", po.getEntId()));
				res.put("rows", rows);
				result=new ResultPo(success,desc,1,rows);
		
			}
			else{
				result=new ResultPo(success,desc);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			success=false;
			desc=e.getMessage();
			result=new ResultPo(success,desc);
			SystemLogUtils.Debug(String.format("添加企业信息,异常,ccodEntId=%s,domainName=%s,loginName=%s,accountType=%s,entName=%s,email=%s,msg=%s", po.getCcodEntId(),po.getDomainName(),loginName,accountType,po.getEntName(),po.getEmail(),e.getMessage()));
		}
		SystemLogUtils.Debug(String.format("添加企业信息返回,ccodEntId=%s,loginName=%s,desc=%s", ccodEntId,loginName,desc));

		return result;
	}
}

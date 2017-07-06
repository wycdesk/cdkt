package com.channelsoft.ems.androidApi.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.channelsoft.cri.logger.SystemLogUtils;
import com.channelsoft.cri.util.CdeskEncrptDes;
import com.channelsoft.cri.util.WebappConfigUtil;
import com.channelsoft.ems.api.po.ResultPo;
import com.channelsoft.ems.ent.po.DatEntInfoPo;
import com.channelsoft.ems.redis.util.ParamUtils;
import com.channelsoft.ems.sso.constant.SsoParamConstants;
import com.channelsoft.ems.sso.service.ISsoServerService;
import com.channelsoft.ems.user.service.IUserMongoService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;


@Controller
@RequestMapping("/androidApi")
public class AndoridLoginApiController {
	
	@Autowired
	IUserMongoService userMongoService;
	@Autowired
	ISsoServerService ssoServerService;
	@ResponseBody
	@RequestMapping("/login")	
	public ResultPo login(HttpServletRequest request,String loginInfo,HttpServletResponse response){
		//设置跨域访问
		response.addHeader("Access-Control-Allow-Origin","*");
		boolean success=true;
		String desc="登录成功";
		ResultPo result=new ResultPo(false,"登录失败");
		
		try{				
			String userId=request.getParameter("userId");
			String ccodEntId=request.getParameter("entId");
			String password=request.getParameter("password");
			SystemLogUtils.Debug(String.format("调用login---登录,解析后: userId=%s,entId=%s,account=%s", userId,ccodEntId,password));
			
			if(StringUtils.isBlank(userId) || StringUtils.isBlank(ccodEntId) || StringUtils.isBlank(password)){
				success=false;
				desc="参数信息不完整或为空";
				
				return ResultPo.failed(new Exception("参数信息不完整或为空"));
			}
			 DatEntInfoPo ent=ParamUtils.getDatEntInfoPo(ccodEntId);						
			// 验证企业存在
	    	if (ent==null||StringUtils.isBlank(ent.getEntId())) {
				success=false;
				desc="企业不存在";
				
				return ResultPo.failed(new Exception("企业不存在"));
	    	}
				    	
			DBObject obj=new BasicDBObject();
			obj.put("entId", ent.getEntId());
			obj.put("userId", userId);
	    	
	    	List<DBObject> list=userMongoService.queryUserList(obj, null);
	    	if(list==null||list.size()==0) return ResultPo.failed(new Exception("用户不存在"));
			String loginPwd = (String)list.get(0).get("loginPwd");
			/*Des解密*/
	    	loginPwd=CdeskEncrptDes.decryptST(loginPwd);
	    	/*MD5加密*/
	    	loginPwd = DigestUtils.md5Hex(loginPwd);
			
	    	/*时间戳*/
    		Date date = new Date();
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    		String str = sdf.format(date);
    		/*登录签名*/
    		String sk=CdeskEncrptDes.encryptST(userId+"_"+WebappConfigUtil.getParameter("Andriod_SKEY")+"_"+str);
	    	
	    	if(!loginPwd.equals(password)){
				success=false;
				desc="账号或密码不正确";
				
				return ResultPo.failed(new Exception("账号或密码不正确"));
	    	}else{	    		
	    		DBObject rows=new BasicDBObject();
	    		rows.put("signKey",sk);
//	    		ssoServerService.login(ent.getEntId().toLowerCase(), (String)list.get(0).get("loginName"),loginPwd, SsoParamConstants.PLATFORM_ID, request);
	    		result=new ResultPo(success,desc,1,rows);
	    	}
	    	
	    	SystemLogUtils.Debug(String.format("登陆成功返回,userId=%s,sk=%s,entId=%s", userId,sk,ccodEntId));
		}catch(Exception e){
			e.printStackTrace();
			success=false;
			desc=e.getMessage();
			result=new ResultPo(success,desc);
			
			SystemLogUtils.Debug(String.format("登录异常,loginInfo=%s",loginInfo));
		}
			
		return result;
		
	}
}

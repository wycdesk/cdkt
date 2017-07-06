package com.channelsoft.ems.iosapi.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.channelsoft.cri.logger.SystemLogUtils;
import com.channelsoft.cri.util.CdeskEncrptDes;
import com.channelsoft.cri.vo.PageInfo;
import com.channelsoft.ems.api.po.ResultPo;
import com.channelsoft.ems.ent.service.IDatEntService;
import com.channelsoft.ems.iosapi.util.IosSignKeyUtils;
import com.channelsoft.ems.user.constant.UserType;
import com.channelsoft.ems.user.service.IUserMongoService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

@Controller
@RequestMapping("/iosApi")
public class IosLoginApiController {
    
	@Autowired
	IDatEntService entService;
	
	@Autowired
	IUserMongoService userMongoService;
	
	@ResponseBody
	@RequestMapping("/login")	
	public ResultPo login(HttpServletRequest request,String loginName,String loginPwd,String entId){
		boolean success=true;
		String desc="登录成功";
		ResultPo result=new ResultPo(false,"登录失败");
		
		try{				
			if(StringUtils.isBlank(loginName) || StringUtils.isBlank(entId) || StringUtils.isBlank(loginPwd)){
				success=false;
				desc="参数信息不完整或为空";				
				return ResultPo.failed(new Exception("参数信息不完整或为空"));
			}
									
			// 验证企业存在
	    	boolean isExist=entService.existThisEntId(entId);
	    	if (!isExist) {
				success=false;
				desc="企业不存在";				
				return ResultPo.failed(new Exception("企业不存在"));
	    	}				    	
			DBObject obj=new BasicDBObject();
			obj.put("entId", entId);
			obj.put("loginName", loginName);
			
			PageInfo pageInfo = new PageInfo(0, 10);
		
	    	List<DBObject> list=userMongoService.queryUserList(obj, pageInfo);
	    	String userId = (String)list.get(0).get("userId");
	    	String userType = (String)list.get(0).get("userType");
			String password = (String)list.get(0).get("loginPwd");
	    	
			/*Des解密*/
			password=CdeskEncrptDes.decryptST(password);
	    	/*MD5加密*/
			password = DigestUtils.md5Hex(password);
			
    		/*登录签名*/
    		String sk=IosSignKeyUtils.getSignKey(userId);
    		
	    	if(!loginPwd.equalsIgnoreCase(password) || userType.equals(UserType.CUSTOMER.value)){
				success=false;
				desc="账号或密码不正确";			
				return ResultPo.failed(new Exception("账号或密码不正确"));
	    	}else{	    		
	    		DBObject rows=new BasicDBObject();
	    		rows.put("userId", userId);
	    		rows.put("sk",sk);
	    		rows.put("entId", entId);
	    		result=new ResultPo(success,desc,1,rows);
	    	}	    	
	    	SystemLogUtils.Debug(String.format("登陆成功返回,userId=%s,sk=%s,entId=%s", userId,sk,entId));
		}catch(Exception e){
			e.printStackTrace();
			success=false;
			desc=e.getMessage();
			result=new ResultPo(success,desc);
			
			SystemLogUtils.Debug(String.format("登录异常,loginName=%s,loginPwd=%s,entId=%s",loginName,loginPwd,entId));
		}
		
		return result;		
	}
}

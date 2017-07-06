package com.channelsoft.ems.androidApi.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.rpc.ServiceException;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.channelsoft.cri.logger.SystemLogUtils;
import com.channelsoft.ems.androidApi.service.IAndroidUserApiService;
import com.channelsoft.ems.androidApi.util.AndoridSignKeyUtils;
import com.channelsoft.ems.api.po.ResultPo;
import com.channelsoft.ems.ent.po.DatEntInfoPo;
import com.channelsoft.ems.redis.util.ParamUtils;
import com.channelsoft.ems.user.constant.LoginType;
import com.channelsoft.ems.user.po.DatEntUserPo;
import com.channelsoft.ems.user.service.IUserMongoService;
import com.mongodb.DBObject;

@Controller
@RequestMapping("/androidApi/user")
public class AndroidUserApiController {

	@Autowired
	IUserMongoService userMongoService;
	@Autowired
	IAndroidUserApiService androidUserApiService;
	
	/**
	 * 查询IM客户的客户资料
	 * @param request
	 * @param response
	 * @return
	 * @author dulei
	 * @time 2016年3月23日 下午4:23:43
	 */
	@ResponseBody
	@RequestMapping(value = "/queryIMUser")
	public ResultPo queryIMUser(HttpServletRequest request,HttpServletResponse response){
		//设置跨域访问
		response.addHeader("Access-Control-Allow-Origin","*");
		boolean success=true;
		String desc="查询成功";
		ResultPo result=new ResultPo(false,"查询失败");
		
		String entId=request.getParameter("entId"); //ccod的entid
		String source=request.getParameter("source"); //渠道类型
		String account=request.getParameter("account"); //用户账号
		String signKey=request.getParameter("signKey"); //签名
		SystemLogUtils.Debug(String.format("调用queryIMUser---查询客户资料,解析后: entId=%s,source=%s,account=%s", entId,source,account));
		try {
			if(StringUtils.isBlank(entId)||StringUtils.isBlank(source)||StringUtils.isBlank(account)){
				success=false;
				desc="企业ID、渠道、用户账号均不能为空";
			}
			if(!AndoridSignKeyUtils.validate(signKey)){
				//success=false;
				//desc="签名错误";
				return ResultPo.failed(new ServiceException("签名错误"));
			}
			if(success){
				/*List<DBObject> list = androidUserApiService.queryUser(entId, account, source);
				if(list!=null&&list.size()>0){
					result=new ResultPo(success,desc,list.size(),list);
				} else{
					success=false;
					desc="未查询到符合条件的客户资料";
					result=new ResultPo(success,desc);
				}*/
				DBObject user= androidUserApiService.queryUserOrAdd(entId, account, source);
				if(user!=null){
					result=new ResultPo(success,desc,1,user);
				} else{
					success=false;
					desc="未查询到符合条件的客户资料,创建失败";
					result=new ResultPo(success,desc);
				}
			} else{
				result=new ResultPo(success,desc);
			}
		} catch (Exception e){
			e.printStackTrace();
			success=false;
			desc=e.getMessage();
			result=new ResultPo(success,desc);
			SystemLogUtils.Debug(String.format("调用queryIMUser---查询客户资料,异常: entId=%s,source=%s,account=%s,success=%s,msg=%s", entId,source,account,success,e.getMessage()));
		}
		SystemLogUtils.Debug(String.format("调用queryIMUser---查询客户资料,返回: entId=%s,source=%s,account=%s,success=%s,desc=%s", entId,source,account,success,desc));
		
		return result;
	}
	
	/**
	 * 查询电话客户的客户资料
	 * @param request
	 * @param response
	 * @return
	 * @author dulei
	 * @time 2016年3月23日 下午4:24:08
	 */
	@ResponseBody
	@RequestMapping(value = "/queryTelUser")
	public ResultPo queryTelUser(HttpServletRequest request,HttpServletResponse response){
		response.addHeader("Access-Control-Allow-Origin","*");
		boolean success=true;
		String desc="查询成功";
		ResultPo result=new ResultPo(false,"查询失败");
		
		String entId=request.getParameter("entId"); //ccod的entid
		String telephone=request.getParameter("telephone"); //用户账号
		String signKey=request.getParameter("signKey"); //签名
		
		String source=LoginType.TELEPHONE.value; //渠道类型
		
		SystemLogUtils.Debug(String.format("调用queryTelUser---查询客户资料,解析后: entId=%s,telephone=%s", entId,telephone));
		try {
			if(StringUtils.isBlank(entId)||StringUtils.isBlank(telephone)){
				success=false;
				desc="企业ID、电话号码均不能为空";
			}
			if(!AndoridSignKeyUtils.validate(signKey)){
				
				return ResultPo.failed(new ServiceException("签名错误"));
			}
			
			if(success){
				/*List<DBObject> list = androidUserApiService.queryUser(entId, telephone, source);
				if(list!=null&&list.size()>0){
					result=new ResultPo(success,desc,list.size(),list);
				} else{
					success=false;
					desc="未查询到符合条件的客户资料";
					result=new ResultPo(success,desc);
				}*/
				DBObject user= androidUserApiService.queryUserOrAdd(entId, telephone, source);
				if(user!=null){
					result=new ResultPo(success,desc,1,user);
				} else{
					success=false;
					desc="未查询到符合条件的客户资料,创建失败";
					result=new ResultPo(success,desc);
				}
			} else{
				result=new ResultPo(success,desc);
			}
		} catch (Exception e){
			e.printStackTrace();
			success=false;
			desc=e.getMessage();
			result=new ResultPo(success,desc);
			SystemLogUtils.Debug(String.format("调用queryTelUser---查询客户资料,异常: entId=%s,telephone=%s,success=%s,msg=%s", entId,telephone,success,e.getMessage()));
		}
		SystemLogUtils.Debug(String.format("调用queryTelUser---查询客户资料,返回: entId=%s,telephone=%s,success=%s,desc=%s", entId,telephone,success,desc));
		
		return result;
	}
	
	/**
	 * 查询视频客户的客户信息
	 * @param request
	 * @param response
	 * @return
	 * @author dulei
	 * @time 2016年3月23日 下午4:24:23
	 */
	@ResponseBody
	@RequestMapping(value = "/queryVidUser")
	public ResultPo queryVidUser(HttpServletRequest request,HttpServletResponse response){
		response.addHeader("Access-Control-Allow-Origin","*");
		boolean success=true;
		String desc="查询成功";
		ResultPo result=new ResultPo(false,"查询失败");
		
		String entId=request.getParameter("entId"); //ccod的entid
		String vedioId=request.getParameter("vedioId"); //用户账号
		String signKey=request.getParameter("signKey"); //签名
		
		String source=""; //渠道类型
		
		SystemLogUtils.Debug(String.format("调用queryVidUser---查询客户资料,解析后: entId=%s,vedioId=%s", entId,vedioId));
		try {
			if(StringUtils.isBlank(entId)||StringUtils.isBlank(vedioId)){
				success=false;
				desc="企业ID、视频号码均不能为空";
			}
			if(!AndoridSignKeyUtils.validate(signKey)){
				
				return ResultPo.failed(new ServiceException("签名错误"));
			}
			
			if(success){
				//未实现-返回默认成功
				desc="默认查询成功";
				result=new ResultPo(success,desc,0,"");
				
			} else{
				result=new ResultPo(success,desc);
			}
		} catch (Exception e){
			e.printStackTrace();
			success=false;
			desc=e.getMessage();
			result=new ResultPo(success,desc);
			SystemLogUtils.Debug(String.format("调用queryVidUser---查询客户资料,异常: entId=%s,vedioId=%s,success=%s,msg=%s", entId,vedioId,success,e.getMessage()));
		}
		SystemLogUtils.Debug(String.format("调用queryVidUser---查询客户资料,返回: entId=%s,vedioId=%s,success=%s,desc=%s", entId,vedioId,success,desc));
		
		return result;
	}
	
	/**
	 * 客户资料修改接口
	 * @param request
	 * @param response
	 * @return
	 * @author dulei
	 * @time 2016年3月24日 上午9:55:30
	 */
	@ResponseBody
	@RequestMapping(value = "/updateUser")
	public ResultPo updateUser(HttpServletRequest request,HttpServletResponse response){
		response.addHeader("Access-Control-Allow-Origin","*");
		boolean success=true;
		String desc="修改成功";
		ResultPo result=new ResultPo(false,"修改失败");
		
		String entId=request.getParameter("entId");  //ccod的entid
		String userId=request.getParameter("userId");
		String userName=request.getParameter("userName");
		String telPhone=request.getParameter("telPhone");
		String fixedPhone=request.getParameter("fixedPhone");
		String email=request.getParameter("email");
		String label=request.getParameter("label");
		String userDesc=request.getParameter("userDesc");
		String signKey=request.getParameter("signKey"); //签名
		
		SystemLogUtils.Debug(String.format("调用updateUser---修改客户资料,解析后: entId=%s,userId=%s,userName=%s,telPhone=%s,fixedPhone=%s,email=%s,label=%s,userDesc=%s",
				entId,userId,userName,telPhone,fixedPhone,email,label,userDesc));
		
		try {
			if(StringUtils.isBlank(entId)||StringUtils.isBlank(userId)){
				success=false;
				desc="企业ID、用户编号均不能为空";
			}
			if(!AndoridSignKeyUtils.validate(signKey)){
				
				return ResultPo.failed(new ServiceException("签名错误"));
			}
			
			if(success){
				//ccodEntId 转为 系统内 entId
				DatEntInfoPo ent=ParamUtils.getDatEntInfoPo(entId);
				if(ent == null){
					return ResultPo.failed(new ServiceException("ccodEntId转换为entId失败，未查询到对应企业ID"));
				}
				entId = ent.getEntId();
				
				DatEntUserPo po=new DatEntUserPo();
				po.setEntId(entId); //ems的 entId
				po.setUserId(userId);
				po.setUserName(userName);
				po.setTelPhone(telPhone);
				po.setFixedPhone(fixedPhone);
				po.setEmail(email);
				po.setUserLabel(label);
				po.setUserDesc(userDesc);
				
				int num=userMongoService.updateUser(po);
				if(num<1){
					success=false;
					desc="执行后返回修改失败";
					result=new ResultPo(success,desc);
				} else{
					result=new ResultPo(success,desc,1,num);
				}
				
			} else{
				result=new ResultPo(success,desc);
			}
		} catch (Exception e){
			e.printStackTrace();
			success=false;
			desc=e.getMessage();
			result=new ResultPo(success,desc);
			SystemLogUtils.Debug(String.format("调用updateUser---修改客户资料,异常: entId=%s,userId=%s,userName=%s,telPhone=%s,fixedPhone=%s,email=%s,label=%s,userDesc=%s,success=%s,msg=%s",
					entId,userId,userName,telPhone,fixedPhone,email,label,userDesc,success,e.getMessage()));
		}
		SystemLogUtils.Debug(String.format("调用updateUser---修改客户资料,返回: entId=%s,userId=%s,userName=%s,telPhone=%s,fixedPhone=%s,email=%s,label=%s,userDesc=%s,success=%s,desc=%s",
				entId,userId,userName,telPhone,fixedPhone,email,label,userDesc,success,desc));
		
		return result;
	}
	
}

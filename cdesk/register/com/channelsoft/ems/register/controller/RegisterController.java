package com.channelsoft.ems.register.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.channelsoft.cri.controller.BaseController;
import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.util.WebappConfigUtil;
import com.channelsoft.cri.vo.AjaxResultPo;
import com.channelsoft.ems.api.client.MailSendClient;
import com.channelsoft.ems.ent.po.DatEntInfoPo;
import com.channelsoft.ems.ent.service.IDatEntService;
import com.channelsoft.ems.register.constants.EntSessionKeyConstants;
import com.channelsoft.ems.register.po.RegisterInfoPo;
import com.channelsoft.ems.register.service.IRegisterInfoService;
import com.channelsoft.ems.register.util.ConfigUtil;
import com.channelsoft.ems.register.util.SendEmail;

@Controller  
@RequestMapping("/register")
public class RegisterController extends BaseController{

	@Autowired
	IRegisterInfoService registerService;
	
	@Autowired
	IDatEntService entService;
	
       /*企业注册首页*/
	   @RequestMapping(value = "/gotoRegister")
	   	public String gotoRegister(Model model,HttpServletRequest request, HttpServletResponse response) throws Exception {		   
		    
		    String ip = getIpAddr(request);
		    
		    model.addAttribute("step", "step1");		    
		    model.addAttribute("ip",ip);
            
		    return "register/register";
	   	}
	   
	   /*注册第一步*/
	   @ResponseBody
	   @RequestMapping(value = "/registerStep1")
	    public AjaxResultPo register(RegisterInfoPo register, HttpServletRequest request,HttpServletResponse response) throws Exception{
	    	
		   AjaxResultPo ret = new AjaxResultPo(true, "成功");		   
		   boolean b = entService.existThisMail(register.getEmail());
		   if(b){					
				ret.setSuccess(false);
				ret.setMsg("此邮箱已注册！");
				return ret;
			}
	        try {
		        String email = request.getParameter("email");
	        	String picCode = request.getParameter("picCode");
	        	String ip = request.getParameter("ip");
	        	
	        	register.setEmail(email);
	        	register.setPicCode(picCode);
	        	register.setIp(ip);
	        	
	        	String checkcode = (String)request.getSession().getAttribute(EntSessionKeyConstants.ENT_REGIST_PIC_CODE);
	        	
	        if(checkcode!=null){
	        	if(!checkcode.equalsIgnoreCase(register.getPicCode())){
	        		ret.setSuccess(false);
	        		ret.setMsg("验证码错误");
	        		return ret;
	        	}else{
					registerService.doRegister(register,request);/*发邮箱激活 */
	        	}
	        }else{
        		ret.setSuccess(false);
        		ret.setMsg("会话过期，请重新注册！");
        		return ret;
	        }       	
			} catch (ServiceException e) {
				e.printStackTrace();
				ret.setSuccess(false);
				ret.setMsg(e.getMessage());
				return ret;
			}catch(Exception e){
				e.printStackTrace();
				ret.setSuccess(false);
				ret.setMsg("未知异常");
				return ret;
			}	        
	        return ret;
	    }  
	   
	   @ResponseBody
	   @RequestMapping(value = "/register2")
	    public AjaxResultPo register2(DatEntInfoPo ent,RegisterInfoPo registerPo, HttpServletRequest request,HttpServletResponse response) throws Exception{
	    	
		   AjaxResultPo ret = new AjaxResultPo(true, "成功");
	        try {
	        	String register = request.getParameter("register");
	        	String email = request.getParameter("email");
		        String password = request.getParameter("password");
	        	String entName = request.getParameter("entName");
	        	String domainName = request.getParameter("domainName").toLowerCase();
	            String activeCode = request.getParameter("activeCode");
	        
	        	ent.setRegister(register);
	        	ent.setEmail(email);
	        	ent.setPassword(password);
	        	ent.setEntName(entName);
	        	ent.setDomainName(domainName);
	            ent.setStatus("1");
	            ent.setEntId(domainName);
	            ent.setCreatorName(register);
	            ent.setUpdatorName(register);	            
	            registerPo.setActiveCode(activeCode);
	            									
				request.setAttribute("activeCode", activeCode);
				/*传入登录信息*/
				JSONObject json=new JSONObject();
				json.put("entId", domainName);
				json.put("loginName", email);
				json.put("password", password);
				json.put("email", email);
					
	            entService.addEntInfo(ent);	
	            registerService.updateRegister(registerPo);	            
	            /*发送邮件*/   
//				SendEmail.send("欢迎加入青牛云客服，改善你的客户服务", email,
//						emailContent);
	            
//	    	    String path1=domainName+".cdesk.com:8080";
//	    	    String path2=request.getContextPath();		    	    
//	    	    String path="http://"+path1+path2+"/";		    
	            String path=WebappConfigUtil.getParameter("CDESK_ROOT").replace("entId", domainName);
	            
	    		Object[] os = new Object[] { path };
				String emailContent = ConfigUtil.getString("success.email.content");		
	    		emailContent = MessageFormat.format(emailContent, os);
	            
	            MailSendClient.sendEntRegisterMail(domainName, email, "欢迎加入青牛云客服，改善你的客户服务", emailContent);
				String rows=json.toString();
				ret = AjaxResultPo.success("成功", 1, rows);
			
			} catch (ServiceException e) {
				e.printStackTrace();
				ret.setSuccess(false);
				ret.setMsg(e.getMessage());
				return ret;
			}catch(Exception e){
				e.printStackTrace();
				ret.setSuccess(false);
				ret.setMsg("未知异常");
				return ret;
			}	       
	        return ret;
	    }  
	   
      /* 跳转到注册第二步（邮件链接来的）*/
	   @RequestMapping(value = "/registerStep2")
	   	public String registerStep2(Model model,HttpServletRequest request, HttpServletResponse response) throws Exception {	       
		   long interval = 0;
	       boolean invalid = false;
	       boolean outTime = false;	
		   /*判断激活码是否失效*/
		   String activeCode = request.getParameter("activeCode");
		   String createTime = registerService.queryCreateTime(activeCode);
		   
		   String email=registerService.queryEmail(activeCode);
		   /*获取同一邮箱的最近一条激活码*/
		   String lastCode=registerService.queryLastCode(email);
		   
		   System.out.println("activeCode="+activeCode+",lastCode="+lastCode);
		   
		 if(!lastCode.equals(activeCode)){
			 outTime = true;
		 }else{
		   
		 if(createTime!=""){		   
		   Date date = new Date();
		   SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		   String now = format.format(date); 
		   
		   Date beginTime = format.parse(createTime);  
	       Date endTime = format.parse(now);  		   	       
	       interval = endTime.getTime() - beginTime.getTime();
	       
	       if(interval >= 1000*60*30){
	    	   invalid = true;
	       }
	       System.out.println(interval+","+interval/60000+","+invalid);
		 }else{
			invalid = true; 
		 }
		 }
		   String domainLast="."+WebappConfigUtil.getParameter("DOMAIN_NAME");
		   
		   model.addAttribute("domainLast",domainLast);
	       model.addAttribute("step", "step2");	
	       request.setAttribute("invalid", invalid);
	       request.setAttribute("outTime", outTime);
	       request.setAttribute("activeCode", activeCode);
		   return "register/register";
	   	}
	    
	  /* 跳转到正在处理页面*/
	   @RequestMapping(value = "/registerStep3")
	   	public String registerStep3(String ename,String register,String email,String password,String entName,String domainName,String activeCode,HttpServletRequest request) throws Exception {		  		   
		   request.setAttribute("step", "step3");	  
		   request.setAttribute("register", register);
		   request.setAttribute("email", email);
		   request.setAttribute("password", password);
		   request.setAttribute("entName", entName);
		   request.setAttribute("domainName", domainName);
           request.setAttribute("activeCode", activeCode);
		   
		   String domainLast="."+WebappConfigUtil.getParameter("DOMAIN_NAME");
       	   boolean check = entService.existThisMail(email);
       	   request.setAttribute("check", check);
       	   request.setAttribute("domainLast", domainLast);

		   return "register/register";
	   	}
	   	
	   /*通过激活码查询邮箱*/
		@ResponseBody
		@RequestMapping(value = "/queryEmail")
		public AjaxResultPo queryEmail(String activeCode, HttpServletRequest request,HttpServletResponse response,Model model) throws Exception{
			AjaxResultPo ret = new AjaxResultPo(true, "成功");
			try{
			   String email = registerService.queryEmail(activeCode);
			   ret.setRows(email);	   
			}catch(ServiceException e){
				e.printStackTrace();
				ret.setSuccess(false);
				ret.setMsg(e.getMessage());
				return ret;
			}				
			return ret;
		}
		
		/*检查邮箱是否已注册*/
		@ResponseBody
		@RequestMapping(value = "/checkEmail")
		public AjaxResultPo checkEmail(String email, HttpServletRequest request,HttpServletResponse response,Model model) throws Exception{
			try {				
				boolean b = entService.existThisMail(email);
				if(b){					
					return new AjaxResultPo(false, "此邮箱已经注册！");
				}else{
					return AjaxResultPo.successDefault();	
				}				
			} catch (ServiceException e) {
				e.printStackTrace();
				return new AjaxResultPo(false, e.getMessage());
			}
		}
			
        /*   获取当前网络ip  */  
        public String getIpAddr(HttpServletRequest request){  
            String ipAddress = request.getHeader("x-forwarded-for");  
                if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {  
                    ipAddress = request.getHeader("Proxy-Client-IP");  
                }  
                if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {  
                    ipAddress = request.getHeader("WL-Proxy-Client-IP");  
                }  
                if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {  
                    ipAddress = request.getRemoteAddr();  
                    if(ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")){  
                        //根据网卡取本机配置的IP  
                        InetAddress inet=null;  
                        try {  
                            inet = InetAddress.getLocalHost();  
                        } catch (UnknownHostException e) {  
                            e.printStackTrace();  
                        }  
                        ipAddress= inet.getHostAddress();  
                    }  
                }  
                //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割  
                if(ipAddress!=null && ipAddress.length()>15){ //"***.***.***.***".length() = 15  
                    if(ipAddress.indexOf(",")>0){  
                        ipAddress = ipAddress.substring(0,ipAddress.indexOf(","));  
                    }  
                }  
                return ipAddress;   
        } 
        
        /*服务条款*/
 	    @RequestMapping(value = "/policy")
 	   	public String serviceTerms(Model model,HttpServletRequest request, HttpServletResponse response) throws Exception {		   
             
 		    return "register/serviceTerms";
 	   	}
}

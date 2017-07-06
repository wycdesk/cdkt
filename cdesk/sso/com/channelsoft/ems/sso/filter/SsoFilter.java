package com.channelsoft.ems.sso.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.channelsoft.cri.common.BaseObject;
import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.logger.LoginLogUtils;
import com.channelsoft.cri.util.CdeskEncrptDes;
import com.channelsoft.cri.util.DomainUtils;
import com.channelsoft.cri.util.HttpPostUtils;
import com.channelsoft.cri.util.JsonUtils;
import com.channelsoft.cri.vo.AjaxResultPo;
import com.channelsoft.ems.sso.client.SsoClient;
import com.channelsoft.ems.sso.constant.SsoParamConstants;
import com.channelsoft.ems.sso.util.SsoCookieUtils;
import com.channelsoft.ems.sso.util.SsoSessionUtils;

public class SsoFilter extends BaseObject implements Filter {

	
	@Override
	public void destroy() {
	}

	private void responseResult(HttpServletRequest request,
			HttpServletResponse response, String msg) throws IOException {
		String accept = request.getHeader("Accept");
		if (accept.indexOf("json") > 0) {
			response.setContentType("text/json; charset=UTF-8");
			response.getWriter().print(
					JsonUtils.toJson(new AjaxResultPo(false, msg)));
		} else {
			response.setContentType("text/html; charset=UTF-8");
			response.getWriter().print(msg);
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;


		/**
		 * 相对路径
		 */
		String url = StringUtils.substringAfter(req.getRequestURI(),
				req.getContextPath());
		
		if (this.isIgnoreUrl(url)) {
			chain.doFilter(request, response);
			return;
		}

		String sessionKey = req.getParameter("sessionKey");
//		String enterpriseid = req.getParameter("enterpriseid");
    	String enterpriseid=DomainUtils.getEntId(req);

		 
		/**
		 * 未登录：调用PlatformSwitch接口
		 */
		if (!SsoSessionUtils.isHasLogin(req)) {
			this.logDebug("未登录，获取到sessionKey:" + sessionKey + "," + url);

			/**
			 * 未登录且SessionKey参数为空，排除假超时，跳转到登陆界面。
			 */
			if (StringUtils.isBlank(sessionKey)) {
				// 解决第一次访问列表页,两个url先后访问,假超时情况
				if (req.getMethod().equalsIgnoreCase("post")
						&& req.getHeader("Accept").indexOf("json") > 0) {
					this.logDebug("json post" + url);
					chain.doFilter(request, response);
				 
					return;
				}
				// 尝试自动登录
				if (autoLogin(req)) {
					chain.doFilter(request, response);
					return;
				}
				this.logDebug("没有登录或会话过期,请重新登录" + "," + url);
				this.redirect(req, res);
				return;
			}

			try {
				SsoClient.platformSwitch(enterpriseid, sessionKey, SsoParamConstants.PLATFORM_ID, req);
			} catch (ServiceException e) {
				LoginLogUtils.LoginFail(sessionKey, SsoParamConstants.PLATFORM_ID, e);
				this.redirect(req, res);
				return;
			}
		}
		/**
		 * 已登陆：判断SessionKey参数是否一致，不一致的话需要重新鉴权
		 */
		if (StringUtils.isNotBlank(sessionKey)) {
			String sKey = SsoSessionUtils.getSessionKey(req);

	 
			if (StringUtils.isNotBlank(sessionKey)
					&& !sessionKey.equalsIgnoreCase(sKey)) {

				try {
					SsoClient.platformSwitch(enterpriseid, sessionKey,
							SsoParamConstants.PLATFORM_ID, req);
					 
				} catch (ServiceException e) {
					e.printStackTrace();
					LoginLogUtils.LoginFail(sessionKey,
							SsoParamConstants.PLATFORM_ID, e);
					SsoClient.logout(enterpriseid, sessionKey,
							SsoParamConstants.PLATFORM_ID, req);
					this.redirect(req, res);
					return;
				}
			}
		}

		/**
		 * SessionKey鉴权通过，判断是否有页面权限
		 */
		do {
			int r = SsoSessionUtils.hasPrivilege(SsoParamConstants.PLATFORM_ID
					+ "_" + url, req);
			/**
			 * 有权限
			 */
			if (r == 0) {
				chain.doFilter(request, response);
				return;
			}
			/**
			 * 有此项配置，但无权限
			 */
			else if (r == 2) {
				this.logDebug("您没有相应操作权限,请与管理员联系:" + url);
				responseResult(req, res, "您没有相应操作权限,请与管理员联系!");
				return;
			}
			/**
			 * 无此项配置，查看上一级
			 */
			else if (r == 1) {
				url = StringUtils.substringBeforeLast(url, "/"); // 寻找上一级
			} else {
				this.redirect(req, res);
				return;
			}
		} while (!url.isEmpty());

		chain.doFilter(request, response);
	}

	/**
	 * 如果以前登录时有勾选下次自动登录，则取出cookie保存的账号，进行自动登录
	 * @param request
	 * @return false 自动登录失败 ture 自动登录成功
	 * @author zhangtie
	 */
	private boolean autoLogin(HttpServletRequest request) {
		try {
			String entId = SsoCookieUtils.getCookieEntId(request);
			String loginName = SsoCookieUtils.getCookieLoginName(request);
			String password = SsoCookieUtils.getCookiePassword(request);
			if (StringUtils.isBlank(entId)
					|| StringUtils.isBlank(loginName)
					|| StringUtils.isBlank(password)) {
				return false;
			}
//			String MD5Password = DigestUtils.md5Hex(password.trim());
			String desPassword = CdeskEncrptDes.encryptST(password.trim());
			String autoLoginSuccess="";
			try {
				Map<String, String> paramMap=new HashMap<String, String>();
				paramMap.put("enterpriseid", entId);
				paramMap.put("loginName", loginName);
				paramMap.put("MD5Password", desPassword);
				paramMap.put("platformId", SsoParamConstants.PLATFORM_ID);
				autoLoginSuccess = HttpPostUtils.httpPost(SsoParamConstants.SSO_AUTOLOGIN_FILTER_URL, paramMap);
				if("true".equals(autoLoginSuccess)) return true;
				return false;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 判断url是否在忽略列表，支持*号。
	 * 
	 * @param url
	 * @return
	 * @CreateDate: 2013-6-8 下午12:35:14
	 * @author 魏铭
	 */
	private boolean isIgnoreUrl(String url) {
		if (url.equalsIgnoreCase("/")) {
			return true;
		}

		String subfix = StringUtils.substringAfterLast(url, ".");
		if (StringUtils.isNotBlank(SsoParamConstants.IGNORE_SUFFIX_URL)
				&& StringUtils.isNotBlank(subfix)) {
			if (SsoParamConstants.IGNORE_SUFFIX_URL.indexOf(subfix) >= 0) {
				return true;
			}
		}

		if (StringUtils.isNotBlank(SsoParamConstants.IGNORE_PRIVILEGE_URL)) {

			String[] ignore = SsoParamConstants.IGNORE_PRIVILEGE_URL.split(",");
			for (String ignoreUrl : ignore) {
				String trimUrl = ignoreUrl.trim();
				if (trimUrl.endsWith("*")) {
					trimUrl = trimUrl.substring(0, trimUrl.length() - 1);
					if (url.startsWith(trimUrl)) {
						return true;
					}
				} else if (trimUrl.equals(url)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 在response中重写提示并跳转到登陆界面
	 * 
	 * @param res
	 * @throws IOException
	 * @CreateDate: 2013-6-8 下午12:26:38
	 * @author 魏铭
	 */
	private void redirect(HttpServletRequest req, HttpServletResponse res) throws IOException {
		   /**
         * wangyong add 处理ajax超时
         */
   		String requestType=((HttpServletRequest)req).getHeader("X-Requested-With");
   		String callback=req.getParameter("callback");
		if (requestType != null && requestType.equals("XMLHttpRequest")) {
			System.out.println("ajax request,session timeout,please login again!");
			res.setContentType("text/html; charset=UTF-8");
			String str="{\"success\":false,\"msg\":\"没有登录或会话过期,请重新登录\"}";
			try {
				res.getWriter().print(str);
				return;		
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        } 
		//ajax跨域请求
		else if(StringUtils.isNotBlank(callback)&&callback.indexOf("jQuery")==0){
			System.out.println("ajax jsonp request,session timeout,please login again!");
			PrintWriter out = null;
			try {
				res.setContentType("text/html;charset=UTF-8");
				out = res.getWriter();
	            StringBuffer data = new StringBuffer();
	            data.append("{")
	                .append("success:"+false).append(",errorType:\"sessionTimeOut\",").append("msg:\""+"没有登录或会话过期,请重新登录"+"\"").append("}");
				out.write(callback + "("+data+")");
				out.flush();
			}
			catch (Exception e) {
				e.printStackTrace();
			} finally {
				if(out !=null) {
					try {
						out.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		res.setContentType("text/html; charset=UTF-8");
		String redirectScript = "<script type=\"text/javascript\">alert(\"没有登录或会话过期,请重新登录\"); top.location.href=\"http://"
				+ req.getServerName() + ":" + req.getServerPort() + SsoParamConstants.ENT_LOGIN_INDEX + "\"; </script>";
		res.getWriter().print(redirectScript);
		 
	}
	
	

	/**
	 * 初始化过滤器参数
	 * 
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig fConfig) throws ServletException {
		SsoParamConstants.PLATFORM_ID = fConfig.getInitParameter("PLATFORM_ID")
				.trim();
		SsoParamConstants.IGNORE_PRIVILEGE_URL = fConfig.getInitParameter(
				"IGNORE_PRIVILEGE_URL").trim();

		SsoParamConstants.ENT_LOGIN_INDEX = fConfig.getInitParameter(
				"ENT_LOGIN_INDEX").trim();
		String suffix = fConfig.getInitParameter("IGNORE_SUFFIX_URL");
		if (StringUtils.isNotBlank(suffix)) {
			SsoParamConstants.IGNORE_SUFFIX_URL = suffix.trim();
		}

		String ssoServerUrl = fConfig.getInitParameter("SSO_SERVER_URL");
		if (StringUtils.isNotBlank(ssoServerUrl)) {
			SsoParamConstants.SSO_LOGIN_URL = ssoServerUrl + "/login";
			SsoParamConstants.SSO_AUTOLOGIN_FILTER_URL = ssoServerUrl + "/loginForFilter";
			SsoParamConstants.SSO_LOGOUT_URL = ssoServerUrl + "/logout";
			SsoParamConstants.SSO_UNIT_NAME_URL = ssoServerUrl + "/unitName";
			SsoParamConstants.SSO_LOGIN_AREA = ssoServerUrl + "/area";
			SsoParamConstants.SSO_LOGIN_DOMAIN = ssoServerUrl + "/domain";
			SsoParamConstants.SSO_LOGIN_ROLE = ssoServerUrl + "/role";
			SsoParamConstants.SSO_LOGIN_ALLDOMAIN = ssoServerUrl + "/allDomain";
			SsoParamConstants.SSO_LOGIN_MENULIST = ssoServerUrl + "/menuList";
			SsoParamConstants.SSO_LOGIN_PRIVILEGELIST = ssoServerUrl
					+ "/privilegeList";
			SsoParamConstants.SSO_LOGIN_USERINFO = ssoServerUrl + "/userInfo";
			SsoParamConstants.SSO_LOGIN_ENT = ssoServerUrl + "/ent";
			SsoParamConstants.SSO_LOGIN_DEPARTMENT = ssoServerUrl
					+ "/department";

		}

		SsoParamConstants.REDIRECT_SCRIPT = "<script type=\"text/javascript\">notice.alert(\"没有登录或会话过期,请重新登录\"); top.location.href=\""
				+ SsoParamConstants.ENT_LOGIN_INDEX + "\"; </script>";
	}

}

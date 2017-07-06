package com.channelsoft.cri.exception;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import com.channelsoft.cri.constant.ServiceErrorCode;
import com.channelsoft.cri.exception.BaseException;
import com.channelsoft.cri.logger.SystemLogUtils;
import com.channelsoft.cri.logger.logger.SystemLogger;


public class ExceptionResolver extends SimpleMappingExceptionResolver {

	public static final String DEFAULT_EXCEPTION_ATTRIBUTE = "exception";
	private String defaultErrorView;
	private String ajaxErrorView;

	public void setDefaultErrorView(String defaultErrorView)
	{
	    this.defaultErrorView = defaultErrorView;
	}

	public void setAjaxErrorView(String ajaxErrorView) {
		this.ajaxErrorView = ajaxErrorView;
	}
	
	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
	{
		SystemLogger.error(ex.getMessage());
		String errorKey = this.getErrorKey(ex);
		//String locale = UserInfoUtils.getLocale(request);
		
		if (request.getHeader("X-Requested-With") == null) {
			request.setAttribute("errorKey", errorKey);
			return getModelAndView(defaultErrorView);
		} else {
			//String errorMessage = FmtMessageUtils.getMessage(locale, errorKey, "SYSTEM_ERROR");
			request.setAttribute("errorMessage", ex.getMessage());
			return getModelAndView(ajaxErrorView);
		}
	}

	private String getErrorKey(Exception ex) {
		if (ex instanceof ServiceException) {
			return ((BaseException)ex).code;
		
		} else {
			ex.printStackTrace();
			return ServiceErrorCode.GENERAL_ERROR.key;
		}
		
	}

	protected ModelAndView getModelAndView(String viewName, HttpServletRequest request)
	{
	    return getModelAndView(viewName);
	}

	protected ModelAndView getModelAndView(String viewName)
	{
	    ModelAndView mv = new ModelAndView(viewName);
	    mv.addObject("testAAA", "asdsad");
	    return mv;
	}
	
	protected ModelAndView getAjaxModel(String msg)
	{
		ModelMap modelMap = new ModelMap();
	    modelMap.addAttribute("success", "false");
	    modelMap.addAttribute("total", "0");
	    modelMap.addAttribute("rows", null);
	    modelMap.addAttribute("msg", msg);
	    return new ModelAndView("", modelMap);
	}
}

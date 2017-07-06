package com.channelsoft.ems.sso.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.channelsoft.cri.common.BaseObject;
import com.channelsoft.cri.util.HttpPostUtils;
import com.channelsoft.ems.sso.constant.SsoParamConstants;
import com.channelsoft.ems.sso.util.SsoSessionUtils;
import com.channelsoft.ems.sso.vo.LoginResultVo;

/**
 * Application Lifecycle Listener implementation class UserSessionListener
 *
 */
public class UserSessionListener extends BaseObject implements HttpSessionListener {

    /**
     * Default constructor. 
     */
    public UserSessionListener() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see HttpSessionListener#sessionCreated(HttpSessionEvent)
     */
    public void sessionCreated(HttpSessionEvent arg0) {
        // TODO Auto-generated method stub
    }

	/**
     * @see HttpSessionListener#sessionDestroyed(HttpSessionEvent)
     */
    public void sessionDestroyed(HttpSessionEvent event){
    	Map<String, String> postMap = new HashMap<String, String>();
    	String sessionKey = (String) event.getSession().getAttribute(SsoSessionUtils.CLIENT_SESSION_SESSION_KEY);
    	if (sessionKey == null) {
    		return;
    	}
    	try {
	    	LoginResultVo loginResult = (LoginResultVo) event.getSession().getAttribute(SsoSessionUtils.CLIENT_SESSION_LOGIN_RESULT);
			postMap.put("sessionKey", sessionKey);
			postMap.put("enterpriseid", loginResult.getEntInfo().getEntId());
			postMap.put("platformId", SsoParamConstants.PLATFORM_ID);
			HttpPostUtils.httpPost(SsoParamConstants.SSO_LOGOUT_URL, postMap);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	this.logDebug("SessionKey失效:" + sessionKey);
    }
	
}

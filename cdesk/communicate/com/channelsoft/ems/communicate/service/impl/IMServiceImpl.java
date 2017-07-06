package com.channelsoft.ems.communicate.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.ems.communicate.constant.IMSource;
import com.channelsoft.ems.communicate.constant.WorkSource;
import com.channelsoft.ems.communicate.service.ICommService;
import com.channelsoft.ems.communicate.service.IIMService;
import com.channelsoft.ems.sso.vo.SsoUserVo;
import com.channelsoft.ems.user.constant.LoginType;
import com.channelsoft.ems.user.po.DatEntUserPo;
import com.channelsoft.ems.user.service.IUserMongoService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class IMServiceImpl implements IIMService {
	
	@Autowired
	ICommService commService;

	@Autowired
	IUserMongoService userMongoService;
	
	@Override
	public DatEntUserPo queryOrAddUser(SsoUserVo user, String userAccount, String startTime, String imSource) throws ServiceException {
		// TODO Auto-generated method stub
		String source = "";
		String userAccountType = "";
		
		if(IMSource.WEBCHAT.key.equals(imSource)){
			source = WorkSource.WEBCHAT.key;
			userAccountType = LoginType.IM.value;
		}else if(IMSource.WECHAT.key.equals(imSource)){
			source = WorkSource.WECHAT.key;
			userAccountType = LoginType.WECHAT.value;
		}else if(IMSource.VIDEO.key.equals(imSource)){
			source = WorkSource.VIDEO.key;
			userAccountType = LoginType.VIDEO.value;
		}else{
			source = WorkSource.WEBCHAT.key;
			userAccountType = LoginType.IM.value;
		}
		DatEntUserPo userPo=null;
		String entId = user.getEntId();
		//判断客户是否存在
		/*String customerId = request.getParameter("customerId");
		userPo = userMongoService.queryUserById(entId, customerId);
		if(userPo!=null){
			saveIMComm(request,userPo);
			return userPo;
		}*/
		//用户不存在则创建用户
		//由于目前source不统一，所以登陆这里暂时写死，定为IM，其他渠道暂时先不调用
		userPo = userMongoService.queryOrAddUser(entId, userAccount, userAccountType);
		saveIMComm(startTime,userPo, user, source);
		return userPo;
	}
	
	public void saveIMComm(String startTimeStr,DatEntUserPo userPo, SsoUserVo agent, String source) throws ServiceException{
		//SsoUserVo agent=SsoSessionUtils.getUserInfo(request);
		if(agent==null){
			throw new ServiceException("坐席信息为空");
		}
		try {
			DBObject commObj=new BasicDBObject();
			commObj.put("userId", userPo.getUserId());
			commObj.put("loginName", userPo.getLoginName());
			commObj.put("userName", userPo.getUserName());
			commObj.put("opId", agent.getUserId());
			commObj.put("opName", agent.getUserName());
			commObj.put("ccodAgentId", agent.getUserId());
			commObj.put("ccodAgentName", agent.getUserName());
			commObj.put("ccodEntId", agent.getCcodEntId());
			commObj.put("source", source);
			Date startTime=new Date(Long.valueOf(startTimeStr));
			commObj.put("startTime", startTime);
			String commId=commService.saveIMComm(userPo.getEntId(), commObj);
			userPo.setCommId(commId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
		
	}

	
}

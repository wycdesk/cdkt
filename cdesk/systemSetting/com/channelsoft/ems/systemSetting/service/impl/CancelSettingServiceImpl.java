package com.channelsoft.ems.systemSetting.service.impl;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import com.channelsoft.cri.exception.BaseException;
import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.logger.ManageLogUtils;
import com.channelsoft.cri.logger.SystemLogUtils;
import com.channelsoft.cri.util.DomainUtils;
import com.channelsoft.cri.util.HttpPostUtils;
import com.channelsoft.cri.util.WebappConfigUtil;
import com.channelsoft.ems.api.client.EntClient;
import com.channelsoft.ems.api.client.MailSendClient;
import com.channelsoft.ems.ent.dao.IDatEntDao;
import com.channelsoft.ems.field.dao.IUserFieldDao;
import com.channelsoft.ems.systemSetting.service.ICancelSettingService;
import com.channelsoft.ems.user.dao.IUserMongoDao;

public class CancelSettingServiceImpl implements ICancelSettingService {

	@Autowired
	IDatEntDao datEntDao;
	@Autowired
	IUserMongoDao userMongoDao;
	@Autowired
	IUserFieldDao userFieldDao;
	
	@Transactional
	@Override
	public int goCancel(HttpServletRequest request,String entId, String userType) throws ServiceException {
		String entIdR=DomainUtils.getEntId(request);
		if(!entIdR.equals(entId)){
			throw new ServiceException("企业不一致，不能销户");
		}
		if(Integer.valueOf(userType)<3){
			throw new ServiceException("权限不够");
		}
		String returnData=null;
		try {
			returnData=EntClient.destroyEnt(entId);
			System.out.println("========接口调用方：=======");
			System.out.println(returnData);
		} catch (Exception e1) {
			ManageLogUtils.DeleteFail(request, new BaseException(e1.getMessage()),
					"企业注销", entId, "entId="+entId);
			e1.printStackTrace();
			throw new ServiceException("调用mongo接口失败");
		}
		JSONObject json=null;
		int flag=0;
		int delEnt=0;
		int delDB=0;
		if(StringUtils.isNotBlank(returnData)){
			try {
				json=new JSONObject(returnData);
				if(json.has("flag")){
					flag=json.getInt("flag");
				}
			} catch (JSONException e) {
				e.printStackTrace();
				throw new ServiceException("删除mongo数据返回有错");
			}
		}else{
			flag=-1;
		}
		if(flag!=-1){
			ManageLogUtils.DeleteSuccess(request, "删除mongo数据", entId, "entId="+entId);
			int resultM=0;
			String data=null;
			try {
				data=MailSendClient.removeDns(entId);
			} catch (Exception e1) {
				e1.printStackTrace();
				throw new ServiceException("删除邮箱失败");
			}
			JSONObject jsonM=null;
			if(StringUtils.isNotBlank(data)){
				try {
					jsonM=new JSONObject(data);
					resultM = jsonM.getInt("result");
				} catch (JSONException e) {
					e.printStackTrace();
					throw new ServiceException("删除企业邮箱返回有错");
				}
			}else{
				resultM=1;
			}
			if(resultM==0){
				ManageLogUtils.DeleteSuccess(request, "删除企业的邮箱", entId, "entId="+entId);
				try {
					this.dropEntInfoForCdesk(entId);
					delEnt=datEntDao.deleteEntInfo(entId);
					delDB=datEntDao.deleteDB(entId);
				} catch (DataAccessException e) {
					ManageLogUtils.DeleteFail(request, new BaseException(e.getMessage()),
							"企业注销", entId, "entId="+entId);
					e.printStackTrace();
					throw new ServiceException("企业销户数据库异常");
				}
			}else{
				ManageLogUtils.DeleteFail(request, new BaseException("企业邮箱删除失败"),
						"企业注销", entId, "entId="+entId);
				throw new ServiceException("企业邮箱删除失败");
			}
		}else{
			ManageLogUtils.DeleteFail(request, new BaseException("mongoDB中删除失败"),
					"企业注销", entId, "entId="+entId);
			throw new ServiceException("mongoDB中删除失败");
		}
		if(delEnt>0&&delDB>0){
			ManageLogUtils.DeleteSuccess(request, "删除平台库中的企业信息", entId, "entId="+entId);
			ManageLogUtils.DeleteSuccess(request, "删除企业数据库", entId, "entId="+entId);
			return 1;
		}else{
			ManageLogUtils.DeleteFail(request, new BaseException("删除数据库失败"),
					"企业注销", entId, "entId="+entId);
			return 0;
		}
		
	}

	@Override
	public int dropEntInfoForCdesk(String entId) throws ServiceException {
		// TODO Auto-generated method stub
		userMongoDao.dropUserTable(entId);
		userFieldDao.dropUserFieldTable(entId);
		return 1;
	}

	@Transactional
	@Override
	public int deleteEnterprise(String entId) throws ServiceException {
		try {
			//调用调用工单项目中的接口注销企业
			SystemLogUtils.Debug(String.format("========调用工单项目中的接口注销企业=======entId=%s", entId));
			String returnData=EntClient.destroyEnt(entId);
			
			JSONObject json=new JSONObject(returnData);
			
			//工单系统企业注销结果判断
			if(json.getInt("flag")!=-1){
				SystemLogUtils.Debug(String.format("========调用工单项目中的接口注销企业成功=======entId=%s，data=%s", entId,returnData));
			}else{
				SystemLogUtils.Debug(String.format("========工单项目中的企业注销失败=======entId=%s，data=%s", entId,returnData));
			}
			
			//调用邮箱接口删除企业邮箱
			SystemLogUtils.Debug(String.format("========准备调用邮箱接口删除企业邮箱=======entId=%s", entId));
			String dataM=MailSendClient.removeDns(entId);
			JSONObject jsonM=new JSONObject(dataM);
			
			//删除企业邮箱结果判断
			if(jsonM.getInt("result")==0){
				SystemLogUtils.Debug(String.format("========删除企业邮箱成功=======entId=%s,data=%s", entId,returnData));
			}else{
				SystemLogUtils.Debug(String.format("========删除企业邮箱失败=======entId=%s，data=%s", entId,returnData));
			}
			
			//删除平台库中的企业信息和企业数据库
			SystemLogUtils.Debug(String.format("========准备删除平台库中的企业信息和企业数据库=======entId=%s", entId));
			this.dropEntInfoForCdesk(entId);
			datEntDao.deleteEntInfo(entId);
			datEntDao.deleteDB(entId);
			
			SystemLogUtils.Debug(String.format("========企业注销成功=======entId=%s", entId));
			return 1;
		} catch (Exception e) {
			SystemLogUtils.Debug(String.format("========企业注销失败=======entId=%s", entId));
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}

}

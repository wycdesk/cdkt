package com.channelsoft.ems.user.service.impl;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import com.channelsoft.cri.constant.BaseErrCode;
import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.logger.ManageLogUtils;
import com.channelsoft.cri.util.CdeskEncrptDes;
import com.channelsoft.cri.util.DBObjectUtils;
import com.channelsoft.cri.vo.AjaxResultPo;
import com.channelsoft.ems.api.client.CCODClient;
import com.channelsoft.ems.api.client.CchatClient;
import com.channelsoft.ems.api.client.MailSendClient;
import com.channelsoft.ems.api.constants.AgentRoleType;
import com.channelsoft.ems.api.po.CCODRequestPo;
import com.channelsoft.ems.api.po.SendMainResponsePo;
import com.channelsoft.ems.ent.po.DatEntInfoPo;
import com.channelsoft.ems.ent.service.IDatEntService;
import com.channelsoft.ems.register.po.RegisterInfoPo;
import com.channelsoft.ems.register.util.ConfigUtil;
import com.channelsoft.ems.sso.vo.SsoUserVo;
import com.channelsoft.ems.user.constant.UserType;
import com.channelsoft.ems.user.dao.IUserDao;
import com.channelsoft.ems.user.po.DatEntUserPo;
import com.channelsoft.ems.user.service.IUserMongoService;
import com.channelsoft.ems.user.service.IUserService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class UserServiceImpl implements IUserService {

	@Autowired
	IUserDao userDao;
	
	@Autowired
	IDatEntService entService;
	
	@Autowired
	IUserMongoService userMongoService;
	
	/*@Transactional
	@Override
	public int registerBase(HttpServletRequest request,DatEntUserPo po,String activeCode) {
		try {
			po.setUserId(userDao.getUserId());
			String userName=po.getLoginName();
			if(StringUtils.isNotBlank(userName)&&userName.indexOf("@")>0){
				userName=userName.substring(0, userName.indexOf("@"));
				if(StringUtils.isBlank(po.getNickName()))po.setNickName(userName);
				if(StringUtils.isBlank(po.getUserName())) po.setUserName(po.getNickName());
			}
			int add=userDao.registerBase(po);
			if(add>0&&StringUtils.isNotBlank(activeCode)){
				SendMainResponsePo resPo=sendMail(request,po,activeCode,false);
				if(!resPo.getResult().equals("0")){
					throw new ServiceException(resPo.getDesc());
				}
			}
			return add;
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR, e.getMessage());
		}catch (ServiceException e){
			throw e;
		}catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.ERR_BASE, "发送邮件失败");
		}
	}*/

	@Override
	public RegisterInfoPo getEntInfo(String domainName) {
		try {
			return userDao.getEntInfo(domainName);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR, e.getMessage());
		}
	}

	@Override
	public SendMainResponsePo sendMail(HttpServletRequest request,DatEntUserPo po,String activeCode,boolean reset) throws Exception {
		// TODO Auto-generated method stub
		request.getSession().setAttribute("code", activeCode);
		String subject;
		Object[] os=null;
		String content;
		String path;
		if(reset){
			path="http://"+request.getHeader("host")+request.getContextPath()
				+"/user/resetPwd?code="+activeCode;
			try {
				subject="重设密码";
				content=ConfigUtil.getString("user.emailresetpwd.content");
				os = new Object[] {path};
				content = MessageFormat.format(content, os);
//				SendEmail.send(subject, po.getEmail(), content);
				return MailSendClient.sendMailForEnt(po.getEntId(), po.getEmail(), subject, content);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new Exception(e.getMessage());
			}
		}else{
			path="http://"+request.getHeader("host")+request.getContextPath()
				+"/user/registerPwd?code="+activeCode;
			try {
				subject="设置密码激活";
				content=ConfigUtil.getString("user.emailpwd.content");
				os = new Object[] {po.getEntId(),path,activeCode};
				content = MessageFormat.format(content, os);
//				SendEmail.send(subject, po.getEmail(), content);
				return MailSendClient.sendMailForEnt(po.getEntId(), po.getEmail(), subject, content);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new Exception(e.getMessage());
			}
		}
		
	}
	@Override
	public SendMainResponsePo sendMailMongo(HttpServletRequest request,DatEntUserPo po,String activeCode,boolean reset) throws Exception {
		// TODO Auto-generated method stub
		request.getSession().setAttribute("code", activeCode);
		String subject;
		Object[] os=null;
		String content;
		String path;
		if(reset){
			path="http://"+request.getHeader("host")+request.getContextPath()
				+"/userMongo/resetPwd?code="+activeCode;
			try {
				subject="重设密码";
				content=ConfigUtil.getString("user.emailresetpwd.content");
				os = new Object[] {path};
				content = MessageFormat.format(content, os);
//				SendEmail.send(subject, po.getEmail(), content);
				return MailSendClient.sendMailForEnt(po.getEntId(), po.getEmail(), subject, content);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new Exception(e.getMessage());
			}
		}else{
			path="http://"+request.getHeader("host")+request.getContextPath()
				+"/userMongo/registerPwd?code="+activeCode;
			try {
				subject="设置密码激活";
				content=ConfigUtil.getString("user.emailpwd.content");
				os = new Object[] {po.getEntId(),path,activeCode};
				content = MessageFormat.format(content, os);
				return MailSendClient.sendMailForEnt(po.getEntId(), po.getEmail(), subject, content);
				
			} catch (Exception e) {
				e.printStackTrace();
				throw new Exception(e.getMessage());
			}
		}
		
	}

	/*@Override
	public DatEntUserPo getEntUserPo(String entId,String code,String email) {
		try {
			return userDao.getEntUserPo(entId,code,email);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR, e.getMessage());
		}
	}*/

	/*@Override
	public String deleteUser(String entId,String code) {
		try {
			return userDao.deleteUser(entId,code);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR, e.getMessage());
		}
	}*/

	@Override
	public SsoUserVo login(String enterpriseid, String loginName, String password, HttpServletRequest request) throws ServiceException {
		try {
			
			/**
			 * 查询租户对象的数据库是否存在
			 */
			boolean isExist=entService.existThisEntId(enterpriseid);
			if(!isExist)
			{
				throw new ServiceException(BaseErrCode.ERR_INVALID_ENTERPRISEDB);
			}
			
			/**
			 * 根据账户密码获取用户信息
			 */
			//List<SsoUserVo> list = userDao.login(enterpriseid,loginName, password);
			
			DBObject dbo = new BasicDBObject();
			dbo.put("entId", enterpriseid);
			dbo.put("loginName", loginName);
			dbo.put("loginPwd", password);
			List<DBObject>  list=userMongoService.queryUserList(dbo, null);
						
			if(list == null || list.size() == 0) {
				throw new ServiceException(BaseErrCode.ERR_INVALID_USER);
			}
			
			//SsoUserVo user = list.get(0);
			SsoUserVo user=new SsoUserVo();
			user = (SsoUserVo)DBObjectUtils.getObject(list.get(0), user);
			
			/*检测用户账号是否被暂停*/
			if(user.getUserStatus().equals("4")){
				throw new ServiceException("你的用户帐号已被暂停！");
			}
			
			return user;
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR, "数据库异常");
		}
	}

	/*@Override
	public boolean existsEmails(String entId, String email) throws ServiceException {
		// TODO Auto-generated method stub
		try {
			return userDao.existsEmails(entId, email);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR, e.getMessage());
		}
	}*/

	@Override
	public String getDomainName(HttpServletRequest request) throws ServiceException {
		// TODO Auto-generated method stub
		String test=request.getServerName();
		String arrayDomain[]=test.split("\\.");
		return arrayDomain[0];
	}

	/*@Override
	@Transactional
	public int registerPwd(String userName,String nickName,String password, String entId, String code) throws ServiceException {
		// TODO Auto-generated method stub
		try {

			int num=userDao.registerPwd(userName,nickName,password,entId,code);
			if(num>0){
				DatEntUserPo user=userDao.getEntUserPo(entId, code, "");
				if(UserType.SERVICE.value.equals(user.getUserType())||UserType.ADMINISTRATOR.value.equals(user.getUserType())){
					//CchatClient.addAgent(entId, user.getUserId(),user.getLoginName(), password);
					*//**
					 * 通知CCOD添加坐席
					 *//*
					CCODRequestPo ccodPo = new CCODRequestPo();
					ccodPo.setEnterpriseId(user.getCcodEntId());
					ccodPo.setAgentId(user.getUserId());
					ccodPo.setAgentName(user.getUserName());
					ccodPo.setAgentPassword(password);
					ccodPo.setAgentRole(AgentRoleType.MANAGER.value);
					CCODClient.addAgent(ccodPo);
				}
	
			}
	
			return num;
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR, e.getMessage());
		}
		
	}*/
	/*@Override
	public void resetUser(DatEntUserPo userPo) throws ServiceException {
		// TODO Auto-generated method stub
		try {
			userDao.resetUser(userPo);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR, e.getMessage());
		}
	}*/
	/*@Override
	public Map<String, List<DatEntUserPo>> queryAll() throws ServiceException {
		try {
			Map<String, List<DatEntUserPo>> map = new HashMap<String, List<DatEntUserPo>>();
			List<DatEntInfoPo> entList = entService.queryAll();
			for (DatEntInfoPo ent : entList) {
				String entId = ent.getEntId();
				List<DatEntUserPo> userList = userDao.queryAll(entId);
				map.put(entId, userList);
			}
			return map;
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR, "数据库异常");
		}
	}*/
	/*@Override
	public List<DatEntUserPo> queryAll(String entId) throws ServiceException {
		try {
			return userDao.queryAll(entId);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR, "数据库异常");
		}
	}*/
	/*@Override
	public Map<String, List<DatEntUserPo>> queryAgentAndAdmin() throws ServiceException {
		try {
			Map<String, List<DatEntUserPo>> map = new HashMap<String, List<DatEntUserPo>>();
			List<DatEntInfoPo> entList = entService.queryAll();
			for (DatEntInfoPo ent : entList) {
				String entId = ent.getEntId();
				List<DatEntUserPo> userList = userDao.queryAgentAndAdmin(entId);
				map.put(entId, userList);
			}
			return map;
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR, "数据库异常");
		}
	}*/
	/*@Override
	public List<DatEntUserPo> queryAgentAndAdmin(String entId) throws ServiceException {
		try {
			return userDao.queryAgentAndAdmin(entId);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR, "数据库异常");
		}
	}*/
	/*@Override
	public DatEntUserPo queryUser(String entId, String userId) throws ServiceException {
		// TODO Auto-generated method stub
		try {
			return userDao.queryUser(entId, userId);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR, "数据库异常");
		}
	}*/

	/*@Override
	public int updateInformation(String entId, String email, String nickName, String userDesc)
			throws ServiceException {
		try {
			return userDao.updateInformation(entId,email,nickName,userDesc);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR, "数据库异常");
		}
	}*/

	/*@Override
	public int updatePassword(String entId, String email, String newLoginPwd) throws ServiceException {
		try {
			return userDao.updatePassword(entId,email,newLoginPwd);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR, "数据库异常");
		}
	}*/
}

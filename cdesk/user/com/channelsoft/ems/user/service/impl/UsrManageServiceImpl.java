package com.channelsoft.ems.user.service.impl;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.channelsoft.cri.constant.BaseErrCode;
import com.channelsoft.cri.constant.DateConstants;
import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.logger.SystemLogUtils;
import com.channelsoft.cri.util.CdeskEncrptDes;
import com.channelsoft.cri.util.WebappConfigUtil;
import com.channelsoft.cri.vo.AjaxResultPo;
import com.channelsoft.cri.vo.PageInfo;
import com.channelsoft.ems.api.client.CCODClient;
import com.channelsoft.ems.api.client.CchatClient;
import com.channelsoft.ems.api.client.MailSendClient;
import com.channelsoft.ems.api.constants.AgentRoleType;
import com.channelsoft.ems.api.po.CCODRequestPo;
import com.channelsoft.ems.api.po.SendMainResponsePo;
import com.channelsoft.ems.ent.service.IDatEntService;
import com.channelsoft.ems.group.po.AgentPo;
import com.channelsoft.ems.group.po.GroupPo;
import com.channelsoft.ems.privilege.constant.RoleType;
import com.channelsoft.ems.register.util.ConfigUtil;
import com.channelsoft.ems.register.util.MD5Util;
import com.channelsoft.ems.sso.util.SsoSessionUtils;
import com.channelsoft.ems.sso.vo.SsoEntInfoVo;
import com.channelsoft.ems.sso.vo.SsoUserVo;
import com.channelsoft.ems.user.constant.LoginType;
import com.channelsoft.ems.user.constant.UserStatus;
import com.channelsoft.ems.user.constant.UserType;
import com.channelsoft.ems.user.dao.IUserDao;
import com.channelsoft.ems.user.dao.IUsrManageDao;
import com.channelsoft.ems.user.po.DatEntUserPo;
import com.channelsoft.ems.user.po.RolePo;
import com.channelsoft.ems.user.service.IUserService;
import com.channelsoft.ems.user.service.IUsrManageService;

public class UsrManageServiceImpl implements IUsrManageService{

	@Autowired
	IUsrManageDao usrManageDao;
	@Autowired
	IUserService userService;
	@Autowired
	IUserDao userDao;
	@Autowired
	IDatEntService datEntService;
	
	/*查询用户*/
	/*@Override
	public List<DatEntUserPo> queryUser(DatEntUserPo po, PageInfo pageInfo) throws ServiceException {
		try {
			return usrManageDao.queryUser(po, pageInfo);
		} catch (Exception e) {
        	e.printStackTrace();
        	throw new ServiceException("查询失败");
		}
	}*/
	
	/*查询创始人邮箱*/
	@Override
	public String queryFounder(String entId) throws ServiceException {
		try {
			return usrManageDao.queryFounder(entId);
		} catch (Exception e) {
        	e.printStackTrace();
        	throw new ServiceException("查询失败");
		}
	}
	
	public List<RolePo> querySecondLevel(RolePo po) throws ServiceException {
		try {
			return usrManageDao.querySecondLevel(po);
		} catch (Exception e) {
        	e.printStackTrace();
        	throw new ServiceException("查询失败");
		}
	}
	
	/*查询最新用户*/
	/*@Override
	public List<DatEntUserPo> queryLately(DatEntUserPo po, PageInfo pageInfo) throws ServiceException {
		try {
			return usrManageDao.queryLately(po, pageInfo);
		} catch (Exception e) {
        	e.printStackTrace();
        	throw new ServiceException("查询失败");
		}
	}*/
	
	/*删除用户*/
	/*@Override
	public int delete(String entId, String[] ids) throws ServiceException {
		try {
			return usrManageDao.delete(entId, ids);
		} catch (Exception e) {
        	e.printStackTrace();
        	throw new ServiceException("删除失败");
		}
	}*/
	
	/*添加用户*/
	/*@Override
	public int add(DatEntUserPo po) throws ServiceException {
		try {
			return usrManageDao.add(po);
		} catch (Exception e) {
        	e.printStackTrace();
        	throw new ServiceException("添加失败");
		}
	}*/
	
	/*更新用户信息*/
	/*@Override
	public int update(DatEntUserPo po) throws ServiceException {
		try {
			return usrManageDao.update(po);
		} catch (Exception e) {
        	e.printStackTrace();
        	throw new ServiceException("更新失败");
		}
	}*/
	
	/*批量编辑用户信息*/
	/*@Override
	public int updateBatch(DatEntUserPo po,String [] ids) throws ServiceException {
		try {
			return usrManageDao.updateBatch(po,ids);
		} catch (Exception e) {
        	e.printStackTrace();
        	throw new ServiceException("更新失败");
		}
	}*/
	
	/*更新用户状态*/
	/*@Override
	public int updateStatus(DatEntUserPo po) throws ServiceException {
		try {
			return usrManageDao.updateStatus(po);
		} catch (Exception e) {
        	e.printStackTrace();
        	throw new ServiceException("更新失败");
		}
	}*/
	
	/*绑定手机*/
	/*@Override
	public int bindPhone(DatEntUserPo po) throws ServiceException {
		try {
			return usrManageDao.bindPhone(po);
		} catch (Exception e) {
        	e.printStackTrace();
        	throw new ServiceException("绑定失败!");
		}
	}*/
	
	/*检测手机是否已绑定*/
	/*@Override
	public boolean existsPhone(String entId, String phone,String userId) throws ServiceException {
		// TODO Auto-generated method stub
		try {
			return usrManageDao.existsPhone(entId, phone,userId);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR, e.getMessage());
		}
	}*/
	
	/*检测手机是否已存在*/
	/*@Override
	public boolean existsPhone1(String entId, String phone,String userId) throws ServiceException {
		// TODO Auto-generated method stub
		try {
			return usrManageDao.existsPhone1(entId, phone,userId);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR, e.getMessage());
		}
	}*/
	
	/*设置密码*/
	/*@Override
	public int setPwd(String password, String entId, String userId) throws ServiceException {
		// TODO Auto-generated method stub
		try {
			int num=usrManageDao.setPwd(password,entId,userId);
			if(num>0){
				//DatEntUserPo user=userDao.getEntUserPo(entId, code, "");
				DatEntUserPo user=queryUserById(entId, userId);
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
					ccodPo.setAgentRole(AgentRoleType.NORMAL.value);
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
	@Transactional
	public DatEntUserPo queryOrAddUser(String entId, String userAccount,String userAccountType) throws ServiceException {
		// TODO Auto-generated method stub
		DatEntUserPo user=usrManageDao.queryUser(entId, userAccount);
		if(user!=null&&StringUtils.isNotBlank(user.getUserId())) return user;
		*//**
		 * 创建一个新用户
		 *//*
		DatEntUserPo po=new DatEntUserPo();
		po.setLoginType(userAccountType);
		po.setLoginName(userAccount);
	
		po.setEntId(entId);
		po.setUserStatus(UserStatus.NORMAL.value);
		po.setUserType(UserType.CUSTOMER.value);
		po.setRoleId(RoleType.CUSTOMER.value);
		if(LoginType.MAILBOX.value.equals(userAccountType)){
			po.setEmail(userAccount);
		}
		this.addCustommer(po);
	
		return po;
	}*/

	/*@Override
	public List<DatEntUserPo> query(DatEntUserPo po, PageInfo pageInfo) throws ServiceException {
		// TODO Auto-generated method stub
		try {
			return usrManageDao.query(po, pageInfo);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR, e.getMessage());
		}
	}*/

	/*@Override
	public DatEntUserPo queryUserById(String entId, String userId) throws ServiceException {
		// TODO Auto-generated method stub
		try {
			DatEntUserPo po=new DatEntUserPo();
			po.setUserId(userId);
			po.setEntId(entId);
			List<DatEntUserPo> list=usrManageDao.query(po, null);
			if(list!=null&&list.size()>0) return list.get(0);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR, e.getMessage());
		}
		return null;
	}*/
	
	/*@Override
	public List<DatEntUserPo> queryEntAll(String entId) throws ServiceException {
		// TODO Auto-generated method stub
		try {
			DatEntUserPo po = new DatEntUserPo();
			po.setEntId(entId);
			return usrManageDao.query(po, null);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR, e.getMessage());
		}
	}*/
	
	/*客服所属客服组*/
	@Override
	public List<GroupPo> belongGroup(DatEntUserPo po) throws ServiceException {
		try {
			return usrManageDao.belongGroup(po);
		} catch (Exception e) {
        	e.printStackTrace();
        	throw new ServiceException("查询失败");
		}
	}
	
	/*分配客服组*/
	@Override
	public int assignAgent(String entId,String[] groupId,String agentId,List<AgentPo> agentPos) throws ServiceException {
		try {
			return usrManageDao.assignAgent(entId,groupId,agentId,agentPos);
		} catch (Exception e) {
        	e.printStackTrace();
        	throw new ServiceException("分配失败");
		}
	}

	/*@Override
	public String changePhoto(HttpServletRequest request, DatEntUserPo po) throws ServiceException {
		// TODO Auto-generated method stub
		String path=request.getSession().getServletContext().getRealPath("/");
		SsoUserVo user = SsoSessionUtils.getUserInfo(request);
		MultipartHttpServletRequest multipartHttpservletRequest = (MultipartHttpServletRequest) request;
		MultipartFile file = multipartHttpservletRequest.getFile("image");
		String origName = file.getOriginalFilename();
		String ext = FilenameUtils.getExtension(origName).toLowerCase();
		if(file.getSize()>300*1024){
			throw new ServiceException("图片大小超过网站限制");
		}
		
		
		String datePath =DateConstants.DATE_FORMAT_NUM_SHORT().format(new Date());
		path=path+ WebappConfigUtil.getParameter("userPhotoURL").replace("entId", user.getEntId()) +datePath;
		
		System.out.println("path="+path);
		
		File dir = new File(path);
		if(!dir.exists()){
			dir.mkdirs();
		}
		String fileName= user.getEntId()+"_"+po.getUserId()+"_"+ new Random().nextInt(10)+"."+ext;
		String destPath = path+"/"+fileName;
		String resultPath = WebappConfigUtil.getParameter("userPhotoURL").replace("entId", user.getEntId())+datePath+"/"+fileName;
		File destFile = new File(destPath);
		try {
			file.transferTo(destFile);
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
			throw new ServiceException("上传文件失败");
		}
		//存入数据库的相对路径
		//数据库存储了/upload/entID/userPhoto/之后的路径
		String url = datePath+"/"+fileName;
		po.setPhotoUrl(url);
		po.setUpdatorId(user.getLoginName());
		po.setUpdatorName(user.getNickName());
		po.setEntId(user.getEntId());
		
		try{
			usrManageDao.changePhoto(po);
			return resultPath;
		}catch(DataAccessException e){
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}*/

	/*@Override
	public List<DatEntUserPo> query(String entId, String userAccount,String userAccountType) throws ServiceException {
		// TODO Auto-generated method stub
		try {
			return usrManageDao.query(entId, userAccount, userAccountType);
		} catch (Exception e) {
        	e.printStackTrace();
        	throw new ServiceException("查询失败");
		}
	}*/

	/*@Override
	@Transactional
	public int addCustommer(DatEntUserPo po) throws ServiceException {
		// TODO Auto-generated method stub
    	if(StringUtils.isBlank(po.getLoginName())){
    		throw new ServiceException("登陆账号不能为空");
    	}
    	if(StringUtils.isBlank(po.getEntId())){
    		throw new ServiceException("企业编号不能为空");
    	}
    	DatEntUserPo u=usrManageDao.queryUser(po.getEntId(), po.getLoginName());
    	if(u!=null){
    		throw new ServiceException("用户"+po.getLoginName()+"已存在");
    	}
        try{

        	po.setUserStatus(UserStatus.NORMAL.value);
    		po.setUserType(UserType.CUSTOMER.value);
    		po.setRoleId(RoleType.CUSTOMER.value);
    		po.setUserId(userDao.getUserId());
    		SsoEntInfoVo ent=datEntService.query(po.getEntId());
    		po.setEntName(ent.getEntName());
    		String activeCode=MD5Util.MD5(po.getEmail()+new Date().getTime());
    		po.setActiveCode(activeCode);
    		//userName值是否为空，空的激活页面与不为空的激活页面不一致
    		boolean userNameNull=false;
    		if(StringUtils.isBlank(po.getUserName())){
    			userNameNull=true;
			}
    		*//**
    		 * 如果昵称为空，则设置为邮箱的名称，如果用户名为空，则设置为昵称
    		 *//*
    		String userName=po.getLoginName();
			if(StringUtils.isNotBlank(userName)&&userName.indexOf("@")>0){
				userName=userName.substring(0, userName.indexOf("@"));
				if(StringUtils.isBlank(po.getNickName()))po.setNickName(userName);
				if(StringUtils.isBlank(po.getUserName())){
					po.setUserName(po.getNickName());
				}
			}
    		int num=userDao.registerBase(po);
    		//添加成功，并且是邮箱用户才发送邮件
    		if(num>0&&StringUtils.isNotBlank(po.getEmail())){
    			SystemLogUtils.Debug(String.format("========创建新用户：=======,entId=%s,email=%s", po.getEntId(),po.getEmail()));
    			*//**
    			 * 发送邮件
    			 *//*

    			String basePath=WebappConfigUtil.getParameter("CDESK_ROOT").replace("entId", po.getEntId());
    			String path=basePath+"/user/registerPwd?code="+activeCode;
    			if(userNameNull) path=basePath+"/user/registerPwd?codeauto="+activeCode;
    			String subject="设置密码激活";
    			String content=ConfigUtil.getString("user.autoCreateUserpwd.content");
    			Object[] os= new Object[] {po.getEntId(),path,activeCode};
    			content = MessageFormat.format(content, os);
    			SendMainResponsePo sr=MailSendClient.sendMailForEnt(po.getEntId(), po.getEmail(), subject, content);
    			if(!"0".equals(sr.getResult())) throw new ServiceException("发送注册邮件失败");
    		}
    		
    		return num;
        }
		catch(Exception e){
			e.printStackTrace();
			throw new ServiceException("添加用户失败");
		}

	}*/

	/*@Override
	public int updateUser(DatEntUserPo po) throws ServiceException {
		// TODO Auto-generated method stub
		try{
	    	if(StringUtils.isBlank(po.getEntId())){
	    		throw new ServiceException("企业编号不能为空");
	    	}
			if(StringUtils.isBlank(po.getLoginName())&&StringUtils.isBlank(po.getUserId())){
	    		throw new ServiceException("登陆账号与用户编号不能同时为空");
	    	}
		    return usrManageDao.updateUser(po);
		}
		catch(Exception e){
				e.printStackTrace();
				throw new ServiceException("修改用户失败");
		}
	}*/

	/*@Override
	public DatEntUserPo queryUser(String entId, String loginName) throws ServiceException {
		// TODO Auto-generated method stub
		try{
			 return usrManageDao.queryUser(entId, loginName);
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException("查询用户失败");
		}

	}*/
	/*@Transactional
	@Override
	public int mergeUser(String entId, String userMerge, String userTarget) throws ServiceException {
		DatEntUserPo merge=userDao.getEntUserPo(entId, null, userMerge);
		DatEntUserPo target=userDao.getEntUserPo(entId, null, userTarget);
		if(merge==null){
			throw new ServiceException("此用户已经不存在！");
		}
		if(target==null){
			throw new ServiceException("目标用户已经不存在！");
		}
		Class<DatEntUserPo> poClass=DatEntUserPo.class;
		Method methods[]=poClass.getDeclaredMethods();
		for(int i=0;i<methods.length;i++){
			if (methods[i].getName().startsWith("get")){
				try {
					String targetProp=(String)methods[i].invoke(target);
					if(StringUtils.isBlank(targetProp)){
						String mergeProp=(String)methods[i].invoke(merge);
						if(StringUtils.isNotBlank(mergeProp)){
							String sufName=methods[i].getName().substring(3);
							String setMdName="set"+sufName;
							Method set=poClass.getDeclaredMethod(setMdName,new Class[]{String.class});
							set.invoke(target, new Object[]{mergeProp});
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					throw new ServiceException(e.getMessage());
				}
			}
		}
		try {
			return usrManageDao.mergeUser(entId,merge,target);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}*/

	/*@Override
	public List<DatEntUserPo> queryOrdinaryByValue(String entId, String value,String email) throws ServiceException {
		try {
			return usrManageDao.queryOrdinaryByValue(entId,value,email);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}*/
	
	@Override
	public String queryLastLogin(String entId,String userId) throws ServiceException {
		try {
			return usrManageDao.queryLastLogin(entId,userId);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
}

package com.channelsoft.ems.user.service.impl;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.channelsoft.cri.constant.BaseErrCode;
import com.channelsoft.cri.constant.DateConstants;
import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.logger.SystemLogUtils;
import com.channelsoft.cri.util.CdeskEncrptDes;
import com.channelsoft.cri.util.DBObjectUtils;
import com.channelsoft.cri.util.DomainUtils;
import com.channelsoft.cri.util.WebappConfigUtil;
import com.channelsoft.cri.vo.PageInfo;
import com.channelsoft.ems.api.client.EntClient;
import com.channelsoft.ems.api.client.MailSendClient;
import com.channelsoft.ems.api.po.DatUserSimplePo;
import com.channelsoft.ems.api.po.ResultPo;
import com.channelsoft.ems.api.po.SendMainResponsePo;
import com.channelsoft.ems.communicate.service.ICommService;
import com.channelsoft.ems.ent.po.DatEntInfoPo;
import com.channelsoft.ems.ent.service.IDatEntService;
import com.channelsoft.ems.group.po.GroupPo;
import com.channelsoft.ems.log.constants.BusinessTypeEnum;
import com.channelsoft.ems.log.constants.LogTypeEnum;
import com.channelsoft.ems.log.service.ILogMongoService;
import com.channelsoft.ems.privilege.constant.RoleType;
import com.channelsoft.ems.redis.util.ParamUtils;
import com.channelsoft.ems.register.po.RegisterInfoPo;
import com.channelsoft.ems.register.util.ConfigUtil;
import com.channelsoft.ems.register.util.MD5Util;
import com.channelsoft.ems.sso.util.SsoSessionUtils;
import com.channelsoft.ems.sso.vo.SsoUserVo;
import com.channelsoft.ems.user.constant.LoginType;
import com.channelsoft.ems.user.constant.UserStatus;
import com.channelsoft.ems.user.constant.UserType;
import com.channelsoft.ems.user.controller.UserManageMongoController;
import com.channelsoft.ems.user.dao.IUserDao;
import com.channelsoft.ems.user.dao.IUserMongoDao;
import com.channelsoft.ems.user.dao.IUsrManageDao;
import com.channelsoft.ems.user.po.DatEntUserPo;
import com.channelsoft.ems.user.service.IUserMongoService;
import com.channelsoft.ems.user.service.IUserService;
import com.channelsoft.ems.user.util.UserUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

public class UserMongoServiceImpl implements IUserMongoService {

	@Autowired
	IUsrManageDao usrManageDao;
	@Autowired
	IUserMongoDao userMongoDao;
	@Autowired
	IUserDao userDao;
	@Autowired
	IUserService userService;
	@Autowired
	IDatEntService entService;
	@Autowired
	ILogMongoService logMongoService;
	@Autowired
	ICommService commService;
	/*查询用户*/
	@Override
	public List<DatEntUserPo> queryUser(DatEntUserPo po, PageInfo pageInfo) throws ServiceException {
		try {
			return userMongoDao.queryUser(po, pageInfo);
		} catch (Exception e) {
        	e.printStackTrace();
        	throw new ServiceException("查询失败");
		}
	}
	
	@Override
	public int resetUser(DatEntUserPo userPo) throws ServiceException {
		try {
			return userMongoDao.resetUser(userPo);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR, e.getMessage());
		}
	}
	
	@Override
	public int registerPwd(String userName, String nickName, String password, String entId, String code)
			throws ServiceException{
		try {
			return userMongoDao.registerPwd(userName,nickName,password,entId,code);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR, e.getMessage());
		}
	}
	
	@Override
	public DatEntUserPo getEntUserPo(String entId,String code,String email) {
		try {
			return userMongoDao.getEntUserPo(entId,code,email);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR, e.getMessage());
		}
	}
	
	@Override
	public int registerBase(HttpServletRequest request, DatEntUserPo po, String activeCode) throws ServiceException{
		try {
			po.setUserId(userDao.getUserId());
			String userName=po.getLoginName();
			if(StringUtils.isNotBlank(userName)&&userName.indexOf("@")>0){
				userName=userName.substring(0, userName.indexOf("@"));
				if(StringUtils.isBlank(po.getNickName()))po.setNickName(userName);
				if(StringUtils.isBlank(po.getUserName())) po.setUserName(po.getNickName());
			}
			int add=userMongoDao.registerBase(po);
			if(add>=0&&StringUtils.isNotBlank(activeCode)){
				SendMainResponsePo resPo=userService.sendMailMongo(request,po,activeCode,false);
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
	}
	
	@Override
	public RegisterInfoPo getEntInfo(String entId) throws ServiceException {
		try {
			return userMongoDao.getEntInfo(entId);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR, e.getMessage());
		}
	}
	
	/*添加用户*/
	@Override
	public int add(DatEntUserPo po, String entId) throws ServiceException {
		try{
			return userMongoDao.add(po, entId);
		}
		catch(DataAccessException e){
			e.printStackTrace();
			throw new ServiceException("添加用户出错");
		}
	
	}

	/*添加用户*/
	@Override
	public int add(DBObject dbo, String entId) throws ServiceException {
		try{
			return userMongoDao.add(dbo, entId);
		}
		catch(DataAccessException e){
			e.printStackTrace();
			throw new ServiceException("添加用户出错");
		}
	}

	/*修改用户信息*/
	@Override
	public int updateUser(String userInfos, String updatorId, String updatorName) throws ServiceException {
		try{
			DBObject dbo = (DBObject) JSON.parse(userInfos);
			
		    Date date=new Date();
		    DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    String time=format.format(date);
			dbo.put("updateTime", time);
			dbo.put("updatorId", updatorId);
			dbo.put("updatorName", updatorName);
			return this.updateUser(dbo, dbo.get("entId")+"", dbo.get("userId")+"");
		}
		catch(DataAccessException e){
			e.printStackTrace();
			throw new ServiceException("修改用户出错");
		}
	}

	/*修改用户信息*/
	@Override
	public int updateUser(DBObject dbo, String entId, String userId)
			throws ServiceException {
		try{
			return userMongoDao.updateUser(dbo, entId, userId);
		}
		catch(DataAccessException e){
			e.printStackTrace();
			throw new ServiceException("修改用户出错");
		}
	}

	/*查询用户*/
	@Override
	public List<DBObject> queryUserList(DBObject queryObject, PageInfo pageInfo)
			throws ServiceException {
		try{
			return userMongoDao.queryUserList(queryObject, pageInfo);
		}
		catch(DataAccessException e){
			e.printStackTrace();
			throw new ServiceException("查询用户出错");
		}
	}
	
	/*检测手机是否已经绑定*/
	@Override
	public boolean existsPhone(String userInfos) throws ServiceException {
		try{
			DBObject dbo = (DBObject) JSON.parse(userInfos);
			boolean exist=false;
			long size = userMongoDao.existsPhone(dbo);			
			if(size>0){
				exist=true;
			}			
			return exist;
		}
		catch(DataAccessException e){
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR, e.getMessage());
		}
	}
	
	/*检测手机号是否已存在*/
	@Override
	public boolean existsPhone1(String userInfos) throws ServiceException {
		try{
			DBObject dbo = (DBObject) JSON.parse(userInfos);
			boolean exist=false;
			long size = userMongoDao.existsPhone1(dbo);			
			if(size>0){
				exist=true;
			}			
			return exist;
		}
		catch(DataAccessException e){
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR, e.getMessage());
		}
	}
	
	@Override
	public boolean existsPhoneIM(String userInfos, HttpServletRequest request) throws ServiceException {
		try{
			String entId=DomainUtils.getEntId(request);
			DBObject dbo = (DBObject) JSON.parse(userInfos);
			dbo.put("entId", entId);
			boolean exist=false;
			long size = userMongoDao.existsPhone1(dbo);			
			if(size>0){
				exist=true;
			}			
			return exist;
		}
		catch(DataAccessException e){
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR, e.getMessage());
		}
	}
	
	/*设置密码*/
	public int setPwd(String userInfos, String updatorId, String updatorName) throws ServiceException {
		try{
			DBObject dbo = (DBObject) JSON.parse(userInfos);
			
		    Date date=new Date();
		    DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    String time=format.format(date);
			dbo.put("updateTime", time);
			dbo.put("updatorId", updatorId);
			dbo.put("updatorName", updatorName);
			
			return this.setPwd(dbo, dbo.get("entId")+"", dbo.get("userId")+"");
		}
		catch(DataAccessException e){
			e.printStackTrace();
			throw new ServiceException("修改用户出错");
		}
	}
	
	/*设置密码*/
	@Override
	public int setPwd(DBObject dbo, String entId, String userId) throws ServiceException {
		try{
			int num=userMongoDao.setPwd(dbo, entId, userId);
			return num;
		}
		catch(DataAccessException e){
			e.printStackTrace();
			throw new ServiceException("修改密码出错");
		}
	}
	
	@Override
	public DatEntUserPo queryUserById(String entId, String userId)
			throws ServiceException {
		DBObject d=this.queryById(entId, userId);
		if(d!=null) {
			DatEntUserPo user=new DatEntUserPo();
			user=(DatEntUserPo)DBObjectUtils.getObject(d, user);
			return user;
		}

		return null;
	}
	
	@Override
	public DBObject queryById(String entId, String userId) throws ServiceException {
		DBObject dbo = new BasicDBObject();
		dbo.put("entId", entId);
		dbo.put("userId", userId);
		List<DBObject> list=this.queryUserList(dbo, null);
		if(list!=null&&list.size()>0) {
			return list.get(0);
		}
		return null;
	}
	
	@Override
	public List<DBObject> query(String entId, String userAccount,String userAccountType) throws ServiceException {
		try{
           return userMongoDao.query(entId, userAccount, userAccountType);
		}
		catch(DataAccessException e){
			e.printStackTrace();
			throw new ServiceException("查询用户出错");
		}
	}

	/*删除用户*/
	@Override
	public int deleteUser(String entId, String[] ids)  throws ServiceException{
		int result = userMongoDao.deleteUser(entId, ids);
		return result;
	}

	/*批量编辑用户*/
	@Override
	public int updateBatch(String[] ids,String userInfos, String updatorId, String updatorName) throws ServiceException, JSONException {
		try{
			DBObject dbo = (DBObject) JSON.parse(userInfos);
			JSONObject json=new JSONObject(userInfos);			
		    Date date=new Date();
		    DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    String time=format.format(date);
		    
			if(json.getString("userStatus").equals("")){
				dbo.removeField("userStatus");
			}		    
			for (String userId : ids)
			{
				dbo.put("updateTime", time);
				dbo.put("updatorId", updatorId);
				dbo.put("updatorName", updatorName);
				this.updateUser(dbo, dbo.get("entId")+"", userId);
			}			
			return 1;
		}
		catch(DataAccessException e){
			e.printStackTrace();
			throw new ServiceException("修改用户出错");
		}
	}
	
	/*检测邮箱是否已经注册*/
	@Override
	public boolean existsEmails(String entId, String email) throws ServiceException {
		try{
			DBObject dbo = new BasicDBObject();
			dbo.put("entId", entId);
			dbo.put("email", email);
			
			boolean exist=false;
			long size = userMongoDao.existsEmails(dbo);			
			if(size>0){
				exist=true;
			}			
			return exist;
		}
		catch(DataAccessException e){
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR, e.getMessage());
		}
	}
	
	@Override
	public boolean existsPhone(String entId, String telPhone) throws ServiceException {
		try{
			DBObject dbo = new BasicDBObject();
			dbo.put("entId", entId);
			dbo.put("telPhone", telPhone);
			
			boolean exist=false;
			long size = userMongoDao.existsPhone1(dbo);		
			if(size>0){
				exist=true;
			}			
			return exist;
		}
		catch(DataAccessException e){
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR, e.getMessage());
		}
	}

	@Override
	public Map<String, List<DatEntUserPo>> queryAgentAndAdmin() throws ServiceException {
		try {
			Map<String, List<DatEntUserPo>> map = new HashMap<String, List<DatEntUserPo>>();
			List<DatEntInfoPo> entList = entService.queryAll();
			for (DatEntInfoPo ent : entList) {
				String entId = ent.getEntId();
				List<DatEntUserPo> userList = userMongoDao.queryAgentAndAdmin(entId);
				map.put(entId, userList);
			}
			return map;
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR, "数据库异常");
		}
	}

	@Override
	public List<DatEntUserPo> queryAgentAndAdmin(String entId) throws ServiceException {
		try {
			return userMongoDao.queryAgentAndAdmin(entId);

		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR, "数据库异常");
		}
		
	}
	
	/*更新用户详情复选框类型自定义字段*/
	@Override
	public int updateCheckBox(String key,String value,String entId,String userId, String field,String checked,String updatorId,String updatorName)  throws ServiceException {
		try{						
			//DBObject dbo = (DBObject) JSON.parse(userInfos);
			Date date=new Date();
			DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time=format.format(date);
			
			DBObject dbo = new BasicDBObject();
			dbo.put(field, value);
			dbo.put("userId", userId);
			dbo.put("entId", entId);
			dbo.put("updateTime", time);
			dbo.put("updatorId", updatorId);
			dbo.put("updatorName", updatorName);
			
			List<DBObject> list=queryUserList(dbo, null);
			boolean hasField=false;
			
		    for(String key1 : list.get(0).keySet()){
		    	if(key1.equals(key)){
		    		hasField=true;
		    	}		    	
		    }
		    			
		    if(hasField==false){
		    	DBObject dbo1 = new BasicDBObject();
		    	List<String> value1=new ArrayList<String>();
		    	value1.add(value);
		    	dbo1.put(field, value1);
		    	dbo1.put("updateTime", time);
		    	
	    		return this.updateUser(dbo1, entId, userId);
		    }else{
		    	return this.updateCheckBox(dbo, entId, userId,field,checked);
		    }
		}
		catch(DataAccessException e){
			e.printStackTrace();
			throw new ServiceException("修改用户出错");
		}
	}
	
	/*更新用户详情复选框类型自定义字段*/
	@Override
	public int updateCheckBox(DBObject dbo, String entId, String userId,String field,String checked)
			throws ServiceException {
		try{
			return userMongoDao.updateCheckBox(dbo, entId, userId,field,checked);
		}
		catch(DataAccessException e){
			e.printStackTrace();
			throw new ServiceException("修改用户出错");
		}
	}


	@Override
	public int addCustommer(DatEntUserPo po) throws ServiceException {
    	if(StringUtils.isBlank(po.getLoginName())){
    		throw new ServiceException("登陆账号不能为空");
    	}
    	if(StringUtils.isBlank(po.getEntId())){
    		throw new ServiceException("企业编号不能为空");
    	}
    	//参数写反了，谁写的
    	DatEntUserPo u=this.queryUserByLoginName(po.getLoginName(), po.getEntId());
//    	DatEntUserPo u=this.queryUserByLoginName(po.getEntId(), po.getLoginName());
    	if(u!=null){
    		throw new ServiceException("用户"+po.getLoginName()+"已存在");
    	}
        try{

        	po.setUserStatus(UserStatus.NORMAL.value);
    		po.setUserType(UserType.CUSTOMER.value);
    		po.setRoleId(RoleType.CUSTOMER.value);
    		po.setUserId(userDao.getUserId());
    		DatEntInfoPo ent=ParamUtils.getEntInfo(po.getEntId());
    		po.setEntName(ent.getEntName());
    		String activeCode=MD5Util.MD5(po.getEmail()+new Date().getTime());
    		po.setActiveCode(activeCode);
    		//userName值是否为空，空的激活页面与不为空的激活页面不一致
    		boolean userNameNull=false;
    		if(StringUtils.isBlank(po.getUserName())){
    			userNameNull=true;
			}
    		/**
    		 * 如果昵称为空，则设置为邮箱的名称，如果用户名为空，则设置为昵称
    		 */
    		String userName=po.getLoginName();
			if(StringUtils.isNotBlank(userName)&&userName.indexOf("@")>0){
				userName=userName.substring(0, userName.indexOf("@"));
				if(StringUtils.isBlank(po.getNickName()))po.setNickName(userName);
				if(StringUtils.isBlank(po.getUserName())){
					po.setUserName(po.getNickName());
				}
			}
			if(StringUtils.isBlank(po.getNickName())){
				po.setNickName(userName);
			}
			if(StringUtils.isBlank(po.getUserName())){
				po.setUserName(po.getNickName());
			}
			String loginPwd=po.getLoginPwd()==null?po.getLoginPwd():CdeskEncrptDes.encryptST(po.getLoginPwd());
			String emailPwd=po.getEmailPwd()==null?null:CdeskEncrptDes.encryptST(po.getEmailPwd());
			po.setLoginPwd(loginPwd);
			po.setEmailPwd(emailPwd);
    		int num=this.add(po, po.getEntId());
    		//添加成功，并且是邮箱用户才发送邮件
    		if(num>0&&StringUtils.isNotBlank(po.getEmail())){
    			
    			SystemLogUtils.Debug(String.format("========创建新用户：=======,entId=%s,email=%s", po.getEntId(),po.getEmail()));
    			/**
    			 * 发送邮件
    			 */

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
    		//添加成功，并且是电话用户才发送短信
    		if(num>0&&StringUtils.isNotBlank(po.getTelPhone())){
    			
    		}
    		//添加成功，并且是IM用户才发送..
    		if(num>0&&StringUtils.isNotBlank(po.getWebchatId())){
    			
    		}
    		return num;
        }
		catch(Exception e){
			e.printStackTrace();
			throw new ServiceException("添加用户失败");
		}



}

	@Override
	public DatEntUserPo queryUserByLoginName(String loginName, String entId)
			throws ServiceException {
		DBObject queryObject=new BasicDBObject();
		queryObject.put("loginName", loginName);
		queryObject.put("entId", entId);
		List<DBObject> list=this.queryUserList(queryObject, null);
		if(list!=null&&list.size()>0){
			DatEntUserPo user=new DatEntUserPo();
			user=(DatEntUserPo)DBObjectUtils.getObject(list.get(0), user);
			return user;
		}
		return null;
	}

	@Override
	public DatEntUserPo queryOrAddUser(String entId, String userAccount,String userAccountType) throws ServiceException {
		DatEntUserPo user=null;
		List<DBObject> list=userMongoDao.query(entId, userAccount, userAccountType);
		if(list!=null&&list.size()>0){
			user=new DatEntUserPo();
			user=(DatEntUserPo)DBObjectUtils.getObject(list.get(0), user);
		}
		if(user!=null&&StringUtils.isNotBlank(user.getUserId())) return user;
		/**
		 * 创建一个新用户
		 */
		DatEntUserPo po=new DatEntUserPo();
		po.setLoginType(userAccountType);
		po.setLoginName(userAccount);
	
		po.setEntId(entId);
		po.setUserStatus(UserStatus.NORMAL.value);
		po.setUserType(UserType.CUSTOMER.value);
		po.setRoleId(RoleType.CUSTOMER.value);
//		if(LoginType.MAILBOX.value.equals(userAccountType)){
//			po.setEmail(userAccount);
//		}
//		if(LoginType.TELEPHONE.value.equals(userAccountType)){
//			po.setTelPhone(userAccount);
//		}
//		if(LoginType.IM.value.equals(userAccountType)){
//			po.setWebchatId(userAccount);
//			po.setUserName(userAccount.substring(0, 10));//IM创建的用户需要将chatId截取前10位
//			po.setNickName(userAccount.substring(0, 10));
//		}
		UserUtil.setUserAccountForAdd(po, userAccount, userAccountType, "");
		this.addCustommer(po);
	
		return po;
				
	}

	@Override
	public String changePhoto(HttpServletRequest request, DatEntUserPo po)
			throws ServiceException {
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
		
//		System.out.println("path="+path);
		
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
		/*po.setPhotoUrl(url);
		po.setUpdatorId(user.getLoginName());
		po.setUpdatorName(user.getNickName());
		po.setEntId(user.getEntId());*/
		
		try{
			String time = DateConstants.DATE_FORMAT().format(new Date());
			DBObject dbo = new BasicDBObject();
			dbo.put("updateTime", time);
			dbo.put("photoUrl", url);
			dbo.put("updatorId", user.getLoginName());
			dbo.put("updatorName", user.getNickName());
			
			userMongoDao.updateUser(dbo, user.getEntId(), po.getUserId());
			return resultPath;
		}catch(DataAccessException e){
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public int updateUser(DatEntUserPo po) throws ServiceException {
		try{
	    	if(StringUtils.isBlank(po.getEntId())){
	    		throw new ServiceException("企业编号不能为空");
	    	}
			if(StringUtils.isBlank(po.getLoginName())&&StringUtils.isBlank(po.getUserId())){
	    		throw new ServiceException("登陆账号与用户编号不能同时为空");
	    	}
		    return userMongoDao.updateUser(DBObjectUtils.getDBObject(po));
		}
		catch(Exception e){
				e.printStackTrace();
				throw new ServiceException("修改用户失败");
		}
	}

	@Override
	public List<GroupPo> belongGroup(String loginName,String userId,String entId) throws ServiceException {
		try{
		 	if(StringUtils.isBlank(entId)){
	    		throw new ServiceException("企业编号不能为空");
	    	}
			if(StringUtils.isBlank(loginName)&&StringUtils.isBlank(userId)){
				throw new ServiceException("客服帐号不能为空");
			}
			DatEntUserPo po=new DatEntUserPo();
			po.setEntId(entId);
			po.setLoginName(loginName);
			if(StringUtils.isBlank(loginName)&&StringUtils.isNotBlank(userId)){
				DatEntUserPo a=ParamUtils.getAgentById(userId, entId);
			    if(a==null){
			    	throw new ServiceException("客服不存在");
			    }
			    else{
			    	po.setLoginName(a.getLoginName());
			    }
			}
	
			
			return usrManageDao.belongGroup(po);
		}
		catch(Exception e){
			e.printStackTrace();
			throw new ServiceException("查询失败");
		}

	}
	
	@Override
	public int updateInformation(String entId,DatEntUserPo po)
			throws ServiceException {
		try {
			return userMongoDao.updateInformation(entId,po);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR, "数据库异常");
		}
	}
	
	@Override
	public int updatePassword(String entId, String userId, String newLoginPwd) throws ServiceException {
		try {
			return userMongoDao.updatePassword(entId,userId,newLoginPwd);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR, "数据库异常");
		}
	}

	@Override
	public List<DatEntUserPo> queryAll(String enterpriseid) throws ServiceException {
		try {
			return userMongoDao.queryAll(enterpriseid);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR, "数据库异常");
		}
	}
	
	@Override
	public List<DatEntUserPo> queryOrdinaryByValue(String entId, String value,String loginName, String userId) throws ServiceException {
		try {
			return userMongoDao.queryOrdinaryByValue(entId,value,loginName,userId);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public int mergeUser(String entId, String userMergeId, String userTargetId,String sessionKey) throws ServiceException {
		
		try {
			
			DatEntUserPo merge=userMongoDao.getEntUserPoById(entId,userMergeId);
			DatEntUserPo target=userMongoDao.getEntUserPoById(entId,userTargetId);
			if(merge==null||target==null){
				throw new Exception("此用户或目标用户已经不存在！");
			}
			
			Class<DatEntUserPo> poClass=DatEntUserPo.class;
			Method methods[]=poClass.getDeclaredMethods();
			for(int i=0;i<methods.length;i++){
				if (methods[i].getName().startsWith("get")){
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
				}
			}
			
			DBObject dbo=new BasicDBObject();
			dbo.put("toUserId", target.getUserId());
			dbo.put("toUserName", target.getUserName());
			dbo.put("toUserEmail", target.getEmail());

			/**
			 * 用户合并后处理工单
			 */
			EntClient.userJoinForwork(entId, merge.getUserId(), dbo.toString(), sessionKey);
			//合并用户信息
			int num= userMongoDao.mergeUser(entId,merge,target);
			//合并联络历史
			commService.mergeComm(entId, userMergeId, userTargetId);
			return num;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		} 
	}
	
	/*查询用户详情*/
	@Override
	public List<DBObject> queryUserDetail(DBObject queryObject, PageInfo pageInfo)
			throws ServiceException {
		try{
			return userMongoDao.queryUserDetail(queryObject, pageInfo);
		}
		catch(DataAccessException e){
			e.printStackTrace();
			throw new ServiceException("查询用户出错");
		}
	}

	@Override
	public void updateOrAddUserForSource(HttpServletRequest request,
			HttpServletResponse response) throws ServiceException {
		String entId=request.getParameter("entId");
		String email=request.getParameter("email");
		String userAccount=request.getParameter("userAccount");
		String userAccountType=request.getParameter("userAccountType");
		String telphone=request.getParameter("telphone");
		String userName=request.getParameter("userName");
		String signature=request.getParameter("signature");
		String updatorId=request.getParameter("updatorAccount");
		String weixin=request.getParameter("weixin");
		String qq=request.getParameter("qq");
		String weibo=request.getParameter("weibo");
	   	SystemLogUtils.Debug(String.format("修改或添加用户, entId=%s,email=%s,telphone=%s,userAccount=%s,userAccountType=%s,userName=%s,signature=%s,updatorId=%s,weixin=%s,qq=%s,weibo=%s", entId, email,telphone,userAccount,userAccountType,userName,signature,updatorId,weixin,qq,weibo));
	   // 验证参数不为空
    	if (StringUtils.isBlank(entId)) {
    		ResultPo.failed(new Exception("企业Id为空")).returnJSONP(request, response);
    	}
    	if (StringUtils.isBlank(updatorId)) {
    		ResultPo.failed(new Exception("修改人登陆帐号为空")).returnJSONP(request, response);
    	}
    	//登陆帐号不能为空
    	if(StringUtils.isBlank(userAccount)){
    		ResultPo.failed(new Exception("账号为空")).returnJSONP(request, response);
    	}
    	if(StringUtils.isBlank(userAccountType)){
    		ResultPo.failed(new Exception("账号类型为空")).returnJSONP(request, response);
    	}
    	if (StringUtils.isBlank(signature)) {
    		ResultPo.failed(new Exception("签名为空")).returnJSONP(request, response);
    	}
    	// 验证签名
    	String skey = WebappConfigUtil.getParameter("ENT_WX_SKEY");
    	String signComp = DigestUtils.md5Hex(entId + "_" + skey);
    	if (!signComp.equals(signature)) {
    		ResultPo.failed(new Exception("签名错误")).returnJSONP(request, response);
    	}
    	try {
    		// 验证企业存在
    	  	boolean isExist=ParamUtils.getEntInfo(entId)==null?false:true;
        	if (!isExist) {
        		ResultPo.failed(new Exception("企业不存在")).returnJSONP(request, response);
        	}
        	DatEntUserPo u=this.queryUserByLoginName(updatorId, entId);

        	DatEntUserPo po=new DatEntUserPo();
        	po.setLoginName(userAccount);
        	po.setLoginType(userAccountType);
        	po.setEmail(email);
        	po.setTelPhone(telphone);
        	po.setEntId(entId);
        	po.setUserName(userName);
        	po.setUpdatorId(updatorId);
        	po.setWeixin(weixin);
        	po.setQq(qq);
        	po.setWeibo(weibo);
        	/**
        	 * 根据登陆帐号和帐号类型自动生成对应信息
        	 */
        	if(LoginType.MAILBOX.value.equals(userAccountType)&&StringUtils.isBlank(email)){
        		po.setEmail(userAccount);
        	}
        	else if(LoginType.TELEPHONE.value.equals(userAccountType)){
        		if(StringUtils.isBlank(telphone)) po.setTelPhone(userAccount);
        		if(StringUtils.isBlank(po.getUserName())) po.setUserName(telphone);
        	}
        	else if(LoginType.WECHAT.value.equals(userAccountType)&&StringUtils.isBlank(weixin)){
        		po.setWeixin(userAccount);
        	}
        	else if(LoginType.QQ.value.equals(userAccountType)&&StringUtils.isBlank(qq)){
        		po.setQq(userAccount);
        	}
        	else if(LoginType.MICROBLOG.value.equals(userAccountType)&&StringUtils.isBlank(weibo)){
        		po.setWeibo(userAccount);
        	}
//        	List<DBObject>  list=this.query(entId, userAccount, userAccountType);
        	List<DBObject>  list=this.queryUser(entId, userAccount, userAccountType);
        	int num=0;
        	//该帐号不存在,添加一个
        	if(list==null||list.size()==0){
        		
        		num=this.addCustommer(po);
        		logMongoService.add(request.getRemoteAddr(), po, LogTypeEnum.ADD, BusinessTypeEnum.USER, po.getLoginName(), BusinessTypeEnum.USER.desc+"("+po.getLoginName()+")", po);
        	}
        	else{
        		DatEntUserPo cust=new DatEntUserPo();
        		 cust=(DatEntUserPo)DBObjectUtils.getObject(list.get(0), cust);
        		po.setUserId(cust.getUserId());
        		//这里需要根据userId更新，所以需要设置loginName为空 
        		po.setLoginName("");
        		num=this.updateUser(po);
        		po=cust;
//      		    logMongoService.add(request.getRemoteAddr(), u, LogTypeEnum.UPDATE, BusinessTypeEnum.USER, po.getLoginName(), BusinessTypeEnum.USER.desc+"("+po.getLoginName()+")", po);

        	}
        	
//        	if(num<1) {
//        		SystemLogUtils.Debug(String.format("修改或添加用户，数据更新没成功, entId=%s,email=%s,telphone=%s,userAccount=%s,userName=%s,signature=%s", entId, email,telphone,userAccount,userName,signature));
//        		new ResultPo(false,"修改失败,数据更新没成功").returnJSONP(request, response);
//        	}
        	
        	DatUserSimplePo userSimplePo=new DatUserSimplePo();
  		    BeanUtils.copyProperties(po, userSimplePo);
  		    SystemLogUtils.Debug(String.format("修改或添加用户成功,entId=%s,email=%s,telphone=%s,userAccount=%s,userName=%s,signature=%s", entId, email,telphone,userAccount,userName,signature));
        	ResultPo.success("修改成功", num, userSimplePo).returnJSONP(request, response);
    	}
    	 catch (Exception e) {
 			e.printStackTrace();
 			SystemLogUtils.Debug(String.format("修改用户异常, entId=%s,email=%s,telphone=%s,userAccount=%s,userName=%s,signature=%s,message=%s", entId, email,telphone,userAccount,userName,signature,e.getMessage()));
	 		ResultPo.failed(new Exception(e.getMessage())).returnJSONP(request, response); 
	 		
 		}
	}

	
	/*添加用户*/
	@Override
	public int add(DBObject dbo, String userId, String entId) throws ServiceException {
		try{
			return userMongoDao.add(dbo,userId, entId);
		}
		catch(DataAccessException e){
			e.printStackTrace();
			throw new ServiceException("添加用户出错");
		}
	
	}

	@Override
	public List<DBObject> queryUserForCCOD(String ccodEntId,String userAccount, String loginType) throws ServiceException {
		try{
			 DatEntInfoPo ent=ParamUtils.getDatEntInfoPo(ccodEntId);
			 if(ent == null){
				 throw new ServiceException("ccodEntId转换为entId失败，未查询到对应企业ID");
			 }
	         return userMongoDao.query(ent.getEntId(), userAccount, loginType);
		 }
		catch(DataAccessException e){
			e.printStackTrace();
			throw new ServiceException("查询用户出错");
		}
			
	}
	
	
	/*按照用户email查询用户信息*/
	@Override
	public DatEntUserPo queryUserByEmail(String entId, String email)
			throws ServiceException {
		DBObject d=this.queryByEmail(entId, email);
		if(d!=null) {
			DatEntUserPo user=new DatEntUserPo();
			user=(DatEntUserPo)DBObjectUtils.getObject(d, user);
			return user;
		}

		return null;
	}
	
	
	@Override
	public DBObject queryByEmail(String entId, String email) throws ServiceException {
		DBObject dbo = new BasicDBObject();
		dbo.put("entId", entId);
		dbo.put("email", email);
		List<DBObject> list=this.queryUserList(dbo, null);
		if(list!=null&&list.size()>0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<DatEntUserPo> queryRelationUser(String entId, String userName) throws ServiceException {
		try{
			return userMongoDao.queryRelationUser(entId, userName);
		}catch(DataAccessException e){
			e.printStackTrace();
			throw new ServiceException("查询关联用户出错,异常:"+e.getMessage());
		}
	}
	
	
	/*登录名是否已存在*/
	@Override
	public boolean existsLoginName(String userInfos, HttpServletRequest request) throws ServiceException {
		try{
			DBObject dbo = (DBObject) JSON.parse(userInfos);
			if(!dbo.containsField("entId")){
				String entId=DomainUtils.getEntId(request);
				dbo.put("entId", entId);
			}
			
			boolean exist=false;
			long size = userMongoDao.existsLoginName(dbo);			
			if(size>0){
				exist=true;
			}			
			return exist;
		}
		catch(DataAccessException e){
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR, e.getMessage());
		}
	}
	
	
	/*检测邮箱是否已经注册（除了自己的邮箱）*/
	@Override
	public boolean existsEmailsNotMe(String entId, String email,String userId) throws ServiceException {
		// TODO Auto-generated method stub
		try{
			DBObject dbo = new BasicDBObject();
			dbo.put("entId", entId);
			dbo.put("email", email);
			dbo.put("userId", userId);
			
			boolean exist=false;
			long size = userMongoDao.existsEmailsNotMe(dbo);			
			if(size>0){
				exist=true;
			}			
			return exist;
		}
		catch(DataAccessException e){
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR, e.getMessage());
		}
	}

	@Override
	public DatEntUserPo getUserByLName(String entId, String loginName) throws ServiceException {
		try {
			return userMongoDao.getUserByLName(entId,loginName);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR, e.getMessage());
		}
	}

	@Override
	public List<DBObject> queryUser(String entId, String userAccount, String userAccountType) throws ServiceException {
		// TODO Auto-generated method stub
		try{
			List<DBObject> list = userMongoDao.query(entId, userAccount, userAccountType);
			if(list ==null || list.size()==0){
				DBObject queryObject=new BasicDBObject();
				queryObject.put("loginName", userAccount);
				queryObject.put("entId", entId);
				List<DBObject> loginList=this.queryUserList(queryObject, null);
				return loginList;
			}
			return list;
		}
		catch(DataAccessException e){
			e.printStackTrace();
			throw new ServiceException("查询用户出错");
		}
	}

	@Override
	public List<DatEntUserPo> queryOrdinaryByEP(String entId, DatEntUserPo po) throws ServiceException {
		try {
			return userMongoDao.queryOrdinaryByEP(entId,po);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	/*检测用户名是否已存在*/
	@Override
	public boolean existsUserName(String userInfos) throws ServiceException {
		try{
			DBObject dbo = (DBObject) JSON.parse(userInfos);
			boolean exist=false;
			long size = userMongoDao.existsUserName(dbo);			
			if(size>0){
				exist=true;
			}			
			return exist;
		}
		catch(DataAccessException e){
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR, e.getMessage());
		}
	}
}

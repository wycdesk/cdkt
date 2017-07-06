package com.channelsoft.ems.user.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;

import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.vo.PageInfo;
import com.channelsoft.ems.group.po.GroupPo;
import com.channelsoft.ems.register.po.RegisterInfoPo;
import com.channelsoft.ems.user.po.DatEntUserPo;
import com.mongodb.DBObject;

public interface IUserMongoService {
	public int resetUser(DatEntUserPo userPo) throws ServiceException;
	
	public int registerPwd(String userName, String nickName, String password, String entId, String code) throws ServiceException;
	
	public DatEntUserPo getEntUserPo(String entId,String code,String email) throws ServiceException;
	
	public int registerBase(HttpServletRequest request, DatEntUserPo po, String activeCode) throws ServiceException;
	
	public RegisterInfoPo getEntInfo(String entId) throws ServiceException;

	/*添加用户*/
	public int add(DatEntUserPo po, String entId) throws ServiceException;
	
	/*添加用户*/
	public int add(DBObject dbo, String entId) throws ServiceException;
	/**
	 * 修改用户信息
	 * @param userInfos
	 * @return
	 * @throws ServiceException
	 */
	public int updateUser(String userInfos, String updatorId, String updatorName)  throws ServiceException;
	/*
	 * 修改用户信息
	 */
	public int updateUser(DBObject dbo,String entId,String userId)  throws ServiceException;
	/**
	 * 查询用户
	 * @param queryObject
	 * @param pageInfo
	 * @return
	 * @throws ServiceException
	 */
	public List<DBObject> queryUserList(DBObject queryObject, PageInfo pageInfo) throws  ServiceException;
	
	/*检测手机是否已经绑定*/
	public boolean existsPhone(String userInfos) throws ServiceException;
	
	/*检测手机号是否已存在*/
	public boolean existsPhone1(String userInfos) throws ServiceException;
	
	/**
	 * 检测手机号是否已存在-IM页面调用
	 * @param userInfos
	 * @param request
	 * @return
	 * @throws ServiceException
	 * @author wangjie
	 * @time 2016年3月24日下午8:22:21
	 */
	public boolean existsPhoneIM(String userInfos, HttpServletRequest request) throws ServiceException;
	
	/*设置密码*/
	public int setPwd(String userInfos, String updatorId, String updatorName) throws ServiceException;
	
	/*设置密码*/
	public int setPwd(DBObject dbo,String entId,String userId)  throws ServiceException;
	
	 /**
	  * 根据用户id查询用户
	  * @param entId
	  * @param userId
	  * @return
	  * @throws ServiceException
	  */
	 public DatEntUserPo queryUserById(String entId, String userId) throws ServiceException;
	 
	 /*删除用户*/
	 public int deleteUser(String entId, String[] ids)  throws ServiceException;

	 /**
	  * 根据用户id查询用户
	  * @param entId
	  * @param userId
	  * @return
	  * @throws ServiceException
	  */
	 public DBObject queryById(String entId, String userId) throws ServiceException;
	 /**
	  * 查询用户
	  * @param entId
	  * @param userAccount帐号
	  * @param userAccountType帐号类型
	  * @return
	  * @throws ServiceException
	  */
	 public List<DBObject> query(String entId, String userAccount,String userAccountType) throws  ServiceException;
	 
	 /*批量编辑用户*/
	 public int updateBatch(String[] ids,String userInfos, String updatorId, String updatorName)  throws ServiceException, JSONException;
     
	 /*邮箱是否已注册*/
	 public boolean existsEmails(String entId,String email) throws ServiceException;
	 
	 /**
	 * 电话号码是否绑定
	 * @param entId
	 * @param phone
	 * @return
	 * @throws ServiceException
	 * @author wangjie
	 * @time 2016年1月20日下午4:35:01
	 */
	public boolean existsPhone(String entId,String telPhone) throws ServiceException;
	 
	 /**
		 * 查询所有客服和管理员
		 * @return
		 * @throws ServiceException
		 * @author zhangtie
		 */
		public Map<String, List<DatEntUserPo>> queryAgentAndAdmin() throws ServiceException;
		/**
		 * 查询指定企业的所有客服和管理员
		 * @param entId
		 * @return
		 * @throws ServiceException
		 * @author zhangtie
		 */
		public List<DatEntUserPo> queryAgentAndAdmin(String entId) throws ServiceException;




		/**
		 * 添加普通用户接口
		 * @param po
		 * @return
		 * @throws ServiceException
		 */
		public int addCustommer(DatEntUserPo po) throws ServiceException ;
		/**
		 * 根据登陆名查询用户
		 * @param loginName
		 * @param entId
		 * @return
		 * @throws ServiceException
		 */
		public DatEntUserPo queryUserByLoginName(String loginName,String entId) throws ServiceException ;
		 /**
		 * 根据企业编号和用户账号查询用户,如果不存在则创建一个新用户，并发送邮件
		 * @param entId 企业编号
		 * @param userName用户账号
		 * @param userType用户类型 区分是哪一种类型用户账号
		 */
		public DatEntUserPo queryOrAddUser(String entId, String userAccount,String userAccountType) throws ServiceException; 


		
		/*更新用户详情复选框类型自定义字段*/
		public int updateCheckBox(String key,String value,String entId,String userId, String field,String checked,String updatorId,String updatorName)  throws ServiceException;
		
		/*更新用户详情复选框类型自定义字段*/
		public int updateCheckBox(DBObject dbo, String entId, String userId,String field,String checked) throws ServiceException;
		

		
		/**
		 * 修改用户头像-mongodb
		 * @param request
		 * @param po
		 * @return
		 * @throws ServiceException
		 * @author wangjie
		 * @time 2015年12月1日下午5:22:35
		 */
		public String changePhoto(HttpServletRequest request, DatEntUserPo po) throws ServiceException;
		
		public int updateUser(DatEntUserPo po) throws ServiceException;
		
		/**
		 * 客服所属客服组(登陆帐号与用户编号必须有一个不能为空)
		 * @param loginName 登陆帐号
		 * @param userId 用户编号
		 * @param entId 企业编号 不能为空
		 * @return
		 * @throws ServiceException
		 */
		public List<GroupPo> belongGroup(String loginName,String userId,String entId) throws ServiceException;

		
		public int updateInformation(String entId, DatEntUserPo po) throws ServiceException;

		public int updatePassword(String entId, String userId, String newLoginPwd) throws ServiceException;

		public List<DatEntUserPo> queryAll(String enterpriseid) throws ServiceException;

		public List<DatEntUserPo> queryUser(DatEntUserPo po, PageInfo pageInfo) throws ServiceException;

		public List<DatEntUserPo> queryOrdinaryByValue(String entId, String value, String loginName, String userId) throws ServiceException;

		public int mergeUser(String entId, String userMergeId, String userTargetId,String sessionKey) throws ServiceException;

		/*查询用户详情*/
		public List<DBObject> queryUserDetail(DBObject queryObject, PageInfo pageInfo) throws  ServiceException;
		/**
		 * 提供给多渠道的用户添加接口，如果用户不存在则添加用户，存在则更新用户
		 * @param request
		 * @param response
		 * @throws ServiceException
		 */
		public void updateOrAddUserForSource(HttpServletRequest request,HttpServletResponse response) throws  ServiceException;

		/*添加用户*/
		public int add(DBObject dbo, String userId,String entId) throws ServiceException;
		/***
		 * 
		 * @param ccodEntId ccod的企业ID 
		 * @param userAccount 帐号
		 * @param loginType 帐号类型 LoginType枚举
		 * @return
		 * @throws ServiceException
		 */
		public List<DBObject> queryUserForCCOD(String ccodEntId, String userAccount,String loginType) throws ServiceException; 

		
		
		
        /*按用户email查询用户信息*/
		public DatEntUserPo queryUserByEmail(String entId, String email) throws ServiceException;

		
		public DBObject queryByEmail(String entId, String email) throws ServiceException;
		
		/**
		 * 查询关联用户-根据用户名查询
		 * @param entId
		 * @param userName
		 * @return
		 * @throws ServiceException
		 * @author wangjie
		 * @time 2016年3月25日下午8:20:48
		 */
		public List<DatEntUserPo> queryRelationUser(String entId, String userName) throws ServiceException;

        
		/*登录名是否已存在*/
		public boolean existsLoginName(String userInfos, HttpServletRequest request) throws ServiceException;

		
		 /*检测邮箱是否已注册（除了自己的邮箱）*/
		 public boolean existsEmailsNotMe(String entId,String email,String userId) throws ServiceException;

		public DatEntUserPo getUserByLName(String entId, String loginName) throws ServiceException;
		
		/**
		 * 查询用户信息
		 * 先根据userAccountType查询，如果用户不存在，则根据loginName查询
		 * @param entId
		 * @param userAccount
		 * @param userAccountType
		 * @return
		 * @throws ServiceException
		 * @author wangjie
		 * @time 2016年4月29日上午11:57:38
		 */
		public List<DBObject> queryUser(String entId, String userAccount,String userAccountType) throws ServiceException;

		/**
		 * 联络弹屏中自动弹出用户合并时查询用户（根据telPhone和email字段）
		 * @param entId
		 * @param po
		 * @return
		 */
		public List<DatEntUserPo> queryOrdinaryByEP(String entId, DatEntUserPo po) throws ServiceException;
		
		/*检测用户名是否已存在*/
		public boolean existsUserName(String userInfos) throws ServiceException;
}

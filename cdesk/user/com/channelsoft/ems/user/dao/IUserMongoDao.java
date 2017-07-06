package com.channelsoft.ems.user.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.vo.PageInfo;
import com.channelsoft.ems.register.po.RegisterInfoPo;
import com.channelsoft.ems.group.po.AgentPo;
import com.channelsoft.ems.user.po.DatEntUserPo;
import com.mongodb.DBObject;

public interface IUserMongoDao {
	public int resetUser(DatEntUserPo userPo) throws DataAccessException;
	
	public int registerPwd(String userName, String nickName, String password, String entId, String code) throws DataAccessException;
	
	public DatEntUserPo getEntUserPo(String entId,String code,String email) throws DataAccessException;
	
	public int registerBase(DatEntUserPo po) throws DataAccessException;
	
	public RegisterInfoPo getEntInfo(String entId) throws DataAccessException;

	/**
	 * 添加用户
	 * @param dbo
	 * @param entId
	 * @return
	 * @throws DataAccessException
	 */
	public int add(DBObject dbo, String entId) throws DataAccessException;
	/**
	 * 添加用户
	 * @param po
	 * @param entId
	 * @return
	 * @throws DataAccessException
	 */
	public int add(DatEntUserPo po, String entId) throws DataAccessException;
	/**
	 * 获取用户集合名
	 * @param entId
	 * @return
	 * @throws DataAccessException
	 */
	public String getUserTable(String entId)throws DataAccessException;
	/**
	 * 删除集合
	 * @param entId
	 * @return
	 * @throws DataAccessException
	 */
	public int  dropUserTable(String entId)throws DataAccessException;
	/**
	 * 修改用户
	 * @param dbo
	 * @param entId
	 * @param userId
	 * @return
	 * @throws DataAccessException
	 */
	public int updateUser(DBObject dbo,String entId,String userId)  throws DataAccessException;
	/**
	 * 查询用户
	 * @param queryObject
	 * @param pageInfo
	 * @return
	 * @throws DataAccessException
	 */
	public List<DBObject> queryUserList(DBObject queryObject, PageInfo pageInfo) throws DataAccessException;

	/*检测手机是否已经绑定*/
	public long existsPhone(DBObject dbo) throws DataAccessException;
	
	/*检测手机号是否已存在*/
	public long existsPhone1(DBObject dbo) throws DataAccessException;
	
	/*设置密码*/
	public int setPwd(DBObject dbo, String entId, String userId) throws DataAccessException;
	
	/*删除用户*/
	public int deleteUser(String entId, String[] ids);

	/**
	 * 查询用户
	 * @param entId
	 * @param userAccount 用户账号
	 * @param userAccountType 用户账号类型
	 * @throws DataAccessException
	 */
	public List<DBObject> query(String entId, String userAccount,String userAccountType) throws DataAccessException;

	/*邮箱是否已注册*/
	public long existsEmails(DBObject dbo) throws DataAccessException;
	/**
	 * 查询客服和管理员
	 * @param entId
	 * @return
	 * @throws DataAccessException
	 */
	public List<DatEntUserPo> queryAgentAndAdmin(String entId) throws DataAccessException;

	
	/*更新用户详情复选框类型自定义字段*/
	public int updateCheckBox(DBObject dbo,String entId,String userId,String field,String checked)  throws DataAccessException;

	
	/**
	 * 根据邮箱修改用户信息
	 * @param dbo
	 * @param entId
	 * @param email
	 * @return
	 * @throws DataAccessException
	 * @author wangjie
	 * @time 2016年1月20日下午4:48:07
	 */
	public int updateUserByEmail(DBObject dbo,String entId,String email)  throws DataAccessException;
	/**
	 * 更新用户信息
	 * @param dbo
	 * @return优先根据登陆帐号更新，登陆帐号不存在则根据用户id更新
	 * @throws DataAccessException
	 */
	public int updateUser(DBObject dbo) throws DataAccessException ;

	public int updateInformation(String entId, DatEntUserPo po) throws DataAccessException ;

	public int updatePassword(String entId, String userId, String newLoginPwd) throws DataAccessException ;

	public List<DatEntUserPo> queryAll(String enterpriseid) throws DataAccessException ;

	public List<DatEntUserPo> queryUser(DatEntUserPo po, PageInfo pageInfo) throws DataAccessException;

	public List<DatEntUserPo> queryOrdinaryByValue(String entId, String value, String loginName, String userId) throws DataAccessException;

	public int mergeUser(String entId, DatEntUserPo merge, DatEntUserPo target) throws DataAccessException;

    /*查询用户详情*/
	public List<DBObject> queryUserDetail(DBObject queryObject, PageInfo pageInfo) throws DataAccessException;

	
	
	public int add(DBObject dbo,String userId, String entId) throws DataAccessException;
	
	/**
	 * 查询关联用户
	 * @param entId
	 * @param userName
	 * @return
	 * @throws DataAccessException
	 * @author wangjie
	 * @time 2016年3月25日下午8:08:59
	 */
	public List<DatEntUserPo> queryRelationUser(String entId, String userName) throws DataAccessException;

	/*登录名是否已存在*/
	public long existsLoginName(DBObject dbo) throws DataAccessException;

	public DatEntUserPo getEntUserPoById(String entId, String userId) throws DataAccessException;
	
	/*邮箱是否已注册(除了自己的邮箱)*/
	public long existsEmailsNotMe(DBObject dbo) throws DataAccessException;

	public DatEntUserPo getUserByLName(String entId, String loginName) throws DataAccessException;

	public boolean hasActiveUser(String enterpriseid, String id) throws DataAccessException;

	/**
	 * 联络弹屏中自动弹出用户合并时查询用户（根据telPhone和email字段）
	 * @param entId
	 * @param po
	 * @return
	 */
	public List<DatEntUserPo> queryOrdinaryByEP(String entId, DatEntUserPo po) throws DataAccessException;

	/*检测用户名是否已存在*/
	public long existsUserName(DBObject dbo) throws DataAccessException;
}

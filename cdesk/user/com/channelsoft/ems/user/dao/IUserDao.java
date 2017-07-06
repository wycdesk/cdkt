package com.channelsoft.ems.user.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.channelsoft.ems.register.po.RegisterInfoPo;
import com.channelsoft.ems.sso.vo.SsoUserVo;
import com.channelsoft.ems.user.po.DatEntUserPo;

public interface IUserDao {
	/**
	 * 判断该邮箱是否已经注册
	 * @param entId
	 * @param email
	 * @return
	 * @throws DataAccessException
	 */
	//public boolean existsEmails(String entId,String email) throws DataAccessException;

	//public int registerBase(DatEntUserPo po) throws DataAccessException;

	public RegisterInfoPo getEntInfo(String domainName) throws DataAccessException;
	/**
	 * 获取用户编号，从序列中获取
	 * @return
	 * @throws DataAccessException
	 */
	public String getUserId() throws DataAccessException;


	//public DatEntUserPo getEntUserPo(String entId,String code,String email) throws DataAccessException;

	//public String deleteUser(String entId, String code) throws DataAccessException;

	/**
	 * 判断某角色下是否存在用户
	 * @param enterpriseid
	 * @param roleId
	 * @return
	 * @throws DataAccessException
	 * @author zhangtie
	 */
	//public boolean hasActiveUser(String enterpriseid, String roleId) throws DataAccessException;
	/**
	 * 用户登陆
	 * @param loginName
	 * @param password
	 * @return
	 * @throws DataAccessException
	 * @CreateDate: 2013-6-11 下午11:22:19
	 * @author 魏铭
	 */
	//public List<SsoUserVo> login(String enterpriseId,String loginName, String password) throws DataAccessException;

	//public int registerPwd(String userName,String nickName,String password, String entId, String code) throws DataAccessException;
	/**
	 * 查询所有用户
	 * @return
	 * @throws DataAccessException
	 * @author zhangtie
	 */
	//public List<DatEntUserPo> queryAll(String entId) throws DataAccessException;

	//public void resetUser(DatEntUserPo userPo) throws DataAccessException;

	/**
	 * 验证电话号码是否已存在
	 * @param phone
	 * @return
	 * @throws DataAccessException
	 * @author wangjie
	 * @time 2015年11月21日下午3:11:10
	 */
	//public boolean existsPhone(String entId, String phone) throws DataAccessException;
	
	/**
	 * 根据userId查询用户
	 * @param entId
	 * @param userId
	 * @return
	 * @throws DataAccessException
	 * @author wangjie
	 * @time 2015年12月4日下午2:05:12
	 */
	//public DatEntUserPo queryUser(String entId, String userId) throws DataAccessException;

	//public int updateInformation(String entId, String email, String nickName, String userDesc) throws DataAccessException;

	//public int updatePassword(String entId, String email, String newLoginPwd) throws DataAccessException;
	/**
	 * 查询客服和管理员
	 * @param entId
	 * @return
	 * @throws DataAccessException
	 * @author zhangtie
	 */
	//public List<DatEntUserPo> queryAgentAndAdmin(String entId) throws DataAccessException;

	String getUserFieldId() throws DataAccessException;

	public String getDocId(String arg) throws DataAccessException;
	
}

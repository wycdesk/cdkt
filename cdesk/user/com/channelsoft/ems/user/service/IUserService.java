package com.channelsoft.ems.user.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.dao.DataAccessException;

import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.ems.api.po.SendMainResponsePo;
import com.channelsoft.ems.register.po.RegisterInfoPo;
import com.channelsoft.ems.sso.vo.SsoUserVo;
import com.channelsoft.ems.user.po.DatEntUserPo;

public interface IUserService {
	
	/**
	 * 判断该邮箱是否已经注册
	 * @param entId
	 * @param email
	 * @return
	 * @throws DataAccessException
	 */
	//public boolean existsEmails(String entId,String email) throws ServiceException;

	//public int registerBase(HttpServletRequest request, DatEntUserPo po, String activeCode) throws ServiceException;

	public RegisterInfoPo getEntInfo(String domainName) throws ServiceException;

	public SendMainResponsePo sendMail(HttpServletRequest request,DatEntUserPo po,String activeCode,boolean reset) throws Exception;

	//public DatEntUserPo getEntUserPo(String entId,String code,String emial) throws ServiceException;

	//public String deleteUser(String entId, String code) throws ServiceException;
	
	/**
	 * 用户登陆：
	 * 1. 根据账户密码查询用户信息
	 * @param loginName
	 * @param password
	 * @return
	 * @throws ServiceException
	 * @CreateDate: 2013-6-7 下午08:09:31
	 * @author 魏铭
	 */
	public SsoUserVo login(String enterpriseid,String loginName, String password, HttpServletRequest request) throws ServiceException;

	public String getDomainName(HttpServletRequest request) throws ServiceException;

	//public int registerPwd(String userName,String nickName,String password, String entId, String code) throws ServiceException;
	
	//public void resetUser(DatEntUserPo userPo) throws ServiceException;
	/**
	 * 查询所有用户
	 * @return
	 * @throws ServiceException
	 * @author zhangtie
	 */
	//public Map<String, List<DatEntUserPo>> queryAll() throws ServiceException;
	/**
	 * 查询指定企业的所有用户
	 * @param entId
	 * @return
	 * @throws ServiceException
	 * @author zhangtie
	 */
	//public List<DatEntUserPo> queryAll(String entId) throws ServiceException;
	/**
	 * 查询所有客服和管理员
	 * @return
	 * @throws ServiceException
	 * @author zhangtie
	 */
	//public Map<String, List<DatEntUserPo>> queryAgentAndAdmin() throws ServiceException;
	/**
	 * 查询指定企业的所有客服和管理员
	 * @param entId
	 * @return
	 * @throws ServiceException
	 * @author zhangtie
	 */
	//public List<DatEntUserPo> queryAgentAndAdmin(String entId) throws ServiceException;
	/**
	 * 根据userId查询用户信息
	 * @param entId
	 * @param userId
	 * @return
	 * @throws ServiceException
	 * @author wangjie
	 * @time 2015年12月4日下午2:08:56
	 */
	//public DatEntUserPo queryUser(String entId, String userId) throws ServiceException;

	//public int updateInformation(String entId, String email, String nickName, String userDesc) throws ServiceException;

	//public int updatePassword(String entId, String email, String newLoginPwd) throws ServiceException;

	public SendMainResponsePo sendMailMongo(HttpServletRequest request, DatEntUserPo po, String activeCode, boolean b) throws Exception;

}

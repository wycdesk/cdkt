package com.channelsoft.ems.user.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.vo.AjaxResultPo;
import com.channelsoft.cri.vo.PageInfo;
import com.channelsoft.ems.group.po.AgentPo;
import com.channelsoft.ems.group.po.GroupPo;
import com.channelsoft.ems.user.po.DatEntUserPo;
import com.channelsoft.ems.user.po.RolePo;

public interface IUsrManageService {

	/*public boolean existsPhone(String entId,String phone,String userId) throws ServiceException;
	
	public boolean existsPhone1(String entId,String phone,String userId) throws ServiceException;
	
	public List<DatEntUserPo> queryUser(DatEntUserPo po, PageInfo pageInfo) throws ServiceException;
	
	public List<DatEntUserPo> queryLately(DatEntUserPo po, PageInfo pageInfo) throws ServiceException;
	
	public List<RolePo> querySecondLevel(RolePo po) throws ServiceException;
	
	public int delete(String entId, String[] ids) throws ServiceException;
	
	//public int add(DatEntUserPo po) throws ServiceException;
	
	public int update(DatEntUserPo po) throws ServiceException;
	
	public int bindPhone(DatEntUserPo po) throws ServiceException;
	
	public int updateBatch(DatEntUserPo po,String [] ids) throws ServiceException;
	
	
	
	public int updateStatus(DatEntUserPo po) throws ServiceException;
	
	public int setPwd(String password, String entId, String userId) throws ServiceException;
	
	
	
	public List<GroupPo> belongGroup(DatEntUserPo po) throws ServiceException;
	 *//**
	 * 根据企业编号和用户账号查询用户,如果不存在则创建一个新用户，并发送邮件
	 * @param entId 企业编号
	 * @param userName用户账号
	 * @param userType用户类型 区分是哪一种类型用户账号
	 *//*
	 //public DatEntUserPo queryOrAddUser(String entId, String userAccount, String userAccountType) throws ServiceException;
	 *//**
	  * 通用查询用户接口
	  * @param po
	  * @param pageInfo
	  * @return
	  * @throws ServiceException
	  *//*
	 public List<DatEntUserPo> query(DatEntUserPo po, PageInfo pageInfo)  throws ServiceException;
	 *//**
	  * 根据用户id查询用户
	  * @param entId
	  * @param userId
	  * @return
	  * @throws ServiceException
	  *//*
	 public DatEntUserPo queryUserById(String entId, String userId) throws ServiceException;
	 *//**
	  * 查询企业的所有用户
	  * @param entId
	  * @return
	  * @throws ServiceException
	  * @author zhangtie
	  *//*
	 public List<DatEntUserPo> queryEntAll(String entId) throws ServiceException;
	 
	 *//**
	 * 修改用户头像
	 * @param request
	 * @param po
	 * @return
	 * @throws ServiceException
	 * @author wangjie
	 * @time 2015年12月1日下午5:22:35
	 *//*
	public String changePhoto(HttpServletRequest request, DatEntUserPo po) throws ServiceException;
	 
	 *//**
	  * 根据账号和账号类型查询用户
	  * @param entId
	  * @param userAccount
	  * @param userAccountType
	  * @return
	  * @throws ServiceException
	  *//*
	 public List<DatEntUserPo> query(String entId, String userAccount,String userAccountType) throws ServiceException;
	 *//**
	  * 添加普通用户接口
	  * @param po
	  * @return
	  * @throws ServiceException
	  *//*
	 //public int addCustommer(DatEntUserPo po) throws ServiceException;
	 *//**
	  * 更新用户信息
	  * @param po loginName或者UserId必须传1个值
	  * @return
	  * @throws ServiceException
	  *//*
	 public int updateUser(DatEntUserPo po) throws ServiceException;
	 *//**
	  * 根据登陆帐号查询用户
	  * @param entId
	  * @param loginName
	  * @return
	  * @throws ServiceException
	  *//*
	 public DatEntUserPo queryUser(String entId, String loginName) throws ServiceException;

	 
	  * 合并用户
	  
	//public int mergeUser(String entId, String userMerge, String userTarget) throws ServiceException;

	public List<DatEntUserPo> queryOrdinaryByValue(String entId, String value, String email) throws ServiceException;
	*/
	//查询用户上次登录时间
	public String queryLastLogin(String entId,String userId) throws ServiceException;
	
	/*分配客服组*/
	public int assignAgent(String entId,String[] groupId,String agentId,List<AgentPo> agentPos) throws ServiceException;
	
	public String queryFounder(String entId) throws ServiceException;
	
	public List<RolePo> querySecondLevel(RolePo po) throws ServiceException;
	
	public List<GroupPo> belongGroup(DatEntUserPo po) throws ServiceException;
}

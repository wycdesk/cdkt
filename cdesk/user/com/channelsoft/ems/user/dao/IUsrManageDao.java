package com.channelsoft.ems.user.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.vo.AjaxResultPo;
import com.channelsoft.cri.vo.PageInfo;
import com.channelsoft.ems.group.po.AgentPo;
import com.channelsoft.ems.group.po.GroupPo;
import com.channelsoft.ems.user.po.DatEntUserPo;
import com.channelsoft.ems.user.po.RolePo;

public interface IUsrManageDao {

	//public boolean existsPhone(String entId,String phone,String userId) throws DataAccessException;
	
	//public boolean existsPhone1(String entId,String phone,String userId) throws DataAccessException;
	
	//public List<DatEntUserPo> queryUser(DatEntUserPo po, PageInfo pageInfo);
	
	//public List<DatEntUserPo> queryLately(DatEntUserPo po, PageInfo pageInfo);
	
	public List<RolePo> querySecondLevel(RolePo po);
	
   // public int delete(String entId, String[] ids) throws DataAccessException;
	
	//public int add(DatEntUserPo po) throws DataAccessException;
	
	//public int update(DatEntUserPo po) throws DataAccessException;
	
	//public int bindPhone(DatEntUserPo po) throws DataAccessException;
		
	//public int updateBatch(DatEntUserPo po,String [] ids) throws DataAccessException;
	
	/*分配客服组*/
	public int assignAgent(String entId,String[] groupId,String agentId,List<AgentPo> agentPos) throws DataAccessException;
	
	//public int updateStatus(DatEntUserPo po) throws DataAccessException;
	
	//public int setPwd(String password, String entId, String userId) throws DataAccessException;
	
	public String queryFounder(String entId);
	
	public List<GroupPo> belongGroup(DatEntUserPo po);
	/**
	 * 根据企业编号和登录账户查询
	 * @param entId 企业编号
	 * @param loginName登录账户
	 * @return
	 * @throws DataAccessException
	 */
	//DatEntUserPo queryUser(String entId, String loginName) throws DataAccessException;
	/**
	 * 查询用户
	 * @param po
	 * @param pageInfo
	 * @return
	 */
	//public List<DatEntUserPo> query(DatEntUserPo po, PageInfo pageInfo);
	
	/**
	 * 批量导入用户时，邮箱已存在，根据邮箱更新用户
	 * @param po
	 * @return
	 * @throws DataAccessException
	 * @author wangjie
	 * @time 2015年11月21日下午3:20:04
	 */
	//public int importUserUpdate(DatEntUserPo po) throws DataAccessException;
	
	/**
	 * 修改用户头像
	 * @param po
	 * @return
	 * @throws DataAccessException
	 * @author wangjie
	 * @time 2015年12月1日下午5:11:30
	 */
	//public int changePhoto(DatEntUserPo po) throws DataAccessException;
	/**
	 * 查询用户
	 * @param entId
	 * @param userAccount 用户账号
	 * @param userAccountType 用户账号类型
	 * @return
	 * @throws DataAccessException
	 */
	//public List<DatEntUserPo> query(String entId,String userAccount,String userAccountType) throws DataAccessException;
	/*
	 * 修改用户信息
	 * 登陆帐号和用户ID必传其一
	 */
	//public int updateUser(DatEntUserPo po) throws DataAccessException;

	//public int mergeUser(String entId,DatEntUserPo merge, DatEntUserPo target) throws DataAccessException;

	//public List<DatEntUserPo> queryOrdinaryByValue(String entId, String value, String email) throws DataAccessException;
	
	/*用户上次登陆的时间*/
	public String queryLastLogin(String entId,String userId) throws DataAccessException;
}

package com.channelsoft.ems.privilege.service;

import java.util.List;

import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.ems.privilege.po.CfgRolePo;
import com.channelsoft.ems.privilege.vo.CfgRoleVo;
import com.channelsoft.ems.sso.vo.SsoRoleVo;

public interface ICfgRoleService {
	/**
	 * 根据角色ID查询其下属的角色列表，包括自身：
	 * 注：此方法调用下面的getRoleList方法，然后进行PO转换返回。
	 * @param roleId
	 * @return
	 * @throws ServiceException
	 * @CreateDate: 2013-6-7 下午08:17:45
	 * @author 魏铭
	 */
	public List<SsoRoleVo> getChildList(String enterpriseid,String roleId) throws ServiceException;
	
	/**
	 * 根据角色ID查询其下属的角色列表，包括自身
	 * @param id
	 * @return
	 * @throws ServiceException
	 * @CreateDate: 2013-6-8 下午03:14:26
	 * @author 魏铭
	 */
	public List<CfgRolePo> getRoleList(String enterpriseid,String id) throws ServiceException;
	/**
	 * 查询roleId的所有下属角色（包括自身），并返回树状结构列表
	 * @return
	 * @throws ServiceException
	 * @author 张铁
	 * @date 2013-7-11
	 */
 	public List<CfgRoleVo> query(String enterpriseid,String roleId) throws ServiceException;  
	
	/**
	 * 添加角色
	 * @param po
	 * @return
	 * @throws ServiceException
	 * @author 张铁
	 * @param enterpriseid 
	 * @date 2013-7-11
	 */
	public int add(String enterpriseid, CfgRolePo po) throws ServiceException;
	
	/**
	 * 更新角色
	 * @param po
	 * @return
	 * @throws ServiceException
	 * @author 张铁
	 * @param enterpriseid 
	 * @date 2013-7-11
	 */
	public int update(String enterpriseid, CfgRolePo po) throws ServiceException;
	
	/**
	 * 删除角色
	 * @param id
	 * @return
	 * @throws ServiceException
	 * @author 张铁
	 * @param id2 
	 * @date 2013-7-11
	 */
	public void delete(String enterpriseid,String id) throws ServiceException;

	/**
	 * 给roleId的用户分配menuIdList所列的权限
	 * @param roleId
	 * @param menuIdList
	 * @author 张铁
	 * @param roleId2 
	 * @date 2013-7-14
	 */
	public void assignPermission(String roleId, String roleId2, String[] menuIdList) throws ServiceException;
	/**
	 * 查询指定id的角色
	 * @param enterpriseid
	 * @param roleId
	 * @return
	 * @throws ServiceException
	 * @author zhangtie
	 */
	public CfgRolePo queryById(String enterpriseid, String roleId) throws ServiceException;
}

package com.channelsoft.ems.privilege.service;

import java.util.List;
import java.util.Map;

import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.ems.privilege.po.CfgPermissionPo;
import com.channelsoft.ems.privilege.po.CfgRolePermissionPo;
import com.channelsoft.ems.privilege.vo.CfgPermissionVo;
import com.channelsoft.ems.sso.vo.SsoPermissionVo;

public interface ICfgPermissionService {
	/**
	 * 查询所有权限并判断用户是否有权限 1. 查出所有的权限列表 2. 查出用户关联的权限列表 3.
	 * 循环比对，用户关联的权限value=1，没关联的value=0 4. key=平台标识_actionUrl，放入map中
	 * 
	 * @return
	 * @throws ServiceException
	 * @CreateDate: 2013-6-7 下午08:24:55
	 * @author 魏铭
	 * @param enterpriseid
	 */
	public Map<String, String> getPrivilege(String enterpriseid,String roleId)
			throws ServiceException;

	/**
	 * 查询用户角色关联的所有菜单,type=menu
	 * 
	 * @param roleId
	 * @return
	 * @throws ServiceException
	 * @CreateDate: 2013-6-8 下午12:12:29
	 * @author 魏铭
	 * @param enterpriseid
	 */
	public List<SsoPermissionVo> getMenuList(String enterpriseid,String roleId)
			throws ServiceException;

	/**
	 * 添加权限
	 * 
	 * @param po
	 * @return
	 * @throws ServiceException
	 * @CreateDate: 2013-6-8 下午02:53:49
	 * @author 魏铭
	 */
	public int add(CfgPermissionPo po) throws ServiceException;

	/**
	 * 修改权限
	 * 
	 * @param po
	 * @return
	 * @throws ServiceException
	 * @CreateDate: 2013-6-8 下午02:55:01
	 * @author 魏铭
	 */
	public int update(CfgPermissionPo po) throws ServiceException;

	/**
	 * 删除权限及其子权限： 先递归查出权限，然后全部删除。参考usportal的menu。
	 * 
	 * @param po
	 * @return
	 * @throws ServiceException
	 * @CreateDate: 2013-6-8 下午02:55:01
	 * @author 魏铭
	 */
	public int delete(String enterpriseid, String id) throws ServiceException;

	/**
	 * 查询用户的菜单，生成树状列表展示
	 * 
	 * @param roleId
	 * @param sessionKey
	 * @return
	 * @throws ServiceException
	 * @CreateDate: 2013-6-24 下午12:20:38
	 * @author 魏铭
	 */
	public List<CfgPermissionVo> queryMenuTree(String enterpriseid,
			String roleId, String sessionKey) throws ServiceException;

	/**
	 * 查询所有权限，并返回树状结构列表
	 * 
	 * @return
	 * @throws ServiceException
	 * @CreateDate: 2013-7-9 下午05:22:35
	 * @author 魏铭
	 */
	public List<CfgPermissionVo> query(String enterpriseid) throws ServiceException;

	/**
	 * 系统配置中的角色-权限关系
	 * 
	 * @return
	 * @throws ServiceException
	 * @CreateDate: 2015-4-16 10:38
	 * @author 程立涛
	 */

	public List<CfgRolePermissionPo> queryForRolePermission(String roleId)
			throws ServiceException;

	/**
	 * 租户自己的角色-权限关系
	 * 
	 * @return
	 * @throws ServiceException
	 * @CreateDate: 2015-4-16 10:28
	 * @author 程立涛
	 */
	public List<CfgRolePermissionPo> queryForRolePermission(
			String enterpriseid, String roleId) throws ServiceException;

	/**
	 * 查询二级菜单
	 * @param enterpriseid
	 * @param roleId
	 * @param sessionKey
	 * @param parentId 一级菜单（父节点）id
	 * @return
	 * @throws ServiceException
	 * @author zhangtie
	 */
	public List<CfgPermissionVo> querySubMenuTree(String enterpriseid, String roleId, String sessionKey, String parentId) throws ServiceException;

}

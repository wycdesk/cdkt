package com.channelsoft.ems.privilege.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.channelsoft.ems.privilege.po.CfgRolePermissionPo;


public interface ICfgRolePermissionDao {

	/**
	 * 添加权限
	 * 
	 * @param roleId
	 * @param menuIdList
	 * @author 张铁
	 * @date 2013-7-14
	 */
	void assignPermission(String enterpriseid, String roleId,
			String[] menuIdList) throws DataAccessException;

	/**
	 * 租户添加权限
	 * 
	 * @param roleId
	 * @param menuIdList
	 * @author 程立涛
	 * @date 2015-04-15 17:15
	 */
	void assignEntPermission(String enterpriseid, String roleId,
			String[] menuIdList) throws DataAccessException;

	/**
	 * 查询roleId对应的角色的权限
	 * 
	 * @param roleId
	 * @return
	 * @author 张铁
	 * @date 2013-7-14
	 */
	public List<CfgRolePermissionPo> queryForRolePermission(String roleId)
			throws DataAccessException;

	/**
	 * 删除roleId对应角色的所有权限
	 * 
	 * @param roleId
	 * @return
	 * @author 张铁
	 * @date 2013-7-14
	 */
	int deleteRolePermission(String enterpriseid, String roleId)
			throws DataAccessException;

	/**
	 * 删除租户数据库中roleId对应角色的所有权限
	 * 
	 * @param roleId
	 * @return
	 * @author 程立涛
	 * @date 2015-04-15 17:11
	 */
	int deleteEntRolePermission(String enterpriseid, String roleId)
			throws DataAccessException;

	/**
	 * 查询租户数据库中roleID对应的权限角色关系
	 * 
	 * @param roleId
	 *            enterpriseid
	 * @return
	 * @author 程立涛
	 * @date 2015-04-16 10:01
	 */

	List<CfgRolePermissionPo> queryForRolePermission(String enterpriseid,
			String roleId) throws DataAccessException;


}

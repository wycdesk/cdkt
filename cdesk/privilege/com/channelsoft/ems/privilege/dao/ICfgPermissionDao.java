package com.channelsoft.ems.privilege.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.channelsoft.ems.privilege.po.CfgPermissionPo;
import com.channelsoft.ems.privilege.vo.CfgPermissionVo;


public interface ICfgPermissionDao {
	/**
	 * 查询权限。
	 * @param roleId
	 * @param type
	 * @return
	 * @throws DataAccessException
	 * @CreateDate: 2013-6-12 上午12:12:28
	 * @author 魏铭
	 */
	public List<CfgPermissionVo> query(String entId, CfgPermissionPo po) throws DataAccessException;
	/**
	 * 根据角色查询权限.
	 * type可为空。
	 * @param roleId
	 * @param tyoe
	 * @return
	 * @CreateDate: 2013-6-12 上午12:18:50
	 * @author 魏铭
	 */
	public List<CfgPermissionVo> queryByRole(String enterpriseid,String roleId, String type) throws DataAccessException;
	
	/**
	 * 添加权限记录
	 * @param po 不带id的po（id是自增的）
	 * @return
	 * @throws DataAccessException
	 * @author 张铁
	 * @date 2013-7-9
	 */
	public int add(CfgPermissionPo po) throws DataAccessException;

	/**
	 * 修改权限记录
	 * @param po 带id的po，id表示要更新的对象
	 * @return
	 * @throws DataAccessException
	 * @author 张铁
	 * @date 2013-7-9
	 */
	public int update(CfgPermissionPo po) throws DataAccessException;
	
	/**
	 * 删除权限记录
	 * @param id
	 * @return
	 * @throws DataAccessException
	 * @author 张铁
	 * @date 2013-7-9
	 */
	public int delete(String id) throws DataAccessException;
	
	/**
	 * 查询parentId的一级子权限，删除时用
	 * @param parentId
	 * @return
	 * @throws DataAccessException
	 * @author 张铁
	 * @date 2013-7-9
	 */
	public List<CfgPermissionPo> queryForChildren(String parentId) throws DataAccessException;
	/**
	 * 判断是否有id平台对应的权限存在
	 * @param id
	 * @return
	 * @throws DataAccessException
	 * @Author 张铁
	 * @date 2013-9-3
	 */
	public boolean hasPlatform(String id) throws DataAccessException;
}

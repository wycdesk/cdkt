package com.channelsoft.ems.privilege.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.channelsoft.cri.vo.PageInfo;
import com.channelsoft.ems.privilege.po.CfgRolePo;

public interface ICfgRoleDao {
	/**
	 * 条件查询角色
	 * @param enterpriseid
	 * @param po
	 * @return
	 * @throws DataAccessException
	 * @author zhangtie
	 */
	public List<CfgRolePo> query(String enterpriseid, CfgRolePo po) throws DataAccessException;
	/**
	 * 查询所有 角色
	 * @param id
	 * @return
	 * @CreateDate: 2013-6-12 上午12:06:13
	 * @author 魏铭
	 */
	public List<CfgRolePo> getAll(String enterpriseid,PageInfo page) throws DataAccessException;
	
	/**
	 * 添加角色记录
	 * @param po
	 * @return
	 * @throws DataAccessException
	 * @author 张铁
	 * @param enterpriseid 
	 * @date 2013-7-11
	 */
	public int add(String enterpriseid, CfgRolePo po) throws DataAccessException;
	
	/**
	 * 更新角色记录
	 * @param po
	 * @return
	 * @throws DataAccessException
	 * @author 张铁
	 * @param enterpriseid 
	 * @date 2013-7-11
	 */
	public int update(String enterpriseid, CfgRolePo po) throws DataAccessException;
	
	/**
	 * 删除角色记录
	 * @param id
	 * @return
	 * @throws DataAccessException
	 * @author 张铁
	 * @param id2 
	 * @date 2013-7-11
	 */
	public int delete( String enterpriseid,String id) throws DataAccessException;

	/**
	 * 判断角色名是否存在
	 * @param roleName
	 * @return
	 * @author 张铁
	 * @date 2013-7-14
	 */
	public boolean isExist(String enterpriseid,String roleName);

	/**
	 * 查询子角色
	 * @param id
	 * @return
	 * @author 张铁
	 * @date 2013-7-14
	 */
	public List<CfgRolePo> queryForChildren(String enterpriseid,String id);
	/**
	 * 从序列获取角色编号
	 * @return
	 * @throws DataAccessException
	 * @author zhangtie
	 */
	public String getSequence() throws DataAccessException;

}

package com.channelsoft.ems.privilege.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import com.channelsoft.cri.constant.BaseErrCode;
import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.service.BaseService;
import com.channelsoft.ems.privilege.constant.RoleConstants;
import com.channelsoft.ems.privilege.dao.ICfgRoleDao;
import com.channelsoft.ems.privilege.dao.ICfgRolePermissionDao;
import com.channelsoft.ems.privilege.po.CfgRolePo;
import com.channelsoft.ems.privilege.service.ICfgRoleService;
import com.channelsoft.ems.privilege.vo.CfgRoleVo;
import com.channelsoft.ems.sso.vo.SsoRoleVo;
import com.channelsoft.ems.user.dao.IUserDao;
import com.channelsoft.ems.user.dao.IUserMongoDao;

public class CfgRoleServiceImpl extends BaseService implements ICfgRoleService {

	@Autowired
	ICfgRoleDao roleDao;
	@Autowired
	IUserDao userDao;
	@Autowired
	IUserMongoDao userMongoDao;
	@Autowired
	ICfgRolePermissionDao rolePermissionDao;

 	private List<CfgRolePo> getAll(String enterpriseid) throws ServiceException {
		try {
			return roleDao.getAll(enterpriseid,null);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR, e.getMessage());
		}
	} 
	
	@Override
	public List<SsoRoleVo> getChildList(String enterpriseid,String roleId) throws ServiceException {
		List<CfgRolePo> list = this.getRoleList(enterpriseid,roleId);
		List<SsoRoleVo> relist = new ArrayList<SsoRoleVo>();
		for(CfgRolePo role: list)
		{
			SsoRoleVo vo = new SsoRoleVo();
			BeanUtils.copyProperties(role, vo);
			relist.add(vo);
		}
		return relist;
	}

	@Override
	public List<CfgRolePo> getRoleList(String enterpriseid,String id) throws ServiceException {
		try {
			List<CfgRolePo> allList = roleDao.getAll(enterpriseid,null);
			
			List<CfgRolePo> list = new ArrayList<CfgRolePo>();
			for(CfgRolePo role:allList)
			{
				if (id.equals(role.getId()))
				{
					list.add(role);
					list.addAll(this.getChild(id, allList));
					break;
				}
			}
			return list;
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR, e.getMessage());
		}
	}
	
	/**
	 * 递归查询子角色
	 * @param id
	 * @return
	 * @CreateDate: 2013-6-12 上午12:03:17
	 * @author 魏铭
	 */
	private List<CfgRolePo> getChild(String id, List<CfgRolePo> allList) throws DataAccessException {
		List<CfgRolePo> list = new ArrayList<CfgRolePo>();
		for (CfgRolePo role: allList)
		{
			if (id.equals(role.getParentId()))
			{
				list.add(role);
				list.addAll(this.getChild(role.getId(), allList));
			}
		}
		return list;
	}

	@Override
	public List<CfgRoleVo> query(String enterpriseid,String roleId) throws ServiceException {
		List<CfgRolePo> list = this.getAll(enterpriseid);
		List<CfgRoleVo> relist = new ArrayList<CfgRoleVo>();
		for(CfgRolePo p : list) {
			CfgRoleVo vo = new CfgRoleVo();
			BeanUtils.copyProperties(p, vo);
			relist.add(vo);
		}
		return getTopRole(relist, roleId);
	}
	
	/**
	 * 查询role列表中所有顶级节点，并组成树状链表
	 * @param menuList
	 * @return
	 * @author 张铁
	 * @date 2013-7-11
	 */
	private List<CfgRoleVo> getTopRole(List<CfgRoleVo> roleList, String roleId) {
		List<CfgRoleVo> topList = new ArrayList<CfgRoleVo>();
        for(CfgRoleVo role: roleList){
        	/**
        	 * 处理父子结构，生成链表
        	 */
        	List<CfgRoleVo> childrenRole = new ArrayList<CfgRoleVo>();
            for(int j=0;j<roleList.size();j++){
            	CfgRoleVo role2 = roleList.get(j);
				if( StringUtils.equals(role2.getParentId(), role.getId()) ){
					role.setHasChildren(true);
					role.setState("open");
					// 设定父节点名称
					role2.setParentName(role.getName());
					childrenRole.add(role2);
				}
			}
            role.setChildren(childrenRole);
            /**
             * 父节点为0的，加入topList
             */
        	if(roleId.equals(role.getParentId()) 
        			&& !roleId.equals(role.getId())){
        		role.setParentName("无");
        		topList.add(role);
			}
        }
        return topList;
	}
	
	private static Object syncAddObj = new Object();

	@Override
	public int add(String enterpriseid,CfgRolePo po) throws ServiceException {
		synchronized (syncAddObj) {
			try {
				if (roleDao.isExist(enterpriseid,po.getName())) {
					throw new ServiceException(BaseErrCode.GENERAL_ERROR, "角色名不能重复");
				}
				po.setId(roleDao.getSequence());
				return roleDao.add(enterpriseid,po);
			} catch (DataAccessException e) {
				e.printStackTrace();
				throw new ServiceException(BaseErrCode.DB_ERROR, e.getMessage(), "添加失败");
			}
		}
	}

	@Override
	public int update(String enterpriseid,CfgRolePo po) throws ServiceException {
		try {
			if (RoleConstants.ROLE_ROOT_NODE_ID().equals(po.getId())) {
				throw new ServiceException(BaseErrCode.GENERAL_ERROR, "超级管理员不允许修改");
			}
			return roleDao.update(enterpriseid,po);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR, e.getMessage(), "修改失败");
		}
	}

	@Override
	@Transactional
	public void delete(String enterpriseid,String id) throws ServiceException {
		try {
			if (RoleConstants.ROLE_ROOT_NODE_ID().equals(id)) {
				throw new ServiceException(BaseErrCode.GENERAL_ERROR, "超级管理员不允许删除");
			}
			/**
			 * 调用递归方法删除角色、子角色和用户
			 */
			this.deleteRole(enterpriseid,id);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR, e.getMessage(), "删除失败");
		}
	}
	/**
	 * 递归处理要删除的角色
	 * @param id
	 * @CreateDate: 2013-7-19 上午10:14:50
	 * @author 张铁
	 * @param id2 
	 */
	@Transactional
	private void deleteRole(String enterpriseid,String id ) throws ServiceException {
		List<CfgRolePo> children = roleDao.queryForChildren(enterpriseid,id);
		for (CfgRolePo po : children) {
			this.deleteRole(enterpriseid,po.getId());
		}
		if (userMongoDao.hasActiveUser(enterpriseid,id)) {
			throw new ServiceException(BaseErrCode.GENERAL_ERROR, "该角色或其子角色有状态正常的用户，不允许删除");
		}
		//userDao.deleteUserWithRole(enterpriseid,id); //删除下属的用户
		roleDao.delete(enterpriseid,id); //删除角色
		rolePermissionDao.deleteRolePermission(enterpriseid,id); //删除角色对应权限绑定关系
	}

	@Override
	@Transactional
	public void assignPermission(String enterpriseid, String roleId, String[] menuIdList) throws ServiceException {
		try {
			rolePermissionDao.deleteRolePermission(enterpriseid,roleId);
			rolePermissionDao.assignPermission(enterpriseid,roleId, menuIdList);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR, e.getMessage(), "权限分配失败");
		}
	}

	@Override
	public CfgRolePo queryById(String enterpriseid, String roleId)
			throws ServiceException {
		try {
			CfgRolePo po = new CfgRolePo();
			po.setId(roleId);
			List<CfgRolePo> list = roleDao.query(enterpriseid,po);
			if (list.size() == 0) {
				throw new ServiceException("查询角色失败");
			}
			return list.get(0);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR, e.getMessage(), "查询角色失败");
		}
	}
	
}

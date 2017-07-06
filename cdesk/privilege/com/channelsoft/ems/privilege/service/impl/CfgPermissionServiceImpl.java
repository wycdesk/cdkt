package com.channelsoft.ems.privilege.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import com.channelsoft.cri.constant.BaseErrCode;
import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.service.BaseService;
import com.channelsoft.ems.privilege.constant.PermissionType;
import com.channelsoft.ems.privilege.dao.ICfgPermissionDao;
import com.channelsoft.ems.privilege.dao.ICfgRolePermissionDao;
import com.channelsoft.ems.privilege.po.CfgPermissionPo;
import com.channelsoft.ems.privilege.po.CfgRolePermissionPo;
import com.channelsoft.ems.privilege.service.ICfgPermissionService;
import com.channelsoft.ems.privilege.vo.CfgPermissionVo;
import com.channelsoft.ems.sso.vo.SsoPermissionVo;

public class CfgPermissionServiceImpl extends BaseService implements
		ICfgPermissionService {

	@Autowired
	ICfgPermissionDao permissionDao;

	@Autowired
	ICfgRolePermissionDao rolePermissionDao;

	@Override
	public Map<String, String> getPrivilege(String enterpriseid, String roleId)
			throws ServiceException {
		/**
		 * 查出所有的权限列表
		 */
		List<CfgPermissionVo> alllist = this.query(enterpriseid, new CfgPermissionPo());
		/**
		 * 查出用户关联的权限列表
		 */
		List<CfgPermissionVo> list = this.queryByRole(enterpriseid, roleId, "");
		try {
			/**
			 * 循环比对，用户关联的权限value=1，没关联的value=0
			 */
			Map<String, String> map = new HashMap<String, String>();

			for (CfgPermissionVo all : alllist) {
				String value = "0";
				for (CfgPermissionVo po : list) {
					if (po.getId().equals(all.getId())) {
						value = "1";
						break;
					}
				}
				/**
				 * key=平台标识_actionUrl，放入map中
				 */
				map.put(all.getPlatformId() + "_" + all.getActionUrl(), value);
			}
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.GENERAL_ERROR,
					e.getMessage());
		}
	}

	@Override
	public List<SsoPermissionVo> getMenuList(String enterpriseid, String roleId)
			throws ServiceException {
		try {
			List<CfgPermissionVo> list = this.queryByRole(enterpriseid, roleId,
					PermissionType.MENU.value);
			List<SsoPermissionVo> relist = new ArrayList<SsoPermissionVo>();
			for (CfgPermissionVo cfg : list) {
				SsoPermissionVo vo = new SsoPermissionVo();
				BeanUtils.copyProperties(cfg, vo);
				relist.add(vo);
			}
			return relist;
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR, e.getMessage());
		}
	}

	private List<CfgPermissionVo> queryByRole(String enterpriseid,
			String roleId, String type) throws ServiceException {
		try {

			return permissionDao.queryByRole(enterpriseid, roleId, type);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR, e.getMessage());
		}
	}

	// 张铁实现
	@Override
	public int add(CfgPermissionPo po) throws ServiceException {
		try {
			if (po.getSortWeight() == null) {
				// 排序默认为0
				po.setSortWeight("0");
			}
			try {
				po.setSortWeightInt(Integer.parseInt(po.getSortWeight()));
			} catch (NumberFormatException e) {
				po.setSortWeightInt(0);
			}
			return permissionDao.add(po);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR, e.getMessage(),
					"添加失败");
		}
	}

	// 张铁实现
	@Override
	public int update(CfgPermissionPo po) throws ServiceException {
		try {
			if (po.getSortWeight() != null) {
				try {
					po.setSortWeightInt(Integer.parseInt(po.getSortWeight()));
				} catch (NumberFormatException e) {
					po.setSortWeightInt(0);
				}
			}
			return permissionDao.update(po);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR, e.getMessage(),
					"修改失败");
		}
	}

	// 张铁实现
	@Override
	@Transactional
	public int delete(String enterpriseid, String id) throws ServiceException {
		try {
			List<CfgPermissionPo> children = permissionDao.queryForChildren(id);
			for (CfgPermissionPo po : children) {
				this.delete(enterpriseid, po.getId());
			}
			// 删除整体数据库中的权限
			return permissionDao.delete(id);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR, e.getMessage(),
					"删除失败");
		}
	}

	/**
	 * 查询所有权限列表 注意：这里没有处理父子关系。
	 * 
	 * @param po
	 * @return
	 * @throws ServiceException
	 * @CreateDate: 2013-7-9 下午05:15:10
	 * @author 魏铭
	 */
	public List<CfgPermissionVo> query(String entId, CfgPermissionPo po)
			throws ServiceException {
		try {
			return permissionDao.query(entId, po);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR, e.getMessage());
		}
	}

	@Override
	public List<CfgPermissionVo> queryMenuTree(String enterpriseid,
			String roleId, String sessionKey) throws ServiceException {
		/**
		 * 查询当前角色ID关联的菜单
		 */
		List<CfgPermissionVo> menuList = this.queryByRole(enterpriseid, roleId,
				PermissionType.MENU.value);
		/**
		 * 查询所有的权限（因为有些父节点可能没关联）
		 */
		CfgPermissionPo po = new CfgPermissionPo();
		po.setType(PermissionType.MENU.value);
		List<CfgPermissionVo> allMenuList = this.query(enterpriseid, po);
		/**
		 * 处理父节点到menuList中
		 */
		this.addParents(menuList, allMenuList);
		/**
		 * 获取最顶层的节点，并形成链表
		 */
		return getTopMenu(enterpriseid, menuList, sessionKey, "0");
	}

	/**
	 * 查询menu列表中所有顶级节点，并组成树状链表 同时处理URL参数
	 * 
	 * @param menuList
	 * @return
	 * @CreateDate: 2013-6-24 下午06:28:43
	 * @author 魏铭
	 */
	private List<CfgPermissionVo> getTopMenu(String enterpriseid,
			List<CfgPermissionVo> menuList, String sessionKey, String rootId) {
		List<CfgPermissionVo> topList = new ArrayList<CfgPermissionVo>();
		for (CfgPermissionVo menu : menuList) {
			/**
			 * 根据permissionUrl处理SessionKey和enterpriseid，生成实际访问地址
			 */
			if (menu.getPermissionUrl() != null
					&& !menu.getPermissionUrl().trim()
							.equals(menu.getRootUrl()) && sessionKey != null) {
				StringBuffer url = new StringBuffer(menu.getPermissionUrl());
				if (StringUtils.isNotBlank(menu.getParameter())) {// 说明有参数
					url.append("?sessionKey=").append(sessionKey)
							.append("&enterpriseid=").append(enterpriseid)
							.append("&").append(menu.getParameter());
				} else {
					url.append("?sessionKey=").append(sessionKey)
							.append("&enterpriseid=").append(enterpriseid);
				}
				menu.setUrl(replaceWWWtoEntId(url.toString(), enterpriseid));
			} else {
				menu.setUrl("");
			}
			/**
			 * 处理父子结构，生成链表
			 */
			List<CfgPermissionVo> childrenMenu = new ArrayList<CfgPermissionVo>();
			for (int j = 0; j < menuList.size(); j++) {
				CfgPermissionVo menu2 = menuList.get(j);
				if (StringUtils.equals(menu2.getParentId(), menu.getId())) {
					menu.setHasChildren(true);
					menu.setState("open");
					// 设定父节点名称
					menu2.setParentName(menu.getName());
					childrenMenu.add(menu2);
				}
			}
			menu.setChildren(childrenMenu);
			/**
			 * 父节点为0的，加入topList
			 */
			if (rootId.equals(menu.getParentId())
					&& !rootId.equals(menu.getId())) {
				topList.add(menu);
			}
		}
		return topList;
	}

	private String replaceWWWtoEntId(String url, String enterpriseid) {
		return url.replaceFirst("www", enterpriseid);
	}

	/**
	 * 从allList中添加父节点到menuList中。
	 * 
	 * @param menuList
	 * @param allMenuList
	 * @throws Exception
	 * @CreateDate: 2013-6-24 下午01:16:48
	 * @author 魏铭
	 */
	private void addParents(List<CfgPermissionVo> menuList,
			List<CfgPermissionVo> allMenuList) {
		List<CfgPermissionVo> relist = new ArrayList<CfgPermissionVo>();
		relist.addAll(menuList);
		for (CfgPermissionVo menu : relist) {
			this.getParentMenus(menu, allMenuList, menuList);
		}
	}

	/**
	 * 1. 获取child的父节点； 2. 判断父节点是否在menuList中，没有在则加入； 3. 递归。
	 * 
	 * @param child
	 * @param allMenuList
	 * @param addList
	 * @CreateDate: 2013-6-24 下午01:14:03
	 * @author 魏铭
	 */
	private void getParentMenus(CfgPermissionVo child,
			List<CfgPermissionVo> allMenuList, List<CfgPermissionVo> menuList) {
		for (CfgPermissionVo parent : allMenuList) {
			if (parent.getId().equals(child.getParentId())) {
				/**
				 * 取到父节点，判断是否已经在menuList中
				 */
				boolean mIsInAdd = false;
				for (CfgPermissionVo menu : menuList) {
					if (parent.getId().equals(menu.getId())) {
						mIsInAdd = true;
						break;
					}
				}
				if (!mIsInAdd) {
					menuList.add(parent);
				}
				/**
				 * 递归
				 */
				this.getParentMenus(parent, allMenuList, menuList);
				break;
			}
		}
	}

	@Override
	public List<CfgPermissionVo> query(String enterpriseid)
			throws ServiceException {
		List<CfgPermissionVo> menuList = this.query(enterpriseid, new CfgPermissionPo());
		return getTopMenu(enterpriseid, menuList, null, "0");
	}

	@Override
	public List<CfgRolePermissionPo> queryForRolePermission(String roleId)
			throws ServiceException {
		try {
			return rolePermissionDao.queryForRolePermission(roleId);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR, e.getMessage(),
					"查询角色的权限失败");
		}
	}

	@Override
	public List<CfgRolePermissionPo> queryForRolePermission(
			String enterpriseid, String roleId) throws ServiceException {
		try {
			return rolePermissionDao.queryForRolePermission(enterpriseid,
					roleId);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR, e.getMessage(),
					"查询角色的权限失败");
		}
	}

	@Override
	public List<CfgPermissionVo> querySubMenuTree(String enterpriseid, String roleId, String sessionKey, String parentId) throws ServiceException {
		if (StringUtils.isBlank(parentId)) {
			return null;
		}
		List<CfgPermissionVo> level1Menu = this.queryMenuTree(enterpriseid, roleId, sessionKey);
		for (CfgPermissionVo vo : level1Menu) {
			if (parentId.equals(vo.getId())) {
				return (List<CfgPermissionVo>) vo.getChildren();
			}
		}
		return null;
	}
}

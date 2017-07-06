package com.channelsoft.ems.privilege.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.channelsoft.cri.constant.BaseConstants;
import com.channelsoft.cri.constant.BaseErrCode;
import com.channelsoft.cri.controller.BaseController;
import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.logger.ManageLogUtils;
import com.channelsoft.cri.vo.AjaxResultPo;
import com.channelsoft.cri.vo.DataResultVo;
import com.channelsoft.ems.privilege.po.CfgPermissionPo;
import com.channelsoft.ems.privilege.po.CfgRolePermissionPo;
import com.channelsoft.ems.privilege.service.ICfgPermissionService;
import com.channelsoft.ems.privilege.vo.CfgPermissionVo;
import com.channelsoft.ems.sso.util.SsoSessionUtils;
import com.channelsoft.ems.sso.vo.SsoEntInfoVo;

@Controller
@RequestMapping("/permission")
public class CfgPemissionController extends BaseController {
	@Autowired
	ICfgPermissionService permissionService;

	@RequestMapping(value = "/list")
	public String list(HttpServletRequest request) {
		return "privilege2/permission/list";
	}

	@RequestMapping(value = "/permissionList")
	public String permissionList(HttpServletRequest request) {
		return "entinfo/permission/list";
	}

	@RequestMapping(value = "/gotoPermissionSelect")
	public String gotoPermissionSelect(HttpServletRequest request) {
		return "privilege2/permission/permissionSelect";
	}

	@RequestMapping(value = "/query")
	@ResponseBody
	public DataResultVo query(HttpServletRequest request) {
		long t1 = System.currentTimeMillis();
		DataResultVo res = new DataResultVo();
		try {
			SsoEntInfoVo entiInfo = SsoSessionUtils.getEntInfo(request);
			String enterpriseid = entiInfo.getEntId();

			List<CfgPermissionVo> list = permissionService.query(enterpriseid);
			res.setTotal(list.size());
			res.setRows(list);
		} catch (ServiceException e) {
			res.setMessage(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			res.setMessage("查询权限失败");
		}
		long t2 = System.currentTimeMillis();
		this.logDebug("查询菜单方法花费时间：" + (t2 - t1) + "毫秒");
		return res;
	}

	/**
	 * @title 租户开户分配权限时调用
	 * @param request
	 * @param roleId
	 * @return
	 * @author 程立涛
	 * @date 2015-4-16 10:26
	 */
	@RequestMapping(value = "/queryForCheckedListEntinfo/{roleId}/{enterpriseid}")
	@ResponseBody
	public DataResultVo queryForCheckedListEntinfo(HttpServletRequest request,
			@PathVariable("roleId") String roleId,
			@PathVariable("enterpriseid") String enterpriseid) {
		DataResultVo res = new DataResultVo();
		try {
			List<CfgPermissionVo> list = permissionService.query(enterpriseid);
			List<CfgRolePermissionPo> rpList = permissionService
					.queryForRolePermission(enterpriseid, roleId);
			setChecked(list, rpList);
			res.setTotal(list.size());
			res.setRows(list);
		} catch (ServiceException e) {
			res.setMessage(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			res.setMessage("查询权限失败");
		}
		return res;
	}

	/**
	 * @title 租户自己的角色分配权限时调用
	 * @param request
	 * @param roleId
	 * @return
	 * @author 程立涛
	 * @date 2015-4-16 10:30
	 */
//	@RequestMapping(value = "/queryForCheckedListEntRoleInfo/{roleId}")
//	@ResponseBody
//	public DataResultVo queryForCheckedListEntRoleInfo(
//			HttpServletRequest request, @PathVariable("roleId") String roleId) {
//		DataResultVo res = new DataResultVo();
//		try {
//			SsoEntInfoVo entiInfo = SsoSessionUtils.getEntInfo(request);
//			String enterpriseid = entiInfo.getEntId();
//			List<CfgPermissionVo> list = permissionService
//					.queryEntPermission(enterpriseid);
//
//			List<CfgRolePermissionPo> rpList = permissionService
//					.queryForRolePermission(enterpriseid, roleId);
//			setChecked(list, rpList);
//			res.setTotal(list.size());
//			res.setRows(list);
//		} catch (ServiceException e) {
//			res.setMessage(e.getMessage());
//		} catch (Exception e) {
//			e.printStackTrace();
//			res.setMessage("查询权限失败");
//		}
//		return res;
//	}

	@SuppressWarnings("unchecked")
	void setChecked(List<CfgPermissionVo> pList,
			List<CfgRolePermissionPo> rpList) {
		for (CfgPermissionVo p : pList) {
			for (CfgRolePermissionPo rp : rpList) {
				if (p.getId().equals(rp.getPermissionId())) {
					p.setCheck(true);
				}
			}
			if (p.isHasChildren()) {
				setChecked((List<CfgPermissionVo>) p.getChildren(), rpList);
			}
		}
	}

	@RequestMapping(value = "/queryMenuTree")
	@ResponseBody
	public DataResultVo queryUserMenuTree(HttpServletRequest request) {
		DataResultVo res = new DataResultVo();
		try {
			String sessionKey = SsoSessionUtils.getSessionKey(request);
			String roleId = SsoSessionUtils.getUserInfo(request).getRoleId();

			SsoEntInfoVo entiInfo = SsoSessionUtils.getEntInfo(request);
			String enterpriseid = entiInfo.getEntId();

			List<CfgPermissionVo> list = permissionService.queryMenuTree(
					enterpriseid, roleId, sessionKey);
			res.setTotal(list.size());
			res.setRows(list);
		} catch (ServiceException e) {
			res.setMessage(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			res.setMessage("获取菜单失败!");
		}
		return res;
	}

	@RequestMapping(value = "/queryPermissionTree")
	@ResponseBody
	public DataResultVo queryPermissionTree(HttpServletRequest request) {
		DataResultVo res = new DataResultVo();
		try {
			String sessionKey = SsoSessionUtils.getSessionKey(request);
			String roleId = SsoSessionUtils.getUserInfo(request).getRoleId();

			SsoEntInfoVo entiInfo = SsoSessionUtils.getEntInfo(request);
			String enterpriseid = entiInfo.getEntId();

			List<CfgPermissionVo> list = permissionService.queryMenuTree(
					enterpriseid, roleId, sessionKey);
			res.setTotal(list.size());
			res.setRows(list);
		} catch (ServiceException e) {
			res.setMessage(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			res.setMessage("获取菜单失败!");
		}
		return res;
	}

	@ResponseBody
	@RequestMapping("/add")
	public AjaxResultPo add(CfgPermissionPo po, HttpServletRequest request) {
		try {
			permissionService.add(po);
			ManageLogUtils.AddSucess(request, "权限", po.getName(), "");
			return BaseConstants.SUCCESS;
		} catch (ServiceException e) {
			ManageLogUtils.AddFail(request, e, "权限", po.getName(), "");
			return new AjaxResultPo(false, e.getDesc());
		} catch (Exception e) {
			e.printStackTrace();
			ManageLogUtils.AddFail(request, new ServiceException(
					BaseErrCode.GENERAL_ERROR), "权限", po.getName(), e
					.getMessage());
			return new AjaxResultPo(false, "添加失败！");
		}
	}

	@ResponseBody
	@RequestMapping("/delete/{id}")
	public AjaxResultPo delete(@PathVariable("id") String id,
			HttpServletRequest request) {

		SsoEntInfoVo entiInfo = SsoSessionUtils.getEntInfo(request);
		String enterpriseid = entiInfo.getEntId();

		try {
			permissionService.delete(enterpriseid, id);
			ManageLogUtils.AddSucess(request, "权限", id, "");
			return BaseConstants.SUCCESS;
		} catch (ServiceException e) {
			ManageLogUtils.AddFail(request, e, "权限", id, "");
			return new AjaxResultPo(false, e.getDesc());
		} catch (Exception e) {
			e.printStackTrace();
			ManageLogUtils.AddFail(request, new ServiceException(
					BaseErrCode.GENERAL_ERROR), "权限", id, e.getMessage());
			return new AjaxResultPo(false, "删除失败！");
		}
	}

	@ResponseBody
	@RequestMapping("/update")
	public AjaxResultPo update(CfgPermissionPo po, HttpServletRequest request) {
		try {
			permissionService.update(po);
			ManageLogUtils.AddSucess(request, "权限", po.getId(), "");
			return BaseConstants.SUCCESS;
		} catch (ServiceException e) {
			ManageLogUtils.AddFail(request, e, "权限", po.getId(), "");
			return new AjaxResultPo(false, e.getDesc());
		} catch (Exception e) {
			e.printStackTrace();
			ManageLogUtils.AddFail(request, new ServiceException(
					BaseErrCode.GENERAL_ERROR), "权限", po.getId(), e
					.getMessage());
			return new AjaxResultPo(false, "修改失败！");
		}
	}
}

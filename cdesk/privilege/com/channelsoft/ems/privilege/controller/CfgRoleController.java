package com.channelsoft.ems.privilege.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
import com.channelsoft.ems.log.constants.BusinessTypeEnum;
import com.channelsoft.ems.log.constants.LogTypeEnum;
import com.channelsoft.ems.log.service.ILogMongoService;
import com.channelsoft.ems.privilege.po.CfgRolePermissionPo;
import com.channelsoft.ems.privilege.po.CfgRolePo;
import com.channelsoft.ems.privilege.service.ICfgPermissionService;
import com.channelsoft.ems.privilege.service.ICfgRoleService;
import com.channelsoft.ems.privilege.vo.CfgPermissionVo;
import com.channelsoft.ems.privilege.vo.CfgRoleVo;
import com.channelsoft.ems.sso.util.SsoSessionUtils;
import com.channelsoft.ems.sso.vo.SsoEntInfoVo;
import com.channelsoft.ems.user.po.DatEntUserPo;
import com.channelsoft.ems.user.service.IUserMongoService;
import com.channelsoft.ems.user.service.IUsrManageService;

@Controller
@RequestMapping("/role")
public class CfgRoleController extends BaseController{
	@Autowired
	ICfgRoleService roleService;
	@Autowired
	ICfgPermissionService permissionService;
	@Autowired
	IUsrManageService usrManageService;
	@Autowired
	ILogMongoService logMongoService;
	@Autowired
	IUserMongoService userMongoService;
	
	@RequestMapping(value = "/list")
	public String list(HttpServletRequest request, Model model) {
		try {
			SsoEntInfoVo entiInfo = SsoSessionUtils.getEntInfo(request);
			String enterpriseid = entiInfo.getEntId();
			//String roleId = SsoSessionUtils.getUserInfo(request).getRoleId();
			List<CfgRoleVo> list = roleService.query(enterpriseid,"2"); // 2是客服
			List<DatEntUserPo> userList = userMongoService.queryAll(enterpriseid);
			// 统计每个角色下属的用户数
			for (CfgRoleVo role : list) {
				int count = 0;
				for (DatEntUserPo user : userList) {
					if (role.getId().equals(user.getRoleId())) {
						count++;
					}
				}
				role.setUserCount(count);
			}
			model.addAttribute("list", list);
			model.addAttribute("roleCount", list.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "privilege/role/list";
	}

	@RequestMapping(value = "/edit/{roleId}")
	public String gotoEdit(@PathVariable("roleId") String roleId, HttpServletRequest request, Model model) {
		try {
			SsoEntInfoVo entiInfo = SsoSessionUtils.getEntInfo(request);
			String enterpriseid = entiInfo.getEntId();
			CfgRolePo role = roleService.queryById(enterpriseid, roleId);
			model.addAttribute("role", role);
			List<CfgPermissionVo> list = permissionService.query(enterpriseid);
			List<CfgRolePermissionPo> rpList = permissionService.queryForRolePermission(enterpriseid, roleId);
			setChecked(list, rpList);
			model.addAttribute("permissionList", list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "privilege/role/edit";
	}
	
	@RequestMapping(value = "/copy/{roleId}")
	public String gotoCopy(@PathVariable("roleId") String roleId, HttpServletRequest request, Model model) {
		try {
			SsoEntInfoVo entiInfo = SsoSessionUtils.getEntInfo(request);
			String enterpriseid = entiInfo.getEntId();
			CfgRolePo role = roleService.queryById(enterpriseid, roleId);
			model.addAttribute("role", role);
			List<CfgPermissionVo> list = permissionService.query(enterpriseid);
			List<CfgRolePermissionPo> rpList = permissionService.queryForRolePermission(enterpriseid, roleId);
			setChecked(list, rpList);
			model.addAttribute("permissionList", list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "privilege/role/copy";
	}
	
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
	
	@RequestMapping(value = "/query")
	@ResponseBody
	public DataResultVo query(HttpServletRequest request)  {
		long t1 = System.currentTimeMillis();
		DataResultVo res = new DataResultVo();

		SsoEntInfoVo entiInfo = SsoSessionUtils.getEntInfo(request);
		String enterpriseid = entiInfo.getEntId();
		try {
			String roleId = SsoSessionUtils.getUserInfo(request).getRoleId();
			List<CfgRoleVo> list = roleService.query(enterpriseid,roleId);
			res.setTotal(list.size());
			res.setRows(list);
		} catch (ServiceException e) {
			res.setMessage(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			res.setMessage("查询角色失败");
		}
		long t2 = System.currentTimeMillis();
		this.logDebug("查询角色花费时间：" + (t2-t1) + "毫秒");
		return res;
	}
	
	@ResponseBody
	@RequestMapping("/add")
	public AjaxResultPo add(CfgRolePo po, HttpServletRequest request) {
		try {
			SsoEntInfoVo entiInfo = SsoSessionUtils.getEntInfo(request);
			String enterpriseid = entiInfo.getEntId();
			roleService.add(enterpriseid,po);
			ManageLogUtils.AddSucess(request, "角色", po.getName(), "");
			return BaseConstants.SUCCESS;
		} catch (ServiceException e) {
			ManageLogUtils.AddFail(request, e, "角色", po.getName(), "");
			return new AjaxResultPo(false, e.getDesc());
		} catch (Exception e) {
			e.printStackTrace();
			ManageLogUtils.AddFail(request, new ServiceException(BaseErrCode.GENERAL_ERROR), "角色", po.getName(), e.getMessage());
			return new AjaxResultPo(false, "添加失败！");
		}
	}
	
	@ResponseBody
	@RequestMapping("/delete/{id}")
	public AjaxResultPo delete(@PathVariable("id") String id, HttpServletRequest request) {
		try {
			SsoEntInfoVo entiInfo = SsoSessionUtils.getEntInfo(request);
			String enterpriseid = entiInfo.getEntId();
			
			CfgRolePo role = roleService.queryById(enterpriseid, id);
			roleService.delete(enterpriseid,id);
			ManageLogUtils.AddSucess(request, "角色", id, "");
			logMongoService.add(request, LogTypeEnum.DELETE, BusinessTypeEnum.ROLE, id, BusinessTypeEnum.ROLE.desc+"("+role.getName()+")", role);
			return new AjaxResultPo(true, "删除成功！");
		} catch (ServiceException e) {
			ManageLogUtils.AddFail(request, e, "角色", id, "");
			return new AjaxResultPo(false, e.getDesc());
		} catch (Exception e) {
			e.printStackTrace();
			ManageLogUtils.AddFail(request, new ServiceException(BaseErrCode.GENERAL_ERROR), "角色", id, e.getMessage());
			return new AjaxResultPo(false, "删除失败！");
		}
	}
	
	@ResponseBody
	@RequestMapping("/update")
	public AjaxResultPo update(CfgRolePo po, HttpServletRequest request) {
		try {
			SsoEntInfoVo entiInfo = SsoSessionUtils.getEntInfo(request);
			String enterpriseid = entiInfo.getEntId();
			
			roleService.update(enterpriseid,po);
			ManageLogUtils.AddSucess(request, "角色", po.getId(), "");
			return BaseConstants.SUCCESS;
		} catch (ServiceException e) {
			ManageLogUtils.AddFail(request, e, "角色", po.getId(), "");
			return new AjaxResultPo(false, e.getDesc());
		} catch (Exception e) {
			e.printStackTrace();
			ManageLogUtils.AddFail(request, new ServiceException(BaseErrCode.GENERAL_ERROR), "角色", po.getId(), e.getMessage());
			return new AjaxResultPo(false, "修改失败！");
		}
	}
	
	@ResponseBody
	@RequestMapping("/assign/{roleId}/{menuIdList}")
	public AjaxResultPo assign(@PathVariable("roleId") String roleId, @PathVariable("menuIdList") String[] menuIdList, HttpServletRequest request) {
		try {
			SsoEntInfoVo entiInfo = SsoSessionUtils.getEntInfo(request);
			String enterpriseid = entiInfo.getEntId();
			roleService.assignPermission(enterpriseid,roleId, menuIdList);
			ManageLogUtils.AddSucess(request, "角色", roleId, "");
			return BaseConstants.SUCCESS;
		} catch (ServiceException e) {
			ManageLogUtils.AddFail(request, e, "角色", roleId, "");
			return new AjaxResultPo(false, e.getDesc());
		} catch (Exception e) {
			e.printStackTrace();
			ManageLogUtils.AddFail(request, new ServiceException(BaseErrCode.GENERAL_ERROR), "角色", roleId, e.getMessage());
			return new AjaxResultPo(false, "删除失败！");
		}
	}
	
	@ResponseBody
	@RequestMapping("/updateAndAssign/{roleId}/{menuIdList}")
	public AjaxResultPo updateAndAssign(@PathVariable("roleId") String roleId, @PathVariable("menuIdList") String[] menuIdList, String description, String name, HttpServletRequest request) {
		try {
			SsoEntInfoVo entiInfo = SsoSessionUtils.getEntInfo(request);
			String enterpriseid = entiInfo.getEntId();
			
			CfgRolePo po = new CfgRolePo();
			po.setId(roleId);
			po.setDescription(description);
			roleService.update(enterpriseid,po );
			
			roleService.assignPermission(enterpriseid,roleId, menuIdList);
			ManageLogUtils.EditSuccess(request, "角色", roleId, "");
			logMongoService.add(request, LogTypeEnum.UPDATE, BusinessTypeEnum.ROLE, roleId, BusinessTypeEnum.ROLE.desc+"("+name+")", po);
			return new AjaxResultPo(true, "角色 " + name + " 编辑成功！");
		} catch (ServiceException e) {
			ManageLogUtils.EditFail(request, e, "角色", roleId, "");
			return new AjaxResultPo(false, e.getDesc());
		} catch (Exception e) {
			e.printStackTrace();
			ManageLogUtils.EditFail(request, new ServiceException(BaseErrCode.GENERAL_ERROR), "角色", roleId, e.getMessage());
			return new AjaxResultPo(false, "更新失败！");
		}
	}
	
	@ResponseBody
	@RequestMapping("/copyRole/{menuIdList}")
	public AjaxResultPo copyRole(@PathVariable("menuIdList") String[] menuIdList, CfgRolePo po, HttpServletRequest request) {
		try {
			SsoEntInfoVo entiInfo = SsoSessionUtils.getEntInfo(request);
			String enterpriseid = entiInfo.getEntId();
			
			po.setParentId("2"); // 父节点为客服
			po.setIsCustom("1"); // 是自定义角色
			roleService.add(enterpriseid, po);
			
			roleService.assignPermission(enterpriseid, po.getId(), menuIdList);
			ManageLogUtils.AddSucess(request, "角色", po.getId(), "");
			logMongoService.add(request, LogTypeEnum.ADD, BusinessTypeEnum.ROLE, po.getId(), BusinessTypeEnum.ROLE.desc+"("+po.getName()+")", po);
			return new AjaxResultPo(true, "新角色 " + po.getName() + " 复制成功！");
		} catch (ServiceException e) {
			ManageLogUtils.AddFail(request, e, "角色", "", "");
			return new AjaxResultPo(false, e.getDesc());
		} catch (Exception e) {
			e.printStackTrace();
			ManageLogUtils.AddFail(request, new ServiceException(BaseErrCode.GENERAL_ERROR), "角色", "", e.getMessage());
			return new AjaxResultPo(false, "复制失败！");
		}
	}
}

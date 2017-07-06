package com.channelsoft.ems.template.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.vo.AjaxResultPo;
import com.channelsoft.ems.api.client.EntClient;
import com.channelsoft.ems.field.constants.FieldStatusEnum;
import com.channelsoft.ems.field.po.BaseFieldPo;
import com.channelsoft.ems.redis.constant.CacheGroup;
import com.channelsoft.ems.redis.util.ParamUtils;
import com.channelsoft.ems.sso.util.SsoSessionUtils;
import com.channelsoft.ems.sso.vo.SsoUserVo;
import com.channelsoft.ems.template.constants.TemplateStatusEnum;
import com.channelsoft.ems.template.po.TemplatePo;
import com.channelsoft.ems.template.service.ICommHistoryTemplateService;

/**
 * 
 * @Description: 联络历史模版
 * @author chenglitao
 * @date 2016年5月26日 上午10:25:25
 *
 */

@Controller
@RequestMapping("/commHistoryTemplate")
public class CommHistoryTemplateController {

	@Autowired
	ICommHistoryTemplateService commHistoryTemplateService;

	/*
	 * 联络历史模板首页
	 */
	@RequestMapping(value = "/commHistoryTemplates")
	public String commHistoryTemplates(HttpServletRequest request, Model model) {
		List<TemplatePo> activeList = new ArrayList<TemplatePo>();
		List<TemplatePo> deactiveList = new ArrayList<TemplatePo>();
		try {
			SsoUserVo user = SsoSessionUtils.getUserInfo(request);
			List<TemplatePo> list = commHistoryTemplateService
					.queryCommHistoryTemplates(user.getEntId(), "", null);
			if (list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getStatus() != null) {
						if (list.get(i).getStatus()
								.equals(TemplateStatusEnum.NORMAL.value)) {
							activeList.add(list.get(i));
						}
						if (list.get(i).getStatus()
								.equals(TemplateStatusEnum.STOPPED.value)) {
							deactiveList.add(list.get(i));
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("activeList", activeList);
		model.addAttribute("deactiveList", deactiveList);
		model.addAttribute("size1", activeList.size());
		model.addAttribute("size2", deactiveList.size());
		return "template/commHistory/commHistoryTemplates";
	}

	/* 添加联络历史模板页面 */
	@RequestMapping(value = "/addCommHistoryTemplate")
	public String addCommHistoryTemplate(HttpServletRequest request,
			HttpServletResponse response, Model model) throws Exception {

		return "template/commHistory/addCommHistoryTemplate";
	}

	/**
	 * 添加联络历史模板
	 * @param po
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/addCommHistoryTemp")
	public AjaxResultPo addCommHistoryTemp(TemplatePo po, HttpServletRequest request) {
		try {
			SsoUserVo user = SsoSessionUtils.getUserInfo(request);
			int num = commHistoryTemplateService.addCommHistoryTemp(po,
					user.getEntId());

			ParamUtils.refreshCache(CacheGroup.CONTACT_HISTORY_TEMPLATE, user.getEntId());
			ParamUtils.refreshCache(CacheGroup.CONTACT_HISTORY_TEMPLATE_FIELD,
					user.getEntId());

			String title = po.getTempName();
			AjaxResultPo ret = new AjaxResultPo(true, "添加成功");
			ret.setRows(title);
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new AjaxResultPo(false, "提交失败");
	}

	/* 模板启停用，删除（逻辑删除） */
	@ResponseBody
	@RequestMapping(value = "/change/{type}/{key}")
	public AjaxResultPo change(HttpServletRequest request,
			@PathVariable String type, @PathVariable String key) {
		SsoUserVo user = SsoSessionUtils.getUserInfo(request);
		String entId = user.getEntId();
		if (type.equals("delete")) {
			int del = 0;
			int delField = 0;
			try {
				/* 删除模板（逻辑删除） */
				del = commHistoryTemplateService.deleteTemplate(entId, key);

				if (del > 0) {
					/* 删除模板内的字段（逻辑删除） */
					delField = commHistoryTemplateService.deleteFieldByTempId(
							entId, key);

					if (delField > 0) {
						ParamUtils
								.refreshCache(CacheGroup.CONTACT_HISTORY_TEMPLATE, entId);
						ParamUtils.refreshCache(CacheGroup.CONTACT_HISTORY_TEMPLATE_FIELD,
								entId);

						return AjaxResultPo.success("删除成功", delField, null);
					} else {
						return AjaxResultPo.failed(new Exception("模板内字段删除失败"));
					}
				} else {
					return AjaxResultPo.failed(new Exception("模板删除失败"));
				}
			} catch (ServiceException e) {
				e.printStackTrace();
				return AjaxResultPo.failed(e);
			}
		} else {
			int chg = 0;
			try {
				chg = commHistoryTemplateService.changeMode(entId, type, key);
				if (chg > 0) {
					ParamUtils.refreshCache(CacheGroup.CONTACT_HISTORY_TEMPLATE, entId);
					ParamUtils.refreshCache(CacheGroup.CONTACT_HISTORY_TEMPLATE_FIELD,
							entId);

					return AjaxResultPo.success("更改成功", chg, null);
				} else {
					return AjaxResultPo.failed(new Exception("更改失败"));
				}
			} catch (ServiceException e) {
				e.printStackTrace();
				return AjaxResultPo.failed(e);
			}
		}
	}

	/* 模板排序 */
	@ResponseBody
	@RequestMapping("/ajax")
	public AjaxResultPo ajax(HttpServletRequest request, String type, String ids) {
		if (type.equals("sort")) {
			SsoUserVo user = SsoSessionUtils.getUserInfo(request);
			String entId = user.getEntId();
			try {
				int suc = commHistoryTemplateService.sortTemplate(entId, ids);
				if (suc >= 0) {
					ParamUtils.refreshCache(CacheGroup.CONTACT_HISTORY_TEMPLATE, entId);

					return AjaxResultPo.success("排序成功", 0, null);
				} else {
					return AjaxResultPo.failed(new Exception("排序失败"));
				}
			} catch (ServiceException e) {
				e.printStackTrace();
				return AjaxResultPo.failed(e);
			}
		} else {
			return AjaxResultPo.failed(new Exception("unknow type"));
		}
	}

	/* 编辑联络历史模板页面 */
	@RequestMapping(value = "/edit")
	public String edit(String tempId, HttpServletRequest request,
			HttpServletResponse response, Model model) throws Exception {

		List<BaseFieldPo> activeList = new ArrayList<BaseFieldPo>();
		List<BaseFieldPo> deactiveList = new ArrayList<BaseFieldPo>();
		List<TemplatePo> temp = new ArrayList<TemplatePo>();

		try {
			SsoUserVo user = SsoSessionUtils.getUserInfo(request);

			TemplatePo po = new TemplatePo();
			po.setTempId(tempId);
			temp = commHistoryTemplateService.query(user.getEntId(), po, null);

			List<BaseFieldPo> list = commHistoryTemplateService
					.queryFieldsByTempId(user.getEntId(), tempId, null);
			if (list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getStatus() != null) {
						if (list.get(i).getStatus()
								.equals(FieldStatusEnum.NORMAL.value)) {
							activeList.add(list.get(i));
						}
						if (list.get(i).getStatus()
								.equals(FieldStatusEnum.STOPPED.value)) {
							deactiveList.add(list.get(i));
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("activeList", activeList);
		model.addAttribute("deactiveList", deactiveList);
		model.addAttribute("size1", activeList.size());
		model.addAttribute("size2", deactiveList.size());
		model.addAttribute("tempId", tempId);

		model.addAttribute("template", temp.get(0));

		return "template/commHistory/commHistoryTemplateFields";
	}

	/* 自定义字段类型选择页面 */
	@RequestMapping(value = "/fieldType")
	public String fieldType(String tempId, HttpServletRequest request,
			HttpServletResponse response, Model model) throws Exception {

		SsoUserVo user = SsoSessionUtils.getUserInfo(request);
		TemplatePo po = new TemplatePo();
		po.setTempId(tempId);
		List<TemplatePo> temp = commHistoryTemplateService.query(
				user.getEntId(), po, null);

		TemplatePo tpo = new TemplatePo();
		if (temp.size() > 0) {
			tpo = temp.get(0);
		}

		model.addAttribute("template", tpo);
		model.addAttribute("tempId", tempId);

		return "functionManage/field/fieldType";
	}

	/* 添加自定义字段页面 */
	@RequestMapping(value = "/addField")
	public String addField(String tempId, String type,
			HttpServletRequest request, HttpServletResponse response,
			Model model) throws Exception {

		SsoUserVo user = SsoSessionUtils.getUserInfo(request);
		TemplatePo po = new TemplatePo();
		po.setTempId(tempId);
		List<TemplatePo> temp = commHistoryTemplateService.query(
				user.getEntId(), po, null);

		TemplatePo tpo = new TemplatePo();
		if (temp.size() > 0) {
			tpo = temp.get(0);
		}

		request.setAttribute("template", tpo);

		request.setAttribute("tempId", tempId);
		request.setAttribute("type", type);
		return "functionManage/field/addField";
	}

	/**
	 * 添加自定义字段
	 * @param po
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/addDefinedField")
	public AjaxResultPo addDefinedField(BaseFieldPo po, String candidateValue,
			HttpServletRequest request) {
		try {
			if (candidateValue != "") {
				JSONObject json = new JSONObject(candidateValue);
				String[] candidateValue1 = new String[json.length()];

				Iterator iterator = json.keys();
				int i = json.length() - 1;
				String k;
				while (iterator.hasNext()) {
					k = (String) iterator.next();
					candidateValue1[i] = json.getString(k);
					i--;
				}
				po.setCandidateValue(candidateValue1);
			}

			SsoUserVo user = SsoSessionUtils.getUserInfo(request);
			int num = commHistoryTemplateService.addDefinedField(po,
					user.getEntId());

			ParamUtils.refreshCache(CacheGroup.CONTACT_HISTORY_TEMPLATE_FIELD,
					user.getEntId());

			String title = po.getName();
			AjaxResultPo ret = new AjaxResultPo(true, "添加成功");
			ret.setRows(title);
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new AjaxResultPo(false, "提交失败");
	}

	/*
	 * 编辑模板信息（标题）
	 */
	@ResponseBody
	@RequestMapping(value = "/goEdit")
	public AjaxResultPo goEdit(String tempName, String tempId,
			HttpServletRequest request) {
		SsoUserVo user = SsoSessionUtils.getUserInfo(request);
		String entId = user.getEntId();

		TemplatePo po = new TemplatePo();
		po.setTempId(tempId);
		po.setTempName(tempName);

		int edit = 0;
		try {
			edit = commHistoryTemplateService.goEdit(entId, po);

			/* 修改工单中的模板名 */
			EntClient.updateWorkTempName(tempId, tempName, entId,
					user.getSessionKey());
			//TODO

			ParamUtils.refreshCache(CacheGroup.CONTACT_HISTORY_TEMPLATE, entId);
		} catch (ServiceException e) {
			e.printStackTrace();
			return new AjaxResultPo(false, e.getMessage());
		}
		if (edit > 0) {
			return new AjaxResultPo(true, "编辑成功");
		} else {
			return new AjaxResultPo(false, "编辑失败");
		}
	}
}

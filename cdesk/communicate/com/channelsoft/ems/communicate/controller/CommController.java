package com.channelsoft.ems.communicate.controller;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.channelsoft.cri.constant.BaseErrCode;
import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.logger.ManageLogUtils;
import com.channelsoft.cri.logger.SystemLogUtils;
import com.channelsoft.cri.util.CdeskEncrptDes;
import com.channelsoft.cri.util.DBObjectUtils;
import com.channelsoft.cri.util.DomainUtils;
import com.channelsoft.cri.util.JsonUtils;
import com.channelsoft.cri.vo.AjaxResultPo;
import com.channelsoft.cri.vo.PageInfo;
import com.channelsoft.ems.api.client.QueryRecordClient;
import com.channelsoft.ems.communicate.constant.ResultType;
import com.channelsoft.ems.communicate.constant.WorkSource;
import com.channelsoft.ems.communicate.po.CommPo;
import com.channelsoft.ems.communicate.service.ICommService;
 
import com.channelsoft.ems.field.po.UserDefinedFiedPo;
import com.channelsoft.ems.field.service.IUserFieldService;
import com.channelsoft.ems.redis.util.ParamUtils;
import com.channelsoft.ems.sso.util.SsoSessionUtils;
import com.channelsoft.ems.sso.vo.SsoUserVo;
import com.channelsoft.ems.user.po.DatEntUserPo;
import com.channelsoft.ems.user.service.IUserMongoService;
import com.channelsoft.ems.user.service.IUsrManageService;
import com.channelsoft.ems.user.util.PhotoUrlUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

@Controller
@RequestMapping("/communicate")
public class CommController {

	@Autowired
	ICommService commService;
	@Autowired
	IUserMongoService userService;
	@Autowired
	IUsrManageService usrManageService;
	@Autowired
	IUserFieldService userFieldService;
	@Autowired
	IUserMongoService userMongoService;

	
	/**
	 * 联络弹框
	 * @param request
	 * @param commPo
	 * @param model
	 * @return
	 */
	@RequestMapping("/goComm")
	public String goComm(HttpServletRequest request, String startTimeStr,
			CommPo commPo, Model model, String param) {
		String entId = DomainUtils.getEntId(request);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

		try {
			String sessionId = null;
			DBObject commObj = null;
			SsoUserVo op = SsoSessionUtils.getUserInfo(request);
			SystemLogUtils.Debug(String.format(
					"准备联络历史的弹屏，传入用户ID = %s,传入的param = %s", commPo.getUserId(),
					param));
			DatEntUserPo userPo = userService.queryUserById(entId,
					commPo.getUserId());

			if (StringUtils.isBlank(entId)
					|| StringUtils.isBlank(commPo.getUserId()) || op == null) {
				throw new Exception("参数异常,企业ID或传入用户ID为空，或者此用户不存在");
			}
			commPo.setOpId(op.getUserId());
			commPo.setOpName(op.getUserName());
			commPo.setUserName(userPo.getUserName());
			commPo.setLoginName(userPo.getLoginName());
			/*-----------------------根据渠道类型进行不同的处理------------------*/

			SystemLogUtils.Debug("根据渠道类型进行不同的处理，渠道ID:" + commPo.getSource());
			// 电话（呼入呼出
			if (commPo.getSource().equals("5")
					|| commPo.getSource().equals("6")) {
				SystemLogUtils.Debug("进入电话渠道进行处理");
				Date start = new Date(Long.valueOf(startTimeStr));
				;
				commObj = DBObjectUtils.getDBObject(commPo);
				commObj.put("startTime", start);
				commObj.put("endTime", start);
				commObj.put("commTime", "0");

				SystemLogUtils.Debug(String.format(
						"解析json字符串param：%s,获取电话联络的信息和当前坐席的信息", param));
				try {
					JSONObject json = new JSONObject(param);
					JSONObject ext = json.getJSONObject("ext");
					sessionId = ext.getString("sessionId");
					String ccodEntId = json.getString("entId");
					String ccodAgentId = json.getString("agentId");

					DBObject phone = (DBObject) JSON.parse(ext.toString());
					String strDnis = (String) phone.get("strDnis");
					;
					String strAni = (String) phone.get("strAni");
					if (strAni.contains(":")) {
						strAni = strAni.substring(strAni.indexOf(":") + 1);
						phone.put("strAni", strAni);
					}
					if (strDnis.contains(":")) {
						strDnis = strDnis.substring(strDnis.indexOf(":") + 1);
						phone.put("strDnis", strDnis);
					}
					commObj.putAll(phone);
					commObj.put("ccodEntId", ccodEntId);
					commObj.put("ccodAgentId", ccodAgentId);
				} catch (Exception e) {
					e.printStackTrace();
					throw new Exception(String.format("解析param:%s 出错,%s",
							param, e.getMessage()));
				}

			}
			// 保存联络历史
			SystemLogUtils.Debug(String.format("保存联络历史，commObj = %s", commObj));
			commService.saveComm(entId, commObj);
			if (commPo.getSource().equals("5")
					|| commPo.getSource().equals("6")) {
				commObj.put("startTime",
						format.format((Date) commObj.get("startTime")));
				commObj.put("endTime",
						format.format((Date) commObj.get("endTime")));
				commObj.put("createTime",
						format.format((Date) commObj.get("createTime")));
				model.addAttribute("commparam", commObj.toString());
			}
			ManageLogUtils.AddSucess(request, "联络历史",
					"sessionId:" + commPo.getSessionId(), "保存沟通历史成功");

			DBObject queryObject = new BasicDBObject();
			queryObject.put("entId", entId);
			queryObject.put("userId", userPo.getUserId());
			List<DBObject> list = userMongoService.queryUserList(queryObject,
					null);
			DBObject dbUser = list.get(0);
			model.addAttribute("user", dbUser);
			model.addAttribute("op", op);
			model.addAttribute("sessionId", sessionId);
			model.addAttribute("source", commPo.getSource());

			// 获取用户头像
			PhotoUrlUtil.getPhotoUrl(request, model, entId,
					userPo.getPhotoUrl());
			/*
			 * String userStatus=userPo.getUserStatus(); for(UserStatus
			 * e:UserStatus.values()){ if(userStatus.equals(e.value)){
			 * userStatus=e.desc; } }
			 */

			/* 登录账号的用户Id和用户类型 */
			request.setAttribute("userId1", op.getUserId());
			request.setAttribute("userType1", op.getUserType());

			/* 登录账号的昵称和邮箱 */
			request.setAttribute("creatorName", op.getNickName());
			request.setAttribute("creatorId", op.getEmail());
			/* 创始人邮箱和登录人邮箱 */
			model.addAttribute("founderEmail",
					usrManageService.queryFounder(entId));
			model.addAttribute("loginEmail", op.getEmail());
			/* 启用的用户自定义字段 */
			List<UserDefinedFiedPo> activeFieldList = userFieldService
					.queryDefinedFiled(entId, "1", null);
			request.setAttribute("activeFieldList", activeFieldList);

			return "userManage/communicate";
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}

	/**
	 * 保存一条联络数据
	 * @param request
	 * @param commPo
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/saveComm")
	public AjaxResultPo saveComm(HttpServletRequest request,
			String startTimeStr, String endTimeStr, CommPo commPo) {
		int save = -1;
		String entId = DomainUtils.getEntId(request);
		SsoUserVo op = SsoSessionUtils.getUserInfo(request);
		Date start = null;
		Date end = null;
		try {
			if (StringUtils.isBlank(entId)) {
				throw new Exception("企业ID为空");
			}
			if (op == null) {
				throw new Exception("登录信息异常");
			}
			commPo.setOpId(op.getUserId());
			commPo.setOpName(op.getUserName());

			start = new Date(Long.valueOf(startTimeStr));
			end = new Date(Long.valueOf(endTimeStr));
			commPo.setStartTime(start);
			commPo.setEndTime(end);
			DBObject commObj = DBObjectUtils.getDBObject(commPo);
			if (commPo.getSource().equals("5")
					|| commPo.getSource().equals("6")) {
				long commTime = (Long.valueOf(endTimeStr) - Long
						.valueOf(startTimeStr)) / 1000;
				commObj.put("commTime", commTime + "");
			}
			commService.saveComm(entId, commObj);
			ManageLogUtils.AddSucess(request, "联络历史",
					"sessionId:" + commPo.getSessionId(), "保存沟通历史成功");

			return AjaxResultPo.success("保存沟通历史成功", save, null);

		} catch (Exception e) {
			e.printStackTrace();
			ManageLogUtils.AddFail(request, new ServiceException(
					BaseErrCode.GENERAL_ERROR), "联络历史",
					"sessionId:" + commPo.getSessionId(), "保存沟通历史失败");
			return AjaxResultPo.failed(new Exception("保存沟通历史失败"));
		}
	}

	/**
	 * 结束联络（主要是保存结束时间和联络时长）
	 * @param request
	 * @param sessionId
	 * @param startTime
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/endComm")
	public AjaxResultPo endComm(HttpServletRequest request, String source,
			String startTimeStr, String endTimeStr, String commTime,
			String param) {
		String entId = DomainUtils.getEntId(request);
		int endSave = -1;
		String sessionId = null;
		DBObject commObj = new BasicDBObject();
		if (StringUtils.isBlank(entId)) {
			return AjaxResultPo.failed(new Exception("企业ID为空"));
		}
		SystemLogUtils
				.Debug(String
						.format("联络结束，进行联络历史信息更新，param = %s,source = %s,startTimeStr = %s,endTimeStr = %s,commTime = %s",
								param, source, startTimeStr, endTimeStr,
								commTime));

		/*-----------------------根据渠道类型进行不同的处理------------------*/

		try {
			// 电话（呼入呼出）
			if (source.equals("5") || source.equals("6")) {

				Date start = null;
				Date end = null;
				JSONObject json = null;
				String isConnected = null;
				SystemLogUtils.Debug(String.format(
						"解析时间 和电话参数，endTimeStr:%s,startTimeStr:%s", endTimeStr,
						startTimeStr));
				try {
					end = new Date(Long.valueOf(endTimeStr));
					start = new Date(Long.valueOf(startTimeStr));
					json = new JSONObject(URLDecoder.decode(param, "UTF-8"));
					sessionId = json.getJSONObject("ext")
							.getString("sessionId");
					isConnected = json.getJSONObject("ext").getString(
							"isConnected");
					commObj.put("isConnected", isConnected);
					commObj.put("startTime", start);
					commObj.put("endTime", end);
					commObj.put("commTime", commTime);
				} catch (Exception e) {
					e.printStackTrace();
					throw new Exception(String.format(
							"时间格式不正确或传入参数param:%s 解析错误", param));
				}

			}
			endSave = commService.endComm(entId, sessionId, commObj);
			ManageLogUtils.EditSuccess(request, "联络结束",
					"sessionId" + sessionId, "联络结束保存成功");
			return AjaxResultPo.success("联络结束保存成功", 1, null);
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxResultPo.failed(e);
		}
	}

	/**
	 * 保存沟通小结
	 * @param request
	 * @param content
	 * @param sessionId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/saveContent")
	public AjaxResultPo saveContent(HttpServletRequest request, String content,
			String commId, String sessionId) {
		SystemLogUtils.Debug(String.format(
				"保存沟通小结，commId = %s,content = %s,sessionId = %s", commId,
				content, sessionId));

		try {
			if (StringUtils.isBlank(content)) {
				throw new Exception("沟通小结为空");
			}
			String entId = DomainUtils.getEntId(request);
			if (StringUtils.isBlank(entId)) {
				throw new Exception("企业为空");
			}
			commService.saveContent(entId, content, sessionId, commId);
			ManageLogUtils.EditSuccess(request, "沟通小结", "sessionId:"
					+ sessionId, "保存沟通小结成功");
			return AjaxResultPo.success("保存沟通小结成功", 1, null);
		} catch (Exception e) {
			e.printStackTrace();
			SystemLogUtils.Debug(String.format(
					"保存沟通小结，commId = %s,sessionId = %s失败", commId, sessionId));
			return AjaxResultPo.failed(new Exception("保存沟通小结失败"));
		}
	}

	/**
	 * 获取联络历史（分页）
	 * @param request
	 * @param commPo
	 * @param rows
	 * @param page
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getComms")
	public AjaxResultPo getComms(HttpServletRequest request, CommPo commPo,
			Integer rows, Integer page) {
		try {
			SystemLogUtils.Debug(String.format("准备查询联络历史，查询条件 ： %s",
					DBObjectUtils.getDBObject(commPo)));

			String entId = DomainUtils.getEntId(request);
			PageInfo pageInfo = null;
			if (rows != null && page != null) {
				pageInfo = new PageInfo(
						(page.intValue() - 1) * rows.intValue(),
						rows.intValue());
			}
			List<DBObject> comms = null;
			comms = commService.getComms(entId, commPo, pageInfo);
			if (comms != null) {
				SimpleDateFormat format = new SimpleDateFormat(
						"yyy-MM-dd HH:mm:ss");
				for (int i = 0; i < comms.size(); i++) {
					DBObject dbI = comms.get(i);
					dbI.put("source",
							WorkSource.getEnum((String) dbI.get("source")).desc);
					dbI.put("createTime",
							format.format((Date) dbI.get("createTime")));
				}
			}
			return AjaxResultPo.success(
					"查询沟通历史成功",
					pageInfo != null ? pageInfo.getTotalRecords() : comms
							.size(), comms);

		} catch (Exception e) {
			e.printStackTrace();
			return AjaxResultPo.failed(new Exception("查询沟通历史失败"));
		}
	}

	/**
	 * 根据客户ID查询联络历史（不分页）
	 * @param request
	 * @param userId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getCommsByUserId")
	public AjaxResultPo getCommsByUserId(HttpServletRequest request,
			String userId) {
		String entId = DomainUtils.getEntId(request);
		try {
			SystemLogUtils.Debug(String.format("准备根据用户ID查询联络历史，查询userId = %s",
					userId));
			SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");

			List<DBObject> comms = commService.getCommsByUserId(entId, userId);
			for (int i = 0; i < comms.size(); i++) {
				DBObject dbI = comms.get(i);
				dbI.put("createTime",
						format.format((Date) dbI.get("createTime")));
				if (dbI.containsField("startTime")) {
					dbI.put("startTime",
							format.format((Date) dbI.get("startTime")));
				}
				if (dbI.containsField("endTime")) {
					dbI.put("endTime", format.format((Date) dbI.get("endTime")));
				}

				dbI.put("param", JsonUtils.toJson(dbI).toString());
				if (dbI.containsField("isConnected")) {
					dbI.put("isConnected",
							ResultType.getEnum((String) dbI.get("isConnected")).desc);
				}
				if (dbI.containsField("callType")) {
					if (((String) dbI.get("callType"))
							.equals("CallTypeOutbound")) {
						dbI.put("callType", "呼出");
					} else if (((String) dbI.get("callType"))
							.equals("CallTypeInbound")) {
						dbI.put("callType", "呼入");
					}
				} else {
					if (((String) dbI.get("source")).equals("5")) {
						dbI.put("callType", "呼入");
					}
					if (((String) dbI.get("source")).equals("6")) {
						dbI.put("callType", "呼出");
					}
				}
				if (dbI.containsField("message")) {
					if (StringUtils.isNotBlank((String) dbI.get("message"))) {
						SimpleDateFormat fmt = new SimpleDateFormat(
								"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
						JSONArray jsonL = new JSONArray(
								(String) dbI.get("message"));
						for (int j = 0; j < jsonL.length(); j++) {
							String dateStr = jsonL.getJSONObject(j).getString(
									"dateObj");
							Date dt = fmt.parse(dateStr);
							jsonL.getJSONObject(j).put("dateObj",
									format.format(dt));
						}
						dbI.put("message", jsonL.toString());
					}
				}

			}
			return AjaxResultPo.success("查询用户沟通历史成功", comms.size(), comms);
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxResultPo.failed(new Exception("查询用户沟通历史失败"));
		}
	}

	@ResponseBody
	@RequestMapping("/getRecordUrl")
	public AjaxResultPo getRecordUrl(HttpServletRequest request,
			String ccodEntId, String ccodAgentId, String sessionId) {
		try {
			SystemLogUtils.Debug(String.format(
					"准备查询录音地址，ccodEntId = %s,ccodAgentId = %s,sessionId = %s",
					ccodEntId, ccodAgentId, sessionId));
			String entId = DomainUtils.getEntId(request);
			String password = CdeskEncrptDes.decryptST(ParamUtils.getAgentById(
					ccodAgentId, entId).getLoginPwd());
			String recordUrl = QueryRecordClient.getRecordUrl(ccodEntId,
					ccodAgentId, password, sessionId);
			// String
			// recordUrl="http://channelsoft.cdesk.com:8090/workorder/download/HeIsPirate.mp3?sessionKey="+SsoSessionUtils.getSessionKey(request);

			return AjaxResultPo.success("success", 1, recordUrl);
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxResultPo.success("success", 0, "");
		}
	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getTodayWKDetailNum")
	public AjaxResultPo getTodayWKDetailNum(HttpServletRequest request) {
		String entId = DomainUtils.getEntId(request);
		List<DBObject> res = null;
		try {
			res = commService.queryTodayWorkOrderDetailNumber(entId);
		} catch (ServiceException e) {
			e.printStackTrace();
			return AjaxResultPo.failed(e);
		}
		if (res != null) {
			return AjaxResultPo.success("查询沟通历史统计成功", res.size(), res);
		}
		return AjaxResultPo.failed(new Exception("查询沟通历史分类--今日数量统计失败"));
	}

	/**
	 * 查询指定时间段内的各渠道分布数量
	 * @param request
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getRecentServiceNum")
	public AjaxResultPo getRecentServiceNum(HttpServletRequest request,
			String beginTime, String endTime) {
		String entId = DomainUtils.getEntId(request);
		List<DBObject> res = null;
		try {
			res = commService.getRecentServiceNum(entId, beginTime, endTime);
		} catch (ServiceException e) {
			e.printStackTrace();
			return AjaxResultPo.failed(e);
		}
		if (res != null) {
			return AjaxResultPo.success("查询沟通历史统计成功", res.size(), res);
		}
		return AjaxResultPo.failed(new Exception("查询沟通历史分类--近来数量统计失败"));
	}

	/**
	 * 下载录音文件
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/dowonloadRecord")
	public AjaxResultPo dowonloadRecord(HttpServletRequest request,
			HttpServletResponse response, String httpUrl) {
		try {
			SystemLogUtils.Debug(String
					.format("准备下载录音，地址httpUrl = %s", httpUrl));

			if (StringUtils.isBlank(httpUrl)) {
				throw new Exception("录音地址为空");
			}
			String temp = httpUrl.split("\\?")[0];
			String fileName = temp.split("/")[temp.split("/").length - 1];
			URL url = null;
			url = new URL(httpUrl);
			URLConnection conn = url.openConnection();
			OutputStream os = null;
			os = response.getOutputStream();
			response.reset();
			response.addHeader("Content-Disposition", "attachment;filename="
					+ java.net.URLEncoder.encode(fileName, "UTF-8"));
			InputStream inStream = conn.getInputStream();
			byte[] buffer = new byte[1204];
			int byteread = 0, bytesum = 0;
			SystemLogUtils.Debug("准备读取地址所对应的输入流");
			while ((byteread = inStream.read(buffer)) != -1) {
				bytesum += byteread;
				os.write(buffer, 0, byteread);
			}
			os.flush();
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxResultPo.failed(e);
		}
		return AjaxResultPo.successDefault();
	}

	/**
	 * IM聊天结束更新联络历史并保存聊天记录接口
	 * @param request
	 * @param commId
	 * @param message
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/endIMComm")
	public AjaxResultPo endIMComm(HttpServletRequest request, String commId,
			String message, String endTimeStr) {
		String entId = DomainUtils.getEntId(request);
		try {
			SystemLogUtils.Debug(String.format(
					"IM联络结束，准备更新联络信息，commId = %s,message = %s,endTimeStr = %s",
					commId, message, endTimeStr));
			Date endTime = new Date(Long.valueOf(endTimeStr));
			String messages = URLDecoder.decode(message, "UTF-8");

			DBObject update = new BasicDBObject("message", messages);
			update.put("endTime", endTime);
			commService.updateComm(entId, commId, update);
			SystemLogUtils.Debug(String.format("IM联络历史更新成功"));
			return AjaxResultPo.success("联络历史更新成功", 1, commId);
		} catch (Exception e) {
			e.printStackTrace();
			SystemLogUtils.Debug(String.format("IM联络历史更新失败"));
			return AjaxResultPo.failed(e);
		}
	}

	@ResponseBody
	@RequestMapping("/mergeComm")
	public AjaxResultPo mergeComm(HttpServletRequest request,
			String userMergeId, String userTargetId) {
		try {
			SystemLogUtils.Debug(String.format(
					"准备联络历史合并，userMergeId = %s,userTargetId = %s", userMergeId,
					userTargetId));
			String entId = DomainUtils.getEntId(request);
			int merge = commService.mergeComm(entId, userMergeId, userTargetId);
			SystemLogUtils.Debug(String.format("准备联络历史合并成功"));
			return AjaxResultPo.success("success", merge, userMergeId + ":"
					+ userTargetId);
		} catch (ServiceException e) {
			e.printStackTrace();
			SystemLogUtils.Debug(String.format("准备联络历史合并失败"));
			return AjaxResultPo.failed(e);
		}
	}

}

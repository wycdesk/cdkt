package com.channelsoft.ems.log.po;



import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.channelsoft.cri.constant.DateConstants;
import com.channelsoft.ems.log.constants.LogTypeEnum;
import com.channelsoft.ems.sso.util.SsoSessionUtils;
import com.channelsoft.ems.sso.vo.SsoUserVo;

public class CfgUserOperateLogPo implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 11212L;
	
	private String logId;
	private String entId; //登录用户企业ID
	private String userId; //登录用户ID
	private String loginName;//登录账号
	private String userName;//登录用户名

	private String operation; //动作

	private String businessType;//操作业务类型
	private String operateObjectId;//操作对象id
	private String operateObject;//操作对象
	private String sourceContent;//操作对象源内容
	private String destContent;//操作对象目标内容
	private String optTime; //操作时间
	private String loginIp;//操作ip
	
	public CfgUserOperateLogPo(){
		
	}
	/**
	 * 操作日志对象
	 * @param operation    动作
	 * @param businessType 操作业务类型
	 * @param operateObjectId 操作对象id
	 * @param operateObject 操作对象说明
	 * @param request
	 */
	public CfgUserOperateLogPo(String operation,String businessType,String operateObjectId,String operateObject,HttpServletRequest request){
		this.operation = operation;
		this.businessType = businessType;
		this.operateObjectId = operateObjectId;
		this.operateObject = operateObject;
		this.loginIp = request.getRemoteAddr();
		SsoUserVo user=SsoSessionUtils.getUserInfo(request);
		this.userId = user.getUserId();
		this.loginName = user.getLoginName();
		this.userName = user.getUserName();
		this.entId=user.getEntId();
	}
	/**
	 * 操作日志对象
	 * @param operation    动作
	 * @param businessType 操作业务类型
	 * @param operateObjectId 操作对象id
	 * @param operateObject 操作对象说明
	 * @param sourceContent 操作前对象内容
	 * @param destContent   操作后对象内容
	 * @param request
	 */
	public CfgUserOperateLogPo(String operation,String businessType,String operateObjectId,String operateObject,String sourceContent,String destContent,HttpServletRequest request){
		this.operation = operation;
		this.businessType = businessType;
		this.operateObjectId = operateObjectId;
		this.operateObject = operateObject;
		this.loginIp = request.getRemoteAddr();
		this.sourceContent = sourceContent;
		this.destContent = destContent;
		SsoUserVo user=SsoSessionUtils.getUserInfo(request);
		this.userId = user.getUserId();
		this.loginName = user.getLoginName();
		this.userName = user.getUserName();
		this.entId=user.getEntId();
	}
	
	
	/**
	 * 操作日志对象
	 * @param operation    动作
	 * @param businessType 操作业务类型
	 * @param operateObjectId 操作对象id
	 * @param operateObject 操作对象说明
	 * @param request
	 */
	public CfgUserOperateLogPo(String operation,String businessType,String operateObjectId,String operateObject,String loginIp,SsoUserVo user){
		this.operation = operation;
		this.businessType = businessType;
		this.operateObjectId = operateObjectId;
		this.operateObject = operateObject;
		this.loginIp = loginIp;
		this.userId = user.getUserId();
		this.loginName = user.getLoginName();
		this.userName = user.getUserName();
		this.entId=user.getEntId();
	}
	/**
	 * 操作日志对象
	 * @param operation    动作
	 * @param businessType 操作业务类型
	 * @param operateObjectId 操作对象id
	 * @param operateObject 操作对象说明
	 * @param sourceContent 操作前对象内容
	 * @param destContent   操作后对象内容
	 * @param request
	 */
	public CfgUserOperateLogPo(String operation,String businessType,String operateObjectId,String operateObject,String sourceContent,String destContent,String loginIp,SsoUserVo user){
		this.operation = operation;
		this.businessType = businessType;
		this.operateObjectId = operateObjectId;
		this.operateObject = operateObject;
		this.loginIp =loginIp;
		this.sourceContent = sourceContent;
		this.destContent = destContent;
		this.userId = user.getUserId();
		this.loginName = user.getLoginName();
		this.userName = user.getUserName();
		this.entId=user.getEntId();
	}
	public String getOperationStr(){
		return LogTypeEnum.getEnum(this.operation).desc;
	}
	public String getLogTimeStr(){
		return "";
	}

	public String getLogId() {
		return logId;
	}
	public void setLogId(String logId) {
		this.logId = logId;
	}

	public String getLoginIp() {
		return loginIp;
	}
	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getEntId() {
		return entId;
	}
	public void setEntId(String entId) {
		this.entId = entId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getBusinessType() {
		return businessType;
	}
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
	public String getOperateObjectId() {
		return operateObjectId;
	}
	public void setOperateObjectId(String operateObjectId) {
		this.operateObjectId = operateObjectId;
	}
	public String getOperateObject() {
		if(StringUtils.isBlank(operateObject)) return "-";
		return operateObject;
	}
	public void setOperateObject(String operateObject) {
		this.operateObject = operateObject;
	}
	public String getSourceContent() {
		return sourceContent;
	}
	public void setSourceContent(String sourceContent) {
		this.sourceContent = sourceContent;
	}
	public String getDestContent() {
		return destContent;
	}
	public void setDestContent(String destContent) {
		this.destContent = destContent;
	}
	public String getOptTime() {
		return optTime;
	}
	public void setOptTime(String optTime) {
		this.optTime = optTime;
	}
	
	
	
}

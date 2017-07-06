package com.channelsoft.ems.log.util;

import javax.servlet.http.HttpServletRequest;

import com.channelsoft.cri.util.BuildParamMapUtils;
import com.channelsoft.ems.log.constants.BusinessTypeEnum;
import com.channelsoft.ems.log.constants.LogTypeEnum;
import com.channelsoft.ems.log.po.CfgUserOperateLogPo;
import com.channelsoft.ems.sso.vo.SsoUserVo;

public class LogUtils {

	/**
	 * 获取日志对象
	 * @param request
	 * @param oper
	 * @param type
	 * @param operateObjectId
	 * @param operateObject
	 * @param obj
	 * @return
	 */
	public static CfgUserOperateLogPo getLogInfo(HttpServletRequest request,LogTypeEnum oper,BusinessTypeEnum type,String operateObjectId,String operateObject,Object obj){

		CfgUserOperateLogPo po = new CfgUserOperateLogPo(oper.value,type.value,operateObjectId,operateObject,request);
		try {
			if(obj!=null&&obj!=""){
				if(obj.getClass().toString().indexOf("Map")>-1){
					po.setDestContent(BuildParamMapUtils.getMapString(obj));
				}else{
					po.setDestContent(BuildParamMapUtils.getPoString(obj,obj.getClass()));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} 
		return po;
	}
	
	/**
	 * 获取日志对象
	 * @param request
	 * @param oper
	 * @param type
	 * @param operateObjectId
	 * @param operateObject
	 * @return
	 */
	public static CfgUserOperateLogPo getLogInfo(HttpServletRequest request,LogTypeEnum oper,BusinessTypeEnum type,String operateObjectId,String operateObject){

		CfgUserOperateLogPo po = new CfgUserOperateLogPo(oper.value,type.value,operateObjectId,operateObject,request);
		return po;
	}
	
	/**
	 * 获取日志对象
	 * @param request
	 * @param oper
	 * @param type
	 * @param operateObjectId
	 * @param operateObject
	 * @param obj
	 * @return
	 */
	public static CfgUserOperateLogPo getLogInfo(SsoUserVo user,LogTypeEnum oper,BusinessTypeEnum type,String operateObjectId,String operateObject,String loginIp,Object obj){

		CfgUserOperateLogPo po = new CfgUserOperateLogPo(oper.value,type.value,operateObjectId,operateObject,loginIp,user);
		try {
			if(obj!=null&&obj!=""){
				if(obj.getClass().toString().indexOf("Map")>-1){
					po.setDestContent(BuildParamMapUtils.getMapString(obj));
				}else{
					po.setDestContent(BuildParamMapUtils.getPoString(obj,obj.getClass()));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} 
		return po;
	}
	
	/**
	 * 获取日志对象
	 * @param request
	 * @param oper
	 * @param type
	 * @param operateObjectId
	 * @param operateObject
	 * @return
	 */
	public static CfgUserOperateLogPo getLogInfo(SsoUserVo user,LogTypeEnum oper,BusinessTypeEnum type,String operateObjectId,String operateObject,String loginIp){

		CfgUserOperateLogPo po = new CfgUserOperateLogPo(oper.value,type.value,operateObjectId,operateObject,loginIp,user);
		return po;
	}
}

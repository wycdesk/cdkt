package com.channelsoft.ems.user.util;

import org.apache.commons.lang.StringUtils;

import com.channelsoft.cri.util.CdeskEncrptDes;
import com.channelsoft.ems.register.util.MD5Util;
import com.channelsoft.ems.user.constant.LoginType;
import com.channelsoft.ems.user.po.DatEntUserPo;

public class UserUtil {

	/**
	 * 自动设置多渠道对应帐号字段信息，这里包含密码信息
	 * @param po
	 * @param userAccount 帐号信息
	 * @param userAccountType帐号类型 LoginType对应枚举类型
	 * @param pwd 可为空
	 */
	public static void setUserAccountForAdd(DatEntUserPo po,String userAccount,String userAccountType,String pwd){
		/**
    	 * 根据登陆帐号和帐号类型自动生成对应信息
    	 */
		if(StringUtils.isNotBlank(pwd)){
			pwd= CdeskEncrptDes.encryptST(pwd);
		}
    	if(LoginType.MAILBOX.value.equals(userAccountType)&&StringUtils.isBlank(po.getEmail())){
    		po.setEmail(userAccount);
    		po.setEmailPwd(pwd);
    	}
    	else if(LoginType.TELEPHONE.value.equals(userAccountType)&&StringUtils.isBlank(po.getTelPhone())){
    		po.setTelPhone(userAccount);
    		po.setTelPwd(pwd);
    	}
    	else if(LoginType.WECHAT.value.equals(userAccountType)&&StringUtils.isBlank(po.getWeixin())){
    		po.setWeixin(userAccount);
    		po.setWxPwd(pwd);
    	}
    	else if(LoginType.IM.value.equals(userAccountType)&&StringUtils.isBlank(po.getWebchatId())){
    		po.setWebchatId(userAccount);
    		po.setWebchatPwd(pwd);
    		po.setUserName(userAccount.substring(0, 10));//IM创建的用户需要将chatId截取前10位
			po.setNickName(userAccount.substring(0, 10));
    	}
     	else if(LoginType.VIDEO.value.equals(userAccountType)&&StringUtils.isBlank(po.getVedioId())){
    		po.setVedioId(userAccount);
    		po.setVedioPwd(pwd);
    	}
 
    	else if(LoginType.QQ.value.equals(userAccountType)&&StringUtils.isBlank(po.getQq())){
    		po.setQq(userAccount);
    		po.setQqPwd(pwd);
    	}
    	else if(LoginType.MICROBLOG.value.equals(userAccountType)&&StringUtils.isBlank(po.getWeibo())){
    		po.setWeibo(userAccount);
    		po.setWbPwd(pwd);
    	}
    	if(StringUtils.isBlank(po.getNickName())){
    		po.setNickName(po.getLoginName());
    	}
    	if(StringUtils.isBlank(po.getUserName())){
    		po.setUserName(po.getLoginName());
    	}
	}
	/**
	 * 自动设置多渠道对应帐号字段信息，查询使用
	 * @param po
	 * @param userAccount
	 * @param userAccountType
	 */
	public static void setUserAccountForQuery(DatEntUserPo po,String userAccount,String userAccountType){
		/**
    	 * 根据登陆帐号和帐号类型自动生成对应信息
    	 */
    	if(LoginType.MAILBOX.value.equals(userAccountType)&&StringUtils.isBlank(po.getEmail())){
    		po.setEmail(userAccount);
    	}
    	else if(LoginType.TELEPHONE.value.equals(userAccountType)&&StringUtils.isBlank(po.getTelPhone())){
    		po.setTelPhone(userAccount);
    	}
    	else if(LoginType.WECHAT.value.equals(userAccountType)&&StringUtils.isBlank(po.getWeixin())){
    		po.setWeixin(userAccount);
    	}
    	else if(LoginType.IM.value.equals(userAccountType)&&StringUtils.isBlank(po.getWebchatId())){
    		po.setWebchatId(userAccount);
    	}
     	else if(LoginType.VIDEO.value.equals(userAccountType)&&StringUtils.isBlank(po.getVedioId())){
    		po.setVedioId(userAccount);
    	}
 
    	else if(LoginType.QQ.value.equals(userAccountType)&&StringUtils.isBlank(po.getQq())){
    		po.setQq(userAccount);
    	}
    	else if(LoginType.MICROBLOG.value.equals(userAccountType)&&StringUtils.isBlank(po.getWeibo())){
    		po.setWeibo(userAccount);
    	}
	}
	
}

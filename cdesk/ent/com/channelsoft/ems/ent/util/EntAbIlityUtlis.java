package com.channelsoft.ems.ent.util;

import com.channelsoft.cri.util.WebappConfigUtil;

public class EntAbIlityUtlis {


	//是否开启邮件能力,1表示开启，其它表示关闭
//	static boolean hasMailAbility="1".equals(WebappConfigUtil.getParameter("MAIL_ABILITY"))?true:false;
	static boolean hasMailAbility=true;
	
	/**
	 * 企业是否具有邮件能力
	 * @param entId
	 * @return
	 */
	public static boolean hasMailAbility(String entId){
		return hasMailAbility;
	}
	
}

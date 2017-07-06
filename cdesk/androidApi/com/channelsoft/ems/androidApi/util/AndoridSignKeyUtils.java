package com.channelsoft.ems.androidApi.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.channelsoft.cri.util.CdeskEncrptDes;
import com.channelsoft.cri.util.WebappConfigUtil;

public class AndoridSignKeyUtils {

	/**
	 * 验证签名是否正确
	 * @param signKey
	 * @return
	 */
	public static boolean validate(String signKey){
		if(StringUtils.isBlank(signKey)) return false;
		try{
			 String[] k=CdeskEncrptDes.decryptST(signKey).split("_");
			 if(!k[1].equals(WebappConfigUtil.getParameter("Andriod_SKEY"))) return false;
			 return true;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return false;
		
	}
	/**
	 * 验证签名是否正确
	 * @param signKey
	 * @param key
	 * @return
	 */
	public static boolean validate(String signKey,String key){
		if(StringUtils.isBlank(signKey)) return false;
		 String[] k=CdeskEncrptDes.decryptST(signKey).split("_");
		 if(!k[1].equals(WebappConfigUtil.getParameter("Andriod_SKEY"))) return false;
		 if(!k[0].equals(key)) return false;
		 return true;
		
	}
	/**
	 * 获取签名
	 * @param key
	 * @return
	 */
	public static String getSignKey(String key){
	  	/*时间戳*/
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String str = sdf.format(date);
		String sk=CdeskEncrptDes.encryptST(key+"_"+WebappConfigUtil.getParameter("Andriod_SKEY")+"_"+str);
		return sk;
	}
	
}

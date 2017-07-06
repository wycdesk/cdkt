package com.channelsoft.cri.util;

public class CdeskEncrptDes {

	public static String KEY = "AD14%6d8";
	/**
	 * des加密2次，2个密钥
	 * @param source
	 * @return
	 * @throws Exception
	 */
	public static String encryptST(String source) 
	{
		try {
			return DES.encryptST(DES.encryptST(source, KEY),DES.KEY);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	public static String decryptST(String source) 
	{
		try {
			return DES.decryptST(DES.decryptST(source, DES.KEY), KEY);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 
		try {
			  String des = CdeskEncrptDes.encryptST("123456");
			  String source=CdeskEncrptDes.decryptST(des);
			  System.out.println(CdeskEncrptDes.decryptST("5C79888BBA5EBBE072D9D23569FDDB0D9709A7F2BC760854"));
			  System.out.println("密文:"+des+",明文:"+source);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}

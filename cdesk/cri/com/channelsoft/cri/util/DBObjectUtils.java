package com.channelsoft.cri.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.channelsoft.ems.user.po.DatEntUserPo;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class DBObjectUtils {

	/**
	 * 从DBObject转化为po对象
	 * @param dbo
	 * @param obj
	 * @return
	 */
	public static Object getObject(DBObject dbo,Object obj){
		 Method[] methods = obj.getClass().getDeclaredMethods();
		 for(Method method:methods){
				if(method.getName().startsWith("set")){
					for(String key:dbo.keySet()){
						 String firstLetter = key.substring(0, 1).toUpperCase();    
					     String setter = "set" + firstLetter + key.substring(1); 
						if(method.getName().equals(setter)){
							try {
								method.invoke(obj, new Object[] {dbo.get(key)});
							} catch (IllegalAccessException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IllegalArgumentException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (InvocationTargetException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							continue;
						}
					}
				}
		}
		
        return obj;
	}
	/**
	 * 从对象中提取非空属性值(包括继承而来的)放入DBObject
	 * @param obj
	 * @return
	 */
	public static DBObject getDBObject(Object obj){
		DBObject dbo = new BasicDBObject();
		Class c = obj.getClass();
        while(null!=c){
        	buildFields(dbo,obj,c);
        	c=c.getSuperclass();
        }
        return dbo;
	}
	
	/**
	 * 从对象中提取非空属性值(包括继承而来的)放入DBObject
	 * @param obj
	 * @return
	 */
	public static DBObject getDBObject(DBObject dbo,Object obj){
		Class c = obj.getClass();
        while(null!=c){
        	buildFields(dbo,obj,c);
        	c=c.getSuperclass();
        }
        return dbo;
	}
	
	/**
	 * 从对象中提取非空属性值(不包括继承而来的)放入DBObject
	 * @param dbo
	 * @param obj
	 * @param c
	 * @return
	 */
	public static DBObject buildFields(DBObject dbo,Object obj,Class c){
		Field[]	 fields = c.getDeclaredFields(); 
		for (Field fi : fields) {
			fi.setAccessible(true);
			if (fi.getName().equalsIgnoreCase("serialVersionUID")) {
				continue;
			}
			try {
				Object value = fi.get(obj);
				if (value != null&&value!="") {
					dbo.put(fi.getName(), value);
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return dbo;
	}
	/**
	 * 填充DBObject中无指定键值的值为空
	 *
	 * @param db
	 * @param field 指定键值数组
	 * @return
	 * @Author 刘海涛
	 * @date 2016-3-25
	 */
	public static DBObject fillField(DBObject db,String field[]){
		for(int i=0;i<field.length;i++){
			if(!db.containsField(field[i]))db.put(field[i], "");
		}
		return db;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DatEntUserPo user=new DatEntUserPo();
		user.setUserId("1121212");
		user.setEmail("wy174013@126.com");
		user.setUserName("wy");
		user.setNickName("fei");
		user.setEmailPwd("123456");
		DBObject dbo=DBObjectUtils.getDBObject(user);
		for(String key:dbo.keySet()){
			System.out.println(key+"="+dbo.get(key));
		}
		
	}

}

package com.channelsoft.ems.communicate.utils;

import java.lang.reflect.Field;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

public class QueryUtils {

	public static Query getQuery(Object obj){
		Query query=new Query();
		Class c=obj.getClass();
		return buildFields(query,obj,c);
	}
	
	/**
	 * 从对象中提取非空属性值(不包括继承而来的)放入DBObject
	 * @param dbo
	 * @param obj
	 * @param c
	 * @return
	 */
	public static Query buildFields(Query query,Object obj,Class c){
		Field[]	 fields = c.getDeclaredFields(); 
		for (Field fi : fields) {
			fi.setAccessible(true);
			if (fi.getName().equalsIgnoreCase("serialVersionUID")) {
				continue;
			}
			try {
				Object value = fi.get(obj);
				if (value != null&&value!="") {
					query.addCriteria(Criteria.where(fi.getName()).is(value));
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return query;
	}
}

package com.channelsoft.cri.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.channelsoft.ems.redis.constant.CacheObjects;
import com.channelsoft.ems.search.po.FieldInfoPo;
import com.mongodb.DBObject;

/**
 * 
 * @ClassName: Converter
 * @Description: 将数据库中的查出来的字段作出相应的转换
 * @author chenglitao
 * @date 2015年12月4日 下午3:28:56
 *
 */
public class Converter
{

	/**
	 * 
	 * @Description: 将传入的list中日期，字符串对应枚举类等字段做相应的转换
	 * @param  @param entId
	 * @param  @param list
	 * @param  @return
	 * @return List<DBObject>  
	 * @author chenglitao
	 * @date 2015年12月4日 下午3:32:43   
	 * @throws
	 */
	public static List<DBObject> getConvertData(String entId, List<DBObject> list)
	{

		HashMap<String, FieldInfoPo> fieldMap = CacheObjects.getFieldMap(entId);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 查询该企业的所用字段信息，并将日期格式的字段做相应的转换
		for (String key : fieldMap.keySet())
		{
			FieldInfoPo filedInfoPo = fieldMap.get(key);
			String filedType = filedInfoPo.getType();
			String filedName = filedInfoPo.getKey();
			switch (filedType)
			{
				case "DATE":
					

					for (int i = 0; i < list.size(); i++)
					{
						if (list.get(i).get(filedName) != null)
						{
							list.get(i).put(filedName, format.format((Date) list.get(i).get(filedName)));
						}
					}
					break;

				default:
					break;
			}
		}
		
		return list;
	}

}

package com.channelsoft.cri.cache.redis.utils;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Json 解析工具类
  *<dl>
  *<dt>类名：JsonUtils</dt>
  *<dd>描述: </dd>
  *<dd>公司: 青牛（北京）技术有限公司</dd>
  *<dd>创建时间：2010-7-19  下午03:05:01</dd>
  *<dd>创建人： 韦水生 </dd>
  *</dl>
 */
public class JsonUtils {
	  private static final ObjectMapper objMapper = new ObjectMapper();

	  	public static ObjectMapper getObjectMapper(){
	  		return objMapper;
	  	}
	    public static <T> Object fromJson(String jsonAsString, Class<T> pojoClass)
	    throws JsonMappingException, JsonParseException, IOException {
	        return objMapper.readValue(jsonAsString, pojoClass);
	    }

	    public static <T> Object fromJson(Reader reader, Class<T> pojoClass)
	    throws JsonParseException, IOException
	    {
	        return objMapper.readValue(reader, pojoClass);
	    }

	    public static String toJson(Object pojo)
	    throws JsonMappingException, IOException {
	      
	        return objMapper.writeValueAsString(pojo);
	    }

	    public static void toJson(Object pojo, Writer writer)
	    throws JsonMappingException, IOException {	      
	    	objMapper.writeValue(writer, pojo);
	    }
}

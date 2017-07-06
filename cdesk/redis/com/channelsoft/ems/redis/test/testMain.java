package com.channelsoft.ems.redis.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.channelsoft.ems.ent.po.DatEntInfoPo;
import com.google.gson.Gson;

public class testMain {

	/**
	 * @param args
	 */
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*List<DatEntInfoPo> list = new ArrayList<DatEntInfoPo>();
		for(int i=0;i<10;i++){
			DatEntInfoPo po = new DatEntInfoPo();
			po.setAddress("1111");
			po.setArea("北京");
			list.add(po);
		}*/
		//DatEntInfoPo po = new DatEntInfoPo();
		//po.setAddress("111");
		//Gson gson = new Gson();
        //Map<String,List<DatEntInfoPo>> map = new HashMap<String,List<DatEntInfoPo>>();
      
		//String key = "123";
		//map.put(key,list);
		//String value = JSON.toJSONString(po);
		//boolean flag = JedisTemplate.set(key, value, new Date());
		//System.out.println(flag);
		//value = JedisTemplate.get(key);
		//System.out.println(value);
		/*po =  JSON.parseObject(value, DatEntInfoPo.class);
		System.out.println(po.getAddress());*/
		 //map = JSON.parseObject(value,new TypeReference<Map<String, List<DatEntInfoPo>>>() {});
		//System.out.println(JedisTemplate.get(key));
		//list =  gson.fromJson(JedisTemplate.get(key), ArrayList<DatEntInfoPo>.class);
		/*list = map.get(key);
		for(int i=0;i<list.size();i++){
			DatEntInfoPo po = list.get(i);
			System.out.println(po.getAddress());
			System.out.println(po.getArea());
		}*/
	}

}

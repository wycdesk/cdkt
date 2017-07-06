package com.channelsoft.cri.mongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import com.channelsoft.cri.util.BeanFactoryUtil;

public class BaseMongoTemplate extends BaseAbsatarctRepository{
	
	@Autowired
	private MongoTemplate cdeskMongoTemplate;

	@Override
	public MongoTemplate getMongoTemplate() {
		if(null==cdeskMongoTemplate){
			cdeskMongoTemplate=(MongoTemplate) BeanFactoryUtil.getBean("cdeskMongoTemplate");
		}
		return cdeskMongoTemplate;
	} 
	
}

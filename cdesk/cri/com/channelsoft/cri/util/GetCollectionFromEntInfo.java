package com.channelsoft.cri.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.MongoException;


public class GetCollectionFromEntInfo {
	
	private static Logger log = LoggerFactory.getLogger(GetCollectionFromEntInfo.class);
	
	public static String getColletionName(String entId, String key) throws MongoException {
		String collectionName="entId_"+entId+"_"+key;
		
		return collectionName;
	}	
}

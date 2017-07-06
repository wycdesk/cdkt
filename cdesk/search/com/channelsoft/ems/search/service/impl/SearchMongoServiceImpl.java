package com.channelsoft.ems.search.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import com.channelsoft.cri.cache.constant.WorkPriority;
import com.channelsoft.cri.cache.constant.WorkType;
import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.util.Converter;
import com.channelsoft.cri.vo.PageInfo;
import com.channelsoft.ems.search.dao.ISearchMongoDao;
import com.channelsoft.ems.search.service.ISearchMongoService;
import com.mongodb.DBObject;

public class SearchMongoServiceImpl implements ISearchMongoService{

	@Autowired
	ISearchMongoDao searchMongoDao;
	
	/*用户模糊搜索*/
	@Override
	public List<DBObject> queryUserList(String keyword, String entId, String startTime, String endTime, String searchType, PageInfo pageInfo)
			throws ServiceException {
		// TODO Auto-generated method stub
		try{
			return searchMongoDao.queryUserList(keyword, entId, startTime, endTime, searchType, pageInfo);
		}
		catch(DataAccessException e){
			e.printStackTrace();
			throw new ServiceException("查询用户出错");
		}
	}
	
	/*工单模糊搜索*/
	@Override
	public List<DBObject> queryOrderList(String keyword, String entId, String startTime, String endTime, String searchType, PageInfo pageInfo)
			throws ServiceException {
		// TODO Auto-generated method stub
		try{
			List<DBObject> list=new ArrayList<DBObject>();	
			String keyword1="";
			if(searchType.equals("priority")){
				keyword1=WorkPriority.getEnum1(keyword).key;
				if(searchMongoDao.isNumber(keyword1) && keyword1!=""){
					list = searchMongoDao.queryOrderList(keyword, entId, startTime, endTime, searchType, pageInfo);				
				}
			}else if(searchType.equals("type")){
				keyword1=WorkType.getEnum1(keyword).key;
				if(searchMongoDao.isNumber(keyword1) && keyword1!=""){
					list = searchMongoDao.queryOrderList(keyword, entId, startTime, endTime, searchType, pageInfo);				
				}
			}else{
				list = searchMongoDao.queryOrderList(keyword, entId, startTime, endTime, searchType, pageInfo);	
			}		    
		    //将日期等类型做相应的转换
		    list =Converter.getConvertData(entId, list);
		    return list; 
		}
		catch(DataAccessException e){
			e.printStackTrace();
			throw new ServiceException("查询工单出错");
		}
	}
	

}

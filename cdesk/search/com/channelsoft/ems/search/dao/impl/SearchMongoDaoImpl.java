package com.channelsoft.ems.search.dao.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.channelsoft.cri.mongo.BaseMongoTemplate;
import com.channelsoft.cri.vo.PageInfo;
import com.channelsoft.ems.search.dao.ISearchMongoDao;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import com.channelsoft.cri.cache.constant.WorkPriority;
import com.channelsoft.cri.cache.constant.WorkType;

public class SearchMongoDaoImpl extends BaseMongoTemplate implements ISearchMongoDao{

	/*用户模糊搜索*/
	@Override
	public List<DBObject> queryUserList(String keyword, String entId, String startTime, String endTime, String searchType, PageInfo pageInfo)
			throws DataAccessException {
		// TODO Auto-generated method stub
		Query query=new Query();
		Criteria cra = new Criteria();
		
		Pattern pattern = Pattern.compile("^.*" + keyword+ ".*$", Pattern.CASE_INSENSITIVE); 
		if(searchType==""){
			query.addCriteria(cra.orOperator(Criteria.where("userName").regex(pattern), Criteria.where("email").regex(pattern), Criteria.where("telPhone").regex(pattern), Criteria.where("nickName").regex(pattern)));

		}else{
			query.addCriteria(Criteria.where(searchType).regex(pattern));
		}
		
		if(startTime!="" && endTime!=""){
			query.addCriteria(Criteria.where("createTime").gte(startTime).lte(endTime));
		}
	
		query.addCriteria(Criteria.where("userStatus").ne("9"));
		// 按创建时间降序排序
		query.with(new Sort(Direction.DESC, "createTime"));
		return this.findByPage(query, getUserTable(entId), DBObject.class, pageInfo);
		
	}
	
	/*获取用户表*/
	@Override
	public String getUserTable(String entId) throws DataAccessException {
		// TODO Auto-generated method stub
		return "entId_"+entId+"_user";
	}
	
	/*获取工单表*/
	@Override
	public String getOrderTable(String entId) throws DataAccessException {
		// TODO Auto-generated method stub
		return "entId_"+entId+"_work";
	}
	
	/*工单模糊搜索*/
	@Override
	public List<DBObject> queryOrderList(String keyword, String entId, String startTime, String endTime, String searchType, PageInfo pageInfo)
			throws DataAccessException {
		// TODO Auto-generated method stub		
		Query query=new Query();
		Criteria cra = new Criteria();
	
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {					    		    
				Pattern pattern = Pattern.compile("^.*" + keyword+ ".*$", Pattern.CASE_INSENSITIVE); 
				String keyword2=WorkPriority.getEnum1(keyword).key;
				String keyword3=WorkType.getEnum1(keyword).key;	
				
				Pattern pattern1 = Pattern.compile("^.*" + keyword2+ ".*$", Pattern.CASE_INSENSITIVE); 
				Pattern pattern2 = Pattern.compile("^.*" + keyword3+ ".*$", Pattern.CASE_INSENSITIVE); 
				
				if(searchType==""){														
					if(isNumber(keyword2) && keyword2!=""){	
						query.addCriteria(cra.orOperator(Criteria.where("title").regex(pattern),
								Criteria.where("creatorName").regex(pattern), 
								Criteria.where("customServiceName").regex(pattern), 
								Criteria.where("serviceGroupName").regex(pattern),								
								Criteria.where("priority").regex(pattern1)));	
						
					}else if(isNumber(keyword3) && keyword3!=""){	
						query.addCriteria(cra.orOperator(Criteria.where("title").regex(pattern),
								Criteria.where("creatorName").regex(pattern), 
								Criteria.where("customServiceName").regex(pattern), 
								Criteria.where("serviceGroupName").regex(pattern),
								Criteria.where("type").regex(pattern2)));	
						
					}else if(isNumber(keyword) && keyword!=""){
						int keyword1=Integer.parseInt(keyword);	
						query.addCriteria(cra.orOperator(Criteria.where("title").regex(pattern),
								Criteria.where("creatorName").regex(pattern), 
								Criteria.where("customServiceName").regex(pattern), 
								Criteria.where("serviceGroupName").regex(pattern),								
								Criteria.where("workId").is(keyword1)));
						
					}else{
						query.addCriteria(cra.orOperator(Criteria.where("title").regex(pattern),
								Criteria.where("creatorName").regex(pattern), 
								Criteria.where("customServiceName").regex(pattern), 
								Criteria.where("serviceGroupName").regex(pattern)));
					}					
				}else if(searchType=="workId"){					
						int keyword1=Integer.parseInt(keyword);	
						query.addCriteria(Criteria.where(searchType).is(keyword1));
				}else if(searchType=="priority"){
					if(isNumber(keyword2) && keyword2!=""){
						query.addCriteria(Criteria.where(searchType).regex(pattern1));
					}					
				}else if(searchType=="type"){
					if(isNumber(keyword3) && keyword3!=""){
						query.addCriteria(Criteria.where(searchType).regex(pattern2));
					}				
				}else{
					query.addCriteria(Criteria.where(searchType).regex(pattern));
				}
				
				if(startTime!="" && endTime!=""){
					Date startTimes=sdf.parse(startTime);
					Date endTimes=sdf.parse(endTime);					
					query.addCriteria(Criteria.where("createDate").gte(startTimes).lte(endTimes));
				}				
				// 按创建时间降序排序
				query.with(new Sort(Direction.DESC, "createDate"));		
				
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.findByPage(query, getOrderTable(entId), DBObject.class, pageInfo);
	}
	
	
	/*校验输入的workId是否为数字字符串*/
    public boolean isNumber(String number)  
    {  
        Pattern pattern = Pattern.compile("[0-9]*");  
        Matcher matcher = pattern.matcher(number);  
          
        if (matcher.matches())  
        {  
            return true;  
        }  
        return false;  
    } 
}

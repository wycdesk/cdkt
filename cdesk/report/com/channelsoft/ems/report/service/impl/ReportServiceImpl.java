package com.channelsoft.ems.report.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import com.channelsoft.cri.cache.constant.WorkSource;
import com.channelsoft.cri.constant.DateConstants;
import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.logger.SystemLogUtils;
import com.channelsoft.cri.util.ColletionName;
import com.channelsoft.cri.util.GetCollectionFromEntInfo;
import com.channelsoft.ems.report.dao.IReportDao;
import com.channelsoft.ems.report.service.IReportService;
import com.channelsoft.ems.report.util.GetKeys;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class ReportServiceImpl implements IReportService {

	@Autowired
	IReportDao reportDao;
	
	@Override
	public Map<String, Object> query(String entId) throws ServiceException {
		// TODO Auto-generated method stub
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String beginTime = DateConstants.DATE_FORMAT_SHORT().format(new Date())+" 00:00:00";
			String endTime = DateConstants.DATE_FORMAT_SHORT().format(new Date())+" 23:59:59";
			
			BasicDBObject queryUserObject = new BasicDBObject();
			BasicDBObject[] array = { new BasicDBObject("createTime", new BasicDBObject("$gte", beginTime)),
					new BasicDBObject("createTime", new BasicDBObject("$lte", endTime)) };
			queryUserObject.put("$and", array);
			queryUserObject.put("entId", entId);
			//查询当天新增客户
			List<DBObject> userDayList = reportDao.queryUser(queryUserObject);
			//查询工单
			BasicDBObject workObject = new BasicDBObject();
			List<DBObject> workList = reportDao.queryWorkOrder(entId,workObject);
			//查询联络历史
			BasicDBObject communicateObject = new BasicDBObject();
			List<DBObject> communicateList = reportDao.queryCommunicate(entId,communicateObject);
			Map<String, Object> map = new HashMap<String, Object>();
			for(Object work : workList){
				String customId = work+"";
				if(StringUtils.isBlank(customId) || "null".equals(customId)){
					continue;
				}else{
					map.put(customId, 1);
				}
			}
			for(Object communicate: communicateList){
				String userId = communicate+"";
				if(StringUtils.isBlank(userId)||"null".equals(userId)){
					continue;
				}else{
					map.put(userId, 1);
				}
			}
			long temp = 0;
			for(DBObject userDay : userDayList){
				String userId = userDay.get("userId")+"";
				if(StringUtils.isBlank(userId)||"null".equals(userId)){
					continue;
				}else{
					if(map.containsKey(userId)){
						temp++;
					}
				}
			}
			resultMap.put("dayText", "新增客户");
			resultMap.put("dayNum", temp);
			
			//查询今天之前的所有客户
			BasicDBObject queryAllUserObject = new BasicDBObject();
			String today = DateConstants.DATE_FORMAT_SHORT().format(new Date())+" 00:00:00";
			BasicDBObject[] arrayUser = { new BasicDBObject("createTime", new BasicDBObject("$lt", today)) };
			queryAllUserObject.put("$and", arrayUser);
			queryAllUserObject.put("entId", entId);
			
			List<DBObject> userAllList = reportDao.queryUser(queryAllUserObject);
			long total = 0;
			for(DBObject user : userAllList){
				String userId = user.get("userId")+"";
				if(StringUtils.isBlank(userId)||"null".equals(userId)){
					continue;
				}else{
					if(map.containsKey(userId)){
						total++;
					}
				}
			}
			resultMap.put("text", "往日客户");
			resultMap.put("total", total);
			resultMap.put("allTotal", total+temp);
			
			//查询今日新增工单和联络历史
			//服务客服数；电话呼出的不统计
			Map<String, Object> dayMap = new HashMap<String, Object>();
			BasicDBObject queryWorkDay = new BasicDBObject();
			BasicDBObject[] arrayWorkDay = { new BasicDBObject("createDate", new BasicDBObject("$gte", sdf.parse(beginTime))),
					new BasicDBObject("createDate", new BasicDBObject("$lte", sdf.parse(endTime))),
					new BasicDBObject("source", new BasicDBObject("$ne", WorkSource.PHONEOUT.key)) };
			
			queryWorkDay.put("$and", arrayWorkDay);
			List<DBObject> workListDay = reportDao.queryWorkOrder(entId,queryWorkDay);
			for(Object w:workListDay){
				String customId = w+"";
				dayMap.put(customId, 1);
			}
			
			BasicDBObject queryCDay = new BasicDBObject();
			BasicDBObject[] arrayCDay = { new BasicDBObject("createTime", new BasicDBObject("$gte", sdf.parse(beginTime))),
					new BasicDBObject("createTime", new BasicDBObject("$lte", sdf.parse(endTime))),
					new BasicDBObject("source", new BasicDBObject("$ne", WorkSource.PHONEOUT.key)) };
			queryCDay.put("$and", arrayCDay);
			List<DBObject> communicateListDay = reportDao.queryCommunicate(entId,queryCDay);
			for(Object c : communicateListDay){
				String customId = c+"";
				dayMap.put(customId, 1);
			}
			resultMap.put("customDayNum", dayMap.size());
			
			
			return resultMap;
		}catch (DataAccessException e){
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Map<String, Object> queryServedCm(String entId, String beginTime, String endTime) throws ServiceException {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			beginTime = beginTime+" 00:00:00";
			endTime = endTime+" 23:59:59";
			
			BasicDBObject queryUserObject = new BasicDBObject();
			BasicDBObject[] array = { new BasicDBObject("createTime", new BasicDBObject("$gte", beginTime)),
					new BasicDBObject("createTime", new BasicDBObject("$lte", endTime)) };
			BasicDBObject[] arrayW = { new BasicDBObject("createDate", new BasicDBObject("$gte", sdf.parse(beginTime))),
					new BasicDBObject("createDate", new BasicDBObject("$lte", sdf.parse(endTime))) };
			BasicDBObject[] arrayC = { new BasicDBObject("createTime", new BasicDBObject("$gte", sdf.parse(beginTime))),
					new BasicDBObject("createTime", new BasicDBObject("$lte", sdf.parse(endTime))) };
			queryUserObject.put("$and", array);
			queryUserObject.put("entId", entId);
			//查询此时间段新增客户
			List<DBObject> userDayList = reportDao.queryUser(queryUserObject);
			//查询工单
			BasicDBObject workObject = new BasicDBObject();
			workObject.put("$and", arrayW);
			List<DBObject> workList = reportDao.queryWorkOrder(entId,workObject);
			List<DBObject> workAllList=reportDao.queryAllWork(entId,workObject);
			//查询联络历史
			BasicDBObject communicateObject = new BasicDBObject();
			communicateObject.put("$and", arrayC);
			List<DBObject> communicateList = reportDao.queryCommunicate(entId,communicateObject);
			Map<String, Object> map = new HashMap<String, Object>();
			for(Object work : workList){
				String customId = work+"";
				if(StringUtils.isBlank(customId) || "null".equals(customId)){
					continue;
				}else{
					map.put(customId, 1);
				}
			}
			for(Object communicate: communicateList){
				String userId = communicate+"";
				if(StringUtils.isBlank(userId)||"null".equals(userId)){
					continue;
				}else{
					map.put(userId, 1);
				}
			}
			long temp = userDayList.size();
			resultMap.put("newText", "新建客户");
			resultMap.put("newNum", temp);
			resultMap.put("servText", "服务客户数");
			resultMap.put("servNum", map.size());
			resultMap.put("workNum", workAllList.size());
			//resultMap.put("allTotal", total+temp);
			
			//查询今日新增工单和联络历史
			//服务客服数；电话呼出的不统计
			/*Map<String, Object> dayMap = new HashMap<String, Object>();
			BasicDBObject queryWorkDay = new BasicDBObject();
			BasicDBObject[] arrayWorkDay = { new BasicDBObject("createDate", new BasicDBObject("$gte", sdf.parse(beginTime))),
					new BasicDBObject("createDate", new BasicDBObject("$lte", sdf.parse(endTime))),
					new BasicDBObject("source", new BasicDBObject("$ne", WorkSource.PHONEOUT.key)) };
			
			queryWorkDay.put("$and", arrayWorkDay);
			List<DBObject> workListDay = reportDao.queryWorkOrder(entId,queryWorkDay);
			for(Object w:workListDay){
				String customId = w+"";
				dayMap.put(customId, 1);
			}
			
			BasicDBObject queryCDay = new BasicDBObject();
			BasicDBObject[] arrayCDay = { new BasicDBObject("createTime", new BasicDBObject("$gte", sdf.parse(beginTime))),
					new BasicDBObject("createTime", new BasicDBObject("$lte", sdf.parse(endTime))),
					new BasicDBObject("source", new BasicDBObject("$ne", WorkSource.PHONEOUT.key)) };
			queryCDay.put("$and", arrayCDay);
			List<DBObject> communicateListDay = reportDao.queryCommunicate(entId,queryCDay);
			for(Object c : communicateListDay){
				String customId = c+"";
				dayMap.put(customId, 1);
			}
			resultMap.put("customDayNum", dayMap.size());*/
			return resultMap;
		}catch (DataAccessException | ParseException e){
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<DBObject> getBaseSortList(String entId, String startTime, String endTime, String type)
			throws ServiceException {
		try {
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			Date sTime=sdf.parse(startTime+" 00:00:00");
			Date eTime=sdf.parse(endTime+" 23:59:59");
			DBObject[] array={new BasicDBObject("createDate",new BasicDBObject("$gte",sTime)),
					new BasicDBObject("createDate",new BasicDBObject("$lte",eTime))};
			DBObject queryDB=new BasicDBObject("$and",array);
			queryDB.put("entId", entId);
			Iterator<DBObject> ito=reportDao.getBaseSortList(queryDB,type);
			List<DBObject> rtn=new ArrayList<DBObject>();
			while(ito.hasNext()){
				rtn.add(ito.next());
			}
			return rtn;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<Long> getServiceByTime(String beginTime, String endTime, String entId) throws ServiceException {
		SystemLogUtils.Debug("进入查询一段时间段内新建工单的数量的dao层方法");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<Long> result = new ArrayList<Long>();
		try
		{
			List<DBObject> listTempsW=reportDao.getServiceByTime(sdf.parse(beginTime), sdf.parse(endTime), entId,0);
			List<DBObject> listTempsC=reportDao.getServiceByTime(sdf.parse(beginTime), sdf.parse(endTime), entId,1);
			
			List<String> caledarList = GetKeys.getBeforeAfterCalendar(beginTime, endTime);
			Calendar caledar = Calendar.getInstance();

			// 统计所有时间段内新建服务的数量
			long count = 0;
			for (int i = 0; (i + 1) < caledarList.size(); i++){
				for (int ii=0;ii<listTempsW.size();ii++){
					DBObject db=listTempsW.get(ii);
					Date date = (Date) db.get("createDate");
					caledar.setTime(date);

					Calendar beginCaledar = Calendar.getInstance();
					beginCaledar.setTime(sdf.parse(caledarList.get(i)));
					Calendar endCaledar = Calendar.getInstance();
					endCaledar.setTime(sdf.parse(caledarList.get(i + 1)));

					boolean re = GetKeys.compareTime(caledar, beginCaledar, endCaledar);
					if (re)
					{
						listTempsW.remove(ii);
						ii--;
						count++;
					}
				}
				for (int kk=0;kk<listTempsC.size();kk++){
					DBObject db=listTempsC.get(kk);
					Date date = (Date) db.get("createTime");
					caledar.setTime(date);

					Calendar beginCaledar = Calendar.getInstance();
					beginCaledar.setTime(sdf.parse(caledarList.get(i)));
					Calendar endCaledar = Calendar.getInstance();
					endCaledar.setTime(sdf.parse(caledarList.get(i + 1)));

					boolean re = GetKeys.compareTime(caledar, beginCaledar, endCaledar);
					if (re){
						listTempsC.remove(kk);
						kk--;
						count++;
					}
				}
				result.add(count);
				count = 0;
			}
			return result;
		} catch (Exception e){
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}

}

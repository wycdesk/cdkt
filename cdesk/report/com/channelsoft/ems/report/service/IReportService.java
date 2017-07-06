package com.channelsoft.ems.report.service;

import java.util.List;
import java.util.Map;

import com.channelsoft.cri.exception.ServiceException;
import com.mongodb.DBObject;

public interface IReportService {

	public Map<String,Object> query(String entId) throws ServiceException;

	public Map<String, Object> queryServedCm(String entId, String beginTime, String endTime) throws ServiceException;

	public List<DBObject> getBaseSortList(String entId, String startTime, String endTime, String type)throws ServiceException;

	public List<Long> getServiceByTime(String beginTime, String endTime, String entId)throws ServiceException;
}

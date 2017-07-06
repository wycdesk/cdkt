package com.channelsoft.ems.androidApi.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.ems.androidApi.service.IAndroidUserApiService;
import com.channelsoft.ems.ent.po.DatEntInfoPo;
import com.channelsoft.ems.redis.util.ParamUtils;
import com.channelsoft.ems.user.po.DatEntUserPo;
import com.channelsoft.ems.user.service.IUserMongoService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class AndroidUserApiServiceImpl implements IAndroidUserApiService {

	@Autowired
	IUserMongoService userMongoService;

	@Override
	public List<DBObject> queryUser(String entId, String account, String source) throws ServiceException {
		List<DBObject> list = userMongoService.queryUserForCCOD(entId, account, source);
		
		List<DBObject> res = new ArrayList<DBObject>();
		if(list!=null&&list.size()>0){
			for(DBObject db:list){
				DBObject d=new BasicDBObject();
			     d.put("userId", db.get("userId"));
			     d.put("userName", db.get("userName"));
			     d.put("nickName", db.get("nickName"));
			     d.put("telPhone", db.get("telPhone"));
			     d.put("fixedPhone", db.get("fixedPhone"));
			     d.put("email", db.get("email"));
			     d.put("userLabel", db.get("userLabel"));
			     d.put("userDesc", db.get("userDesc"));
			     res.add(d);
			}
		}
		return res;
	}

	@Override
	public DBObject queryUserOrAdd(String entId, String account, String source) throws ServiceException {
		DatEntInfoPo ent=ParamUtils.getDatEntInfoPo(entId);
		if(ent == null){
			 throw new ServiceException("ccodEntId转换为entId失败，未查询到对应企业ID");
		}
		//ccod entId 转换 cdesk
		entId = ent.getEntId();
		DatEntUserPo user= userMongoService.queryOrAddUser(entId, account, source);
		DBObject d=new BasicDBObject();
		if(user!=null){
			d.put("userId", user.getUserId());
			d.put("userName", user.getUserName());
		    d.put("nickName", user.getNickName());
		    d.put("telPhone", user.getTelPhone());
		    d.put("fixedPhone", user.getFixedPhone());
		    d.put("email", user.getEmail());
		    d.put("userDesc", user.getUserDesc());
		} else{
			return null;
		}
		return d;
	}
	
}

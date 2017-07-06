package com.channelsoft.ems.field.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.vo.PageInfo;
import com.channelsoft.ems.field.dao.IUserFieldDao;
import com.channelsoft.ems.field.po.UserDefinedFiedPo;
import com.channelsoft.ems.field.service.IUserFieldService;
import com.channelsoft.ems.user.dao.IUserDao;

public class UserFieldServiceImpl implements IUserFieldService {

	static Object o=new Object();
	
	@Autowired
	IUserFieldDao userFieldDao;
	@Autowired
	IUserDao userDao;
	@Override
	public List<UserDefinedFiedPo> query(String entId, UserDefinedFiedPo po,
			PageInfo page) throws ServiceException {
		try{
			return userFieldDao.query(entId, po, page);
		}
		catch(DataAccessException e){
			e.printStackTrace();
			throw new ServiceException("查询出错");
		}

	}

	@Override
	public List<UserDefinedFiedPo> queryDefinedFiled(String entId,String status, PageInfo page)
			throws ServiceException {
		 
		UserDefinedFiedPo po=new UserDefinedFiedPo();
		po.setDefault(false);
		po.setStatus(status);
		return this.query(entId, po, page);
	}

	@Override
	public List<UserDefinedFiedPo> queryDefultFiled(String entId,String status, PageInfo page)
			throws ServiceException {
		// TODO Auto-generated method stub
		UserDefinedFiedPo po=new UserDefinedFiedPo();
		po.setDefault(true);
		po.setStatus(status);
		return this.query(entId, po, page);
	}

	@Override
	public int addDefinedField(UserDefinedFiedPo po, String entId)throws ServiceException {
		// TODO Auto-generated method stub
		try{
			synchronized(UserFieldServiceImpl.class){
				 po.setKey(this.getKey(entId));
				 return userFieldDao.add(po, entId);
			}
	       
		}
		catch(Exception e){
			e.printStackTrace();
			throw new ServiceException("添加自定义字段出错");
		}
	
	}
	@Override
	public String getKey(String entId) throws ServiceException{
		/*List<UserDefinedFiedPo> list=this.query(entId, new UserDefinedFiedPo() , null);
		Map<String,String> keyMap=new HashMap<String,String>();
		for(UserDefinedFiedPo u:list){
			keyMap.put(u.getKey(),u.getKey());
		}
		for(int i=1;i<=60;i++){
			String key="field";
			if(i<10) key+="0"+i;
			else key+=i;
			if(!keyMap.containsKey(key)){
				return key;
			}
		}
		throw new ServiceException("自定义字段最多60个");*/
		String num=userDao.getUserFieldId();
		if(Integer.valueOf(num)<10){
			num="0"+num;
		}
		return "field"+num;
	}

	@Override
	public UserDefinedFiedPo queryFieldByKey(String entId,String key, String status) throws ServiceException {
		UserDefinedFiedPo po=new UserDefinedFiedPo();
		po.setKey(key);
		po.setStatus(status);
		List<UserDefinedFiedPo> list=null;
		try {
			list = this.query(entId, po, null);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public int goEdit(String entId, UserDefinedFiedPo po) throws ServiceException {
		try {
			return userFieldDao.goEdit(entId,po);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public int deleteField(String entId, String key) throws ServiceException {
		try {
			return userFieldDao.deleteField(entId,key);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public int changeMode(String entId, String type, String key) throws ServiceException {
		try {
			return userFieldDao.changeMode(entId,type,key);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public int sortUserField(String entId, String ids) throws ServiceException {
		String[] sortKey=ids.trim().split(",");
		try {
			int[] sortNums=userFieldDao.getSortNums(entId);
			return userFieldDao.sortUserField(entId,sortKey,sortNums);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
		
	}
}

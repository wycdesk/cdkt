package com.channelsoft.ems.register.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.channelsoft.cri.vo.PageInfo;
import com.channelsoft.ems.register.po.RegisterInfoPo;


public interface IRegisterInfoDao {

	public List<RegisterInfoPo> query(RegisterInfoPo po, PageInfo page) throws DataAccessException;
	
	public void addRegister(RegisterInfoPo po) throws DataAccessException;
	
	public void updateRegister(RegisterInfoPo registerPo) throws DataAccessException;
	
	public List<RegisterInfoPo> queryEmail(RegisterInfoPo po, PageInfo pageInfo);
	
	public List<RegisterInfoPo> queryCreateTime(RegisterInfoPo po, PageInfo pageInfo);

	public List<RegisterInfoPo> queryLastMsg(RegisterInfoPo po, PageInfo pageInfo);
}

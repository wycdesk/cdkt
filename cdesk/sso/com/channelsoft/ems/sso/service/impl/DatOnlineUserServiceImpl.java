package com.channelsoft.ems.sso.service.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;
import org.tuckey.web.filters.urlrewrite.utils.StringUtils;

import com.channelsoft.cri.constant.BaseErrCode;
import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.service.BaseService;
import com.channelsoft.cri.util.IpUtils;
import com.channelsoft.ems.sso.dao.IDatOnlineUserDao;
import com.channelsoft.ems.sso.dao.IDatUserLoginDao;
import com.channelsoft.ems.sso.po.DatOnlineUserPo;
import com.channelsoft.ems.sso.po.DatUserLoginPo;
import com.channelsoft.ems.sso.service.IDatOnlineUserService;
import com.channelsoft.ems.sso.vo.DatOnlineUserVo;
import com.channelsoft.ems.sso.vo.SsoUserVo;

public class DatOnlineUserServiceImpl extends BaseService implements IDatOnlineUserService {

	@Autowired
	IDatOnlineUserDao onlineUserDao;
	@Autowired
	IDatUserLoginDao userLoginDao;
	
	@Override
	@Transactional
	public String login(String enterpriseid,SsoUserVo userInfo, String platformId, HttpServletRequest request) throws ServiceException {
		try {
			onlineUserDao.deleteByLoginName(enterpriseid,userInfo.getLoginName());
			String sessionKey = DigestUtils.md5Hex(System.currentTimeMillis() + userInfo.getUserId() + enterpriseid);
			DatOnlineUserPo onlineUserPo = new DatOnlineUserPo();
			onlineUserPo.setLoginName(userInfo.getLoginName());
			onlineUserPo.setUserName(userInfo.getUserName());
			onlineUserPo.setUserId(userInfo.getUserId());
			onlineUserPo.setLoginIp(IpUtils.getClientIp(request));
			onlineUserPo.setNickName(userInfo.getNickName());
			onlineUserPo.setSessionKey(sessionKey);
			onlineUserDao.add(enterpriseid,onlineUserPo);
			DatUserLoginPo userLoginPo = new DatUserLoginPo();
			BeanUtils.copyProperties(onlineUserPo, userLoginPo);
			userLoginDao.add(enterpriseid, userLoginPo);
			return sessionKey;
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR, e.getMessage());
		}
	}

	@Override
	public SsoUserVo getUserBySessionKey(String enterpriseid,String sessionKey) throws ServiceException {
		try {
			if(StringUtils.isBlank(enterpriseid) || StringUtils.isBlank(sessionKey)) {
				System.out.println("enterpriseid="+enterpriseid+",sessionKey="+sessionKey);
				throw new ServiceException(BaseErrCode.GENERAL_ERROR, "企业ID或sessionKey为空");
			}
			List<DatOnlineUserVo> list = onlineUserDao.queryUserForMongo(enterpriseid,sessionKey);
			if (list.size() == 0)
			{
				throw new ServiceException(BaseErrCode.ERR_INVALID_USER, "SESSIONKEY无效");
			}
			DatOnlineUserVo vo = list.get(0);
			SsoUserVo user = new SsoUserVo();
			try {
				BeanUtils.copyProperties(vo, user);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return user;
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR, e.getMessage());
		}
	}

	@Override
	public void logout(String enterpriseid,String sessionKey) throws ServiceException {
		try {
			onlineUserDao.logout(enterpriseid,sessionKey);
			userLoginDao.logout(enterpriseid, sessionKey);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR, e.getMessage());
		}
	}
}

package com.channelsoft.ems.user.dao.impl;

import java.sql.Types;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;

import com.channelsoft.cri.dao.jdbc.BaseJdbcMysqlDao;
import com.channelsoft.cri.util.CdeskEncrptDes;
import com.channelsoft.ems.register.po.RegisterInfoPo;
import com.channelsoft.ems.register.util.MD5Util;
import com.channelsoft.ems.sso.util.SsoSessionUtils;
import com.channelsoft.ems.sso.vo.SsoUserVo;
import com.channelsoft.ems.user.constant.UserStatus;
import com.channelsoft.ems.user.dao.IUserDao;
import com.channelsoft.ems.user.po.DatEntUserPo;

public class UserDaoImpl extends BaseJdbcMysqlDao implements IUserDao {
	/*@Override
	public int registerBase(DatEntUserPo po) throws DataAccessException {
		// TODO Auto-generated method stub
		StringBuffer sql=new StringBuffer();
		String loginPwd=po.getLoginPwd()==null?po.getLoginPwd():CdeskEncrptDes.encryptST(po.getLoginPwd());
		String emailPwd=po.getEmailPwd()==null?null:CdeskEncrptDes.encryptST(po.getEmailPwd());
		sql.append("INSERT INTO ").append(getEntDbName(po.getEntId())).append(".DAT_ENT_USER (USER_ID,ENT_ID,ENT_NAME,USER_TYPE,ROLE_ID,LOGIN_TYPE,LOGIN_NAME,NICK_NAME,"
				+ "EMAIL,USER_STATUS,ACTIVE_CODE,LOGIN_PWD,EMAIL_PWD,USER_NAME,CREATE_TIME,TELPHONE) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,NOW(),?)");
		Object args[]={po.getUserId(),po.getEntId(),po.getEntName(),
				po.getUserType(),po.getRoleId(),po.getLoginType(),po.getLoginName(),po.getNickName(),
				po.getEmail(),po.getUserStatus(),po.getActiveCode(),loginPwd,emailPwd,po.getUserName(),po.getTelPhone()};
		int argTypes[]=new int[args.length];
		for(int i=0;i<args.length;i++){
			argTypes[i]=Types.VARCHAR;
		}
		return this.update(sql.toString(), args, argTypes);
	}*/

	/*@Override
	public int registerPwd(String userName,String nickName,String password, String entId, String code) throws DataAccessException{
		// TODO Auto-generated method stub
		StringBuffer sql=new StringBuffer();
		String pwd=CdeskEncrptDes.encryptST(password);
		sql.append("UPDATE ").append(getEntDbName(entId)).append(".DAT_ENT_USER SET LOGIN_PWD='").append(pwd+"'")
			.append(",EMAIL_PWD='"+pwd+"'").append(",USER_STATUS='"+UserStatus.NORMAL.value+"'");
		if(StringUtils.isNotBlank(nickName)){
			sql.append(",NICK_NAME='"+nickName+"'");
		}
		if(StringUtils.isNotBlank(userName)){
			sql.append(",USER_NAME='"+userName+"'");
		}
		sql.append(" WHERE ACTIVE_CODE='"+code+"'");
		return this.update(sql.toString());
	}*/

	@Override
	public RegisterInfoPo getEntInfo(String domainName) throws DataAccessException {
		// TODO Auto-generated method stub
		String sql="SELECT * FROM DAT_ENT_INFO WHERE DOMAIN_NAME = '"+domainName+"'";
		return this.queryForObject(sql, RegisterInfoPo.class);
	}

	@Override
	public String getUserId() throws DataAccessException {
		// TODO Auto-generated method stub
		return this.getSequenceStr("SEQ_DAT_ENT_USER");
	}
	
	@Override
	public String getUserFieldId() throws DataAccessException {
		// TODO Auto-generated method stub
		return this.getSequenceStr("SEQ_USER_FIELD");
	}
	
	/*@Override
	public DatEntUserPo getEntUserPo(String entId,String code,String email) throws DataAccessException {
		// TODO Auto-generated method stub
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT * FROM ").append(getEntDbName(entId)).append(".DAT_ENT_USER WHERE 1=1 ");
		//ACTIVE_CODE='"+code+"'");
		if(code!=null)
			sql.append("AND ACTIVE_CODE='"+code+"'");
		else if(email!=null)
			sql.append("AND LOGIN_NAME='"+email+"'");
		DatEntUserPo po=this.queryForObject(sql.toString(), DatEntUserPo.class);
		return po;
	}*/

	/*@Override
	public String deleteUser(String entId,String code) throws DataAccessException {
		// TODO Auto-generated method stub
		StringBuffer sql=new StringBuffer();
		sql.append("DELETE FROM ").append(getEntDbName(entId)).append(".DAT_ENT_USER WHERE ACTIVE_CODE='"+code+"'");
		String sql1="SELECT * FROM "+getEntDbName(entId)+".DAT_ENT_USER WHERE ACTIVE_CODE='"+code+"'";
		String email=this.queryForObject(sql1, DatEntUserPo.class).getEmail();
		this.update(sql.toString());
		return email;
	}*/

	/*@Override
	public boolean hasActiveUser(String enterpriseid, String roleId) throws DataAccessException {
		String sql = "SELECT COUNT(1) FROM"
				+ SsoSessionUtils.getEntDB(enterpriseid)
				+ ".DAT_ENT_USER WHERE ROLE_ID=?";
		Object[] params = new Object[] { roleId };
		int[] types = new int[] { Types.INTEGER };
		return this.queryForInt(sql, params, types) > 0;
	}*/
	
	/*@Override
	public List<SsoUserVo> login(String enterpriseId, String loginName,String password) throws DataAccessException {

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DU.*, CR.NAME ROLE_NAME FROM ")
			.append(getEntDbName(enterpriseId))
			.append(".DAT_ENT_USER DU, ")
			.append(getEntDbName(enterpriseId))
			.append(".CFG_ROLE CR WHERE DU.ROLE_ID=CR.ID AND DU.LOGIN_NAME=? AND DU.LOGIN_PWD=? AND (DU.USER_STATUS='")
			.append(UserStatus.NORMAL.value + "' OR DU.USER_STATUS='" + UserStatus.FORAUDIT.value + "')");

		Object[] params = new Object[] { loginName, password };
		int[] types = new int[] { Types.VARCHAR, Types.VARCHAR };
		return this
				.queryForList(sql.toString(), params, types, SsoUserVo.class);
	}*/

	/*@Override
	public boolean existsEmails(String entId, String email) throws DataAccessException {
		// TODO Auto-generated method stub
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT COUNT(*) FROM ").append(getEntDbName(entId)).append(".DAT_ENT_USER WHERE EMAIL='").append(email).append("'");
		int num=this.queryForInt(sql.toString());
		return num>0?true:false;
	}*/
	/*@Override
	public void resetUser(DatEntUserPo userPo) throws DataAccessException {
		StringBuffer sql=new StringBuffer();
		sql.append("UPDATE ").append(getEntDbName(userPo.getEntId()))
			.append(".DAT_ENT_USER SET ACTIVE_CODE='"+userPo.getActiveCode()+"'")
			.append(",CREATE_TIME=NOW() WHERE EMAIL='").append(userPo.getEmail()).append("'");
		this.update(sql.toString());
	}*/
	/*@Override
	public List<DatEntUserPo> queryAll(String entId) throws DataAccessException {
		StringBuffer sqlBuffer = new StringBuffer("SELECT * FROM ");
		sqlBuffer.append(getEntDbName(entId)).append(".DAT_ENT_USER");
		return this.queryForList(sqlBuffer.toString(), DatEntUserPo.class);
	}*/

	/*@Override
	public boolean existsPhone(String entId, String phone) throws DataAccessException {
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT COUNT(*) FROM ").append(getEntDbName(entId)).append(".DAT_ENT_USER WHERE TELPHONE='").append(phone).append("'");
		int num=this.queryForInt(sql.toString());
		return num>0?true:false;
	}*/

	/*@Override
	public DatEntUserPo queryUser(String entId, String userId) throws DataAccessException {
		// TODO Auto-generated method stub
		StringBuffer sql = new StringBuffer("SELECT * FROM ");
		sql.append(getEntDbName(entId)).append(".DAT_ENT_USER WHERE USER_ID='").append(userId).append("'");
		return this.queryForObject(sql.toString(), DatEntUserPo.class);
	}*/

	/*@Override
	public int updateInformation(String entId, String email, String nickName, String userDesc)
			throws DataAccessException {
		StringBuffer sql=new StringBuffer("UPDATE ");
		sql.append(getEntDbName(entId)).append(".DAT_ENT_USER SET NICK_NAME='")
			.append(nickName.replaceAll("'", "\\\\"+"'").replaceAll("\"", "\\\\"+"\"")+"',")
			.append("USER_DESC='"+userDesc.replaceAll("'", "\\\\"+"'").replaceAll("\"", "\\\\"+"\"")+"' WHERE EMAIL='"+email+"'");
		return this.update(sql.toString());
	}*/

	/*@Override
	public int updatePassword(String entId, String email, String newLoginPwd) throws DataAccessException {
		StringBuffer sql=new StringBuffer("UPDATE ");
		sql.append(getEntDbName(entId)).append(".DAT_ENT_USER SET LOGIN_PWD='").append(newLoginPwd+"',")
			.append("EMAIL_PWD='"+newLoginPwd+"'")
			.append(" WHERE EMAIL='"+email+"'");
		return this.update(sql.toString());
	}*/

	/*@Override
	public List<DatEntUserPo> queryAgentAndAdmin(String entId) throws DataAccessException {
		StringBuffer sqlBuffer = new StringBuffer("SELECT * FROM ");
		sqlBuffer.append(getEntDbName(entId)).append(".DAT_ENT_USER").append(" WHERE USER_TYPE='2' OR USER_TYPE='3'");
		return this.queryForList(sqlBuffer.toString(), DatEntUserPo.class);
	}*/

	@Override
	public String getDocId(String arg) throws DataAccessException {
		return this.getSequenceStr(arg);
	}

}

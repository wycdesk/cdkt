package com.channelsoft.ems.user.dao.impl;

import java.sql.Types;

import org.springframework.dao.DataAccessException;

import com.channelsoft.cri.dao.jdbc.BaseJdbcMysqlDao;
import com.channelsoft.cri.util.CdeskEncrptDes;
import com.channelsoft.ems.register.util.MD5Util;
import com.channelsoft.ems.user.dao.IUserImportDao;
import com.channelsoft.ems.user.po.DatEntUserPo;

public class UserImportDaoImpl extends BaseJdbcMysqlDao implements IUserImportDao {

	/*@Override
	public int userImport(DatEntUserPo po) throws DataAccessException {
		// TODO Auto-generated method stub
		StringBuffer sql=new StringBuffer();
		String loginPwd=po.getLoginPwd()==null?po.getLoginPwd():CdeskEncrptDes.encryptST(po.getLoginPwd());
		String emailPwd=po.getEmailPwd()==null?null:CdeskEncrptDes.encryptST(po.getEmailPwd());
		sql.append("INSERT INTO ").append(getEntDbName(po.getEntId())).append(".DAT_ENT_USER ");
		sql.append("(USER_ID,ENT_ID,ENT_NAME,USER_TYPE,ROLE_ID,")
			.append("LOGIN_TYPE,LOGIN_NAME,NICK_NAME,EMAIL,USER_STATUS,")
			.append("ACTIVE_CODE,LOGIN_PWD,EMAIL_PWD,USER_NAME,CREATE_TIME,")
			.append("TELPHONE, USER_DESC, REMARK, SIGNATURE, CREATOR_ID,")
			.append("CREATOR_NAME)");
		sql.append(" VALUES(?,?,?,?,?,")
			.append("?,?,?,?,?,")
			.append("?,?,?,?,NOW(),")
			.append("?,?,?,?,?,")
			.append("?)");
		Object args[]={
				po.getUserId(),po.getEntId(),po.getEntName(), po.getUserType(),po.getRoleId(),
				po.getLoginType(),po.getLoginName(),po.getNickName(), po.getEmail(),po.getUserStatus(),
				po.getActiveCode(), loginPwd, emailPwd, po.getUserName(),
				po.getTelPhone(),po.getUserDesc(), po.getRemark(), po.getSignature(), po.getCreatorId(),
				po.getCreatorName()
		};
		int argTypes[]=new int[args.length];
		for(int i=0;i<args.length;i++){
			argTypes[i]=Types.VARCHAR;
		}
		return this.update(sql.toString(), args, argTypes);
	}*/

}

package com.channelsoft.ems.user.dao.impl;

import java.sql.Types;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;

import com.channelsoft.cri.dao.jdbc.BaseJdbcMysqlDao;
import com.channelsoft.cri.util.CdeskEncrptDes;
import com.channelsoft.cri.vo.PageInfo;
import com.channelsoft.ems.group.po.AgentPo;
import com.channelsoft.ems.group.po.GroupPo;
import com.channelsoft.ems.register.util.MD5Util;
import com.channelsoft.ems.user.constant.LoginType;
import com.channelsoft.ems.user.constant.UserStatus;
import com.channelsoft.ems.user.dao.IUsrManageDao;
import com.channelsoft.ems.user.po.DatEntUserPo;
import com.channelsoft.ems.user.po.RolePo;

public class UsrManageDaoImpl extends BaseJdbcMysqlDao implements IUsrManageDao{
	
	/*查询用户列表*/
	/*@Override
	public List<DatEntUserPo> queryUser(DatEntUserPo po, PageInfo pageInfo) {
		// TODO Auto-generated method stub		
		StringBuffer sql=new StringBuffer("SELECT t.*,LOG_GROUP.LOGIN_TIME FROM ");
		sql.append(getEntDbName(po.getEntId())).append(".DAT_ENT_USER t LEFT JOIN ");
		
		sql.append("(SELECT d.USER_ID,max(d.LOGIN_TIME) LOGIN_TIME FROM ");
		sql.append(getEntDbName(po.getEntId())).append(".DAT_USER_LOGIN d");
		sql.append(" GROUP BY d.USER_ID) LOG_GROUP ON t.USER_ID = LOG_GROUP.USER_ID WHERE 1=1 ");
		
		if(StringUtils.isNotBlank(po.getUserType())){		
			sql.append(" AND t.USER_TYPE = '").append(po.getUserType()).append("'");
		}						
		if (StringUtils.isNotBlank(po.getRoleId())) {
			sql.append(" AND t.ROLE_ID = '").append(po.getRoleId()).append("'");
		}
		if(StringUtils.isNotBlank(po.getUserStatus())) {
			sql.append(" AND t.USER_STATUS = '").append(po.getUserStatus()).append("'");
		}
		sql.append(" ORDER BY t.CREATE_TIME DESC");
	    
		System.out.println("sql="+sql);
		return this.queryByPage(sql.toString(), pageInfo, DatEntUserPo.class);
	}*/
	
	/*查询最新用户*/
	/*@Override
	public List<DatEntUserPo> queryLately(DatEntUserPo po, PageInfo pageInfo) {
		// TODO Auto-generated method stub
		StringBuffer sql=new StringBuffer("SELECT t.USER_ID,t.NICK_NAME,t.EMAIL,t.TELPHONE,t.CREATE_TIME,LOG_GROUP.LOGIN_TIME FROM ");
		sql.append(getEntDbName(po.getEntId())).append(".DAT_ENT_USER t LEFT JOIN ");
		
		sql.append("(SELECT d.USER_ID,max(d.LOGIN_TIME) LOGIN_TIME FROM ");
		sql.append(getEntDbName(po.getEntId())).append(".DAT_USER_LOGIN d");
		sql.append(" GROUP BY d.USER_ID) LOG_GROUP ON t.USER_ID = LOG_GROUP.USER_ID");
		sql.append(" WHERE 1=1 ORDER BY t.CREATE_TIME DESC");
		
		System.out.println(sql);
		return this.queryByPage(sql.toString(), pageInfo, DatEntUserPo.class);
	}*/
	
	/*客服类型二级下拉框*/
	public List<RolePo> querySecondLevel(RolePo po) {
		StringBuffer sql=new StringBuffer("SELECT ID, NAME FROM ");
		sql.append(getEntDbName(po.getEntId())).append(".CFG_ROLE WHERE 1=1 ");
		if(StringUtils.isNotBlank(po.getParentId())){
	        sql.append(" AND PARENT_ID='"+po.getParentId()+"'");
		}
		if(StringUtils.isNotBlank(po.getId())){
			sql.append(" AND ID='"+po.getId()+"'");
		}
		System.out.println(sql);
		return this.queryForList(sql.toString(), RolePo.class);
	}
	
	/*删除用户*/
	/*@Override
	public int delete(String entId, String[] ids) throws DataAccessException {
		StringBuffer sql=new StringBuffer("DELETE FROM ");
		sql.append(getEntDbName(entId)).append(".DAT_ENT_USER ");
		String id="";
		if(ids.length!=0)
		id = ids[0];
	
		for(int i=1;i<ids.length;i++){
			id = id+"','"+ids[i];
		}

		sql.append(" WHERE USER_ID IN('").append(id).append("')");
		
		System.out.println(sql);
		return this.update(sql.toString());
	}*/
	
	/*添加用户*/
	/*
	 * @Override
	public int add(DatEntUserPo po) throws DataAccessException {
		StringBuffer sql=new StringBuffer();
		sql.append("INSERT INTO ").append(getEntDbName(po.getEntId())).append(".DAT_ENT_USER (USER_ID,ENT_ID,ENT_NAME,USER_TYPE,ROLE_ID,LOGIN_TYPE,LOGIN_NAME,NICK_NAME,"
				+ "EMAIL,USER_NAME,ACTIVE_CODE,USER_STATUS,CREATE_TIME,UPDATE_TIME) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,NOW(),NOW())");
		Object args[]={po.getUserId(),po.getEntId(),po.getEntName(),
				po.getUserType(),po.getRoleId(),po.getLoginType(),po.getLoginName(),po.getNickName(),
				po.getEmail(),po.getUserName(),po.getActiveCode(),po.getUserStatus()};
		int argTypes[]=new int[args.length];
		for(int i=0;i<args.length;i++){
			argTypes[i]=Types.VARCHAR;
		}
		return this.update(sql.toString(), args, argTypes);
	}*/
	
	/*修改用户信息*/
	/*@Override
	public int update(DatEntUserPo po) throws DataAccessException {
		StringBuffer sql = new StringBuffer();
		
		sql.append("UPDATE ").append(getEntDbName(po.getEntId())).append(".DAT_ENT_USER SET USER_TYPE='"+po.getUserType()+"',EMAIL='"+po.getEmail());
		sql.append("',FIXED_PHONE='"+po.getFixedPhone()+"',ORG='"+po.getOrg()+"',USER_LABEL='"+po.getUserLabel()+"',LANG='"+po.getLang()+"',REMARK='"+po.getRemark());
		sql.append("',USER_DESC='"+po.getUserDesc()+"',USER_NAME='"+po.getUserName()+"',NICK_NAME='"+po.getNickName()+"'");		
		
		sql.append(",TELPHONE='"+po.getTelPhone()+"'");
		
		sql.append(",ROLE_ID='"+po.getRoleId()+"',UPDATE_TIME=NOW(), CONTACT_PHONE='"+po.getContactPhone()+"' ,SIGNATURE='"+po.getSignature()+"'");
		sql.append(" WHERE USER_ID='"+po.getUserId()+"'");
		
		System.out.println(sql);
		return this.update(sql.toString());
	}*/
	
	
	/*绑定手机*/
	/*@Override
	public int bindPhone(DatEntUserPo po) throws DataAccessException {
		StringBuffer sql = new StringBuffer();		
		sql.append("UPDATE ").append(getEntDbName(po.getEntId())).append(".DAT_ENT_USER SET PHONE_BINDED='"+po.getPhoneBinded()+"'");
		sql.append(" ,UPDATE_TIME=NOW()");
		sql.append(" WHERE 1=1 AND USER_ID='"+po.getUserId()+"'");
		
		System.out.println(sql);
		return this.update(sql.toString());
	}*/
	
	/*检测手机是否已绑定*/
	/*@Override
	public boolean existsPhone(String entId, String phone,String userId) throws DataAccessException {
		// TODO Auto-generated method stub
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT COUNT(*) FROM ").append(getEntDbName(entId)).append(".DAT_ENT_USER WHERE 1=1 AND TELPHONE='").append(phone).append("'");
		sql.append(" AND PHONE_BINDED=1 AND USER_ID <> '"+userId+"'");
		
		System.out.println(sql.toString());
		
		int num=this.queryForInt(sql.toString());
		return num>0?true:false;
	}*/
	
	/*检测手机是否已存在*/
	/*@Override
	public boolean existsPhone1(String entId, String phone,String userId) throws DataAccessException {
		// TODO Auto-generated method stub
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT COUNT(*) FROM ").append(getEntDbName(entId)).append(".DAT_ENT_USER WHERE 1=1 AND TELPHONE='").append(phone).append("'");
		sql.append(" AND USER_ID <> '"+userId+"'");
		
		System.out.println(sql.toString());
		
		int num=this.queryForInt(sql.toString());
		return num>0?true:false;
	}*/
	
	/*批量编辑用户信息*/
	/*@Override
	public int updateBatch(DatEntUserPo po,String [] ids) throws DataAccessException {
		StringBuffer sql = new StringBuffer();		
		String id = ids[0];		
		for(int i=1;i<ids.length;i++){
			id = id+"','"+ids[i];
		}		
		sql.append("UPDATE ").append(getEntDbName(po.getEntId())).append(".DAT_ENT_USER SET USER_LABEL='"+po.getUserLabel()+"',UPDATE_TIME=NOW()");
		if(StringUtils.isNotBlank(po.getUserStatus())){		
			sql.append(",USER_STATUS = '").append(po.getUserStatus()).append("'");
		}	
		sql.append(" WHERE USER_ID IN('").append(id).append("')");
		
		System.out.println(sql);
		return this.update(sql.toString());
	}*/
	
	/*修改用户状态*/
	/*@Override
	public int updateStatus(DatEntUserPo po) throws DataAccessException {
		StringBuffer sql = new StringBuffer();
		
		sql.append("UPDATE ").append(getEntDbName(po.getEntId())).append(".DAT_ENT_USER SET USER_STATUS='"+po.getUserStatus()+"'");
		sql.append(",UPDATE_TIME=NOW()");
		sql.append(" WHERE 1=1 AND USER_ID='"+po.getUserId()+"'");
		return this.update(sql.toString());
	}*/
	
	/*修改密码*/
	/*@Override
	public int setPwd(String password, String entId, String userId) throws DataAccessException{
		// TODO Auto-generated method stub
		StringBuffer sql=new StringBuffer();
		String pwd=CdeskEncrptDes.encryptST(password);
		sql.append("UPDATE ").append(getEntDbName(entId)).append(".DAT_ENT_USER SET LOGIN_PWD='").append(pwd+"'")
			.append(",EMAIL_PWD='"+pwd+"',UPDATE_TIME=NOW(),USER_STATUS=").append(UserStatus.NORMAL.value)
			.append(" WHERE USER_ID='"+userId+"'");
		
		System.out.println(sql);
		return this.update(sql.toString());
	}*/

	/*@Override
	public DatEntUserPo queryUser(String entId, String loginName) throws DataAccessException {
		// TODO Auto-generated method stub
		StringBuffer sql=new StringBuffer("SELECT t.* FROM ").append(getEntDbName(entId)).append(".DAT_ENT_USER t ");
		sql.append("where t.LOGIN_NAME='").append(loginName).append("'");
		return this.queryForObject(sql.toString(), DatEntUserPo.class);
	}*/

	/*@Override
	public List<DatEntUserPo> query(DatEntUserPo po, PageInfo pageInfo) {
		// TODO Auto-generated method stub
		StringBuffer sql=new StringBuffer("SELECT t.* FROM ").append(getEntDbName(po.getEntId())).append(".DAT_ENT_USER t ");
		sql.append("where 1=1");
		if(StringUtils.isNotBlank(po.getUserId())){
			sql.append(" and USER_ID='").append(po.getUserId()).append("'");
		}
		if(StringUtils.isNotBlank(po.getLoginName())){
			sql.append(" and LOGIN_NAME='").append(po.getLoginName()).append("'");
		}
		if(StringUtils.isNotBlank(po.getEmail())){
			sql.append(" and EMAIL='").append(po.getEmail()).append("'");
		}
		if(StringUtils.isNotBlank(po.getUserType())){
			sql.append(" and USER_TYPE='").append(po.getUserType()).append("'");
		}
		if(StringUtils.isNotBlank(po.getUserStatus())){
			sql.append(" and USER_STATUS='").append(po.getUserStatus()).append("'");
		}
		
		return this.queryByPage(sql.toString(), pageInfo, DatEntUserPo.class);
	}*/

	/*@Override
	public int importUserUpdate(DatEntUserPo po) throws DataAccessException {
		// TODO Auto-generated method stub
		StringBuffer sql = new StringBuffer();
		
		sql.append("UPDATE ").append(getEntDbName(po.getEntId())).append(".DAT_ENT_USER SET UPDATE_TIME=NOW()");
		if(StringUtils.isNotBlank(po.getUserType())){
			sql.append(" ,USER_TYPE='").append(po.getUserType()).append("'");
		}
		if(StringUtils.isNotBlank(po.getRoleId())){
			sql.append(" ,ROLE_ID='").append(po.getRoleId()).append("'");
		}
		if(StringUtils.isNotBlank(po.getLoginType())){
			sql.append(" ,LOGIN_TYPE='").append(po.getLoginType()).append("'");
		}
		if(StringUtils.isNotBlank(po.getLoginName())){
			sql.append(" ,LOGIN_NAME='").append(po.getLoginName()).append("'");
		}
		if(StringUtils.isNotBlank(po.getNickName())){
			sql.append(" ,NICK_NAME='").append(po.getNickName()).append("'");
		}
		if(StringUtils.isNotBlank(po.getTelPhone())){
			sql.append(" ,TELPHONE='").append(po.getTelPhone()).append("'");
		}
		if(StringUtils.isNotBlank(po.getContactPhone())){
			sql.append(" ,CONTACT_PHONE='").append(po.getContactPhone()).append("'");
		}
		if(StringUtils.isNotBlank(po.getUpdatorId())){
			sql.append(" ,UPDATOR_ID='").append(po.getUpdatorId()).append("'");
		}
		if(StringUtils.isNotBlank(po.getUpdatorName())){
			sql.append(" ,UPDATOR_NAME='").append(po.getUpdatorName()).append("'");
		}
		if(StringUtils.isNotBlank(po.getUserDesc())){
			sql.append(" ,USER_DESC='").append(po.getUserDesc().replaceAll("'", "\\\\"+"'").replaceAll("\"", "\\\\"+"\"")).append("'");
		}
		if(StringUtils.isNotBlank(po.getRemark())){
			sql.append(" ,REMARK='").append(po.getRemark().replaceAll("'", "\\\\"+"'").replaceAll("\"", "\\\\"+"\"")).append("'");
		}
		if(StringUtils.isNotBlank(po.getSignature())){
			sql.append(" ,SIGNATURE='").append(po.getSignature().replaceAll("'", "\\\\"+"'").replaceAll("\"", "\\\\"+"\"")).append("'");
		}
		sql.append(" WHERE EMAIL='"+po.getEmail()+"'");
		
//		System.out.println(sql);
		return this.update(sql.toString());
	}*/
	
	/*客服所属客服组*/
	public List<GroupPo> belongGroup(DatEntUserPo po) {
		// TODO Auto-generated method stub
		StringBuffer sql=new StringBuffer();
         
		sql.append("SELECT A.AGENT_ID,A.USER_ID,G.GROUP_ID,G.GROUP_NAME FROM ").append(getEntDbName(po.getEntId())).append(".DAT_AGENT_ASSIGN  A inner join ").append(getEntDbName(po.getEntId()))
		   .append(".DAT_USER_GROUP G ON A.GROUP_ID=G.GROUP_ID WHERE 1=1");
         if(StringUtils.isNotBlank(po.getLoginName())){
    	     sql.append(" AND A.USER_ID='").append(po.getUserId()).append("'");
         }
		
         System.out.println(sql.toString());
		return this.queryForList(sql.toString(), GroupPo.class);
	}
	
	/*分配客服组,先删再加*/
	@Override
	public int assignAgent(String entId,String[] groupId,String agentId,List<AgentPo> agentPos) throws DataAccessException {
		StringBuffer sql = new StringBuffer();	
		StringBuffer sql1 = new StringBuffer();			
		int result=0;
		
		if(groupId.length>0){		
			String oId = groupId[0];			
		 for(int i=1;i<groupId.length;i++){
		 	oId = oId+"','"+groupId[i];
		 }			
			sql=new StringBuffer("DELETE FROM ");
			sql.append(getEntDbName(entId)).append(".DAT_AGENT_ASSIGN ");
			sql.append(" WHERE 1=1 AND AGENT_ID='"+agentId+"'");
			
			result=this.update(sql.toString());
		}
		
		if(agentPos.size()>0){
		sql1.append("INSERT INTO ").append(getEntDbName(entId)).append(".DAT_AGENT_ASSIGN")
			.append("(AGENT_ID,GROUP_ID,CREATE_TIME,CREATOR_ID,CREATOR_NAME,USER_ID) VALUES");
		for(int i=0;i<agentPos.size();i++){
			AgentPo poI=agentPos.get(i);
			sql1.append("('"+poI.getAgentId()+"','"+poI.getGroupId()+"',NOW(),'"+poI.getCreatorId()+"','"+poI.getCreatorName()+"','"+poI.getUserId()+"')");
			if(i<agentPos.size()-1)
				sql1.append(",");
		}
		result= this.update(sql1.toString());		
	    }
		
		return result;
	}

	/*@Override
	public int changePhoto(DatEntUserPo po) throws DataAccessException {
		// TODO Auto-generated method stub
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE ").append(getEntDbName(po.getEntId())).append(".DAT_ENT_USER SET UPDATE_TIME=NOW()");
		sql.append(" ,UPDATOR_ID='").append(po.getUpdatorId()).append("'");
		sql.append(" ,UPDATOR_NAME='").append(po.getUpdatorName()).append("'");
		sql.append(" ,PHOTO_URL='").append(po.getPhotoUrl()).append("'");
		sql.append(" WHERE USER_ID='"+po.getUserId()+"'");
		return this.update(sql.toString());
	}*/

	/*@Override
	public List<DatEntUserPo> query(String entId, String userAccount,
			String userAccountType) throws DataAccessException {
		// TODO Auto-generated method stub
		StringBuffer sql=new StringBuffer("SELECT t.* FROM ").append(getEntDbName(entId)).append(".DAT_ENT_USER t ");
		sql.append("where 1=1");
		*//**
		 * 根据账号类型查询用户
		 *//*
		if(LoginType.MAILBOX.value.equals(userAccountType)){
			sql.append(" and t.EMAIL='").append(userAccount).append("'");
		}
		else if(LoginType.TELEPHONE.value.equals(userAccountType)){
			sql.append(" and t.TELPHONE='").append(userAccount).append("'");
		}
		else if(LoginType.WECHAT.value.equals(userAccountType)){
			sql.append(" and t.WEIXIN='").append(userAccount).append("'");
		}
		else if(LoginType.QQ.value.equals(userAccountType)){
			sql.append(" and t.QQ='").append(userAccount).append("'");
		}
		else if(LoginType.MICROBLOG.value.equals(userAccountType)){
			sql.append(" and t.WEIBO='").append(userAccount).append("'");
		}
		sql.append(" order by t.CREATE_TIME DESC");
		return this.queryForList(sql.toString(), DatEntUserPo.class);
	}*/

	/*@Override
	public int updateUser(DatEntUserPo po) throws DataAccessException {
		// TODO Auto-generated method stub
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE ").append(getEntDbName(po.getEntId())).append(".DAT_ENT_USER SET UPDATE_TIME=NOW()");
		if(StringUtils.isNotBlank(po.getNickName())){
			sql.append(" ,NICK_NAME='").append(po.getNickName()).append("'");
		}
		if(StringUtils.isNotBlank(po.getUserName())){
			sql.append(" ,USER_NAME='").append(po.getUserName()).append("'");
		}
		if(StringUtils.isNotBlank(po.getTelPhone())){
			sql.append(" ,TELPHONE='").append(po.getTelPhone()).append("'");
		}
		if(StringUtils.isNotBlank(po.getContactPhone())){
			sql.append(" ,CONTACT_PHONE='").append(po.getContactPhone()).append("'");
		}
		if(StringUtils.isNotBlank(po.getUpdatorId())){
			sql.append(" ,UPDATOR_ID='").append(po.getUpdatorId()).append("'");
		}
		if(StringUtils.isNotBlank(po.getUpdatorName())){
			sql.append(" ,UPDATOR_NAME='").append(po.getUpdatorName()).append("'");
		}
		if(StringUtils.isNotBlank(po.getUserDesc())){
			sql.append(" ,USER_DESC='").append(po.getUserDesc()).append("'");
		}
		if(StringUtils.isNotBlank(po.getRemark())){
			sql.append(" ,REMARK='").append(po.getRemark()).append("'");
		}
		if(StringUtils.isNotBlank(po.getUserDesc())){
			sql.append(" ,USER_DESC='").append(po.getUserDesc()).append("'");
		}
		if(StringUtils.isNotBlank(po.getEmail())){
			sql.append(" ,EMAIL='").append(po.getEmail()).append("'");
		}
		sql.append(" WHERE 1=1 ");
		if(StringUtils.isNotBlank(po.getLoginName())){
			sql.append(" and LOGIN_NAME='").append(po.getLoginName()).append("'");
		}
		else{
			sql.append(" and USER_ID='").append(po.getUserId()).append("'");
		}
		return this.update(sql.toString());
	}*/

	
	/*查询创始人邮箱*/
	@Override
	public String queryFounder(String entId) {
		StringBuffer sql=new StringBuffer("SELECT EMAIL FROM DAT_ENT_INFO");
		sql.append(" WHERE ENT_ID='"+entId+"'");
		
		String email=this.queryForString(sql.toString());
		System.out.println("FOUNDER'S EMAIL="+email);
		return email;	
	}

	/*@Override
	public int mergeUser(String entId,DatEntUserPo merge, DatEntUserPo target) throws DataAccessException {
		int up=this.updateUser(target);
		if(up>0){
			if(StringUtils.isNotBlank(merge.getEmail())){
				StringBuffer sql=new StringBuffer();
				sql.append("DELETE FROM ").append(getEntDbName(entId))
				.append(".DAT_ENT_USER WHERE EMAIL='"+merge.getEmail()+"'");
				int del=this.update(sql.toString());
				return del;
			}else{
				return 0;
			}
		}
		return 0;
	}*/

	/*@Override
	public List<DatEntUserPo> queryOrdinaryByValue(String entId, String value,String email) throws DataAccessException {
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT * FROM ").append(getEntDbName(entId))
		.append(".DAT_ENT_USER WHERE (NICK_NAME LIKE '%"+value+"%' OR EMAIL LIKE '%"+value+"%')")
		.append(" AND USER_TYPE='1' AND EMAIL!='"+email+"'");
		return this.queryForList(sql.toString(), DatEntUserPo.class);
	}*/

	/*查询用户上次登录时间*/
	public String queryLastLogin(String entId,String userId) throws DataAccessException{
		StringBuffer sql=new StringBuffer("SELECT max(d.LOGIN_TIME) LOGIN_TIME FROM ");
		sql.append(getEntDbName(entId)).append(".DAT_USER_LOGIN d WHERE 1=1 ");
		if(StringUtils.isNotBlank(userId)){
	        sql.append("AND d.USER_ID = '"+userId+"'");
		}
		System.out.println(sql);
		return this.queryForString(sql.toString());
	}
}

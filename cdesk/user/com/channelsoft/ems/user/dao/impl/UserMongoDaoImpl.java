package com.channelsoft.ems.user.dao.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.Assert;

import com.channelsoft.cri.mongo.BaseMongoTemplate;
import com.channelsoft.cri.util.CdeskEncrptDes;
import com.channelsoft.cri.util.DBObjectUtils;
import com.channelsoft.cri.util.UUIDGeneratorUtil;
import com.channelsoft.cri.vo.PageInfo;
import com.channelsoft.ems.ent.po.DatEntInfoPo;
import com.channelsoft.ems.redis.util.ParamUtils;
import com.channelsoft.ems.register.po.RegisterInfoPo;
import com.channelsoft.ems.user.constant.LoginType;
import com.channelsoft.ems.user.constant.UserStatus;
import com.channelsoft.ems.user.constant.UserType;
import com.channelsoft.ems.user.dao.IUserMongoDao;
import com.channelsoft.ems.user.po.DatEntUserPo;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class UserMongoDaoImpl   extends BaseMongoTemplate  implements IUserMongoDao {
	@Autowired
	UserDaoImpl userDaoImpl;
	/*查询用户列表*/
	@Override
	public List<DatEntUserPo> queryUser(DatEntUserPo po, PageInfo pageInfo) {
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT d.USER_ID,max(d.LOGIN_TIME) LOGIN_TIME FROM ");
		sql.append(getEntDbName(po.getEntId())).append(".DAT_USER_LOGIN d");
		sql.append(" GROUP BY d.USER_ID ");
		List<DatEntUserPo> loginList=userDaoImpl.queryForList(sql.toString(), DatEntUserPo.class);
		
		Query user=new Query();
		List<DatEntUserPo> userList=null;
		if(StringUtils.isNotBlank(po.getUserType())){
			user.addCriteria(Criteria.where("userType").is(po.getUserType()));
		}						
		if (StringUtils.isNotBlank(po.getRoleId())) {
			user.addCriteria(Criteria.where("roleId").is(po.getRoleId()));
		}
		if(StringUtils.isNotBlank(po.getUserStatus())) {
			user.addCriteria(Criteria.where("userStatus").is(po.getUserStatus()));
		}
		//sql.append(" ORDER BY t.CREATE_TIME DESC");
		userList=this.findByPage(user, this.getUserTable(po.getEntId()), DatEntUserPo.class, pageInfo);
		for(DatEntUserPo userPo : userList){
			for(int i=0;i<loginList.size();i++){
				DatEntUserPo loginPo=loginList.get(i);
				if(loginPo.getUserId().equals(userPo.getUserId())){
					userPo.setLoginTime(loginPo.getLoginTime());
					loginList.remove(i);
					break;
				}
			}
			if(loginList.size()<=0){
				break;
			}
		}
		return userList;
	}
	
	@Override
	public int resetUser(DatEntUserPo userPo) throws DataAccessException {
		DBObject quey=new BasicDBObject("email",userPo.getEmail());
		DBObject update=new BasicDBObject("activeCode",userPo.getActiveCode());
		DBObject set=new BasicDBObject();
		Date time=new Date();
		String updateTime=time.toLocaleString();
		update.put("updateTime", updateTime);
		update.put("updator", userPo.getEmail());
		set.put("$set", update);
		return this.updateFromDbObject(quey, set, getUserTable(userPo.getEntId()));
	}
	
	@Override
	public int registerPwd(String userName, String nickName, String password, String entId, String code)
			throws DataAccessException{
		DBObject quey=new BasicDBObject("activeCode",code);
		DBObject update=new BasicDBObject();
		DBObject set=new BasicDBObject();
		String pwd=CdeskEncrptDes.encryptST(password);
		update.put("loginPwd", pwd);
		update.put("emailPwd", pwd);
		update.put("userStatus", UserStatus.NORMAL.value);
		if(StringUtils.isNotBlank(nickName)){
			update.put("nickName",nickName);
		}
		if(StringUtils.isNotBlank(userName)){
			update.put("userName", userName);
		}
		set.put("$set", update);
		return this.updateFromDbObject(quey, set, getUserTable(entId));
	}

	@Override
	public DatEntUserPo getEntUserPo(String entId,String code,String email) throws DataAccessException {

		Query query=new Query();
		if(StringUtils.isNotBlank(code))
			query.addCriteria(Criteria.where("activeCode").is(code));
		else if(StringUtils.isNotBlank(email))
			query.addCriteria(Criteria.where("email").is(email));
		query.addCriteria(Criteria.where("userStatus").ne("9"));
		return this.findOne(query, getUserTable(entId), DatEntUserPo.class);
	}
	@Override
	public int registerBase(DatEntUserPo po) throws DataAccessException{
		//Date time=new Date();
		//String createTime=time.toLocaleString();
	    Date date=new Date();
	    DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    String time=format.format(date);
		po.setCreateTime(time);
		return this.add(po, po.getEntId());
	}
	
	@Override
	public RegisterInfoPo getEntInfo(String entId) throws DataAccessException{
		Query query=new Query();
		query.addCriteria(Criteria.where("domainName").is(entId));
		String collectionName="cdesk_ent_info";
		return this.findOne(query, collectionName, RegisterInfoPo.class);
	}

	/*添加用户*/
	@Override
	public int add(DBObject dbo, String entId) throws DataAccessException {
		// TODO Auto-generated method stub
		Assert.notNull(entId, "企业id不能为空！");
		Assert.notNull(dbo.get("userId"), "用户编号不能为空！");
		//Assert.notNull(dbo.get("loginName"), "登录帐号不能为空！");
		String webchatId=UUIDGeneratorUtil.getUUID();
		if(!dbo.containsField("webchatId")||StringUtils.isBlank((String)dbo.get("webchatId"))){
			dbo.put("webchatId", webchatId);
		}
		if(!dbo.containsField("vedioId")||StringUtils.isBlank((String)dbo.get("vedioId"))){
			dbo.put("vedioId", webchatId);
		}
		if(dbo.containsField("userName")&&"null".equals((String)dbo.get("userName"))){
			dbo.put("userName", "");
		}
		return this.intsertFromDbObject(dbo, getUserTable(entId));
	}

	@Override
	public String getUserTable(String entId) throws DataAccessException {
		// TODO Auto-generated method stub
		return "entId_"+entId+"_user";
	}

	/*添加用户*/
	@Override
	public int add(DatEntUserPo po, String entId) throws DataAccessException {
		// TODO Auto-generated method stub
		
	    //if(StringUtils.isBlank(po.getLang())) po.setLang("1");
	    if(StringUtils.isBlank(po.getPhoneBinded())) po.setPhoneBinded("0");
	    if(StringUtils.isBlank(po.getCreateTime())){
	        Date date=new Date();
		    DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    String time=format.format(date);
		    po.setCreateTime(time);
		    po.setUpdateTime(time);
	    }
	    DatEntInfoPo ent=ParamUtils.getEntInfo(entId);
		boolean isExist=ent==null?false:true;
		if(StringUtils.isBlank(po.getCcodEntId())&&isExist){
			po.setCcodEntId(ent.getCcodEntId());
		}
		DBObject dbo=DBObjectUtils.getDBObject(po);
	  
		return this.add(dbo, entId);

	}

	@Override
	public int dropUserTable(String entId) throws DataAccessException {
		// TODO Auto-generated method stub
		this.drop(getUserTable(entId));
		return 1;
	}

	/*更新用户信息*/
	@Override
	public int updateUser(DBObject dbo, String entId, String userId)
			throws DataAccessException {
		// TODO Auto-generated method stub
		Query query=new Query();
		query.addCriteria(Criteria.where("userId").is(userId));
		Update update=new Update();
		for(String key:dbo.keySet()){
			update.set(key, dbo.get(key));
		}
		
		this.update(query, update, getUserTable(entId));
		return 1;
	}

	/*查询用户*/
	@Override
	public List<DBObject> queryUserList(DBObject queryObject, PageInfo pageInfo)
			throws DataAccessException {
		// TODO Auto-generated method stub

		Query query=new Query();
		
		
		if(queryObject.get("userType")!=null){
			query.addCriteria(Criteria.where("userType").is(queryObject.get("userType")));
		}
		if(queryObject.get("loginName")!=null){
			query.addCriteria(Criteria.where("loginName").is(queryObject.get("loginName")));
		}
		if(queryObject.get("userId")!=null){
			try{
				DBObject tran=(DBObject)queryObject.get("userId");
				if(tran.containsField("$in")){
					query.addCriteria(Criteria.where("userId").in((String[])tran.get("$in")));
				}
			}catch(Exception e){
				query.addCriteria(Criteria.where("userId").is(queryObject.get("userId")));
			}
		}
		
		if(queryObject.get("userStatus")!=null){
			query.addCriteria(Criteria.where("userStatus").is(queryObject.get("userStatus")));
		}
		if(queryObject.get("userStatus")==null){
			query.addCriteria(Criteria.where("userStatus").ne("9"));
		}
		//查询负责人（客服或管理员）的情况
		if(queryObject.get("isCharge")!=null){
			queryObject.removeField("isCharge");
			Criteria or = new Criteria();
			query.addCriteria(or.orOperator(Criteria.where("userType").is("2"),
					Criteria.where("userType").is("3")));
			query.with(new Sort(Direction.DESC, "createTime"));
			return this.findByPage(query, getUserTable(queryObject.get("entId")+""), DBObject.class, pageInfo);
		}
		if(queryObject.get("loginPwd")!=null){
			query.addCriteria(Criteria.where("loginPwd").is(queryObject.get("loginPwd")));
		}
		if(queryObject.get("roleId")!=null){
			query.addCriteria(Criteria.where("roleId").is(queryObject.get("roleId")));
		}
		
		if(queryObject.get("email")!=null){
			query.addCriteria(Criteria.where("email").is(queryObject.get("email")));
		}
		
		if(queryObject.get("dptId")!=null){
			query.addCriteria(Criteria.where("dptId").is(queryObject.get("dptId")));
		}
		if(queryObject.get("jobId")!=null){
			query.addCriteria(Criteria.where("jobId").is(queryObject.get("jobId")));
		}
            
		// 按创建时间降序排序
		query.with(new Sort(Direction.DESC, "createTime"));
		return this.findByPage(query, getUserTable(queryObject.get("entId")+""), DBObject.class, pageInfo);
	}
	
	/*检测手机是否已经绑定*/
	public long existsPhone(DBObject dbo) throws DataAccessException{
		Query query=new Query();
		if(dbo.get("telPhone")!=null){
			query.addCriteria(Criteria.where("telPhone").is(dbo.get("telPhone")));
		}	    
		if(dbo.get("userId")!=null){
			query.addCriteria(Criteria.where("userId").ne(dbo.get("userId")));
		}
		query.addCriteria(Criteria.where("phoneBinded").is("1"));
		
		query.addCriteria(Criteria.where("userStatus").ne("9"));
		
		return this.count(query, getUserTable(dbo.get("entId")+""));
	}
	
	/*检测手机号是否已存在*/
	public long existsPhone1(DBObject dbo) throws DataAccessException{
		Query query=new Query();
		if(dbo.get("telPhone")!=null){
			query.addCriteria(Criteria.where("telPhone").is(dbo.get("telPhone")));
		}
		if(dbo.get("userId")!=null){
			query.addCriteria(Criteria.where("userId").ne(dbo.get("userId")));
		}
		query.addCriteria(Criteria.where("userStatus").ne("9"));
		
		return this.count(query, getUserTable(dbo.get("entId")+""));
	}
	
	/*设置密码*/
	@Override
	public int setPwd(DBObject dbo, String entId, String userId)throws DataAccessException {
		// TODO Auto-generated method stub
		String loginPwd=CdeskEncrptDes.encryptST(dbo.get("loginPwd")+"");
		String emailPwd=loginPwd;
		
		dbo.put("loginPwd", loginPwd);
		dbo.put("emailPwd", emailPwd);
		
		Query query=new Query();
		query.addCriteria(Criteria.where("userId").is(userId));
		Update update=new Update();
		for(String key:dbo.keySet()){
			update.set(key, dbo.get(key));
		}
		
		this.update(query, update, getUserTable(entId));
		return 1;
	}
	
	/*删除用户*/
	@Override
	public int deleteUser(String entId, String[] ids)
	{
/*		String collectioName = this.getUserTable(entId);
		BasicDBList value = new BasicDBList();
		for (String userId : ids)
		{
			value.add((new BasicDBObject("userId", userId)));
		}
		BasicDBObject queryObject = new BasicDBObject();
		queryObject.append("$or", value);
		return this.deleteFromDbObject(queryObject, collectioName);*/
		
		String collectioName = this.getUserTable(entId);			
		DBObject queryObject=new BasicDBObject();
		DBObject update=new BasicDBObject();	
		DBObject set=new BasicDBObject();
					
		for (String userId : ids)
		{
			queryObject.put("userId", userId);
			update.put("userStatus","9");
			set.put("$set", update);
			this.updateFromDbObject(queryObject, set, collectioName);
		}	
				
		return 1;
	}

	@Override
	public List<DBObject> query(String entId, String userAccount,String userAccountType) throws DataAccessException {
		// TODO Auto-generated method stub
		Query query=new Query();
		/**
		 * 根据账号类型查询用户
		 */
		if(LoginType.MAILBOX.value.equals(userAccountType)){
			query.addCriteria(Criteria.where("email").is(userAccount));
		}
		else if(LoginType.TELEPHONE.value.equals(userAccountType)){
			query.addCriteria(Criteria.where("telPhone").is(userAccount));
		}
		else if(LoginType.WECHAT.value.equals(userAccountType)){
			query.addCriteria(Criteria.where("weixin").is(userAccount));
		}
		else if(LoginType.QQ.value.equals(userAccountType)){
			query.addCriteria(Criteria.where("qq").is(userAccount));
		}
		else if(LoginType.MICROBLOG.value.equals(userAccountType)){
			query.addCriteria(Criteria.where("weibo").is(userAccount));
		}
		else if(LoginType.IM.value.equals(userAccountType)){
			query.addCriteria(Criteria.where("webchatId").is(userAccount));
		}
		query.addCriteria(Criteria.where("userStatus").ne(UserStatus.DELETED.value));
		return this.findList(query, getUserTable(entId), DBObject.class);
	}

	/*检测邮箱是否已经注册*/
	public long existsEmails(DBObject dbo) throws DataAccessException{
		Query query=new Query();
		Criteria or=new Criteria();
		if(dbo.get("email")!=null){
			query.addCriteria(or.orOperator(Criteria.where("email").is(dbo.get("email")),Criteria.where("loginName").is(dbo.get("email"))) );
			query.addCriteria(Criteria.where("userStatus").ne("9"));
		}	    
		return this.count(query, getUserTable(dbo.get("entId")+""));
	}

	@Override
	public List<DatEntUserPo> queryAgentAndAdmin(String entId)
			throws DataAccessException {
		// TODO Auto-generated method stub
		Query query=new Query();
		List<String> userTypeList=Arrays.asList(new String[]{UserType.SERVICE.value,UserType.ADMINISTRATOR.value});
		query.addCriteria(Criteria.where("userType").in(userTypeList));
		query.addCriteria(Criteria.where("userStatus").is(UserStatus.NORMAL.value));
		return this.findList(query, getUserTable(entId), DatEntUserPo.class);
	}
	
	/*更新用户详情复选框类型自定义字段*/
	@Override
	public int updateCheckBox(DBObject dbo, String entId, String userId,String field,String checked)
			throws DataAccessException {
		// TODO Auto-generated method stub
		Query query=new Query();
		query.addCriteria(Criteria.where("userId").is(userId));
		Update update=new Update();

		if(checked.equals("true")){
			update.addToSet(field, dbo.get(field));	
			update.set("updateTime", dbo.get("updateTime"));
		}else{
			update.pull(field, dbo.get(field));	
			update.set("updateTime", dbo.get("updateTime"));
		}
		this.update(query, update, getUserTable(entId));
		return 1;
	}
	

	@Override
	public int updateUserByEmail(DBObject dbo, String entId, String email) throws DataAccessException {
		// TODO Auto-generated method stub
		Query query=new Query();
		query.addCriteria(Criteria.where("email").is(email));
		Update update=new Update();
		for(String key:dbo.keySet()){
			update.set(key, dbo.get(key));
		}
		
		return this.update(query, update, getUserTable(entId));
	}

	@Override
	public int updateUser(DBObject dbo) throws DataAccessException {
		Query query=new Query();
	
		if(StringUtils.isNotBlank((String)dbo.get("loginName"))){
			query.addCriteria(Criteria.where("loginName").is(dbo.get("loginName")));
		}
		else{
			query.addCriteria(Criteria.where("userId").is(dbo.get("userId")));
		}
	
		Update update=new Update();
		for(String key:dbo.keySet()){
			if(StringUtils.isNotBlank(dbo.get(key)+"")){
				update.set(key, dbo.get(key));
			}			
		}
		
		this.update(query, update, getUserTable(dbo.get("entId")+""));
		return 1;
	}
	
	@Override
	public int updateInformation(String entId,DatEntUserPo po)
			throws DataAccessException {
		DBObject update=new BasicDBObject();
		DBObject queryObject=new BasicDBObject();
		DBObject set=new BasicDBObject();
		queryObject.put("userId", po.getUserId());
		update.put("nickName", po.getNickName());
		update.put("userDesc", po.getUserDesc());
		update.put("email", po.getEmail());
		set.put("$set", update);
		String collection=this.getUserTable(entId);
		return this.updateFromDbObject(queryObject, set, collection);
	}
	
	@Override
	public int updatePassword(String entId, String userId, String newLoginPwd) throws DataAccessException {
		DBObject update=new BasicDBObject();
		DBObject queryObject=new BasicDBObject();
		DBObject set=new BasicDBObject();
		queryObject.put("userId", userId);
		update.put("loginPwd",newLoginPwd);
		//update.put("emailPwd",newLoginPwd);
		set.put("$set", update);
		String collection=this.getUserTable(entId);
		return this.updateFromDbObject(queryObject, set, collection);
		
	}

	@Override
	public List<DatEntUserPo> queryAll(String enterpriseid) throws DataAccessException {
		String collection=this.getUserTable(enterpriseid);
		Query  query = new Query();
		query.addCriteria(Criteria.where("userStatus").ne("9"));
		return this.findList(query, collection, DatEntUserPo.class);
	}
	
	@Override
	public List<DatEntUserPo> queryOrdinaryByValue(String entId, String value,String loginName, String userId) throws DataAccessException {
		String collection=this.getUserTable(entId);
		value=value.replaceAll("\\.", "\\\\.");
		Query query=new Query();
		Criteria or=new Criteria();
		query.addCriteria(Criteria.where("userType").is("1"));
		query.addCriteria(Criteria.where("userStatus").ne("9"));
		if(StringUtils.isNotBlank(loginName)){
			query.addCriteria(Criteria.where("loginName").ne(loginName));
		}
		if(StringUtils.isNotBlank(userId)){
			query.addCriteria(Criteria.where("userId").ne(userId));
		}
		query.addCriteria(or.orOperator(Criteria.where("loginName").regex(".*"+value+".*"),Criteria.where("userName").regex(".*"+value+".*")));
		List<DatEntUserPo> list=this.findList(query, collection, DatEntUserPo.class);
		return list;
	}
	
	@Override
	public int mergeUser(String entId,DatEntUserPo merge, DatEntUserPo target) throws DataAccessException {
		DBObject update=DBObjectUtils.getDBObject(target);
		DBObject queryObject=new BasicDBObject("userId",target.getUserId());
		Date date=new Date();
		String time=date.toLocaleString(),collection=this.getUserTable(entId);
		update.put("updateTime", time);
		DBObject set=new BasicDBObject("$set",update);
		
		this.updateFromDbObject(queryObject, set, collection);
		
		if(StringUtils.isNotBlank(merge.getUserId())){
			queryObject.put("userId", merge.getUserId());
			DBObject updt=new BasicDBObject("updateTime",time);
			updt.put("userStatus", "9");
			set.put("$set", updt);
			int del=this.updateFromDbObject(queryObject, set, collection);
			return del;
		}
		return -1;
	}
	
	public String getEntDbName(String entId){
		return "ent_"+entId;
	}

	
	/*查询用户详情*/
	@Override
	public List<DBObject> queryUserDetail(DBObject queryObject, PageInfo pageInfo)
			throws DataAccessException {
		// TODO Auto-generated method stub
		Query query=new Query();
		if(queryObject.get("userId")!=null){
			query.addCriteria(Criteria.where("userId").is(queryObject.get("userId")));
		}       
		return this.findByPage(query, getUserTable(queryObject.get("entId")+""), DBObject.class, pageInfo);
	}
	
	
	
	/*添加用户*/
	@Override
	public int add(DBObject dbo, String userId, String entId) throws DataAccessException {
		// TODO Auto-generated method stub		
		
	    DatEntInfoPo ent=ParamUtils.getEntInfo(entId);
		boolean isExist=ent==null?false:true;

	    if(StringUtils.isBlank((String)dbo.get("ccodEntId"))&&isExist){
			dbo.put("ccodEntId", ent.getCcodEntId());
		}
		
		return this.add(dbo, entId);

	}

	@Override
	public List<DatEntUserPo> queryRelationUser(String entId, String userName) throws DataAccessException {
		// TODO Auto-generated method stub
		Query query=new Query();
		query.addCriteria(Criteria.where("userName").is(userName));
		
		return this.findList(query, getUserTable(entId), DatEntUserPo.class);
	}


	/*登录名是否已存在*/
	public long existsLoginName(DBObject dbo) throws DataAccessException{		
		Query query=new Query();
		if(dbo.get("loginName")!=null){
			query.addCriteria(Criteria.where("loginName").is(dbo.get("loginName")));
		}
		if(dbo.get("userId")!=null){
			query.addCriteria(Criteria.where("userId").ne(dbo.get("userId")));
		}
		query.addCriteria(Criteria.where("userStatus").ne("9"));
		
		return this.count(query, getUserTable(dbo.get("entId")+""));
	}

	@Override
	public DatEntUserPo getEntUserPoById(String entId, String userId) throws DataAccessException {
		Query query=new Query();
		if(StringUtils.isNotBlank(userId)){
			query.addCriteria(Criteria.where("userId").is(userId));
		}
		return this.findOne(query, getUserTable(entId), DatEntUserPo.class);
	}
	
	
	/*检测邮箱是否已经注册(除了自己的邮箱)*/
	public long existsEmailsNotMe(DBObject dbo) throws DataAccessException{
		Query query=new Query();
		if(dbo.get("email")!=null){
			query.addCriteria(Criteria.where("email").is(dbo.get("email")));
			
			query.addCriteria(Criteria.where("userStatus").ne("9"));
		}
        if(dbo.get("userId")!=null){
        	query.addCriteria(Criteria.where("userId").ne(dbo.get("userId")));
        }
		return this.count(query, getUserTable(dbo.get("entId")+""));
	}

	@Override
	public DatEntUserPo getUserByLName(String entId, String loginName) throws DataAccessException {
		Query query=new Query();
		query.addCriteria(Criteria.where("loginName").is(loginName));
		query.addCriteria(Criteria.where("userStatus").ne("9"));
		return this.findOne(query, getUserTable(entId), DatEntUserPo.class);
	}

	@Override
	public boolean hasActiveUser(String enterpriseid, String id) throws DataAccessException {
		String collection= getUserTable(enterpriseid);
		Query query=new Query();
		query.addCriteria(Criteria.where("roleId").is(id));
		query.addCriteria(Criteria.where("userStatus").ne("9"));
		long num=this.count(query, collection);
		if(num>0){
			return true;
		}
		return false;
	}

	@Override
	public List<DatEntUserPo> queryOrdinaryByEP(String entId, DatEntUserPo po) throws DataAccessException{
		String collection = getUserTable(entId);
		Query query = new Query();
		query.addCriteria(Criteria.where("userStatus").ne("9"));
		query.addCriteria(Criteria.where("userId").ne(po.getUserId()));
		query.addCriteria(Criteria.where("userType").is("1"));
		if(StringUtils.isNotBlank(po.getEmail())){
			Criteria or = new Criteria();
			query.addCriteria(or.orOperator(Criteria.where("telPhone").is(po.getTelPhone()),
					Criteria.where("email").is(po.getEmail())));
		}else{
			query.addCriteria(Criteria.where("telPhone").is(po.getTelPhone()));
		}
		return this.findList(query, collection, DatEntUserPo.class);
	}
	
	/*检测用户名是否已存在*/
	public long existsUserName(DBObject dbo) throws DataAccessException{
		Query query=new Query();
		if(dbo.get("userName")!=null){
			query.addCriteria(Criteria.where("userName").is(dbo.get("userName")));
		}
		if(dbo.get("userId")!=null){
			query.addCriteria(Criteria.where("userId").ne(dbo.get("userId")));
		}
		query.addCriteria(Criteria.where("userStatus").ne("9"));
		
		return this.count(query, getUserTable(dbo.get("entId")+""));
	}
}

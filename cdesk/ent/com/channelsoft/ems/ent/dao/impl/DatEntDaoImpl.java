package com.channelsoft.ems.ent.dao.impl;

import java.sql.Types;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;

import com.channelsoft.cri.dao.jdbc.BaseJdbcMysqlDao;
import com.channelsoft.ems.ent.dao.IDatEntDao;
import com.channelsoft.ems.ent.po.DatEntInfoPo;

public class DatEntDaoImpl extends BaseJdbcMysqlDao  implements IDatEntDao {

	private String ENT_TABLE_NAME="DAT_ENT_INFO";
	@Override
	public int addEntInfo(DatEntInfoPo po) throws DataAccessException {
		// TODO Auto-generated method stub
		//添加企业信息		 
		StringBuffer sql = new StringBuffer();
		sql.append("insert into DAT_ENT_INFO (ENT_ID,ENT_NAME,EMAIL,REGISTER,PASSWORD,DOMAIN_NAME,CREATOR_NAME,CREATE_TIME,STATUS,UPDATE_TIME,UPDATOR_NAME,CCOD_ENT_ID,CONTACT_WAY) values ");		
		sql.append("(?,?,?,?,?,?,?,NOW(),?,NOW(),?,?,?);");
		
		Object[] params = new Object[] {
				po.getEntId(),po.getEntName(),po.getEmail(),po.getRegister(),po.getPassword(),po.getDomainName(),po.getCreatorName(),po.getStatus(),po.getUpdatorName(),po.getCcodEntId(),po.getContactWay()
		};
		int[] types = new int[]{Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR
		};
		 
		return this.update(sql.toString(), params, types);
	}

	@Override
	public boolean existThisMail(String email) throws DataAccessException {
		// TODO Auto-generated method stub
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT COUNT(*) FROM ").append(ENT_TABLE_NAME).append(" WHERE EMAIL='").append(email).append("'");
		int num=this.queryForInt(sql.toString());
		return num>0?true:false;
	}

	@Override
	public boolean existThisEntId(String entId) throws DataAccessException {
		// TODO Auto-generated method stub
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT COUNT(*) FROM ").append(ENT_TABLE_NAME).append(" WHERE ENT_ID='").append(entId).append("'");
		int num=this.queryForInt(sql.toString());
		return num>0?true:false;
	}

	@Override
	public int createDataBase(String entId) throws DataAccessException {
		// TODO Auto-generated method stub
		String sql = "CREATE DATABASE " + getEntDbName(entId)+"";
		return this.update(sql);

	}

	@Override
	public int createTableName(String entId) throws DataAccessException {
		// TODO Auto-generated method stub
		 final String psql = "call add_ent_tables('" +getEntDbName(entId)+"')";   
         return this.update(psql);
	}

	@Override
	public List<DatEntInfoPo> query(DatEntInfoPo po) throws DataAccessException {
		StringBuffer sqlBuffer = new StringBuffer("SELECT * FROM DAT_ENT_INFO WHERE 1=1");
		if (StringUtils.isNotBlank(po.getEntId())) {
			sqlBuffer.append(" AND ENT_ID='").append(po.getEntId()).append("'");
		}
		if (StringUtils.isNotBlank(po.getCcodEntId())) {
			sqlBuffer.append(" AND CCOD_ENT_ID='").append(po.getCcodEntId()).append("'");
		}
		return this.queryForList(sqlBuffer.toString(), DatEntInfoPo.class);
	}

	@Override
	public int brandSetting(DatEntInfoPo po) throws DataAccessException {
		// TODO Auto-generated method stub
		StringBuffer sql=new StringBuffer();
		sql.append("UPDATE DAT_ENT_INFO SET UPDATE_TIME=now()");
		sql.append(",ENT_NAME='").append(po.getEntName()).append("'");
		sql.append(",ENT_DESC='").append(po.getEntDesc()).append("'");
		sql.append(",SEO='").append(po.getSeo()).append("'");
		/*sql.append(",LOGO_URL='").append(po.getLogoUrl()).append("'");
		sql.append(",FAVICON_URL='").append(po.getFaviconUrl()).append("'");*/
		sql.append(",UPDATOR_NAME='").append(po.getUpdatorName()).append("'");
		sql.append(",UPDATOR_ID='").append(po.getUpdatorId()).append("'");
		
		sql.append(" WHERE ENT_ID='").append(po.getEntId()).append("'");
		return this.update(sql.toString());
	}

	@Override
	public int changeimage(DatEntInfoPo po,String type) throws DataAccessException {
		// TODO Auto-generated method stub
		StringBuffer sql=new StringBuffer();
		sql.append("UPDATE DAT_ENT_INFO SET UPDATE_TIME=now()");
		if("logo".equals(type)){
			sql.append(",LOGO_URL='").append(po.getLogoUrl()).append("'");
		}
		if("favicon".equals(type)){
			sql.append(",FAVICON_URL='").append(po.getFaviconUrl()).append("'");
		}
		sql.append(",UPDATOR_NAME='").append(po.getUpdatorName()).append("'");
		sql.append(",UPDATOR_ID='").append(po.getUpdatorId()).append("'");
		
		sql.append(" WHERE ENT_ID='").append(po.getEntId()).append("'");
		return this.update(sql.toString());
	}

	@Override
	public int delimage(DatEntInfoPo po, String type) throws DataAccessException {
		// TODO Auto-generated method stub
		StringBuffer sql=new StringBuffer();
		sql.append("UPDATE DAT_ENT_INFO SET UPDATE_TIME=now()");
		if("logo".equals(type)){
			sql.append(",LOGO_URL=''");
		}
		if("favicon".equals(type)){
			sql.append(",FAVICON_URL=''");
		}
		sql.append(",UPDATOR_NAME='").append(po.getUpdatorName()).append("'");
		sql.append(",UPDATOR_ID='").append(po.getUpdatorId()).append("'");
		
		sql.append(" WHERE ENT_ID='").append(po.getEntId()).append("'");
		return this.update(sql.toString());
	}

	@Override
	public int deleteEntInfo(String entId) {
		StringBuffer sql=new StringBuffer();
		sql.append("DELETE FROM DAT_ENT_INFO WHERE ENT_ID='").append(entId).append("'");
		return this.update(sql.toString());
	}

	@Override
	public int deleteDB(String entId) throws DataAccessException {
		StringBuffer isExistsSql=new StringBuffer();
		isExistsSql.append("select count(*) from information_schema.schemata where schema_name='").append(getEntDbName(entId)).append("'");
		int num=this.queryForInt(isExistsSql.toString());
		if(num>0){
			StringBuffer sql=new StringBuffer();
			sql.append("DROP DATABASE ").append(getEntDbName(entId));
			return this.update(sql.toString());
		}
        return 1;
	}
	
	@Override
	public int update(DatEntInfoPo po) throws DataAccessException {
		StringBuffer sql=new StringBuffer();
		sql.append("UPDATE DAT_ENT_INFO SET ");
		if(po.getEntName() != null){
			sql.append("ENT_NAME='").append(po.getEntName()).append("', ");
		}
		if(po.getIndustry() != null){
			sql.append("INDUSTRY='").append(po.getIndustry()).append("', ");
		}
		if(po.getScale() != null){
			sql.append("SCALE='").append(po.getScale()).append("', ");
		}
		if(po.getContactUser() != null){
			sql.append("CONTACT_USER='").append(po.getContactUser()).append("', ");
		}
		if(po.getProvince() != null){
			sql.append("PROVINCE='").append(po.getProvince()).append("', ");
		}
		if(po.getCity() != null){
			sql.append("CITY='").append(po.getCity()).append("', ");
		}
		if(po.getArea() != null){
			sql.append("AREA='").append(po.getArea()).append("', ");
		}
		if(po.getAddress() != null){
			sql.append("ADDRESS='").append(po.getAddress()).append("', ");
		}
		if(po.getZipCode() != null){
			sql.append("ZIP_CODE='").append(po.getZipCode()).append("', ");
		}
		if(po.getUpdatorName() != null){
			sql.append("UPDATOR_NAME='").append(po.getUpdatorName()).append("', ");
		}
		if(po.getUpdatorId() != null){
			sql.append("UPDATOR_ID='").append(po.getUpdatorId()).append("', ");
		}
		sql.append(" UPDATE_TIME=now() WHERE ENT_ID='").append(po.getEntId()).append("'");
		return this.update(sql.toString());
	}

}

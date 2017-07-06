package com.channelsoft.cri.dao.jdbc;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.channelsoft.cri.dao.IBaseJdbcDao;
import com.channelsoft.cri.logger.api.DBLogApi;
import com.channelsoft.cri.logger.vo.LogCaller;
import com.channelsoft.cri.util.SqlParamCheckUtil;
import com.channelsoft.cri.vo.PageInfo;

/**
 * <dl>
 * <dt>SPMS</dt>
 * <dd>Description:基于JDBC的DAO基类,对于需要批量处理,或者涉及到大数据量的操作,请使用此基类。
 * 注意：必须注入参数jdbcTemplate</dd>
 * <dd>Copyright: Copyright (C) 2004</dd>
 * <dd>Company: 北京青牛软件技术有限责任公司</dd>
 * <dd>CreateDate: 2009-11-10</dd>
 * </dl>
 * 
 * @author 刘江宁
 */
public class BaseJdbcDao extends JdbcDaoSupport implements IBaseJdbcDao {

	public BaseJdbcDao() {
		super();
	}

	public int update(String sql) throws DataAccessException {
		try {
			int i = getJdbcTemplate().update(sql);
			DBLogApi.UpdateSuccess(sql, i, new LogCaller(-2));
			return i;
		} catch (DataAccessException e) {
			DBLogApi.UpdateFail(sql,  new LogCaller(-2));
			throw e;
		}
	}

	/**
	 * 执行更新SQL语句
	 * 
	 * @param sql
	 * @param args
	 * @param argTypes
	 * @return 返回成功更新的记录数
	 * @throws DataAccessException
	 */
	public int update(String sql, Object[] args, int[] argTypes) throws DataAccessException {
		try {
			int i = getJdbcTemplate().update(sql, args, argTypes);
			DBLogApi.UpdateSuccess(SqlParamCheckUtil.check(sql, args, argTypes), i, new LogCaller(-1));
			return i;
		} catch (DataAccessException e) {
			DBLogApi.UpdateFail(sql,  new LogCaller(-1));
			throw e;
		}
	}

	/**
	 * 执行查询SQL语句
	 * 
	 * @param sql
	 * @return 返回String值
	 * @throws DataAccessException
	 */
	public String queryForString(String sql) throws DataAccessException {
		try {
			String str = (String) getJdbcTemplate().queryForObject(sql, String.class);
			DBLogApi.QuerySuccess(sql, "queryForString", 1, new LogCaller(-1));
			return str;
		} catch (DataAccessException e) {
			DBLogApi.QueryFail(sql, "queryForString", new LogCaller(-1));
			throw e;
		}
	}
	
	/**
	 * 执行查询SQL语句
	 * 
	 * @param sql
	 * @return 返回String值
	 * @throws DataAccessException
	 */
	public String queryForString(String sql, Object[] args, int[] argTypes) throws DataAccessException {
		try {
			String str = (String) getJdbcTemplate().queryForObject(sql, args, argTypes, String.class);
			DBLogApi.QuerySuccess(sql, "queryForString", 1, new LogCaller(-1));
			return str;
		} catch (DataAccessException e) {
			DBLogApi.QueryFail(sql, "queryForString", new LogCaller(-1));
			throw e;
		}
	}

	/**
	 * 执行查询SQL语句
	 * 
	 * @param sql
	 * @return 返回int值
	 * @throws DataAccessException
	 */
	public int queryForInt(String sql) throws DataAccessException {
		try {
			int i = getJdbcTemplate().queryForInt(sql);
			DBLogApi.QuerySuccess(sql, "queryForInt", i, new LogCaller(-1));
			return i;
		} catch (DataAccessException e) {
			DBLogApi.QueryFail(sql, "queryForInt", new LogCaller(-1));
			throw e;
		}
	}
	
	/**
	 * 执行查询SQL语句
	 * 
	 * @param sql
	 * @return 返回int值
	 * @throws DataAccessException
	 */
	public int queryForInt(String sql, Object[] args, int[] argTypes) throws DataAccessException {
		try {
			int i = getJdbcTemplate().queryForInt(sql, args, argTypes);
			DBLogApi.QuerySuccess(sql, "queryForInt", i, new LogCaller(-1));
			return i;
		} catch (DataAccessException e) {
			DBLogApi.QueryFail(sql, "queryForInt", new LogCaller(-1));
			throw e;
		}
	}

	/**
	 * 执行查询SQL语句
	 * 
	 * @param sql
	 * @return 返回long值
	 * @throws DataAccessException
	 */
	public long queryForLong(String sql) throws DataAccessException {
		try {
			long i = getJdbcTemplate().queryForLong(sql);
			DBLogApi.QuerySuccess(sql, "queryForLong", i, new LogCaller(-1));
			return i;
		} catch (DataAccessException e) {
			DBLogApi.QueryFail(sql, "queryForLong", new LogCaller(-1));
			throw e;
		}
	}

	/**
	 * 执行查询SQL语句
	 * 
	 * @param sql
	 * @param objClass
	 * @return 返回Class的对象
	 * @throws DataAccessException
	 */
	public <T> T queryForObject(String sql, Class<T> objClass) throws DataAccessException {
		try {
			T r = null;
			try{
				r = getJdbcTemplate().queryForObject(sql,new BeanPropertyRowMapper<T>(objClass));
			}catch(EmptyResultDataAccessException e){
				
			}
			DBLogApi.QuerySuccess(sql, "queryForObject", 1, new LogCaller(-1));
			return r;
		} catch (DataAccessException e) {
			DBLogApi.QueryFail(sql, "queryForObject", new LogCaller(-1));
			throw e;
		}
	}

	/**
	 * 执行查询SQL语句,验证该表是否存在
	 * 
	 * @param tableName
	 * @return 返回boolean
	 * @throws DataAccessException
	 */
	public boolean tableExist(String tableName) throws DataAccessException {
		StringBuffer sql = new StringBuffer(200);
		sql.append("SELECT 1 FROM TAB WHERE UPPER(TNAME)=UPPER('");
		sql.append(tableName).append("')");
		List<?> list = getJdbcTemplate().queryForList(sql.toString());
		return (list != null && !list.isEmpty());
	}

	/**
	 * 执行查询SQL语句,验证是否存在满足whereClause条件的数据且是唯一
	 * 
	 * @param tableName
	 * @param whereClause
	 * @return 返回boolean
	 * @throws DataAccessException
	 */
	public boolean dataExist(String tableName, String whereClause) throws DataAccessException {
		if (StringUtils.isBlank(tableName)) {
			throw new IllegalArgumentException("数据判存失败：表名参数不合法！");
		}
		StringBuffer sql = new StringBuffer(200);
		sql.append("SELECT 1 FROM ").append(tableName).append(
				" WHERE ROWNUM = 1 ");
		if (StringUtils.isNotBlank(whereClause)) {
			sql.append(" AND (").append(whereClause).append(")");
		}
		return getJdbcTemplate().queryForList(sql.toString()).size() == 1;
	}

	/**
	 * 得到Sequence
	 * 
	 * @param seqName
	 * @return 返回long型
	 * @throws DataAccessException
	 */
	public long getSequence(String seqName) throws DataAccessException {
		StringBuffer sql = new StringBuffer(100);
		sql.append("SELECT ").append(seqName).append(".nextval FROM DUAL");
		return getJdbcTemplate().queryForInt(sql.toString());
	}

	/**
	 * 执行查询SQL语句
	 * 
	 * @param sql
	 * @return 返回结果列表
	 * @throws DataAccessException
	 * @author weiss
	 */
	public <T> List<T> queryForList(String sql, Object[] args, int[] argTypes,
			Class<T> poClass) throws DataAccessException {
		try {
			List<T> list = getJdbcTemplate().query(sql, args, argTypes,
					new BeanPropertyRowMapper<T>(poClass));
			DBLogApi.QuerySuccess(sql, "queryForList", list.size(), new LogCaller(-1));
			return list;
		} catch (DataAccessException e) {
			DBLogApi.QueryFail(sql, "queryForList", new LogCaller(-1));
			throw e;
		}
	}

	/**
	 * 执行查询SQL语句
	 * 
	 * @param sql
	 * @return 返回结果列表
	 * @throws DataAccessException
	 * 
	 * @author weiss
	 */
	public <T> List<T> queryForList(String sql, Class<T> poClass)
			throws DataAccessException {

		try {
			List<T> list = getJdbcTemplate().query(sql, new BeanPropertyRowMapper<T>(poClass));

			DBLogApi.QuerySuccess(sql, "queryForList", list.size(), new LogCaller(-1));
			return list;
		} catch (DataAccessException e) {
			DBLogApi.QueryFail(sql, "queryForList", new LogCaller(-1));
			throw e;
		}
	}

	/**
	 * 执行查询SQL语句
	 * 
	 * @param sql
	 * @return Object
	 * @throws DataAccessException
	 * @author weiss
	 */
	public <T> T queryForObject(String sql, Object[] args, int[] argTypes,
			Class<T> poClass) throws DataAccessException {

		try {
			T r = null;
			try{
				r = getJdbcTemplate().queryForObject(sql, args, argTypes, new BeanPropertyRowMapper<T>(poClass));
			}catch(EmptyResultDataAccessException e){
				
			}
			DBLogApi.QuerySuccess(sql, "queryForObject", 1, new LogCaller(-1));
			return r;
		} catch (DataAccessException e) {
			DBLogApi.QueryFail(sql, "queryForObject", new LogCaller(-1));
			throw e;
		}
	}

	public String prepareSql(String sql, PageInfo page) {
		if(page == null) return sql;
		
		StringBuffer sqlBuf = new StringBuffer(50 + sql.length());
		long PageTotalRecords = page.getTotalRecords();
		/*** 总记录数 */
		int PageStartIndex = page.getStartIndex();
		/** 显示记录开始页数 */
		int PageResults = page.getResults();
		/** 页面显示记录条数 */
		int end = PageStartIndex + PageResults;
		/** 显示到第几条记录条数 */
		sqlBuf
				.append("SELECT tt.* FROM")
				.append(" (SELECT ROWNUM rid,t.* FROM (")
				.append(sql)
				.append(") t) tt WHERE rid >")
				.append(
						(PageTotalRecords > 0 && PageStartIndex > PageTotalRecords) ? 0
								: PageStartIndex)
				.append(" AND rid <= ")
				.append(
						(PageTotalRecords > 0 && end > PageTotalRecords) ? PageTotalRecords
								: end);
		return sqlBuf.toString();
	}

	/**
	 * 执行分页查询SQL语句
	 * 
	 * @param sql
	 * @return 返回结果列表
	 * @throws DataAccessException
	 * @author weiss
	 */
	public <T> List<T> queryByPage(String sql, Object[] args, int[] argTypes,
			PageInfo page, Class<T> poClass) throws DataAccessException {
		return queryByPage(sql, args, argTypes, page, new BeanPropertyRowMapper<T>(poClass));
	}

	/**
	 * 执行分页查询SQL语句
	 * 
	 * @param sql
	 * @return 返回结果列表
	 * @throws DataAccessException
	 * @author weiss
	 */
	public <T> List<T> queryByPage(String sql, PageInfo page, Class<T> poClass)
			throws DataAccessException {
		return queryByPage(sql, page, new BeanPropertyRowMapper<T>(poClass));
	}

	/**
	 * 执行分页查询SQL语句
	 * 
	 * @param sql
	 * @return 返回结果列表
	 * @throws DataAccessException
	 * @author weiss
	 */
	public <T> List<T> queryByPage(String sql, Object[] args, int[] argTypes,
			PageInfo page, RowMapper<T> mapper) throws DataAccessException {
		try {
			if (page != null)
				page.setTotalRecords(getMaxRowCount(sql, args, argTypes));
			List<T> list = getJdbcTemplate().query(prepareSql(sql, page), args, argTypes, mapper);
			DBLogApi.QuerySuccess(sql, "queryByPage", list.size(), new LogCaller(-1));
			return list;
		} catch (DataAccessException e) {
			DBLogApi.QueryFail(sql, "queryByPage", new LogCaller(-1));
			throw e;
		}
	}

	/**
	 * 执行分页查询SQL语句
	 * 
	 * @param sql
	 * @return 返回结果列表
	 * @throws DataAccessException
	 * @author weiss
	 */
	public <T> List<T> queryByPage(String sql, PageInfo page, RowMapper<T> mapper)
			throws DataAccessException {
		try {
			if(page != null){
				page.setTotalRecords(getMaxRowCount(sql));			
			}
			List<T> list = getJdbcTemplate().query(prepareSql(sql, page), mapper);
			DBLogApi.QuerySuccess(sql, "queryByPage", list.size(), new LogCaller(-1));
			return list;
		} catch (DataAccessException e) {
			DBLogApi.QueryFail(sql, "queryByPage", new LogCaller(-1));
			throw e;
		}
	}

	/**
	 * 得到Sequence
	 * 
	 * @param seqName
	 * @return String
	 * @throws DataAccessException
	 */
	public String getSequenceStr(String seqName) throws DataAccessException {
		StringBuffer sql = new StringBuffer(100);
		sql.append("SELECT TO_CHAR(").append(seqName).append(
				".nextval) FROM DUAL");
		return (String) getJdbcTemplate().queryForObject(sql.toString(),
				String.class);
	}

	/**
	 * 执行sql语句 得到记录总数
	 * 
	 * @param sql
	 * @param args
	 * @param argTypes
	 * @return int
	 * @throws DataAccessException
	 */
	public int getMaxRowCount(String sql, Object[] args, int[] argTypes)
			throws DataAccessException {
		StringBuffer countSql = new StringBuffer(30 + sql.length());
		countSql.append(" SELECT COUNT(*) FROM (").append(sql).append(") Z");

		return getJdbcTemplate().queryForInt(countSql.toString(), args,
				argTypes);
	}

	/**
	 * 执行sql语句 得到记录总数
	 * 
	 * @param sql
	 * @return int
	 * @throws DataAccessException
	 */
	public int getMaxRowCount(String sql) throws DataAccessException {
		StringBuffer countSql = new StringBuffer(30 + sql.length());
		countSql.append(" SELECT COUNT(*) FROM (").append(sql).append(") Z");
		return getJdbcTemplate().queryForInt(countSql.toString());
	}
}

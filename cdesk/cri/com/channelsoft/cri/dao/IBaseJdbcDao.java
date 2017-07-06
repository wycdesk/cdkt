/**
 * 
 */
package com.channelsoft.cri.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.channelsoft.cri.vo.PageInfo;


/**
 * 基于JDBC的DAO的公共操作接口，对数据库表提供标准的增删改查功能
 * 
 * @author 刘江宁
 * 
 */
public interface IBaseJdbcDao {
	/**
	 * 
	 * @param sql  sql语句
	 * @param args 参数值
	 * @param argTypes 参数类型
	 * @return
	 * @throws DataAccessException
	 */
	public int update(String sql, Object[] args, int[] argTypes)throws DataAccessException;
	
	/**
	 * 执行查询SQL语句，返回String值
	 * 
	 * @param sql
	 * @return
	 * @throws DaoException
	 */
	public String queryForString(String sql) throws DataAccessException;
	/**
	 * 执行查询SQL语句，返回int值
	 * 
	 * @param sql
	 * @return
	 * @throws DataAccessException
	 */
	public int queryForInt(String sql) throws DataAccessException;
	/**
	 * 执行查询SQL语句，返回long值
	 * 
	 * @param sql
	 * @return
	 * @throws DataAccessException
	 * @date Jul 6, 2007 10:05:16 AM
	 */
	public long queryForLong(String sql) throws DataAccessException;

	/**
	 * 执行查询SQL语句
	 * 
	 * @param sql
	 * @param objectClass
	 * @return 返回Class的对象
	 * @throws DataAccessException
	 * @date Jul 6, 2007 9:27:38 AM
	 */
	public <T> Object queryForObject(String sql, Class<T> objectClass)
			throws DataAccessException;

	/**
	 * 校验表是否存在
	 * 
	 * @param tableName
	 * @return
	 * @throws DataAccessException
	 */
	public boolean tableExist(String tableName) throws DataAccessException;

	/**
	 * 校验数据是否存在。
	 * 
	 * @param tableName
	 *            需要校验的表名
	 * @param whereClause
	 *            查询条件子句，不需要包含“where”关键字
	 * @return
	 * @throws DataAccessException
	 * @date Aug 6, 2007 3:22:12 PM
	 */
	public boolean dataExist(String tableName, String whereClause)
			throws DataAccessException;

	/**
	 * 返回指定的sequence的下一个值
	 * 
	 * @param name
	 * @return
	 * @throws DataAccessException
	 */
	public long getSequence(String name) throws DataAccessException;

	/**
	 * 执行查询SQL语句
	 * 
	 * @param sql
	 * @return 返回结果列表
	 * @throws DataAccessException
	 */
	public <T> List<T> queryForList(String sql, Object[] args, int[] argTypes,
			Class<T> poClass) throws DataAccessException;

	/**
	 * 执行查询SQL语句
	 * 
	 * @param sql
	 * @return Object
	 * @throws DataAccessException
	 */
	public <T> T queryForObject(String sql, Object[] args, int[] argTypes,
			Class<T> poClass) throws DataAccessException;

	

	/**
	 * 执行分页查询SQL语句
	 * 
	 * @param sql
	 * @return 返回结果列表
	 * @throws DataAccessException
	 */
	public <T> List<T> queryByPage(String sql, Object[] args, int[] argTypes,
			PageInfo page, Class<T> poClass) throws DataAccessException;

	/**
	 * 执行分页查询SQL语句
	 * 
	 * @param sql
	 * @return 返回结果列表
	 * @throws DataAccessException
	 */
	public <T> List<T> queryByPage(String sql, PageInfo page, Class<T> poClass)
			throws DataAccessException;
	
	/**
	 * 获取Sequence
	 * @param seqName
	 * @return
	 * @throws DataAccessException
	 */
	public String getSequenceStr(String seqName) throws DataAccessException;
	 /**
     * 执行sql语句 得到记录总数
     * @param sql
     * @param args
     * @param argTypes
     * @return int
     * @throws DataAccessException
     */
	public int getMaxRowCount(String sql, Object[] args, int[] argTypes)
			throws DataAccessException;
	/**
     * 执行sql语句 得到记录总数
     * @param sql
     * @return int
     * @throws DataAccessException
     */
	public int getMaxRowCount(String sql) throws DataAccessException;
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
			throws DataAccessException;
}

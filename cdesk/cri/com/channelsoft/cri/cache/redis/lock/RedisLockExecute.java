package com.channelsoft.cri.cache.redis.lock;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.channelsoft.cri.cache.redis.JedisTemplate;
import com.channelsoft.cri.cache.redis.JedisTemplate.JedisAction;


import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

/**
 * redis3.0单实例下的并发锁DLM
 * (并发下，一条指令本身是否是原子操作，一条指令与下一条指令逻辑间是否是原子操作)
 * @author channelsoft-xf
 */
public class RedisLockExecute {
	private static final Logger LOGGER = LoggerFactory.getLogger(RedisLockExecute.class); 
	
	/** 
     * 创建一个redis锁 （如果需要循环获取锁，要有个前置条件即是否需要去获取锁。暂未考虑好，本次只实现获取一次锁。获取不到则退出）
     * @param redisKey         需要添加的锁对象 
     * @param expiredTime       设置锁的过期时间(单位:s) 
     * @return  RedisLock
     * 返回一个封装好后的一个锁对象 
     */  
    public static RedisLock acquireLock(final String redisKey, final Integer expiredTime) {  
        //随即获取一个uuid，作为一个key的标识区别与，同名lock  
        final String uuid = UUID.randomUUID().toString();  
  
        Boolean result = JedisTemplate.execute(new JedisAction<Boolean>() {
			@Override
			public Boolean action(Jedis jedis) {
                LOGGER.debug("try lock key={} ", redisKey);
                //下面两个操作不是原子操作，如果执行了第一个操作，服务器挂掉，就会导致死锁了
//                if (jedis.setnx(redisKey, uuid) == 1) {  
//                    jedis.expire(redisKey, expiredTime);
                String status = jedis.set(redisKey, uuid,"NX" , "EX", expiredTime);
                if(StringUtils.equalsIgnoreCase("ok",status)) {
                    LOGGER.debug("get lock, key={}, expire in seconds={}", redisKey, expiredTime);  
                    return true;  
                } else {  
                    String desc = jedis.get(redisKey);  
                    LOGGER.debug("key={} locked by another business={}", redisKey, desc);
                    return false; 
                }  
			}
        });
        RedisLock redisLock = new RedisLock(redisKey);  
        redisLock.setLockId(result ? uuid : null);  
        return redisLock;  
    }  
  
    /** 
     * 通过获得的锁编号判断锁添加是否成功 
     * @param redisLock    redis锁对象 
     * @return  Boolean 
     * 返回是否添加成功 
     */  
    public static Boolean ACQUIRE_RESULT(RedisLock redisLock){  
        return StringUtils.isNotBlank(redisLock.getLockId());  
    }  
  
    //释放锁  ：（1）正常释放锁（2）进程阻塞，导致锁有效时间过期，锁被其他进程获取（3）业务抛出异常
    //0,代表正常释放。1，代表事务执行失败（判断锁有效后，被其他进程改写）2，代表锁已经过期 ; 3,无效锁
    public static int releaseLock(final RedisLock redisLock) {  
    	return JedisTemplate.execute(new JedisAction<Integer>() {
			@Override
			public Integer action(Jedis jedis) {
				if(StringUtils.isBlank(redisLock.getLockId())) {
					LOGGER.debug("key={},lockId={},释放无效锁",redisLock.getLockName(),redisLock.getLockId());
					return 3;//没有获取到锁
				}
				//watch事务同步机制,判断在释放锁的时候，key没有被其他进程修改  
                jedis.watch(redisLock.getLockName());  
                //如果锁没有过期,则锁的值没有改变，
                //加入watch，防止在判断锁的值没有被改变后，锁刚好过期，被其他进程重写锁的值(判断过期和删除锁是两个操作，不是原子操作)
                if (StringUtils.equals(redisLock.getLockId(), jedis.get(redisLock.getLockName()))) {  
                	Transaction tx = jedis.multi();
                	//删除锁  
                    tx.del(redisLock.getLockName());  
                    List<Object> result = tx.exec();
                    if (result == null || result.isEmpty()) {
                        LOGGER.debug("Transaction事务没有执行，锁已被其他进程改写……");  
                        return 1;
                    }
                    else {
                    	LOGGER.debug("Transaction事务正常执行，key={}被正常释放……",redisLock.getLockName());
                    	return 0; 
                    }
                }  
                //锁已经过期了，释放watch操作 
                LOGGER.debug("key={},锁已经过期了，释放watch操作",redisLock.getLockName());
                jedis.unwatch();  
                return 2;  
			}
    	});
    }  
    
}

package com.channelsoft.cri.cache.redis.lock;

public class RedisLock {
	
	public RedisLock(String lockName){  
		this.lockName = lockName;  
	}

	private String lockId;    //每个进程独一的值，防止删除其他进程的锁
    private String lockName;  //redis中存储的key
     
    public String getLockId() {
		return lockId;
	}

	public void setLockId(String lockId) {
		this.lockId = lockId;
	}

	public String getLockName() {
		return lockName;
	}

	public void setLockName(String lockName) {
		this.lockName = lockName;
	}
}

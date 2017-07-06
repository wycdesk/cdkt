package com.channelsoft.ems.register.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * 线程池工具类
 * @author cuihc
 *
 */
public class ThreadPoolUtil implements InitializingBean,DisposableBean{
	
	private static final Logger logger = Logger.getLogger(ThreadPoolUtil.class);

	 // 线程池
    private final static int THREAD_SIZE = 10;
    private ExecutorService threadPool = null;
    
	@Override
	public void destroy() throws Exception {
		if (threadPool != null) {
			threadPool.shutdown();
			try {  
			     // Wait a while for existing tasks to terminate  
			     if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {  
			    	 threadPool.shutdownNow(); // Cancel currently executing tasks  
			       // Wait a while for tasks to respond to being cancelled  
			       if (!threadPool.awaitTermination(60, TimeUnit.SECONDS))  
			           logger.error("线程池没有正常停止............");  
			     }  
			   } catch (InterruptedException ie) {  
			     // (Re-)Cancel if current thread also interrupted  
				  threadPool.shutdownNow();  
			     // Preserve interrupt status  
			     Thread.currentThread().interrupt();  
			   }  
			logger.info("线程池正常停止....");
		}
		
	}
	@Override
	public void afterPropertiesSet() throws Exception {
		threadPool = Executors.newFixedThreadPool(THREAD_SIZE);
	}
	
	public void execute(Runnable task) {
		threadPool.execute(task);
	}
 	
 	
 	
}

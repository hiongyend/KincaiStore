package com.kincai.store.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import com.kincai.store.utils.LogTest;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 * 
 * @author kincai
 * 
 * @description 我们网络请求线程池:限制并行的网络请求线程。
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.thread
 *
 * @time 2015-7-15 下午9:47:36
 *
 */

public class CachedThreadPoolUtils {

	private static final String TAG = "CachedThreadPoolUtils";

	private CachedThreadPoolUtils() {

	}

	// 线程工厂,把传递进来的runnable对象生成一个Thread
	private static ThreadFactory threadFactory;

	
	// 线程池ThreadPoolExecutor java自带的线程池
	private static ExecutorService threadpool;

	public static void execute(Runnable runnable) {
		
		LogTest.LogMsg(TAG, "threadFactory 为空? "+(null == threadFactory));
		LogTest.LogMsg(TAG, "threadpool 为空? "+(null == threadpool));
		
		if(null == threadFactory) {
			threadFactory = new ThreadFactory() {

				// 原子型的integer变量生成的integer值不会重复
				private final AtomicInteger ineger = new AtomicInteger();

				@Override
				public Thread newThread(Runnable arg0) {
					return new Thread(arg0, "MyThreadPool thread:"
							+ ineger.getAndIncrement());
				}
			};
		}
		
		if(null == threadpool) {
			//可缓存的线程池  核心线程数0 最大线程数整形最大值2147483647 线程空闲时间为60L
			threadpool = Executors.newCachedThreadPool(threadFactory);
			
			//定长线程池  可控制线程最大并发数 10个 超出的线程会在队列中等待  线程空闲时间为0L 
//			threadpool = Executors.newFixedThreadPool(10, threadFactory);
			//单线程化的线程池，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序  线程空闲时间为0L
//			threadpool = Executors.newSingleThreadExecutor(threadFactory);
			//创建一个大小为整形最大值2147483647 线程池 可定核心线程数，支持定时及周期性任务执行
//			threadpool = Executors.newScheduledThreadPool(1, threadFactory);
		}
		
		
		threadpool.execute(runnable);
	}
	
	
}

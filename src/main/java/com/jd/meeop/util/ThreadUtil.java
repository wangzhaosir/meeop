/*
 * Copyright 2004-2016 JD.com Group.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jd.meeop.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.jd.meeop.exception.UtilException;

import java.util.concurrent.*;

/**
 * 线程池工具
 * @ClassName: ThreadUtil
 * @Description:
 * @author: 冷水(Toby)
 * @date: 16-12-7 14:45
 * @mail: zhao.wang@aliyun.com
 * @version: v1.0
 * @since: JDK 1.7.0_79(Liunx)
 */
public class ThreadUtil {
	private static ExecutorService executor = newExecutor();

	/**
	 * 静态类不可实例化
	 */
	private ThreadUtil(){}

	/**
	 * 直接在公共线程池中执行线程
	 * 
	 * @param runnable 可运行对象
	 */
	public static void execute(Runnable runnable) {
		try {
			executor.execute(runnable);
		} catch (Exception e) {
			throw new UtilException("Exception when running task!", e);
		}
	}

	/**
	 * 重启公共线程池
	 */
	public static void restart() {
		executor.shutdownNow();
		executor = newExecutor();
	}

	/**
	 * 新建一个线程池
	 * 
	 * @param threadSize 同时执行的线程数大小
	 * @return ExecutorService
	 */
	public static ExecutorService newExecutor(int threadSize) {
		return Executors.newFixedThreadPool(threadSize);
	}

	/**
	 * 获得一个新的线程池
	 * 
	 * @return ExecutorService
	 */
	private static ExecutorService newExecutor() {
		return new ThreadPoolExecutor(200, 1024, 60L,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(100),
                new ThreadFactoryBuilder().setNameFormat("biz-pool-%d").build(),
                new ThreadPoolExecutor.AbortPolicy());
	}

	/**
	 * 获得一个新的线程池，只有单个线程
	 * 
	 * @return ExecutorService
	 */
	public static ExecutorService newSingleExecutor() {
		return Executors.newSingleThreadExecutor();
	}

	/**
	 * 执行异步方法
	 * 
	 * @param runnable 需要执行的方法体
	 * @return 执行的方法体
	 */
	public static Runnable excAsync(final Runnable runnable, boolean isDeamon) {
		Thread thread = new Thread(){
			@Override
			public void run() {
				runnable.run();
			}
		};
		thread.setDaemon(isDeamon);
		thread.start();

		return runnable;
	}

	/**
	 * 执行有返回值的异步方法<br/>
	 * Future代表一个异步执行的操作，通过get()方法可以获得操作的结果，如果异步操作还没有完成，则，get()会使当前线程阻塞
	 * 
	 * @return Future
	 */
	public static <T> Future<T> execAsync(Callable<T> task) {
		return executor.submit(task);
	}

	/**
	 * 新建一个CompletionService，调用其submit方法可以异步执行多个任务，最后调用take方法按照完成的顺序获得其结果。，若未完成，则会阻塞
	 * 
	 * @return CompletionService
	 */
	public static <T> CompletionService<T> newCompletionService() {
		return new ExecutorCompletionService<T>(executor);
	}

	/**
	 * 新建一个CompletionService，调用其submit方法可以异步执行多个任务，最后调用take方法按照完成的顺序获得其结果。，若未完成，则会阻塞
	 * 
	 * @return CompletionService
	 */
	public static <T> CompletionService<T> newCompletionService(ExecutorService executor) {
		return new ExecutorCompletionService<T>(executor);
	}

	/**
	 * 新建一个CountDownLatch
	 * 
	 * @param threadCount 线程数量
	 * @return CountDownLatch
	 */
	public static CountDownLatch newCountDownLatch(int threadCount) {
		return new CountDownLatch(threadCount);
	}

	/**
	 * 挂起当前线程
	 * 
	 * @param millis 挂起的毫秒数
	 * @return 被中断返回false，否则true
	 */
	public static boolean sleep(Number millis) {
		if (millis == null) {
			return true;
		}

		try {
			Thread.sleep(millis.longValue());
		} catch (InterruptedException e) {
			return false;
		}
		return true;
	}
	
	/**
	 * @return 获得堆栈列表
	 */
	public static StackTraceElement[] getStackTrace() {
		return Thread.currentThread().getStackTrace();
	}

	/**
	 * 获得堆栈项
	 * 
	 * @param i 第几个堆栈项
	 * @return 堆栈项
	 */
	public static StackTraceElement getStackTraceElement(int i) {
		StackTraceElement[] stackTrace = getStackTrace();
		if (i < 0) {
			i += stackTrace.length;
		}
		return stackTrace[i];
	}
	
	/**
	 * 创建本地线程对象
	 * @return 本地线程
	 */
	public static <T> ThreadLocal<T> createThreadLocal(boolean isInheritable){
		if(isInheritable){
			return new InheritableThreadLocal<T>();
		}else{
			return new ThreadLocal<T>();
		}
	}
}

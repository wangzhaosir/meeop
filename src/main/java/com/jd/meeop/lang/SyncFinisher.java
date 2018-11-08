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
package com.jd.meeop.lang;

import com.jd.meeop.exception.NotInitedException;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

/**
 * 线程同步结束器
 * @ClassName: SyncFinisher
 * @Description:
 * 在完成一组正在其他线程中执行的操作之前，它允许一个或多个线程一直等待。<br>
 * 不能保证同时开始
 * @author: 冷水(Toby)
 * @date: 16-12-11 14:45
 * @mail: zhao.wang@aliyun.com
 * @version: v1.0
 * @since: JDK 1.7.0_79(Liunx)
 */
public class SyncFinisher{
	private CountDownLatch countDownLatch;
	
	private Set<Worker> workers = new LinkedHashSet<Worker>();
	
	/**
	 * 增加工作线程
	 * @param worker 工作线程
	 */
	synchronized public void addWorker(Worker worker) {
		workers.add(worker);
	}
	
	/**
	 * 开始工作
	 */
	public void start() {
		countDownLatch = new CountDownLatch(workers.size());
		for (Worker worker : workers) {
			worker.start();
		}
	}
	
	/**
	 * 等待所有Worker工作结束，否则阻塞
	 * @throws InterruptedException
	 */
	public void await() throws InterruptedException {
		if(countDownLatch == null) {
			throw new NotInitedException("Please call start() method first!");
		}
		
		countDownLatch.await();
	}
	
	/**
	 * 清空工作线程对象
	 */
	public void clearWorker() {
		workers.clear();
	}
	
	/**
	 * @return 并发数
	 */
	public long count() {
		return countDownLatch.getCount();
	}
	
	/**
	 * 工作者，为一个线程
	 *
	 */
	public abstract class Worker extends Thread {

		@Override
		public void run() {
			try {
				work();
			} finally {
				countDownLatch.countDown();
			}
		}
		
		public abstract void work();
	}
}

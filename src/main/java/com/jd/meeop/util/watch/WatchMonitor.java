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
package com.jd.meeop.util.watch;

import com.jd.meeop.exception.WatchException;
import com.jd.meeop.util.IoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.WatchEvent.Kind;

/**
 * 路径监听器
 * @ClassName: WatchMonitor
 * @Description:
 * @author: 冷水(Toby)
 * @date: 16-12-11 14:45
 * @mail: zhao.wang@aliyun.com
 * @version: v1.0
 * @since: JDK 1.7.0_79(Liunx)
 */
public class WatchMonitor {
	private static final Logger logger = LoggerFactory.getLogger(WatchMonitor.class);
	
	/** 监听路径 */
	private Path path;
	/** 监听服务 */
	private WatchService watchService;
	
	//------------------------------------------------------ Static method start
	/**
	 * 创建并初始化监听
	 * @param path 路径
	 * @return 监听对象
	 */
	public static WatchMonitor create(Path path){
		return new WatchMonitor(path);
	}
	
	/**
	 * 创建并初始化监听
	 * @param file 文件
	 * @return 监听对象
	 */
	public static WatchMonitor create(File file){
		return new WatchMonitor(file);
	}
	//------------------------------------------------------ Static method end
	
	//------------------------------------------------------ Constructor method start
	/**
	 * 创建并初始化监听
	 * @param path 路径
	 * @return 监听对象
	 */
	public static WatchMonitor create(String path){
		return new WatchMonitor(path);
	}
	
	/**
	 * 构造
	 * @param path Path
	 */
	public WatchMonitor(Path path) {
		this.path = path;
		this.init();
	}
	/**
	 * 构造
	 * @param file 文件
	 */
	public WatchMonitor(File file) {
		this(file.toPath());
	}
	/**
	 * 构造
	 * @param path 字符串路径
	 */
	public WatchMonitor(String path) {
		this(Paths.get(path));
	}
	//------------------------------------------------------ Constructor method end
	
	/**
	 * 初始化
	 * @throws IOException
	 */
	public void init(){
		try {
			watchService = FileSystems.getDefault().newWatchService();
			path.register(watchService, 
					StandardWatchEventKinds.OVERFLOW,
					StandardWatchEventKinds.ENTRY_MODIFY, 
					StandardWatchEventKinds.ENTRY_CREATE, 
					StandardWatchEventKinds.ENTRY_DELETE
					);
		} catch (Exception e) {
			throw new WatchException(e);
		}
	}
	
	/**
	 * 开始监听事件
	 * @param listener 监听
	 * @throws InterruptedException
	 */
	public void start(WatchListener listener){
		logger.debug("Start watching path: [{}]", this.path);
		while (true) {
			WatchKey wk;
			try {
				wk = watchService.take();
			} catch (InterruptedException e) {
				logger.warn("warn", e);
				return;
			}
			
			Kind<?> kind;
			for (WatchEvent<?> event : wk.pollEvents()) {
				kind = event.kind();
				if(kind == StandardWatchEventKinds.ENTRY_CREATE){
					listener.onCreate(event);
				}else if(kind == StandardWatchEventKinds.ENTRY_MODIFY){
					listener.onModify(event);
				}else if(kind == StandardWatchEventKinds.ENTRY_DELETE){
					listener.onDelete(event);
				}else if(kind == StandardWatchEventKinds.OVERFLOW){
					listener.onOverflow(event);
				}
			}
			wk.reset();
		}
	}
	
	/**
	 * 关闭监听
	 */
	public void close(){
		IoUtil.close(watchService);
	}
}

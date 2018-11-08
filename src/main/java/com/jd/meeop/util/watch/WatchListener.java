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

import java.nio.file.WatchEvent;

/**
 * 监测
 * @ClassName: WatchListener
 * @Description:
 * @author: 冷水(Toby)
 * @date: 16-12-11 14:45
 * @mail: zhao.wang@aliyun.com
 * @version: v1.0
 * @since: JDK 1.7.0_79(Liunx)
 */
public interface WatchListener {
	/**
	 * 文件创建时执行的方法
	 * @param event 事件
	 */
    void onCreate(WatchEvent<?> event);
	
	/**
	 * 文件修改时执行的方法<br>
	 * 文件修改可能触发多次
	 * @param event 事件
	 */
    void onModify(WatchEvent<?> event);
	
	/**
	 * 文件删除时执行的方法
	 * @param event 事件
	 */
    void onDelete(WatchEvent<?> event);
	
	/**
	 * 事件丢失或出错时执行的方法
	 * @param event 事件
	 */
    void onOverflow(WatchEvent<?> event);
}

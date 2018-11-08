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
package com.jd.meeop.util.http;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Cookie池。此池针对所有HTTP请求可用
 * @ClassName: CookiePool
 * @Description:
 * 此Cookie池用于模拟浏览器的Cookie，当访问后站点，记录Cookie，下次再访问这个站点时，一并提交Cookie到站点。
 * @author: 冷水(Toby)
 * @date: 16-12-11 14:45
 * @mail: zhao.wang@aliyun.com
 * @version: v1.0
 * @since: JDK 1.7.0_79(Liunx)
 */
public class CookiePool {
	
	//key: host, value: cookies字符串
	private static Map<String, String> cookies = new ConcurrentHashMap<String, String>();
	
	/**
	 * 获得某个网站的Cookie信息
	 * @param host 网站Host
	 * @return Cookie字符串
	 */
	public static String get(String host) {
		return cookies.get(host);
	}
	
	/**
	 * 将某个网站的Cookie放入Cookie池
	 * @param host 网站Host
	 * @param cookie Cookie字符串
	 */
	public static void put(String host, String cookie) {
		cookies.put(host, cookie);
	}
}

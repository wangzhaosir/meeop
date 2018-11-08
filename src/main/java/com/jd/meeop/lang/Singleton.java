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

import com.jd.meeop.util.ClassUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 单例类
 * @ClassName: Singleton
 * @Description:
 * 提供单例对象的统一管理，当调用get方法时，如果对象池中存在此对象，返回此对象，否则创建新对象返回
 * @author: 冷水(Toby)
 * @date: 16-12-11 14:45
 * @mail: zhao.wang@aliyun.com
 * @version: v1.0
 * @since: JDK 1.7.0_79(Liunx)
 */
public final class Singleton {
	private static Map<Class<?>, Object> pool = new ConcurrentHashMap<Class<?>, Object>();
	
	private Singleton() {
		//类对象
	}
	
	/**
	 * 获得指定类的单例对象<br>
	 * 对象存在于池中返回，否则创建，每次调用此方法获得的对象为同一个对象<br>
	 * @param clazz 类
	 * @return 单例对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> T get(Class<T> clazz, Object... params) {
		T obj = (T) pool.get(clazz);
		
		if(null == obj) {
			synchronized(Singleton.class) {
				obj = (T) pool.get(clazz);
				if(null == obj) {
					obj = ClassUtil.newInstance(clazz, params);
					pool.put(clazz, obj);
				}
			}
		}
		
		return obj;
	}
	
	/**
	 * 获得指定类的单例对象<br>
	 * 对象存在于池中返回，否则创建，每次调用此方法获得的对象为同一个对象<br>
	 * @param className 类名
	 * @param params 构造参数
	 * @return 单例对象
	 */
	public static <T> T get(String className, Object... params) {
		final Class<T> clazz = ClassUtil.loadClass(className);
		return get(clazz, params);
	}
	
	/**
	 * 移除指定Singleton对象
	 * @param clazz 类
	 */
	public static void remove(Class<?> clazz) {
		pool.remove(clazz);
	}
	
	/**
	 * 清除所有Singleton对象
	 */
	public static void destroy() {
		pool.clear();
	}
}

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

import java.io.Serializable;

/**
 * 为不可变的对象引用提供一个可变的包装
 * @ClassName: Holder
 * @Description:
 * @author: 冷水(Toby)
 * @date: 16-12-11 14:45
 * @mail: zhao.wang@aliyun.com
 * @version: v1.0
 * @since: JDK 1.7.0_79(Liunx)
 */
public final class Holder<T> implements Serializable{
	private static final long serialVersionUID = 861411261825135385L;
	
	public T value;
	
	/**
	 * 新建Holder类，持有指定值，当值为空时抛出空指针异常
	 * @param value 值，不能为空
	 * @return Holder
	 */
	public static <T> Holder<T> of(T value) throws NullPointerException{
		if(null == value){
			throw new NullPointerException("Holder can not hold a null value!");
		}
		return new Holder<T>(value);
	}
	
	//--------------------------------------------------------------------------- Constructor start
	public Holder() {
	}
	
	public Holder(T value) {
		this.value = value;
	}
	//--------------------------------------------------------------------------- Constructor end
}

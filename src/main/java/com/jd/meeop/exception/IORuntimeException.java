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
package com.jd.meeop.exception;

import com.jd.meeop.util.StringUtil;

/**
 * IO运行时异常异常
 * @ClassName: BeanUtil
 * @Description:
 * @author: 冷水(Toby)
 * @date: 16-12-11 14:45
 * @mail: zhao.wang@aliyun.com
 * @version: v1.0
 * @since: JDK 1.7.0_79(Liunx)
 */
public class IORuntimeException extends RuntimeException{
	private static final long serialVersionUID = 8247610319171014183L;

	public IORuntimeException(Throwable e) {
		super(e);
	}
	
	public IORuntimeException(String message) {
		super(message);
	}
	
	public IORuntimeException(String messageTemplate, Object... params) {
		super(StringUtil.format(messageTemplate, params));
	}
	
	public IORuntimeException(String message, Throwable throwable) {
		super(message, throwable);
	}
	
	public IORuntimeException(Throwable throwable, String messageTemplate, Object... params) {
		super(StringUtil.format(messageTemplate, params), throwable);
	}
}

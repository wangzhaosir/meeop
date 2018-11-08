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
 * 带有状态码的异常
 * @ClassName: BeanUtil
 * @Description:
 * @author: 冷水(Toby)
 * @date: 16-12-11 14:45
 * @mail: zhao.wang@aliyun.com
 * @version: v1.0
 * @since: JDK 1.7.0_79(Liunx)
 */
public class StatefulException extends Exception{
	private static final long serialVersionUID = 6057602589533840889L;
	
	//异常状态码
	private int status;

	public StatefulException() {
	}
	
	public StatefulException(String msg) {
		super(msg);
	}
	
	public StatefulException(String messageTemplate, Object... params) {
		super(StringUtil.format(messageTemplate, params));
	}
	
	public StatefulException(Throwable throwable) {
		super(throwable);
	}
	
	public StatefulException(String msg, Throwable throwable) {
		super(msg, throwable);
	}
	
	public StatefulException(int status, String msg) {
		super(msg);
		this.status = status;
	}
	
	public StatefulException(int status, Throwable throwable) {
		super(throwable);
		this.status = status;
	}
	
	public StatefulException(int status, String msg, Throwable throwable) {
		super(msg, throwable);
		this.status = status;
	}
	
	/**
	 * @return 获得异常状态码
	 */
	public int getStatus() {
		return status;
	}
}

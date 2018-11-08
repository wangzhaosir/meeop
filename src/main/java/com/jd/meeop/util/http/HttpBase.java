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

import com.jd.meeop.util.StringUtil;
import com.jd.meeop.util.CharsetUtil;
import com.jd.meeop.util.CollectionUtil;

import java.nio.charset.Charset;
import java.util.*;
import java.util.Map.Entry;

/**
 * http基类
 * @ClassName: HttpBase
 * @Description:
 * @author: 冷水(Toby)
 * @date: 16-12-11 14:45
 * @mail: zhao.wang@aliyun.com
 * @version: v1.0
 * @since: JDK 1.7.0_79(Liunx)
 */
public abstract class HttpBase<T> {

	/**HTTP/1.0*/
	public static final String HTTP_1_0 = "HTTP/1.0";
	/**HTTP/1.1*/
	public static final String HTTP_1_1 = "HTTP/1.1";
	
	/**存储头信息*/
	protected Map<String, List<String>> headers = new HashMap<String, List<String>>();
	/**编码*/
	protected String charset = CharsetUtil.UTF_8;
	/**http版本*/
	protected String httpVersion = HTTP_1_1;
	/**存储主体*/
	protected String body;
	
	// ---------------------------------------------------------------- Headers start
	/**
	 * 根据name获取头信息
	 * @param name Header名
	 * @return Header值
	 */
	public String header(String name) {
		if(StringUtil.isBlank(name)) {
			return null;
		}
		
		List<String> values = headers.get(name.trim());
		if(CollectionUtil.isEmpty(values)) {
			return null;
		}
		return values.get(0);
	}
	
	/**
	 * 根据name获取头信息
	 * @param name Header名
	 * @return Header值
	 */
	public String header(Header name) {
		return header(name.toString());
	}
	
	/**
	 * 移除一个头信息
	 * @param name Header名
	 */
	public void removeHeader(String name) {
		if(name != null) {
			headers.remove(name.trim());
		}
	}
	
	/**
	 * 移除一个头信息
	 * @param name Header名
	 */
	public void removeHeader(Header name) {
		removeHeader(name.toString());
	}

	/**
	 * 设置一个header<br>
	 * 如果覆盖模式，则替换之前的值，否则加入到值列表中
	 * @param name Header名
	 * @param value Header值
	 * @param isOverride 是否覆盖已有值
	 * @return T 本身
	 */
	public T header(String name, String value, boolean isOverride) {
		if(null != name && null != value){
			final List<String> values = headers.get(name.trim());
			if(isOverride || CollectionUtil.isEmpty(values)) {
				final ArrayList<String> valueList = new ArrayList<String>();
				valueList.add(value);
				headers.put(name.trim(), valueList);
			}else {
				values.add(value.trim());
			}
		}
		return (T) this;
	}
	
	/**
	 * 设置一个header<br>
	 * 如果覆盖模式，则替换之前的值，否则加入到值列表中
	 * @param name Header名
	 * @param value Header值
	 * @param isOverride 是否覆盖已有值
	 * @return T 本身
	 */
	public T header(Header name, String value, boolean isOverride) {
		return header(name.toString(), value, isOverride);
	}
	
	/**
	 * 设置一个header<br>
	 * 覆盖模式，则替换之前的值
	 * @param name Header名
	 * @param value Header值
	 * @return T 本身
	 */
	public T header(Header name, String value) {
		return header(name.toString(), value, true);
	}
	
	/**
	 * 设置一个header<br>
	 * 覆盖模式，则替换之前的值
	 * @param name Header名
	 * @param value Header值
	 * @return T 本身
	 */
	public T header(String name, String value) {
		return header(name, value, true);
	}
	
	/**
	 * 设置请求头<br>
	 * 不覆盖原有请求头
	 * 
	 * @param headers 请求头
	 */
	public void header(Map<String, List<String>> headers) {
		if(CollectionUtil.isEmpty(headers)) {
			return;
		}
		
		String name;
		for (Entry<String, List<String>> entry : headers.entrySet()) {
			name = entry.getKey();
			for (String value : entry.getValue()) {
				this.header(name, StringUtil.nullToEmpty(value), false);
			}
		}
	}

	/**
	 * 获取headers
	 * @return Map<String, List<String>>
	 */
	public Map<String, List<String>> headers() {
		return Collections.unmodifiableMap(headers);
	}
	// ---------------------------------------------------------------- Headers end
	
	/**
	 * 返回http版本
	 * @return String
	 */
	public String httpVersion() {
		return httpVersion;
	}
	/**
	 * 设置http版本
	 * @param httpVersion
	 * @return T
	 */
	public T httpVersion(String httpVersion) {
		this.httpVersion = httpVersion;
		return (T) this;
	}

	/**
	 * 返回字符集
	 * @return 字符集
	 */
	public String charset() {
		return charset;
	}
	
	/**
	 * 设置字符集
	 * @param charset 字符集
	 * @return T 自己
	 */
	public T charset(String charset) {
		if(StringUtil.isNotBlank(charset)){
			this.charset = charset;
		}
		return (T) this;
	}
	
	/**
	 * 设置字符集
	 * @param charset 字符集
	 * @return T 自己
	 */
	public T charset(Charset charset) {
		if(null != charset){
			this.charset = charset.name();
		}
		return (T) this;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = StringUtil.builder();
		sb.append("Request Headers: ").append(StringUtil.CRLF);
		for (Entry<String, List<String>> entry : this.headers.entrySet()) {
			sb.append("    ").append(entry).append(StringUtil.CRLF);
		}
		
		sb.append("Request Body: ").append(StringUtil.CRLF);
		sb.append("    ").append(this.body).append(StringUtil.CRLF);
		
		return sb.toString();
	}
}

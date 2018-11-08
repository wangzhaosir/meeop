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

import com.jd.meeop.exception.UtilException;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.*;

/**
 * 统一资源定位符相关工具类
 * @ClassName: URLUtil
 * @Description:
 * @author: 冷水(Toby)
 * @date: 16-12-7 14:45
 * @mail: zhao.wang@aliyun.com
 * @version: v1.0
 * @since: JDK 1.7.0_79(Liunx)
 */
public class URLUtil {

	/**
	 * 静态类不可实例化
	 */
	private URLUtil(){}
	
	/**
	 * 创建URL对象
	 * @param url URL
	 * @return URL对象
	 */
	public static URL url(String url){
		try {
			return new URL(url);
		} catch (MalformedURLException e) {
			throw new UtilException(e.getMessage(), e);
		}
	}

	/**
	 * 获得URL
	 * 
	 * @param pathBaseClassLoader 相对路径（相对于classes）
	 * @return URL
	 */
	public static URL getURL(String pathBaseClassLoader) {
		return ClassUtil.getClassLoader().getResource(pathBaseClassLoader);
	}

	/**
	 * 获得URL
	 * 
	 * @param path 相对给定 class所在的路径
	 * @param clazz 指定class
	 * @return URL
	 */
	public static URL getURL(String path, Class<?> clazz) {
		return clazz.getResource(path);
	}

	/**
	 * 获得URL，常用于使用绝对路径时的情况
	 * 
	 * @param file URL对应的文件对象
	 * @return URL
	 * @exception UtilException MalformedURLException
	 */
	public static URL getURL(File file) {
		try {
			return file.toURI().toURL();
		} catch (MalformedURLException e) {
			throw new UtilException("Error occured when get URL!", e);
		}
	}
	
	/**
	 * 获得URL，常用于使用绝对路径时的情况
	 * 
	 * @param files URL对应的文件对象
	 * @return URL
	 * @exception UtilException MalformedURLException
	 */
	public static URL[] getURLs(File... files) {
		final URL[] urls = new URL[files.length];
		try {
			for(int i = 0; i < files.length; i++){
				urls[i] = files[i].toURI().toURL();
			}
		} catch (MalformedURLException e) {
			throw new UtilException("Error occured when get URL!", e);
		}
		
		return urls;
	}
	
	/**
	 * 格式化URL链接
	 * 
	 * @param url 需要格式化的URL
	 * @return 格式化后的URL，如果提供了null或者空串，返回null
	 */
	public static String formatUrl(String url) {
		if (StringUtil.isBlank(url)){
			return null;
		}
		if (url.startsWith("http://") || url.startsWith("https://")){
			return url;
		}
		return "http://" + url;
	}

	/**
	 * 补全相对路径
	 * 
	 * @param baseUrl 基准URL
	 * @param relativePath 相对URL
	 * @return 相对路径
	 * @exception UtilException MalformedURLException
	 */
	public static String complateUrl(String baseUrl, String relativePath) {
		baseUrl = formatUrl(baseUrl);
		if (StringUtil.isBlank(baseUrl)) {
			return null;
		}

		try {
			final URL absoluteUrl = new URL(baseUrl);
			final URL parseUrl = new URL(absoluteUrl, relativePath);
			return parseUrl.toString();
		} catch (MalformedURLException e) {
			throw new UtilException(e);
		}
	}
	
	/**
	 * 编码URL
	 * @param url URL
	 * @param charset 编码
	 * @return 编码后的URL
	 * @exception UtilException UnsupportedEncodingException
	 */
	public static String encode(String url, String charset) {
		try {
			return URLEncoder.encode(url, charset);
		} catch (UnsupportedEncodingException e) {
			throw new UtilException(e);
		}
	}
	
	/**
	 * 解码URL
	 * @param url URL
	 * @param charset 编码
	 * @return 解码后的URL
	 * @exception UtilException UnsupportedEncodingException
	 */
	public static String decode(String url, String charset) {
		try {
			return URLDecoder.decode(url, charset);
		} catch (UnsupportedEncodingException e) {
			throw new UtilException(e);
		}
	}
	
	/**
	 * 获得path部分<br>
	 * URI -> http://www.aaa.bbb/search?scope=ccc&q=ddd
	 * PATH -> /search
	 * 
	 * @param uriStr URI路径
	 * @return path
	 * @exception UtilException URISyntaxException
	 */
	public static String getPath(String uriStr){
		URI uri = null;
		try {
			uri = new URI(uriStr);
		} catch (URISyntaxException e) {
			throw new UtilException(e);
		}
		
		return uri == null ? null : uri.getPath();
	}
}

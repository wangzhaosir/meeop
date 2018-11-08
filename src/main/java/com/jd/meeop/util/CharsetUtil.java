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

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 字符集工具类
 * @ClassName: CharsetUtil
 * @Description:
 * @author: 冷水(Toby)
 * @date: 16-12-11 14:45
 * @mail: zhao.wang@aliyun.com
 * @version: v1.0
 * @since: JDK 1.7.0_79(Liunx)
 */
public class CharsetUtil {
	
	/** ISO-8859-1 */
	public static final String ISO_8859_1 = "ISO-8859-1";
	/** UTF-8 */
	public static final String UTF_8 = "UTF-8";
	/** GBK */
	public static final String GBK = "GBK";
	
	/** ISO-8859-1 */
	public static final Charset CHARSET_ISO_8859_1 = Charset.forName(ISO_8859_1);
	/** UTF-8 */
	public static final Charset CHARSET_UTF_8 = Charset.forName(UTF_8);
	/** GBK */
	public static final Charset CHARSET_GBK = Charset.forName(GBK);

	/**
	 * 静态类不可实例化
	 */
	private CharsetUtil() {
	}
	
	/**
	 * 转换为Charset对象
	 * @param charset 字符集，为空则返回默认字符集
	 * @return Charset
	 */
	public static Charset charset(String charset){
		return StringUtil.isBlank(charset) ? Charset.defaultCharset() : Charset.forName(charset);
	}
	
	/**
	 * 转换字符串的字符集编码
	 * @param source 字符串
	 * @param srcCharset 源字符集，默认ISO-8859-1
	 * @param destCharset 目标字符集，默认UTF-8
	 * @return 转换后的字符集
	 */
	public static String convert(String source, String srcCharset, String destCharset) {
		return convert(source, Charset.forName(srcCharset), Charset.forName(destCharset));
	}
	
	/**
	 * 转换字符串的字符集编码
	 * @param source 字符串
	 * @param srcCharset 源字符集，默认ISO-8859-1
	 * @param destCharset 目标字符集，默认UTF-8
	 * @return 转换后的字符集
	 */
	public static String convert(String source, Charset srcCharset, Charset destCharset) {
		if(null == srcCharset) {
			srcCharset = StandardCharsets.ISO_8859_1;
		}
		
		if(null == destCharset) {
			srcCharset = StandardCharsets.UTF_8;
		}
		
		if (StringUtil.isBlank(source) || srcCharset.equals(destCharset)) {
			return source;
		}
		return new String(source.getBytes(srcCharset), destCharset);
	}
	
	/**
	 * 系统字符集编码，与 {@link CharsetUtil#defaultCharsetName()}功能相同，别名不同
	 * 
	 * @see CharsetUtil#defaultCharsetName()
	 * @return 系统字符集编码
	 */
	public static String systemCharset() {
		return defaultCharsetName();
	}
	
	/**
	 * 系统默认字符集编码
	 * 
	 * @see CharsetUtil#defaultCharsetName()
	 * @return 系统字符集编码
	 */
	public static String defaultCharsetName() {
		return Charset.defaultCharset().name();
	}
	
	/**
	 * 系统默认字符集编码
	 * 
	 * @see CharsetUtil#defaultCharsetName()
	 * @return 系统字符集编码
	 */
	public static Charset defaultCharset() {
		return Charset.defaultCharset();
	}
}

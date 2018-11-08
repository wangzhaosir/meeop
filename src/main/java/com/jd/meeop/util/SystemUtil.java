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

import com.jd.meeop.lang.Singleton;
import com.jd.meeop.util.system.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.util.Properties;

/**
 * System类封装工具类
 * @ClassName: SystemUtil
 * @Description:
 * @author: 冷水(Toby)
 * @date: 16-12-7 14:45
 * @mail: zhao.wang@aliyun.com
 * @version: v1.0
 * @since: JDK 1.7.0_79(Liunx)
 */
public class SystemUtil {
	private static Logger logger = LoggerFactory.getLogger(SystemUtil.class);
	/***** Java运行时环境信息 *****/
	// Java 运行时环境规范名称
	public final static String SPECIFICATION_NAME = "java.specification.name";
	// Java 运行时环境版本
	public final static String VERSION = "java.version";
	// Java 运行时环境规范版本
	public final static String SPECIFICATION_VERSION = "java.specification.version";
	// Java 运行时环境供应商
	public final static String VENDOR = "java.vendor";
	// Java 运行时环境规范供应商
	public final static String SPECIFICATION_VENDOR = "java.specification.vendor";
	// Java 供应商的 URL
	public final static String VENDOR_URL = "java.vendor.url";
	// Java 安装目录
	public final static String HOME = "java.home";
	// 加载库时搜索的路径列表
	public final static String LIBRARY_PATH = "java.library.path";
	// 默认的临时文件路径
	public final static String TMPDIR = "java.io.tmpdir";
	// 要使用的 JIT 编译器的名称
	public final static String COMPILER = "java.compiler";
	// 一个或多个扩展目录的路径
	public final static String EXT_DIRS = "java.ext.dirs";

	/***** Java虚拟机信息 *****/
	// Java 虚拟机实现名称
	public final static String VM_NAME = "java.vm.name";
	// Java 虚拟机规范名称
	public final static String VM_SPECIFICATION_NAME = "java.vm.specification.name";
	// Java 虚拟机实现版本
	public final static String VM_VERSION = "java.vm.version";
	// Java 虚拟机规范版本
	public final static String VM_SPECIFICATION_VERSION = "java.vm.specification.version";
	// Java 虚拟机实现供应商
	public final static String VM_VENDEOR = "java.vm.vendor";
	// Java 虚拟机规范供应商
	public final static String VM_SPECIFICATION_VENDOR = "java.vm.specification.vendor";

	/***** Java类信息 *****/
	// Java 类格式版本号
	public final static String CLASS_VERSION = "java.class.version";
	// Java 类路径
	public final static String CLASS_PATH = "java.class.path";

	/***** OS信息 *****/
	// 操作系统的名称
	public final static String OS_NAME = "os.name";
	// 操作系统的架构
	public final static String OS_ARCH = "os.arch";
	// 操作系统的版本
	public final static String OS_VERSION = "os.version";
	// 文件分隔符（在 UNIX 系统中是“/”）
	public final static String FILE_SEPRATOR = "file.separator";
	// 路径分隔符（在 UNIX 系统中是“:”）
	public final static String PATH_SEPRATOR = "path.separator";
	// 行分隔符（在 UNIX 系统中是“\n”）
	public final static String LINE_SEPRATOR = "line.separator";

	/***** 用户信息 *****/
	// 用户的账户名称
	public final static String USER_NAME = "user.name";
	// 用户的主目录
	public final static String USER_HOME = "user.home";
	// 用户的当前工作目录
	public final static String USER_DIR = "user.dir";

	/**
	 * 静态类不可实例化
	 */
	private SystemUtil(){}

	/**
	 * 取得系统属性，如果因为Java安全的限制而失败，则将错误打在Log中，然后返回 <code>null</code>。
	 * @param name 属性名
	 * @param defaultValue 默认值
	 * @return 属性值或<code>null</code>
	 */
	public static String get(String name, String defaultValue){
		return StringUtil.nullToDefault(get(name, false), defaultValue);
	}
	
	/**
	 * 取得系统属性，如果因为Java安全的限制而失败，则将错误打在Log中，然后返回 <code>null</code>。
	 * 
	 * @param name 属性名
	 * @param quiet 安静模式，不将出错信息打在<code>System.err</code>中
	 * 
	 * @return 属性值或<code>null</code>
	 */
	public static String get(String name, boolean quiet) {
		try {
			return System.getProperty(name);
		} catch (SecurityException e) {
			if (!quiet) {
				logger.error("Caught a SecurityException reading the system property '{}'; the SystemUtil property value will default to null.", name);
			}
			return null;
		}
	}

	/**
	 * 获得System属性（调用System.getProperty）
	 * 
	 * @param key 键
	 * @return 属性值
	 */
	public static String get(String key) {
		return get(key, null);
	}

	/**
	 * 获得boolean类型值
	 * 
	 * @param key 键
	 * @param defaultValue 默认值
	 * @return 值
	 */
	public static boolean getBoolean(String key, boolean defaultValue) {
		String value = get(key);
		if (value == null) {
			return defaultValue;
		}

		value = value.trim().toLowerCase();
		if (value.isEmpty()) {
			return true;
		}

		if ("true".equals(value) || "yes".equals(value) || "1".equals(value)) {
			return true;
		}

		if ("false".equals(value) || "no".equals(value) || "0".equals(value)) {
			return false;
		}

		return defaultValue;
	}

	/**
	 * 获得int类型值
	 * 
	 * @param key 键
	 * @param defaultValue 默认值
	 * @return 值
	 */
	public static long getInt(String key, int defaultValue) {
		return ConvertUtil.toInt(get(key), defaultValue);
	}

	/**
	 * 获得long类型值
	 * 
	 * @param key 键
	 * @param defaultValue 默认值
	 * @return 值
	 */
	public static long getLong(String key, long defaultValue) {
		return ConvertUtil.toLong(get(key), defaultValue);
	}

	/**
	 * @return 属性列表
	 */
	public static Properties props() {
		return System.getProperties();
	}
	//----------------------------------------------------------------------- Basic end

	/**
	 * 取得Java Virtual Machine Specification的信息。
	 * 
	 * @return <code>JvmSpecInfo</code>对象
	 */
	public static final JvmSpecInfo getJvmSpecInfo() {
		return Singleton.get(JvmSpecInfo.class);
	}

	/**
	 * 取得Java Virtual Machine Implementation的信息。
	 * 
	 * @return <code>JvmInfo</code>对象
	 */
	public static final JvmInfo getJvmInfo() {
		return Singleton.get(JvmInfo.class);
	}

	/**
	 * 取得Java Specification的信息。
	 * 
	 * @return <code>JavaSpecInfo</code>对象
	 */
	public static final JavaSpecInfo getJavaSpecInfo() {
		return Singleton.get(JavaSpecInfo.class);
	}

	/**
	 * 取得Java Implementation的信息。
	 * 
	 * @return <code>JavaInfo</code>对象
	 */
	public static final JavaInfo getJavaInfo() {
		return Singleton.get(JavaInfo.class);
	}

	/**
	 * 取得当前运行的JRE的信息。
	 * 
	 * @return <code>JreInfo</code>对象
	 */
	public static final JavaRuntimeInfo getJavaRuntimeInfo() {
		return Singleton.get(JavaRuntimeInfo.class);
	}

	/**
	 * 取得OS的信息。
	 * 
	 * @return <code>OsInfo</code>对象
	 */
	public static final OsInfo getOsInfo() {
		return Singleton.get(OsInfo.class);
	}

	/**
	 * 取得User的信息。
	 * 
	 * @return <code>UserInfo</code>对象
	 */
	public static final UserInfo getUserInfo() {
		return Singleton.get(UserInfo.class);
	}

	/**
	 * 取得Host的信息。
	 * 
	 * @return <code>HostInfo</code>对象
	 */
	public static final HostInfo getHostInfo() {
		return Singleton.get(HostInfo.class);
	}

	/**
	 * 将系统信息输出到<code>System.out</code>中。
	 */
	public static final void dumpSystemInfo() {
		dumpSystemInfo(new PrintWriter(System.out));
	}

	/**
	 * 将系统信息输出到指定<code>PrintWriter</code>中。
	 * 
	 * @param out <code>PrintWriter</code>输出流
	 */
	public static final void dumpSystemInfo(PrintWriter out) {
		out.println("--------------");
		out.println(getJvmSpecInfo());
		out.println("--------------");
		out.println(getJvmInfo());
		out.println("--------------");
		out.println(getJavaSpecInfo());
		out.println("--------------");
		out.println(getJavaInfo());
		out.println("--------------");
		out.println(getJavaRuntimeInfo());
		out.println("--------------");
		out.println(getOsInfo());
		out.println("--------------");
		out.println(getUserInfo());
		out.println("--------------");
		out.println(getHostInfo());
		out.println("--------------");
		out.flush();
	}
	
	/**
	 * 输出到<code>StringBuilder</code>。
	 *
	 * @param builder <code>StringBuilder</code>对象
	 * @param caption 标题
	 * @param value 值
	 */
	public static void append(StringBuilder builder, String caption, String value) {
		builder.append(caption).append(StringUtil.nullToDefault(value, "[n/a]")).append("\n");
	}
}

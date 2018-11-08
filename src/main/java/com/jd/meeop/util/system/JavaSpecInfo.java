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
package com.jd.meeop.util.system;

import com.jd.meeop.util.SystemUtil;

/**
 * 代表Java Specification的信息
 * @ClassName: JavaSpecInfo
 * @Description:
 * @author: 冷水(Toby)
 * @date: 16-12-11 14:45
 * @mail: zhao.wang@aliyun.com
 * @version: v1.0
 * @since: JDK 1.7.0_79(Liunx)
 */
public class JavaSpecInfo{

	private final String JAVA_SPECIFICATION_NAME = SystemUtil.get("java.specification.name", false);
	private final String JAVA_SPECIFICATION_VERSION = SystemUtil.get("java.specification.version", false);
	private final String JAVA_SPECIFICATION_VENDOR = SystemUtil.get("java.specification.vendor", false);

	/**
	 * 取得当前Java Spec.的名称（取自系统属性：<code>java.specification.name</code>）。
	 * 
	 * <p>
	 * 例如Sun JDK 1.4.2：<code>"Java Platform API Specification"</code>
	 * </p>
	 * 
	 * @return 属性值，如果不能取得（因为Java安全限制）或值不存在，则返回<code>null</code>。
	 * 
	 */
	public final String getName() {
		return JAVA_SPECIFICATION_NAME;
	}

	/**
	 * 取得当前Java Spec.的版本（取自系统属性：<code>java.specification.version</code>）。
	 * 
	 * <p>
	 * 例如Sun JDK 1.4.2：<code>"1.4"</code>
	 * </p>
	 * 
	 * @return 属性值，如果不能取得（因为Java安全限制）或值不存在，则返回<code>null</code>。
	 * 
	 * @since Java 1.3
	 */
	public final String getVersion() {
		return JAVA_SPECIFICATION_VERSION;
	}

	/**
	 * 取得当前Java Spec.的厂商（取自系统属性：<code>java.specification.vendor</code>）。
	 * 
	 * <p>
	 * 例如Sun JDK 1.4.2：<code>"Sun Microsystems Inc."</code>
	 * </p>
	 * 
	 * @return 属性值，如果不能取得（因为Java安全限制）或值不存在，则返回<code>null</code>。
	 * 
	 */
	public final String getVendor() {
		return JAVA_SPECIFICATION_VENDOR;
	}

	/**
	 * 将Java Specification的信息转换成字符串。
	 * 
	 * @return JVM spec.的字符串表示
	 */
	public final String toString() {
		StringBuilder builder = new StringBuilder();

		SystemUtil.append(builder, "Java Spec. Name:    ", getName());
		SystemUtil.append(builder, "Java Spec. Version: ", getVersion());
		SystemUtil.append(builder, "Java Spec. Vendor:  ", getVendor());

		return builder.toString();
	}

}

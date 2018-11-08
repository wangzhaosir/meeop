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

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 代表当前主机的信息
 * @ClassName: HostInfo
 * @Description:
 * @author: 冷水(Toby)
 * @date: 16-12-11 14:45
 * @mail: zhao.wang@aliyun.com
 * @version: v1.0
 * @since: JDK 1.7.0_79(Liunx)
 */
public class HostInfo{

	private final String HOST_NAME;
	private final String HOST_ADDRESS;

	public HostInfo() {
		String hostName;
		String hostAddress;

		try {
			InetAddress localhost = InetAddress.getLocalHost();

			hostName = localhost.getHostName();
			hostAddress = localhost.getHostAddress();
		} catch (UnknownHostException e) {
			hostName = "localhost";
			hostAddress = "127.0.0.1";
		}

		HOST_NAME = hostName;
		HOST_ADDRESS = hostAddress;
	}

	/**
	 * 取得当前主机的名称。
	 * 
	 * <p>
	 * 例如：<code>"webserver1"</code>
	 * </p>
	 * 
	 * @return 主机名
	 */
	public final String getName() {
		return HOST_NAME;
	}

	/**
	 * 取得当前主机的地址。
	 * 
	 * <p>
	 * 例如：<code>"192.168.0.1"</code>
	 * </p>
	 * 
	 * @return 主机地址
	 */
	public final String getAddress() {
		return HOST_ADDRESS;
	}

	/**
	 * 将当前主机的信息转换成字符串。
	 * 
	 * @return 主机信息的字符串表示
	 */
	public final String toString() {
		StringBuilder builder = new StringBuilder();

		SystemUtil.append(builder, "Host Name:    ", getName());
		SystemUtil.append(builder, "Host Address: ", getAddress());

		return builder.toString();
	}

}

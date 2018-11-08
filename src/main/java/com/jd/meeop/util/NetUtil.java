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
import com.jd.meeop.lang.Validator;

import java.net.*;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * 网络相关工具
 * @ClassName: NetUtil
 * @Description:
 * @author: 冷水(Toby)
 * @date: 16-12-7 14:45
 * @mail: zhao.wang@aliyun.com
 * @version: v1.0
 * @since: JDK 1.7.0_79(Liunx)
 */
public class NetUtil {
	public final static String LOCAL_IP = "127.0.0.1";

	/**
	 * 静态类不可实例化
	 */
	private NetUtil(){}

	/**
	 * 根据long值获取ip v4地址
	 * 
	 * @param longIP IP的long表示形式
	 * @return IP V4 地址
	 */
	public static String longToIpv4(long longIP) {
		StringBuffer sb = new StringBuffer();
		// 直接右移24位
		sb.append(String.valueOf(longIP >>> 24));
		sb.append(".");
		// 将高8位置0，然后右移16位
		sb.append(String.valueOf((longIP & 0x00FFFFFF) >>> 16));
		sb.append(".");
		sb.append(String.valueOf((longIP & 0x0000FFFF) >>> 8));
		sb.append(".");
		sb.append(String.valueOf(longIP & 0x000000FF));
		return sb.toString();
	}

	/**
	 * 根据ip地址计算出long型的数据
	 * 
	 * @param strIP IP V4 地址
	 * @return long值
	 */
	public static long ipv4ToLong(String strIP) {
		if (Validator.isIpv4(strIP)) {
			long[] ip = new long[4];
			// 先找到IP地址字符串中.的位置
			int position1 = strIP.indexOf(".");
			int position2 = strIP.indexOf(".", position1 + 1);
			int position3 = strIP.indexOf(".", position2 + 1);
			// 将每个.之间的字符串转换成整型
			ip[0] = Long.parseLong(strIP.substring(0, position1));
			ip[1] = Long.parseLong(strIP.substring(position1 + 1, position2));
			ip[2] = Long.parseLong(strIP.substring(position2 + 1, position3));
			ip[3] = Long.parseLong(strIP.substring(position3 + 1));
			return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
		}
		return 0;
	}
	
	/**
	 * 检测本地端口可用性
	 * 
	 * @param port 被检测的端口
	 * @return 是否可用
	 */
	public static boolean isUsableLocalPort(int port) {
		if (! isValidPort(port)) {
			// 给定的IP未在指定端口范围中
			return false;
		}
		try {
			new Socket(LOCAL_IP, port).close();
			// socket链接正常，说明这个端口正在使用
			return false;
		} catch (Exception e) {
			return true;
		}
	}
	
	/**
	 * 是否为有效的端口
	 * @param port 端口号
	 * @return 是否有效
	 */
	public static boolean isValidPort(int port) {
		//有效端口是0～65535
		return port >= 0 && port <= 0xFFFF;
	}
	
	/**
	 * 判定是否为内网IP<br>
	 * 私有IP：A类 10.0.0.0-10.255.255.255 B类 172.16.0.0-172.31.255.255 C类
	 * 192.168.0.0-192.168.255.255 当然，还有127这个网段是环回地址
	 **/
	public static boolean isInnerIP(String ipAddress) {
		boolean isInnerIp = false;
		long ipNum = NetUtil.ipv4ToLong(ipAddress);
		
		long aBegin = NetUtil.ipv4ToLong("10.0.0.0");
		long aEnd = NetUtil.ipv4ToLong("10.255.255.255");
		
		long bBegin = NetUtil.ipv4ToLong("172.16.0.0");
		long bEnd = NetUtil.ipv4ToLong("172.31.255.255");
		
		long cBegin = NetUtil.ipv4ToLong("192.168.0.0");
		long cEnd = NetUtil.ipv4ToLong("192.168.255.255");
		
		isInnerIp = isInner(ipNum, aBegin, aEnd) || isInner(ipNum, bBegin, bEnd) || isInner(ipNum, cBegin, cEnd) || ipAddress.equals(LOCAL_IP);
		return isInnerIp;
	}
	
	/**
	 * 获得本机的IP地址列表
	 * @return IP地址列表
	 */
	public static Set<String> localIpv4s() {
		Enumeration<NetworkInterface> networkInterfaces = null;
		try {
			networkInterfaces = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e) {
			throw new UtilException(e.getMessage(), e);
		}
		
		if(networkInterfaces == null) {
			throw new UtilException("Get network interface error!");
		}
		
		final HashSet<String> ipSet = new HashSet<String>();
		
		while(networkInterfaces.hasMoreElements()) {
			final NetworkInterface networkInterface = networkInterfaces.nextElement();
			final Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
			while(inetAddresses.hasMoreElements()) {
				final InetAddress inetAddress = inetAddresses.nextElement();
				if(inetAddress != null && inetAddress instanceof Inet4Address) {
					ipSet.add(inetAddress.getHostAddress());
				}
			}
		}
		
		return ipSet;
	}
	
	/**
	 * 相对URL转换为绝对URL
	 * @param absoluteBasePath 基准路径，绝对
	 * @param relativePath 相对路径
	 * @return 绝对URL
	 */
	public static String toAbsoluteUrl(String absoluteBasePath, String relativePath) {
		try {
			URL absoluteUrl = new URL(absoluteBasePath);
			return new URL(absoluteUrl ,relativePath).toString();
		} catch (Exception e) {
			throw new UtilException(StringUtil.format("To absolute url [{}] base [{}] error!", relativePath, absoluteBasePath), e);
		}
	}
	
	/**
	 * 隐藏掉IP地址的最后一部分为 * 代替
	 * @param ip IP地址
	 * @return 隐藏部分后的IP
	 */
	public static String hideIpPart(String ip) {
		return new StringBuffer(ip.length())
			.append(ip.substring(0, ip.lastIndexOf(".") + 1))
			.append("*").toString();
	}
	
	/**
	 * 隐藏掉IP地址的最后一部分为 * 代替
	 * @param ip IP地址
	 * @return 隐藏部分后的IP
	 */
	public static String hideIpPart(long ip) {
		return hideIpPart(longToIpv4(ip));
	}
	
	/**
	 * 构建InetSocketAddress<br>
	 * 当host中包含端口时（用“：”隔开），使用host中的端口，否则使用默认端口<br>
	 * 给定host为空时使用本地host（127.0.0.1）
	 * @param host Host
	 * @param defaultPort 默认端口
	 * @return InetSocketAddress
	 */
	public static InetSocketAddress buildInetSocketAddress(String host, int defaultPort) {
		if(StringUtil.isBlank(host)) {
			host = LOCAL_IP;
		}
		
		String destHost = null;
		int port = 0;
		int index = host.indexOf(":");
		if (index != -1) {
			// host:port形式
			destHost = host.substring(0, index);
			port = Integer.parseInt(host.substring(index + 1));
		} else {
			destHost = host;
			port = defaultPort;
		}
		
		return new InetSocketAddress(destHost, port);
	}
	
	//----------------------------------------------------------------------------------------- Private method start
	/**
	 * 指定IP的long是否在指定范围内
	 * @param userIp 用户IP
	 * @param begin 开始IP
	 * @param end 结束IP
	 * @return 是否在范围内
	 */
	private static boolean isInner(long userIp, long begin, long end) {
		return (userIp >= begin) && (userIp <= end);
	}

}

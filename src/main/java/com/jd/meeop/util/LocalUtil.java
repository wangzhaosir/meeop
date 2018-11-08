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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.*;
import java.util.Enumeration;

/**
 * @ClassName:
 * @Description:
 * @author: 冷水(Toby)
 * @date: 2017/3/6 16:45
 * @mail: zhao.wang@aliyun.com
 * @version: v1.0
 * @since: JDK 1.7.0_79(Liunx)
 */
public class LocalUtil {
    private static final Logger log = LoggerFactory.getLogger(LocalUtil.class);
    private static NetworkInterface localNetworkInterface;
    private static String hostName;

    public LocalUtil() {
    }

    public static NetworkInterface getLocalNetworkInterface() {
        return localNetworkInterface;
    }

    public static String getMachineName() {
        return hostName;
    }

    static {
        try {
            Enumeration e = NetworkInterface.getNetworkInterfaces();

            while(e.hasMoreElements()) {
                NetworkInterface networkInterface = (NetworkInterface)e.nextElement();
                if(!networkInterface.getName().equals("lo")) {
                    localNetworkInterface = networkInterface;
                    break;
                }
            }
        } catch (SocketException var3) {
            log.error("init LocalHost error!", var3);
        }

        try {
            hostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException var2) {
            log.error("init hostname error!", var2);
        }

    }



    public static String getLocalIP(){
        String ip = null;
        boolean isWindows = isWindowsOS(); // 判断是否是windows系统
        if (isWindows) {
            InetAddress address = null;
            try {
                address = InetAddress.getLocalHost();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            if (address!=null){
                ip = address.getHostAddress(); // 获取ip地址
                log.info("windows ip地址：" + ip);
            } else{
                log.info("address为null， 未获取到ip");
            }
        } else { // 如果是Linux系统
            try {
                ip = getLinuxIP();
            } catch (SocketException e) {
                e.printStackTrace();
            }
            log.info("linux ip地址：" + ip);
        }
        return ip;
    }

    public static boolean isWindowsOS() {
        boolean isWindowsOS = false;
        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().indexOf("windows") > -1) {
            isWindowsOS = true;
        }
        return isWindowsOS;
    }
    /**
     * 判断是否是回环IP地址
     * <p>回环IP地址范围：
     * <br/>127.0.0.1 - 127.255.255.254
     *
     * @param ip 需要识别的ip
     * @return 如果是环IP地址，返回true；否则，返回false
     */
    public static boolean isLoopbackIp(String ip) {
        boolean flag = false;
        try {
            flag = InetAddress.getByName(ip).isLoopbackAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 判断是否是内网IP
     * <p>内网IP范围：
     * <br/>10.0.0.0~10.255.255.255
     * <br/>172.16.0.0~172.31.255.255
     * <br/>192.168.0.0~192.168.255.255
     *
     * @param ip 需要识别的ip
     * @return 如果是内网ip，返回true；否则，返回false
     */
    public static boolean isInnerIp(String ip) {
        boolean flag = false;
        try {
            flag = InetAddress.getByName(ip).isSiteLocalAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static String getLinuxIP() throws SocketException {
        // 根据网卡取本机配置的IP
        Enumeration netInterfaces = NetworkInterface.getNetworkInterfaces();
        InetAddress ipAddress = null;
        String ip = "";
        while (netInterfaces.hasMoreElements()) {
            NetworkInterface ni = (NetworkInterface) netInterfaces
                    .nextElement();
            if (!ni.getName().equals("eth0")) {
                continue;
            } else {
                Enumeration e2 = ni.getInetAddresses();
                while (e2.hasMoreElements()) {
                    ipAddress = (InetAddress) e2.nextElement();
                    if (ipAddress instanceof Inet6Address)
                        continue;
                    ip = ipAddress.getHostAddress();
                    log.info("getLinuxIp:" + ip);
                }
                break;
            }
        }
        return ip;
    }

    public static void main(String args[]) throws UnknownHostException,
            SocketException {
        String ip;
        boolean isWindows = isWindowsOS(); // 判断是否是windows系统
        if (isWindows) {
            InetAddress address = InetAddress.getLocalHost();
            ip = address.getHostAddress(); // 获取ip地址
            log.info("windows ip地址：" + ip);
        } else { // 如果是Linux系统
            ip = getLinuxIP();
            log.info("linux ip地址：" + ip);
        }
    }
}

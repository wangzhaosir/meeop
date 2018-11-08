package com.jd.meeop.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * UUID工具类
 * @ClassName: UuidUtil
 * @Description:
 * @author: 冷水(Toby)
 * @date: 16-12-7 14:45
 * @mail: zhao.wang@aliyun.com
 * @version: v1.0
 * @since: JDK 1.7.0_79(Liunx)
 */
public class UuidUtil {
	
	public static String getUUID() {
		return UUID.randomUUID().toString().trim().replaceAll("-", "");
	}
	
	public static String getUUID(String code) throws Exception {
		String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "")
				+ RandomNumber() + code;
		return encrypt(uuid);
	}

	private static String RandomNumber() throws Exception {
		TimeUnit.MILLISECONDS.sleep(2L);
		Random r = new Random(System.currentTimeMillis());
		int t1 = (1 + r.nextInt(8)) * 10000 + r.nextInt(10000);
		int t2 = (1 + r.nextInt(8)) * 10000 + r.nextInt(10000);
		return t1 + "" + t2;
	}

	private static String encrypt(String inputText) {
		if (inputText == null || "".equals(inputText.trim())) {
			throw new IllegalArgumentException("请输入要加密的内容");
		}
		String encryptText = null;
		try {
			MessageDigest m = MessageDigest.getInstance("MD5");
			m.update(inputText.getBytes("UTF-8"));
			byte s[] = m.digest();
			return hex(s);
		} catch (NoSuchAlgorithmException e) {
		} catch (UnsupportedEncodingException e) {
		}
		return encryptText;
	}

	// 返回十六进制字符串
	private static String hex(byte[] arr) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < arr.length; ++i) {
			sb.append(Integer.toHexString((arr[i] & 0xFF) | 0x100).substring(1,
					3));
		}
		return sb.toString();
	}
	
	public static void main(String[] args) {
//		System.out.println(getUUID());
	}
}

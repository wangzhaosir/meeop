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
import com.jd.meeop.lang.Base64;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.io.File;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * 安全相关工具类
 * @ClassName: SecureUtil
 * @Description:
 * @author: 冷水(Toby)
 * @date: 16-12-7 14:45
 * @mail: zhao.wang@aliyun.com
 * @version: v1.0
 * @since: JDK 1.7.0_79(Liunx)
 */
public class SecureUtil {

	public static final String HMAC_SHA1 = "HmacSHA1";

	public static final String RIPEMD128 = "RIPEMD128";
	public static final String RIPEMD160 = "RIPEMD160";

	/**
	 * 静态类不可实例化
	 */
	private SecureUtil(){}

	/**
	 * MD5算法加密
	 * 
	 * @param source 被加密的字符串
	 * @return 被加密后的字符串
	 * @see DigestUtil#md5Hex(byte[])
	 */
	public static String md5(byte[] source) {
		return DigestUtil.md5Hex(source);
	}
	
	/**
	 * MD5算法加密
	 * 
	 * @param file 被加密的文件
	 * @return 被加密后的字符串
	 * @see DigestUtil#md5Hex(File)
	 */
	public static String md5(File file) {
		return DigestUtil.md5Hex(file);
	}
	
	/**
	 * MD5算法加密
	 * 
	 * @param source 被加密的字符串
	 * @param charset 字符集
	 * @return 被加密后的字符串
	 * @see DigestUtil#md5Hex(String, String)
	 */
	public static String md5(String source, String charset) {
		return DigestUtil.md5Hex(source, charset);
	}
	
	/**
	 * MD5算法加密
	 * 
	 * @param source 被加密的字符串
	 * @return 被加密后的字符串
	 * @see DigestUtil#md5Hex(String, String)
	 */
	public static String md5(String source) {
		return DigestUtil.md5Hex(source);
	}
	
	//------------------------------------------------------------------- SHA-1

	/**
	 * SHA-1算法加密
	 * 
	 * @param source 被加密的字符串
	 * @param charset 字符集
	 * @return 被加密后的字符串
	 * @see DigestUtil#sha1Hex(String, String)
	 */
	public static String sha1(String source, String charset) {
		return DigestUtil.sha1Hex(source, charset);
	}
	
	/**
	 * SHA-1算法加密
	 * 
	 * @param file 被加密的字符串
	 * @return 被加密后的字符串
	 * @see DigestUtil#sha1Hex(File)
	 */
	public static String sha1(File file) {
		return DigestUtil.sha1Hex(file);
	}

	// ------------------------------------------------------------------------ MAC
	/**
	 * MAC 算法加密
	 * 
	 * @param algorithm 算法
	 * @param key 加密使用的key
	 * @param data 待加密的数据
	 * @return 被加密后的bytes
	 */
	public static byte[] mac(String algorithm, byte[] key, byte[] data) {
		Mac mac = null;
		try {
			mac = Mac.getInstance(algorithm);
			mac.init(new SecretKeySpec(key, algorithm));
		} catch (NoSuchAlgorithmException e) {
			throw new UtilException(e, "No such algorithm: {}", algorithm);
		} catch (InvalidKeyException e) {
			throw new UtilException(e, "Invalid key: {}", key);
		}
		return mac == null ? null : mac.doFinal(data);
	}

	/**
	 * MAC SHA-1算法加密
	 * 
	 * @param key 加密使用的key
	 * @param data 待加密的数据
	 * @return 被加密后的bytes
	 */
	public static byte[] hmacSha1(byte[] key, byte[] data) {
		return mac(HMAC_SHA1, key, data);
	}

	/**
	 * MAC SHA-1算法加密
	 * 
	 * @param key 加密使用的key
	 * @param data 被加密的字符串
	 * @param charset 字符集
	 * @return 被加密后的字符串
	 */
	public static String hmacSha1(String key, String data, String charset) {
		final Charset charsetObj = Charset.forName(charset);
		final byte[] bytes = hmacSha1(key.getBytes(charsetObj), data.getBytes(charsetObj));
		return Base64.encode(bytes, charset);
	}

	/**
	 * 初始化HMAC密钥
	 * 
	 * @param algorithm 算法
	 * @param charset 字符集
	 * @return key
	 * @throws Exception
	 */
	public static String initMacKey(String algorithm, String charset) throws Exception {
		return Base64.encode(KeyGenerator.getInstance(algorithm).generateKey().getEncoded(), charset);
	}

	/**
	 * @return 简化的UUID，去掉了横线
	 */
	public static String simpleUUID(){
		return UUID.randomUUID().toString().replace("-", "");
	}

    /**
     * 生成key
     * @param seed
     * @return
     * @throws Exception
     */
    public static String key(String seed) throws Exception {
        byte[] seedBase64DecodeBytes = Base64.decode(seed);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		DESKeySpec keySpec = new DESKeySpec(seedBase64DecodeBytes);
		keyFactory.generateSecret(keySpec);
		SecretKey secretKey = keyFactory.generateSecret(keySpec);
        byte[] bytes = secretKey.getEncoded();
        return Base64.encode(bytes);
    }

    /**
     * 加密数据
     * @param text 数据
     * @param key 密钥
     * @param charset 编码
     * @return
     * @throws Exception
     */
    public static String encrypt(String text, String key, String charset) throws Exception {
        byte[] keyBase64DecodeBytes = Base64.decode(key);
        DESKeySpec desKeySpec = new DESKeySpec(keyBase64DecodeBytes);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] textBytes = text.getBytes(charset);
        byte[] bytes = cipher.doFinal(textBytes);
        return new String(Base64.encode(bytes, true, true));
    }

    /**
     * 解密数据
     * @param text 数据
     * @param key 密钥
     * @param charset 编码
     * @return
     * @throws Exception
     */
    public static String decrypt(String text, String key, String charset) throws Exception {
        byte[] keyBase64DecodeBytes = Base64.decode(key);
        DESKeySpec desKeySpec = new DESKeySpec(keyBase64DecodeBytes);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] textBytes = Base64.decode(text);
        byte[] bytes = cipher.doFinal(textBytes);
        return new String(bytes, charset);
    }

    /**
     * 生成签名，SHA-512消息摘要算法
     * @param data
     * @return
     * @throws Exception
     */
    public static String encodeSHA512(byte[] data) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        byte[] digest = md.digest(data);
        return new HexBinaryAdapter().marshal(digest);
    }

    public static String desEncrypt(String inputStr, String seed) throws Exception {
        String key = key(seed);  //见生成密钥方法和加密种子
        String encrypt = encrypt(inputStr, key, "UTF-8"); //见DES加密方法
        return encrypt;
    }

    public static String decode(String text, String seed) throws Exception {
        String key = key(seed);  //见生成密钥方法和加密种子
        return decrypt(text, key, "UTF-8");
    }


}

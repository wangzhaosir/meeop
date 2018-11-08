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

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 数字相关工具类
 * @ClassName: MathUtil
 * @Description:
 * @author: 冷水(Toby)
 * @date: 16-12-7 14:45
 * @mail: zhao.wang@aliyun.com
 * @version: v1.0
 * @since: JDK 1.7.0_79(Liunx)
 */
public class MathUtil {

	/**
	 * 静态类不可实例化
	 */
	private MathUtil(){}

	/**
	 * 保留小数位
	 * @param number 被保留小数的数字
	 * @param digit 保留的小数位数
	 * @return 保留小数后的字符串
	 */
	public static String roundStr(double number, int digit) {
		return String.format("%."+digit + 'f', number);
	}
	
	/**
	 * 保留小数位
	 * @param number 被保留小数的数字
	 * @param digit 保留的小数位数
	 * @return 保留小数后的字符串
	 */
	public static double round(double number, int digit) {
		final BigDecimal bg = new BigDecimal(number);
		return bg.setScale(digit, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 数据相乘
	 * @param a
	 * @param b
	 * @return
	 */
	public static BigDecimal multiply(BigDecimal a, BigDecimal b){
		return a.multiply(b);
	}

	/**
	 * 数据相加
	 * @param a
	 * @param b
	 * @return
	 */
	public static BigDecimal add(BigDecimal a, BigDecimal b){
		return a.add(b);
	}

	/**
	 * 多数值相加
	 * @param as
	 * @return
	 */
	public static BigDecimal adds(BigDecimal... as){
		BigDecimal b = BigDecimal.ZERO;
		for(BigDecimal a : as) b = add(a, b);
		return b;
	}

	/**
	 * 数据相减
	 * @param a
	 * @param b
	 * @return
	 */
	public static BigDecimal subtract(BigDecimal a, BigDecimal b){
		return a.subtract(b);
	}

	/**
	 * 数据相减
	 * @param a
	 * @param bs
	 * @return
	 */
	public static BigDecimal subtracts(BigDecimal a, BigDecimal... bs){
		for(BigDecimal b : bs){
			a = subtract(a, b);
		}
		return a;
	}

	/**
	 * 两个值本比较
	 * @param a
	 * @param b
	 * @return
	 */
	public static int compare(BigDecimal a, BigDecimal b){
		return a.compareTo(b);
	}

	/**
	 * 金额格式化
	 * @param b
	 * @return
	 */
	public static String format(BigDecimal b, String fmt){
		if(StringUtil.isBlank(fmt)) return format(b);
		DecimalFormat df = new DecimalFormat(fmt);
		return df.format(b);
	}

	/**
	 * 金额格式化
	 * @param b
	 * @return
	 */
	public static String format(BigDecimal b){
		DecimalFormat df = new DecimalFormat("#0.00");
		return df.format(b);
	}

	/**
	 * 判断两个值是否相等
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean equals(BigDecimal a, BigDecimal b){
		if(a == null || b == null) return Boolean.FALSE;
		if(a.compareTo(b) == 0) return Boolean.TRUE;
		return Boolean.FALSE;
	}

	/**
	 * 计算平方（此方法为int类型，会有问题）
	 * @param a
	 * @param b
	 * @return
	 */
	public static int pow(int a, int b){
		int r = 1;
		while(b > 0) {
			r *= a;
			b--;
		}
		return r;
	}
}

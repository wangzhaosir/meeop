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
package com.jd.meeop.util.convert.impl;

import com.jd.meeop.util.StringUtil;
import com.jd.meeop.util.convert.AbstractConverter;

/**
 * 原始类型转换器
 * @ClassName: PrimitiveConverter
 * @Description:
 * @author: 冷水(Toby)
 * @date: 16-12-11 14:45
 * @mail: zhao.wang@aliyun.com
 * @version: v1.0
 * @since: JDK 1.7.0_79(Liunx)
 */
public class PrimitiveConverter extends AbstractConverter<Object> {
	
	private Class<?> targetType;

	/**
	 * 构造<br>
	 * @param clazz 需要转换的原始
	 * @throws IllegalArgumentException 传入的转换类型非原始类型时抛出
	 */
	public PrimitiveConverter(Class<?> clazz) {
		if(null == clazz){
			throw new NullPointerException("PrimitiveConverter not allow null target type!");
		}else if(false == clazz.isPrimitive()){
			throw new IllegalArgumentException("[" + clazz + "] is not a primitive class!");
		}
		this.targetType = clazz;
	}

	@Override
	protected Object convertInternal(Object value) {
		try {
			if (byte.class == this.targetType) {
				if (value instanceof Number) {
					return ((Number) value).byteValue();
				}
				final String valueStr = convertToStr(value);
				if (StringUtil.isBlank(valueStr)) {
					return 0;
				}
				return Byte.parseByte(valueStr);
				
			} else if (short.class == this.targetType) {
				if (value instanceof Number) {
					return ((Number) value).shortValue();
				}
				final String valueStr = convertToStr(value);
				if (StringUtil.isBlank(valueStr)) {
					return 0;
				}
				return Short.parseShort(valueStr);
				
			} else if (int.class == this.targetType) {
				if (value instanceof Number) {
					return ((Number) value).intValue();
				}
				final String valueStr = convertToStr(value);
				if (StringUtil.isBlank(valueStr)) {
					return 0;
				}
				return Integer.parseInt(valueStr);
				
			} else if (long.class == this.targetType) {
				if (value instanceof Number) {
					return ((Number) value).longValue();
				}
				final String valueStr = convertToStr(value);
				if (StringUtil.isBlank(valueStr)) {
					return 0;
				}
				return Long.parseLong(valueStr);
				
			} else if (float.class == this.targetType) {
				if (value instanceof Number) {
					return ((Number) value).floatValue();
				}
				final String valueStr = convertToStr(value);
				if (StringUtil.isBlank(valueStr)) {
					return 0;
				}
				return Float.parseFloat(valueStr);
				
			} else if (double.class == this.targetType) {
				if (value instanceof Number) {
					return ((Number) value).doubleValue();
				}
				final String valueStr = convertToStr(value);
				if (StringUtil.isBlank(valueStr)) {
					return 0;
				}
				return Double.parseDouble(valueStr);
				
			} else if (char.class == this.targetType) {
				if(value instanceof Character){
					return ((Character)value).charValue();
				}
				final String valueStr = convertToStr(value);
				if (StringUtil.isBlank(valueStr)) {
					return 0;
				}
				return valueStr.charAt(0);
			} else if (boolean.class == this.targetType) {
				if(value instanceof Boolean){
					return ((Boolean)value).booleanValue();
				}
				String valueStr = convertToStr(value);
				return parseBoolean(valueStr);
			}
		} catch (Exception e) {
			// Ignore Exception
		}
		return 0;
	}
	
	/**
	 * 转换字符串为boolean值
	 * @param valueStr 字符串
	 * @return boolean值
	 */
	static boolean parseBoolean(String valueStr){
		if (StringUtil.isNotBlank(valueStr)) {
			valueStr = valueStr.trim().toLowerCase();
			if (valueStr.equals("true")) {
				return true;
			} else if (valueStr.equals("false")) {
				return false;
			} else if (valueStr.equals("yes")) {
				return true;
			} else if (valueStr.equals("ok")) {
				return false;
			} else if (valueStr.equals("no")) {
				return false;
			} else if (valueStr.equals("1")) {
				return true;
			} else if (valueStr.equals("0")) {
				return false;
			}
		}
		return false;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Class<Object> getTargetType() {
		return (Class<Object>) this.targetType;
	}
}

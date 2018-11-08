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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;

/**
 * 数字转换器
 * @ClassName: NumberConverter
 * @Description:
 * @author: 冷水(Toby)
 * @date: 16-12-11 14:45
 * @mail: zhao.wang@aliyun.com
 * @version: v1.0
 * @since: JDK 1.7.0_79(Liunx)
 */
public class NumberConverter extends AbstractConverter<Number> {

	private Class<? extends Number> targetType;

	public NumberConverter() {
		this.targetType = Number.class;
	}

	/**
	 * 构造<br>
	 * @param clazz 需要转换的数字类型，默认 {@link Number}
	 */
	public NumberConverter(Class<? extends Number> clazz) {
		this.targetType = (null == clazz) ? Number.class : clazz;
	}

	@Override
	protected Number convertInternal(Object value) {
		try {
			if (Byte.class == this.targetType) {
				if (value instanceof Number) {
					return Byte.valueOf(((Number) value).byteValue());
				}
				final String valueStr = convertToStr(value);
				if (StringUtil.isBlank(valueStr)) {
					return null;
				}
				return Byte.valueOf(valueStr);
				
			} else if (Short.class == this.targetType) {
				if (value instanceof Number) {
					return Short.valueOf(((Number) value).shortValue());
				}
				final String valueStr = convertToStr(value);
				if (StringUtil.isBlank(valueStr)) {
					return null;
				}
				return Short.valueOf(valueStr);
				
			} else if (Integer.class == this.targetType) {
				if (value instanceof Number) {
					return Integer.valueOf(((Number) value).intValue());
				}
				final String valueStr = convertToStr(value);
				if (StringUtil.isBlank(valueStr)) {
					return null;
				}
				return Integer.valueOf(valueStr);
				
			} else if (Long.class == this.targetType) {
				if (value instanceof Number) {
					return Long.valueOf(((Number) value).longValue());
				}
				final String valueStr = convertToStr(value);
				if (StringUtil.isBlank(valueStr)) {
					return null;
				}
				return Long.valueOf(valueStr);
				
			} else if (Float.class == this.targetType) {
				if (value instanceof Number) {
					return Float.valueOf(((Number) value).floatValue());
				}
				final String valueStr = convertToStr(value);
				if (StringUtil.isBlank(valueStr)) {
					return null;
				}
				return Float.valueOf(valueStr);
				
			} else if (Double.class == this.targetType) {
				if (value instanceof Number) {
					return Double.valueOf(((Number) value).doubleValue());
				}
				final String valueStr = convertToStr(value);
				if (StringUtil.isBlank(valueStr)) {
					return null;
				}
				return Double.valueOf(valueStr);
				
			} else if (BigDecimal.class == this.targetType) {
				return toBigDecimal(value);
				
			} else if (BigInteger.class == this.targetType) {
				toBigInteger(value);
			}

			final String valueStr = convertToStr(value);
			if (StringUtil.isBlank(valueStr)) {
				return null;
			}
			return NumberFormat.getInstance().parse(valueStr);
		} catch (Exception e) {
			// Ignore Exception
		}
		return null;
	}

	/**
	 * 转换为BigDecimal<br>
	 * 如果给定的值为空，或者转换失败，返回默认值<br>
	 * 转换失败不会报错
	 * 
	 * @param value 被转换的值
	 * @return 结果
	 */
	private BigDecimal toBigDecimal(Object value) {
		if (value instanceof Long) {
			return new BigDecimal((Long) value);
		} else if (value instanceof Double) {
			return new BigDecimal((Double) value);
		} else if (value instanceof Integer) {
			return new BigDecimal((Integer) value);
		} else if (value instanceof BigInteger) {
			return new BigDecimal((BigInteger) value);
		}

		final String valueStr = convertToStr(value);
		if (StringUtil.isBlank(valueStr)) {
			return null;
		}
		return new BigDecimal(valueStr);
	}

	/**
	 * 转换为BigInteger<br>
	 * 如果给定的值为空，或者转换失败，返回默认值<br>
	 * 转换失败不会报错
	 * 
	 * @param value 被转换的值
	 * @return 结果
	 */
	private BigInteger toBigInteger(Object value) {
		if (value instanceof Long) {
			return BigInteger.valueOf((Long) value);
		}
		final String valueStr = convertToStr(value);
		if (StringUtil.isBlank(valueStr)) {
			return null;
		}
		return new BigInteger(valueStr);
	}
	
	@Override
	protected String convertToStr(Object value){
		final String valueStr = super.convertToStr(value);
		return (null == valueStr) ? null : valueStr.trim();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Class<Number> getTargetType() {
		return (Class<Number>) this.targetType;
	}
}

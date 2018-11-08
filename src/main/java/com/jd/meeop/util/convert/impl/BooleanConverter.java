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

import com.jd.meeop.util.convert.AbstractConverter;

/**
 * 波尔转换器
 * @ClassName: BooleanConverter
 * @Description:
 * @author: 冷水(Toby)
 * @date: 16-12-11 14:45
 * @mail: zhao.wang@aliyun.com
 * @version: v1.0
 * @since: JDK 1.7.0_79(Liunx)
 */
public class BooleanConverter extends AbstractConverter<Boolean> {

	@Override
	protected Boolean convertInternal(Object value) {
		if(boolean.class == value.getClass()){
			return (Boolean)value;
		}
		String valueStr = convertToStr(value);
		return Boolean.valueOf(PrimitiveConverter.parseBoolean(valueStr));
	}
}

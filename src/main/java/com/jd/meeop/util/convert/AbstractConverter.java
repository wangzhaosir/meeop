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
package com.jd.meeop.util.convert;

import com.jd.meeop.util.CollectionUtil;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.MessageFormat;

/**
 * 抽象转换器，提供通用的转换逻辑
 * @ClassName: AbstractConverter
 * @Description:
 * @author: 冷水(Toby)
 * @date: 16-12-11 14:45
 * @mail: zhao.wang@aliyun.com
 * @version: v1.0
 * @since: JDK 1.7.0_79(Liunx)
 */
public abstract class AbstractConverter<T> implements Converter<T>{
	
	@Override
	public T convert(Object value, T defaultValue) {
		Class<T> targetType = getTargetType();
		if(null == targetType){
			targetType = (Class<T>) defaultValue.getClass();
		}
		
		if(targetType.isPrimitive()){
			//原始类型直接调用内部转换，内部转换永远不会返回null
			return convertInternal(value);
		}
		
		if(null == value){
			return defaultValue;
		}
		if(null == defaultValue || targetType.isInstance(defaultValue)){
			if(targetType.isInstance(value)){
				//已经是目标类型，不需要转换
				return targetType.cast(value);
			}
			final T convertInternal = convertInternal(value);
			return ((null == convertInternal) ? defaultValue : convertInternal);
		}else{
			throw new IllegalArgumentException(MessageFormat.format("Default value [{0}] is not the instance of [{1}]]", defaultValue, targetType));
		}
	}
	
	/**
	 * 内部转换器，被 {@link AbstractConverter#convert(Object, Object)} 调用，实现基本转换逻辑
	 * @param value 值
	 * @return 转换后的类型
	 */
	protected abstract T convertInternal(Object value);
	
	/**
	 * 值转为String
	 * @param value 值
	 * @return String
	 */
	protected String convertToStr(Object value) {
		if (null == value) {
			return null;
		}
		if (value instanceof String) {
			return (String) value;
		}else if (CollectionUtil.isArray(value)) {
			return CollectionUtil.toString(value);
		}
		return value.toString();
	}
	
	/**
	 * 获得此类实现类的泛型类型
	 * @return 此类的泛型类型
	 */
	@SuppressWarnings("unchecked")
	public Class<T> getTargetType() {
		Type superType = getClass().getGenericSuperclass();
		if(superType instanceof ParameterizedType){
			ParameterizedType genericSuperclass = (ParameterizedType) superType;
			Type[] types = genericSuperclass.getActualTypeArguments();
			if(null != types && types.length > 0){
				Type type = types[0];
				if(type instanceof Class){
					return (Class<T>)type;
				}
			}
		}
		return null;
	}
}

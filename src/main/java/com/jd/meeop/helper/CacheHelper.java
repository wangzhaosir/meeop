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
package com.jd.meeop.helper;

/**
 * @ClassName: Project
 * @Description:
 * @author: 冷水(Toby)
 * @date: 16-12-12 18:16
 * @mail: zhao.wang@aliyun.com
 * @version: v1.0
 * @since: JDK 1.7.0_79(Liunx)
 */
public class CacheHelper<T> {

    /**
     * 缓存实体含key
     */
    public static final String CACHE_ENIGMA_ENTITY_KEY = "enigma_entity:%s:%s";

    /**
     * 缓存实体属性含key
     */
    public static final String CACHE_ENIGMA_ENTITY_PROP_KEY = "enigma_entity:%s:%s:%s";

    /**
     * 生成一个标准key
     * @param kind 种类
     * @param fix 值
     * @return
     */
    public static final String makeCacheKey(String kind,String fix){
        return String.format(kind,fix);
    }

    /**
     * 生成一个标准key
     * @param kind 种类
     * @param fix1 值
     * @param fix12 值
     * @return
     */
    public static final String makeCacheKey(String kind,String fix1,String fix12){
        return String.format(kind,fix1,fix12);
    }

    /**
     * 生成一个标准key
     * @param entity 实体
     * @param id 主键值
     * @return
     */
    public static final String makeEntityKeyCacheKey(String entity, Long id){
        return String.format(CACHE_ENIGMA_ENTITY_KEY,entity.toLowerCase(), id);
    }

    /**
     * 生成一个标准key
     * @param entity 实体
     * @param prop 属性
     * @param pk key值
     * @return
     */
    public static final String makeEntityPropKeyCacheKey(String entity, String prop, Object pk){
        return String.format(CACHE_ENIGMA_ENTITY_PROP_KEY,entity.toLowerCase(),prop, pk);
    }
}

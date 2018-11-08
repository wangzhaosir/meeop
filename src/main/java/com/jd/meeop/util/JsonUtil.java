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


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * JSON工具类
 *
 * @ClassName: JsonUtil
 * @Description:
 * @author: 冷水(Toby)
 * @date: 16-12-7 14:45
 * @mail: zhao.wang@aliyun.com
 * @version: v1.0
 * @since: JDK 1.7.0_79(Liunx)
 */
public class JsonUtil {

    /**
     * 静态类不可实例化
     */
    private JsonUtil() {
    }

    /**
     * 创建JSONObject
     *
     * @return JSONObject
     */
    public static JSONObject createObj() {
        return new JSONObject();
    }

    /**
     * 创建 JSONArray
     *
     * @return JSONArray
     */
    public static JSONArray createArray() {
        return new JSONArray();
    }

    /**
     * JSON字符串转JSONObject对象
     *
     * @param json JSON字符串
     * @return JSONObject
     */
    public static JSONObject parseObj(String json) {
        return JSON.parseObject(json);
    }

    /**
     * 将json字符串转化为JSON
     * 注意：
     * 转换时仅对bean与JSON中对应的key进行赋值，其他无法映射的值均为空
     *
     * @param json
     * @param targetClass
     * @return
     */
    public static Object parseObj(Object json, Class<?> targetClass) {
        if (null == json || json.toString().length() == 0) {
            return null;
        }
        try {
            return JSON.parseObject(json.toString(), targetClass);
        } catch (Exception ex) {
            throw new JSONException("Met error in converting json string to bean!Error:" + ex.getMessage());
        }
    }

    /**
     * JSON字符串转JSONArray
     *
     * @param json JSON字符串
     * @return JSONArray
     */
    public static JSONArray parseArray(String json) {
        return JSON.parseArray(json);
    }

    /**
     * 转换为JSON字符串
     *
     * @param obj 被转为JSON的对象
     * @return JSON字符串
     */
    public static String toJson(Object obj) {
        return toJsonPretty(obj, Boolean.FALSE);
    }

    /**
     * 转换为JSON字符串
     *
     * @param obj 被转为JSON的对象
     * @return JSON字符串
     */
    public static String beanToJson(Object obj) {
        return obj == null ? null : toJsonPretty(obj, Boolean.FALSE);
    }

    /**
     * 转换为格式化后的JSON字符串
     *
     * @param obj Bean对象
     * @return JSON字符串
     */
    public static String toJsonPretty(Object obj, boolean prettyFormat) {
        return JSON.toJSONString(obj, prettyFormat);
    }

    /**
     * 转换为JSON字符串
     *
     * @param obj      被转为JSON的对象
     * @param features 序列化
     * @return JSON字符串
     */
    public static String toJson(Object obj, SerializerFeature... features) {
        if (CollectionUtil.isEmpty(features))
            return toJson(obj);
        return JSON.toJSONString(obj, features);
    }

    /**
     * 转为实体类对象
     *
     * @param json  JSONObject
     * @param clazz 实体类对象
     * @return 实体类对象
     */
    public static <T> T toBean(JSONObject json, Class<T> clazz) {
        return toBean(json.toJSONString(), clazz);
    }

    /**
     * 转为实体类对象
     *
     * @param json  JSONObject
     * @param clazz 实体类对象
     * @return 实体类对象
     */
    public static <T> T toBean(String json, Class<T> clazz) {
        return null == json ? null : JSON.parseObject(json, clazz);
    }

    /**
     * 转为实体类对象
     *
     * @param json  JSONObject
     * @param clazz 实体类对象
     * @return 实体类对象
     */
    public static <T> T toBean(String json, Class<T> clazz, Feature... features) {
        return null == json ? null : JSON.parseObject(json, clazz,features);
    }

    /**
     * 转为实体类对象
     *
     * @param json  JSONObject
     * @param clazz 实体类对象
     * @return 实体类对象
     */
    public static List<?> toList(String json, Class<?> clazz) {
        return JSONArray.parseArray(json, clazz);
    }

    /**
     * Properties文件转化为JSONObject
     *
     * @param properties Setting文件
     * @return JSONObject
     */
    public static JSONObject parseFromProperties(Properties properties) {
        JSONObject jsonObject = new JSONObject();
        Set<Map.Entry<Object, Object>> entrySet = properties.entrySet();
        for (Map.Entry<Object, Object> entry : entrySet) {
            propertyPut(jsonObject, ConvertUtil.toStr(entry.getKey()), entry.getValue());
        }
        return jsonObject;
    }

    /**
     * 将Property的键转化为JSON形式<br>
     *
     * @param jsonObject JSONObject
     * @param key        键
     * @param value      值
     * @return JSONObject
     */
    private static JSONObject propertyPut(JSONObject jsonObject, Object key, Object value) {
        String keyStr = ConvertUtil.toStr(key);
        String[] path = StringUtil.split(keyStr, StringUtil.DOT);
        int last = path.length - 1;
        JSONObject target = jsonObject;
        for (int i = 0; i < last; i += 1) {
            String segment = path[i];
            JSONObject nextTarget = target.getJSONObject(segment);
            if (nextTarget == null) {
                nextTarget = new JSONObject();
                target.put(segment, nextTarget);
            }
            target = nextTarget;
        }
        target.put(path[last], value);
        return jsonObject;
    }
}

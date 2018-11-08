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

import java.io.*;

/**
 * 序列化工具
 * @ClassName: SerializationUtil
 * @Description:
 * @author: 冷水(Toby)
 * @date: 2017/3/18 17:26
 * @mail: zhao.wang@aliyun.com
 * @version: v1.0
 * @since: JDK 1.7.0_79(Liunx)
 */
public class SerializationUtil {
    private static final Logger logger = LoggerFactory.getLogger(SerializationUtil.class);
    private SerializationUtil(){
    }

    public static byte[] serialize(Object object) {
        if (object == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            oos.flush();
        }catch (IOException ex) {
            throw new IllegalArgumentException("Failed to serialize object of type: " + object.getClass(), ex);
        }finally {
            try {
                if(baos != null){
                    baos.close();
                }
                if(oos != null){
                    oos.close();
                }
            } catch (IOException e) {
                logger.error("close IOException", e);
            }
        }
        return baos.toByteArray();
    }


    public static Object deserialize(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
            return ois.readObject();
        }catch (IOException ex) {
            throw new IllegalArgumentException("Failed to deserialize object", ex);
        }catch (ClassNotFoundException ex) {
            throw new IllegalStateException("Failed to deserialize object type", ex);
        }finally {
            try {
                if(ois != null){
                    ois.close();
                }
            } catch (IOException e) {
                logger.error("close IOException", e);
            }
        }
    }
}

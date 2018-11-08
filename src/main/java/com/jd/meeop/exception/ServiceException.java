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
package com.jd.meeop.exception;

/**
 * @ClassName:
 * @Description:
 * @author: 冷水(Toby)
 * @date: 2017/1/18 08:57
 * @mail: zhao.wang@aliyun.com
 * @version: v1.0
 * @since: JDK 1.7.0_79(Liunx)
 */
public class ServiceException extends RuntimeException {
    private String code;
    private String message;

    public ServiceException(Throwable e) {
        super(e);
    }

    public ServiceException(String message) {
        super(message);
        this.message = message;
    }

    public ServiceException(String code, String message){
        super(message);
        this.code = code;
        this.message = message;
    }


    public ServiceException(String code, String message,Throwable e){
        super(e);
        this.code = code;
        this.message = message;
    }

    public ServiceException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

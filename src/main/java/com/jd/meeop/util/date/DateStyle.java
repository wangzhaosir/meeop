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
package com.jd.meeop.util.date;
/**
 * 日期类型
 * @ClassName: DateStyle
 * @Description:
 * 日期样式枚举
 * 
 * @author:	冷水(Toby)
 * @date: 2016年12月08日 16:46:36
 * @mail: zhao.wang@aliyun.com
 * @version: V1.0
 * @since: JDK 1.7.0_79(Liunx)
 *
 */
public enum DateStyle {
	YYYY_MM("yyyy-MM", false),  
    YYYY_MM_DD("yyyy-MM-dd", false),  
    YYYY_MM_DD_HH_MM("yyyy-MM-dd HH:mm", false),  
    YYYY_MM_DD_HH_MM_SS("yyyy-MM-dd HH:mm:ss", false),
    YYYY_MM_DD_HH_MM_SS_STAMP("yyyy-MM-dd HH.mm.ss", false),  
      
    YYYY_MM_EN("yyyy/MM", false),  
    YYYY_MM_DD_EN("yyyy/MM/dd", false),  
    YYYY_MM_DD_HH_MM_EN("yyyy/MM/dd HH:mm", false),  
    YYYY_MM_DD_HH_MM_SS_EN("yyyy/MM/dd HH:mm:ss", false),

    YYYY_MM_LINK("yyyyMM", false),
    YYYY_MM_DD_LINK("yyyyMMdd", false),
    YYYY_MM_DD_HH_MM_LINK("yyyMMddHHmm", false),
    YYYY_MM_DD_HH_MM_SS_LINK("yyyyMMddHHmmss", false),
    YYYY_MM_DD_HH_MM_SS_SSS_LINK("yyyyMMddHHmmssSSS", false),

    YYYY_MM_CN("yyyy年MM月", false),  
    YYYY_MM_DD_CN("yyyy年MM月dd日", false),  
    YYYY_MM_DD_HH_MM_CN("yyyy年MM月dd日 HH:mm", false),  
    YYYY_MM_DD_HH_MM_SS_CN("yyyy年MM月dd日 HH:mm:ss", false),  
      
    HH_MM("HH:mm", true),  
    HH_MM_SS("HH:mm:ss", true),  
      
    MM_DD("MM-dd", true),  
    MM_DD_HH_MM("MM-dd HH:mm", true),  
    MM_DD_HH_MM_SS("MM-dd HH:mm:ss", true),  
      
    MM_DD_EN("MM/dd", true),  
    MM_DD_HH_MM_EN("MM/dd HH:mm", true),  
    MM_DD_HH_MM_SS_EN("MM/dd HH:mm:ss", true),  
      
    MM_DD_CN("MM月dd日", true),  
    MM_DD_HH_MM_CN("MM月dd日 HH:mm", true),  
    MM_DD_HH_MM_SS_CN("MM月dd日 HH:mm:ss", true);  
      
    private String value;  
      
    private boolean isShowOnly;  
      
    DateStyle(String value, boolean isShowOnly) {  
        this.value = value;  
        this.isShowOnly = isShowOnly;  
    }  
      
    public String getValue() {  
        return value;  
    }  
      
    public boolean isShowOnly() {  
        return isShowOnly;  
    }  
}


/*
* Copyright 2004-2017 JD.com Group.
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
package com.jd.meeop.test;

import com.jd.meeop.util.DateUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日期工具类（测试）
* @ClassName:DateUtilTest
* @Description:
* @author: 冷水(Toby)
* @date: 8/11/2018 3:16 PM
* @mail: zhao.wang@aliyun.com
* @version: v1.0
* @since: JDK 1.8.0_131(MacOS)
*/
public class DateUtilTest {
    private static final Logger logger = LoggerFactory.getLogger(DateUtilTest.class);
    @Test
    public void getNow(){
        logger.info(DateUtil.now());
    }
}

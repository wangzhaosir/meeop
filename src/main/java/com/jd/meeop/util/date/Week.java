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
 * 星期枚举类
 * @ClassName: Week
 * @Description:
 *
 * @author:	冷水(Toby)
 * @date: 2016年12月08日 16:46:36
 * @mail: zhao.wang@aliyun.com
 * @version: V1.0
 * @since: JDK 1.7.0_79(Liunx)
 *
 */
public enum Week {
	MONDAY("星期一", "Monday", "Mon.", 1), TUESDAY("星期二", "Tuesday", "Tues.", 2), WEDNESDAY(
			"星期三", "Wednesday", "Wed.", 3), THURSDAY("星期四", "Thursday",
			"Thur.", 4), FRIDAY("星期五", "Friday", "Fri.", 5), SATURDAY("星期六",
			"Saturday", "Sat.", 6), SUNDAY("星期日", "Sunday", "Sun.", 7);

	String name_cn;
	String name_en;
	String name_enShort;
	int number;

	Week(String name_cn, String name_en, String name_enShort, int number) {
		this.name_cn = name_cn;
		this.name_en = name_en;
		this.name_enShort = name_enShort;
		this.number = number;
	}

	public String getChineseName() {
		return name_cn;
	}

	public String getName() {
		return name_en;
	}

	public String getShortName() {
		return name_enShort;
	}

	public int getNumber() {
		return number;
	}
}


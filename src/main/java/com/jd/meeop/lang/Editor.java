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
package com.jd.meeop.lang;

/**
 * 编辑器接口
 * @ClassName: Editor
 * @Description:
 * 常用于对于集合中的元素做统一编辑<br>
 * 此编辑器两个作用：<br>
 * 1、如果返回值为<code>null</code>，表示此值被抛弃
 * 2、对对象做修改
 * @author: 冷水(Toby)
 * @date: 16-12-11 14:45
 * @mail: zhao.wang@aliyun.com
 * @version: v1.0
 * @since: JDK 1.7.0_79(Liunx)
 */
public interface Editor<T> {
	/**
	 * 修改过滤后的结果
	 * @param t 被过滤的对象
	 * @return 修改后的对象，如果被过滤返回<code>null</code>
	 */
    T edit(T t);
}

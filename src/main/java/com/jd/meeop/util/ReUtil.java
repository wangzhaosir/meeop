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

import com.jd.meeop.lang.Validator;
import com.jd.meeop.lang.Holder;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则相关工具类
 * @ClassName: ReUtil
 * @Description:
 * @author: 冷水(Toby)
 * @date: 16-12-7 14:45
 * @mail: zhao.wang@aliyun.com
 * @version: v1.0
 * @since: JDK 1.7.0_79(Liunx)
 */
public class ReUtil {
	
	/** 正则表达式匹配中文 */
	public final static String RE_CHINESE = "[\u4E00-\u9FFF]";
	
	/** 分组 */
	public final static Pattern GROUP_VAR =  Pattern.compile("\\$(\\d+)");
	
	/** 正则中需要被转义的关键字 */
	public final static Set<Character> RE_KEYS = CollectionUtil.newHashSet('$', '(', ')', '*', '+', '.', '[', ']', '?', '\\', '^', '{', '}', '|');

	/**
	 * 静态类不可实例化
	 */
	private ReUtil() {}

	/**
	 * 获得匹配的字符串
	 * 
	 * @param regex 匹配的正则
	 * @param content 被匹配的内容
	 * @param groupIndex 匹配正则的分组序号
	 * @return 匹配后得到的字符串，未匹配返回null
	 */
	public static String get(String regex, String content, int groupIndex) {
		if(null == content || null == regex){
			return null;
		}
		
		Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
		return get(pattern, content, groupIndex);
	}
	
	/**
	 * 获得匹配的字符串
	 * 
	 * @param pattern 编译后的正则模式
	 * @param content 被匹配的内容
	 * @param groupIndex 匹配正则的分组序号
	 * @return 匹配后得到的字符串，未匹配返回null
	 */
	public static String get(Pattern pattern, String content, int groupIndex) {
		if(null == content || null == pattern){
			return null;
		}
		
		Matcher matcher = pattern.matcher(content);
		if (matcher.find()) {
			return matcher.group(groupIndex);
		}
		return null;
	}
	
	/**
	 * 从content中匹配出多个值并根据template生成新的字符串<br>
	 * 例如：<br>
	 * 		content		2013年5月
	 * 		pattern			(.*?)年(.*?)月
	 * 		template：	$1-$2
	 * 		return 			2013-5
	 * 
	 * @param pattern 匹配正则
	 * @param content 被匹配的内容
	 * @param template 生成内容模板，变量 $1 表示group1的内容，以此类推
	 * @return 新字符串
	 */
	public static String extractMulti(Pattern pattern, String content, String template) {
		if(null == content || null == pattern || null == template){
			return null;
		}
		
		HashSet<String> varNums = findAll(GROUP_VAR, template, 1, new HashSet<String>());
		
		Matcher matcher = pattern.matcher(content);
		if (matcher.find()) {
			for (String var : varNums) {
				int group = Integer.parseInt(var);
				template = template.replace("$" + var, matcher.group(group));
			}
			return template;
		}
		return null;
	}
	
	/**
	 * 从content中匹配出多个值并根据template生成新的字符串<br>
	 * 匹配结束后会删除匹配内容之前的内容（包括匹配内容）<br>
	 * 例如：<br>
	 * 		content		2013年5月
	 * 		pattern			(.*?)年(.*?)月
	 * 		template：	$1-$2
	 * 		return 			2013-5
	 * 
	 * @param regex 匹配正则字符串
	 * @param content 被匹配的内容
	 * @param template 生成内容模板，变量 $1 表示group1的内容，以此类推
	 * @return 按照template拼接后的字符串
	 */
	public static String extractMulti(String regex, String content, String template) {
		if(null == content || null == regex || null == template){
			return null;
		}
		
		Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
		return extractMulti(pattern, content, template);
	}
	
	/**
	 * 从content中匹配出多个值并根据template生成新的字符串<br>
	 * 匹配结束后会删除匹配内容之前的内容（包括匹配内容）<br>
	 * 例如：<br>
	 * 		content		2013年5月
	 * 		pattern			(.*?)年(.*?)月
	 * 		template：	$1-$2
	 * 		return 			2013-5
	 * 
	 * @param pattern 匹配正则
	 * @param contentHolder 被匹配的内容的Holder，value为内容正文，经过这个方法的原文将被去掉匹配之前的内容
	 * @param template 生成内容模板，变量 $1 表示group1的内容，以此类推
	 * @return 新字符串
	 */
	public static String extractMultiAndDelPre(Pattern pattern, Holder<String> contentHolder, String template) {
		if(null == contentHolder || null == pattern || null == template){
			return null;
		}
		
		HashSet<String> varNums = findAll(GROUP_VAR, template, 1, new HashSet<String>());
		
		final String content = contentHolder.value;
		Matcher matcher = pattern.matcher(content);
		if (matcher.find()) {
			for (String var : varNums) {
				int group = Integer.parseInt(var);
				template = template.replace("$" + var, matcher.group(group));
			}
			contentHolder.value = StringUtil.sub(content, matcher.end(), content.length());
			return template;
		}
		return null;
	}
	
	/**
	 * 从content中匹配出多个值并根据template生成新的字符串<br>
	 * 例如：<br>
	 * 		content		2013年5月
	 * 		pattern			(.*?)年(.*?)月
	 * 		template：	$1-$2
	 * 		return 			2013-5
	 * 
	 * @param regex 匹配正则字符串
	 * @param contentHolder 被匹配的内容的Holder，value为内容正文，经过这个方法的原文将被去掉匹配之前的内容
	 * @param template 生成内容模板，变量 $1 表示group1的内容，以此类推
	 * @return 按照template拼接后的字符串
	 */
	public static String extractMultiAndDelPre(String regex, Holder<String> contentHolder, String template) {
		if(null == contentHolder || null == regex || null == template){
			return null;
		}
		
		final Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
		return extractMultiAndDelPre(pattern, contentHolder, template);
	}

	/**
	 * 删除匹配的内容
	 * 
	 * @param regex 正则
	 * @param content 被匹配的内容
	 * @return 删除后剩余的内容
	 */
	public static String delFirst(String regex, String content) {
		if(null == content || null == regex){
			return content;
		}
		
		return content.replaceFirst(regex, "");
	}

	/**
	 * 删除正则匹配到的内容之前的字符 如果没有找到，则返回原文
	 * 
	 * @param regex 定位正则
	 * @param content 被查找的内容
	 * @return 删除前缀后的新内容
	 */
	public static String delPre(String regex, String content) {
		if(null == content || null == regex){
			return content;
		}
		
		Matcher matcher = Pattern.compile(regex, Pattern.DOTALL).matcher(content);
		if (matcher.find()) {
			return StringUtil.sub(content, matcher.end(), content.length());
		}
		return content;
	}

	/**
	 * 取得内容中匹配的所有结果
	 * 
	 * @param regex 正则
	 * @param content 被查找的内容
	 * @param group 正则的分组
	 * @param collection 返回的集合类型
	 * @return 结果集
	 */
	public static <T extends Collection<String>> T findAll(String regex, String content, int group, T collection) {
		if(null == regex){
			return null;
		}
		
		Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
		return findAll(pattern, content, group, collection);
	}
	
	/**
	 * 取得内容中匹配的所有结果
	 * 
	 * @param pattern 编译后的正则模式
	 * @param content 被查找的内容
	 * @param group 正则的分组
	 * @param collection 返回的集合类型
	 * @return 结果集
	 */
	public static <T extends Collection<String>> T findAll(Pattern pattern, String content, int group, T collection) {
		if(null == pattern || null == content){
			return null;
		}
		
		if(null == collection){
			throw new NullPointerException("Null collection param provided!");
		}
		
		Matcher matcher = pattern.matcher(content);
		while(matcher.find()){
			collection.add(matcher.group(group));
		}
		return collection;
	}
	
	/**
	 * 计算指定字符串中，匹配pattern的个数
	 * @param regex 正则表达式
	 * @param content 被查找的内容
	 * @return 匹配个数
	 */
	public static int count(String regex, String content){
		if(null == regex){
			return 0;
		}
		
		Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
		return count(pattern, content);
	}
	
	/**
	 * 计算指定字符串中，匹配pattern的个数
	 * @param pattern 编译后的正则模式
	 * @param content 被查找的内容
	 * @return 匹配个数
	 */
	public static int count(Pattern pattern, String content){
		if(null == pattern || null == content){
			return 0;
		}
		
		int count = 0;
		Matcher matcher = pattern.matcher(content);
		while(matcher.find()){
			count++;
		}
		
		return count;
	}

	/**
	 * 从字符串中获得第一个整数
	 * 
	 * @param StringWithNumber 带数字的字符串
	 * @return 整数
	 */
	public static Integer getFirstNumber(String StringWithNumber) {
		return ConvertUtil.toInt(get(Validator.NUMBERS, StringWithNumber, 0), null);
	}
	
	/**
	 * 给定内容是否匹配正则
	 * @param regex 正则
	 * @param content 内容
	 * @return 正则为null或者""则不检查，返回true，内容为null返回false
	 */
	public static boolean isMatch(String regex, String content) {
		if(content == null) {
			//提供null的字符串为不匹配
			return false;
		}
		
		if(StringUtil.isEmpty(regex)) {
			//正则不存在则为全匹配
			return true;
		}
		
		return Pattern.matches(regex, content);
	}
	
	/**
	 * 给定内容是否匹配正则
	 * @param pattern 模式  
	 * @param content 内容
	 * @return 正则为null或者""则不检查，返回true，内容为null返回false
	 */
	public static boolean isMatch(Pattern pattern, String content) {
		if(content == null || pattern == null) {
			//提供null的字符串为不匹配
			return false;
		}
		return pattern.matcher(content).matches();
	}
	
	/**
	 * 正则替换指定值<br>
	 * 通过正则查找到字符串，然后把匹配到的字符串加入到replacementTemplate中，$1表示分组1的字符串
	 * @param content 文本
	 * @param regex 正则
	 * @param replacementTemplate 替换的文本模板，可以使用$1类似的变量提取正则匹配出的内容
	 * @return 处理后的文本
	 */
	public static String replaceAll(String content, String regex, String replacementTemplate) {
		if(StringUtil.isEmpty(content)){
			return content;
		}
		
		final Matcher matcher = Pattern.compile(regex, Pattern.DOTALL).matcher(content);
		matcher.reset();
		boolean result = matcher.find();
		if (result) {
			final Set<String> varNums = findAll(GROUP_VAR, replacementTemplate, 1, new HashSet<String>());
			final StringBuffer sb = new StringBuffer();
			do {
				String replacement = replacementTemplate;
				for (String var : varNums) {
					int group = Integer.parseInt(var);
					replacement = replacement.replace("$" + var, matcher.group(group));
				}
				matcher.appendReplacement(sb, escape(replacement));
				result = matcher.find();
			} while (result);
			matcher.appendTail(sb);
			return sb.toString();
		}
		return content;
	}
	
	/**
	 * 转义字符串，将正则的关键字转义
	 * @param content 文本
	 * @return 转义后的文本
	 */
	public static String escape(String content) {
		if(StringUtil.isBlank(content)){
			return content;
		}
		
		final StringBuilder builder = new StringBuilder();
		char current;
		for(int i = 0; i < content.length(); i++) {
			current = content.charAt(i);
			if(RE_KEYS.contains(current)) {
				builder.append('\\');
			}
			builder.append(current);
		}
		return builder.toString();
	}
}
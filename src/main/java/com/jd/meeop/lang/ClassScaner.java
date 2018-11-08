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

import com.jd.meeop.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 类扫描器
 * @ClassName: ClassScaner
 * @Description:
 * @author: 冷水(Toby)
 * @date: 16-12-11 14:45
 * @mail: zhao.wang@aliyun.com
 * @version: v1.0
 * @since: JDK 1.7.0_79(Liunx)
 */
public class ClassScaner {
	private static Logger logger = LoggerFactory.getLogger(ClassScaner.class);
	
	/**
	 * 扫描指定包路径下所有包含指定注解的类
	 * 
	 * @param packageName 包路径
	 * @param annotationClass 注解类
	 * @return 类集合
	 */
	public static Set<Class<?>> scanPackageByAnnotation(String packageName, final Class<? extends Annotation> annotationClass) {
		return scanPackage(packageName, new Filter<Class<?>>(){
			@Override
			public boolean accept(Class<?> clazz) {
				return clazz.isAnnotationPresent(annotationClass);
			}
		});
	}

	/**
	 * 扫描指定包路径下所有指定类或接口的子类或实现类
	 * 
	 * @param packageName 包路径
	 * @param superClass 父类或接口
	 * @return 类集合
	 */
	public static Set<Class<?>> scanPackageBySuper(String packageName, final Class<?> superClass) {
		return scanPackage(packageName, new Filter<Class<?>>(){
			@Override
			public boolean accept(Class<?> clazz) {
				return superClass.isAssignableFrom(clazz) && !superClass.equals(clazz);
			}
		});
	}
	
	/**
	 * 扫面该包路径下所有class文件
	 * 
	 * @return 类集合
	 */
	public static Set<Class<?>> scanPackage() {
		return scanPackage(StringUtil.EMPTY, null);
	}

	/**
	 * 扫面该包路径下所有class文件
	 * 
	 * @param packageName 包路径 com | com. | com.abs | com.abs.
	 * @return 类集合
	 */
	public static Set<Class<?>> scanPackage(String packageName) {
		return scanPackage(packageName, null);
	}

	/**
	 * 扫面包路径下满足class过滤器条件的所有class文件，</br>
	 * 如果包路径为 com.abs + A.class 但是输入 abs会产生classNotFoundException</br>
	 * 因为className 应该为 com.abs.A 现在却成为abs.A,此工具类对该异常进行忽略处理,有可能是一个不完善的地方，以后需要进行修改</br>
	 * 
	 * @param packageName 包路径 com | com. | com.abs | com.abs.
	 * @param classFilter class过滤器，过滤掉不需要的class
	 * @return 类集合
	 */
	public static Set<Class<?>> scanPackage(String packageName,  Filter<Class<?>> classFilter) {
		if (StringUtil.isBlank(packageName)) {
			packageName = StringUtil.EMPTY;
		}
		logger.debug("Scan classes from package [{}]...", packageName);
		packageName = getWellFormedPackageName(packageName);

		final Set<Class<?>> classes = new HashSet<Class<?>>();
		final Set<String> classPaths = ClassUtil.getClassPaths(packageName);
		for (String classPath : classPaths) {
			// bug修复，由于路径中空格和中文导致的Jar找不到
			classPath = URLUtil.decode(classPath, CharsetUtil.systemCharset());

			logger.debug("Scan classpath: [{}]", classPath);
			// 填充 classes
			fillClasses(classPath, packageName, classFilter, classes);
		}

		// 如果在项目的ClassPath中未找到，去系统定义的ClassPath里找
		if (classes.isEmpty()) {
			final String[] javaClassPaths = ClassUtil.getJavaClassPaths();
			for (String classPath : javaClassPaths) {
				// bug修复，由于路径中空格和中文导致的Jar找不到
				classPath = URLUtil.decode(classPath, CharsetUtil.systemCharset());

				logger.debug("Scan java classpath: [{}]", classPath);
				// 填充 classes
				fillClasses(classPath, new File(classPath), packageName, classFilter, classes);
			}
		}
		return classes;
	}
	
	// --------------------------------------------------------------------------------------------------- Private method start
	/**
	 * 改变 com -> com. 避免在比较的时候把比如 completeTestSuite.class类扫描进去，如果没有"."</br>
	 * 那class里面com开头的class类也会被扫描进去,其实名称后面或前面需要一个 ".",来添加包的特征
	 * 
	 * @param packageName
	 * @return 格式化后的包名
	 */
	private static String getWellFormedPackageName(String packageName) {
		return packageName.lastIndexOf(StringUtil.DOT) != packageName.length() - 1 ? packageName + StringUtil.DOT : packageName;
	}

	/**
	 * 填充满足条件的class 填充到 classes<br>
	 * 同时会判断给定的路径是否为Jar包内的路径，如果是，则扫描此Jar包
	 * 
	 * @param path Class文件路径或者所在目录Jar包路径
	 * @param packageName 需要扫面的包名
	 * @param classFilter class过滤器
	 * @param classes List 集合
	 */
	private static void fillClasses(String path, String packageName, Filter<Class<?>> classFilter, Set<Class<?>> classes) {
		// 判定给定的路径是否为Jar
		int index = path.lastIndexOf(FileUtil.JAR_PATH_EXT);
		if (index != -1) {
			// Jar文件
			path = path.substring(0, index + FileUtil.JAR_FILE_EXT.length()); // 截取jar路径
			path = StringUtil.removePrefix(path, FileUtil.PATH_FILE_PRE); // 去掉文件前缀
			processJarFile(new File(path), packageName, classFilter, classes);
		} else {
			fillClasses(path, new File(path), packageName, classFilter, classes);
		}
	}

	/**
	 * 填充满足条件的class 填充到 classes
	 * 
	 * @param classPath 类文件所在目录，当包名为空时使用此参数，用于截掉类名前面的文件路径
	 * @param file Class文件或者所在目录Jar包文件
	 * @param packageName 需要扫面的包名
	 * @param classFilter class过滤器
	 * @param classes List 集合
	 */
	private static void fillClasses(String classPath, File file, String packageName, Filter<Class<?>> classFilter, Set<Class<?>> classes) {
		if (file.isDirectory()) {
			processDirectory(classPath, file, packageName, classFilter, classes);
		} else if (isClassFile(file)) {
			processClassFile(classPath, file, packageName, classFilter, classes);
		} else if (isJarFile(file)) {
			processJarFile(file, packageName, classFilter, classes);
		}
	}

	/**
	 * 处理如果为目录的情况,需要递归调用 fillClasses方法
	 * 
	 * @param directory 目录
	 * @param packageName 包名
	 * @param classFilter 类过滤器
	 * @param classes 类集合
	 */
	private static void processDirectory(String classPath, File directory, String packageName, Filter<Class<?>> classFilter, Set<Class<?>> classes) {
		for (File file : directory.listFiles(fileFilter)) {
			fillClasses(classPath, file, packageName, classFilter, classes);
		}
	}

	/**
	 * 处理为class文件的情况,填充满足条件的class 到 classes
	 * 
	 * @param classPath 类文件所在目录，当包名为空时使用此参数，用于截掉类名前面的文件路径
	 * @param file class文件
	 * @param packageName 包名
	 * @param classFilter 类过滤器
	 * @param classes 类集合
	 */
	private static void processClassFile(String classPath, File file, String packageName, Filter<Class<?>> classFilter, Set<Class<?>> classes) {
		if (false == classPath.endsWith(File.separator)) {
			classPath += File.separator;
		}
		String path = file.getAbsolutePath();
		if (StringUtil.isEmpty(packageName)) {
			path = StringUtil.removePrefix(path, classPath);
		}
		final String filePathWithDot = path.replace(File.separator, StringUtil.DOT);

		int subIndex = -1;
		if ((subIndex = filePathWithDot.indexOf(packageName)) != -1) {
			final int endIndex = filePathWithDot.lastIndexOf(FileUtil.CLASS_EXT);

			final String className = filePathWithDot.substring(subIndex, endIndex);
			fillClass(className, packageName, classes, classFilter);
		}
	}

	/**
	 * 处理为jar文件的情况，填充满足条件的class 到 classes
	 * 
	 * @param file jar文件
	 * @param packageName 包名
	 * @param classFilter 类过滤器
	 * @param classes 类集合
	 */
	private static void processJarFile(File file, String packageName, Filter<Class<?>> classFilter, Set<Class<?>> classes) {
		try {
			for (JarEntry entry : Collections.list(new JarFile(file).entries())) {
				if (isClass(entry.getName())) {
					final String className = entry.getName().replace(StringUtil.SLASH, StringUtil.DOT).replace(FileUtil.CLASS_EXT, StringUtil.EMPTY);
					fillClass(className, packageName, classes, classFilter);
				}
			}
		} catch (Exception ex) {
			logger.error("exception", ex);
		}
	}

	/**
	 * 填充class 到 classes
	 * 
	 * @param className 类名
	 * @param packageName 包名
	 * @param classes 类集合
	 * @param classFilter 类过滤器
	 */
	private static void fillClass(String className, String packageName, Set<Class<?>> classes, Filter<Class<?>> classFilter) {
		if (className.startsWith(packageName)) {
			try {
				final Class<?> clazz = Class.forName(className, false, ClassUtil.getClassLoader());
				if (classFilter == null || classFilter.accept(clazz)) {
					classes.add(clazz);
				}
			} catch (Exception ex) {
				// Pass Load Error.
			}
		}
	}
	
	/**
	 * 文件过滤器，过滤掉不需要的文件<br>
	 * 只保留Class文件、目录和Jar
	 */
	private static FileFilter fileFilter = new FileFilter(){
		@Override
		public boolean accept(File pathname) {
			return isClass(pathname.getName()) || pathname.isDirectory() || isJarFile(pathname);
		}
	};

	/**
	 * @param file 文件
	 * @return 是否为类文件
	 */
	private static boolean isClassFile(File file) {
		return isClass(file.getName());
	}
	
	/**
	 * @param fileName 文件名
	 * @return 是否为类文件
	 */
	private static boolean isClass(String fileName) {
		return fileName.endsWith(FileUtil.CLASS_EXT);
	}

	/**
	 * @param file 文件
	 * @return是否为Jar文件
	 */
	private static boolean isJarFile(File file) {
		return file.getName().endsWith(FileUtil.JAR_FILE_EXT);
	}
	// --------------------------------------------------------------------------------------------------- Private method end
}

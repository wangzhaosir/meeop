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

import com.jd.meeop.lang.Filter;
import com.jd.meeop.lang.Singleton;
import com.jd.meeop.util.convert.BasicType;
import com.jd.meeop.exception.UtilException;
import com.jd.meeop.lang.ClassScaner;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 类工具类
 * @ClassName: ClassUtil
 * @Description:
 * 扫描指定包下的所有类
 *
 * @author: 冷水(Toby)
 * @date: 16-12-7 14:45
 * @mail: zhao.wang@aliyun.com
 * @version: v1.0
 * @since: JDK 1.7.0_79(Liunx)
 */
public class ClassUtil {
	private static final ConstructorCache CONSTRUCTOR_CACHE = new ConstructorCache();

	/**
	 * 静态类不可实例化
	 */
	private ClassUtil() {
	}

	/**
	 * 获得对象数组的类数组
	 *
	 * @param objects 对象数组
	 * @return 类数组
	 */
	public static Class<?>[] getClasses(Object... objects) {
		Class<?>[] classes = new Class<?>[objects.length];
		for (int i = 0; i < objects.length; i++) {
			classes[i] = objects[i].getClass();
		}
		return classes;
	}

	// ----------------------------------------------------------------------------------------- Scan classes
	/**
	 * 扫描指定包路径下所有包含指定注解的类
	 *
	 * @param packageName 包路径
	 * @param annotationClass 注解类
	 * @return 类集合
	 * @see ClassScaner#scanPackageByAnnotation(String, Class)
	 */
	public static Set<Class<?>> scanPackageByAnnotation(String packageName, final Class<? extends Annotation> annotationClass) {
		return ClassScaner.scanPackageByAnnotation(packageName, annotationClass);
	}

	/**
	 * 扫描指定包路径下所有指定类或接口的子类或实现类
	 *
	 * @param packageName 包路径
	 * @param superClass 父类或接口
	 * @return 类集合
	 * @see ClassScaner#scanPackageBySuper(String, Class)
	 */
	public static Set<Class<?>> scanPackageBySuper(String packageName, final Class<?> superClass) {
		return ClassScaner.scanPackageBySuper(packageName, superClass);
	}

	/**
	 * 扫面该包路径下所有class文件
	 *
	 * @return 类集合
	 * @see ClassScaner#scanPackage()
	 */
	public static Set<Class<?>> scanPackage() {
		return ClassScaner.scanPackage();
	}

	/**
	 * 扫面该包路径下所有class文件
	 *
	 * @param packageName 包路径 com | com. | com.abs | com.abs.
	 * @return 类集合
	 * @see ClassScaner#scanPackage(String)
	 */
	public static Set<Class<?>> scanPackage(String packageName) {
		return ClassScaner.scanPackage(packageName);
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
	public static Set<Class<?>> scanPackage(String packageName, Filter<Class<?>> classFilter) {
		return ClassScaner.scanPackage(packageName, classFilter);
	}

	// ----------------------------------------------------------------------------------------- Method
	/**
	 * 获得指定类中的Public方法名<br>
	 * 去重重载的方法
	 *
	 * @param clazz 类
	 */
	public static Set<String> getPublicMethodNames(Class<?> clazz) {
		HashSet<String> methodSet = new HashSet<String>();
		Method[] methodArray = getPublicMethods(clazz);
		for (Method method : methodArray) {
			String methodName = method.getName();
			methodSet.add(methodName);
		}
		return methodSet;
	}

	/**
	 * 获得本类及其父类所有Public方法
	 *
	 * @param clazz 查找方法的类
	 * @return 过滤后的方法列表
	 */
	public static Method[] getPublicMethods(Class<?> clazz) {
		return clazz.getMethods();
	}

	/**
	 * 获得指定类过滤后的Public方法列表
	 *
	 * @param clazz 查找方法的类
	 * @param filter 过滤器
	 * @return 过滤后的方法列表
	 */
	public static List<Method> getPublicMethods(Class<?> clazz, Filter<Method> filter) {
		if (null == clazz) {
			return null;
		}

		Method[] methods = getPublicMethods(clazz);
		List<Method> methodList;
		if (null != filter) {
			methodList = new ArrayList<Method>();
			for (Method method : methods) {
				if (filter.accept(method)) {
					methodList.add(method);
				}
			}
		} else {
			methodList = CollectionUtil.newArrayList(methods);
		}
		return methodList;
	}

	/**
	 * 获得指定类过滤后的Public方法列表
	 *
	 * @param clazz 查找方法的类
	 * @param excludeMethods 不包括的方法
	 * @return 过滤后的方法列表
	 */
	public static List<Method> getPublicMethods(Class<?> clazz, Method... excludeMethods) {
		final HashSet<Method> excludeMethodSet = CollectionUtil.newHashSet(excludeMethods);
		return getPublicMethods(clazz, new Filter<Method>(){
			@Override
			public boolean accept(Method method) {
				return false == excludeMethodSet.contains(method);
			}
		});
	}

	/**
	 * 获得指定类过滤后的Public方法列表
	 *
	 * @param clazz 查找方法的类
	 * @param excludeMethodNames 不包括的方法名列表
	 * @return 过滤后的方法列表
	 */
	public static List<Method> getPublicMethods(Class<?> clazz, String... excludeMethodNames) {
		final HashSet<String> excludeMethodNameSet = CollectionUtil.newHashSet(excludeMethodNames);
		return getPublicMethods(clazz, new Filter<Method>(){
			@Override
			public boolean accept(Method method) {
				return false == excludeMethodNameSet.contains(method.getName());
			}
		});
	}

	/**
	 * 查找指定Public方法
	 *
	 * @param clazz 类
	 * @param methodName 方法名
	 * @param paramTypes 参数类型
	 * @return 方法
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	public static Method getPublicMethod(Class<?> clazz, String methodName, Class<?>... paramTypes) throws NoSuchMethodException, SecurityException {
		try {
			return clazz.getMethod(methodName, paramTypes);
		} catch (NoSuchMethodException ex) {
			return getDeclaredMethod(clazz, methodName, paramTypes);
		}
	}

	/**
	 * 获得指定类中的Public方法名<br>
	 * 去重重载的方法
	 *
	 * @param clazz 类
	 */
	public static Set<String> getDeclaredMethodNames(Class<?> clazz) {
		HashSet<String> methodSet = new HashSet<String>();
		Method[] methodArray = getDeclaredMethods(clazz);
		for (Method method : methodArray) {
			String methodName = method.getName();
			methodSet.add(methodName);
		}
		return methodSet;
	}

	/**
	 * 获得声明的所有方法，包括本类及其父类和接口的所有方法和Object类的方法
	 *
	 * @param clazz 类
	 * @return 方法数组
	 */
	public static Method[] getDeclaredMethods(Class<?> clazz) {
		Set<Method> methodSet = new HashSet<Method>();
		Method[] declaredMethods;
		for (; null != clazz; clazz = clazz.getSuperclass()) {
			declaredMethods = clazz.getDeclaredMethods();
			for (Method method : declaredMethods) {
				methodSet.add(method);
			}
		}
		return methodSet.toArray(new Method[methodSet.size()]);
	}

	/**
	 * 查找指定对象中的所有方法（包括非public方法），也包括父对象和Object类的方法
	 *
	 * @param obj 被查找的对象
	 * @param methodName 方法名
	 * @param args 参数
	 * @return 方法
	 * @throws NoSuchMethodException 无此方法
	 * @throws SecurityException
	 */
	public static Method getDeclaredMethod(Object obj, String methodName, Object... args) throws NoSuchMethodException, SecurityException {
		return getDeclaredMethod(obj.getClass(), methodName, getClasses(args));
	}

	/**
	 * 查找指定类中的所有方法（包括非public方法），也包括父类和Object类的方法
	 *
	 * @param clazz 被查找的类
	 * @param methodName 方法名
	 * @param parameterTypes 参数类型
	 * @return 方法
	 * @throws NoSuchMethodException 无此方法
	 * @throws SecurityException
	 */
	public static Method getDeclaredMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException, SecurityException {
		Method method = null;
		for (; null != clazz; clazz = clazz.getSuperclass()) {
			try {
				method = clazz.getDeclaredMethod(methodName, parameterTypes);
				break;
			} catch (NoSuchMethodException e) {
				// 继续向上寻找
			}
		}
		return method;
	}

	/**
	 * 是否为equals方法
	 *
	 * @param method 方法
	 * @return 是否为equals方法
	 */
	public static boolean isEqualsMethod(Method method) {
		if (method == null || !method.getName().equals("equals")) {
			return false;
		}
		Class<?>[] paramTypes = method.getParameterTypes();
		return (paramTypes.length == 1 && paramTypes[0] == Object.class);
	}

	/**
	 * 是否为hashCode方法
	 *
	 * @param method 方法
	 * @return 是否为hashCode方法
	 */
	public static boolean isHashCodeMethod(Method method) {
		return (method != null && method.getName().equals("hashCode") && method.getParameterTypes().length == 0);
	}

	/**
	 * 是否为toString方法
	 *
	 * @param method 方法
	 * @return 是否为toString方法
	 */
	public static boolean isToStringMethod(Method method) {
		return (method != null && method.getName().equals("toString") && method.getParameterTypes().length == 0);
	}

	// ----------------------------------------------------------------------------------------- Classpath
	/**
	 * 获得ClassPath
	 *
	 * @return ClassPath集合
	 */
	public static Set<String> getClassPathResources() {
		return getClassPaths(StringUtil.EMPTY);
	}

	/**
	 * 获得ClassPath
	 *
	 * @param packageName 包名称
	 * @return ClassPath路径字符串集合
	 */
	public static Set<String> getClassPaths(String packageName) {
		String packagePath = packageName.replace(StringUtil.DOT, StringUtil.SLASH);
		Enumeration<URL> resources;
		try {
			resources = getClassLoader().getResources(packagePath);
		} catch (IOException e) {
			throw new UtilException(StringUtil.format("Loading classPath [{}] error!", packagePath), e);
		}
		Set<String> paths = new HashSet<String>();
		while (resources.hasMoreElements()) {
			paths.add(resources.nextElement().getPath());
		}
		return paths;
	}

	/**
	 * 获得ClassPath
	 *
	 * @return ClassPath
	 */
	public static String getClassPath() {
		return getClassPathURL().getPath();
	}

	/**
	 * 获得ClassPath URL
	 *
	 * @return ClassPath URL
	 */
	public static URL getClassPathURL() {
		return getURL(StringUtil.EMPTY);
	}

	/**
	 * 获得资源的URL
	 *
	 * @param resource 资源（相对Classpath的路径）
	 * @return 资源URL
	 */
	public static URL getURL(String resource) {
		return ClassUtil.getClassLoader().getResource(resource);
	}

	/**
	 * @return 获得Java ClassPath路径，不包括 jre
	 */
	public static String[] getJavaClassPaths() {
		String[] classPaths = System.getProperty("java.class.path").split(System.getProperty("path.separator"));
		return classPaths;
	}

	/**
	 * @return 当前线程的class loader
	 */
	public static ClassLoader getContextClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}

	/**
	 * 获得class loader<br>
	 * 若当前线程class loader不存在，取当前类的class loader
	 *
	 * @return 类加载器
	 */
	public static ClassLoader getClassLoader() {
		ClassLoader classLoader = getContextClassLoader();
		if (classLoader == null) {
			classLoader = ClassUtil.class.getClassLoader();
		}
		return classLoader;
	}

	/**
	 * 实例化对象
	 *
	 * @param clazz 类名
	 * @return 对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> T newInstance(String clazz) {
		try {
			return (T) Class.forName(clazz).newInstance();
		} catch (Exception e) {
			throw new UtilException(StringUtil.format("Instance class [{}] error!", clazz), e);
		}
	}

	/**
	 * 实例化对象
	 *
	 * @param clazz 类
	 * @return 对象
	 */
	public static <T> T newInstance(Class<T> clazz) {
		try {
			return clazz.newInstance();
		} catch (Exception e) {
			throw new UtilException(StringUtil.format("Instance class [{}] error!", clazz), e);
		}
	}

	/**
	 * 实例化对象
	 *
	 * @param clazz 类
	 * @return 对象
	 */
	public static <T> T newInstance(Class<T> clazz, Object... params) {
		if (CollectionUtil.isEmpty(params)) {
			return newInstance(clazz);
		}

		final Class<?>[] paramTypes = getClasses(params);
		final Constructor<?> constructor = getConstructor(clazz, getClasses(params));
		if(null == constructor){
			throw new UtilException("No Constructor matched for parameter types: [{}]", new Object[]{paramTypes});
		}
		try {
			return getConstructor(clazz, paramTypes).newInstance(params);
		} catch (Exception e) {
			throw new UtilException(StringUtil.format("Instance class [{}] error!", clazz), e);
		}
	}

	/**
	 * 查找类中的指定参数的构造方法
	 * @param <T>
	 * @param clazz 类
	 * @param parameterTypes 参数类型，只要任何一个参数是指定参数的父类或接口或相等即可
	 * @return 构造方法，如果未找到返回null
	 */
	@SuppressWarnings("unchecked")
	public static <T> Constructor<T> getConstructor(Class<T> clazz, Class<?>... parameterTypes){
		if(null == clazz){
			return null;
		}

		final Constructor<?>[] constructors = clazz.getConstructors();
		Class<?>[] pts;
		for (Constructor<?> constructor : constructors) {
			pts = constructor.getParameterTypes();
			if(isAllAssignableFrom(pts, parameterTypes)){
				return (Constructor<T>) constructor;
			}
		}
		return null;
	}

	/**
	 * 比较判断types1和types2两组类，如果types1中所有的类都与types2对应位置的类相同，或者是其父类或接口，则返回<code>true</code>
	 * @param types1 类组1
	 * @param types2 类组2
	 * @return 是否相同、父类或接口
	 */
	public static boolean isAllAssignableFrom(Class<?>[] types1, Class<?>[] types2){
		if(CollectionUtil.isEmpty(types1) && CollectionUtil.isEmpty(types2)){
			return true;
		}
		if(types1.length == types2.length){
			for(int i = 0; i < types1.length; i++){
				if(false == types1[i].isAssignableFrom(types2[i])){
					return false;
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * 加载类
	 *
	 * @param <T>
	 * @param className 类名
	 * @param isInitialized 是否初始化
	 * @return 类
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> loadClass(String className, boolean isInitialized) {
		Class<T> clazz;
		try {
			clazz = (Class<T>) Class.forName(className, isInitialized, getClassLoader());
		} catch (ClassNotFoundException e) {
			throw new UtilException(e);
		}
		return clazz;
	}

	/**
	 * 加载类并初始化
	 *
	 * @param <T>
	 * @param className 类名
	 * @return 类
	 */
	public static <T> Class<T> loadClass(String className) {
		return loadClass(className, true);
	}

	/**
	 * 转换class
	 * @param className
	 * @return
	 */
	public static Class<?> forName(String className) {
		Class<?> clazz;
		try {
			clazz = (Class<?>) Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new UtilException(e);
		}
		return clazz;
	}


	/**
	 * 执行方法<br>
	 * 可执行Private方法，也可执行static方法<br>
	 * 执行非static方法时，必须满足对象有默认构造方法<br>
	 * 非单例模式，如果是非静态方法，每次创建一个新对象
	 *
	 * @param <T>
	 * @param classNameDotMethodName 类名和方法名表达式，例如：StringUtil.isEmpty
	 * @param args 参数，必须严格对应指定方法的参数类型和数量
	 * @return 返回结果
	 */
	public static <T> T invoke(String classNameDotMethodName, Object[] args) {
		return invoke(classNameDotMethodName, false, args);
	}

	/**
	 * 执行方法<br>
	 * 可执行Private方法，也可执行static方法<br>
	 * 执行非static方法时，必须满足对象有默认构造方法<br>
	 *
	 * @param <T>
	 * @param classNameDotMethodName 类名和方法名表达式，例如：StringUtil.isEmpty
	 * @param isSingleton 是否为单例对象，如果此参数为false，每次执行方法时创建一个新对象
	 * @param args 参数，必须严格对应指定方法的参数类型和数量
	 * @return 返回结果
	 */
	public static <T> T invoke(String classNameDotMethodName, boolean isSingleton, Object[] args) {
		if (StringUtil.isBlank(classNameDotMethodName)) {
			throw new UtilException("Blank classNameDotMethodName!");
		}
		final int dotIndex = classNameDotMethodName.lastIndexOf('.');
		if (dotIndex <= 0) {
			throw new UtilException("Invalid classNameDotMethodName [{}]!", classNameDotMethodName);
		}

		final String className = classNameDotMethodName.substring(0, dotIndex);
		final String methodName = classNameDotMethodName.substring(dotIndex + 1);

		return invoke(className, methodName, isSingleton, args);
	}

	/**
	 * 执行方法<br>
	 * 可执行Private方法，也可执行static方法<br>
	 * 执行非static方法时，必须满足对象有默认构造方法<br>
	 * 非单例模式，如果是非静态方法，每次创建一个新对象
	 *
	 * @param <T>
	 * @param className 类名，完整类路径
	 * @param methodName 方法名
	 * @param args 参数，必须严格对应指定方法的参数类型和数量
	 * @return 返回结果
	 */
	public static <T> T invoke(String className, String methodName, Object[] args) {
		return invoke(className, methodName, false, args);
	}

	/**
	 * 执行方法<br>
	 * 可执行Private方法，也可执行static方法<br>
	 * 执行非static方法时，必须满足对象有默认构造方法<br>
	 *
	 * @param <T>
	 * @param className 类名，完整类路径
	 * @param methodName 方法名
	 * @param isSingleton 是否为单例对象，如果此参数为false，每次执行方法时创建一个新对象
	 * @param args 参数，必须严格对应指定方法的参数类型和数量
	 * @return 返回结果
	 */
	public static <T> T invoke(String className, String methodName, boolean isSingleton, Object[] args) {
		Class<Object> clazz = loadClass(className);
		try {
			return invoke(isSingleton ? Singleton.get(clazz) : clazz.newInstance(), methodName, args);
		} catch (Exception e) {
			throw new UtilException(e);
		}
	}

	/**
	 * 执行方法<br>
	 * 可执行Private方法，也可执行static方法<br>
	 *
	 * @param <T>
	 * @param obj 对象
	 * @param methodName 方法名
	 * @param args 参数，必须严格对应指定方法的参数类型和数量
	 * @return 返回结果
	 */
	public static <T> T invoke(Object obj, String methodName, Object[] args) {
		try {
			final Method method = getDeclaredMethod(obj, methodName, args);
			if(null == method){
				throw new NoSuchMethodException(StringUtil.format("No such method: [{}]", methodName));
			}
			return invoke(obj, method, args);
		} catch (UtilException e) {
			throw new UtilException(e.getMessage());
		}catch (Exception e) {
			throw new UtilException(e);
		}
	}

	/**
	 * 执行方法
	 *
	 * @param obj 对象
	 * @param method 方法（对象方法或static方法都可）
	 * @param args 参数对象
	 * @return 结果
	 * @throws UtilException IllegalAccessException and IllegalArgumentException
	 * @throws InvocationTargetException 目标方法执行异常
	 * @throws IllegalArgumentException 参数异常
	 */
	@SuppressWarnings("unchecked")
	public static <T> T invoke(Object obj, Method method, Object[] args) throws InvocationTargetException, IllegalArgumentException{
		if (false == method.isAccessible()) {
			method.setAccessible(true);
		}
		try {
			return (T) method.invoke(isStatic(method) ? null : obj, args);
		} catch (IllegalAccessException e) {
			throw new UtilException(e.getMessage());
		}catch (IllegalArgumentException ila){
			throw new UtilException(ila.getMessage());
		}catch (InvocationTargetException ite){
			throw new UtilException(ite.getTargetException().getMessage());
		}
	}
	// ---------------------------------------------------------------------------------------------------- Invoke end

	/**
	 * 是否为包装类型
	 *
	 * @param clazz 类
	 * @return 是否为包装类型
	 */
	public static boolean isPrimitiveWrapper(Class<?> clazz) {
		if (null == clazz) {
			return false;
		}
		return BasicType.wrapperPrimitiveMap.containsKey(clazz);
	}

	/**
	 * 是否为基本类型（包括包装类和原始类）
	 *
	 * @param clazz 类
	 * @return 是否为基本类型
	 */
	public static boolean isBasicType(Class<?> clazz) {
		if (null == clazz) {
			return false;
		}
		return (clazz.isPrimitive() || isPrimitiveWrapper(clazz));
	}

	/**
	 * 是否简单值类型或简单值类型的数组<br>
	 * 包括：原始类型,、String、other CharSequence, a Number, a Date, a URI, a URL, a Locale or a Class及其数组
	 *
	 * @param clazz 属性类
	 * @return 是否简单值类型或简单值类型的数组
	 */
	public static boolean isSimpleTypeOrArray(Class<?> clazz) {
		if (null == clazz) {
			return false;
		}
		return isSimpleValueType(clazz) || (clazz.isArray() && isSimpleValueType(clazz.getComponentType()));
	}

	/**
	 * 是否为简单值类型<br>
	 * 包括：原始类型,、String、other CharSequence, a Number, a Date, a URI, a URL, a Locale or a Class.
	 *
	 * @param clazz 类
	 * @return 是否为简单值类型
	 */
	public static boolean isSimpleValueType(Class<?> clazz) {
		return isBasicType(clazz) || clazz.isEnum() || CharSequence.class.isAssignableFrom(clazz) || Number.class.isAssignableFrom(clazz) || Date.class.isAssignableFrom(clazz) || clazz
				.equals(URI.class) || clazz.equals(URL.class) || clazz.equals(Locale.class) || clazz.equals(Class.class);
	}

	/**
	 * 检查目标类是否可以从原类转化<br>
	 * 转化包括：<br>
	 * 1、原类是对象，目标类型是原类型实现的接口<br>
	 * 2、目标类型是原类型的父类<br>
	 * 3、两者是原始类型或者包装类型（相互转换）
	 *
	 * @param targetType 目标类型
	 * @param sourceType 原类型
	 * @return 是否可转化
	 */
	public static boolean isAssignable(Class<?> targetType, Class<?> sourceType) {
		if (null == targetType || null == sourceType) {
			return false;
		}

		// 对象类型
		if (targetType.isAssignableFrom(sourceType)) {
			return true;
		}

		// 基本类型
		if (targetType.isPrimitive()) {
			// 原始类型
			Class<?> resolvedPrimitive = BasicType.wrapperPrimitiveMap.get(sourceType);
			if (resolvedPrimitive != null && targetType.equals(resolvedPrimitive)) {
				return true;
			}
		} else {
			// 包装类型
			Class<?> resolvedWrapper = BasicType.primitiveWrapperMap.get(sourceType);
			if (resolvedWrapper != null && targetType.isAssignableFrom(resolvedWrapper)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 指定类是否为Public
	 *
	 * @param clazz 类
	 * @return 是否为public
	 */
	public static boolean isPublic(Class<?> clazz) {
		if (null == clazz) {
			throw new NullPointerException("Class to provided is null.");
		}
		return Modifier.isPublic(clazz.getModifiers());
	}

	/**
	 * 指定方法是否为Public
	 *
	 * @param method 方法
	 * @return 是否为public
	 */
	public static boolean isPublic(Method method) {
		if (null == method) {
			throw new NullPointerException("Method to provided is null.");
		}
		return isPublic(method.getDeclaringClass());
	}

	/**
	 * 指定类是否为非public
	 *
	 * @param clazz 类
	 * @return 是否为非public
	 */
	public static boolean isNotPublic(Class<?> clazz) {
		return false == isPublic(clazz);
	}

	/**
	 * 指定方法是否为非public
	 *
	 * @param method 方法
	 * @return 是否为非public
	 */
	public static boolean isNotPublic(Method method) {
		return false == isPublic(method);
	}

	/**
	 * 是否为静态方法
	 *
	 * @param method 方法
	 * @return 是否为静态方法
	 */
	public static boolean isStatic(Method method) {
		return Modifier.isStatic(method.getModifiers());
	}

	/**
	 * 设置方法为可访问
	 *
	 * @param method 方法
	 * @return 方法
	 */
	public static Method setAccessible(Method method) {
		if (null != method && ClassUtil.isNotPublic(method)) {
			method.setAccessible(true);
		}
		return method;
	}

	/**
	 * 是否为抽象类
	 *
	 * @param clazz 类
	 * @return 是否为抽象类
	 */
	public static boolean isAbstract(Class<?> clazz) {
		return Modifier.isAbstract(clazz.getModifiers());
	}

	/**
	 * 是否为标准的类<br>
	 * 这个类必须：<br>
	 * 1、非接口 2、非抽象类 3、非Enum枚举 4、非数组 5、非注解 6、非原始类型（int, long等）
	 *
	 * @param clazz 类
	 * @return 是否为标准类
	 */
	public static boolean isNormalClass(Class<?> clazz) {
		return null != clazz && false == clazz.isInterface() && false == isAbstract(clazz) && false == clazz.isEnum() && false == clazz.isArray() && false == clazz.isAnnotation() && false == clazz
				.isSynthetic() && false == clazz.isPrimitive();
	}

	/**
	 * 获取实体名
	 * @param clazz
	 * @return
     */
	public static final String getEntityName(Class clazz){
		String[] clazzs = StringUtil.split(clazz.getName(),".");
		return clazzs[clazzs.length-1];
	}


	public static <T> T newInstance(ClassLoader classLoader, final String className) throws Exception {
		classLoader = classLoader == null ? ClassUtil.class.getClassLoader() : classLoader;
		Constructor<T> constructor = CONSTRUCTOR_CACHE.get(classLoader, className);
		if (constructor != null) {
			return constructor.newInstance();
		}
		Class<T> clazz = (Class<T>) loadClass(classLoader, className);
		return newInstance(clazz, classLoader, className);
	}

	public static Class<?> loadClass(final ClassLoader classLoader, final String className)
			throws ClassNotFoundException {

		ClassLoader theClassLoader = classLoader;
		if (theClassLoader == null) {
			theClassLoader = Thread.currentThread().getContextClassLoader();
		}

		if (theClassLoader != null) {
			try {
				return tryLoadClass(className, theClassLoader);
			} catch (ClassNotFoundException ignore) {
			}
		}
		return Class.forName(className);
	}

	private static Class<?> tryLoadClass(String className, ClassLoader classLoader)
			throws ClassNotFoundException {

		if (className.startsWith("[")) {
			return Class.forName(className, false, classLoader);
		} else {
			return classLoader.loadClass(className);
		}
	}

	public static <T> T newInstance(Class<T> clazz, ClassLoader classLoader, String className) throws Exception {
		final Constructor<T> constructor = clazz.getDeclaredConstructor();
		if (!constructor.isAccessible()) {
			constructor.setAccessible(true);
		}
		CONSTRUCTOR_CACHE.put(classLoader, className, constructor);
		return constructor.newInstance();
	}

	private static final class ConstructorCache {
		private final ConcurrentMap<ClassLoader, ConcurrentMap<String, WeakReference<Constructor>>> cache;

		private ConstructorCache() {
			cache = new ConcurrentHashMap<ClassLoader, ConcurrentMap<String, WeakReference<Constructor>>>();
		}

		private <T> Constructor put(ClassLoader classLoader, String className, Constructor<T> constructor) {
			ClassLoader cl = classLoader == null ? ClassUtil.class.getClassLoader() : classLoader;
			ConcurrentMap<String, WeakReference<Constructor>> innerCache = cache.get(cl);
			if (innerCache == null) {
				innerCache = new ConcurrentHashMap<String, WeakReference<Constructor>>(100);
				ConcurrentMap<String, WeakReference<Constructor>> old = cache.putIfAbsent(cl, innerCache);
				if (old != null) {
					innerCache = old;
				}
			}
			innerCache.put(className, new WeakReference<Constructor>(constructor));
			return constructor;
		}

		@SuppressWarnings("unchecked")
		public <T> Constructor<T> get(ClassLoader classLoader, String className) {
			ConcurrentMap<String, WeakReference<Constructor>> innerCache = cache.get(classLoader);
			if (innerCache == null) {
				return null;
			}
			WeakReference<Constructor> reference = innerCache.get(className);
			Constructor constructor = reference == null ? null : reference.get();
			if (reference != null && constructor == null) {
				innerCache.remove(className);
			}
			return (Constructor<T>) constructor;
		}
	}
}
package com.jd.meeop.helper;

import com.jd.meeop.util.CollectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * 重试辅助类
 * @ClassName: TryHelper
 * @Description:
 * 可以对选择的方法进行重试次数
 * @author: 冷水(Toby)
 * @date: 16-12-7 14:45
 * @mail: zhao.wang@aliyun.com
 * @version: v1.0
 * @since: JDK 1.7.0_79(Liunx)
 */
public class TryHelper {

	private final static Logger logger = LoggerFactory.getLogger(TryHelper.class);
	
	/**
	 * 对call请求重试
	 * @param retryCount
	 * @param call
	 * @return
	 * @throws Exception
	 */
	public static <T> T retry(int retryCount, final Callable<T> call) throws Exception {
		Exception exception = null;
		int retry=1;
		if(retryCount > 1) retry = retryCount;
		for (int i = 0; i < retry; i++) {
			try {
				return call.call();
			} catch (Exception e) {
				exception = e;
				logger.error((new StringBuilder()).append("[Retry call error]-- count ").append(i).toString(), e);
			}
			try {
				Thread.sleep(30L);
			} catch (InterruptedException e1) {
			}
		}
		throw exception;
	}
	
	/**
	 * 
	 * @param retryCount
	 * @param call
	 * @param needRetryThrowables 
	 * @return
	 * @throws Exception
	 */
	public static <T> T retry(int retryCount, final Callable<T> call, final List<Class<? extends Throwable>> needRetryThrowables) throws Exception {
		Exception exception = null;
		int retry=1;
		if(retryCount > 1) retry = retryCount;
		for (int i = 0; i < retry ; i++) {
			try {
				return call.call();
			} catch (Exception e) {
				boolean needRetry = false;
				if(CollectionUtil.isEmpty(needRetryThrowables)){
					needRetry=true;
				}else{
					for (Class<? extends Throwable> cls : needRetryThrowables) {
						if(cls.isInstance(e)){
							needRetry=true;
							break;
						}
					}
				}
				if (!needRetry) throw e;
				exception = e;
				logger.error((new StringBuilder()).append("[Retry call error]-- count").append(i).append("---needRetry--").append(needRetry)	.toString(), e);
			}
			try {
				Thread.sleep(30L);
			} catch (InterruptedException e1) {
			}
		}
		throw exception;
	}
}

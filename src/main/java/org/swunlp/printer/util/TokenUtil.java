package org.swunlp.printer.util;

/**
 *	存储/获取当前线程的token信息工具类
 */
public abstract class TokenUtil {

	//线程变量，存放username信息，即使是静态的与其他线程也是隔离的
	private static ThreadLocal<String> tokenThreadLocal = new ThreadLocal<String>();

	//从当前线程变量中获取token信息
	public static String getToken() {
		String token = tokenThreadLocal.get();
		return token;
	}

	//为当前的线程变量赋值上token信息
	public static void setToken(String token) {
		tokenThreadLocal.set(token);
	}

	//清除线程变量
	public static void remove() {
		tokenThreadLocal.remove();
	}
}


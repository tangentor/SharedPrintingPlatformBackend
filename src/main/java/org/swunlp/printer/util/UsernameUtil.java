package org.swunlp.printer.util;

/**
 *	存储/获取当前线程的用户信息工具类
 */
public abstract class UsernameUtil {

	//线程变量，存放username信息，即使是静态的与其他线程也是隔离的
	private static ThreadLocal<String> userThreadLocal = new ThreadLocal<>();

	private static ThreadLocal<Boolean> isAdmin = new ThreadLocal<>();

	//从当前线程变量中获取用户信息
	public static String getLoginUser() {
		String user = userThreadLocal.get();
		return user;
	}

	//为当前的线程变量赋值上用户信息
	public static void setLoginUser(String user,boolean admin) {
		isAdmin.set(admin);
		userThreadLocal.set(user);
	}

	//清除线程变量
	public static void removeUser() {
		userThreadLocal.remove();
	}

	public static boolean isAdmin(){
		return isAdmin.get();
	}
}


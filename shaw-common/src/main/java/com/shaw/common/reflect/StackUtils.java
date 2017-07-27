package com.shaw.common.reflect;

/**
 * 反射机制工具类
 * @author shaw
 */
public class StackUtils {
	/**
	 * 获得当前执行方法
	 */
	public static String getCurrentMethodName(){
		return new Exception().getStackTrace()[1].getMethodName();
	}
}

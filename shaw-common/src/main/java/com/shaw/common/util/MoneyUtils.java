package com.shaw.common.util;

/**
 * 金额处理工具类
 * 
 * @author shaw
 */
public class MoneyUtils {

	/**
	 * 带小数点格式转换为分为单位的纯数字：例 toCent("12.34")得到 1234
	 */
	public static int toCent(String yuan) {
		return Math.round((Float.valueOf(yuan).floatValue() * 100));
	}

	/**
	 * 为分为单位的纯数字转换为带小数点格式：例 fromCent("1234")得到 "12.34"
	 */
	public static float fromCent(String cent) {
		return Integer.valueOf(cent).floatValue() / 100f;
	}
}

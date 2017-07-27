package com.shaw.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 时间工具类
 * 
 * @author shaw
 */
public class TimeUtils {
	private static Logger logger = LoggerFactory.getLogger(TimeUtils.class);

	/**
	 * 根据给定格式生成当前时间
	 * 
	 * @param format
	 *            SimpleDateFormat的日期格式
	 * @return 生成好的时间字符串
	 */
	public static String getFormatTime(String format) {
		try {
			return new SimpleDateFormat(format).format(new Date(System
					.currentTimeMillis()));
		} catch (Exception e) {
			logger.warn("无法根据格式'{}'生成时间", format, e);
			return "";
		}
	}

	/**
	 * yyyy-MM-dd
	 */
	public static String getDateString() {
		return getFormatTime("yyyy-MM-dd");
	}

	/**
	 * yyyyMMdd
	 */
	public static String getCompactDateString() {
		return getFormatTime("yyyyMMdd");
	}

	/**
	 * HH:mm:SS
	 */
	public static String getTimeString() {
		return getFormatTime("HH:mm:SS");
	}

	/**
	 * yyyy-MM-dd HH:mm:SS
	 */
	public static String getFullTimeString() {
		return getFormatTime("yyyy-MM-dd HH:mm:SS");
	}
}

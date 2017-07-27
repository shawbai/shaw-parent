package com.shaw.common.constant;

public class CommonConstant {

	/**
	 * 启用|| 是
	 */
	public static final String COMMON_ON = "1";
	/**
	 * 禁用 || 否
	 */
	public static final String COMMON_UN = "0";

	public static final int COMMON_YES = 1;
	public static final int COMMON_NO = 0;
	
	// 实体审计类 常量pro
	public abstract class EntityAuditProperty {

		public static final String CREATER = "creater";
		public static final String CREATE_TIME = "createTime";
		public static final String MODIFIER = "modifier";
		public static final String MODIFY_TIME = "modifyTime";
		public static final String VERSION = "version";
	}

	// 通用的消息
	public abstract class CommonMessage {
		public static final String FAILED_MESSAGE = "获取数据失败!"; // 获取数据失败
		public static final String SUCCESS_MESSAGE = "请求数据成功!"; // 获取数据失败
		public static final String ERROR_MESSAGE = "请求数据出错!!"; // 获取数据出错!
		public static final String PARAM_ERROR_MESSAGE = "请求参数传递错误!!"; // 参数传递错误
		public static final String IS_EXIST = "记录已存在";
		public static final String JSON_FORMAT_ERROR = "json格式错误";
	}

	// 通用的状态码
	public abstract class CommonCode {
		public static final String SUCCESS_CODE = "200"; // 获取数据成功状态码
		public static final String ERROR_CODE = "500"; // 获取数据出错状态码
		public static final String PARAM_ERROR_CODE = "0"; // 参数传递错误状态码
		public static final String UN_LOGINED_CODE = "-99"; // 未登录状态码
		public static final String CHILD_ACCOUNT_ERROR_CODE = "-98"; // 未登录状态码
	}

}

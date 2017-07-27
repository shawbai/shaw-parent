package com.shaw.common.pojo;

import java.io.Serializable;

import com.shaw.common.constant.CommonConstant.CommonCode;
import com.shaw.common.constant.CommonConstant.CommonMessage;

/**
 * 响应的基类
 */
public class BaseResponse implements Serializable {

	private static final long serialVersionUID = -1011440293454989799L;
//    public static final String callbackKey="callback";
//	private String callback;
	private String status;
	private Object data;
	private String detail;

	private BaseResponse(String status, Object data, String detail) {
		super();
		this.status = status;
		this.data = data;
		this.detail = detail;
	}

	public static BaseResponse success() {
		return newInstance(CommonCode.SUCCESS_CODE, "",  CommonMessage.SUCCESS_MESSAGE);
	}
	
	public static BaseResponse successComp(String errorMsg) {
		return newInstance(CommonCode.SUCCESS_CODE, "",  errorMsg);
	}

	public static BaseResponse success(Object data) {
		return newInstance( CommonCode.SUCCESS_CODE, data,  CommonMessage.SUCCESS_MESSAGE);
	}

	public static BaseResponse error() {
		return newInstance( CommonCode.ERROR_CODE, "",  CommonMessage.ERROR_MESSAGE);
	}

	public static BaseResponse error(String msg) {
		return newInstance( CommonCode.ERROR_CODE, "", msg);
	}

	public static BaseResponse paramError() {
		return newInstance( CommonCode.PARAM_ERROR_CODE, "",  CommonMessage.PARAM_ERROR_MESSAGE);
	}

	public static BaseResponse paramError(String msg) {
		return newInstance( CommonCode.PARAM_ERROR_CODE, "", msg);
	}

	public BaseResponse(String status) {
		this.status = status;
	}

	public BaseResponse(String status, Object data) {
		this.status = status;
		this.setData(data);
	}

	public static BaseResponse newInstance(String status) {
		return new BaseResponse(status);
	}

	public static BaseResponse newInstance(String status, Object data) {
		return new BaseResponse(status, data);
	}

	public static BaseResponse newInstance(String status, Object data, String detail) {
		return new BaseResponse(status, data, detail);
	}

	public String getStatus() {
		return status;
	}

	public BaseResponse setStatus(String status) {
		if (null == status)
			this.status = "";
		else
			this.status = status;
		return this;
	}

	public Object getData() {
		return data;
	}

	public String getDetail() {
		return detail;
	}

	/**
	 * 设置 响应数据 列表类型数据(list)会自动转换为分页类型的数据
	 * 
	 * @param data
	 * @return
	 */
	public BaseResponse setData(Object data) {
		this.data = data;
		return this;
	}

	public BaseResponse setDetail(String message) {
		if (null == message)
			this.detail = "";
		else
			this.detail = message;
		return this;
	}

//	public String getCallback() {
//		return callback;
//	}
//
//	public void setCallback(String callback) {
//		this.callback = callback;
//	}

}

package com.shaw.common.format;

import java.io.InputStream;

import com.alibaba.fastjson.JSONObject;

public class ConvertUtils {

	public static String json2singleLine(InputStream is, JSONObject json) {
		return SingleLineUtils.fromJSON(is, json);
	}

	public static String singleLine2json(InputStream is, String singleLine) {
		try {
			return SingleLineUtils.toJSON(is, singleLine);
		} catch (SingleLineFormatException e) {
			return "";
		}
	}

	public static String json2xml(String jsonString) {
		return JSONUtils.json2Xml(jsonString);
	}

	public static String xml2json(String xmlString) {
		return JSONUtils.xml2Json(xmlString);
	}

	public static String jsonObject2json(JSONObject json) {
		return json.toString();
	}

	public static JSONObject json2jsonObject(String jsonStr) {
		return JSONObject.parseObject(jsonStr);
	}
}

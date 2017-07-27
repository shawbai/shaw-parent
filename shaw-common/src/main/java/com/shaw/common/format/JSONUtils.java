package com.shaw.common.format;


import org.json.JSONObject;
import org.json.XML;

import com.alibaba.fastjson.JSON;

/**
 * JSON工具类
 * <ul>依赖JAR包：
 * <li>json-lib</li>
 * <li>ezmorph</li>
 * <li>xom</li>
 * <li>common-beanutils</li>
 * <li>common-collections</li>
 * </ul>
 * @author shaw
 */
public class JSONUtils {
	/**
	 * XML 转换为 JSON
	 * @param xmlString
	 * @return
	 */
	public static String xml2Json(String xmlString){
		if ("".equals(xmlString.trim())){
			return "";
		}
		JSONObject json = XML.toJSONObject(xmlString);
		return json.toString();
	}
	
	/**
	 * JSON 转换为XML
	 * @param jsonString
	 * @return
	 */
	public static String json2Xml(String jsonString){
		if ("".equals(jsonString.trim())){
			return "";
		}
		return XML.toString(new JSONObject(jsonString));
	}
	
	/**
	 * 将JSON转换为干净的格式（带缩进）
	 * @param jsonString
	 * @return
	 */
	public static String toPrettyJson(String jsonString){
		return JSON.toJSONString(jsonString, true);
	}
	
}

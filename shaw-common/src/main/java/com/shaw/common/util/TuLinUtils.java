package com.shaw.common.util;

import com.alibaba.fastjson.JSONObject;
import com.shaw.common.encrypt.Aes;
import com.shaw.common.encrypt.Md5;

public class TuLinUtils {
	//图灵网站上的secret
	private static final String secret = "14f468627f5b35d7";
	//图灵网站上的apiKey
	private static final String apiKey = "dea65ae65e29675b9e0017243bd4b7e9";
	
	/**
	 * 
	 * @param cmd 发送的消息
	 */
	public static String sendMsg(String cmd){
		//待加密的json数据
		String data = "{\"key\":\""+apiKey+"\",\"info\":\""+cmd+"\"}";
		//获取时间戳
		String timestamp = String.valueOf(System.currentTimeMillis());
		//生成密钥
		String keyParam = secret+timestamp+apiKey;
		String key = Md5.MD5(keyParam);
		//加密
		Aes mc = new Aes(key);
		data = mc.encrypt(data);		
		//封装请求参数
		JSONObject json = new JSONObject();
		json.put("key", apiKey);
		json.put("timestamp", timestamp);
		json.put("data", data);
		//请求图灵api
		String result = PostServer.SendPost(json.toString(), "http://www.tuling123.com/openapi/api");
//	    System.out.println("POST DONE");
//		System.out.println(result);
		return result;
	}
	
}

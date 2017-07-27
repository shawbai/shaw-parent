package com.shaw.common.format;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shaw.common.io.FileUtils;
import com.shaw.common.util.ShawStringUtils;
import com.shaw.common.util.ShawStringUtils.FillType;

public class SingleLineUtils {
	private static Logger log = Logger.getLogger(SingleLineUtils.class);

	private String format = "";
	private String singleLine = "";
	private String jsonStr = "";
	private JSONObject json = null;
	
	/**
	 * 构造函数，用于单行格式转JSON
	 * 
	 * @param formatIS
	 *            单行格式定义文件输入流
	 * @param singleLine
	 *            单行字符串
	 * @throws IOException
	 * @throws SingleLineFormatException
	 */
	public SingleLineUtils(InputStream formatIS, String singleLine)
			throws IOException, SingleLineFormatException {
		this.singleLine = singleLine;
		format = FileUtils.read(formatIS);
		Document doc = Dom4jUtils.parseFrom(format);
		Element root = doc.getRootElement();
		json = singleLine2json(singleLine, root);
		jsonStr = json.toString();
		log.info("RESULT:\n" + JSONUtils.toPrettyJson(json.toString()));
	}

	/**
	 * 构造函数，用于json转单行格式
	 * 
	 * @param formatIS
	 *            单行格式定义文件输入流
	 * @param json
	 *            json对象
	 * @throws IOException
	 */
	public SingleLineUtils(InputStream formatIS, JSONObject json)
			throws IOException {
		this.json = json;
		format = FileUtils.read(formatIS);
		Document doc = Dom4jUtils.parseFrom(format);
		Element root = doc.getRootElement();
		singleLine = json2singleLine(json, root);
	}

	private String mergeList(JSONObject json, Element root) {
		if (!root.getName().equals("list")) {
			return "";
		}
		String name = root.attributeValue("name");
		JSONArray array = json.getJSONArray(name);
		String delimiter = root.attributeValue("delimiter");
		if (delimiter == null) {
			log.error("没有delimiter属性");
			return "";
		} else {
			String[] vals = new String[array.size()];
			boolean nested = root.elements().size() == 1
					&& ((Element) root.elements().get(0)).getName().equals(
							"json");

			Iterator<?> iter = array.iterator();
			int i = 0;
			while (iter.hasNext()) {
				Object obj = iter.next();
				System.out.println(obj);
				if (nested) {
					JSONObject jsonObj = (JSONObject) obj;
					vals[i++] = mergeJson(jsonObj, (Element) root.elements()
							.get(0));
				} else {
					String value = obj.toString();
					vals[i++] = value;
				}
			}
			System.out.println("MERGED:" + ShawStringUtils.join(vals, delimiter)
					+ delimiter);
			return ShawStringUtils.join(vals, delimiter);
		}
	}

	private String mergeJson(JSONObject json, Element root) {
		if (!root.getName().equals("json")) {
			return "";
		}
		String delimiter = root.attributeValue("delimiter");
		if (delimiter == null) {
			log.error("没有delimiter属性");
			return "";
		}
		Iterator<?> iter = root.elementIterator();
		String[] vals = new String[root.elements().size()];

		int i = 0;
		while (iter.hasNext()) {
			Element elem = (Element) iter.next();
			String tagName = elem.getName();
			String key = elem.attributeValue("name");
			vals[i] = "";
			if (tagName.equals("val")) {
				if (json.containsKey(key))
					vals[i] = json.getString(key);
			} else if (tagName.equals("json")) {
				vals[i] = mergeJson(json.getJSONObject(key), elem);
			} else if (tagName.equals("list")) {
				vals[i] = mergeList(json, elem);
			}
			i++;
		}
		System.out.println("MERGED:" + ShawStringUtils.join(vals, delimiter)
				+ delimiter);
		return ShawStringUtils.join(vals, delimiter);
	}

	private String mergeFixedLength(JSONObject json, Element root) {
		if (!root.getName().equals("fixed-length")) {
			return "";
		}
		Iterator<?> iter = root.elementIterator();
		String[] vals = new String[root.elements().size()];

		int i = 0;
		while (iter.hasNext()) {
			Element elem = (Element) iter.next();
			String tagName = elem.getName();
			String key = elem.attributeValue("name");
			if (tagName.equals("val")) {
				String in = "";
				if (json.containsKey(key))
					in = json.getString(key);
				int length = ShawStringUtils.toANSI(in).length();
				FillType fillType = FillType.LEFT_SPACE;
				if (elem.attribute("length") != null) {
					length = Integer.parseInt(elem.attributeValue("length"));
				}
				if (elem.attribute("fillType") != null) {
					fillType = FillType.valueOf(elem.attributeValue("fillType")
							.toUpperCase());
				}
				vals[i] = ShawStringUtils.format(fillType, in, length);
			}
			i++;
		}
		System.out.println("MERGED:" + ShawStringUtils.join(vals, ""));
		return ShawStringUtils.join(vals, "");
	}

	/**
	 * json转换为单行格式
	 * 
	 * @param json
	 * @param root
	 *            对应的格式定义，必须为一个xml元素对象
	 * @return 单行字符串
	 */
	private String json2singleLine(JSONObject json, Element root) {
		String tagName = root.getName();
		System.err.println(tagName);
		if (tagName.equals("json")) {
			return mergeJson(json, root);
		} else if (tagName.equals("list")) {
			return mergeList(json, root);
		} else if (tagName.equals("fixed-length")) {
			return mergeFixedLength(json, root);
		} else {
			return null;
		}
	}

	/**
	 * 根据格式获取JSONArray
	 * 
	 * @param targetStr
	 *            目标单行完整或部分字符串
	 * @param root
	 *            单行格式定义的Element
	 * @return
	 * @throws SingleLineFormatException
	 */
	private JSONArray extractList(String targetStr, Element root)
			throws SingleLineFormatException {
		String delimiter = "";
		try {
			Attribute attr = root.attribute("delimiter");
			delimiter = translate(attr.getValue());
			targetStr = targetStr.trim();
			if (targetStr.lastIndexOf(attr.getValue()) == targetStr.length() - 1) {
				targetStr = targetStr.substring(0, targetStr.length() - 1);
			}
		} catch (NullPointerException e) {
			log.error("没有delimiter属性");
			return null;
		}
		System.out.println("BEFORE:" + targetStr);
		targetStr = targetStr.replaceAll(delimiter, " " + delimiter + " ");
		System.out.println("AFTER REPL:" + targetStr);
		JSONArray array = new JSONArray();
		String[] vals = targetStr.split(delimiter);
		for (int i = 0; i < vals.length; i++) {
			vals[i] = vals[i].trim();
		}

		if (root.elements().size() > 0) {
			for (String str : vals) {
				JSONObject json = extractJson(str, (Element) root.elements()
						.get(0));
				array.add(json);
			}
		} else {
			array.addAll(Arrays.asList(vals));
		}
		return array;
	}

	/**
	 * 单行格式转为json，只支持fixed-length节点
	 * 
	 * @param targetStr
	 *            目标单行完整或局部字符串
	 * @param root
	 *            单行格式定义的Element
	 * @return
	 * @throws SingleLineFormatException
	 */
	private JSONObject extractFixedLength(String targetStr, Element root)
			throws SingleLineFormatException {
		try {
			if (targetStr.length() < 1)
				throw new SingleLineFormatException("输入字符串长度不足："
						+ targetStr.length());
		} catch (NullPointerException e) {
			log.error("没有delimiter属性");
			return null;
		}
		Iterator<?> iter = root.elementIterator();
		JSONObject json = new JSONObject();
		int index = 0;
		targetStr = ShawStringUtils.toANSI(targetStr);
		while (iter.hasNext()) {
			Element elem = (Element) iter.next();
			int length = Integer.valueOf(elem.attribute("length").getValue());
			String key = elem.attributeValue("name");
			int endIndex = (index + length) > targetStr.length() ? targetStr
					.length() : (index + length);
			log.debug("START: " + index + " END: " + endIndex);
			System.out.println("START: " + index + " END: " + endIndex);
			json.put(key,
					ShawStringUtils.fromANSI(targetStr.substring(index, endIndex))
							.trim());
			index += length;
		}
		return json;
	}

	/**
	 * 单行格式转为json，注意，此方法会递归调用
	 * 
	 * @param targetStr
	 *            目标单行完整或局部字符串
	 * @param root
	 *            单行格式定义的Element
	 * @return
	 * @throws SingleLineFormatException
	 */
	private JSONObject extractJson(String targetStr, Element root)
			throws SingleLineFormatException {
		String delimiter = "";
		try {
			Attribute attr = root.attribute("delimiter");
			delimiter = translate(attr.getValue());
			targetStr = targetStr.trim();
			if (targetStr.length() < 1)
				throw new SingleLineFormatException("输入字符串长度不足："
						+ targetStr.length());
			if (targetStr.lastIndexOf(attr.getValue()) == targetStr.length() - 1) {
				targetStr = targetStr.substring(0, targetStr.length() - 1);
			}
		} catch (NullPointerException e) {
			log.error("没有delimiter属性");
			return null;
		}
		System.out.println("BEFORE:" + targetStr);
		targetStr = targetStr.replaceAll(delimiter, " " + delimiter + " ");
		System.out.println("AFTER REPL:" + targetStr);
		String[] vals = targetStr.split(delimiter);
		int keyCount = root.elements().size();
		int valCount = vals.length;
		if (keyCount != valCount) {
			log.error(String.format(
					"模板xml定义的参数个数与实际数目不符，实际有%d个参数，而XML中定义了%d个参数", valCount,
					keyCount));
			throw new SingleLineFormatException(String.format(
					"模板xml定义的参数个数与实际数目不符，实际有%d个参数，而XML中定义了%d个参数", valCount,
					keyCount));
		}
		int i = 0;
		JSONObject json = new JSONObject();
		for (Iterator<?> iter = root.elementIterator(); iter.hasNext();) {
			Element elem = (Element) iter.next();
			if (elem.getName().equals("json")) {
				json.put(elem.attribute("name").getValue(),
						extractJson(vals[i], elem));
			} else if (elem.getName().equals("list")) {
				json.put(elem.attribute("name").getValue(),
						extractList(vals[i], elem));
			} else
				json.put(elem.attribute("name").getValue(), vals[i].trim());
			i++;
		}
		return json;
	}

	public JSONObject singleLine2json(String targetStr, Element root)
			throws SingleLineFormatException {
		String tagName = root.getName();
		if (tagName.equals("json")) {
			return extractJson(targetStr, root);
		} else if (tagName.equals("list")) {
			JSONObject json = new JSONObject();
			JSONArray array = extractList(targetStr, root);
			json.put(root.attributeValue("name"), array);
			return json;
		} else if (tagName.equals("fixed-length")) {
			return extractFixedLength(targetStr, root);
		} else {
			return null;
		}
	}

	/**
	 * 根据分隔符生成对应的分割正则表达式
	 * 
	 * @param delimiter
	 *            分隔符
	 * @return 分割正则表达式
	 */
	private String translate(String delimiter) {
		if (delimiter.equals("|"))
			return "\\|";
		if (delimiter.equals("^"))
			return "\\^";
		if (delimiter.equals("$"))
			return "\\$";
		return "";
	}

	/**
	 * 单行字符串转json字符串
	 * 
	 * @param formatIS
	 *            单行格式定义文件输入流
	 * @param singleLine
	 *            单行字符串（完整）
	 * @return json字符串
	 * @throws SingleLineFormatException
	 */
	public static String toJSON(InputStream formatIS, String singleLine)
			throws SingleLineFormatException {
		if (formatIS == null) {
			log.error("单行字符串定义文件为空，无法翻译");
			return null;
		}
		try {
			return new SingleLineUtils(formatIS, singleLine).getJsonStr();
		} catch (IOException e) {
			log.error(e);
			return null;
		}
	}

	/**
	 * 单行字符串转json对象
	 * 
	 * @param formatIS
	 *            单行格式定义文件输入流
	 * @param singleLine
	 *            单行字符串（完整）
	 * @return json字符串
	 * @throws SingleLineFormatException
	 */
	public static JSONObject toJSONObject(InputStream formatIS,
			String singleLine) throws SingleLineFormatException {
		String jsonStr = toJSON(formatIS, singleLine);
		if (jsonStr != null) {
			return JSONObject.parseObject(jsonStr);
		} else {
			return null;
		}
	}

	/**
	 * json对象转换为单行字符串
	 * 
	 * @param formatIS
	 *            formatIS 单行格式定义文件输入流
	 * @param json
	 *            json对象
	 * @return singleLine 单行字符串（完整）
	 */
	public static String fromJSON(InputStream formatIS, JSONObject json) {
		if (formatIS == null) {
			log.error("单行字符串定义文件为空，无法翻译");
			return null;
		}
		try {
			return new SingleLineUtils(formatIS, json).getSingleLine();
		} catch (Exception e) {
			log.error("fromJson出错", e);
			return null;
		}
	}

	// Getters & Setters
	protected String getSingleLine() {
		return singleLine;
	}

	protected void setSingleLine(String singleLine) {
		this.singleLine = singleLine;
	}

	protected String getJsonStr() {
		return jsonStr;
	}

	protected JSONObject getJson() {
		try {
			return JSONObject.parseObject(jsonStr);
		} catch (Exception e) {
			return null;
		}
	}
}

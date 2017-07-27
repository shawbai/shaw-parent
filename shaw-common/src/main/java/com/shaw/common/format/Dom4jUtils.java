package com.shaw.common.format;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.dom.DOMElement;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 * XML工具类：Document对象与 XML字符串转换、Document对象与File转换、设置XML节点及其值
 * @author shaw
 */
public class Dom4jUtils {

	private static final Logger log = Logger.getLogger(Dom4jUtils.class);

	/**
	 * 将Document对象内容转为字符串
	 * 
	 * @param document
	 *            org.dom4j.Document对象
	 * @param encoding
	 *            字符编码
	 */
	public static String toXML(Document document, String encoding) {
		try {
			// 使用输出流来进行转化
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			OutputFormat format = new OutputFormat("  ", true, encoding);
			// format.setIndent(false);//缩进
			// format.setNewlines(false);
			format.setTrimText(true);
			format.setExpandEmptyElements(true);
			XMLWriter writer = new XMLWriter(out, format);
			writer.write(document);
			return out.toString(encoding);
		} catch (Exception ex) {
			log.error(ex);
			return "";
		}
	}

	/**
	 * 转换为漂亮格式
	 * 
	 * @param xmlString
	 *            xml格式的字符串
	 */
	public static String toPrettyXml(String xml, String encoding) {
		try {
			final OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding(encoding);
			final Document document = DocumentHelper.parseText(xml);
			StringWriter sw = new StringWriter();
			final XMLWriter writer = new XMLWriter(sw, format);
			writer.write(document);
			return sw.toString();
		} catch (Exception ex) {
			log.error(ex);
			return null;
		}
	}
	
	/**
	 * 将字符串转为Document对象
	 * 
	 * @param xmlString
	 *            xml格式的字符串
	 */
	public static Document parseFrom(String xmlString) {
		try {
			return DocumentHelper.parseText(xmlString);
		} catch (Exception ex) {
			log.error(ex);
			return null;
		}
	}

	/**
	 * 将org.dom4j.Document对象保存为一个xml文件到指定路径
	 * 
	 * @param document
	 *            需要保存的document对象
	 * @param fileName
	 *            目标路径
	 * @return true:保存成功 false:失败
	 */
	public static boolean writeToFile(Document document, String fileName,
			String encoding) {
		try {
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding(encoding);
			XMLWriter writer = new XMLWriter(
					new FileWriter(new File(fileName)), format);
			writer.write(document);
			writer.close();
			return true;
		} catch (Exception ex) {
			log.error(ex);
			return false;
		}
	}

	/**
	 * 将xml格式的字符串保存为本地文件，如果字符串格式不符合xml规则，则返回失败
	 * 
	 * @return true:保存成功 flase:失败
	 * @param fileName
	 *            保存的文件名
	 * @param xmlString
	 *            需要保存的XML字符串
	 */
	public static boolean writeToFile(String xmlString, String fileName,
			String encoding) {
		try {
			Document doc = DocumentHelper.parseText(xmlString);
			return writeToFile(doc, fileName, encoding);
		} catch (Exception ex) {
			log.error(ex);
			return false;
		}
	}

	/**
	 * 载入XML文件生成一个org.dom4j.Document对象
	 * 
	 * @param fileName
	 *            XML文件路径
	 * @return 成功返回Document对象，失败返回null
	 */
	public static Document load(String fileName) {
		try {
			SAXReader saxReader = new SAXReader();
			return saxReader.read(new File(fileName));
		} catch (Exception ex) {
			log.error(ex);
			return null;
		}
	}

	/**
	 * 获得某个节点的值
	 * 
	 * @param document
	 *            org.dom4j.Document对象
	 * @param nodeName
	 *            节点名称
	 */
	public static String getText(Document document, String nodeName) {
		try {
			Node node = document.selectSingleNode("//" + nodeName);
			if (node != null) {
				return node.getText();
			} else {
				log.warn("节点[" + nodeName + "]不存在");
			}
		} catch (Exception e) {
			log.error(e);
		}
		return null;
	}

	/**
	 * 设置一个节点的text
	 * 
	 * @param nodeName
	 *            节点名
	 * @param nodeValue
	 *            节点值
	 */
	public static void setText(Document document, String nodeName,
			String nodeValue) {
		try {
			Node node = document.selectSingleNode("//" + nodeName);
			if (node != null) {
				node.setText(nodeValue);
			} else {
				log.warn("节点[" + nodeName + "]不存在");
			}
		} catch (Exception e) {
			log.error(e);
		}
	}

	/**
	 * 根据nodeMaps中的键->值来批量设置各个node的文本内容
	 */
	public static void setTexts(Document document,
			HashMap<String, String> nodeMaps) {
		for (String nodeName : nodeMaps.keySet()) {
			setText(document, nodeName, nodeMaps.get(nodeName));
		}
	}

	/**
	 * 获取指定节点属性的值
	 * 
	 * @param document
	 * @param nodeName
	 * @param attributeName
	 * @return
	 */
	public static String getAttribute(Document document, String nodeName,
			String attributeName) {
		try {
			Node node = document.selectSingleNode("//" + nodeName);
			if (node != null) {
				Element element = (Element) node;
				return element.attributeValue(attributeName);
			} else {
				log.warn("节点[" + nodeName + "]不存在");
			}
		} catch (Exception e) {
			log.error(e);
		}
		return null;
	}

	/**
	 * 设置属性的值
	 * 
	 * @param document
	 * @param nodeName
	 * @param attributeName
	 * @param attributeValue
	 * @return
	 */
	public static void setAttribute(Document document, String nodeName,
			String attributeName, String attributeValue) {
		try {
			Node node = document.selectSingleNode("//" + nodeName);
			if (node != null) {
				DOMElement element = (DOMElement) node;
				element.setAttribute(attributeName, attributeValue);
			} else {
				System.out.println("节点[" + nodeName + "]不存在");
			}
		} catch (Exception e1) {
			System.out.println("setAttributeValue() Exception："
					+ e1.getMessage());
		}
	}

	/**
	 * 批量设置属性的值
	 * 
	 * @param document
	 * @param nodeName
	 * @param attributeMaps
	 */
	public static void setAttributes(Document document, String nodeName,
			HashMap<String, String> attributeMaps) {
		for (String attributeName : attributeMaps.keySet()) {
			setAttribute(document, nodeName, attributeName,
					attributeMaps.get(attributeName));
		}
	}

	/**
	 * 批量设置属性的值
	 * 
	 * @param document
	 * @param nodeNameAndAttributeMaps
	 *            <nodeName, <attributeName, attributeValue>>
	 */
	public static void setAttributeValue(Document document,
			HashMap<String, HashMap<String, String>> nodeNameAndAttributeMaps) {
		for (String nodeName : nodeNameAndAttributeMaps.keySet()) {
			HashMap<String, String> attributeMaps = nodeNameAndAttributeMaps
					.get(nodeName);
			for (String attributeName : attributeMaps.keySet()) {
				setAttribute(document, nodeName, attributeName,
						attributeMaps.get(attributeName));
			}
		}
	}

	/**
	 * 获得某个节点的子节点的值
	 */
	public static String getText(Document document, String parentNodeName,
			String childNodeName) {
		try {
			Node node = document.selectSingleNode("//" + parentNodeName + "/"
					+ childNodeName);
			if (node != null) {
				return node.getText();
			} else {
				log.warn("节点[" + parentNodeName + "." + childNodeName + "]不存在");
			}
		} catch (Exception e) {
			log.error(e);
		}
		return null;
	}

	/**
	 * 获得子节点的属性值
	 * 
	 * @return
	 */
	public static String getAttribute(Document document, String parentNodeName,
			String childNodeName, String attributeName) {
		try {
			Node node = document.selectSingleNode("//" + parentNodeName + "/"
					+ childNodeName);
			if (node != null) {
				Element element = (Element) node;
				return element.attributeValue(attributeName);
			} else {
				log.warn("节点[" + parentNodeName + "." + childNodeName + "]不存在");
			}
		} catch (Exception e) {
			log.error(e);
		}
		return null;
	}

	/**
	 * 设置子节点属性的值
	 */
	public static void setAttributeOfChild(Document document,
			String parentNodeName, String childNodeName, String attributeName,
			String attributeValue) {
		try {
			Node node = document.selectSingleNode("//" + parentNodeName + "/"
					+ childNodeName);
			if (node != null) {
				DOMElement element = (DOMElement) node;
				element.setAttribute(attributeName, attributeValue);
			} else {
				log.warn("节点[" + parentNodeName + "." + childNodeName + "]不存在");
			}
		} catch (Exception e) {
			log.error(e);
		}
	}

	/**
	 * 设置子节点属性值
	 */
	public static void setAttributeOfChild(Document document,
			String parentNodeName, String childNodeName,
			HashMap<String, String> attributeMaps) {
		for (String attributeName : attributeMaps.keySet()) {
			setAttributeOfChild(document, parentNodeName, childNodeName,
					attributeName, attributeMaps.get(attributeName));
		}
	}

	/**
	 * 设置属性的值
	 * 
	 * @param document
	 * @param parentNodeName
	 * @param nodeNameAndAttributeMaps
	 */
	public static void setAttributeValueByParentNodeName(Document document,
			String parentNodeName,
			HashMap<String, HashMap<String, String>> nodeNameAndAttributeMaps) {
		for (String childNodeName : nodeNameAndAttributeMaps.keySet()) {
			setAttributeOfChild(document, parentNodeName, childNodeName,
					nodeNameAndAttributeMaps.get(childNodeName));
		}
	}

	/**
	 * 设置一个节点值
	 * 
	 * @param nodeName
	 *            父节点名
	 * @param childNodeName
	 *            节点名
	 * @param nodeValue
	 *            节点值
	 */
	public static void setTextOfChild(Document document, String nodeName,
			String childNodeName, String childNodeValue) {
		try {
			Node node = document.selectSingleNode("//" + nodeName + "/"
					+ childNodeName);
			if (node != null) {
				node.setText(childNodeValue);
			} else {
				log.warn("节点[" + nodeName + "." + childNodeName + "]不存在");
			}
		} catch (Exception e) {
			log.error(e);
		}
	}

	/**
	 * 设置节点的值
	 * 
	 * @param document
	 * @param nodeName
	 * @param nodeMaps
	 */
	public static void setTextOfChildren(Document document,
			String parentNodeName, HashMap<String, String> nodeMaps) {
		for (String childNodeName : nodeMaps.keySet()) {
			setTextOfChild(document, parentNodeName, childNodeName,
					nodeMaps.get(childNodeName));
		}
	}

	public static void main(String[] args) {
		Document document = Dom4jUtils
				.load("D:\\AgentConfig.xml");
		Dom4jUtils.setTextOfChild(document, "tx", "pktype", "gs1");
		Dom4jUtils.setTextOfChild(document, "tx", "trcd", "gs2");
		System.out.println(Dom4jUtils.getAttribute(document, "tx1", "id1"));
		System.out.println(Dom4jUtils.getAttribute(document, "tx", "type"));
		Dom4jUtils.setAttribute(document, "tx", "type", "2");
		Dom4jUtils.setAttribute(document, "tx", "type1", "3");
		System.out.println(Dom4jUtils.toXML(document, "GBK"));
	}
	
}

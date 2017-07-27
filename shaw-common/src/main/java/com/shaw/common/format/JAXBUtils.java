package com.shaw.common.format;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;


/**
 * Java类和XML的映射工具类，使用JAXB：Java 对象与XML字符串转换<br>
 * 
 * 使用 Java 6 以上不需要jar包。
 * @author shaw
 */
public class JAXBUtils {
	
	/**
	 * 将Java实例根据Annotation转化为XML，依赖JAXB实现。
	 * @param klass Java类
	 * @param object Java实例
	 * @param properties 需要交付Marshaller的配置属性
	 * @return XML字符串
	 * @throws JAXBException JAXB标准异常
	 */
	public static String marshal(Class<?> klass, Object object,
			HashMap<String, Object> properties) throws JAXBException {
		if (!object.getClass().getName().equals(klass.getName())) {
			throw new IllegalArgumentException(
					"Argument 'object' must be instance of argument 'klass'");
		}

		JAXBContext jc = JAXBContext.newInstance(klass);
		Marshaller jaxbMarshaller = jc.createMarshaller();
		StringWriter result = new StringWriter();

		if (properties != null)
			for (String key : properties.keySet()) {
				jaxbMarshaller.setProperty(key, properties.get(key));
			}

		jaxbMarshaller.marshal(object, result);
		return result.toString();
	}

	/**
	 * 将一个XML的内容填充到对应Java类中，形成一个Java实例。依赖JAXB实现
	 * 
	 * @param klass
	 *            Java类
	 * @param xml
	 *            xml字符串，注意必须包含XML头定义
	 * @param encoding
	 *            字符编码，必须和XML头定义中的encoding相一致
	 * @return Java类实例
	 * @throws JAXBException
	 *             JAXB标准异常
	 * @throws UnsupportedEncodingException
	 *             不支持的编码
	 */
	public static Object unmarshal(Class<?> klass, String xml, String encoding)
			throws JAXBException, UnsupportedEncodingException {
		JAXBContext jc = JAXBContext.newInstance(klass);
		Unmarshaller jaxbUnmarshaller = jc.createUnmarshaller();

		ByteArrayInputStream is = new ByteArrayInputStream(
				xml.getBytes(encoding));
		return jaxbUnmarshaller.unmarshal(is);

	}

}

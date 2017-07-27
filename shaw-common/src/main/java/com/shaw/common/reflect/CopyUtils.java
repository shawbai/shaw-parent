package com.shaw.common.reflect;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;

/**
 * 对象拷贝工具类
 * @author shaw
 *
 */
public class CopyUtils {

	static Logger log = Logger.getLogger(CopyUtils.class);

	/**
	 * 复制一个对象的属性到另一个对象
	 */
	public static void copy(Object source, Object dest) throws Exception {
		// 获取属性
		BeanInfo sourceBean = Introspector.getBeanInfo(source.getClass(),
				java.lang.Object.class);
		PropertyDescriptor[] sourceProperty = sourceBean
				.getPropertyDescriptors();

		BeanInfo destBean = Introspector.getBeanInfo(dest.getClass(),
				java.lang.Object.class);
		PropertyDescriptor[] destProperty = destBean.getPropertyDescriptors();

		for (int i = 0; i < sourceProperty.length; i++) {
			for (int j = 0; j < destProperty.length; j++) {
				if (sourceProperty[i].getName().equals(
						destProperty[j].getName())) {
					// 调用source的getter方法和dest的setter方法
					Method sourceWriteMethod = sourceProperty[i]
							.getWriteMethod();
					if (sourceWriteMethod == null)
						continue;

					Method destWriteMethod = dest.getClass().getDeclaredMethod(
							sourceWriteMethod.getName(),
							sourceWriteMethod.getParameterTypes());
					if (destWriteMethod != null) {
						log.info("Calling: source."
								+ sourceProperty[i].getReadMethod().getName()
								+ " -> target." + destWriteMethod.getName());
						Object obj = sourceProperty[i].getReadMethod().invoke(
								source);
						if (obj != null)
							destWriteMethod.invoke(dest, sourceProperty[i]
									.getReadMethod().invoke(source));
					}
					break;
				}
			}
		}

	}
}

package com.shaw.common.reflect;


import java.lang.reflect.Method;

import com.shaw.common.util.ShawStringUtils;

public class ReflectChecker {

	public static boolean check(Object param, Enum<?>[] checkFields) throws NoSuchMethodException, ClassNotFoundException {
		if (param == null) {
			return false;
		}

		if (checkFields == null) {
			return false;
		}

		for (Enum<?> field : checkFields) {
			Method getFieldMethod = ReflectUtils.findMethodByMethodName(param.getClass(), "get"
					+ field.toString().substring(0, 1).toUpperCase() + field.toString().substring(1));
			try {
				Object value = getFieldMethod.invoke(param);
				if (value == null) {
					System.out.println(field + " is null, false will be return .");
					return false;
				}
				if (value instanceof String && ShawStringUtils.isNullString(value.toString())) {
					System.out.println(field + " is null, false will be return .");
					return false;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}

		return true;
	}
}

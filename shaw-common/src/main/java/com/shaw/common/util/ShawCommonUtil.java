package com.shaw.common.util;

import java.lang.reflect.Method;

public class ShawCommonUtil {
	
    /**
     * 枚举类中是否含有code
     * @param code
     * @return
     */
	public static <E> boolean containsCode(Class<E> clazz,String code){
		if(!clazz.isEnum())
			return false;
		boolean hasGetCode = false;
		Method GetCodeMethod = null;
		for (Method method : clazz.getMethods()) {
			if(method.getName().equals("getCode"))
			{
				hasGetCode = true;
				GetCodeMethod = method;
				break;
			}
		}
		for (E e : clazz.getEnumConstants()) {
			if(hasGetCode){
				try {
					if(code.equals(GetCodeMethod.invoke(e)))
						return true;
				} catch (Exception exception) {
					return false;
				}
			}else{
				if(e.toString().equals(code))
					return true;
			}
		}
		return false;
	}

}

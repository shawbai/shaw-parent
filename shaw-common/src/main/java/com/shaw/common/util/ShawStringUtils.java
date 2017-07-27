package com.shaw.common.util;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.shaw.common.constant.ConstantsUtil;

/**
 * String工具类
 * 
 * @author shaw
 */
public class ShawStringUtils extends org.springframework.util.StringUtils{

	public static String mask = "xxxxo";

	public enum FillType {
		LEFT_SPACE, RIGHT_SPACE, LEFT_ZERO, RIGHT_ZERO
	};

	/**
	 * 根据给定的格式整理字符串，限定最大长度，超出部分会被截去 （中文长度问题已经处理）
	 * 
	 * @param format
	 *            格式（同String.format）
	 * @param str
	 *            目标字符串
	 * @param length
	 *            限定最大长度
	 * @return 格式化后的字符串
	 */
	public static String format(String format, String str, int length) {
		int maxLength = toANSI(str).length() < length ? toANSI(str).length()
				: length;
		return fromANSI(String.format(format,
				toANSI(str).substring(0, maxLength)));
	}

	/**
	 * 根据给定的要求整理字符串（中文长度问题已经处理）
	 * 
	 * @param fillType
	 *            左补空格（LEFT_SPACE）、右补空格（RIGHT_SPACE）、左补0（LEFT_ZERO）、右补0（
	 *            RIGHT_ZERO）
	 * @param length
	 *            限定最大长度
	 * @return 格式化后的字符串
	 */
	public static String format(FillType fillType, String str, int length) {
		int maxLength = toANSI(str).length() < length ? toANSI(str).length()
				: length;
		String fix = "";
		for (int i = 0; i < length - maxLength; i++) {
			switch (fillType) {
			case LEFT_SPACE:
			case RIGHT_SPACE:
				fix += " ";
				break;
			case LEFT_ZERO:
			case RIGHT_ZERO:
				fix += "0";
				break;
			}
		}
		switch (fillType) {
		case LEFT_SPACE:
		case LEFT_ZERO:
			return fromANSI(fix + str.substring(0, maxLength));
		case RIGHT_SPACE:
		case RIGHT_ZERO:
			return fromANSI(str.substring(0, maxLength) + fix);
		}
		return str;
	}

	/**
	 * 根据给定的格式整理字符串，不限定最大长度（中文长度问题已经处理）
	 * 
	 * @param format
	 *            格式（同String.format）
	 * @param str
	 *            目标字符串
	 * @return 格式化后的字符串
	 */
	public static String format(String format, String str) {
		return fromANSI(String.format(format, toANSI(str)));
	}

	/**
	 * 把输入字符串，从默认编码集转至ANSI编码集，主要用于正确确定字符串长度所用（中文占2个byte）
	 */
	public static String toANSI(String str) {
		try {
			return new String(str.getBytes(), "ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			return str;
		}
	}

	/**
	 * 把输入字符串，从ANSI编码集转至默认编码集
	 */
	public static String fromANSI(String str) {
		try {
			return new String(str.getBytes("ISO-8859-1"));
		} catch (UnsupportedEncodingException e) {
			return str;
		}
	}

	/**
	 * 大写第一个字母（仅支持英文）
	 */
	public static String upperFirstLetter(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	/**
	 * 小写第一个字母（仅支持英文）
	 */
	public static String lowerFirstLetter(String str) {
		return str.substring(0, 1).toLowerCase() + str.substring(1);
	}

	/**
	 * 使用指定分隔符将数组连接成一个字符串输出
	 * 
	 * @param src
	 *            String数组
	 * @param sign
	 *            分隔符号
	 * @return
	 */
	public static String join(String[] src, String sign) {
		if (src == null) {
			return null;
		}
		String value = "";
		int len = src.length;
		for (int i = 0; i < len; i++) {
			value = value + src[i] + sign;
		}
		return value;
	}

	/**
	 * 使用指定分隔符将数组连接成一个字符串输出
	 * 
	 * @param src
	 *            Object数组
	 * @param delimiter
	 *            分隔符号
	 * @return
	 */
	public static String join(Object[] src, String delimiter) {
		if (src == null) {
			return null;
		}
		String value = "";
		int len = src.length;
		for (int i = 0; i < len; i++) {
			value = value + src[i].toString() + delimiter;
		}
		return value.substring(0, value.length() - delimiter.length());
	}

	/**
	 * 将float字符串整理为可控精度的字符串
	 * 
	 * @param precision
	 *            小数点后位数
	 * @param money
	 *            浮点数字的字符串
	 * @return
	 */
	public static String formatFloat(int precision, String money) {
		return String.format("%." + String.valueOf(precision) + "f",
				Float.valueOf(money));
	}

	/**
	 * 使用默认遮罩模式，屏蔽输入字符串，被屏蔽部分会被*取代
	 * @param target
	 * @return
	 */
	public static String mask(String target) {
		return mask(target, mask);
	}

	/**
	 * 使用指定的遮罩模式，屏蔽输入字符串，被屏蔽部分会被*取代
	 * @param target
	 * @param mask 遮罩模式，*或x表示遮罩位置
	 * @return
	 */
	public static String mask(String target, String mask) {
		if (target == null) {
			return null;
		}
		mask = mask.toLowerCase();
		String clear = target.trim();
		StringBuffer result = null;
		if (clear.length() < mask.length()) {
			return target;
		} else {
			result = new StringBuffer(clear.substring(0,
					clear.length() - mask.length()));
			String leftover = clear.substring(clear.length() - mask.length());
			for (int i = 0; i < mask.length(); i++) {
				if (mask.charAt(i) == 'x' || mask.charAt(i) == '*')
					result.append("*");
				else
					result.append(leftover.charAt(i));
			}
		}
		return result.toString();
	}

	/**
	 * 下划线格式字符串转换为驼峰格式： CAMEL_CASE => CamelCase
	 * 
	 * @param str
	 *            下划线格式字符串（大小写无关）
	 * @return 驼峰字符串
	 */
	public static String underscore2camelCase(String str) {
		str = str.toLowerCase();
		String[] parts = str.split("_");
		StringBuffer buffer = new StringBuffer("");
		for (String part : parts) {
			buffer.append(upperFirstLetter(part));
		}
		return buffer.toString();
	}

	/**
	 * 驼峰格式字符串转换为下划线格式： CamelCase => CAMEL_CASE
	 * 
	 * @param str
	 *            驼峰格式字符串（首字母大小写不影响结果）
	 * @return 下划线字符串
	 */
	public static String camelCase2underscore(String str) {
		return str.replaceAll("(\\p{Ll})(\\p{Lu})", "$1_$2").toUpperCase();
	}
	
    /**
     * 按定长分割字符串，不包含汉字
     * @param src 
     * @param len
     * @return
     */
    public static String[] splitStringByLen(String src, int len)
    {
        int i = 0; // 数组大小
        if (src.length() == 0) {
            return null;
        }
        if (src.length() % len == 0) {
            i = src.length() / len;
        } else {
            i = src.length() / len + 1;
        }
        String[] rs = new String[i];
        for (int j = 0; j < i; j++) {
            if (j == i - 1) {
                rs[j] = src.substring(len * j);
            } else {
                rs[j] = src.substring(len * j, len * (j + 1));
            }
        }
        return rs;
    }
    
    /**
     * 判断字符串是否为空
     *
     * @param str null、“ ”、“null”都返回true
     * @return
     */
    public static boolean isNullString(String str) {
        return (null == str || StringUtils.isBlank(str.trim()) || "null".equals(str.trim().toLowerCase()));
    }
    
    public static boolean haveNullString(String[] array) {
    	for (int i = 0; i < array.length; i++) {
			String str = array[i];
			if(null == str || StringUtils.isBlank(str.trim()) || "null".equals(str.trim().toLowerCase()))
				return true;
		}
        return false;
    }

    public static String join(String[] array) {
        if (array.length == 0) return "";
        StringBuilder sb = new StringBuilder();
        for (String s : array)
            sb.append(s);
        return sb.toString();
    }

    /**
     * 格式化字符串
     * 如果为空，返回“”
     *
     * @param str
     * @return
     */
    public static String formatString(String str) {
        if (isNullString(str)) {
            return "";
        } else {
            return str;
        }
    }

    public static String formatStringTrim(String str) {
        return formatString(str).trim();
    }

    /**
     * 截取字符串，字母、汉字都可以，汉字不会截取半
     *
     * @param str 字符串
     * @param n   截取的长度，字母数，如果为汉字，一个汉字等于两个字母数
     * @return
     */
    public static String subStringByByte(String str, int n) {
        int num = 0;
        try {
            byte[] buf = str.getBytes("GBK");
            if (n >= buf.length) {
                return str;
            }
            boolean bChineseFirstHalf = false;
            for (int i = 0; i < n; i++) {
                if (buf[i] < 0 && !bChineseFirstHalf) {
                    bChineseFirstHalf = true;
                } else {
                    num++;
                    bChineseFirstHalf = false;
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str.substring(0, num);
    }

    /**
     * Created by zhoujun on 2014/5/30
     * 验证输入的是否是数字
     *
     * @param num 字符串数字
     * @return
     */
    public static boolean inputIsNum(final String num) {
        boolean flag = true;
        try {
            Long.valueOf(num);
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    public static boolean inputIsDouble(String num) {
        boolean flag = true;
        try {
            Double.valueOf(num);
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 替换中间字符串
     *
     * @param str
     * @param n
     * @return
     */
    public static String replaceSubString(String str, int n) {
        String repaceStr = "";
        try {
            //前n位
            String headStr = str.substring(0, n);
            //后n位
            String lastStr = str.substring(str.length() - n, str.length());
            /*
			 * 中间的字符串替换*
			 */
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < str.length() - 2 * n; i++) {
                sb = sb.append("*");
            }
            repaceStr = headStr + sb.toString() + lastStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return repaceStr;
    }

    /**
     * @param ch
     * @param strs
     * @return
     * @说明：将字符串转成map格式,如"红色:#ff0000"转成key为"红色",值为"红色:#ff0000"
     * @author xuefeng
     * @date:2015-5-25 上午10:40:46
     */
    public static Map<String, String> subString(String ch, List<String> strs) {
        Map<String, String> retmap = new HashMap<>();
        for (String oldStr : strs) {
            int index = oldStr.indexOf(ch);
            if (index != -1) {
                String newStr = oldStr.substring(0, index);
                retmap.put(newStr, oldStr);
            } else {
                retmap.put(oldStr, oldStr);
            }
        }
        return retmap;
    }

    /**
     * @param ch
     * @param strs
     * @return
     * @throws
     * @说明：将字符串按指定分隔符分隔成数组后,再进行连接(默认连接方式为空字符串,待扩展)
     * @return: String
     * @author: xuefeng
     * @2015-5-25 上午09:22:38
     */
    public static String subString(String ch, String[] strs) {
        String retStr = "";
        for (String oldStr : strs) {
            int index = oldStr.indexOf(ch);
            if (index != -1) {
                String newStr = oldStr.substring(0, index);
                retStr += newStr;
            } else {
                retStr += oldStr;
            }
        }
        return retStr;
    }

    /**
     * @param arrStr
     * @return
     * @throws
     * @说明：对字符串数组中的元素进行排序
     * @return: String[]
     * @author: xuefeng
     * @2015-5-25 上午09:21:45
     */
    public static String[] sortStringArray(String[] arrStr) {
        for (int i = 0; i < arrStr.length - 1; i++) {
            String maxStr = arrStr[i];
            int index = i;
            for (int j = i + 1; j < arrStr.length; j++) {
                if (maxStr.compareTo(arrStr[j]) < 0) {
                    maxStr = arrStr[j];
                    index = j;
                }
            }
            arrStr[index] = arrStr[i];
            arrStr[i] = maxStr;
        }
        return arrStr;
    }

    /**
     * @param array1
     * @param array2
     * @return
     * @throws
     * @说明：比较两个字符串数组是否相等
     * @return: boolean
     * @author: xuefeng
     * @2015-5-25 上午09:22:06
     */
    public static boolean equalsStringArray(String[] array1, String[] array2) {
        if (array1 == array2)
            return true;
        if (array1 == null || array2 == null)
            return false;
        if (array1.length != array2.length)
            return false;
        array1 = sortStringArray(array1);
        array2 = sortStringArray(array2);
        for (int i = 0; i < array1.length; i++) {
            if (!array1[i].equals(array2[i]))
                return false;
        }
        return true;
    }


    public static String fetchAlipayProcessId(Long id) {
        String idstr = id.toString();
        if (idstr.length() < 3) {
            int bu = 3 - idstr.length();
            for (int i = 0; i < bu; i++) {
                idstr += "0";
            }
        }
        return idstr;
    }

    /**
     * 根据规则分割字符串
     *
     * @param str
     * @param regex
     * @return
     */
    public static String splitFirstStr(String str, String regex) {
        if (ShawStringUtils.isNullString(str)) {
            return null;
        } else {
            return str.split(regex)[0];
        }

    }

    public static String listToString(List<String> list) {
        StringBuilder sb = new StringBuilder();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (i < list.size() - 1) {
                    sb.append(list.get(i)).append(ConstantsUtil.separator_douhao);
                } else {
                    sb.append(list.get(i));
                }
            }
        }
        return sb.toString();
    }

    public static List<String> stringToList(String str) {
        String[] vals = str.split(ConstantsUtil.separator_douhao);
        return Arrays.asList(vals);
    }
    
    /**
     * 移除数组中空的内容
     * 
     * @param list
     * @return
     */
    public static <E> List<E> removeEmptyList(List<E> list) {  
    List<E> list1 = new ArrayList<E>();  
      if(list==null||list.size()<=0)  
          return null;  
      for(int i=0;i<list.size();i++) {  
          //进入每一个list  
          E listi = list.get(i);
          for (Field f : listi.getClass().getDeclaredFields()) {
              f.setAccessible(true);
              try {
  				if (!isEmpty(f.get(listi))) { 
  					list1.add(listi);  
  					break;
  				}
  			} catch (IllegalArgumentException | IllegalAccessException e) {
  				e.printStackTrace();
  				return list;
  			}
          }
      }  
      return list1;  
  }
    
    public static String longArrayToString(Long[] longArray){
		// 自定义一个字符缓冲区，  
        StringBuilder sb = new StringBuilder();  
        //遍历Long数组，并将Long数组中的元素转换成字符串储存到字符缓冲区中去  
        for(int i=0;i<longArray.length;i++){ 
        	sb.append(longArray[i]);
            if(i<longArray.length-1)  
                sb.append(ConstantsUtil.separator_douhao);  
        }  
        return sb.toString();
	}
	
    public static Long[] stringToLongArray(String str){
		String[] arr = str.split(ConstantsUtil.separator_douhao);
		Long[] longArray = new Long[arr.length];
		for(int i=0;i<arr.length;i++){
			longArray[i] = Long.valueOf(arr[i]);
		}
		return longArray;
	}

	public static LinkedList<Long> stringToLongList(String str){
		String[] arr = str.split(ConstantsUtil.separator_douhao);
        LinkedList<Long> longList = Lists.newLinkedList();
        for (String anArr : arr) {
            longList.add(Long.valueOf(anArr));
        }
		return longList;
	}

	public static String encodeFormate(String str) {
		if (isNullString(str)) {
			return "";
		} else {
			try {
				return URLEncoder.encode(str, "utf-8");
			} catch (UnsupportedEncodingException e) {
				return "";
			}
		}
	}
    
	public static <T> List<T> arrayDistinct(T[] newArray, T[] oldArray) {
		List<T> newList = Arrays.asList(newArray);
		List<T> distinctList = new ArrayList<T>();
		for (T t : oldArray) {
			if (!newList.contains(t)) {
				distinctList.add(t);
			}
		}
		return distinctList;
	}
	
    public static String formatAmount(BigDecimal amount) {
    	//double v = amount.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    	double v = amount.doubleValue();
        return String.format("%.2f", v);
    }

	// solr查询转义特殊字符(*和？除外)
	public static String escapeQueryChars(String s) {  
	    StringBuilder sb = new StringBuilder();  
	    for (int i = 0; i < s.length(); i++) {  
	    	char c = s.charAt(i);  
	    	// These characters are part of the query syntax and must be escaped  
	    	if (c == '\\' || c == '+' || c == '-' || c == '!'  || c == '(' || c == ')' || c == ':'  
	    		|| c == '^' || c == '[' || c == ']' || c == '\"' || c == '{' || c == '}' || c == '~'  
	    		|| c == '*' || c == '?' || c == '|' || c == '&'  || c == ';' || c == '/'  
	    		|| Character.isWhitespace(c)) {  
	    		sb.append('\\');  
	    	}  
	    	sb.append(c);  
	    }  
	    return sb.toString();  
	}

    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }
}

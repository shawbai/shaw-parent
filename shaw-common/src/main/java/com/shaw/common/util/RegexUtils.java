package com.shaw.common.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.shaw.common.constant.ConstantsUtil;

/**
 * 正则工具类 提供验证邮箱、手机号、电话号码、身份证号码、数字等方法
 */
public final class RegexUtils {


	public final static String EmailReg = "\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?";
	
	/**
	 * 验证Email
	 * 
	 * @param email
	 *            email地址，格式：zhangsan@sina.com，zhangsan@xxx.com.cn，xxx代表邮件服务商
	 * @return 验证成功返回true，验证失败返回false
	 */
	public static boolean checkEmail(String email) {
		return Pattern.matches(EmailReg, email);
	}

	
	public final static String IdCardReg = "[1-9]\\d{13,16}[a-zA-Z0-9]{1}";
	/**
	 * 验证身份证号码
	 * 
	 * @param idCard
	 *            居民身份证号码15位或18位，最后一位可能是数字或字母
	 * @return 验证成功返回true，验证失败返回false
	 */
	public static boolean checkIdCard(String idCard) {
		return Pattern.matches(IdCardReg, idCard);
	}

	public final static String MobileReg = "(\\+\\d+)?1[3458]\\d{9}$";
	/**
	 * 验证手机号码（支持国际格式，+86135xxxx...（中国内地），+00852137xxxx...（中国香港））
	 * 
	 * @param mobile
	 *            移动、联通、电信运营商的号码段
	 *            <p>
	 *            移动的号段：134(0-8)、135、136、137、138、139、147（预计用于TD上网卡）
	 *            、150、151、152、157（TD专用）、158、159、187（未启用）、188（TD专用）
	 *            </p>
	 *            <p>
	 *            联通的号段：130、131、132、155、156（世界风专用）、185（未启用）、186（3g）
	 *            </p>
	 *            <p>
	 *            电信的号段：133、153、180（未启用）、189
	 *            </p>
	 * @return 验证成功返回true，验证失败返回false
	 */
	public static boolean checkMobile(String mobile) {
		return Pattern.matches(MobileReg, mobile);
	}

	public final static String PhoneReg = "(\\+\\d+)?(\\d{3,4}\\-?)?\\d{7,8}$";
	/**
	 * 验证固定电话号码
	 * 
	 * @param phone
	 *            电话号码，格式：国家（地区）电话代码 + 区号（城市代码） + 电话号码，如：+8602085588447
	 *            <p>
	 *            <b>国家（地区） 代码 ：</b>标识电话号码的国家（地区）的标准国家（地区）代码。它包含从 0 到 9
	 *            的一位或多位数字， 数字之后是空格分隔的国家（地区）代码。
	 *            </p>
	 *            <p>
	 *            <b>区号（城市代码）：</b>这可能包含一个或多个从 0 到 9 的数字，地区或城市代码放在圆括号——
	 *            对不使用地区或城市代码的国家（地区），则省略该组件。
	 *            </p>
	 *            <p>
	 *            <b>电话号码：</b>这包含从 0 到 9 的一个或多个数字
	 *            </p>
	 * @return 验证成功返回true，验证失败返回false
	 */
	public static boolean checkPhone(String phone) {
		return Pattern.matches(PhoneReg, phone);
	}
	

	public final static String DigitReg = "\\-?[1-9]\\d+";
	/**
	 * 验证整数（正整数和负整数）
	 * 
	 * @param digit
	 *            一位或多位0-9之间的整数
	 * @return 验证成功返回true，验证失败返回false
	 */
	public static boolean checkDigit(String digit) {
		return Pattern.matches(DigitReg, digit);
	}

	public final static String DecimalsReg = "\\-?[1-9]\\d+(\\.\\d+)?";
	/**
	 * 验证整数和浮点数（正负整数和正负浮点数）
	 * 
	 * @param decimals
	 *            一位或多位0-9之间的浮点数，如：1.23，233.30
	 * @return 验证成功返回true，验证失败返回false
	 */
	public static boolean checkDecimals(String decimals) {
		return Pattern.matches(DecimalsReg, decimals);
	}

	public final static String BlankSpaceReg = "\\s+";
	/**
	 * 验证空白字符
	 * 
	 * @param blankSpace
	 *            空白字符，包括：空格、\t、\n、\r、\f、\x0B
	 * @return 验证成功返回true，验证失败返回false
	 */
	public static boolean checkBlankSpace(String blankSpace) {
		return Pattern.matches(BlankSpaceReg, blankSpace);
	}

	public final static String ChineseReg = "^[\u4E00-\u9FA5]+$";
	/**
	 * 验证中文
	 * 
	 * @param chinese
	 *            中文字符
	 * @return 验证成功返回true，验证失败返回false
	 */
	public static boolean checkChinese(String chinese) {
		return Pattern.matches(ChineseReg, chinese);
	}

	public final static String BirthdayReg = "[1-9]{4}([-./])\\d{1,2}\\1\\d{1,2}";
	/**
	 * 验证日期（年月日）
	 * 
	 * @param birthday
	 *            日期，格式：1992-09-03，或1992.09.03
	 * @return 验证成功返回true，验证失败返回false
	 */
	public static boolean checkBirthday(String birthday) {
		return Pattern.matches(BirthdayReg, birthday);
	}

	public final static String Postcode = "[1-9]\\d{5}";
	/**
	 * 匹配中国邮政编码
	 * 
	 * @param postcode
	 *            邮政编码
	 * @return 验证成功返回true，验证失败返回false
	 */
	public static boolean checkPostcode(String postcode) {
		return Pattern.matches(Postcode, postcode);
	}

	public final static String IpAddress = "[1-9](\\d{1,2})?\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))";
	/**
	 * 匹配IP地址(简单匹配，格式，如：192.168.1.1，127.0.0.1，没有匹配IP段的大小)
	 * 
	 * @param ipAddress
	 *            IPv4标准地址
	 * @return 验证成功返回true，验证失败返回false
	 */
	public static boolean checkIpAddress(String ipAddress) {
		return Pattern.matches(IpAddress, ipAddress);
	}
	
	
	public final static String URLReg = "(https?://(w{3}\\.)?)?\\w+\\.\\w+(\\.[a-zA-Z]+)*(:\\d{1,5})?(/\\w*)*(\\??(.+=.*)?(&.+=.*)?)?";
	/**
	 * 验证URL地址
	 * 
	 * @param url
	 *            格式：http://blog.csdn.net:80/xyang81/article/details/7705960? 或
	 *            http://www.csdn.net:80
	 * @return 验证成功返回true，验证失败返回false
	 */
	public static boolean checkURL(String url) {
		return Pattern.matches(URLReg, url);
	}
	
	 /**
     * <pre>
     * 获取网址 URL 的一级域
     * </pre>
     * 
     * @param url http://www.baidu.com
     * @return baidu.com
     */
    public static String getDomain(String url) {
        try {
            Pattern p = Pattern
                    .compile("(?<=http(s?)://|\\.)[^.]*?\\.(com\\.cn|org\\.cn|net\\.cn|com|cn|net|org|biz|info|cc|tv|me|edu|gov|uk|pro|top|so|la|io)", Pattern.CASE_INSENSITIVE);
            Matcher matcher = p.matcher(URLDecoder.decode(url, "UTF-8"));
            matcher.find();
            return matcher.group();//baidu.com
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * <pre>
     * 获取网址 URL地址
     * </pre>
     * 
     * @param url http://www.baidu.com
     * @return www.baidu.com
     */
    public static String getUrl(String url) {
        try {
            Pattern p = Pattern.compile("[^//]*?\\.(com\\.cn|org\\.cn|net\\.cn|com|cn|net|org|biz|info|cc|tv|me|edu|gov|uk|pro|top|so|la|io|ml)", Pattern.CASE_INSENSITIVE);  
            Matcher matcher = p.matcher(URLDecoder.decode(url, "UTF-8"));
            matcher.find();
            return matcher.group();//baidu.com
        } catch (Exception e) {
            return null;
        }
    }

    public static String getDomainName(String url) {
        try {
            Pattern p = Pattern.compile("http(s?)(://)([^/]+)(.*?)", Pattern.CASE_INSENSITIVE);
            Matcher matcher = p.matcher(URLDecoder.decode(url, "UTF-8"));
            matcher.find();
            return matcher.group().contains("localhost") ? "localhost" : matcher.group();//baidu.com
        } catch (Exception e) {
            return "localhost";
        }
    }
    
    public static String getDomainNameByUrl(String url) throws URISyntaxException {
        if (url.startsWith(ConstantsUtil.HTTP_PREFIX)) {
            if (!url.contains(ConstantsUtil.HTTP_PREFIX)) {
                url = url.replaceAll("http:/", ConstantsUtil.HTTP_PREFIX);
            }
        } else {
            url = ConstantsUtil.HTTP_PREFIX + url;
        }
        URI uri = new URI(url);
        String domain = uri.getHost();
        return domain.startsWith("www.") ? domain.substring(4) : domain;
    }
    
    /**
     * @param url
     * @return
     * @Description: 获取域名
     * @author: wanglei
     * @date:2016-9-13下午2:56:23
     */
    public static String getWebSite(String url) {
        try {
            Pattern p = Pattern.compile("http(s?)(://)([^/]+)(.*?)", Pattern.CASE_INSENSITIVE);
            Matcher matcher = p.matcher(URLDecoder.decode(url, "UTF-8"));
            matcher.find();
            return matcher.group();
        } catch (Exception e) {
            return null;
        }
    }
    

}
package com.shaw.common.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Copyright (C), 2012-2014, 苏州海云融通有限公司
 * Author:   Wanglei
 * Date:    2016-1-12下午2:36:28
 * Description:  CookieUtils 工具类
 * <author>      <time>      <version>    <desc>
 * 修改人姓名        修改时间             版本号              描述
 */
public final class CookieUtils {

    /**
     * 得到Cookie的值, 不编码
     *
     * @param request
     * @param cookieName
     * @return
     */
    public static String getCookieValue(HttpServletRequest request, String cookieName) {
        return getCookieValue(request, cookieName, false);
    }

    /**
     * 得到Cookie的值,
     *
     * @param request
     * @param cookieName
     * @return
     */
    public static String getCookieValue(HttpServletRequest request, String cookieName, boolean isDecoder) {
        Cookie[] cookieList = request.getCookies();
        if (cookieList == null || cookieName == null) {
            return null;
        }
        String retValue = null;
        try {
            for (Cookie aCookieList : cookieList) {
                if (aCookieList.getName().equals(cookieName)) {
                    if (isDecoder) {
                        retValue = URLDecoder.decode(aCookieList.getValue(), "UTF-8");
                    } else {
                        retValue = aCookieList.getValue();
                    }
                    break;
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return retValue;
    }

    /**
     * 得到Cookie的值,
     *
     * @param request
     * @param cookieName
     * @return
     */
    public static String getCookieValue(HttpServletRequest request, String cookieName, String encodeString) {
        Cookie[] cookieList = request.getCookies();
        if (cookieList == null || cookieName == null) {
            return null;
        }
        String retValue = null;
        try {
            for (Cookie aCookieList : cookieList) {
                if (aCookieList.getName().equals(cookieName)) {
                    retValue = URLDecoder.decode(aCookieList.getValue(), encodeString);
                    break;
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return retValue;
    }

    /**
     * 设置Cookie的值 不设置生效时间默认浏览器关闭即失效,也不编码
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
                                 String cookieValue) {
        setCookie(request, response, cookieName, cookieValue, -1);
    }

    /**
     * 设置Cookie的值 不设置生效时间,但编码
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
                                 String cookieValue, String encodeString) {
        setCookie(request, response, cookieName, cookieValue, -1, encodeString);
    }

    //httponly 设置的
    public static void setCookie(boolean httpOnly, HttpServletRequest request, HttpServletResponse response, String cookieName, String cookieValue) {
        doSetCookie(request, response, cookieName, cookieValue, -1, null, httpOnly);
    }

    //httponly 设置的 指定时长
    public static void setCookie(boolean httpOnly, HttpServletRequest request, HttpServletResponse response, String cookieName, String cookieValue,int cookieMaxage) {
        doSetCookie(request, response, cookieName, cookieValue, cookieMaxage, null, httpOnly);
    }

    /********************************************************************************************************************/

    /**
     * 设置Cookie的值 在指定时间内生效,但不编码
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
                                 String cookieValue, int cookieMaxage) {
        setCookie(request, response, cookieName, cookieValue, cookieMaxage, null);
    }

    /**
     * 设置Cookie的值 在指定时间内生效, 编码参数
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
                                 String cookieValue, int cookieMaxage, String encodeString) {
        doSetCookie(request, response, cookieName, cookieValue, cookieMaxage, encodeString, false);
    }

    public static void setCookieCurrentDomain(HttpServletRequest request, HttpServletResponse response, String cookieName, String cookieValue, int cookieMaxage) {
        doSetCookie(request, response, cookieName, cookieValue, cookieMaxage, null, false, true);
    }

    public static void deleteCurrentDomainCookie(HttpServletRequest request, HttpServletResponse response, String cookieName) {
        doSetCookie(request, response, cookieName, "", -1, null, false, true);
    }

    // 两周内记住登录状态 setMaxAge 单位秒 会话保持应大于两周 取15天 -->
    public static void postponeExpiration2Weeks(boolean httpOnly, HttpServletRequest request, HttpServletResponse response, String cookieName, String cookieValue) {
        doSetCookie(request, response, cookieName, cookieValue, 60 * 60 * 24 * 15, null, httpOnly);
    }


    /**
     * 删除Cookie带cookie域名
     */
    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response,
                                    String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (null != cookies && cookies.length > 0) {
            for (Cookie c : cookies) {
                if (cookieName.equals(c.getName())) {
                    c.setMaxAge(0);
                    String requestURL = request.getRequestURL().toString();
                    String domainName = RegexUtils.getDomainName(requestURL);
                    if (!"".equals(domainName)) {
                        c.setDomain(domainName);
                    } else {
                        c.setDomain("localhost");
                    }
                    c.setPath("/");
                    response.addCookie(c);
                }
            }
        }
    }

    private static void doSetCookie(HttpServletRequest request, HttpServletResponse response,
                                    String cookieName, String cookieValue, int cookieMaxage, String encodeString, boolean httpOnly) {

        doSetCookie(request, response, cookieName, cookieValue, cookieMaxage, encodeString, httpOnly, false);
    }

    /**
     * 设置Cookie的值，并使其在指定时间内生效
     *
     * @param cookieMaxage cookie生效的最大秒数
     *                     isCurrentDomain为true即设置cookie为当前域名（包括二级域名等）
     */
    private static void doSetCookie(HttpServletRequest request, HttpServletResponse response,
                                    String cookieName, String cookieValue, int cookieMaxage, String encodeString, boolean httpOnly, boolean isCurrentDomain) {
        try {
            if (cookieValue == null) {
                cookieValue = "";
            } else {
                if (encodeString != null) {
                    cookieValue = URLEncoder.encode(cookieValue, encodeString);
                } else {
                    cookieValue = URLEncoder.encode(cookieValue, "utf-8");
                }
            }
            Cookie cookie = new Cookie(cookieName, cookieValue);
            // if (cookieMaxage > 0)
                cookie.setMaxAge(cookieMaxage);

            if (null != request && !isCurrentDomain) {// 设置域名的cookie
            	String requestURL = request.getRequestURL().toString();
                String domainName = RegexUtils.getDomainName(requestURL);
                if (!"".equals(domainName)) {
                    cookie.setDomain(domainName);
                }
            }
            cookie.setPath("/");
            cookie.setHttpOnly(httpOnly);
            response.addCookie(cookie);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    

//    private static String getDomainName(HttpServletRequest request) {
//        return RegexUtils.getDomainName(request.getRequestURL().toString());
//    }



//	public static void main(String[] args) {
//		 //String domain = getDomainName("https://luzhenghaotea.tmall.com/?ali_trackid=30_25bee94daaed7c19261ac7d165eae831&spm=a21bo.50862.201862-2.1.IGrBIt");
//		 //String domain = getDomainName("https://baidu.com");
//		 String domain = getDomainName("http://shawsheng.uicp.io/passport/qqLogin.do");
//		 String website = HaiyunRegexUtils.getWebSite("https://luzhenghaotea.tmall.com/?ali_trackid=30_25bee94daaed7c19261ac7d165eae831&spm=a21bo.50862.201862-2.1.IGrBIt");
//		 System.err.println("  "+domain);
//			System.err.println(getUrl("https://luzhenghaotea.tmall.com/?ali_trackid=30_25bee94daaed7c19261ac7d165eae831&spm=a21bo.50862.201862-2.1.IGrBIt"));
//			// TODO Auto-generated catch block
//		 System.err.println(website);
//	}
}

package com.shaw.common.util;

/**
 * Byte工具类：异或、BCD、hexString与 byteArray转换、子byteArray
 * @author shaw
 */
public class ByteUtils {

    /**
     * 取异或
     * @param aByte
     * @param bByte
     * @return 
     */
    public static byte[] XOR(byte[] aByte, byte[] bByte) {
        if (aByte.length != bByte.length) {
            return null;
        }
        byte[] resultXors = new byte[aByte.length];
        for (int i = 0; i < aByte.length; i++) {
            int b = (int) aByte[i] ^ (int) bByte[i];
            resultXors[i] = (byte) b;
        }
        return resultXors;
    }

    /**
     * 字符串转成BCD码
     * @param str 
     * @return
     */
    public static byte[] str2BCD(String str) {
        int len = str.length();
        int mod = len % 2;
        if (mod != 0) {
            str = "0" + str;
            len = str.length();
        }
        byte[] abt = new byte[len];
        if (len >= 2) {
            len = len / 2;
        }
        byte[] bbt = new byte[len];
        abt = str.getBytes();
        int j, k;
        for (int p = 0; p < str.length() / 2; p++) {
            if ((abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {
                j = abt[2 * p] - '0';
            } else if ((abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {
                j = abt[2 * p] - 'a' + 0x0a;
            } else {
                j = abt[2 * p] - 'A' + 0x0a;
            }

            if ((abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {
                k = abt[2 * p + 1] - '0';
            } else if ((abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {
                k = abt[2 * p + 1] - 'a' + 0x0a;
            } else {
                k = abt[2 * p + 1] - 'A' + 0x0a;
            }
            int a = (j << 4) + k;
            byte b = (byte) a;
            bbt[p] = b;
        }
        return bbt;
    }

    /**
     * 十六进制字符串转成byte数组
     * @param hexString
     */
    public static byte[] hexString2byteArray(String hexString) {
        int strHexLen = hexString.getBytes().length / 2;
        byte[] hexBytes = new byte[strHexLen];
        for (int i = 0; i < strHexLen; i++) {
            hexBytes[i] = (byte) Integer.parseInt(hexString.substring(i * 2, i * 2 + 2), 16);
        }
        return hexBytes;
    }
    
    /**
     * byte数组转化为十六进制字符串
     * @param byteArray 
     */
    public static String byteArray2hexString(byte byteArray[]) {
        String s = "";
        for (int i = 0; i < byteArray.length; i++) {
            byte byte0 = byteArray[i];
            String s1 = Integer.toHexString(byte0);
            if (s1.length() > 2) {
                s1 = s1.substring(s1.length() - 2);
            }
            if (s1.length() < 2) {
                s1 = "0" + s1;
            }
            s = s + s1;
        }
        return s;
    }

    /**
     * 从起始位置(startPos)开始取src长度为len的数据,如果len=0则结果为src剩下的字符串
     * @param src
     * @param startPos 起始位置
     * @param len 要获取的字符串长度
     * @return
     */
    public static byte[] mid(byte[] srcBytes, int startPos, int len) {
        if (srcBytes == null || srcBytes.length == 0) {
            return null;
        }
        if (len < 0 || startPos < 0) {
            return null;
        }
        if (srcBytes.length < (startPos + len)) {
            return null;
        }
        if (len == 0) {//如果len小于0，则获取剩下的字符串
            len = srcBytes.length - startPos;
        }
        byte[] resBytes = new byte[len];
        System.arraycopy(srcBytes, startPos, resBytes, 0, len);
        return resBytes;
    }
}
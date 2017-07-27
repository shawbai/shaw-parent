package com.shaw.common.encrypt;

import java.security.MessageDigest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DigestUtils {
	private static Logger logger = LoggerFactory.getLogger(DigestUtils.class);

	// MD5 encrypt

	public enum MD5Length {
		L16(16), L32(32);
		public int length;

		private MD5Length(int length) {
			this.length = length;
		}
	}

	public static String MD5(String str, MD5Length lenType) {
		int encryptLen = lenType.length;
		String result = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			byte[] b = md.digest();
			StringBuffer buf = new StringBuffer("");
			int i;
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}

			if (encryptLen == 16) {
				result = buf.toString().substring(8, 24);
			} else {
				result = buf.toString();
			}
			return result;
		} catch (Exception e) {
			logger.error("{},str2Md5 error:", encryptLen, e);
		}
		return result;
	}

	public static String SHA1(String str) {
		try {
			return encryResult(str, "SHA-1");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("SHA1 encrypt error:", e);
		}
		return "";
	}

	public static String SHA(String str) {
		try {
			return encryResult(str, "SHA");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("SHA encrypt error:", e);
		}
		return "";
	}

	private static String encryResult(String str, String type) {
		try {
			MessageDigest digest = MessageDigest.getInstance(type);
			digest.update(str.getBytes());
			byte messageDigest[] = digest.digest();
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < str.getBytes().length; i++) {
				String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
				if (shaHex.length() < 2) {
					hexString.append(0);
				}
				hexString.append(shaHex);
			}
			return hexString.toString();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("encryResult encrypt error:", e);
		}
		return "";
	}
}

package com.shaw.common.email;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;


public class MailUtil {
	private static Email email = null;

	// 阿里云企业邮箱
	public static Email getAliyunQiyeEmail() {
		defaultEmail();
		email.setHostName("smtp.mxhichina.com");
		return email;
	}

	// 163邮箱
	public static Email get163Email() {
		defaultEmail();
		email.setHostName("smtp.163.com");
		return email;
	}

	// 163vip邮箱
	public static Email get163VipEmail() {
		defaultEmail();
		email.setHostName("smtp.vip.163.com");
		return email;
	}

	// 126邮箱
	public static Email get126Email() {
		defaultEmail();
		email.setHostName("smtp.126.com");
		return email;
	}

	// gmail邮箱
	public static Email getGmailEmail() {
		defaultEmail();
		email.setHostName("smtp.gmail.com");
		return email;
	}

	// yahoo邮箱
	public static Email getYahooEmail() {
		defaultEmail();
		email.setHostName("smtp.mail.yahoo.com");
		return email;
	}

	// hotmail邮箱
	public static Email getHotmailEmail() {
		defaultEmail();
		email.setHostName("smtp.live.com");
		return email;
	}

	// qq邮箱
	public static Email getQQEmail() {
		defaultEmail();
		email.setHostName("smtp.qq.com");
		return email;
	}

	// qq企业邮箱
	public static Email getQQQiyeEmail() {
		defaultEmail();
		email.setHostName("smtp.exmail.qq.com");
		return email;
	}

	// sina/企业邮箱
	public static Email getSinaEmail() {
		defaultEmail();
		email.setHostName("smtp.sina.com");
		return email;
	}

	// sinavip邮箱
	public static Email getSinaVipEmail() {
		defaultEmail();
		email.setHostName("smtp.vip.sina.com");
		return email;
	}

	// souhu邮箱
	public static Email getSouhuEmail() {
		defaultEmail();
		email.setHostName("smtp.sina.com");
		return email;
	}

	public static void defaultEmail() {
		email = new SimpleEmail();
		email.setSSLOnConnect(true);
		email.setSmtpPort(465);
	}

}

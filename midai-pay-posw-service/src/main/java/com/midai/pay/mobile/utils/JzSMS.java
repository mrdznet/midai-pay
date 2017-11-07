package com.midai.pay.mobile.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jianzhou.sdk.BusinessService;

/**
 * 
 * @author peipei
 * @time 2014-11-05
 *
 */
public class JzSMS {

	private static Logger logger = LoggerFactory.getLogger(JzSMS.class);

	private JzSMS() {

	}

	// 已经自行实例化
	private static final BusinessService single = new BusinessService();
	public static String url = "";
	private static String account = "";
	private static String password = "";
	private static String boottitle = "";
	public static String CALLCENTER = "";
	private static String accountzf = null;
	private static String passwordzf = null;
	private static String boottitlezf = "";
	public static String CALLCENTERZF = "";
	private static boolean mobileSwitch = false;

	// 静态工厂方法
	public static BusinessService getInstance() {
		return single;
	}

	public static int sendSMS(String phonenumber, String content, int type) {

		return 1;
	}

	public static void loadProperties() throws IOException {
		// InputStream instream =
		// JzSMS.class.getClassLoader().getResourceAsStream("com/uen/sms/jzsms.properties");
		InputStream instream = JzSMS.class.getClassLoader().getResourceAsStream("application.properties");
		Properties pp = null;
		pp = new Properties();
		pp.load(instream);
		url = pp.getProperty("mobile.url");
		account = pp.getProperty("mobile.account");
		password = pp.getProperty("mobile.password");
		boottitle = pp.getProperty("mobile.boottitle");
		CALLCENTER = pp.getProperty("mobile.CALLCENTER");
		accountzf = pp.getProperty("mobile.accountzf");
		passwordzf = pp.getProperty("mobile.passwordzf");
		boottitlezf = pp.getProperty("mobile.boottitlezf");
		CALLCENTERZF = pp.getProperty("mobile.CALLCENTERZF");
		mobileSwitch = Boolean.parseBoolean(pp.getProperty("mobile.switch"));
	}

	public static int sendSMS(String phonenumber, String content, int type, String inscode) {
		BusinessService bs = JzSMS.getInstance();
		int sendMessCount = 0;
		try {
		    if(StringUtils.isEmpty(url)){
		    	loadProperties();
		    }
			bs.setWebService(url);
			String account1 = "";
			String password1 = "";
			String boottitle1 = "";
			String td = "【安心智付】";
			if (null == inscode || inscode.isEmpty()) {
				inscode = "101";
			}
			if (inscode.equals("101") || inscode.equals("101002")) {
				account1 = JzSMS.account;
				password1 = JzSMS.password;
				boottitle1 = JzSMS.boottitle;
				content = content.replace("CALLCENTER", JzSMS.CALLCENTER);
			}
			if (inscode.equals("101004")) {
				account1 = JzSMS.accountzf;
				password1 = JzSMS.passwordzf;
				boottitle1 = JzSMS.boottitlezf;
				content = content.replace("CALLCENTER", JzSMS.CALLCENTERZF);
				td = "【掌上支付】";
			}
			System.out.println(account1 + phonenumber);
			if (mobileSwitch) {
				sendMessCount = bs.sendBatchMessage(account1, password1, phonenumber, content + "" + boottitle1);
			} else {
				sendMessCount = -1;
			}
			if (sendMessCount < 0) {
				logger.error("#################### " + td + "# 手机号：" + phonenumber + "# 返回值：" + sendMessCount + "# 发送短信失败！");
			} else {
				logger.error("####################" + td + "# 手机号：" + phonenumber + "# 返回值：" + sendMessCount + "# 发送短信成功！");
			}
		} catch (IOException e) {
			logger.error("#################### 发送短信异常！#################### ");
			logger.error(e.getMessage());
			logger.error("#####################################################  ");
			return -1;
		}
		
		return sendMessCount;
	}

}

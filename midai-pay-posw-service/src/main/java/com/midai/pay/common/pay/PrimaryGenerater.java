package com.midai.pay.common.pay;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

public class PrimaryGenerater {

	private static final String SERIAL_NUMBER = "AAAA"; // 流水号格式
	private static final String SERIAL_NUMBER1 = "AAA";
	private static PrimaryGenerater primaryGenerater = null;

	private PrimaryGenerater() {
	}

	/**
	 * 取得PrimaryGenerater的单例实现
	 *
	 * @return
	 */
	public static PrimaryGenerater getInstance() {
		if (primaryGenerater == null) {
			synchronized (PrimaryGenerater.class) {
				if (primaryGenerater == null) {
					primaryGenerater = new PrimaryGenerater();
				}
			}
		}
		return primaryGenerater;
	}

	/**
	 * 生成下一个编号
	 */
	public synchronized String generaterNextNumber(String sno) {
		String id = null;
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		if (sno == null) {
			id = formatter.format(date) + "0001";
		} else {
			int count = SERIAL_NUMBER.length();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < count; i++) {
				sb.append("0");
			}
			DecimalFormat df = new DecimalFormat("0000");
			id = formatter.format(date) + df.format(1 + Integer.parseInt(sno.substring(8, 12)));
		}
		return id;
	}

	// 得到快速提现的流水号
	public synchronized String getTXNextNumber(String sno) {
		String id = null;
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		if (sno == null) {
			id = formatter.format(date) + "001";
		} else {
			int count = SERIAL_NUMBER1.length();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < count; i++) {
				sb.append("0");
			}
			DecimalFormat df = new DecimalFormat("000");
			id = formatter.format(date) + df.format(1 + Integer.parseInt(sno.substring(8, 11)));
		}
		return id;
	}

	// 民生银行
	public synchronized static String generaterNextNum(String sno) {
		String id = null;
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		if (sno == null) {
			id = formatter.format(date) + "00001";
		} else {
			// int count = SERIAL_NUMBER.length();
			// StringBuilder sb = new StringBuilder();
			// for (int i = 0; i < count; i++) {
			// sb.append("0");
			// }
			DecimalFormat df = new DecimalFormat("00000");
			id = formatter.format(date) + df.format(1 + Integer.parseInt(sno.substring(14, 19)));
		}
		return id;
	}

	// 银盛
	public synchronized static String generaterNextNum2(String sno) {
		String id = null;
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmm");
		if (sno == null) {
			id = formatter.format(date) + "001";
		} else {
			int count = SERIAL_NUMBER1.length();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < count; i++) {
				sb.append("0");
			}
			DecimalFormat df = new DecimalFormat("000");
			id = formatter.format(date) + df.format(1 + Integer.parseInt(sno.substring(12, 15)));
		}
		return id;
	}

	// 生成通联编号
	public synchronized static String generaterNextNum1(String sno) {
		String id = null;
		if (sno == null) {
			id = "000001";
		} else {
			int count = SERIAL_NUMBER.length();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < count; i++) {
				sb.append("0");
			}
			DecimalFormat df = new DecimalFormat("000000");
			id = df.format(1 + Integer.parseInt(sno));
		}
		return id;
	}

	// 生成钜真打款金额
	public synchronized static String generaterpaymoney(String money) {
		StringBuilder sb = new StringBuilder();
		int count = 15 - (money.length());

		for (int i = 0; i <= count - 1; i++) {
			sb.append("0");
			// sb=sb.append(money);
			/*
			 * DecimalFormat df = new DecimalFormat("000000"); id =df.format(1 +
			 * Integer.parseInt(money));
			 */
		}
		return sb.toString();
	}

	// 生成矩真打款流水号
	public synchronized static String generaterpaylogno(String orderId) {
		StringBuilder sb = new StringBuilder();

		int lognocount = orderId.length();
		int count = 20 - lognocount; // 得到我们流水号与钜真流水号相差的长度

		for (int i = 0; i <= count - 1; i++) {
			sb.append("0");
		}

		return sb.toString();
	}

	/**
	 * 生成打款金额
	 * 
	 * @param money
	 *            原始金额
	 * @param weishu
	 *            位数
	 * @return
	 */
	public synchronized static String generaterMishuaPayMoney(String money, int weishu) {
		String id = null;
		int count = weishu - (money.length());
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i <= count - 1; i++) {
			sb.append("0");
		}
		System.out.println(sb);
		return sb.toString();
	}

	public static void main(String[] args) {
		String id = PrimaryGenerater.getInstance().generaterNextNumber("201606040001");
		// System.out.println(id);

		String tixianamt = "925";
		// String mishua = generaterMishuaPayMoney("9.25",9);
		String paymoney = PrimaryGenerater.generaterMishuaPayMoney(tixianamt, 9) + tixianamt;
		System.out.println(paymoney);
	}

}
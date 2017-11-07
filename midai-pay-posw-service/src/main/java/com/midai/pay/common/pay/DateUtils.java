package com.midai.pay.common.pay;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author ct @version 创建时间：2014-12-1 下午3:31:45 @category
 */
public class DateUtils {
	/***
	 * yyyy-MM-dd hh:mm:ss
	 */
	public static final String SMALL_FULL_DATE_TIME = "yyyy-MM-dd hh:mm:ss";
	/**
	 * yyyy-MM-dd HH:mm:ss
	 */
	public static final String BIG_FULL_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
	/**
	 * yyyyMMddHHmmssS
	 */
	public static final String NO_STYLE_FULL_DATE_TIME = "yyyyMMddHHmmssS";
	/**
	 * yyyyMMddHHmm
	 */
	public static final String NO_STYLE_FULL_DATE_TIME_MINUS_ssS = "yyyyMMddHHmm";
	/**
	 * yyyy-MM-dd
	 */
	public static final String _DATE = "yyyy-MM-dd";
	/**
	 * yyyyMMdd
	 */
	public static final String NO_STYLE_DATE = "yyyyMMdd";
	/**
	 * yyyyMM
	 */
	public static final String YM = "yyyyMM";
	/**
	 * yyyyMMddHHmm
	 */
	public static final String yyyyMMddHHmmss = "yyyyMMddHHmmss";

	/**
	 * 12位时间戳-yyMMddHHmmss
	 */
	public static final String yyMMddHHmmss = "yyMMddHHmmss";

	/**
	 * @param dateFormat
	 *            1: NORMAL_DATE_FORMAT_NEW = "yyyy-MM-dd hh:mm:ss" 2:
	 *            DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss" 3: DATE_ALL =
	 *            "yyyyMMddHHmmssS" 4: DATE_FORMAT = "yyyy-MM-dd" 5:
	 *            COMPACT_DATE_FORMAT = "yyyyMMdd" 6: YM = "yyyyMM" 7:
	 *            NO_STYLE_FULL_DATE_TIME_MINUS_ssS = "yyyyMMddHHmm"
	 * @return
	 */
	public static final String getNowDate(int dateFormat) {
		SimpleDateFormat format = getSimpleDateFormat(dateFormat);
		return format.format(new Date());
	}

	private static final SimpleDateFormat getSimpleDateFormat(int dateFormat) {
		switch (dateFormat) {
		case 1:
			return new SimpleDateFormat(SMALL_FULL_DATE_TIME);
		case 2:
			return new SimpleDateFormat(BIG_FULL_DATE_TIME);
		case 3:
			return new SimpleDateFormat(NO_STYLE_FULL_DATE_TIME);
		case 4:
			return new SimpleDateFormat(_DATE);
		case 5:
			return new SimpleDateFormat(NO_STYLE_DATE);
		case 6:
			return new SimpleDateFormat(YM);
		case 7:
			return new SimpleDateFormat(NO_STYLE_FULL_DATE_TIME_MINUS_ssS);
		case 8:
			return new SimpleDateFormat(yyyyMMddHHmmss);
		case 9:
			return new SimpleDateFormat(yyMMddHHmmss);
		default:
			return new SimpleDateFormat(BIG_FULL_DATE_TIME);
		}
	}

	public static String format(long date, String pattern) {
		return format(new Date(date), pattern);
	}

	public static String format(Date date, String pattern) {
		DateFormat df = createDateFormat(pattern);
		return df.format(date);
	}

	public static String format(String inputDate, String inputPattern, String returnPattern) {
		String str = null;
		try {
			TimeZone gmt = TimeZone.getTimeZone("GMT+8");
			SimpleDateFormat inFormat = new SimpleDateFormat(inputPattern);
			inFormat.setTimeZone(gmt);
			inFormat.setLenient(true);
			SimpleDateFormat outFormat = new SimpleDateFormat(returnPattern);
			outFormat.setTimeZone(gmt);
			outFormat.setLenient(true);

			Date indate = inFormat.parse(inputDate);
			str = outFormat.format(indate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	private static DateFormat createDateFormat(String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		TimeZone gmt = TimeZone.getTimeZone("GMT+8");
		sdf.setTimeZone(gmt);
		sdf.setLenient(true);
		return sdf;
	}

	/**
	 * 获取传入日期的当前月的第一天
	 * 
	 * @param inputDate
	 * @param inputPattern
	 * @param returnPattern
	 * @return
	 */
	public static String getThisMonthFirstDayRelInputDate(String inputDate, String inputPattern, String returnPattern) {
		String returnStr = null;
		try {
			TimeZone gmt = TimeZone.getTimeZone("GMT+8");
			SimpleDateFormat inFormat = new SimpleDateFormat(inputPattern);
			inFormat.setTimeZone(gmt);
			inFormat.setLenient(true);
			SimpleDateFormat outFormat = new SimpleDateFormat(returnPattern);
			outFormat.setTimeZone(gmt);
			outFormat.setLenient(true);

			Date indate = inFormat.parse(inputDate);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(indate);
			// calendar.add(Calendar.DAY_OF_MONTH, -1);
			calendar.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
			Date outDate = calendar.getTime();
			returnStr = outFormat.format(outDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnStr;
	}

	/**
	 * 获取传入日期的当前月的最后一天
	 * 
	 * @param inputDate
	 * @param inputPattern
	 * @param returnPattern
	 * @return
	 */
	public static String getThisMonthLastDayRelInputDate(String inputDate, String inputPattern, String returnPattern) {
		String returnStr = null;
		try {
			TimeZone gmt = TimeZone.getTimeZone("GMT+8");
			SimpleDateFormat inFormat = new SimpleDateFormat(inputPattern);
			inFormat.setTimeZone(gmt);
			inFormat.setLenient(true);
			SimpleDateFormat outFormat = new SimpleDateFormat(returnPattern);
			outFormat.setTimeZone(gmt);
			outFormat.setLenient(true);

			Date indate = inFormat.parse(inputDate);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(indate);

			calendar.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
			calendar.add(Calendar.MONTH, 1);
			calendar.add(Calendar.DATE, -1);
			Date outDate = calendar.getTime();
			returnStr = outFormat.format(outDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnStr;
	}

	/**
	 * 返回date
	 * 
	 * @param str
	 * @param pattern
	 * @return
	 */
	public static Date getDateByStr(String str, String pattern) {
		try {
			SimpleDateFormat inFormat = new SimpleDateFormat(pattern);
			return inFormat.parse(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getChineseRemark(String str) {
		try {
			return str.substring(0, 4) + "年" + str.substring(4, 6) + "月" + str.substring(6, 8) + "日"
					+ str.substring(8, 10) + "时" + str.substring(10, 12) + "分" + str.substring(12, 14) + "秒";
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	/**
	 * 判断两个字符串日期的间隔时间
	 * 
	 * @param orgTime
	 * @param nowTime
	 * @return
	 */
	public static int checkTimeMin(String orgTime, String nowTime) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date nowTimeDate = null;
		Date date = null;
		long min = 0;
		try {
			nowTimeDate = df.parse(nowTime);
			date = df.parse(orgTime);
			long l = nowTimeDate.getTime() - date.getTime();
			long day = l / (24 * 60 * 60 * 1000);
			long hour = (l / (60 * 60 * 1000) - day * 24);
			min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
			long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
			System.out.println("" + day + "天" + hour + "小时" + min + "分" + s + "秒");
			if (hour > 0) {
				min += 60;
			}
			if (day > 0) {
				min += 60;
			}
			System.out.println("====fen==:" + min);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return Integer.parseInt(String.valueOf(min));
	}

	/**
	 * 日期格式转换yyyy-MM-dd hh:mm:ss->yyyyMMdd
	 */
	public static String getDateyyyyMMdd(String date) {
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String str = "";
		try {
			str = sdf1.format(sdf2.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();

		}
		return str;
	}

	/**
	 * @param args
	 * @throws ParseException
	 */
	public static void main(String[] args) throws ParseException {
		/*
		 * System.out.println(getThisMonthFirstDayRelInputDate("19890227",
		 * "yyyyMMdd","yyyyMMdd"));
		 * System.out.println(getThisMonthLastDayRelInputDate("1989-02-27",
		 * "yyyy-MM-dd","yyyy-MM-dd"));
		 * System.out.println(format("19890227","yyyyMMdd","yyyy-MM-dd"));
		 */

		System.out.println(getDateyyyyMMdd("2016-09-22 11:25:14"));
		// System.out.println(checkTimeMin("2016-09-22 11:25:14","2016-09-22
		// 11:25:14"));

		// String in="20150123456789";
		// System.out.println(in.length());
		// String str=DateUtils.getChineseRemark("20150123456789");
		// System.out.println(str);
	}

}

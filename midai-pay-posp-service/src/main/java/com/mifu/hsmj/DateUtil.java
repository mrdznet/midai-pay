package com.mifu.hsmj;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	/**
	 * 获取星期
	 * 
	 * @return
	 */
	public static int getWeek() {
		Calendar rightNow = Calendar.getInstance();
		int day = rightNow.get(Calendar.DAY_OF_WEEK) - 1;
		return day;
	}

	/**
	 * 获取年份
	 * 
	 * @return
	 */
	public static String getYeer() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		Calendar rightNow = Calendar.getInstance();
		String time = sdf.format(rightNow.getTime());
		return time;
	}

	/**
	 * 获取当前完整时间-yyyyMMddHHmmss
	 * 
	 * @return
	 */
	public static String getCompleteTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar rightNow = Calendar.getInstance();
		String time = sdf.format(rightNow.getTime());
		return time;
	}

	/**
	 * 获取距离当前前后多少小时的完整时间
	 * 
	 * @param hour距离当前多少小时
	 * @return
	 */
	public static String getCompleteTime(int hour) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar rightNow = Calendar.getInstance();
		rightNow.set(Calendar.HOUR, Calendar.HOUR + hour);
		String time = sdf.format(rightNow.getTime());
		return time;
	}

	/**
	 * 获取距离当前前后多少分钟的完整时间
	 * 
	 * @param hour距离当前多少小时
	 * @return
	 */
	public static String getCompleteTimeByMin(int minute) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar rightNow = Calendar.getInstance();
		rightNow.add(Calendar.MINUTE, minute);
		String time = sdf.format(rightNow.getTime());
		return time;
	}

	/**
	 * 获取当前时间-HHmmss
	 * 
	 * @return
	 */
	public static String getTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
		Calendar rightNow = Calendar.getInstance();
		String time = sdf.format(rightNow.getTime());
		return time;
	}

	/**
	 * 获取当前日期-yyyyMMdd
	 * 
	 * @return
	 */
	public static String getDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Calendar rightNow = Calendar.getInstance();
		String time = sdf.format(rightNow.getTime());
		return time;
	}

	/**
	 * 
	 * @param date
	 *            距当前第几天
	 * @param format
	 *            日期格式
	 * @return
	 */
	public static String getOtherDate(int date, String format) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, date);
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(c.getTime());
	}

	public static String getOtherDate(String format) {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(c.getTime());
	}

	/**
	 * 格式化日期
	 * 
	 * @param date
	 *            日期时间
	 * @param formart1
	 *            原格式
	 * @param formart2
	 *            转换后格式
	 * @return
	 * @throws Exception
	 */
	public static String formartDate(String date, String formart1, String formart2) throws Exception {
		SimpleDateFormat sdf1 = new SimpleDateFormat(formart1);
		SimpleDateFormat sdf2 = new SimpleDateFormat(formart2);
		Date time = sdf1.parse(date);
		return sdf2.format(time);
	}

}

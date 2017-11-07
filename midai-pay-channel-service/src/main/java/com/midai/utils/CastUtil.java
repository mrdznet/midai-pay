package com.midai.utils;

/**
 * 
 * @author wangrt
 * @date 2017年2月15日
 * 转型操作工具类
 *
 */
public final class CastUtil {

	/**
	 * 转为int型
	 */
	public static int castInt(Object object) {
		return castIntDefault(object, 0);
	}
	
	/**
	 * 转为int型（提供默认值）
	 */
	public static int castIntDefault(Object obj, int defaultValue) {
		int value = defaultValue;
		
		if(obj != null) {
			String strValue = castString(obj);
			if(StringUtil.isNotEmpty(strValue)) {
				try {
					value = Integer.parseInt(strValue);
				} catch (NumberFormatException e) {
					value = defaultValue;
				}
			}
		}
		
		return value;
	}
	
	/**
	 * 转为long型
	 */
	public static long castLong(Object object) {
		return castLongDefault(object, 0);
	}

	/**
	 * 转为long型（提供默认值）
	 */
	public static long castLongDefault(Object obj, long defaultValue) {
		long value = defaultValue;
		if(obj != null) {
			String strLong = castString(obj);
			if(StringUtil.isNotEmpty(strLong)) {
				try {
					value = Long.parseLong(strLong);
				} catch (NumberFormatException e) {
					value = defaultValue;
				}
			}
		}
		return value;
	}
	
	/**
	 * 转为double型
	 */
	public static double castDouble(Object obj) {
		return castDoubleDefault(obj, 0);
	}
	
	/**
	 * 转为double型（提供默认值）
	 */
	public static double castDoubleDefault(Object obj, double defaultValue) {
		double value = defaultValue;
		
		if(obj != null) {
			String strDouble = castString(obj);
			if(StringUtil.isNotEmpty(strDouble)) {
				try {
					value = Double.parseDouble(strDouble);
				} catch (NumberFormatException e) {
					value = defaultValue;
				}
			}
		}
		return value;
	}
	
	/**
	 * 转为boolean型
	 */
	public static boolean castBoolean(Object obj) {
		return castBooleanDefault(obj, false);
	}

	/**
	 * 转为boolean型（提供默认值）
	 */
	public static boolean castBooleanDefault(Object obj, boolean defaultValue) {
		boolean value = defaultValue;
		if(obj != null) {
			value = Boolean.parseBoolean(castString(obj));
		}
		return value;
	}
	
	/**
	 * 转为String型
	 */
	public static String castString(Object obj) {
		return castStringDefault(obj, "");
	}
	
	/**
	 * 转为String型（提供默认值）
	 */
	public static String castStringDefault(Object obj, String defaultValue) {
		return obj != null ? String.valueOf(obj) : defaultValue;
	}
}

package com.midai.utils;

import org.apache.commons.lang.StringUtils;

public final class StringUtil {

	
	public static boolean isEmpty(String value) {
		
		if(value != null) {
			value = value.trim();
		}
		return StringUtils.isNotEmpty(value);
	}
	
	public static boolean isNotEmpty(String value) {
		return !isEmpty(value);
	}
}

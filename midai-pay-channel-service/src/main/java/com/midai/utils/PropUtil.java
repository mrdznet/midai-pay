package com.midai.utils;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PropUtil {

	private final static Logger logger = LoggerFactory.getLogger(PropUtil.class);
	
	public static Properties getProperties(String fileName) {
		Properties properties = null;
		InputStream in= null;
		try {
			in= Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
			if(in == null) {
				throw new FileNotFoundException(fileName + " file is not found");
			}
			properties = new Properties();
			properties.load(in);
		} catch(IOException e) {
			logger.error("load properties file failure", e);
		} finally {
			try {
				if (in != null) {
					in.close();
				} 
			} catch (IOException e) {
				logger.error("close input stream failure", e);
			}
		}

		return properties;
	}
	
	public static String getString(Properties properties, String key) {
		return getStringDefault(properties, key, "");
	}
	
	public static String getStringDefault(Properties properties, String key, String defaultValue) {
		String value = defaultValue;
		if(properties.containsKey(key)) {
			value = properties.getProperty(key);
		}
		return value;
	}
	
	public static int getInt(Properties properties, String key) {
		return getIntDefault(properties, key, 0);
	}
	
	public static int getIntDefault(Properties props, String key, int defaultValue) {
		int value = defaultValue;
		
		if(props.contains(key)) {
			value = CastUtil.castInt(props.getProperty(key));
		}
		return value;
	}
	
	public static long getLong(Properties props, String key) {
		return getLongDefault(props, key, 0);
	}
	
	public static long getLongDefault(Properties props, String key, long defaultLong) {
		long value = defaultLong;
		
		if(props.containsKey(key)) {
			value = CastUtil.castLong(props.getProperty(key));
		}
		return value;
	}
	
	public static boolean getBoolean(Properties props, String key) {
		return getBooleanDefault(props, key, false);
	}
	
	public static boolean getBooleanDefault(Properties props, String key, boolean defaultValue) {
		boolean value = defaultValue;
		if(props.containsKey(value)) {
			value = CastUtil.castBoolean(props.getProperty(key));
		}
		return value;
	}
}

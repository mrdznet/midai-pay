package com.midai.pay.common.pay;

import org.apache.log4j.Logger;

/**
 * @author ct @version 创建时间：2014-12-1 下午2:22:33 @category
 */
public class Log4JBase {

	private Logger log = null;
	private String classname = null;

	public Log4JBase(Class clazz) {
		this.log = Logger.getLogger(clazz.getName());
		classname = clazz.getName();
	}

	public void info(String methodname, Object errormessage) {
		log.info(">>>class name is " + classname + ", method name is " + methodname + " ,error message is "
				+ errormessage);
	}

	public void error(String methodname, Object errormessage) {
		log.info(">>>class name is " + classname + ", method name is " + methodname + " ,error message is "
				+ errormessage);
	}

	public void info(String methodname) {
		log.info(">>>class name is " + classname + ", method name is " + methodname);
	}
}

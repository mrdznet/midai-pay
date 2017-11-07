package com.midai.pay.common.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang3.StringUtils;
import org.python.core.PyFloat;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.util.PythonInterpreter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.midai.pay.routetransactionInformation.TransactionInformation;

public class PythonUtil {

	private static Logger logger = LoggerFactory.getLogger(PythonUtil.class);
	
	private static PythonInterpreter interpreter = null;

	static {
		interpreter = new PythonInterpreter();
	}


	public static String getInsCode(String filePath, TransactionInformation tra) {
		if (StringUtils.isNotEmpty(filePath)) {
			InputStream is = null;
			PyObject pyObject = null;
			try {
				is = Thread.currentThread().getContextClassLoader().getResource(filePath).openStream();
				interpreter.execfile(is);
				String file = filePath.substring(filePath.lastIndexOf("/") + 1);
				String fileName = file.substring(0, file.indexOf("."));
				String className = fileName.substring(0, 1).toLowerCase() + fileName.substring(1);
				interpreter.exec( className + "=" + fileName + "()");
				pyObject = interpreter.get(className);
				pyObject.invoke("setMoble", new PyString(tra.getMoble()));
				pyObject.invoke("setMoney", new PyFloat(tra.getMoney()));
				pyObject = pyObject.invoke("getChannelCodes");
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if(is != null) {
						is.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			return pyObject.toString();
		} else {
			logger.error("规则文件不能为空！");
		}
		return null;
	}

}

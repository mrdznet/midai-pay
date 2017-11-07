package com.midai.framework.xml;

import java.io.InputStream;

import javax.xml.validation.Schema;

public interface MidaiXmlParser {
	/**
	 * 
	 * midaiXmlSchema:(获取米袋xml规范). <br/>
	 *
	 * @author 陈勋
	 * @return
	 * @since JDK 1.7
	 */
	Schema midaiXmlSchema();
	
	/**
	 * 
	 * validate:(校验xml). <br/>
	 *
	 * @author 陈勋
	 * @param xmlInputStream  xml输入流
	 * @return
	 * @since JDK 1.7
	 */
	void validate(InputStream xmlInputStream);
	
	
	

}

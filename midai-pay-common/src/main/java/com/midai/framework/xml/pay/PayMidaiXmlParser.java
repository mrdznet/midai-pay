/**
 * Project Name:midai-pay-common
 * File Name:PayMidaiXmlEntity.java
 * Package Name:com.midai.framework.xml.pay
 * Date:2016年11月11日上午10:14:17
 * Copyright (c) 2016, Shanghai Law Cloud Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.framework.xml.pay;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.midai.framework.xml.MidaiXmlParser;

import lombok.extern.slf4j.Slf4j;

/**
 * ClassName:PayMidaiXmlEntity <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年11月11日 上午10:14:17 <br/>
 * @author   陈勋
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
@Slf4j
public class PayMidaiXmlParser implements MidaiXmlParser{
	
	
	public static final String SCHEMA_PATH="midai-pay.xsd";
	public static final String DEFAUTL_ENCODING="UTF-8";
	
	

	@Override
	public Schema midaiXmlSchema() {
		 String schemaLanguage = XMLConstants.W3C_XML_SCHEMA_NS_URI;
         SchemaFactory schemaFactory = SchemaFactory.newInstance(schemaLanguage);
         Schema schema=null;
         InputSource inputSource = new InputSource(PayMidaiXmlParser.class.getClassLoader().getResourceAsStream(SCHEMA_PATH));
         inputSource.setEncoding(DEFAUTL_ENCODING);
         Source source = new SAXSource(inputSource);   
		try {
			schema = schemaFactory.newSchema(source);
		} catch (SAXException e) {
			log.error(e.getMessage());
			throw new RuntimeException(e);		
		}
         return schema;
	}

	@Override
	public void validate(InputStream xmlInputStream) {
		InputSource inputSource = new InputSource(xmlInputStream);
        inputSource.setEncoding(DEFAUTL_ENCODING);
        Source source = new SAXSource(inputSource);
        
        Validator validator = midaiXmlSchema().newValidator();
			try {
				validator.validate(source);
			} catch (SAXException | IOException e) {
				log.error(e.getMessage());
				throw new RuntimeException(e);
			}
	}

	
	
	



}


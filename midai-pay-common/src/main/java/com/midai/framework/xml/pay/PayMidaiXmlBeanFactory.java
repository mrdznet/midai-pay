/**
 * Project Name:midai-pay-common
 * File Name:PayMidaiXmlEntityFactory.java
 * Package Name:com.midai.framework.pay.xml
 * Date:2016年11月11日上午9:52:10
 * Copyright (c) 2016, Shanghai Law Cloud Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.framework.xml.pay;

import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.midai.framework.xml.MidaiXmlBean;
import com.midai.framework.xml.MidaiXmlBeanFactory;

import lombok.extern.slf4j.Slf4j;

/**
 * ClassName:PayMidaiXmlEntityFactory <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2016年11月11日 上午9:52:10 <br/>
 * 
 * @author 陈勋
 * @version
 * @since JDK 1.7
 * @see
 */
@Slf4j
public class PayMidaiXmlBeanFactory implements MidaiXmlBeanFactory {

	public static final String DEFAULT_PATH = "midai-pay.xml";

	public static final String DEFAUTL_ENCODING = "UTF-8";

	@Override
	public MidaiXmlBean parserXml(String xmlPath) {
		if (xmlPath == null || xmlPath.trim().length() == 0) {
			xmlPath = DEFAULT_PATH;
		}
		PayMidaiXmlParser xmlEntity = new PayMidaiXmlParser();
		InputStream xmlInputStream = PayMidaiXmlBeanFactory.class.getClassLoader().getResourceAsStream(xmlPath);
		log.info("start validate xml :" + xmlPath);
		xmlEntity.validate(xmlInputStream);
		PayMidaiXmlBean xmlBean=fromXML(xmlPath,PayMidaiXmlBean.class);		
		log.info("end  parser xml :" + xmlPath);
		return xmlBean;
	}
	
	
	private <T> T fromXML(String xml,Class<T> cls){
		  JAXBContext jaxbContext;
		try {
			jaxbContext = JAXBContext.newInstance(cls);
		    Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();  
	         SAXParserFactory sax = SAXParserFactory.newInstance();  
	         sax.setNamespaceAware(false);  
	         XMLReader xmlReader;
	     	xmlReader = sax.newSAXParser().getXMLReader();
		    Source source = new SAXSource(xmlReader, new InputSource(PayMidaiXmlBeanFactory.class.getClassLoader().getResourceAsStream(xml)));  
		    return (T) unmarshaller.unmarshal(source);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	          
	}
	
	
	

}

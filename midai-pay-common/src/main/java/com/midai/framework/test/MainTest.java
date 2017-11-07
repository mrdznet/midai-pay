/**
 * Project Name:midai-pay-common
 * File Name:MainTest.java
 * Package Name:com.midai.framework.test
 * Date:2016年10月18日下午2:49:30
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.framework.test;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.xml.sax.SAXException;

/**
 * ClassName:MainTest <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年10月18日 下午2:49:30 <br/>
 * @author   陈勋
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
public class MainTest {

    public static void main(String[] args) {
    	test2();
        System.out.println("Hello world!");
        
        
        System.out.println((int)'E');
        
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
        System.out.println(sdf.format(new Date()));
        
        
        System.out.println("刘国宇".getBytes().length);
        
        
        String[]  arr="aaaa".split(",");
        System.out.println(arr);
        
        
        validateXMLSchema("D:\\git\\midai-pay\\midai-pay-common\\src\\main\\resources\\midai-pay.xsd", 
        		"D:\\git\\midai-pay\\midai-pay-common\\src\\main\\resources\\NewFile.xml");
        

        
    }
    
    public static boolean validateXMLSchema(String xsdPath, String xmlPath){

        try {
            SchemaFactory factory = 
                    SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File(xsdPath));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File(xmlPath)));
        } catch (IOException | SAXException e) {
            System.out.println("Exception: "+e.getMessage());
            return false;
        }
        return true;
    }
    

	private static class ParamContext {
		public Map<String, Object> params = new HashMap<>();

	}
	
	private static class User{
		public String name;
	}
    
    public static void test2(){
    	String exp="#{params[user].name}";
    	ExpressionParser parser = new SpelExpressionParser();
    	ParamContext paramContext = new ParamContext();
    	User user=new User();
        user.name="abc";
    	paramContext.params.put("user", user);
    	
		EvaluationContext context = new StandardEvaluationContext(paramContext);
		Expression expression = parser.parseExpression(exp, new TemplateParserContext());
		System.out.println(expression.getValue(context, String.class));
    }

}


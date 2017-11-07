/**
 * Project Name:midai-pay-sercurity-service
 * File Name:HsmConfig.java
 * Package Name:com.midai.pay.sercurity.config
 * Date:2016年9月12日上午10:52:18
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.pay.posp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.midai.framework.xml.pay.PayMidaiXmlBean;
import com.midai.framework.xml.pay.PayMidaiXmlBeanFactory;

/**
 * ClassName:HsmConfig <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年9月12日 上午10:52:18 <br/>
 * @author   陈勋
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
@Configuration
@EnableConfigurationProperties({PayMiShuaProperties.class,MidaiPayHsmProperties.class})
public class HsmConfig {
    

	@Autowired
	private  Environment environment;
	
	@Autowired
	private MidaiPayHsmProperties midaiPayHsmProperties;

    @Bean
    public MidaiPayHsmTemplate hsmTemplate(){
		MidaiPayHsmTemplate hsmTemplate=new MidaiPayHsmTemplate(midaiPayHsmProperties);
        return hsmTemplate;
    }
    
    @Bean
    public PayMidaiXmlBean payMidaiXmlBean(){
    	PayMidaiXmlBeanFactory beanFactory=new PayMidaiXmlBeanFactory();
    	return (PayMidaiXmlBean) beanFactory.parserXml(environment.getProperty("midai.pay.xml.path"));

    }
    
    @Bean
    public Pay8583Message pay8583Message(MidaiPayHsmTemplate hsmTemplate,PayMidaiXmlBean payMidaiXmlBean){
        Pay8583Message pay8583Message=new Pay8583Message(hsmTemplate,payMidaiXmlBean);
        return pay8583Message;
    }
    
    
    
   
    
    
    
    

}


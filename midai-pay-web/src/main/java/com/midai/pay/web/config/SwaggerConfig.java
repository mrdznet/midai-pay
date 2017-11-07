/**
 * Project Name:midai-pay-web
 * File Name:SwaggerConfig.java
 * Package Name:com.midai.pay.web.config
 * Date:2016年9月1日下午2:40:44
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.pay.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * ClassName:SwaggerConfig <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年9月1日 下午2:40:44 <br/>
 * @author   陈勋
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
@Configuration
@EnableSwagger2
@ComponentScan(basePackages={"com.midai.pay.web.controller"})
public class SwaggerConfig {

    @Bean(name="midai-pay-api")
    public Docket createRestMidaiApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("midai-pay-api")
                .apiInfo(midaiApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.midai.pay.web.controller"))
                .paths(PathSelectors.any())
                .build();
    }
    


    private ApiInfo midaiApiInfo() {
        ApiInfo apiInfo = new ApiInfoBuilder().title("米袋支付posw系统api文档")
                .description("The APIs here demonstrate creating a service built with Spring Boot")
                .license("MIT")
                .licenseUrl("http://opensource.org/licenses/MIT")
                .contact(new Contact("米袋支付", "pay.midaigroup.com", "chenxun@midaigroup.com"))
                .version("1.0")
                .build();

        return apiInfo;
    }
    

}


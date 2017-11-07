/**
 * Project Name:midai-pay-web
 * File Name:WebConfig.java
 * Package Name:com.midai.pay.web.config
 * Date:2016年9月1日下午2:54:25
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
 */

package com.midai.pay.web.config;

import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * ClassName:WebConfig <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2016年9月1日 下午2:54:25 <br/>
 * 
 * @author 陈勋
 * @version
 * @since JDK 1.7
 * @see
 */
@Configuration
@EnableWebSecurity
@EnableConfigurationProperties({OSSProperties.class, OSSFolderProperties.class, MidaiPayMessageProperties.class})
public class WebConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    @Bean
    @ConditionalOnClass({ ObjectMapper.class, Jackson2ObjectMapperBuilder.class })
    public Jackson2ObjectMapperBuilder jacksonBuilder() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.indentOutput(true).dateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));   
        return builder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests().antMatchers("/view/mobile-*.html", "/view/js/**","/imgs/**","/session/invalidate","/login.html","/trade/review/pay/**","/view/img.html**","/view/*_help.html","/view/*_notice.html", "/mifu/mobile/*").permitAll();
        http.authorizeRequests().anyRequest().authenticated().and()
        .formLogin().loginPage("/session/invalidate").
        failureHandler(authenticationFailureHandler).
        loginProcessingUrl("/login").permitAll();
        //session 管理
        http.sessionManagement().invalidSessionUrl("/session/invalidate");
   

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
        auth.userDetailsService(userDetailsService).passwordEncoder(
        new Md5PasswordEncoder());
        auth.eraseCredentials(false);

    }

}

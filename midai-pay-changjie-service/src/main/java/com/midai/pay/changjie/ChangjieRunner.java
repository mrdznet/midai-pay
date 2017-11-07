package com.midai.pay.changjie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;


@SpringBootApplication
@ImportResource("classpath*:META-INF/spring/*.xml")
public class ChangjieRunner {
	
	 public static void main(String[] args) {     
	        SpringApplication.run(ChangjieRunner.class, args);      
	    }

}

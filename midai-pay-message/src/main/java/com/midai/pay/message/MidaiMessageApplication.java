package com.midai.pay.message;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@SpringBootApplication
@ImportResource("classpath*:META-INF/spring/*.xml")
@EnableWebSocket
public class MidaiMessageApplication {

	public static void main(String[] args) {
		SpringApplication.run(MidaiMessageApplication.class, args);
	}
}

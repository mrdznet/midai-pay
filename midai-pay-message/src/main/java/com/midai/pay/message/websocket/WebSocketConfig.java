package com.midai.pay.message.websocket;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
/**
 * 
 * ClassName: WebSocketConfig <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2016年6月2日 下午4:30:52 <br/>
 *
 * @author 陈勋
 * @version 
 * @since JDK 1.7
 */
@Component
@EnableWebSocket
public class WebSocketConfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer {
	@Resource
	MyWebSocketHandler handler;
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(handler, "/ws").addInterceptors(new HandShake()).setAllowedOrigins("*");
		
		registry.addHandler(handler, "/ws/sockjs").addInterceptors(new HandShake()).setAllowedOrigins("*").withSockJS();
	}
}
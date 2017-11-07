package com.midai.pay.message.websocket;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

/**
 * 
 * ClassName: HandShake <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2016年6月2日 下午4:29:24 <br/>
 *
 * @author 陈勋
 * @version
 * @since JDK 1.7
 */
public class HandShake implements HandshakeInterceptor {

	private static final Logger log = LoggerFactory.getLogger(HandShake.class);

	public boolean beforeHandshake(ServerHttpRequest request,
			ServerHttpResponse response, WebSocketHandler wsHandler,
			Map<String, Object> attributes) throws Exception {

		if (request instanceof ServletServerHttpRequest) {
			ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
			HttpSession session = servletRequest.getServletRequest()
					.getSession(true);
//			if(session==null){
//				return true;
//			}
			// 标记用户
			String uid = (String) session.getAttribute("uid");
			if (uid != null) {
				attributes.put("uid", uid);
				log.info("Websocket:用户[ID:"
						+ uid + "]已经建立连接");
			} else {
				String params = servletRequest.getURI().toURL().getQuery();
				if (params.contains("uid") && params.contains("=")) {
					if (params.contains("&")) {
						String[] arr = params.split("&");
						for (String str : arr) {
							if (str.startsWith("uid")) {
								String[] temp = str.split("=");
								uid = temp[1];
							}
						}

					} else {
						String[] temp = params.split("=");
						uid = temp[1];
					}
					attributes.put("uid", uid);
					log.info("Websocket:用户[ID:"
							+ uid + "]已经建立连接");
					return true;
				}

				return false;
			}
		}
		return true;
	}

	public void afterHandshake(ServerHttpRequest request,
			ServerHttpResponse response, WebSocketHandler wsHandler,
			Exception exception) {
	}
}
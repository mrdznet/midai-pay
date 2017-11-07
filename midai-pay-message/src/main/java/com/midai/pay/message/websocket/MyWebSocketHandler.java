package com.midai.pay.message.websocket;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.midai.pay.message.entity.Message;


/**
 * 
 * ClassName: MyWebSocketHandler <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2016年6月2日 下午4:29:40 <br/>
 *
 * @author 陈勋
 * @version 
 * @since JDK 1.7
 */
@Component
public class MyWebSocketHandler implements WebSocketHandler {
	public static final Map<String,List<WebSocketSession> > userSocketSessionMap;
	
	private static final Logger log=LoggerFactory.getLogger(HandShake.class);
	
	static {
		userSocketSessionMap = new HashMap<String, List<WebSocketSession>>();
	}
	/**
	 * 建立连接后
	 */
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		String uid =null;
		String params=session.getUri().getQuery();
		if(params.contains("uid")&&params.contains("=")){
			if(params.contains("&")){
				String[] arr=params.split("&");
				for(String str:arr){
					if(str.startsWith("uid")){
						String[] temp=str.split("=");
						uid=temp[1];
					}
				}
				
			}else{
				String[] temp=params.split("=");
				uid=temp[1];
			}
		}
		//支持多终端推送
		if (userSocketSessionMap.get(uid) == null) {
			List<WebSocketSession> list=new ArrayList<>();
			list.add(session);
			userSocketSessionMap.put(uid, list);
		}else{
			userSocketSessionMap.get(uid).add(session);
		}
		
		
		//踢掉之前的终端，只维持最新一个链接
	//	userSocketSessionMap.put(uid, session);
		
	}
	/**
	 * 消息处理，在客户端通过Websocket API发送的消息会经过这里，然后进行相应的处理
	 */
	public void handleMessage(WebSocketSession session,WebSocketMessage<?> message) throws Exception {
		if (message.getPayloadLength() == 0)
			return;
		ObjectMapper om=new ObjectMapper();
		om.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		Message msg =om.readValue(message.getPayload().toString(), Message.class);
		msg.setDate(new Date());
		sendMessageToUser(msg.getTo(), new TextMessage(om.writeValueAsString(msg)));
	}

	/**
	 * 消息传输错误处理
	 */
	public void handleTransportError(WebSocketSession session,Throwable exception) throws Exception {
		if (session.isOpen()) {
			session.close();
		}
		Iterator<Entry<String, List<WebSocketSession>>> it = userSocketSessionMap.entrySet().iterator();
		// 移除Socket会话
		while (it.hasNext()) {
			Entry<String,  List<WebSocketSession>> entry = it.next();
			for(int i=0;i<entry.getValue().size();i++){
				if(session.getId().equals(entry.getValue().get(i).getId())){
					userSocketSessionMap.remove(entry.getKey());
					log.info("Socket会话已经移除:用户ID" + entry.getKey()+"---SessionId:"+session.getId());
					break;
				}
			}

		}
	}

	/**
	 * 关闭连接后
	 */
	public void afterConnectionClosed(WebSocketSession session,CloseStatus closeStatus) throws Exception {
		log.info("Websocket:" + session.getId() + "已经关闭");
		Iterator<Entry<String, List<WebSocketSession>>> it = userSocketSessionMap.entrySet().iterator();
		// 移除Socket会话
		while (it.hasNext()) {
			Entry<String,  List<WebSocketSession>> entry = it.next();
			for(int i=0;i<entry.getValue().size();i++){
				if(session.getId().equals(entry.getValue().get(i).getId())){
					userSocketSessionMap.remove(entry.getKey());
					log.info("Socket会话已经移除:用户ID" + entry.getKey()+"---SessionId:"+session.getId());
					break;
				}
			}
		}
	}

	public boolean supportsPartialMessages() {
		return false;
	}

	/**
	 * 给所有在线用户发送消息
	 * 
	 * @param message
	 * @throws IOException
	 */
	public void broadcast(final TextMessage message) throws IOException {
		Iterator<Entry<String, List<WebSocketSession>>> it = userSocketSessionMap.entrySet().iterator();
		// 多线程群发
		while (it.hasNext()) {
			final Entry<String, List<WebSocketSession>> entry = it.next();
			for(final WebSocketSession session:entry.getValue()){
				if (session.isOpen()) {
					// entry.getValue().sendMessage(message);
					new Thread(new Runnable() {
						public void run() {
							try {
								if (session.isOpen()) {
									session.sendMessage(message);
								}
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}).start();
				}
			}
			
		}
	}

	/**
	 * 给某个用户发送消息
	 * 
	 * @param userName
	 * @param message
	 * @throws IOException
	 */
	public int sendMessageToUser(String uid, TextMessage message)throws IOException {
		List<WebSocketSession> list = userSocketSessionMap.get(uid);
		if(list==null||list.isEmpty()){
			return 0;
		}
		for(WebSocketSession session :list){
			if (session != null && session.isOpen()) {
				session.sendMessage(message);
			}
		}
		return list.size();
		
	}
}
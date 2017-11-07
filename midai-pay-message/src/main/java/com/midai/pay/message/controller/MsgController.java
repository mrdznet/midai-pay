package com.midai.pay.message.controller;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.socket.TextMessage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.midai.pay.message.entity.Message;
import com.midai.pay.message.entity.User;
import com.midai.pay.message.websocket.MyWebSocketHandler;
/**
 * 
 * ClassName: MsgController <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2016年6月2日 下午4:29:10 <br/>
 *
 * @author 陈勋
 * @version 
 * @since JDK 1.7
 */
@Controller
@RequestMapping("/msg")
public class MsgController {
	@Resource
	MyWebSocketHandler handler;



	/**
	 * 群发
	 * @param text
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value = "broadcast")
	public void broadcast(String text) throws IOException {
		Message msg = new Message();
		msg.setDate(new Date());
		msg.setFrom("system");
		msg.setText(text);
		ObjectMapper om=new ObjectMapper();
		om.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		String json=om.writeValueAsString(msg);
		handler.broadcast(new TextMessage(json));
	}
	
	  /**
	   * 
	   * single:(发送单条信息). <br/>
	   *
	   * @author 陈勋
	   * @param text
	   * @throws IOException
	   * @since JDK 1.7
	   */
		@ResponseBody
		@RequestMapping(value = "single")
		public void single(String text,String from,String to) throws IOException {
			Message msg = new Message();
			msg.setDate(new Date());
			msg.setFrom(from);
			msg.setTo(to);
			msg.setText(text);
			ObjectMapper om=new ObjectMapper();
			om.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
			handler.sendMessageToUser(to,
					new TextMessage(om.writeValueAsString(msg)));
			
		}
		
		/**
		   * 
		   * single:(多人发送单条信息). <br/>
		   *
		   * @author 陈勋
		   * @param text
		   * @throws IOException
		   * @since JDK 1.7
		   */
			@ResponseBody
			@RequestMapping(value = "multiple", method = RequestMethod.POST)
			public String multiple(String text,String to) throws IOException {
				String[] arr=to.split(",");
				int count=0;
				for(String str:arr){
					Message msg = new Message();
					msg.setDate(new Date());
					msg.setFrom("system");
					msg.setTo(str);
					msg.setText(text);
					ObjectMapper om=new ObjectMapper();
					om.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
					count+=handler.sendMessageToUser(str,
							new TextMessage(om.writeValueAsString(msg)));
				}
				
				return String.valueOf(count);
				
			
				
			}
	
	
}
package com.midai.pay.message.entity;
import java.util.Date;

import lombok.Data;
/**
 * 
 * ClassName: Message <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2016年6月2日 下午4:26:38 <br/>
 *
 * @author 陈勋
 * @version 
 * @since JDK 1.7
 */
public @Data class Message {
	// 发送者
	public String from;
	// 接收者
	public String to;
	// 发送的文本
	public String text;
	// 发送日期
	public Date date;

	
}

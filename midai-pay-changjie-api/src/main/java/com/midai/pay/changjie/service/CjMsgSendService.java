package com.midai.pay.changjie.service;

import com.midai.pay.changjie.bean.G10001Bean;
import com.midai.pay.changjie.bean.G20001Bean;

public interface CjMsgSendService {

	public String G10002SendMessage(G10001Bean data);
	
	public G20001Bean G20001SendMessage(G20001Bean data);
}

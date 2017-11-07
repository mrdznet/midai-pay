package com.midai.pay.agent.common.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 代理商常量类
 * @author miaolei
 * 2016年9月24日 下午2:04:47
 */
public class AgentConstant {
	
	/**************************代理商状态**********************/
	/**
	 * 代理商状态-关闭
	 */
	public static final String OP_STATE_CLOSED = "0";
	
	/**
	 * 代理商状态-开通
	 */
	public static final String OP_STATE_OPEN = "1"; 
	
	/**
	 * 代理商状态Map
	 */
	public static Map<String, String> OP_STATE_MAP = new LinkedHashMap<String, String>();
	static {
		OP_STATE_MAP.put(OP_STATE_CLOSED, "关闭");
		OP_STATE_MAP.put(OP_STATE_OPEN, "开通");
	}

}

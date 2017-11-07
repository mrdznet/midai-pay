/**
 * Project Name:midai-monitor
 * File Name:MidaiLogTraceService.java
 * Package Name:com.midai.monitor
 * Date:2016年8月3日下午3:50:34
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.framework.monitor;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * ClassName:MidaiLogTraceService <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2016年8月3日 下午3:50:34 <br/>
 * 
 * @author 陈勋
 * @version
 * @since JDK 1.7
 * @see
 */
public class MidaiLogTraceService {

	public static final String TRACE_ID = "traceId";

	public static final String TRACE_LOG = "midai.trace";

	private static final ObjectMapper objectMapper = new ObjectMapper();

	private static final ThreadLocal<MidaiLogTrace> traceThreadLocal = new ThreadLocal<MidaiLogTrace>();

	private static final Logger logger = LoggerFactory.getLogger(MidaiLogTraceService.class);
	
	
	private static boolean enable;
	
	
	

	/**
	 * 
	 * @return uuid
	 */
	public static String createTraceId() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	public static void setMidaiLogTrace(MidaiLogTrace trace) {
		traceThreadLocal.set(trace);
		
	}

	public static MidaiLogTrace getMidaiLogTrace() {
		return traceThreadLocal.get();
	}

	public static void traceLog(MidaiLogTrace trace) {
		if(!isEnable()){
			return ;
		}
		
		StringBuilder sb=new StringBuilder();
		sb.append("traceId:"+trace.getTraceId());
		sb.append("---");
		sb.append("start:"+trace.getStart());
		sb.append("---");
		sb.append("end:"+trace.getEnd());
		sb.append("---");
		sb.append("url:"+trace.getUrl());
		sb.append("---");
		sb.append("result:"+trace.getResult());
		
		logger.info(sb.toString());
		
	}

	public static void traceSpanLog(MidaiLogSpan span) {
		if(!isEnable()){
			return ;
		}
		
		
		StringBuilder sb=new StringBuilder();
		sb.append("traceId:"+span.getTraceId());
		sb.append("---");
		sb.append("clientIp:"+span.getClientIp());
		sb.append("---");
		sb.append("clientPort:"+span.getClientPort());
		sb.append("---");
		sb.append("servierIp:"+span.getServierIp());
		sb.append("---");
		sb.append("servicePort:"+span.getServicePort());
		sb.append("---");
		sb.append("url:"+span.getUrl());
		sb.append("---");
	//	sb.append("interfaceDesc:"+span.getInterfaceDesc());
	//	sb.append("---");
		sb.append("start:"+span.getStart());
		sb.append("---");
		sb.append("end:"+span.getEnd());
		logger.info(sb.toString());
		
	}
	
	public static void traceSqlLog(String traceId,String sql){
		if(!isEnable()){
			return ;
		}
		
		
		StringBuilder sb=new StringBuilder();
		sb.append("traceId:"+traceId);
		sb.append("---");
		sb.append("sql:"+sql);
		logger.info(sb.toString());
	}


	public static String serializable(MidaiLogTrace trace) {
		try {
			return objectMapper.writer().writeValueAsString(trace);
		} catch (JsonProcessingException e) {
			logger.error("trace serializable unsupport !!!");
			return null;
		}
	}

	public  static MidaiLogTrace unserializable(String str) {
		try {
			return objectMapper.reader().readValue(str);
		} catch (Exception e) {
			logger.error("trace unserializable unsupport !!!");
			return null;
		}
	}

	public static boolean isEnable() {
		return enable;
	}

	public static void setEnable(boolean enable) {
		MidaiLogTraceService.enable = enable;
	}

}

/**
 * Project Name:midai-monitor
 * File Name:MidaiLogWebFilter.java
 * Package Name:com.midai.monitor
 * Date:2016年8月3日下午4:07:50
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.pay.app.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.midai.framework.monitor.MidaiLogSpan;
import com.midai.framework.monitor.MidaiLogTrace;
import com.midai.framework.monitor.MidaiLogTraceService;

/**
 * ClassName:MidaiLogWebFilter <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2016年8月3日 下午4:07:50 <br/>
 * 
 * @author 陈勋
 * @version
 * @since JDK 1.7
 * @see
 */
public class MidaiLogWebFilter implements HandlerInterceptor {

	private static final ThreadLocal<MidaiLogSpan> spanArray = new ThreadLocal<MidaiLogSpan>();

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		// 从heqd里面获取traceId
		String tmp = request.getHeader(MidaiLogTraceService.TRACE_ID);

		MidaiLogTrace trace = null;
		if (tmp == null) {
			trace = new MidaiLogTrace();
			trace.setTraceId(MidaiLogTraceService.createTraceId());
			trace.setStart(System.currentTimeMillis());
			trace.setUrl(request.getRequestURI());
		} else {
			trace = MidaiLogTraceService.unserializable(tmp);
		}

		MidaiLogSpan span = new MidaiLogSpan();
		span.setClientIp(request.getRemoteHost());
		span.setClientPort(request.getRemotePort());
		span.setServierIp(request.getLocalAddr());
		span.setServicePort(request.getLocalPort());
		span.setStart(System.currentTimeMillis());
		span.setInterfaceDesc(request.getRequestURL().toString());
		span.setTraceId(trace.getTraceId());
		trace.getSpans().add(span);
		spanArray.set(span);

		// 设置到当前线程
		MidaiLogTraceService.setMidaiLogTrace(trace);

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

		// TODO Auto-generated method stub

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

		MidaiLogTrace trace = MidaiLogTraceService.getMidaiLogTrace();

		String tmp = request.getHeader(MidaiLogTraceService.TRACE_ID);
		MidaiLogSpan span = spanArray.get();
		span.setEnd(System.currentTimeMillis());
		span.setResult(ex == null ? MidaiLogSpan.Result.SUCCESS : MidaiLogSpan.Result.FAIL);
		MidaiLogTraceService.traceSpanLog(span);
		if (tmp == null) {
			trace.setEnd(System.currentTimeMillis());
			trace.setResult(ex == null ? MidaiLogTrace.Result.SUCCESS : MidaiLogTrace.Result.FAIL);
			MidaiLogTraceService.traceLog(trace);
			
		}

	}

}

/**
 * Project Name:midai-monitor
 * File Name:MidaiLogDubboFiter.java
 * Package Name:com.midai.monitor
 * Date:2016年8月3日下午12:25:10
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
 */

package com.midai.framework.monitor;

import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.RpcInvocation;

/**
 * ClassName:MidaiLogDubboFiter <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2016年8月3日 下午12:25:10 <br/>
 * 
 * @author 陈勋
 * @version
 * @since JDK 1.7
 * @see
 */
public class MidaiLogDubboFilter implements Filter {

	@Override
	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {

		RpcContext context = RpcContext.getContext();
		MidaiLogTrace trace = null;
		MidaiLogSpan span=null;
		boolean root = false;
		if (context.isConsumerSide()) { // 消费者
			// 从当前线程获取trace对象
			trace = MidaiLogTraceService.getMidaiLogTrace();
			if (trace == null) {
				root = true;
				trace = new MidaiLogTrace();
				trace.setStart(System.currentTimeMillis());
				trace.setTraceId(MidaiLogTraceService.createTraceId());
				trace.setUrl(context.getUrl().toFullString());
				// 设置到当前线程
				MidaiLogTraceService.setMidaiLogTrace(trace);
			}
				span = new MidaiLogSpan();
				span.setClientIp(context.getLocalHost());
				span.setClientPort(context.getLocalPort());
				span.setServierIp(context.getRemoteHost());
				span.setServicePort(context.getRemotePort());
				span.setStart(System.currentTimeMillis());
				span.setInterfaceDesc(context.getUrl().toFullString());
				span.setTraceId(trace.getTraceId());
				trace.getSpans().add(span);
			
			// 把trace对象传递给服务者
			putTrace(trace, (RpcInvocation) invocation);
			Result result = invoker.invoke(invocation);
			
			span.setEnd(System.currentTimeMillis());
			span.setResult(result.hasException()?MidaiLogSpan.Result.SUCCESS:MidaiLogSpan.Result.FAIL);
			MidaiLogTraceService.traceSpanLog(span);
			if (root) {
				trace.setEnd(System.currentTimeMillis());
				trace.setResult(result.hasException()?MidaiLogTrace.Result.SUCCESS:MidaiLogTrace.Result.FAIL);
				MidaiLogTraceService.traceLog(trace);
			}

			return result;
		} else { // 服务者
			// 获取从消费者传递来的trace
			String traceId = getTrace(invocation);		
			if(traceId!=null){
				// 设置到当前线程
				trace=new MidaiLogTrace();
				trace.setTraceId(traceId);
				
				MidaiLogTraceService.setMidaiLogTrace(trace);
			}
			return invoker.invoke(invocation);

		}

	}

	private void putTrace(MidaiLogTrace trace, RpcInvocation invocation) {
		invocation.setAttachment(MidaiLogTraceService.TRACE_ID, trace.getTraceId());
	}

	private String getTrace(Invocation invocation) {
		return invocation.getAttachment(MidaiLogTraceService.TRACE_ID);
	}

}

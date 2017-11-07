package com.midai.framework.concurrent;

/**
 * 
 * ClassName: Callback <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2016年12月5日 下午2:06:54 <br/>
 *
 * @author 陈勋
 * @version 
 * @since JDK 1.7
 */
public interface Callback {

    public Object onGetLock() throws InterruptedException;

    public Object onTimeout() throws InterruptedException;
}

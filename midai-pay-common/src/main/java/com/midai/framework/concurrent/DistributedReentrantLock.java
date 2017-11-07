package com.midai.framework.concurrent;

import java.util.concurrent.TimeUnit;

/**
 * 
 * ClassName: DistributedReentrantLock <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2016年12月5日 下午2:07:11 <br/>
 *
 * @author 陈勋
 * @version 
 * @since JDK 1.7
 */
public interface DistributedReentrantLock {
    public boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException;

    public void unlock();
}

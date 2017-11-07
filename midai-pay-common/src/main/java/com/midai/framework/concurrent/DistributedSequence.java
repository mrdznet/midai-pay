package com.midai.framework.concurrent;

/**
 * 
 * ClassName: DistributedSequence <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2016年12月5日 下午2:17:55 <br/>
 *
 * @author 陈勋
 * @version 
 * @since JDK 1.7
 */
public interface DistributedSequence {

    public Long sequence(String sequenceName);
}

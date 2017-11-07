package com.midai.framework.concurrent.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.midai.framework.concurrent.DistributedSequence;

/**
 * 
 * ClassName: ZkDistributedSequence <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2016年12月5日 下午2:23:31 <br/>
 *
 * @author 陈勋
 * @version 
 * @since JDK 1.7
 */
public class ZkDistributedSequence implements DistributedSequence {
    private static final Logger log = LoggerFactory.getLogger(ZkReentrantLockCleanerTask.class);

    private CuratorFramework client;
     /**
     * Curator RetryPolicy maxRetries
     */
    private int maxRetries=3;
    /**
     * Curator RetryPolicy baseSleepTimeMs
     */
    private final int baseSleepTimeMs=1000;

    public ZkDistributedSequence(String zookeeperAddress){
        try{
            RetryPolicy retryPolicy = new ExponentialBackoffRetry(baseSleepTimeMs, maxRetries);
            client = CuratorFrameworkFactory.newClient(zookeeperAddress, retryPolicy);
            client.start();
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }catch (Throwable ex){
            ex.printStackTrace();
            log.error(ex.getMessage(),ex);
        }
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    public int getBaseSleepTimeMs() {
        return baseSleepTimeMs;
    }

    public Long sequence(String sequenceName) {
        try {
            int value=client.setData().withVersion(-1).forPath("/"+sequenceName,"".getBytes()).getVersion();
            return new Long(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

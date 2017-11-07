/**
 * Project Name:midai-pay-common
 * File Name:ZookeeperDistributedLockConfig.java
 * Package Name:com.midai.framework.config.zookeeper
 * Date:2016年12月5日下午2:27:12
 * Copyright (c) 2016, Shanghai Law Cloud Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.framework.config.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnResource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.midai.framework.concurrent.zookeeper.ZkDistributedLockTemplate;



/**
 * ClassName:ZookeeperDistributedLockConfig <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年12月5日 下午2:27:12 <br/>
 * @author   陈勋
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
@Configuration
@ConditionalOnProperty(name="lock.zookeeper.address")
public class ZookeeperDistributedLockAutoConfig {
	
	@Bean(initMethod="start" ,destroyMethod="close")
	public CuratorFramework  curatorFramework(@Value("${lock.zookeeper.address}")String zookeeper){
	    RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
	    CuratorFramework client = CuratorFrameworkFactory.newClient(zookeeper, retryPolicy);
	    return client;
	}
	
	@Bean
	public ZkDistributedLockTemplate zkDistributedLockTemplate(CuratorFramework curatorFramework){
		ZkDistributedLockTemplate zkDistributedLockTemplate =new ZkDistributedLockTemplate(curatorFramework);
		return zkDistributedLockTemplate;
	}

}


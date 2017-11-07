/**
 * Project Name:midai-pay-job
 * File Name:JobConfig.java
 * Package Name:com.midai.pay.job.config
 * Date:2016年9月26日上午9:46:52
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.pay.job.config;

import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.JobTypeConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.api.listener.ElasticJobListener;
import com.dangdang.ddframe.job.lite.spring.namespace.parser.common.AbstractJobConfigurationDto;
import com.dangdang.ddframe.job.lite.spring.schedule.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;

/**
 * ClassName:JobConfig <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2016年9月26日 上午9:46:52 <br/>
 * 
 * @author 陈勋
 * @version
 * @since JDK 1.7
 * @see
 */
@EnableConfigurationProperties(JobProperties.class)
// @MapperScan(basePackages="com.midai.pay.job.service.impl")
@Configuration
public class JobConfig implements ApplicationContextAware {

	private ApplicationContext applicationContext;

	@Autowired
	private JobProperties jobProperties;

	@Bean
	public ZookeeperConfiguration zookeeperConfiguration() {
		ZookeeperConfiguration zookeeperConfiguration = new ZookeeperConfiguration(jobProperties.getServerList(),
				jobProperties.getNamespace());
		zookeeperConfiguration.setBaseSleepTimeMilliseconds(jobProperties.getBaseSleepTimeMilliseconds());
		zookeeperConfiguration.setMaxSleepTimeMilliseconds(jobProperties.getMaxSleepTimeMilliseconds());
		zookeeperConfiguration.setMaxRetries(jobProperties.getMaxRetries());
		return zookeeperConfiguration;

	}

	    @PreDestroy
	    public void shutdownDestroy() {
	        //销毁调度器
	        String[] jobSchedulerNames = applicationContext.getBeanNamesForType(JobScheduler.class);
	        for (String jobSchedulerName : jobSchedulerNames) {
	            JobScheduler jobScheduler = (JobScheduler) applicationContext.getBean(jobSchedulerName);
	            jobScheduler.shutdown();
	        }
	    }
	
	@Bean(destroyMethod = "close")
	@Order(Integer.MAX_VALUE)
	public CoordinatorRegistryCenter regCenter(ZookeeperConfiguration zookeeperConfiguration) {
		CoordinatorRegistryCenter regCenter = new ZookeeperRegistryCenter(zookeeperConfiguration);
		regCenter.init();
		Map<String, SimpleJob> simpleMap = applicationContext.getBeansOfType(SimpleJob.class);
		int i=0;
		for (Entry<String, SimpleJob> simpleEntry : simpleMap.entrySet()) {
			final Class<? extends SimpleJob> cls = simpleEntry.getValue().getClass();
			SimpleMidaiJob sj = (SimpleMidaiJob) cls.getAnnotation(SimpleMidaiJob.class);
			SpringJobScheduler jobScheduler=	new SpringJobScheduler(regCenter, new AbstractJobConfigurationDto(sj.value(),  sj.cron(), sj.shardingTotalCount()) {			
				@Override
				protected JobTypeConfiguration toJobConfiguration(JobCoreConfiguration jobCoreConfig) {
					return new SimpleJobConfiguration(jobCoreConfig, cls.getCanonicalName());
				}
			}, new ElasticJobListener[]{});
			jobScheduler.setApplicationContext(this.applicationContext);
			  DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
	            String beanName = JobScheduler.class.getCanonicalName() + "$" + i;
	            beanFactory.registerSingleton(beanName, jobScheduler);
			jobScheduler.init();
			i++;
			
		}

		return regCenter;
	}
	
	

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;

	}

}

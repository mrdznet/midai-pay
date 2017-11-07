/**
 * Project Name:midai-pay-user-service
 * File Name:ActivitiConfig.java
 * Package Name:com.midai.pay.user.config
 * Date:2016年9月20日下午12:35:05
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.pay.user.config;

import java.io.IOException;

import javax.sql.DataSource;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.persistence.StrongUuidGenerator;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.transaction.PlatformTransactionManager;

import com.midai.framework.config.mybatis.MybatisAutoConfiguration;
import com.midai.pay.process.service.impl.NodeUserExtendServiceImpl;
import com.midai.pay.process.service.impl.ProcessFacadeServiceImpl;
import com.midai.pay.process.service.impl.RoleInOrgExtendServiceImpl;
import com.midai.pay.process.service.impl.RoleNoOrgExtendServiceImpl;
import com.midai.pay.user.mapper.SystemProcessExecutorConfigMapper;
import com.midai.pay.user.service.impl.SystemProcessExecutorConfigServiceImpl;

/**
 * ClassName:ActivitiConfig <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年9月20日 下午12:35:05 <br/>
 * @author   陈勋
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
@Configuration
@AutoConfigureAfter(value=MybatisAutoConfiguration.class)
public class ActivitiConfig {
    
    @Bean
    public ProcessEngineConfigurationImpl processEngineConfiguration(DataSource dataSource,PlatformTransactionManager transactionManager ) throws IOException{
        SpringProcessEngineConfiguration processEngineConfiguration=new SpringProcessEngineConfiguration();   
        processEngineConfiguration.setDataSource(dataSource);
        processEngineConfiguration.setTransactionManager(transactionManager);
        
        processEngineConfiguration.setDbIdentityUsed(false);
        processEngineConfiguration.setDatabaseSchemaUpdate(SpringProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        processEngineConfiguration.setJobExecutorActivate(false);
        ResourcePatternResolver resourceLoader=new PathMatchingResourcePatternResolver();
        processEngineConfiguration.setDeploymentResources(resourceLoader.getResources("classpath*:/META-INF/diagrams/*.bpmn20.xml"));
        processEngineConfiguration.setActivityFontName("宋体");
        processEngineConfiguration.setLabelFontName("宋体");
        processEngineConfiguration.setIdGenerator(new StrongUuidGenerator());
        return processEngineConfiguration;
    }
    
    @Bean
    public ProcessEngineFactoryBean processEngine(ProcessEngineConfigurationImpl processEngineConfiguration){
        ProcessEngineFactoryBean processEngine=new ProcessEngineFactoryBean();
        processEngine.setProcessEngineConfiguration(processEngineConfiguration);
        return processEngine;
    }
    @Bean
    public RepositoryService repositoryService(ProcessEngineFactoryBean processEngine) throws Exception{
        return processEngine.getObject().getRepositoryService();        
    }
    @Bean
    public RuntimeService runtimeService(ProcessEngineFactoryBean processEngine) throws Exception{
        return processEngine.getObject().getRuntimeService();
    }
    @Bean
    public TaskService taskService(ProcessEngineFactoryBean processEngine) throws Exception{
        return processEngine.getObject().getTaskService();
    }
    @Bean
    public HistoryService historyService(ProcessEngineFactoryBean processEngine) throws Exception{
        return processEngine.getObject().getHistoryService();
    }
    @Bean
    public ManagementService managementService(ProcessEngineFactoryBean processEngine) throws Exception{
        return processEngine.getObject().getManagementService();
    }
    
    @Bean
    public SystemProcessExecutorConfigServiceImpl processExecutorConfigService(SystemProcessExecutorConfigMapper systemProcessExecutorConfigMapper){
    	SystemProcessExecutorConfigServiceImpl processExecutorConfigService = new SystemProcessExecutorConfigServiceImpl(systemProcessExecutorConfigMapper);
    	return processExecutorConfigService;
    }
    
    @Bean
    public RoleInOrgExtendServiceImpl roleInOrgExtend(){
    	RoleInOrgExtendServiceImpl roleInOrgExtend = new RoleInOrgExtendServiceImpl();
    	return roleInOrgExtend;
    }
    
    @Bean
    public NodeUserExtendServiceImpl nodeUserExtend(){
    	NodeUserExtendServiceImpl nodeUserExtend = new NodeUserExtendServiceImpl();
    	return nodeUserExtend;
    }
    
    @Bean
    public ProcessFacadeServiceImpl processFacadeService(){
    	ProcessFacadeServiceImpl processFacadeService = new ProcessFacadeServiceImpl();
    	return processFacadeService;
    }
    
    @Bean
    public RoleNoOrgExtendServiceImpl roleNoOrgExtend(){
    	RoleNoOrgExtendServiceImpl roleNoOrgExtend = new RoleNoOrgExtendServiceImpl();
    	return roleNoOrgExtend;
    }

}


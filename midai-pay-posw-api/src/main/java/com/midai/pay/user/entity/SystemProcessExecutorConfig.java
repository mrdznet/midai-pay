/**
 * Project Name:chenxun-framework-start
 * File Name:User.java
 * Package Name:com.chenxun.framework.entity
 * Date:2016年9月1日上午10:12:33
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.pay.user.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.midai.framework.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ClassName:User <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年9月13日  <br/>
 * @author   wrt
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name="tbl_process_executor_config")
public class SystemProcessExecutorConfig extends BaseEntity implements Serializable{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id 
    private int id;
    
    private String processName;
    
    private String processKey;
    
    private String taskName;
    private String taskKey;
    private String executorExp;
    private String extendClassname;
    private Integer lastoneRedo;
    
 	public SystemProcessExecutorConfig(){
 	}
 	
 	public SystemProcessExecutorConfig(String processKey,String taskKey,String executorExp){
 		this.processKey = processKey;
 		this.taskKey = taskKey;
 		this.executorExp = executorExp;
 	}
    
}


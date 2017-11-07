/**
 * Project Name:chenxun-framework-start
 * File Name:User.java
 * Package Name:com.chenxun.framework.entity
 * Date:2016年9月1日上午10:12:33
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.pay.web.vo.system;

import java.io.Serializable;
import java.util.List;

import com.midai.pay.user.vo.SystemProcessExecutorConfigSVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(description="流程模块")
@Data
@EqualsAndHashCode(callSuper=false)
public class SystemProcessExecutorConfigVo implements Serializable{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("流程名称")
    private String name;
    
	@ApiModelProperty("流程节点集合")
    private List<SystemProcessExecutorConfigSVo> list;
}


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

import com.midai.pay.user.vo.SystemModuleSVo;

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
@ApiModel(description="菜单模块")
@Data
@EqualsAndHashCode(callSuper=false)
public class SystemModuleVo  implements Serializable{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("菜单ID")
    private int moduleId;
    
	@ApiModelProperty("菜单名称")
    private String moduleName;
    
	@ApiModelProperty("子菜单集合")
    List<SystemModuleSVo> list;
    
}


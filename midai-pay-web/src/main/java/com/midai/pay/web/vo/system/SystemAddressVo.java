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
@ApiModel(description="通用地址")
@Data
@EqualsAndHashCode(callSuper=false)
public class SystemAddressVo  implements Serializable{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	 @ApiModelProperty("行政区划编码")
    private String code;
    
	 @ApiModelProperty("名称")
    private String name;
    
}


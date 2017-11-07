/**
 * Project Name:midai-pay-web
 * File Name:ErrorMessage.java
 * Package Name:com.midai.pay.web.config
 * Date:2016年12月9日上午9:44:00
 * Copyright (c) 2016, Shanghai Law Cloud Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.pay.web.config;

import java.io.Serializable;

import lombok.Data;

/**
 * ClassName:ErrorMessage <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年12月9日 上午9:44:00 <br/>
 * @author   陈勋
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
@Data
public class ErrorMessage implements Serializable {
	
	/**
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么).
	 * @since JDK 1.7
	 */
	private static final long serialVersionUID = 1L;

	private String errorMsg;
	
	private String code;

}


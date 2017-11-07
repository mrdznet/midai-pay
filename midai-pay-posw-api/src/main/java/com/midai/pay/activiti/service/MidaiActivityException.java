/**
 * Project Name:midai-pay-posw-service
 * File Name:MidaiActivityException.java
 * Package Name:com.midai.pay.user.config
 * Date:2016年12月8日下午2:02:03
 * Copyright (c) 2016, Shanghai Law Cloud Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.pay.activiti.service;
/**
 * ClassName:MidaiActivityException <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年12月8日 下午2:02:03 <br/>
 * @author   陈勋
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
public class MidaiActivityException extends RuntimeException {
	
	/**
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么).
	 * @since JDK 1.7
	 */
	private static final long serialVersionUID = -8206844238134732611L;
	
	public final int HTTP_CODE= 802;
	
	public MidaiActivityException(){
		super();
	}
	
	public MidaiActivityException(String message){
		super(message);
	}
	

}


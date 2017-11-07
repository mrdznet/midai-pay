/**
 * Project Name:midai-pay-posp-service
 * File Name:MidaiPayHsmCallBack.java
 * Package Name:com.midai.pay.posp.config
 * Date:2016年11月16日上午10:06:43
 * Copyright (c) 2016, Shanghai Law Cloud Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.pay.posp.config;
/**
 * ClassName:MidaiPayHsmCallBack <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年11月16日 上午10:06:43 <br/>
 * @author   陈勋
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
public interface MidaiPayHsmCallBack {
	
	public void call(byte[] out);

}


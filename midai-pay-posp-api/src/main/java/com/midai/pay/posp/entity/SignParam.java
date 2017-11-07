/**
 * Project Name:midai-pay-sercurity-api
 * File Name:SignParam.java
 * Package Name:com.midai.pay.sercurity.entity
 * Date:2016年9月12日上午11:18:51
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.pay.posp.entity;

import lombok.Data;

/**
 * ClassName:SignParam <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年9月12日 上午11:18:51 <br/>
 * @author   陈勋
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
@Data
public class SignParam {
    
   
    private String  PHONENUMBER;  
  
    private String  TERMINALNUMBER;
 
    private String  PSAMCARDNO;
  
    private String  TRANCODE;
   
    private String  TERMINALSERIANO;

}


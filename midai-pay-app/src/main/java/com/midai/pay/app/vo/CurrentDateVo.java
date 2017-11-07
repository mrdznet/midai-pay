/**
 * Project Name:midai-pay-app
 * File Name:CurrentDateVo.java
 * Package Name:com.midai.pay.app.vo
 * Date:2016年9月20日下午2:41:19
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.pay.app.vo;

import java.io.Serializable;
import java.util.Date;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.KeyHolder;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * ClassName:CurrentDateVo <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年9月20日 下午2:41:19 <br/>
 * @author   陈勋
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
@Data
public class CurrentDateVo implements Serializable{
    
   
    private String DATE;  
	private String RSPCOD;
	private String RSPMSG;
    
    
    
    

}


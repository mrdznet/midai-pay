/**
 * Project Name:chenxun-framework-start
 * File Name:BaseEntity.java
 * Package Name:com.chenxun.framework.entity
 * Date:2016年9月1日上午10:11:05
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.framework.common;

import java.util.Date;

import lombok.Data;

/**
 * ClassName:BaseEntity <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年9月1日 上午10:11:05 <br/>
 * @author   陈勋
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
@Data
public class BaseEntity {
    
    private Date createTime;
    private Date updateTime;
    
    

}


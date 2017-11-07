/**
 * Project Name:chenxun-framework-start
 * File Name:BaseService.java
 * Package Name:com.chenxun.framework.common
 * Date:2016年9月1日上午10:46:58
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.framework.common;

import java.util.List;

/**
 * ClassName:BaseService <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年9月1日 上午10:46:58 <br/>
 * @author   陈勋
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
public interface BaseService<T extends BaseEntity> {
    
    
    public List<T> findAll();
    
    public T findByPrimaryKey(Object key);
    
    public int insert(T t);
    
    public int deleteByPrimaryKey(Object key);
    
    public int update(T t);
    
    public T findOne(T t);
    

}


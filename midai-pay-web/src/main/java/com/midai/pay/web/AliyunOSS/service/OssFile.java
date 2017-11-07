/**
 * Project Name:midai-car-web-manage
 * File Name:OssFile.java
 * Package Name:com.midai.car.controller
 * Date:2016年5月31日上午9:28:23
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.pay.web.AliyunOSS.service;

import io.swagger.annotations.ApiModel;

import java.util.List;

import lombok.Data;
/**
 * ClassName:OssFile <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年5月31日 上午9:28:23 <br/>
 * @author   陈勋
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
@ApiModel
public @Data class OssFile {
	
	public enum Type { 
		DIRECTORY, FILE
    }
	
	public enum Access{
		
		READ,WRITE,DELETE
	}

	/**
	 * 文件名称
	 */
	private String name;
	
	
	/**
	 * 唯一编号(文件路径)
	 */
	private String key;
	
	/**
	 * 文件类型（文件夹或者文件）
	 */
	private Type type ;
	
	/**
	 * 上级编号（只能指向文件夹）
	 */
	private String parentKey;
	
	/**
	 * 文件权限
	 */
	private List<Access> accessList;
	
	

}


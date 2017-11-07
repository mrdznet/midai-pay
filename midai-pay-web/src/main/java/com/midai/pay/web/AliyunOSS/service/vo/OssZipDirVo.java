/**
 * Project Name:midai-car-web-manage
 * File Name:OssZipDirVo.java
 * Package Name:com.midai.car.controller.vo
 * Date:2016年8月8日下午5:00:14
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.pay.web.AliyunOSS.service.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

import lombok.Data;

/**
 * ClassName:OssZipDirVo <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年8月8日 下午5:00:14 <br/>
 * @author   陈勋
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
@ApiModel
@Data
public class OssZipDirVo implements Serializable {
	
	@ApiModelProperty(value="压缩文件url")
	private String zipPath;
}


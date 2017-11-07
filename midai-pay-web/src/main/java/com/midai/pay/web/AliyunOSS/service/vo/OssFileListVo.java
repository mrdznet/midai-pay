/**
 * Project Name:midai-car-web-manage
 * File Name:OSSFileListVo.java
 * Package Name:com.midai.car.controller.vo
 * Date:2016年8月8日下午4:22:43
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.pay.web.AliyunOSS.service.vo;

import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.midai.pay.web.AliyunOSS.service.OssFile;

import lombok.Data;

/**
 * ClassName:OSSFileListVo <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年8月8日 下午4:22:43 <br/>
 * @author   陈勋
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
@ApiModel
@Data
public class OssFileListVo implements Serializable {
	
	private List<OssFile> list=new ArrayList<>();

}


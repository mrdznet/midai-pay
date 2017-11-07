/**
 * Project Name:midai-pay-app
 * File Name:SignVo.java
 * Package Name:com.midai.pay.app.vo
 * Date:2016年9月8日下午3:27:53
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.pay.app.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

/**
 * ClassName:SignVo <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年9月8日 下午3:27:53 <br/>
 * @author   陈勋
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */

@Data
public class SignVo implements Serializable{
    
    @Length(max=11,min=11)
    @ApiModelProperty(value="手机号",example="18860971839")
    private String PHONENUMBER;
    @ApiModelProperty(value="终端号",example="5011603100000349")
    private String TERMINALNUMBER;
    @ApiModelProperty(value="PSAM卡号",example="5021603040000466")
    private String PSAMCARDNO;
    @ApiModelProperty(value="接口码",example="170000")
    private String TRANCODE;
    @ApiModelProperty(value="终端流水号",example="000001")
    private String TERMINALSERIANO;
    
    


}


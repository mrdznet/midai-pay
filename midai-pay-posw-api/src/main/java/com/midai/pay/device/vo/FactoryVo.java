package com.midai.pay.device.vo;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class FactoryVo implements Serializable {

	private static final long serialVersionUID = 1L;
	
    private Integer id;
    /* 名称 */
    private String name;
    /* 地址 */
    private String address;
    
    /* 备注 */
    private String note;
    /*状态(1: 启用, 0:暂停)*/
    private String state;

}

package com.midai.pay.customer.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@ApiModel
public class CustomerImgVo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	
	private String mercNo;
	
	private String url;
	
	// 类型(1:手持银行卡, 2:身份证正面, 3:身份证反面, 4:银行卡, 5:经营资质, 6:经营场景1, 7:经营场景1, 8:补充照片)
	private Integer type;
}

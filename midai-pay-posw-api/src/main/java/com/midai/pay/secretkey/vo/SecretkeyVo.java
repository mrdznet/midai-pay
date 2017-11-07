package com.midai.pay.secretkey.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class SecretkeyVo {

	@ApiModelProperty("厂商")
	private String facture;
	
	@ApiModelProperty("批次")
	private String batch;
	
	@ApiModelProperty("密钥数量")
	private Integer num;
	
	@ApiModelProperty("密钥生成结果提示:如提示密钥生成成功！")
	private String message;
}

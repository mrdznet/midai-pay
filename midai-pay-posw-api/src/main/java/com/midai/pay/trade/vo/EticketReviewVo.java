package com.midai.pay.trade.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class EticketReviewVo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value="交易流水号, 多个流水以逗号分隔")
	private String hostTransSsn;
	
	@ApiModelProperty(value="拒绝原因")
	private String reason;
	
	@ApiModelProperty(value="是否重签:0-否, 1:是")
	private String reSign;
	
	@ApiModelProperty(value="是否发送短信:0-否, 1:是")
	private Integer sendMsg;
	
	private Integer state;
	private String createUser;
}

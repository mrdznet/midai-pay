package com.midai.pay.trade.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class TradeReviewVo implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="大图地址")
	private String bigImgUrl;
	
	@ApiModelProperty(value="缩略图地址 ")
	private String smallImgUrl;
	
	@ApiModelProperty(value="交易流水号")
	private String hostTransSsn;
	
	@ApiModelProperty(value="状态")
	private String state;
	
	@ApiModelProperty(value="商户类型")
	private String mercType;
	
	@ApiModelProperty(value="小票号")
	private String mercId;
	
	@ApiModelProperty(value="金额")
	private String money;
	
	@ApiModelProperty(value="商户名")
	private String mercName;
	
    @ApiModelProperty(value="卡类型")
    private String cardKind;
}

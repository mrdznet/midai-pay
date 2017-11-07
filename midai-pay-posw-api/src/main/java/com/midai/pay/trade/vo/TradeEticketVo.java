package com.midai.pay.trade.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;



import lombok.Data;

@Data
@ApiModel
public class TradeEticketVo implements Serializable {


	@ApiModelProperty(value="小票号")
	private String mchntCodeIn;
	
	@ApiModelProperty(value="商户名称")
	private String mchntName;
	
    @ApiModelProperty(value="交易时间")
	private Date transTime;
	
    @ApiModelProperty(value="终端号")
    private String deviceNoIn;
    
    @ApiModelProperty(value="交易类型")
	private String transCodeName;
    
    @ApiModelProperty(value="交易状态  0:成功  1:已上传 5:失败 ")
    private String transStatus;
    
    @ApiModelProperty(value="返回码")
    private String respCdLoc;
    
    @ApiModelProperty(value="交易金额")
	private Double transAmt;
	
    @ApiModelProperty(value="交易卡号")
	private String transCardNo;
    
    @ApiModelProperty(value="交易审核状态 0:待审核  1:通过 2:拒绝 ")
	private Integer state;
    
    @ApiModelProperty(value="卡类型")
    private String cardKind;
	
}

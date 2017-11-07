package com.midai.pay.settlemoney.vo;

import java.util.Date;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class SettleCustomerVo {

	@ApiModelProperty(value="小票号")
	private String mercId;
	
	@ApiModelProperty(value="商户名称")
	private String mercName;
	
	@ApiModelProperty(value="提现日期")
	private Date tixianDatetime;
	
	@ApiModelProperty(value="提现金额")
	private Double tixianAmt;
	
	@ApiModelProperty(value="提现手续费")
	private Double tixianFeeamt;
	
	@ApiModelProperty(value="开户名")
	private String accountName;
  
	@ApiModelProperty(value="卡号")
	private String accountNo;
	
	@ApiModelProperty(value="开户行名称")
	private String branchBankName;
	
	@ApiModelProperty(value="清算状态  0:待审   1:审核通过")
	private Integer settleState;

    @ApiModelProperty(value="操作时间")
	private Date settleDate;
	
    @ApiModelProperty(value="操作人")
	private String settlePerson;
	
    @ApiModelProperty(value="流水号")
    private String logNo;
    
    @ApiModelProperty(value="省")
	private String peovinceId;
	
    @ApiModelProperty(value="市")
	private String cityId;
    
    @ApiModelProperty(value="交易审核状态 0:待审 2:审核通过  4:冻结")
    private Integer checkState;
    
    @ApiModelProperty(value="省名称")
    private String provinceName;
    
    @ApiModelProperty(value="市名称")
    private String cityName;
	
}

package com.midai.pay.settlemoney.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

import lombok.Data;

import com.midai.framework.query.PaginationQuery;

@Data
@ApiModel
public class BoGetMoneyQuery extends PaginationQuery implements Serializable {

	@ApiModelProperty(value="小票号")
	private String mercId;
	
	@ApiModelProperty(value="商户名称")
	private String mercName;
	
	@ApiModelProperty("提现开始时间")
	private String startTime;
	
	@ApiModelProperty("提现结束时间")
	private String endTime;
	
	//交易
    @ApiModelProperty(value="审核状态 0:待审 2:审核通过  4:冻结")
	private Integer checkState;
    
    @ApiModelProperty(value="提现金额开始")
	private Integer tixianAmtStart;
    
    @ApiModelProperty(value="提现金额结束")
	private Integer tixianAmtEnd;
	
	@ApiModelProperty(value="卡号")
	private String accountNo;
	
	@ApiModelProperty(value="流水号")
    private String logNo;
	    
}

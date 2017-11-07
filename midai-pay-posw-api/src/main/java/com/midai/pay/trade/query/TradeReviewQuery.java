package com.midai.pay.trade.query;

import java.io.Serializable;
import java.util.Date;

import com.midai.framework.query.PaginationQuery;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@ApiModel
public class TradeReviewQuery extends PaginationQuery implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value="流水号")
	private String hostTransSsn;
	
	@ApiModelProperty(value="商户小票号")
	private String mercId;
	
	@ApiModelProperty(value="终端编号")
	private String terminalNo;
	
	@ApiModelProperty(value="交易审核状态")
	private Integer  state;
	
	@ApiModelProperty(value="商户类型")
	private Integer mercType;
	
	@ApiModelProperty(value="商户名称")
	private String mercName;
	
	@ApiModelProperty(value="交易日期-起")
	private String transDateStart;
	
	@ApiModelProperty(value="交易日期-止")
	private String transDateEnd;
	
	@ApiModelProperty(value="交易金额-起")
	private String transAmtStart;
	
	@ApiModelProperty(value="交易金额-止")
	private String transAmtEnd;
}

package com.midai.pay.autopaymoney.query;

import java.io.Serializable;

import com.midai.framework.query.PaginationQuery;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@ApiModel
public class BoAutoPayMoneyQuery extends PaginationQuery implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6888800358009781686L;

	@ApiModelProperty("商户小票号")
	private String mercId;
	
	@ApiModelProperty("商户名称")
	private String mercName;
	
	@ApiModelProperty("开户名")
	private String accountName;
	
	@ApiModelProperty("开户账号")
	private String accountNo;
	
	@ApiModelProperty("提现开始日期")
	private String tixianDatetimeStart;
	
	@ApiModelProperty("提现结束日期")
	private String tixianDatetimeEnd;
	
	@ApiModelProperty("打款通道")
	private String payChannel;
	
	@ApiModelProperty("开户行")
	private String branchBankName;
	
	@ApiModelProperty("结算状态 0:待结算;1:结算中;3:结算成功;4:结算失败")
	private Integer payState;

	@ApiModelProperty("结算开始日期")
	private String  payTimeStart;
	
	@ApiModelProperty("结算结束日期")
	private String payTimeEnd;
}

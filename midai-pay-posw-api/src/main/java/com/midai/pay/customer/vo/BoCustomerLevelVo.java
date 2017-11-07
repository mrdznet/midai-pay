package com.midai.pay.customer.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@ApiModel
public class BoCustomerLevelVo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value="等级")
	private String level;

	@ApiModelProperty(value="单笔限额")
	private Double limitTradSingle;	//单笔限额
	
	@ApiModelProperty(value="单卡最高限额")
	private Double limitSingleCardMax;	//单卡最高限额
	@ApiModelProperty(value="单卡最低限额")
	private Double limitSingleCardMin;	//单卡最低限额
	@ApiModelProperty(value="单卡限额笔数")
	private Integer limitSingleCardCount;	//单卡限额笔数
	
	@ApiModelProperty(value="日限额")
	private Double limitTradDay;	//日限额
	@ApiModelProperty(value="日限额笔数")
	private Integer limitTradDayCount;	//日限额笔数
	
	@ApiModelProperty(value="月限额")
	private Double limitTradMonth;	//月限额
	@ApiModelProperty(value="月限额笔数")
	private Integer limitTradMonthCount;	//月限额笔数
	
	@ApiModelProperty(value="备注")
	private String note;
	
}

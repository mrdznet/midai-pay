package com.midai.pay.frozen.query;

import java.io.Serializable;

import com.midai.framework.query.PaginationQuery;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@ApiModel
public class TradeFrozenQuery extends PaginationQuery implements Serializable{
	
	@ApiModelProperty(value="商户小票号")
	private String mercId;
	
	@ApiModelProperty(value="商户名称")
	private String mercName;
	
	@ApiModelProperty(value="冻结日期始")
	private String frozenTimeBegin;
	
	@ApiModelProperty(value="冻结日期止")
	private String frozenTimeEnd;
	
	@ApiModelProperty(value="解冻日期始")
	private String unfrozenTimeBegin;
	
	@ApiModelProperty(value="解结日期止")
	private String unfrozenTimeEnd;
	
	@ApiModelProperty(value="流水号")
	private String hostTransssn;
	
}

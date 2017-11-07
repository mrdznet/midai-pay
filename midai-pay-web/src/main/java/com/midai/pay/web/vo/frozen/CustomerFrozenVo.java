package com.midai.pay.web.vo.frozen;



import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class CustomerFrozenVo {
	
	@ApiModelProperty(name="主键")
	private Integer id;

	@ApiModelProperty(name="商户小票号")
	private String mercId;
	
	@ApiModelProperty(name="商户名称")
	private String mercName;
	
	@ApiModelProperty(name="冻结时间")
	private Date frozenTime;
	
	@ApiModelProperty(name="解冻时间")
	private Date unfrozenTime;
	
	@ApiModelProperty(name="冻结原因")
	private String frozenReason;
	
	@ApiModelProperty(name="冻结操作人")
	private String frozenPerson;
	
	@ApiModelProperty(name="解冻操作人")
	private String unfrozenPerson;
	
	
	
	
}

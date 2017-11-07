package com.midai.pay.qrcode.query;

import java.io.Serializable;

import com.midai.framework.query.PaginationQuery;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@ApiModel
public class AgentQrCodeCustomerQuery  extends PaginationQuery implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty("二维码编号")
	private String qrCodeNo;
	
	@ApiModelProperty("代理商编号")
	private String agentNo;
	
	@ApiModelProperty("未绑定标识 0未绑定， 1绑定")
	private String unbound;
}

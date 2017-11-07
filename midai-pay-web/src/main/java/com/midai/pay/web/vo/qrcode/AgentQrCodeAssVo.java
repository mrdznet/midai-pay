package com.midai.pay.web.vo.qrcode;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@ApiModel
public class AgentQrCodeAssVo  implements Serializable  {

	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty("代理商编号")
	private String agentNo;
	@ApiModelProperty("新增数量")
	private Integer count;
}

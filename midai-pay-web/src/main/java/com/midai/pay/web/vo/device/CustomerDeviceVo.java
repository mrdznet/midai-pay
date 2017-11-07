package com.midai.pay.web.vo.device;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel
public class CustomerDeviceVo implements Serializable {

	private static final long serialVersionUID = 1L;
	
    @ApiModelProperty("商户号")
    private String mercNo;

}

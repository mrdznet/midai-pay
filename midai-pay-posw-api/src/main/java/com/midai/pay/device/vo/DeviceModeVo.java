package com.midai.pay.device.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@ApiModel
public class DeviceModeVo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3006219231212408983L;

	@ApiModelProperty(value="主键")
	private Integer id;
	
	@ApiModelProperty(value="型号名称")
	private String name;
	
	@ApiModelProperty(value="厂商编号")
	private Integer factoryId;
	
	@ApiModelProperty(value="厂商编号")
	private String factoryName;
	
	@ApiModelProperty(value="设备类型id")
	private Integer deviceTypeId;
	
	@ApiModelProperty(value="设备类型名称")
	private String deviceTypeName;
	
	@ApiModelProperty(value="状态 0:废除,1:启用,2:暂停")
	private Integer state;
	
	@ApiModelProperty(value="状态 0:废除,1:启用,2:暂停")
	private String stateName;
	
	@ApiModelProperty(value="备注")
	private String note;

}

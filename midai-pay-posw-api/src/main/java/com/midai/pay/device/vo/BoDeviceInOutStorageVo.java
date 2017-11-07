package com.midai.pay.device.vo;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@ApiModel
public class BoDeviceInOutStorageVo implements Serializable {
	
	@ApiModelProperty("编号机身号")
    private String deviceNo;
	
	@ApiModelProperty("创建时间")
	private Date createTime;
	

	/*@ApiModelProperty("状态1 入库 2出库 3变更 4使用")
	private String operateState;*/
		
	@ApiModelProperty("源代理商名称")
	private String agentName;
	
	@ApiModelProperty("目标代理商名称")
	private String destagentName;
	
	
	@ApiModelProperty("操作员")
	private String  appUser;
	
	@ApiModelProperty("状态 0 入库  1出库  2变更")
	private String operateState;
	
}

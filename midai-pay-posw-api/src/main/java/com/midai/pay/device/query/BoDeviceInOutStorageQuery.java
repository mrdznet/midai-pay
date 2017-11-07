package com.midai.pay.device.query;

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
public class BoDeviceInOutStorageQuery  extends PaginationQuery implements Serializable{
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7981295934812453059L;

	@ApiModelProperty("编号机身号始")
	private String  deviceNoBegin;
	
	@ApiModelProperty("编号机身号终")
	private String  deviceNoEnd;
	
	@ApiModelProperty("创建时间始")
	private String createTimeBegin;
	
	@ApiModelProperty("创建时间终")
	private String  createTimeEnd;
	
	private String deviceNobn;
	
	@ApiModelProperty("状态 0 入库 1出库 2变更 ")
	private String operateState;

	

	

}

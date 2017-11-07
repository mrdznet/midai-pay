package com.midai.pay.device.query;

import java.io.Serializable;

import com.midai.framework.query.PaginationQuery;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@ApiModel
public class BoDeviceModeQuery extends PaginationQuery implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3736747661745094076L;

}

package com.midai.pay.handpay.query;

import java.io.Serializable;

import com.midai.framework.query.PaginationQuery;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@ApiModel
@Data
@EqualsAndHashCode(callSuper = false)
public class HyTztxQuery extends PaginationQuery implements Serializable {

    private static final long serialVersionUID = 6265058636890769728L;

	@ApiModelProperty(value="流水号")
	private String hostTransSsn;
    

}

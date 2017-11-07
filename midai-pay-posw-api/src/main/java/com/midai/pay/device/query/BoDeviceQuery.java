package com.midai.pay.device.query;

import com.midai.framework.query.PaginationQuery;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@ApiModel
@Data
@EqualsAndHashCode(callSuper = false)
public class BoDeviceQuery extends PaginationQuery implements Serializable {

    private static final long serialVersionUID = 6265058636890769728L;

    @ApiModelProperty("编号机身号")
    private String deviceNo;
    
    @ApiModelProperty("编号机身号开始")
    private String deviceNoStart;
    
    @ApiModelProperty("编号机身号结束")
    private String deviceNoEnd;
    
    private String deviceNobn;
    
    @ApiModelProperty("商户编号")
    private String customerId;

    @ApiModelProperty("设备类型名")
    private String typeName;
    @ApiModelProperty("设备型号名")
    private String modeName;

    private String agentNo;
    
    @ApiModelProperty("商户手机号")
    private String mobile;
}

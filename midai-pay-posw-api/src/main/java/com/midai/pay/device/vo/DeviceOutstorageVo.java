package com.midai.pay.device.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceOutstorageVo implements Serializable {

    @NotNull
    @ApiModelProperty("出库批次号")
    private String ckNo;

    @NotNull
    @ApiModelProperty("源代理商编号")
    private String agentId;

    @NotNull
    @ApiModelProperty("源代理商名称")
    private String agentName;

    @NotNull
    @ApiModelProperty("目标代理商编号")
    private String destagentId;

    @NotNull
    @ApiModelProperty("目标代理商名称")
    private String destagentName;


    @ApiModelProperty("设备出库详情")
    private List<DeviceOutstorageDetailVo> outstorageDetailList;
}

package com.midai.pay.device.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@ApiModel("设备入库")
@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceInstorageVo implements Serializable {

    private static final long serialVersionUID = -4343945596019069463L;

    @NotNull
    @ApiModelProperty("入库单号")
    private String rkNo;

    @ApiModelProperty("厂商编号")
    private Integer factoryId;

    @NotNull
    @ApiModelProperty("厂商名称")
    private String factoryName;

    @ApiModelProperty("设备类型id")
    private Integer deviceTypeId;

    @NotNull
    @ApiModelProperty("设备类型")
    private String deviceTypeName;

    @ApiModelProperty("设备型号id")
    private Integer deviceModeId;

    @NotNull
    @ApiModelProperty("设备型号")
    private String deviceModeName;

    @Length(min = 16)
    @ApiModelProperty("机身号始")
    private String bodyNoStart;

    @ApiModelProperty("数量")
    private Integer num;

    @ApiModelProperty("代理商编号")
    private String agentId;

    @NotNull
    @ApiModelProperty("代理商名称")
    private String agentName;

    @ApiModelProperty("入库设备明细")
    private List<DeviceInstorageDetailVo> deviceDetailList;

    @ApiModelProperty("入库设备明细中设备号")
    private String beforeBodyNos;
}

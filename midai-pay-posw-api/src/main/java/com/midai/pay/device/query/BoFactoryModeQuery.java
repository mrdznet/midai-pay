package com.midai.pay.device.query;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.midai.framework.query.PaginationQuery;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@ApiModel
@Data
@EqualsAndHashCode(callSuper=false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BoFactoryModeQuery extends PaginationQuery implements Serializable {

    private static final long serialVersionUID = 8215833172153047227L;

    /* 厂商名称 */
    private String factoryName;
    /* 设备型号 */
    private String modeName;
}

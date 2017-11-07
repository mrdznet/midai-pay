package com.midai.pay.customer.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper=false)
@ApiModel
public class BoCustomerDeviceVo implements Serializable {

    private static final long serialVersionUID = -8479354209452475313L;

    /* 商户编号 */
    private String mercNo;

    /* 商户名称 */
    private String mercName;

    /* 小票号 */
    private String mercId;

    /* 绑定时间 */
    private Date bundingTime;

    /* 解绑时间 */
    private Date unbundingTime;

}

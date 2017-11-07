package com.midai.pay.device.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.midai.framework.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "tbl_bo_device")
public class BoDevice extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -3515199487165992764L;

    @Id
    private Integer id;
    /* 入库单号 */
    private String rkNo;
    /* 设备编号机身号 */
    private String deviceNo;
    /* sim卡号 */
    private String simNo;
    /* 状态;1-未开通，2-已开通，3-激活,4-废除停用 */
    private Integer state;
    /* 设备状态-暂时只显示用 */
    private String deviceStatus;
    /* 商户编号 */
    private String customerId;
    /* 首选项：1:首选|0:否 */
    private Integer isFirst;
    /* 设备型号表id */
    @Column(name = "devicemode_id")
    private Integer deviceModeId;
    
}

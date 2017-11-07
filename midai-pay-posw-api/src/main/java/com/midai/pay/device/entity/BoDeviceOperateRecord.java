package com.midai.pay.device.entity;

import com.midai.framework.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "tbl_bo_device_operate_record")
public class BoDeviceOperateRecord extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 4058072335057310540L;

    @Id
    private String batchNo; /* 批次号 */

    @Id
    private String deviceNo; /* 设备编号机身号 */

}

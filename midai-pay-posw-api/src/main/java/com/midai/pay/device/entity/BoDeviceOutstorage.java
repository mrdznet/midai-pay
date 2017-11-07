package com.midai.pay.device.entity;

import com.midai.framework.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "tbl_bo_device_outstorage")
public class BoDeviceOutstorage extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 3585140304439571499L;

    @Id
    private Integer id;
    /* 出库批次号 */
    private String ckNo;
    /* 源代理商编号 */
    private String agentId;
    /* 源代理商名称 */
    private String agentName;
    /* 目标代理商编号 */
    private String destagentId;
    /* 目标代理商名称 */
    private String destagentName;
    /* 状态1 出库 2变更 */
    private Integer operateState;
    /* 操作人 */
    private String appUser;
    
}

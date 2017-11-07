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
@Table(name = "tbl_bo_iostorage")
public class BoIostorage extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -1743121819094252686L;

    @Id
    private Integer id; /* 出入库表Id */

    private String deviceId; /* 对应设备表ID */

    private String deviceNo; /* 设备机身号 */
    
    private Integer deviceFlag; /* 1-表示POS，2-SIM */

    private String agentId; /* 源代理商编号 */
    
    private String agentName; /* 代理商名称 */
    
    private String destagentId; /* 目标代理商编号 */
    
    private String destagentName; /* 目标代理商名称 */
    
    private String destmercId; /* 目标商户编号 */
    
    private String state; /* 状态 */
    
    private Integer operatorId; /* 操作人Id */
    
}

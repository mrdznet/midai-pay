/**
 * Project Name:midai-pay-sercurity-api
 * File Name:StoTidInfoEntity.java
 * Package Name:com.midai.pay.sercurity.entity
 * Date:2016年9月19日上午9:54:49
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.pay.posp.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.midai.framework.common.BaseEntity;

/**
 * ClassName:StoTidInfoEntity <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年9月19日 上午9:54:49 <br/>
 * @author   陈勋
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */

@Entity
@Table(name="tbl_sto_tid_info")
@EqualsAndHashCode(callSuper=false)
@Data
public class StoTidInfoEntity extends BaseEntity implements Serializable {
    
    //app_type, app_sub_type, mchnt_id, term_id
    
    private String appType;
    
    private String appSubType;
    
    private String mchntId;
    
    private String termId;
    
    private String terminalId;

}


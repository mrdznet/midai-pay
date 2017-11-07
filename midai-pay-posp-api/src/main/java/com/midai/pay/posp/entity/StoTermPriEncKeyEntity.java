/**
 * Project Name:midai-pay-sercurity-api
 * File Name:StoTermPriEncKeyEntity.java
 * Package Name:com.midai.pay.sercurity.entity
 * Date:2016年9月18日下午3:52:19
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.pay.posp.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.hibernate.validator.constraints.Length;

import com.midai.framework.common.BaseEntity;

/**
 * ClassName:StoTermPriEncKeyEntity <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年9月18日 下午3:52:19 <br/>
 * @author   陈勋
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
@Entity
@Table(name="tbl_sto_term_pri_enc_key")
@EqualsAndHashCode(callSuper=false)
@Data
public class StoTermPriEncKeyEntity extends BaseEntity  implements Serializable{
    
    @Id
    @Length(max=48)
    private String termTmkIdx;
    @Length(max=48)
    private String termTmk;
    @Length(max=4)
    private String sekIndId;
    @Length(max=1)
    private String encAlgo;
    @Length(max=32)
    private String eventId;
    @Length(max=1)
    private String operSt;    
    private Date recCrtTime;   
    private Date recUpdTime;   
    @Length(max=10)
    private BigDecimal recUpdTimes;
    @Length(max=32)
    private String oprId;
    @Length(max=32)
    private String recId;
    
   
    
    

    

}


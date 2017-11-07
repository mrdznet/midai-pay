/**
 * Project Name:midai-pay-sercurity-api
 * File Name:StoTermWkEncKeyEntity.java
 * Package Name:com.midai.pay.sercurity.entity
 * Date:2016年9月19日上午10:19:34
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
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.hibernate.validator.constraints.Length;

import com.midai.framework.common.BaseEntity;

/**
 * ClassName:StoTermWkEncKeyEntity <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年9月19日 上午10:19:34 <br/>
 * @author   陈勋
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
@Entity
@Table(name="tbl_sto_term_wk_enc_key")
@EqualsAndHashCode(callSuper=false)
@Data
public class StoTermWkEncKeyEntity extends BaseEntity implements Serializable {
    
    @Id
    @NotNull
    @Length(max=60)
    private String termWkId;
    
    @NotNull
    @Length(max=4)
    private String sek;
    @Length(max=1)
    private String encAlgo;
    @NotNull
    @Length(max=48)
    private String termPikEncKey;
    @NotNull
    @Length(max=48)
    private String termMacEncKey;
    @Length(max=48)
    private String termTckEncKey;
    @Length(max=48)
    private String oldTermPikEncKey;
    @Length(max=48)
    private String oldTermMacEncKey;
    @Length(max=48)
    private String oldTermTckEncKey;
    @Length(max=32)
    private String eventId;
    @Length(max=1)
    private String operSt;    
    private Date recCrtTime;  
    private Date recUpdTime;   
    private BigDecimal recUpdTimes;
    @Length(max=32)
    private String oprId;
    @Length(max=32)
    private String recId;
    
    


}


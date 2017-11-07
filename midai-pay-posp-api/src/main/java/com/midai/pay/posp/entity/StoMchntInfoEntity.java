/**
 * Project Name:midai-pay-sercurity-api
 * File Name:StoTermPriEncKeyEntity.java
 * Package Name:com.midai.pay.sercurity.entity
 * Date:2016年9月18日下午2:43:56
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.pay.posp.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.hibernate.validator.constraints.Length;

import com.midai.framework.common.BaseEntity;

/**
 * ClassName:StoTermPriEncKeyEntity <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年9月18日 下午2:43:56 <br/>
 * @author   陈勋
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */

@Entity
@Table(name="tbl_sto_mchnt_info")
@EqualsAndHashCode(callSuper=false)
@Data
public class StoMchntInfoEntity  extends BaseEntity implements Serializable{

    @NotNull
    @Length(max=4)
    private String appType;
    @NotNull
    @Length(max=4)
    private String appSubType;    
    @NotNull
    @Length(max=15)
    private String mchntId;
    @NotNull
    @Length(max=11)
    private String acqInsId;
    @Length(max=11)
    private String settInsId;
    @Length(max=32)
    private String customerId;
    @Length(max=32)
    private String mchntAppId;
    @Length(max=10)
    private String mchntGrp;
    @Length(max=1)
    private String amtRouterFlag ;
    @Length(max=200)
    private String mchntName;
    @Length(max=40)
    private String mchntAbbr;
    @Length(max=200)
    private String mchntEnName;
    @Length(max=40)
    private String enAbbr;
    @Length(max=4)
    private String mccCode;
    @Length(max=2)
    private String mchntProvince;
    @Length(max=4)
    private String mchntCity;
    @Length(max=6)
    private String mchntCounty;
    @Length(max=90)
    private String mchntAddr;
    @Length(max=2)
    private String mchntKind;
    @Length(max=20)
    private String mchntPhone;
    @Length(max=30)
    private String fax;
    @Length(max=80)
    private String email;
    @Length(max=6)
    private String mchntZipCode;
    @Length(max=30)
    private String contactName ;
    @Length(max=30)
    private String corpContactName;
    @Length(max=11)
    private String fwdInsId;
    @Length(max=3)
    private String currCode ;
    @Length(max=10)
    private String countryCode;
    @Length(max=4)
    private String acqAreaCode;
    @Length(max=255)
    private String mchntCurryAuthBmp;
    @Length(max=255)
    private String mchntCardAuthBmp ;
    @Length(max=12)
    private BigDecimal signAmt;
    @Length(max=200)
    private String mchntAmtCtrlFlag;
    @Length(max=12)
    private BigDecimal transLowerLimit=new BigDecimal(0);
    @Length(max=12)
    private BigDecimal transUpperLimit =new BigDecimal(9999999999L);
    @Length(max=12)
    private BigDecimal perConsuTransAmt ;
    @Length(max=12)
    private BigDecimal perWithdrawTransAmt;
    @Length(max=12)
    private BigDecimal perTransferTransAmt;
    @Length(max=12)
    private BigDecimal perColloectTransAmt;
    @Length(max=12)
    private BigDecimal perPayTransAmt;
    @Length(max=12)
    private BigDecimal creditTransLimit;
    @Length(max=12)
    private BigDecimal creditConsuTransLimit;
    @Length(max=12)
    private BigDecimal creditWithdrawTransLimit;
    @Length(max=12)
    private BigDecimal creditTransferTransLimit;
    @Length(max=12)
    private BigDecimal creditColloTransLimit;
    @Length(max=12)
    private BigDecimal creditPayTransLimit;
    @Length(max=1)
    private String special_fee_value;
    @Length(max=2)
    private String special_fee_type;
    @Length(max=11)
    private String belongBranch;
    @Length(max=11)
    private String branchCode;
    @Length(max=1)
    private String misFlag;
    @Length(max=1)
    private String fcFlag;
    @Length(max=1)
    private String fallbackFlag;
    @Length(max=1)
    private String mchntCtrlFlag;
    @Length(max=1)
    private String mchntBmsFlag;
    @Length(max=4)
    private String clearCycleGrp ="0000";
    @Length(max=8)
    private String appBeginDate ;
    @Length(max=8)
    private String appEndDate;
    @Length(max=200)
    private String remark;
    @Length(max=32)
    private String eventId;
    @Length(max=1)
    private String operSt;
    @Length(max=26)
    private String recCrtTime;
    @Length(max=26)
    private String recUpdTime;
    @Length(max=10)
    private BigDecimal recUpdTimes;
    @Length(max=32)
    private String oprId;
    @Length(max=64)
    private String recId;

}


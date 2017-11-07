package com.midai.pay.posp.entity;

import java.util.Date;

import javax.persistence.Table;

import com.midai.framework.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Table(name="tbl_par_card_bin")
@EqualsAndHashCode(callSuper=false)
public class ParCardBin extends BaseEntity {
	

	  private String cardCataId;
	
	  private String cardBin;
	  
	  private String cardBinOffset;
	  
	  private String cardBinLen;
	  
	  private String trackFlag;
	  
	  private String cardKindId;
	  
	  private String panOffset;
	  
	  private String cardLen;
	  
	  private String cardBrandId;
	  
	  private String cardNameCn;
	  
	  private String unionPayFlag;
	  
	  private String issInsCode;
	  
	  private String issKind;
	  
	  private String cardMediaId;
	  
	  private int cardTransLimit;
	  
	  private String cardTransLimitFlag;
	  
	  private int cardDayTransLimit;
	  
	  private String flag;
	  
	  private String eventId;
	  
	  private String operSt;
	  
	  private Date recCrtTime;
	  
	  private Date recUpdTime;
	  
	  private int recUpdTimes;
	  
	  private String oprId;
	  
	  private String recId;
	  
	  
	  

}

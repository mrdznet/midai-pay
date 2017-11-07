package com.midai.pay.posp.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import com.midai.framework.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Table(name = "tbl_deal_total_pos")
@EqualsAndHashCode(callSuper=false)
public class DealTotalPos extends BaseEntity {
	  
	  @Id
	  private String hostTransSsn;
	
	  @Column(name="send_8583")
	  private String send8583;
	  @Column(name="recieve_8583")
	  private String recieve8583;
}

package com.midai.pay.frozen.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.midai.framework.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name="mi_ipla_frozenreason")
public class CustomerFrozen extends BaseEntity implements Serializable {
	
	/**
	 * 主键唯一
	 */
	@Id
	private Integer id;
	
	/**
	 * 序号
	 */
	@Column(name="unid")
	private String unid;
	
	/**
	 * 高位风险代码
	 */
	@Column(name="code")
	private String code;
	
	/**
	 * 商户小票号
	 */
	@Column(name="merc_id")
	private String mercId;
	
	/**
	 * 商户名称
	 */
	@Column(name="merc_name")
	private String  mercName;
	
	/**
	 *虚拟商户状态
	 */
     @Column(name="virtual_account_state")
     private String virtualAccountState;
     
	/**
	 * 冻结时间
	 */
     @Column(name="frozen_time")
     private Date frozenTime;
	
	/**
	 * 冻结人员
	 */
     @Column(name="frozen_person")
     private String frozenPerson;
	
	/**
	 *预留字段1 
	 */
     @Column(name="reserved1")
     private String reserved1;
     
     /**
 	 *预留字段2 
 	 */
      @Column(name="reserved2")
      private String reserved2;
      
     /**
  	 *冻结解冻编号 
  	 */
       @Column(name="reserved3")
       private String reserved3;
	
	/**
	 *问题卡号
	 */
       @Column(name="card_no")
       private String cardNo;
	
	/**
	 * 冻结原因
	 */
       @Column(name="frozen_reason")
       private String frozenReason;
       
       @Column(name="frozen_id")
       private String frozenId;
	
	/**
	 * 解冻时间
	 */
       @Column(name="unfrozen_time")
       private Date unfrozenTime;
	
	/**
	 * 解冻人员
	 */
      @Column(name="unfrozen_person")
      private String unfrozenPerson;
      
      /**
       * 解冻原因
       */
      @Column(name="unfrozen_reason")
      private String unfrozenReason;

}

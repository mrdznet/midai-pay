package com.midai.pay.secretkey.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.midai.framework.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "tbl_bo_secretkey")
public class BoSecretkey extends BaseEntity implements Serializable {

	 private static final long serialVersionUID = -1743121819094252686L;
	 
	 /**
	  * 厂商表
	  */
	 private String facture;
	 
	 /**
	  * 批次
	  */
	 private String batch;
	 
	 /**
	  * 附件
	  */
	 private String fj;
}

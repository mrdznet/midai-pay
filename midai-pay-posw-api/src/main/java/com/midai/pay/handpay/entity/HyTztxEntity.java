package com.midai.pay.handpay.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.midai.framework.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name="tbl_bo_hy_tztx_log")
public class HyTztxEntity extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	private Integer Id;
	private String hostTramsSsn;
	private String reqData;
	private String respData;
	
	

}

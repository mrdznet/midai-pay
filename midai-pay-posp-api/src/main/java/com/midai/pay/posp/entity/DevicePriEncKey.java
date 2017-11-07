package com.midai.pay.posp.entity;

import java.io.Serializable;

import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.context.annotation.Primary;

import com.midai.framework.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Table(name="tbl_device_pri_enc_key")
@EqualsAndHashCode(callSuper=false)
public class DevicePriEncKey extends BaseEntity implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -809847227669494919L;

	@Id
	private String deviceNo;
	
	private String priKey; 
	
	private String sekId;
	
	
	
}

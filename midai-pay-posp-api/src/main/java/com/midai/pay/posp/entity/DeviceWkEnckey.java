package com.midai.pay.posp.entity;

import java.io.Serializable;

import javax.persistence.Id;
import javax.persistence.Table;

import com.midai.framework.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Table(name="tbl_device_wk_enc_key")
@EqualsAndHashCode(callSuper=false)
public class DeviceWkEnckey extends BaseEntity implements Serializable{
	
	  /**
	 * 
	 */
	private static final long serialVersionUID = -3696732278797617694L;
	@Id
	  private String deviceNo;
	  @Id
	  private String mercId;
	  
	  private String sekId;
	  
	  private String termPikEncKey;
	  
	  private String termMacEncKey;
	  
	  private String termTckEncKey;

}

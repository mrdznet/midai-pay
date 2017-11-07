package com.midai.pay.trade.entity;

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
@Table(name="tbl_bo_eticket")
public class BoEticket extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	private Integer id;
	
	@Column(name="create_user")
	private String createUser;

	@Column(name="note")
	private String note;
	
	@Column(name="state")
	private Integer state;
	
	@Column(name="host_trans_ssn")
	private String hostTransSsn;
	
	@Column(name="un_through_reason")
	private String unThroughReason;
	
	@Column(name="return_sign")
	private Integer returnSign;
	
	@Column(name="merc_id")
	private String mercId;
	
	@Column(name="channel_pic")
	private String channelPic;
	
	@Column(name="create_date")
	private Date createDate;

}

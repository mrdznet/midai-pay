package com.midai.pay.posp.entity;

import javax.persistence.Column;
import javax.persistence.Table;

import com.midai.framework.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Table(name = "tbl_pay_mishua")
@EqualsAndHashCode(callSuper=false)
public class PayMishua extends BaseEntity {
	@Column(name="deal_ssn")
	private String dealSsn;
	@Column(name="pay_ssn")
	private String paySsn;
	@Column(name="pay_send")
	private String paySend;
	@Column(name="pay_recieve")
	private String payRecieve;
	@Column(name="query_ssn")
	private String querySsn;
	@Column(name="query_send")
	private String querySend;
	@Column(name="query_recieve")
	private String queryRecieve;
}

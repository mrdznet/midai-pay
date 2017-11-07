package com.midai.pay.customer.entity;

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
@Table(name="tbl_bo_customer_level")
public class BoCustomerLevel extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String level;

	@Column(name="limit_trad_single")
	private Double limitTradSingle;	//单笔限额
	
	@Column(name="limit_single_card_max")
	private Double limitSingleCardMax;	//单卡最高限额
	@Column(name="limit_single_card_min")
	private Double limitSingleCardMin;	//单卡最低限额
	@Column(name="limit_single_card_count")
	private Integer limitSingleCardCount;	//单卡限额笔数
	
	@Column(name="limit_trad_day")
	private Double limitTradDay;	//日限额
	@Column(name="limit_trad_day_count")
	private Integer limitTradDayCount;	//日限额笔数
	
	@Column(name="limit_trad_month")
	private Double limitTradMonth;	//月限额
	@Column(name="limit_trad_month_count")
	private Integer limitTradMonthCount;	//月限额笔数
	
	@Column(name="note")
	private String note;
	
	@Column(name="create_time")
	private Date createTime;
	
	@Column(name="update_time")
    private Date updateTime;
}

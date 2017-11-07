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
@Table(name = "tbl_secretkey")
public class Secretkey extends BaseEntity implements Serializable  {
	
	private String  deviceNo;
	private String ptmk;
	private String tmk;
	private String tmkCheckValue;
}

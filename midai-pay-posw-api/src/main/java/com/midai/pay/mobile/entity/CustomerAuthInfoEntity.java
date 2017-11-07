package com.midai.pay.mobile.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.midai.framework.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "tbl_bo_customer_auth")
public class CustomerAuthInfoEntity extends BaseEntity implements Serializable{
	
	private static final long serialVersionUID = -8377186350265990359L;
	private Integer id;
	private String loginTel;//用户登录的手机号码
	private String authTel;//4要素验证时手机号码
	private String idCard;//4要素验证身份证号码
	private String customer;//4要素验证用户姓名
	private String bankCard;//4要素验证的银行卡号
}

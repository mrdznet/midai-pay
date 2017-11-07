package com.midai.pay.user.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.midai.framework.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name="tbl_bank_branch")
public class BankBranch extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String branchbankcode;
	
	private String branchbankname;
	
	private String bankcode;
	
}

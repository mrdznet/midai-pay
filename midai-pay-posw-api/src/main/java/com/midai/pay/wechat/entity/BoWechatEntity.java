package com.midai.pay.wechat.entity;

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
@Table(name="tbl_bo_wechat")
public class BoWechatEntity extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	private Integer id;
	
	private String createuser;
	
	private String updateuser;
	
	private Integer isend;
	
	private String tixilogno;
	
	private String paytime;
	
	private String payperson;
	
	private Integer paystate;
	
	private String errorcode;
	
	private String errormsg;
	
	private Integer paycount;
	
	private Integer paymoney;
	
	private String channelcode;
	
	private String autohitflag;
	
	private String payremark;
	
	private String paylogno;
	
	private String paychannel;
	
	private String mercid;
	
	private String bnakname;
	
	private String accountname;
	
	private String accountno;
	
	private Integer tixiflag;
	
	private String inscode;
	
	private String mercname;
	
	private String tixiandatetime;
	
	private String autoflag;
	
	private String bankcode;
	
	private String cjcodechannel;
	
}

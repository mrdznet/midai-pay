package com.midai.pay.web.vo.customer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;

import com.midai.pay.customer.vo.CustomerImgVo;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper=false)
@ApiModel
public class CustomerVo {

		private static final long serialVersionUID = 1L;
	   
		@ApiModelProperty(value="商户编号")
		private String mercNo;
		
		@ApiModelProperty(value="代理商ID")
		private String agentId;
		
		@ApiModelProperty(value="代理商name")
		private String agentName;
	
		@ApiModelProperty(value="顶级代理商ID")
		private String topAgentId;
	
		@ApiModelProperty(value="顶级代理商name")
		private String topAgentName;

		@ApiModelProperty(value="商户name")
		private String mercName;
	
		@ApiModelProperty(value="小票号")
		private String mercId;
	
		@ApiModelProperty(value="法人名称")
		private String legalName;

		@ApiModelProperty(value="身份证号")
		private String idCard;
		
		@ApiModelProperty(value="地址")
		private String address;

		@ApiModelProperty(value="电话号码")
		private String mobile;
		
		@ApiModelProperty(value="经营范围")
		private String scobus;
	 
		@ApiModelProperty(value="商户类别 0:普通商户  1:自动到账商户")
		private Integer merAuto;
		
		@ApiModelProperty(value="商户类型 0:个人 1:普通用户")
		private Integer merType; 

		@ApiModelProperty(value="所属省")
		private String peovinceId;
		
		@ApiModelProperty(value="所属市")
		private String cityId;

		@ApiModelProperty(value="开户名")
		private String accountName;
	  
		@ApiModelProperty(value="开户账号")
		private String accountNo;
		
		@ApiModelProperty(value="开户总行ID")
		private String bankId;
		
		@ApiModelProperty(value="开户支行ID")
		private String branchbankId;
	
		@ApiModelProperty(value="商户联系人")
		private String merlinkman;

		@ApiModelProperty(value="商户联系人手机号")
		private String merlinkmobile;
		
		@ApiModelProperty(value="电子邮件")
		private String email;

		@ApiModelProperty(value="qq")
		private String qq;
		
		@ApiModelProperty(value="申请设备描述")
		private String description;

		@ApiModelProperty(value="状态 0:申请中  1:初审中 2:复审中 3:初审未通过 4:审核通过")
	    private Integer state;

		@ApiModelProperty(value="开通状态:0:关闭 1:开通 2:冻结 3:黑名单")
	    private Integer opstate ;
		
		@ApiModelProperty(value="借记卡扣率(%)")
	    private Double feeRate;

		@ApiModelProperty(value="借记卡封顶")
	    private Double uplmt;
		
		@ApiModelProperty(value="贷记卡扣率(%)")
	    private Double dfeeRate;
		
		@ApiModelProperty(value="备注")
	    private String note;
		
		@ApiModelProperty(value="申请人")
	    private String applyName;

		@ApiModelProperty(value="开户行")
		private String branchbankName;
		
		@ApiModelProperty(value="初审人员")
		private String firstInstancePerson;
		
		@ApiModelProperty(value="风控人员")
		private String windControlPerson;

	    private Date createTime;
	    private Date updateTime;
}

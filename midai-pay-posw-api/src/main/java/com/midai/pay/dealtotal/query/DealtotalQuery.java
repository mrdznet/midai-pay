package com.midai.pay.dealtotal.query;

import java.io.Serializable;

import com.midai.framework.query.PaginationQuery;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ClassName:User <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年9月21日  <br/>
 * @author   zt
 * @version  
 * @since    JDK 1.7
 * @see 	 
*/

@Data
@EqualsAndHashCode(callSuper=false)
@ApiModel
public class DealtotalQuery  extends PaginationQuery implements Serializable{
    
	private static final long serialVersionUID = 1L;

// --------------------------------------------------------页面查询字段	
	@ApiModelProperty(value="交易流水号")
	 private String hostTransSsn;
	 	
	@ApiModelProperty(value="交易类型（消费-0200,消费冲正-0400,余额查询-0210,提现申请-0500,转账-0700,信用卡还款-0600,退款-0220）")
	private String transCode;
	
	@ApiModelProperty(value="交易时间起") 
	private String transTimeBegin;
	
	@ApiModelProperty(value="交易时间终")
	private String  transTimeEnd;
	
	@ApiModelProperty(value="商户名称")
	private String mchntName;
	
	@ApiModelProperty(value="交易金额起")
	private Double transAmtBegin;
	
	@ApiModelProperty(value="交易金额止")
	private Double transAmtEnd;
	
	@ApiModelProperty(value="交易方式（1-mpos, 2-无卡, 3-大pos）")
	private String payMode;
	
	@ApiModelProperty(value="卡类型(01-借记卡, 02-贷记卡)")
	private  String cardKind;
	
//	-----------------------------------------数据过滤字段
	@ApiModelProperty(value="代理商编号")
	 private String agentId;
	
	@ApiModelProperty(value="商户小票号")
     private String mchntCodeIn;
	 @ApiModelProperty(value="交易状态 0:成功   1:已上传  5:失败   ")
	 private String transStatus;
	 @ApiModelProperty(value="机身号")
	 private String deviceNoIn;
	 @ApiModelProperty(value="银行卡号")
	 private String transCardNo;
	 @ApiModelProperty(value="商户手机号")
	 private String mobile;
	 @ApiModelProperty(value="大商户")
	 private String mchntCodeOut;
	 @ApiModelProperty(value="通道机构")
	 private  String routInstIdCd;
	 
}

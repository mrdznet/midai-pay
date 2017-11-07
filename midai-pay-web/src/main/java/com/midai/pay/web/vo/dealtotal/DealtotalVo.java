package com.midai.pay.web.vo.dealtotal;

import java.util.Date;

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
@ApiModel
public class DealtotalVo {
	
	@ApiModelProperty(value="交易流水号")
	private String hostTransSsn;

	@ApiModelProperty(value="返回码")
	private String respCdLoc;
	
	@ApiModelProperty(value="返回信息")
	private  String respCdLocDsp;

	@ApiModelProperty(value="商户小票号")
    private String mchntCodeIn;

	@ApiModelProperty(value="商户名称")
	private String mchntName;
	
	@ApiModelProperty(value="商户手机号")
	 private String mobile;

	@ApiModelProperty(value="机身号")
	private String deviceNoIn;	
	
	@ApiModelProperty(value="交易时间")
	private Date transTime;
	
	/*@ApiModelProperty(value="交易总数")
	private Integer cc;*/
	
	@ApiModelProperty(value="交易金额")
	private String transAmt;
	
	@ApiModelProperty(value="银行卡号")
	private String transCardNo;
	
	@ApiModelProperty(value="发卡行")
	private  String cardIssName;
	
	@ApiModelProperty(value="卡类型")
	private  String cardKind;
	
	@ApiModelProperty(value="发卡行编号")
	private String cardIssId;
	
	@ApiModelProperty(value="交易类型 ")
    private String transCode;
	
	@ApiModelProperty(value="商户类型")
	private String specialFeeType;
	
	@ApiModelProperty(value="交易状态")
	private String transStatus;
	 
	 @ApiModelProperty(value="大商户")
	 private String mchntCodeOut;
	 
	 @ApiModelProperty(value="通道机构")
	 private   String routInstIdCd;
	 
	 @ApiModelProperty(value="代理商编号")
	 private String agentId;
	
	 @ApiModelProperty(value="代理商名称")
	 private String agentName;
	 
	 @ApiModelProperty(value="借记卡结算手续费（固定值）%")
	 private Double instFeeRate;
	
	 @ApiModelProperty(value="贷记卡结算手续费（固定值）%")
	 private Double instDfeeRate;
	 
	 @ApiModelProperty(value="结算手续费（固定值）%")
	 private Double instaFeeRate;
	 
	 @ApiModelProperty(value="结算手续费（封顶值）%")
	 private Integer instFeeMax;
	 
	 @ApiModelProperty(value="商户扣率") 
	 private Double mchntRate;
	
	

	/*String tempDate = "2016-09-27 23:00:00";
	public void temp() throws ParseException {
		Date time = new SimpleDateFormat().parse(tempDate);
	}*/
	
	

}

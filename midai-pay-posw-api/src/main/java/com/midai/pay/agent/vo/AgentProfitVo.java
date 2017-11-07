package com.midai.pay.agent.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * 
*    
* 项目名称：midai-pay-posw-api   
* 类名称：AgentProfit   
* 类描述：  代理商分润
* 创建人：wrt   
* 创建时间：2017年7月5日 上午10:50:48   
* 修改人：wrt   
* 修改时间：2017年7月5日 上午10:50:48   
* 修改备注：   
* @version    
*
 */

@ApiModel("代理商分润辅助类")
@Data
public class AgentProfitVo implements Serializable {

	private static final long serialVersionUID = 1L;
	

	
	@ApiModelProperty("交易流水号")
	private String hostTransSsn;
	
	@ApiModelProperty("商户号")
	private String mercNo;
	
	@ApiModelProperty("代理商编号")
	private String agentId;
	
	@ApiModelProperty("代理商名称")
	private String agentName;
	
	@ApiModelProperty("父代理商编号")
	private String parentAgentId;
	
	@ApiModelProperty("父代理商名称")
	private String parentAgentName;
	
	@ApiModelProperty("交易笔数")
	private Integer transCount;
	
	@ApiModelProperty("交易金额")
	private Double transAmt;
	
	@ApiModelProperty("本级利润")
	private Double profit;
	
	@ApiModelProperty("下级利润")
	private Double profitSub;
	
	@ApiModelProperty("总利润")
	private Double profitTotal;
	
	@ApiModelProperty("本级已发利润")
	private Double profitOut;
	
	@ApiModelProperty("下级已发利润")
	private Double profitSubOut;
	
	@ApiModelProperty("结算状态:0未结算 1已结算")
	private Integer settlementStatus;
	
	@ApiModelProperty("代理商规范代码")
	private String code;
	
	@ApiModelProperty("结算时间")
	private String settlementTime;
	
	@ApiModelProperty("卡号")
	private String transCardNo;
	
	@ApiModelProperty("卡类型：01借记卡 02贷记卡")
	private String cardKind;
	
	@ApiModelProperty("交易类型 1MPOS 2无卡 3传统POS 4扫码-微信 5扫码-支付宝 6扫码-银联 7扫码-花呗 8扫码-其他 9扫码-京东")
	private Integer transType;
	
	@ApiModelProperty("交易类型 1MPOS 2无卡 3传统POS 4扫码-微信 5扫码-支付宝 6扫码-银联 7扫码-花呗 8扫码-其他 9扫码-京东")
	private String transTypeName;
}

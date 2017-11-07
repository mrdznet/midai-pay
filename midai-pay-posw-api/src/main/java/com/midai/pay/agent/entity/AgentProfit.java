package com.midai.pay.agent.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.midai.framework.common.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
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

@ApiModel("代理商分润")
@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name="tbl_bo_agent_profit")
public class AgentProfit extends BaseEntity  implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	@ApiModelProperty("主键ID")
	private Integer Id;
	
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
	
	@ApiModelProperty("交易金额")
	private Integer transAmt;
	
	@ApiModelProperty("本级利润")
	private Integer profit;
	
	@ApiModelProperty("下级利润")
	private Integer profitSub;
	
	@ApiModelProperty("总利润")
	private Integer profitTotal;
	
	@ApiModelProperty("本级已发利润")
	private Integer profitOut;
	
	@ApiModelProperty("下级已发利润")
	private Integer profitSubOut;
	
	@ApiModelProperty("结算状态:0未结算 1已结算")
	private Integer settlementStatus;
	
	@ApiModelProperty("代理商规范代码")
	private String code;
	
	@ApiModelProperty("结算时间")
	private Date settlementTime;
	
	@ApiModelProperty("刷卡成本信息-借记卡封顶手续费")
	private Double   swingCardLimit;
	
	@ApiModelProperty("刷卡成本信息-借记卡手续费")
	private Double   swingCardDebitRate;
	
	@ApiModelProperty("刷卡成本信息-贷记卡手续费")
	private Double   swingCardCreditRate;
	
	@ApiModelProperty("刷卡成本信息-单笔手续费")
	private Double   swingCardSettleFee;
	   
	@ApiModelProperty("无卡成本信息-借记卡手续费率")
	private Double   nonCardDebitRate;
	
	@ApiModelProperty("无卡成本信息-贷记卡手续费率")
	private Double   nonCardCreditRate;
	
	@ApiModelProperty("传统POS成本信息-借记卡封顶手续费")
	private Double   posDebitLimit;
	
	@ApiModelProperty("传统POS成本信息-借记卡手续费")
	private Double   posDebitRate;
	
	@ApiModelProperty("传统POS成本信息-单笔手续费")
	private Double   posSettleFee;
	
	@ApiModelProperty("传统POS成本信息-贷记卡手续费")
	private Double   posCreditRate;
	
	@ApiModelProperty("扫码成本信息-微信手续费率")
	private Double   scanCodeWxRate;
	
	@ApiModelProperty("扫码成本信息-支付宝手续费率")
	private Double   scanCodeZfbRate;
	
	@ApiModelProperty("扫码成本信息-银联扫码手续费率")
	private Double   scanCodeYlRate;
	
	@ApiModelProperty("京东白条手续费")
	private Double   scanCodeJdbtRate;
	
	@ApiModelProperty("其它手续费")
	private Double   scanCodeOtherRate;
	
	private Double   scanCodeMyhbRate;
	
	@ApiModelProperty("交易类型： 1MPOS 2无卡 3传统POS 4扫码-微信 5扫码-支付宝 6扫码-银联 7扫码-花呗 8扫码-其他 9扫码-京东")
	private Integer transType;
}

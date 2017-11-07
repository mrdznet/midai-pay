package com.midai.pay.agent.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商户签约扣率
 * @author miaolei
 * 2016年9月22日 上午10:08:51
 */
@Data
@EqualsAndHashCode(callSuper=false)
@ApiModel
public class AgentMerchantRateVo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7184216034122769720L;
	
	
	/**
	 * 应前端需要添加，无业务含义
	 */
	@ApiModelProperty(value="前端标识")
	private Integer id;

	@ApiModelProperty(value="商户编号")
	private String mercNo;
	
	@ApiModelProperty(value="商户名称")
	private String mercName;
	
	@ApiModelProperty(value="借记卡扣率(%)")
	private Double feeRate;
	
	@ApiModelProperty(value="借记卡封顶")
	private Integer uplmt;
	
	@ApiModelProperty(value="贷记卡扣率(%)")
	private Double dfeeRate;
	
	@ApiModelProperty(value="单笔手续费")
	private Double singleMoney;
}

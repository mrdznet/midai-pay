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
public class DealtotalQuickQuery  extends PaginationQuery implements Serializable{
    
	private static final long serialVersionUID = 1L;

// --------------------------------------------------------页面查询字段	
	@ApiModelProperty(value="交易流水号")
	 private String seqId;
	 	
	@ApiModelProperty(value="商户名称")
	private String mercName;
	
	@ApiModelProperty(value="交易时间起") 
	private String transTimeBegin;
	
	@ApiModelProperty(value="交易时间终")
	private String  transTimeEnd;
	
	@ApiModelProperty(value="交易金额起")
	private Double transAmtBegin;
	
	@ApiModelProperty(value="交易金额止")
	private Double transAmtEnd;
	
	@ApiModelProperty(value="交易方式（1-mpos, 2-无卡, 3-大pos）")
	private String transChannel;
	
//	-----------------------------------------数据过滤字段
	@ApiModelProperty(value="代理商编号")
	 private String agentId;
}

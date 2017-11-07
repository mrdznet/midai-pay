package com.midai.pay.mobile;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@ApiModel("扫码支付返回信息")
@Data
@EqualsAndHashCode(callSuper=false)
public class ScanAppBaseEntity extends AppBaseEntity {
	
		@ApiModelProperty("交易时间")
	    private String SENDTIME;
		@ApiModelProperty("支付链接")
	    private String IMGURL;
		@ApiModelProperty("交易类型")
	    private String TRANSTYPE;
	    @ApiModelProperty("交易流水号")
	    private String SENDSEQID;
	    @ApiModelProperty("商户编码")
	    private String MERCNO;
	    @ApiModelProperty("商户名称")
	    private String MERCNAME;
}

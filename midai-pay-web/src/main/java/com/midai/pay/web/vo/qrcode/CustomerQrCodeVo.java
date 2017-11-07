package com.midai.pay.web.vo.qrcode;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@ApiModel
public class CustomerQrCodeVo   implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String mercNo;
	private List<String> qrCodes;

}

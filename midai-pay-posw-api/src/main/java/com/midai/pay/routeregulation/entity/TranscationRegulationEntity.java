package com.midai.pay.routeregulation.entity;

import java.io.Serializable;
import java.util.Date;

import com.midai.framework.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class TranscationRegulationEntity extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	/** * 卡号 */
	private String cardNo;
	/** * 商户手机号 */
	private String mobile;
	/** * 区域 */
	private String areaCode;
	/** * 初始金额 */
	private Double moneyStart;
	/** * 最高金额 */
	private Double moneyEnd;
	/** * 交易开始时间 */
	private Date dateStart;
	/** * 交易结束时间 */
	private Date dateEnd;
	/** * 规则对应备选通道编码 */
	private String instCodes;
	/** * 状态 0 不可用，1 可用 */
	private Integer status;
	
	
	
}

package com.midai.pay.routehistory.entity;

import lombok.Data;

@Data
public class HistoryData {

	/** * 通道编码 */
	private String instCode;
	/** * 通道总交易额 */
	private Double transactionTotalAmount;
	/** * 交易月数 */
	private Integer transactionMonths;
	/** * 当月通道交易额 */
	private Double currentMonthAmount;
}

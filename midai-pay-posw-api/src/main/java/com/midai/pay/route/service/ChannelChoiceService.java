package com.midai.pay.route.service;

import java.util.Set;

import com.midai.pay.routetransactionInformation.TransactionInformation;

public interface ChannelChoiceService {

	/**
	 * 获取通道列表
	 * @param tsv 交易信息
	 * @return
	 */
	public Set<String> getChannelList(TransactionInformation tsv);
}

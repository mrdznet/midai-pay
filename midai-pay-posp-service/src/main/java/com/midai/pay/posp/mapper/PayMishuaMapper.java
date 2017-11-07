package com.midai.pay.posp.mapper;

import org.apache.ibatis.annotations.Update;

import com.midai.framework.config.mybatis.MyMapper;
import com.midai.pay.posp.entity.PayMishua;

public interface PayMishuaMapper extends MyMapper<PayMishua> {
	@Update(" update tbl_pay_mishua set pay_recieve = #{payRecieve}"
			+ " where deal_ssn =  #{dealSsn} and pay_ssn = #{paySsn}")
	int updatePayMishuaRecieve(PayMishua payMishua);
	
	@Update(" update tbl_pay_mishua set query_ssn = #{querySsn},query_send = #{querySend}"
			+ " where deal_ssn =  #{dealSsn} and pay_ssn = #{paySsn}")
	int updatePayMishuaQuery(PayMishua payMishua);
	
	@Update(" update tbl_pay_mishua set query_recieve = #{queryRecieve}"
			+ " where deal_ssn =  #{dealSsn} and query_ssn = #{querySsn}")
	int updatePayMishuaQueryRecieve(PayMishua payMishua);
}

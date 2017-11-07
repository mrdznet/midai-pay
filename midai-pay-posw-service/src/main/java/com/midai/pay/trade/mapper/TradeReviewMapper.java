package com.midai.pay.trade.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import com.midai.framework.config.mybatis.MyMapper;
import com.midai.pay.mobile.vo.EticketSignVo;
import com.midai.pay.trade.entity.DealCustomer;
import com.midai.pay.trade.entity.DealTotal;
import com.midai.pay.trade.entity.SaleeSlip;
import com.midai.pay.trade.query.TradeReviewQuery;
import com.midai.pay.trade.vo.TradeReviewVo;

public interface TradeReviewMapper extends MyMapper<DealTotal>{
	
	@SelectProvider(type=com.midai.pay.trade.provider.TradeReviewProvider.class, method="findQuery")
	List<TradeReviewVo> findQuery(TradeReviewQuery query);
	
	@SelectProvider(type=com.midai.pay.trade.provider.TradeReviewProvider.class, method="findQueryCount")
	int findQueryCount(TradeReviewQuery query);
	
	@SelectProvider(type=com.midai.pay.trade.provider.TradeReviewProvider.class, method="findCardInfo")
	SaleeSlip findCardInfo(String hostTransSsn);
	
	@Update(" update tbl_deal_total set eticket_path=#{imgUrl} where host_trans_ssn=#{hostTransSsn}")
	int updateEticketPath(@Param("hostTransSsn")String hostTransSsn, @Param("imgUrl")String imgUrl);
	
	@SelectProvider(type=com.midai.pay.trade.provider.TradeReviewProvider.class, method="findAllByHostTransSsn")
	List<DealCustomer> findAllByHostTransSsn(String hostTransSsn);
	
	@Select(" SELECT count(1) FROM tbl_deal_total t LEFT JOIN tbl_bo_eticket e ON t.host_trans_ssn=e.host_trans_ssn "
			+ " LEFT JOIN tbl_bo_customer c ON t.mchnt_code_in=c.merc_id WHERE e.return_sign=1 AND c.mobile=#{mobile}")
	int countReturnSign(String mobile);
	
	@Select(" SELECT b.host_trans_ssn LOGNO, DATE_FORMAT(b.trans_time,'%Y-%m-%d %H:%i:%s') DATETIME, b.trans_code_name TRANSCODENAME, round(b.trans_amt/100,2) TRANSAMT, b.trans_card_no CARDNO,"
			+"			b.sign_path PICPATH, b.card_iss_name BANKNAME, e.un_through_reason REASON "
			+" FROM tbl_deal_total b, tbl_bo_eticket e"
			+" WHERE e.host_trans_ssn=b.host_trans_ssn AND b.mobile=#{mobile} AND e.return_sign=1 AND b.trans_status=0 ")
	List<EticketSignVo> findAllReSign(String mobile);
	
	@SelectProvider(type=com.midai.pay.trade.provider.TradeReviewProvider.class, method="channelEticketQuery")
	List<TradeReviewVo> channelEticketQuery(TradeReviewQuery query);
	
	@SelectProvider(type=com.midai.pay.trade.provider.TradeReviewProvider.class, method="channelEticketCount")
	int channelEticketCount(TradeReviewQuery query);
	
	@Select(" select eticket_path from tbl_deal_total where host_trans_ssn=#{hostTransSsn}")
	String findEticketPath(String hostTransSsn);
	
	@Select(" select count(1) from tbl_deal_total where host_trans_ssn=#{hostTransSsn}")
	int hostTransSsnExit(String hostTransSsn);
}

package com.midai.pay.trade.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.midai.framework.config.mybatis.MyMapper;
import com.midai.pay.customer.entity.BoCustomer;
import com.midai.pay.trade.entity.BoEticket;
import com.midai.pay.trade.vo.EticketReviewVo;

public interface BoEticketMapper extends MyMapper<BoEticket> {
	
	@Insert(" INSERT tbl_bo_eticket(state, host_trans_ssn,create_date, create_user) VALUES(#{state}, #{hostTransSsn},#{createDate},#{createUser}) ")
	int insertEticket(BoEticket eticket);
	
	@Select(" SELECT host_trans_ssn FROM tbl_bo_eticket WHERE host_trans_ssn in ('${hostTransSsn}')")
	List<String> findByHostTransSsn( @Param("hostTransSsn") String hostTransSsn);
	
	@Update(" update tbl_bo_eticket e  set  e.STATE=#{state}, e.un_through_reason=#{reason}, e.return_sign=#{reSign}, create_user=#{createUser},"
			+ "e.merc_id=(select d.mchnt_code_in from tbl_deal_total d where d.host_trans_ssn=e.host_trans_ssn)"
			+ "	 where e.host_trans_ssn in ('${hostTransSsn}') ")
	void updateReview(EticketReviewVo vo);
	
	@Select(" SELECT c.merc_no mercNo, c.agent_id agentId, c.merc_name mercName,c.merc_id mercId,c.address, c.mobile, c.inscode "
			+ "	 FROM tbl_bo_customer c LEFT JOIN tbl_deal_total t ON t.mchnt_code_in=c.merc_id 	WHERE t.host_trans_ssn=#{hostTransSsn} ")
	BoCustomer findCustomerByHostTransSsn(String hostTransSsn);
	
	@Update(" update tbl_bo_eticket set state=0, return_sign=2 where host_trans_ssn=#{hostTransSsn}")
	int updateResign(String hostTransSsn);
	
	@Update(" update tbl_bo_eticket set channel_pic=#{channelPic} where host_trans_ssn=#{hostTransSsn}")
	int updateChannelPic(@Param("hostTransSsn")String hostTransSsn, @Param("channelPic")String channelPic);
	
	@Select( " SELECT COUNT(1) FROM tbl_bo_eticket WHERE host_trans_ssn=#{hostTransSsn} ")
	int countByHostTransSsn(String hostTransSsn);
}

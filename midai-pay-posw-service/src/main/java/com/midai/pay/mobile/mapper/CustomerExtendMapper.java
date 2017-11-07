package com.midai.pay.mobile.mapper;

import org.apache.ibatis.annotations.Select;

import com.midai.pay.mobile.vo.CustomerExtendVo;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

public interface CustomerExtendMapper extends Mapper<CustomerExtendVo> ,MySqlMapper<CustomerExtendVo>{

	@Select( "select c.customer_level customerLevel, c.mer_type merType, c.merc_no mercNo, c.merc_id mercId, c.merc_name mercName, c.state, c.account_no accountNo, c.account_name accountName, c.branchbank_name branchbankName, c.opstate, c.agent_id agentId, c.mer_auto merAuto, '0' accountstate, "
			+ " 0 balance, '0' superstate, 0 totalableamt, 0 totalfrozenamt, '' inscode "
			+ "  from tbl_bo_customer c "
			+ " where c.mobile=#{mobile}" )
	CustomerExtendVo getCustomerExtendVoByMobile(String mobile);

}

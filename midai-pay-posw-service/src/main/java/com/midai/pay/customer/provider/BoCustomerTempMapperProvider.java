package com.midai.pay.customer.provider;

public class BoCustomerTempMapperProvider {
	
    /**
     * 代办事项
     */
    public String selectbymercId(String mercIds)
    {
   	 String sql = " SELECT '开户行修改申请' subject, merc_id mercId, merc_name mercName, 9 state, merc_no mercNo, create_time createTime "
   			 	+ " from tbl_bo_customer_temp where merc_no in ("+mercIds+") "
   			 	+ "	and state=1 and del_state=0  ";
   	 
   	 return sql;
    }
}

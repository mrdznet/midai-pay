package com.midai.pay.customer.provider;

import com.midai.pay.customer.query.BoCustomerLevelModeQuery;

public class BoCustomerLevelProvider {
	
	public String queryCount() {
		StringBuffer sql = new StringBuffer("SELECT count(1) FROM tbl_bo_customer_level");
		return sql.toString();
	}
	
	public String queryList(BoCustomerLevelModeQuery query) {
		StringBuffer sql = new StringBuffer("SELECT level, "
				+ " limit_trad_single limitTradSingle, "
				+ " limit_single_card_max limitSingleCardMax, limit_single_card_min limitSingleCardMin, limit_single_card_count limitSingleCardCount, "
				+ " limit_trad_day limitTradDay, limit_trad_day_count limitTradDayCount, "
				+ " limit_trad_month limitTradMonth, limit_trad_month_count limitTradMonthCount, "
				+ " note "
				+ " FROM tbl_bo_customer_level");
		
		if (query != null) {
			if (query.getPageSize() > 0) {
				sql.append(" limit " + (query.getPageNumber()-1)*query.getPageSize()+","+query.getPageSize());
			}
		}
		return sql.toString();
	}
	
}

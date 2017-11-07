package com.midai.pay.mobile.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import com.midai.pay.mobile.vo.HistroylogVo;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

public interface MobileHislogMapper extends Mapper<HistroylogVo> ,MySqlMapper<HistroylogVo>{

	@Select( " select b.trans_time transrecvdate, b.mchnt_code_in mchntcodein, a.merc_name mercname, b.host_trans_ssn hosttransssn, b.trans_code_name transcodename, b.trans_amt transamt "
			+ "  from  tbl_deal_total b left join tbl_bo_customer a on b.mchnt_code_in=a.merc_id "
			+ " where b.host_trans_ssn=#{logno} and b.trans_code='0200'")
	List<HistroylogVo> getMobileHislogByLogno(String logno);

	@SelectProvider(type=com.midai.pay.mobile.provider.MobileHislogProvider.class, method="searchHislog")
	List<HistroylogVo> searchHislog(Map<String, String> param);


}

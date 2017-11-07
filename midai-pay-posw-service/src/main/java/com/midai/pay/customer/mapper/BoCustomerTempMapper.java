package com.midai.pay.customer.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import com.midai.framework.config.mybatis.MyMapper;
import com.midai.pay.common.po.TasksInfo;
import com.midai.pay.customer.entity.BoCustomerTemp;

public interface BoCustomerTempMapper extends MyMapper<BoCustomerTemp> {

	String insert="merc_no,merc_id,merc_name,mobile,account_name,account_no,bank_id,branchbank_name,branchbank_id,pic_path,del_state,create_time,note,province_id,city_id";
	String insertProperty="#{mercNo},#{mercId},#{mercName},#{mobile},#{accountName},#{accountNo},#{bankId},#{branchBankName},#{branchBankId},#{picPath},#{delState},#{createTime},#{note},#{provinceId},#{cityId}";

	@Insert("insert into tbl_bo_customer_temp("+insert+") values("+insertProperty+")")
	int insertBoCustomerTemp(BoCustomerTemp boCustomerTemp);
	
	@Update(" update tbl_bo_customer_temp set state=#{state} where merc_no=#{mercNo} and del_state=0 ")
	int updateState(@Param("mercNo")String mercNo, @Param("state")int state);
	
	String select="merc_no mercNo,merc_id mercId,merc_name mercName,mobile mobile,account_name accountName,account_no accountNo,bank_id bankId,branchbank_name branchBankName,branchbank_id branchBankId,pic_path picPath,del_state delState,note note, province_id provinceId, city_id cityId  ";
	
	@Select("select "+select+"  from tbl_bo_customer_temp where merc_no=#{mercNo} and del_state=0")
	BoCustomerTemp selectBoCustomerTemp(String mercNo);
	
	@Update("update tbl_bo_customer_temp set del_state=1,review_result=#{reviewResult},advice=#{advice} where merc_no=#{mercNo}")
	int updateDelState(BoCustomerTemp boCustomerTemp);
	
	/**
	 * 待审进件列表使用
	 */
	@SelectProvider(type=com.midai.pay.customer.provider.BoCustomerTempMapperProvider.class,method="selectbymercId")
	List<TasksInfo> selectbymercId(String mercIds);
	
	@Select(" SELECT '开户行修改申请' subject, merc_id mercId, merc_name mercName, 9 state, merc_no mercNo, create_time createTime"
			+ "  from tbl_bo_customer_temp where merc_no=#{mercId} and state=1 and del_state=0 ")
	TasksInfo findByMercId(String mercId);
}

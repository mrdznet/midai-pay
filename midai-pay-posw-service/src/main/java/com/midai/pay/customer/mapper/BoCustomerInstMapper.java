package com.midai.pay.customer.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.midai.framework.config.mybatis.MyMapper;
import com.midai.pay.customer.entity.BoCustomerInst;

public interface BoCustomerInstMapper extends MyMapper<BoCustomerInst>{

	@Select("select merc_no mercNo,inst_code instCode from tbl_bo_customer_inst where merc_no=#{mercNo}")
	public List<BoCustomerInst> selectCustomerInst(String mercNo);
	
	@Insert("insert into tbl_bo_customer_inst(merc_no,inst_code,create_time) values(#{mercNo},#{instCode},now()) ")
	public int insertCustomerInst(@Param("mercNo")String mercNo,@Param("instCode")String instCode);
	
	@Delete("delete from tbl_bo_customer_inst where merc_no=#{mercNo}")
	public int deleteCustomerInst(String mercNo);
}
